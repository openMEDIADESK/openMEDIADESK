package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.core.database.sc.FavoriteService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Iterator;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

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
 * Date: 21.07.2008
 * Time: 15:58:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMediaPreviewController extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        int imageId = 0;
        Logger logger = Logger.getLogger(PopupMediaViewController.class);

        try {
            imageId = Integer.parseInt(httpServletRequest.getParameter("id"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendError(409,"Conflict: Keine MediaID angegeben, No MediaID given.");
            return null;
        }

        FavoriteService favoriteService = new FavoriteService();
        ShoppingCartService shoppingCartService = new ShoppingCartService();
        //---

        if (httpServletRequest.getParameter("lightbox")!=null) {

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user")!=null) {
                //user eingeloggt
                User user = (User)session.getAttribute("user");
                if (httpServletRequest.getParameter("lightbox").equals("add")) {
                    //hinzufügen
                    favoriteService.addMediaToFav(
                            Integer.parseInt((String)httpServletRequest.getParameter("id")), user.getUserId()
                    );
                } else {
                    //löschen
                    favoriteService.removeMediaFromFav(
                            Integer.parseInt((String)httpServletRequest.getParameter("id")), user.getUserId()
                    );
                }
            }
        }
        if (httpServletRequest.getParameter("shoppingCart")!=null) {

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user")!=null) {
                //user eingeloggt
                User user = (User)session.getAttribute("user");
                if (httpServletRequest.getParameter("shoppingCart").equals("add")) {
                    //hinzufügen
                    shoppingCartService.addImageToShoppingCart(
                            Integer.parseInt((String)httpServletRequest.getParameter("id")), user.getUserId()
                    );
                }
            }
        }

        //Map model = new HashMap();
        MediaService imageService = new MediaService();
        LngResolver lngResolver = new LngResolver();
        imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        MediaObject image = imageService.getMediaObjectById(
                Integer.parseInt(httpServletRequest.getParameter("id")));

        if (image==null) {
            httpServletResponse.sendError(404,"BasicMediaObject Not Available, Media nicht gefunden");
            return null;
        }

        //model.put("image",image);
        httpServletRequest.setAttribute("image",image);


        //---

        boolean showAddToLightbox = Config.useLightbox;
        boolean showAddToCart = Config.useShoppingCart;

        //cart + lightbox check:
        if (httpServletRequest.getSession().getAttribute("user")!=null) {
            User user = (User) httpServletRequest.getSession().getAttribute("user");

            List lightboxList = favoriteService.getMediaObjectList(user.getUserId());
            Iterator lightboxImages = lightboxList.iterator();
            while (lightboxImages.hasNext()) {
                MediaObject lightboxImage = (MediaObject) lightboxImages.next();
                if (lightboxImage.getIvid()==image.getIvid()) {
                    showAddToLightbox = false;
                }
            }

            List chartList = shoppingCartService.getShoppingCartImageList(user.getUserId());
            Iterator chartImages = chartList.iterator();
            while(chartImages.hasNext()) {
                MediaObject chartImage = (MediaObject) chartImages.next();
                if (chartImage.getIvid()==image.getIvid()) {
                    showAddToCart = false;
                }
            }
        }

        this.setPrevAndNextImage(httpServletRequest, image);

        /*
        model.put("showAddToLightbox",new Integer(showAddToLightbox));
        model.put("showAddToCart",new Integer(showAddToCart));*/

        if (image.getMayorMime().equalsIgnoreCase("image") || image.getMimeType().equalsIgnoreCase("application/pdf")) {
            httpServletRequest.setAttribute("hasImage",new Boolean(true));
        } else {
            httpServletRequest.setAttribute("hasImage",new Boolean(false));
        }

        if (image.getMayorMime().equalsIgnoreCase("video")) {
            System.out.println("videostream: ");
            httpServletRequest.setAttribute("hasVideo",new Boolean(true));
            String videoStreamUrl = "/stream/object/"+image.getIvid()+"."+image.getExtention();
            httpServletRequest.setAttribute("streamUrl",videoStreamUrl);
        } else {
            httpServletRequest.setAttribute("hasVideo",new Boolean(false));
        }

        if (image.getMayorMime().equalsIgnoreCase("audio")) {
            System.out.println("audiostream: ");
            httpServletRequest.setAttribute("hasAudio",new Boolean(true));
            String videoStreamUrl = "/stream/object/"+image.getIvid()+"."+image.getExtention();
            httpServletRequest.setAttribute("streamUrl",videoStreamUrl);
        } else {
            httpServletRequest.setAttribute("hasAudio",new Boolean(false));
        }

        httpServletRequest.setAttribute("showShoppingCart",new Boolean(showAddToCart));
        httpServletRequest.setAttribute("showLightbox",new Boolean(showAddToLightbox));
        httpServletRequest.setAttribute("showQuickDownload",new Boolean(Config.quickDownload));
        httpServletRequest.setAttribute("showSendImage",new Boolean(Config.showSendImage));

        if (Config.showDownloadToVisitors && Config.quickDownload) {
            httpServletRequest.setAttribute("showDownload", new Boolean(true));
            httpServletRequest.setAttribute("showImageActionMenu", new Boolean(true));
        } else {
            if (isLoggedIn(httpServletRequest)) {
                httpServletRequest.setAttribute("showImageActionMenu", new Boolean(true));
                if (Config.quickDownload) {
                    httpServletRequest.setAttribute("showDownload", new Boolean(true));
                } else {
                    httpServletRequest.setAttribute("showDownload", new Boolean(false));
                }
            } else {
                httpServletRequest.setAttribute("showImageActionMenu", new Boolean(false));
                httpServletRequest.setAttribute("showDownload", new Boolean(false));
            }
        }

        httpServletRequest.setAttribute("pageNavTop",new Boolean(Config.popupIvidPageNavTop));
        httpServletRequest.setAttribute("pageNavBottom",new Boolean(Config.popupIvidPageNavBottom));

        //View loggen
            DownloadLoggerService dlls2 = new DownloadLoggerService();
            dlls2.log(getUser(httpServletRequest).getUserId(),imageId, null,
                    SimpleDownloadLogger.DTYPE_VIEW, httpServletRequest, 0);

        //Einstellung: Leere Felder Ausblenden
        httpServletRequest.setAttribute("blankWhenFieldEmpty",Config.blankWhenFieldEmpty);

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);
    }

    protected void setPrevAndNextImage(HttpServletRequest httpServletRequest, MediaObject image) {

        //Checken ob es eine BasicMediaObject-List gibt und ob WEITER-> oder <- ZURÜCK-Buttons angezeigt werden sollen
        if (httpServletRequest.getSession().getAttribute("allImageList")!=null) {
            List allImageList = (List)httpServletRequest.getSession().getAttribute("allImageList");


            MediaObject next = this.getNextImage(allImageList,image);
            MediaObject prev = this.getPrevImage(allImageList,image);

            if (next!=null) { httpServletRequest.setAttribute("nextImageId",new Integer(next.getIvid())); }
            if (prev!=null) { httpServletRequest.setAttribute("prevImageId",new Integer(prev.getIvid())); }
        } else {
            logger.debug("allImageList = NULL, Popup wurde nicht von einem Thumbnail-View aufgerufen");
        }

    }
    private MediaObject getNextImage(List imageList, MediaObject actualImage) {

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            Object nextObject = images.next();
            if (nextObject instanceof MediaObject) {
                MediaObject image = (MediaObject)nextObject;
                if (image.getIvid()==actualImage.getIvid()) {
                    //Aktuelles Bild

                    //NEXT:
                    //int index = imageList.indexOf(image);
                    if (images.hasNext()) {
                        //es gibt ein nächstes
                        MediaObject nextImage = (MediaObject)images.next();
                        return nextImage;
                    }
                }
            }
        }

        return null;
    }

    private MediaObject getPrevImage(List imageList, MediaObject actualImage) {

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject image = (MediaObject)images.next();
            if (image.getIvid()==actualImage.getIvid()) {
                //Aktuelles Bild

                //PREV:
                int index = imageList.indexOf(image);
                if (index>0) {
                    //es gibt vorheriges
                    return (MediaObject)imageList.get(index-1);
                }
            }
        }

        return null;
    }

}
