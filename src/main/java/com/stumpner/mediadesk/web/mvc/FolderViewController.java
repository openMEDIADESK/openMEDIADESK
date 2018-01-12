package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;

import java.util.List;

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
 * Date: 02.05.2005
 * Time: 22:42:49
 * To change this template use File | Settings | File Templates.
 */
public class FolderViewController extends AbstractThumbnailAjaxController {
    protected MediaMenu mediaMenuBaker(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MediaMenu mediaMenu = getMediaMenu(request);
        if (getUser(request).getRole()>=User.ROLE_EDITOR) {
            //mediaMenu.setDelete(true);
            //mediaMenu.setDeleteFromDB(true);
            //mediaMenu.setDeleteFromFolder(true);

            mediaMenu.setSelectionAsCatImage(true);
            mediaMenu.setSelectionCopy(true);
            mediaMenu.setSelectionMove(true);

            if (Config.pinPicEnabled) { mediaMenu.setSelectionToPin(true); }
        }
        mediaMenu.setSelection(true);
        mediaMenu.setSelectionMarkAll(true);
        mediaMenu.setSelectionMarkSite(true);
        mediaMenu.setSelectionUnmarkAll(true);

        if (Config.quickDownload) mediaMenu.setDownloadSelected(true);

        return mediaMenu;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        /** Deprecated: seit 3.3 gibt es keine Folder mehr:
         *
         */

        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("cat"));
        return null;

        /*

        int showMessage = 0;
        String message = "";
        FolderService folderService = new FolderService();
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        Folder folder = new Folder();

        try {
            folder = folderService.getFolderById(Integer.parseInt(httpServletRequest.getParameter("id")));

            if (folder==null) {
                //kein folder vorhanden: Page Not Found
                httpServletResponse.setStatus(404);
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            httpServletResponse.setStatus(404);
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            httpServletResponse.setStatus(404);
            return null;            
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            httpServletResponse.setStatus(404);
            return null;
        }

        //set primary-ivid from folder:
        if (httpServletRequest.getParameter("setIvid")!=null) {

            if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                List imageList = (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                Iterator images = imageList.iterator();
                if (images.hasNext()) {
                    ImageVersion image = (ImageVersion) images.next();
                    folder.setPrimaryIvid(image.getIvid());
                    folderService.save(folder);
                }
                httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                message = "folderview.action.ividset";
            } else {
                message = "folderview.action.notselected";
            }
            showMessage = 1;
        }

        /* Save Breadcrumb */
        /*
        LinkedList breadCrumb = new LinkedList();
        breadCrumb.add(folder);
        this.setBreadCrumb(breadCrumb,httpServletRequest);

        httpServletRequest.setAttribute("folder",folder);
        httpServletRequest.setAttribute("showMessage",showMessage);
        if (!message.equalsIgnoreCase("")) { httpServletRequest.setAttribute("message",message); }
        httpServletRequest.setAttribute("showSorter",new Boolean(true));

        this.setContentTemplateFile("folderview.jsp",httpServletRequest);

        if (this.getUser(httpServletRequest).getRole()!=User.ROLE_ADMIN) {
            AclControllerContext aclCtx = AclContextFactory.getAclContext(httpServletRequest);
                if (!aclCtx.checkPermission(new AclPermission("read"),folder)) {
                    //Keine Berechtigung für diesn Event
                    this.denyByAcl(httpServletResponse);
                }
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
        */
    }

    protected void insert(ImageVersion image, HttpServletRequest request) throws DublicateEntry {

        FolderService folderService = new FolderService();
        folderService.addImageToFolder(getContainerId(request),image.getIvid());
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {

        FolderService folderService = new FolderService();
        folderService.deleteImageFromFolder(getContainerId(request),image.getIvid());
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "/index/folder";
    }

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

    /**
     * Bilder dieses Ordner als Liste (PaginatedList) laden
     * @param httpServletRequest
     * @param httpServletResponse
     * @return Liste der Bilder im Ordner
     */
    protected List loadThumbnailImageList(int sortBy, int orderBy, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        LngResolver lngResolver = new LngResolver();
        ImageVersionService imageService = new ImageVersionService();
        imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        
        //LoaderClass
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(getContainerId(httpServletRequest));
        loaderClass.setSortBy(sortBy);
        loaderClass.setOrderBy(orderBy);

        //todo: ersetzen in: Ohne PaginatedList:
        // getFolderImagesPages(loaderClass,getImageCountPerPage(),this.getPageIndex(httpServletRequest));
        /** ALTE LOGIK
        PaginatedList imageList = imageService.getFolderImagesPages(
                loaderClass,getImageCountPerPage());
         imageList.gotoPage(this.getPageIndex(httpServletRequest)-1);
         **/

    // Neue Logik
        List imageList = imageService.getFolderImagesPages(
                loaderClass,getImageCountPerPage(),this.getPageIndex(httpServletRequest));


        return imageList;
    }

    /**
     * Alle Bilder laden (nicht nur die von der aktuellen Seite)
     * @param httpServletRequest
     * @param httpServletResponse
     * @return Liste aller Bilder
     */
    protected List loadAllImageList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        ImageVersionService imageService = new ImageVersionService();
        //LoaderClass
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(getContainerId(httpServletRequest));
        loaderClass.setSortBy(getSortBy(httpServletRequest));
        loaderClass.setOrderBy(getOrderBy(httpServletRequest));

        return imageService.getFolderImages(loaderClass);

    }

    protected Object getContainerObject(HttpServletRequest request) {
        FolderService folderService = new FolderService();
        Folder folder = null;

        try {
            folder = folderService.getFolderById(getContainerId(request));
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return  folder;
    }

    protected int getDefaultSort(HttpServletRequest request) {
        Folder folder = (Folder)getContainerObject(request);
        if (folder.getSortBy()==0) {
            return Config.sortByFolder;
        } else {
            return folder.getSortBy();
        }
    }

    protected int getDefaultOrder(HttpServletRequest request) {
        Folder folder = (Folder)getContainerObject(request);
        if (folder.getOrderBy()==0) {        
            return Config.orderByFolder;
        } else {
            return folder.getOrderBy();
        }
    }

    protected String getDependendKey(HttpServletRequest request) {
        return super.getDependendKey(request)+"."+getContainerId(request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected String getListViewJsp(HttpServletRequest request) {
        return TEMPLATEFILE_THUMBNAILS;
    }
}
