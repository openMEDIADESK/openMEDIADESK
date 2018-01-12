package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;
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
public class CategoryIndexController extends AbstractThumbnailAjaxController {

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
                //System.out.println("CategoryIndexController: user is NOT logged in");
                WebHelper.processAutologin(request);
            }
        } else {
            if (Config.useAutoLogin) {
                //System.out.println("CategoryIndexController: user is logged in");
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

        Logger log = Logger.getLogger(CategoryIndexController.class);
        log.debug("handleRequestInternal from CategoryIndexController");
        User user = getUser(request);

        AclCategoryService categoryService = new AclCategoryService(request);
        LngResolver lngResolver = new LngResolver();
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));
        int id = 0;
        boolean showSorter = true;
        Category category = new Category();
        List parentCategoryList = new ArrayList();

        try {

            id = getCategoryId(request);

            if (id!=0) {
                category = categoryService.getCategoryById(id);
                request.setAttribute("category",category);
            } else {
                category.setCategoryId(0);
                category.setCatTitle("");
                category.setDescription("");
                category.setDefaultview(Config.categoryDefaultViewOnRoot);
                request.setAttribute("category",category);
            }


        } catch (NumberFormatException e) {
            //Keine Ziffer als Kategorie angegeben: 404 PAGE NOT FOUND
            log.error("HTTP_404 /index/cat?id= Kein Ordner als Ziffer angegeben");
            response.sendError(404);
            return null;
        } catch (ObjectNotFoundException e) {
            //Kategorie nicht vorhanden: 404 PAGE NOT FOUND
            log.error("HTTP_404 /index/cat?id="+id+" Ordner existiert nicht");
            response.sendError(404);
            return null;
        }

        if (Config.podcastEnabled) {
            request.setAttribute("podcastEnabled",new Boolean(true));
            request.setAttribute("podcastUrl","/rss/podcast/category/"+category.getCategoryId());
        } else {
            request.setAttribute("podcastEnabled",new Boolean(false));
        }

        //Für die Sharer Links
        request.setAttribute("sharerTitle", URLEncoder.encode(category.getCatTitle(),"UTF-8"));

        try {
            parentCategoryList = categoryService.getParentCategoryList(id);
        } catch (ObjectNotFoundException e) {
            //irgendeine parent-kategory wurde nicht gefunden
            //todo: wenn eine Kategorie aufgelöst wird, müssen auch die unterkategorien aufgelöst werden
            e.printStackTrace();
            System.err.println("getParentCategoryList liefert eine gelöschte Kategorie: "+e.getMessage());
            response.setStatus(404);
            return null;
        }
        //todo performance: nicht machen wenn nicht aktiviert in config (kein kategoriebaum)
        List categoryListTree = categoryService.getCategorySubTree(id,4);

        if (Config.categoryLatestOnRoot && id==0) {
            showSorter = false;
        }

        //ACL Überprüfen (nur wenn User kein Admin & nicht root Kategory!)
        if (this.getUser(request).getRole()!=User.ROLE_ADMIN) {

            if (id != 0) {
                AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
                if (!aclCtx.checkPermissionsOr(AclContextFactory.getViewAndReadPermission(),category)) {
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
                        category.setPrimaryIvid(image.getIvid());
                        categoryService.save(category);
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
        this.setBreadCrumb(parentCategoryList, request);

        /* Kategorien laden und in das Model speichern */
        request.setAttribute("parentCategoryList",parentCategoryList);
        request.setAttribute("folderPathArray",getFolderPathArray(parentCategoryList));
        request.setAttribute("folderId", category.getCategoryId());
        request.setAttribute("categoryList",categoryListTree);
        //todo: auslagern in AbstractThumbnailViewController
        request.setAttribute("showSorter",new Boolean(showSorter));
        request.setAttribute("showInsertUrl",new Boolean(isShowInsertUrl(request)));
        request.setAttribute("showRemoveUrl",new Boolean(isShowRemoveUrl(request)));

        request.setAttribute("webSiteTitle", category.getCatTitle());

        putOpenGraphDataAttributes(request);

        return super.handleRequestInternal(request, response);
    }

    private void putOpenGraphDataAttributes(HttpServletRequest request) {

        Map og = new HashMap();

        if (request.getAttribute("category")!=null) {
            Category c = (Category)request.getAttribute("category");
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

    private String getFolderPathArray(List parentCategoryList) {

        StringBuffer sb = new StringBuffer("[");
        for (int a=0;a<parentCategoryList.size();a++) {
            if (a>0) { sb = sb.append(","); }
            CategoryMultiLang folder = (CategoryMultiLang)parentCategoryList.get(a);
            sb = sb.append(folder.getCategoryId());
        }
        sb = sb.append("]");
        return sb.toString();
    }

    protected MediaMenu mediaMenuBaker(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MediaMenu mediaMenu = getMediaMenu(request);

        //if (getUser(request).getRole()>=User.ROLE_USER) {
            mediaMenu.setVisible(true);
        //} else {
        //    mediaMenu.setVisible(false);    //Nicht angemeldete Benutzer sehen kein Menü
        //}

        if (getUser(request).getRole()>=User.ROLE_USER) {
            mediaMenu.setSelection(true);
            mediaMenu.setSelectionMarkAll(true);
            mediaMenu.setSelectionMarkSite(true);
            mediaMenu.setSelectionUnmarkAll(true);

            mediaMenu.setDownloadSelected(true);

            if (Config.quickDownload) { mediaMenu.setDownloadSelected(true); }
        } else {
            //Öffentlich (nicht angemeldete Benutzer
        }

        mediaMenu.setView(true);
        mediaMenu.setShare(true);


        if (getUser(request).getRole()>=User.ROLE_EDITOR) {
            mediaMenu.setSelectionRemoveMedia(true);
            mediaMenu.setSelectionDeleteMedia(true);
            mediaMenu.setSelectionAsCatImage(true);

            mediaMenu.setSelectionCopy(true);
            mediaMenu.setSelectionMove(true);
        }

        if (getUser(request).getRole()>=User.ROLE_PINMAKLER) {
            mediaMenu.setSelectionToPin(true);
        }

        if (getUser(request).getRole()>=User.ROLE_EDITOR) {

            int catId = getCategoryId(request);
            if (catId==0 && Config.categoryLatestOnRoot) {
                //In der Root Kategorie wenn die aktuellsten Objekte angezeigt werden sollen, kein Upload zeigen
                request.setAttribute("canUploadContext",new Boolean(false)); //Wird in der alten GUI verwendet
                request.setAttribute("uploadEnabled",new Boolean(false)); //Wird in der neuen GUI verwendet
            } else {

                if (getUser(request).getRole()<User.ROLE_HOME_EDITOR) {
                //Lieferant, Redakteur von den ACL Settings abhängig

                    Category category = getCategory(request);
                    AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
                    boolean canUploadContext = aclCtx.checkPermission(new AclPermission("write"), category);

                    request.setAttribute("canUploadContext",new Boolean(canUploadContext)); //Wird in der alten GUI verwendet
                    request.setAttribute("uploadEnabled",new Boolean(canUploadContext));
                } else {
                    //Chef-Redakteur, Admin
                    request.setAttribute("canUploadContext",new Boolean(true)); //Wird in der alten GUI verwendet
                    request.setAttribute("uploadEnabled",new Boolean(true)); //Wird in der neuen GUI verwendet
                }
                mediaMenu.setAction(true);
                mediaMenu.setActionUpload(true);
            }
        }

        if (getUser(request).getRole()>=User.ROLE_HOME_EDITOR) {
            //Nur wenn es eine Subkategorie der Home-Kategorie ist, kann der Benutzer die Kategorie ändern
            CategoryService categoryService = new CategoryService();
            boolean isCanModifyCategory = categoryService.isCanModifyCategory(getUser(request),getCategoryId(request));

            if (isCanModifyCategory) {
                request.setAttribute("canModifyCategory",true);
                //Eigene Kategorien verändern/erstellen/löschen
                mediaMenu.setAction(true);
                mediaMenu.setActionEditCat(true);
                mediaMenu.setActionNewCat(true);
                mediaMenu.setActionDeleteCat(true);
            }
        }

        if (getUser(request).getRole()>=User.ROLE_MASTEREDITOR) {
            request.setAttribute("canModifyCategory",true);
            mediaMenu.setAction(true);
            mediaMenu.setActionEditCat(true);
            mediaMenu.setActionNewCat(true);
            mediaMenu.setActionDeleteCat(true);

            Category category = (Category)getContainerObject(request);
            if (category.getParent()==0) {
                mediaMenu.setActionIconCls("buttonAddCat");
            } else {
                mediaMenu.setActionIconCls("buttonEditCat");
            }
        }

        if (getUser(request).getRole()>=User.ROLE_MASTEREDITOR) {
            if (getCategoryId(request)==0 && Config.categoryLatestOnRoot) {
                mediaMenu.setAction(true);
                mediaMenu.setActionDeleteCat(false);
                mediaMenu.setSelectionCopy(false);
                mediaMenu.setSelectionMove(false);
            }

            if (getCategoryId(request)==0) { //Die Root Kategorie kann nicht bearbeitet werden
                mediaMenu.setActionEditCat(false);
                mediaMenu.setActionDeleteCat(false);
            }
        }

        return mediaMenu;

    }

    protected String getListViewCustom(HttpServletRequest request) {

        Category category = (Category)request.getAttribute("category");
        final int defaultView = category.getDefaultview();
        switch (defaultView) {
            case Category.VIEW_LIST:
                return TEMPLATEFILE_LISTVIEW;
            case Category.VIEW_THUMBNAILS:
                return TEMPLATEFILE_THUMBNAILS;
        }

        return null;

    }

    protected void insert(ImageVersion image, HttpServletRequest request) throws DublicateEntry {

        CategoryService categoryService = new CategoryService();
        categoryService.addImageToCategory(getContainerId(request),image.getIvid());
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {

        CategoryService categoryService = new CategoryService();
        categoryService.deleteImageFromCategory(getContainerId(request),image.getIvid());
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "c";
    }

    //todo:
    protected int getImageCount(HttpServletRequest request) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected boolean showSelect(HttpServletRequest request) {
        if (Config.quickDownload) {
            return this.getUser(request).getRole() >= User.ROLE_USER;
        } else {
            return this.getUser(request).getRole() > User.ROLE_EDITOR;
        }
    }

    protected int getContainerId(HttpServletRequest request) {
        return getCategoryId(request);
    }

    private List loadImageList(int sortBy, int orderBy, HttpServletRequest request, HttpServletResponse response) {

        LngResolver lngResolver = new LngResolver();
        ImageVersionService imageService = new ImageVersionService();
        imageService.setUsedLanguage(lngResolver.resolveLng(request));

        List imageList = null;
        //Loader-Class: definieren was geladen werden soll
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        if (sortBy!=-1) { loaderClass.setSortBy(sortBy); }
        if (sortBy!=-1) { loaderClass.setOrderBy(orderBy); }
        loaderClass.setId(getCategoryId(request));

        logger.debug("{loadThumbnailImageList} sortBy="+sortBy +", categoryId="+loaderClass.getId());


        if (getCategoryId(request)!=0) {
            //Wenn es eine Unterkategorie ist immer die Kategoriebilder zeigen
            //imageList = imageService.getCategoryImagesPages(loaderClass,filesPerPage);
            //if (Config.showSubCategoryInListView) {
                imageList = imageService.getCategoryImages(loaderClass);
            //} else {
                //todo: bei Performance-Problemen eventuell die alte Logik (unterhalb) wiederherstellen und über SQL Category+Images der Seite laden
            //    imageList = imageService.getCategoryImagesPage(loaderClass,getImageCountPerPage(),this.getPageIndex(request));
            //}
        } else {
            //Wenn es die Hauptkategorie ist, anhand den einstellungen prüfen ob
            //die Kategoriebilder gezeigt werden oder die Neuesten
            if (Config.categoryLatestOnRoot) {
                imageList = imageService.getLastImagesAcl(48,getUser(request));
            } else {
                imageList = imageService.getCategoryImages(loaderClass);
            }
        }

        /** Kategorie-Liste für den Thumbnail-View **/
        AclCategoryService categoryService = new AclCategoryService(request);
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));

        boolean showCategoriesInList = false;
        if (Config.showSubCategoryInListView) {
            if (Config.showSubCategoryInListViewOnlyWhenEmpty && imageList.size()==0) { showCategoriesInList = true; }
            if (!Config.showSubCategoryInListViewOnlyWhenEmpty) { showCategoriesInList = true; }

            if (showCategoriesInList) {

                List categoryList = new ArrayList();
                try {
                    categoryList = categoryService.getCategorySubTree(getCategoryId(request),0);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (IOServiceException e) {
                    e.printStackTrace();
                }

                //Kategorien zur gesamtliste hinzufügen
                imageList.addAll(0,categoryList);
            }
        }

        //Prüfung Ob der Yahoo-Verzeichnis-Anzeige der Kategorien angezeigt wird: WIRD ANGEZEIGT WENN
        //  - Wenn Kategorie-Tree deaktiviert und keine Kategorien in der Liste angezeigt werden.
        request.setAttribute("showCategoryDirectory",
                !Config.showCategoryTree && !showCategoriesInList ? true : false);

        return imageList;

    }

    /**
     * Bilder in dieser Kategorie laden und als Liste zurückgeben
     * @param httpServletRequest
     * @param httpServletResponse
     * @return List der Bilder in einer PaginatedList
     */
    protected List loadThumbnailImageList(int sortBy, int orderBy, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        List imageList = loadImageList(sortBy, orderBy, httpServletRequest, httpServletResponse);

        //Nur aktuelle Seite zurückgeben:
        int start = (getPageIndex(httpServletRequest)-1)*getImageCountPerPage();
        int end = getPageIndex(httpServletRequest) * getImageCountPerPage();
        if (end>=imageList.size()) { end = imageList.size(); }
        if (start>end) { return new LinkedList(); /* ungültige seitenanzahl */ }
        return imageList.subList(start, end);
    }


    protected List loadAllImageList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        return loadImageList(getSortBy(httpServletRequest),getOrderBy(httpServletRequest),httpServletRequest, httpServletResponse);
    } 

    private int getCategoryId(HttpServletRequest httpServletRequest) {

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

    public Category getCategory(HttpServletRequest request) {
        return (Category)getContainerObject(request);
    }

    protected Object getContainerObject(HttpServletRequest request) {

        CategoryService categoryService = new CategoryService();
        Category category = new Category();
        //Wenn es "nur" um die Root Kategorie geht, muss sie garnicht geladen werden
        if (getContainerId(request)!=0) {
            try {
                category = categoryService.getCategoryById(this.getContainerId(request));
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return super.getContainerObject(request);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return super.getContainerObject(request);
            }
        } else {
            //"Hauptkategorie = 0"
            category.setCategoryId(0);
        }
        return category;
    }

    private boolean isShowRemoveUrl(HttpServletRequest request) {
        return showActionUrls(request);
    }

    private boolean isShowInsertUrl(HttpServletRequest request) {
        return showActionUrls(request);
    }

    private boolean showActionUrls(HttpServletRequest request) {

        if (getCategoryId(request)!=0) {
            //Wenn es eine Unterkategorie ist immer die Kategoriebilder zeigen
            return true;
        } else {
            //Wenn es die Hauptkategorie ist, anhand den einstellungen prüfen ob
            //die Kategoriebilder gezeigt werden oder die Neuesten
            if (Config.categoryLatestOnRoot) {
                return false;
            } else {
                return true;
            }
        }

    }

    protected int getDefaultSort(HttpServletRequest request) {
        Category category = (Category)getContainerObject(request);
        if (category.getSortBy()==0) {
            return Config.sortByCategory;
        } else {
            return category.getSortBy();
        }
    }

    protected int getDefaultOrder(HttpServletRequest request) {

        Category category = (Category)getContainerObject(request);
        if (category.getOrderBy()==0) {
            return Config.orderByCategory;
        } else {
            return category.getOrderBy();
        }
    }
    
    protected String getDependendKey(HttpServletRequest request) {
        return super.getDependendKey(request)+"."+getContainerId(request);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
