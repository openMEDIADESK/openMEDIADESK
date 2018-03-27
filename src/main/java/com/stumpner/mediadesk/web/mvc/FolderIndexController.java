package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

import java.util.*;
import java.net.URLEncoder;

import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franzstumpner
 * Date: 05.10.2005
 * Time: 23:02:04
 * To change this template use File | Settings | File Templates.
 */
public class FolderIndexController extends AbstractThumbnailAjaxController {

    //private int filesPerPage = 12; //Bilder bzw. Dateien die Pro Seite angezeigt werden

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        UserService userService = new UserService();
        if (userService.processAutologin(request)) {
            System.out.println("autologin processed");
        }

        //Für mobile website: wenn der Benutzer nicht eingeloggt ist,...
        if (!Config.userEmailAsUsername) {
            //Benutzername als Benutzername
            request.setAttribute("usernameCaptionMessage","login.username");
        } else {
            //Emailadresse als Benutzername
            request.setAttribute("usernameCaptionMessage","register.email");
        }

        /* Session und Autologin prüfen **TESTING ** */
        HttpSession session = request.getSession();
        if (!isLoggedIn(request)) { // Benutzer ist noch nicht angemeldet
            //Autologin-Cookie prüfen und ggf. einloggen
            if (Config.onlyLoggedinUsers) {
                //System.out.println("Visitor-Access from jsessionid="+session.getId());
                session.setAttribute(LoginController.ATTRIBUTE_REDIRECT_AFTER_LOGIN,request.getRequestURI()+"?"+request.getQueryString());
                String redirectTo = response.encodeRedirectURL("login");
                //System.out.println("redirectTo="+redirectTo);
                response.sendRedirect(redirectTo);
                return null;
            }
            if (Config.useAutoLogin) {
                //System.out.println("FolderIndexController: user is NOT logged in");
                WebHelper.processAutologin(request);
            }
        } else {
            if (Config.useAutoLogin) {
                //System.out.println("FolderIndexController: user is logged in");
                //Benutzer ist angemeldet, prüfen ob das Autologin-Cookie gesetzt werden muss
                if (session.getAttribute("autologin")!=null) { // Benutzer ist ein NEUER Autologin-Benutzer
                    WebHelper.setAutologinCookie((String)session.getAttribute("username"),
                            (String)session.getAttribute("password"),
                            response);
                    session.removeAttribute("username");
                    session.removeAttribute("password");
                    session.removeAttribute("autologin");
                }
            }
        }

        /**
         * Konfigurations-Daten in den Request stellen:
         */

        //String sessionIdRequest = request.getSession().getId();
        //System.out.println("SessionID from Request: "+sessionIdRequest);
        /* Ende Session und Autologin prüfen     */

        Logger log = Logger.getLogger(FolderIndexController.class);
        log.debug("handleRequestInternal from FolderIndexController");
        User user = getUser(request);

        AclFolderService folderService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(request));
        int id = 0;
        boolean showSorter = true;
        Folder folder = new Folder();
        List parentFolderList = new ArrayList();

        try {

            id = getFolderId(request);

            if (id!=0) {
                folder = folderService.getFolderById(id);
                request.setAttribute("folder", folder);
            } else {
                folder.setCategoryId(0);
                folder.setCatTitle("");
                folder.setDescription("");
                folder.setDefaultview(Config.categoryDefaultViewOnRoot);
                request.setAttribute("folder", folder);
            }


        } catch (NumberFormatException e) {
            //Keine Ziffer als Kategorie angegeben: 404 PAGE NOT FOUND
            log.error("HTTP_404 /index/c?id= Kein Ordner als Ziffer angegeben");
            response.sendError(404);
            return null;
        } catch (ObjectNotFoundException e) {
            //Kategorie nicht vorhanden: 404 PAGE NOT FOUND
            log.error("HTTP_404 /index/c?id="+id+" Ordner existiert nicht");
            response.sendError(404);
            return null;
        }

        if (Config.podcastEnabled) {
            request.setAttribute("podcastEnabled",new Boolean(true));
            request.setAttribute("podcastUrl","/rss/podcast/folder/"+ folder.getCategoryId());
        } else {
            request.setAttribute("podcastEnabled",new Boolean(false));
        }

        //Für die Sharer Links
        request.setAttribute("sharerTitle", URLEncoder.encode(folder.getCatTitle(),"UTF-8"));

        try {
            parentFolderList = folderService.getParentFolderList(id);
        } catch (ObjectNotFoundException e) {
            //irgendeine parent-kategory wurde nicht gefunden
            //todo: wenn eine Kategorie aufgelöst wird, müssen auch die unterkategorien aufgelöst werden
            e.printStackTrace();
            System.err.println("getParentFolderList liefert einen gelöschten Ordner: "+e.getMessage());
            response.setStatus(404);
            return null;
        }
        //todo performance: nicht machen wenn nicht aktiviert in config (kein kategoriebaum)
        List folderListTree = folderService.getFolderSubTree(id,4);

        if (Config.categoryLatestOnRoot && id==0) {
            showSorter = false;
        }

        //ACL Überprüfen (nur wenn User kein Admin & nicht root Kategory!)
        if (this.getUser(request).getRole()!=User.ROLE_ADMIN) {

            if (id != 0) {
                AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
                if (!aclCtx.checkPermissionsOr(AclContextFactory.getViewAndReadPermission(), folder)) {
                    this.denyByAcl(response);
                }
            }
        }

        //set primary-ivid from folder:
        if (request.getParameter("setIvid")!=null) {

            if (request.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                List imageList = (List) request.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                Iterator images = imageList.iterator();
                if (images.hasNext()) {
                    ImageVersion image = (ImageVersion) images.next();
                    if (image.getMayorMime().equalsIgnoreCase("image")) {
                        folder.setPrimaryIvid(image.getIvid());
                        folderService.save(folder);
                        request.setAttribute("showInfoMessage","folderview.action.ividset");
                    } else {
                        request.setAttribute("showInfoMessage","folderview.action.notselected");
                    }
                }
                request.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                //message = "folderview.action.ividset";
            } else {
                request.setAttribute("showInfoMessage","folderview.action.notselected");
                //message = "folderview.action.notselected";
            }
            //showMessage = 1;
        }

        /* Save Breadcrumb */
        this.setBreadCrumb(parentFolderList, request);

        /* Kategorien laden und in das Model speichern */
        request.setAttribute("parentFolderList",parentFolderList);
        request.setAttribute("folderPathArray",getFolderPathArray(parentFolderList));
        request.setAttribute("folderId", folder.getCategoryId());
        request.setAttribute("folderList",folderListTree);
        //todo: auslagern in AbstractThumbnailViewController
        request.setAttribute("showSorter",new Boolean(showSorter));
        request.setAttribute("showInsertUrl",new Boolean(isShowInsertUrl(request)));
        request.setAttribute("showRemoveUrl",new Boolean(isShowRemoveUrl(request)));

        request.setAttribute("webSiteTitle", folder.getCatTitle());

        putOpenGraphDataAttributes(request);

        //Berechtigungen/Links/Menüs
        if (getUser(request).getRole()>=User.ROLE_EDITOR) {

            int catId = getFolderId(request);
            if (catId==0 && Config.categoryLatestOnRoot) {
                //In der Root Kategorie wenn die aktuellsten Objekte angezeigt werden sollen, kein Upload zeigen
                request.setAttribute("uploadEnabled",new Boolean(false)); //Wird in der neuen GUI verwendet
            } else {

                if (getUser(request).getRole()<User.ROLE_HOME_EDITOR) {
                    //Lieferant, Redakteur von den ACL Settings abhängig

                    AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
                    boolean canUploadContext = aclCtx.checkPermission(new AclPermission("write"), folder);

                    request.setAttribute("uploadEnabled",new Boolean(canUploadContext));
                } else {
                    //Chef-Redakteur, Admin
                    request.setAttribute("uploadEnabled",new Boolean(true)); //Wird in der neuen GUI verwendet
                }
            }
        }

        if (getUser(request).getRole()>=User.ROLE_HOME_EDITOR) {
            //Nur wenn es eine Subkategorie der Home-Kategorie ist, kann der Benutzer die Kategorie ändern
            boolean isCanModifyFolder = folderService.isCanModifyFolder(getUser(request), getFolderId(request));

            if (isCanModifyFolder) {
                request.setAttribute("canModifyFolder",true);
                //Eigene Kategorien verändern/erstellen/löschen
            }
        }

        if (getUser(request).getRole()>=User.ROLE_MASTEREDITOR) {
            request.setAttribute("canModifyFolder",true);

            //Folder folder = (Folder)getContainerObject(request);
            if (folder.getParent()==0) {
                //mediaMenu.setActionIconCls("buttonAddCat");
            } else {
                //mediaMenu.setActionIconCls("buttonEditCat");
            }
        }

        return super.handleRequestInternal(request, response);
    }

    private void putOpenGraphDataAttributes(HttpServletRequest request) {

        Map og = new HashMap();

        if (request.getAttribute("folder")!=null) {
            Folder c = (Folder)request.getAttribute("folder");
            og.put("url",WebHelper.getServerNameUrlPathWithQueryString(request));
            og.put("type","article");
            og.put("title",c.getCatTitle());
            og.put("description",c.getDescription());
            og.put("site_name",Config.webTitle);
            if (c.getPrimaryIvid()>0) {
                og.put("image",WebHelper.getServerNameUrl(request)+"/imageservlet/"+c.getPrimaryIvid()+"/1/Ordnerbild.jpg");
            }
        }

        request.setAttribute("og",og);
        //To change body of created methods use File | Settings | File Templates.
    }

    private String getFolderPathArray(List parentFolderList) {

        StringBuffer sb = new StringBuffer("[");
        for (int a=0;a<parentFolderList.size();a++) {
            if (a>0) { sb = sb.append(","); }
            FolderMultiLang folder = (FolderMultiLang)parentFolderList.get(a);
            sb = sb.append(folder.getCategoryId());
        }
        sb = sb.append("]");
        return sb.toString();
    }

    protected void insert(ImageVersion image, HttpServletRequest request) throws DublicateEntry {

        FolderService folderService = new FolderService();
        folderService.addMediaToFolder(getContainerId(request),image.getIvid());
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {

        FolderService folderService = new FolderService();
        folderService.deleteMediaFromFolder(getContainerId(request),image.getIvid());
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "c";
    }

    //todo:
    protected int getImageCount(HttpServletRequest request) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected int getContainerId(HttpServletRequest request) {
        return getFolderId(request);
    }


    private int getFolderId(HttpServletRequest httpServletRequest) {

        int id = 0;
            if (httpServletRequest.getParameter("id")!=null)
                id = Integer.parseInt(httpServletRequest.getParameter("id"));
        
        if (Config.homeCategoryId!=-1) {
            User user = getUser(httpServletRequest);
            if (id==0 && user.getHomeCategoryId()!=-1) {
                id = getUser(httpServletRequest).getHomeCategoryId();
            }
        }

        return id;
    }

    public Folder getFolder(HttpServletRequest request) {
        return (Folder)getContainerObject(request);
    }

    protected Object getContainerObject(HttpServletRequest request) {

        FolderService folderService = new FolderService();
        Folder folder = new Folder();
        //Wenn es "nur" um die Root Kategorie geht, muss sie garnicht geladen werden
        if (getContainerId(request)!=0) {
            try {
                folder = folderService.getFolderById(this.getContainerId(request));
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return super.getContainerObject(request);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return super.getContainerObject(request);
            }
        } else {
            //"Hauptordner = 0"
            folder.setCategoryId(0);
        }
        return folder;
    }

    private boolean isShowRemoveUrl(HttpServletRequest request) {
        return showActionUrls(request);
    }

    private boolean isShowInsertUrl(HttpServletRequest request) {
        return showActionUrls(request);
    }

    private boolean showActionUrls(HttpServletRequest request) {

        if (getFolderId(request)!=0) {
            //Wenn es ein Unterorder ist immer die Ordnerthumbnail zeigen
            return true;
        } else {
            //Wenn es die Root-Ordner ist, anhand den einstellungen prüfen ob
            //die Ordnerbilder gezeigt werden oder die Neuesten
            if (Config.categoryLatestOnRoot) {
                return false;
            } else {
                return true;
            }
        }

    }

    protected int getDefaultSort(HttpServletRequest request) {
        Folder folder = (Folder)getContainerObject(request);
        if (folder.getSortBy()==0) {
            return Config.sortByCategory;
        } else {
            return folder.getSortBy();
        }
    }

    protected int getDefaultOrder(HttpServletRequest request) {

        Folder folder = (Folder)getContainerObject(request);
        if (folder.getOrderBy()==0) {
            return Config.orderByCategory;
        } else {
            return folder.getOrderBy();
        }
    }
    
    protected String getDependendKey(HttpServletRequest request) {
        return super.getDependendKey(request)+"."+getContainerId(request);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
