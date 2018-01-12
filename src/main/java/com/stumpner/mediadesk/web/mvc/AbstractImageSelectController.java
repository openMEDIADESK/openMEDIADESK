package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.web.mvc.exceptions.LoadThumbnailException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;
import com.stumpner.mediadesk.core.service.MediaObjectService;

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
 * User: franz.stumpner
 * Date: 05.01.2007
 * Time: 10:50:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageSelectController extends AbstractImageLoaderController {


    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Logger logger = Logger.getLogger(AbstractImageSelectController.class);
        //check for action
        if (httpServletRequest.getParameter("select")!=null) {
            //Aktuelles Bild auswählen
            logger.debug("AbstractImageSelectController: calling selectImage(...)");
            this.selectImage(Integer.parseInt(httpServletRequest.getParameter("select")),httpServletRequest);
        }
        if (httpServletRequest.getParameter("deselect")!=null) {
            //Aktuelles Bild abwählen
            logger.debug("AbstractImageSelectController: calling deselectMedia(...)");
            this.deselectImage(Integer.parseInt(httpServletRequest.getParameter("deselect")),httpServletRequest);
        }
        if (httpServletRequest.getParameter("mark")!=null) {
            String action = httpServletRequest.getParameter("mark");
            if (action.equalsIgnoreCase("all")) {
                //Alle markieren
                logger.debug("AbstractImageSelectController: calling markAll(...)");
                this.markAll(httpServletRequest,httpServletResponse);
            }
            if (action.equalsIgnoreCase("site")) {
                this.markSite(httpServletRequest,httpServletResponse);
            }
        }
        if (httpServletRequest.getParameter("unmark")!=null) {
            //Alle abwählen (alle!)
            logger.debug("AbstractImageSelectController: calling unmarkAll(...)");
            this.unmarkAll(httpServletRequest,httpServletResponse);
        }

        //Anzahl der selektierten (markierten) Bilder in den Request schreiben
        List imageList = this.getSelectedImageList(httpServletRequest.getSession());
        httpServletRequest.setAttribute("selectedImages",new Integer(imageList.size()));

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void selectImage(int ivid, HttpServletRequest request) {

        Logger logger = Logger.getLogger(AbstractImageSelectController.class);

        Object container = getContainerObject(request);
        Integer categoryId = null;
        if (container!=null) {
            categoryId = ((Category)container).getCategoryId();
        }
        MediaObjectService.selectMedia(ivid, categoryId, request);
    }

    public static void deselectImage(int ivid, HttpServletRequest request) {

        MediaObjectService.deselectMedia(ivid, request);
    }

    /**
     * Bilder der aktuellen Seite auswählen
     * @param request
     * @param response
     */
    private void markSite(HttpServletRequest request, HttpServletResponse response) throws LoadThumbnailException {

        List imageList = this.loadThumbnailImageList(
                getSortBy(request),
                getOrderBy(request),
                request,response
                );

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            Object siteObject = images.next();
            if (siteObject instanceof ImageVersion) {
                ImageVersion image = (ImageVersion)siteObject;
                this.selectImage(image.getIvid(),request);
            }
        }

    }

    /**
     * Alle Bilder markieren, muss vom ImageLoader Controller gemacht werden, weil
     * nur der ja die Bilder dieser ansicht lädt.
     * Vorher kann niemand sagen welche Bilder markiert werden sollen
     */
    protected void markAll(HttpServletRequest request,HttpServletResponse response) {
        /*
        List imageList = this.loadThumbnailImageList(request,response);
        */
        List imageList = null;
        try {
            List allImageList = this.loadAllImageList(request,response);
            if (allImageList.size()>1000) {
                //Liste kürzen
                imageList = allImageList.subList(0,999);
            } else {
                //volle liste
                imageList = allImageList;
            }
            Iterator images = imageList.iterator();
            boolean select = true;

            while (images.hasNext()) {
                Object siteObject = images.next();
                if (siteObject instanceof ImageVersion) {
                    ImageVersion image = (ImageVersion)siteObject;
                    if (select)
                    {   this.selectImage(image.getIvid(),request); }
                    else
                    {   this.deselectImage(image.getIvid(),request); }
                }
            }
        } catch (LoadThumbnailException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * ALLE Bilder abwählen
     * @param request
     * @param response
     */
    protected void unmarkAll(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM);
        request.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
    }

    /**
     * Liste mit ausgewählten Images zurückgeben
     * @param session
     * @return Liste von <ImageVersion> Bildern die ausgewählt sind.
     */
    public static List getSelectedImageList(HttpSession session) {

        return MediaObjectService.getSelectedImageList(session);

    }

    /**
     * Gibt das Object (folder,category) zurück, der aktuellen Ansicht
     * @return gibt <null> zurück wenn es kein Object gibt, ansonsten das Object (folder, category)
     */
    protected Object getContainerObject(HttpServletRequest request) {
        return null;
    }



}
