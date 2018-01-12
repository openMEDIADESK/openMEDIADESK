package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.image.inbox.InboxService;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.usermanagement.User;

import java.util.List;
import java.util.LinkedList;

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
 * Date: 27.04.2005
 * Time: 21:27:43
 * To change this template use File | Settings | File Templates.
 */
public class InboxViewController extends AbstractPageController {

    public InboxViewController() {
        //only logged in persons are permitted
        permitOnlyLoggedIn=true;
        permitMinimumRole = User.ROLE_IMPORTER;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        InboxService inboxService = new InboxService();
        LngResolver lngResolver = new LngResolver();
        //select action
        if (httpServletRequest.getParameter("delete")!=null) {
            //löschen
            if (httpServletRequest.getParameterValues("ivid")!=null) {
                this.removeImagesFromInbox(httpServletRequest.getParameterValues("ivid"));
                this.deleteImages(httpServletRequest.getParameterValues("ivid"));
            }
        } else {
            //add image to folder...
            if (httpServletRequest.getParameter("folderId")!=null && httpServletRequest.getParameter("ivid")!=null) {
                //zu folder...
                if (!httpServletRequest.getParameter("folderId").equals("0")) {
                    this.addImagesToFolder(
                            httpServletRequest.getParameter("folderId"),
                            httpServletRequest.getParameterValues("ivid"));
                }
            }
            if (httpServletRequest.getParameter("categoryId")!=null && httpServletRequest.getParameter("ivid")!=null) {
                if (!httpServletRequest.getParameter("categoryId").equals("0")) {
                    this.addImagesToCategory(
                            httpServletRequest.getParameter("categoryId"),
                            httpServletRequest.getParameterValues("ivid"));
                }
            }
            if (httpServletRequest.getParameter("selectedToPin")!=null) {
                List imageList = this.getSelectedImageList(httpServletRequest);
                if (imageList!=null) {
                    httpServletRequest.getSession().setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,imageList);
                    httpServletResponse.sendRedirect("pinwizard");
                    return null;
                } else {
                    //todo: Meldungen ausgeben wenn keine Bilder ausgewählt
                }
            }
            //Popup-Cache-List entfernen:
            httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST);
            httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST_CACHER);
        }

        ImageVersionService imageService = new ImageVersionService();
        imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        inboxService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        
        List inbox = inboxService.getInbox();
        httpServletRequest.setAttribute("imageList",inbox);
        httpServletRequest.setAttribute("inboxSize",new Integer(inbox.size()));
        httpServletRequest.setAttribute("imageCount",new Integer(imageService.getImageCount()));

        //es werden nur die ersten 50 folder angezeigt...
        FolderService folderService = new FolderService();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        CategoryService categoryService = new CategoryService();
        categoryService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        httpServletRequest.setAttribute("folderList",folderService.getFolderList(50));
        //model.put("catList",categoryService.getCategoryList(0));
        httpServletRequest.setAttribute("catList",categoryService.getAllCategoryListSuborder());

        this.setContentTemplateFile("inbox.jsp",httpServletRequest);

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void addImagesToFolder(String sfolderId, String[] sivids) {

        int folderId = Integer.parseInt(sfolderId);
        FolderService folderService = new FolderService();
        InboxService inboxService = new InboxService();
        int lastImageIvid = 0;

        for (int p=0;p<sivids.length;p++) {
            int ivid = Integer.parseInt(sivids[p]);
            try {
                folderService.addImageToFolder(folderId,ivid);
                inboxService.removeImage(ivid);
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            lastImageIvid = ivid;
        }

        try {
            Folder folder = folderService.getFolderById(folderId);
            folder.setPrimaryIvid(lastImageIvid);
            folderService.save(folder);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private List getSelectedImageList(HttpServletRequest httpServletRequest) {

        List imageList = new LinkedList();
        String[] sivids = httpServletRequest.getParameterValues("ivid");
        if (sivids!=null) {
            ImageVersionService imageService = new ImageVersionService();

            for (int p=0;p<sivids.length;p++) {
                int ivid = Integer.parseInt(sivids[p]);
                ImageVersion image = imageService.getImageVersionById(ivid);
                imageList.add(image);
            }
            return imageList;

        } else {
            //todo: mit Exception handling machen!
            return null;
        }

    }

    private void addImagesToCategory(String sfolderId, String[] sivids) {

        int folderId = Integer.parseInt(sfolderId);
        CategoryService folderService = new CategoryService();
        InboxService inboxService = new InboxService();
        int lastImageIvid = 0;

        for (int p=0;p<sivids.length;p++) {
            int ivid = Integer.parseInt(sivids[p]);
            try {
                folderService.addImageToCategory(folderId,ivid);
                inboxService.removeImage(ivid);
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            lastImageIvid = ivid;
        }

    }

    private void removeImagesFromInbox(String[] ivids) {

        InboxService inboxService = new InboxService();

        for (int p=0;p<ivids.length;p++) {
            int ivid = Integer.parseInt(ivids[p]);
            inboxService.removeImage(ivid);
        }

    }

    private void deleteImages(String[] ivids) {

        ImageVersionService imageService = new ImageVersionService();

        for (int p=0;p<ivids.length;p++) {
            int ivid = Integer.parseInt(ivids[p]);
            ImageVersion image = imageService.getImageVersionById(ivid);
            try {
                imageService.deleteImageVersion(image);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }



}
