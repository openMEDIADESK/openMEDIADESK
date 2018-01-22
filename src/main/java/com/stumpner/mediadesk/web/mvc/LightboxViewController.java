package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.LightboxService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;

import java.util.List;

import com.ibatis.common.util.PaginatedList;

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
 * Date: 16.05.2005
 * Time: 23:15:09
 * To change this template use File | Settings | File Templates.
 */
public class LightboxViewController extends AbstractThumbnailAjaxController {

    public LightboxViewController() {

        this.permitOnlyLoggedIn=true;            

    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        LightboxService lightboxService = new LightboxService();
        ImageVersionService imageService = new ImageVersionService();
        Folder folder = new Folder();
        folder.setFolderTitle("Lightbox");
        User user = getUser(httpServletRequest);

        if (httpServletRequest.getParameter("remove")!=null) {

            if (httpServletRequest.getParameter("remove").equalsIgnoreCase("selectedMedia")) {
                if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                    lightboxService.removeImagesToLightbox(
                            (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES),
                            user.getUserId());
                    httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                }
            } else {
                //remove all images
                lightboxService.removeImagesToLightbox(
                        lightboxService.getLightboxImageList(user.getUserId()),
                        user.getUserId()
                );
            }

        }
        if (httpServletRequest.getParameter("addToCart")!=null) {
            //todo: add to cart service
            ShoppingCartService shoppingCartService = new ShoppingCartService();

            if (httpServletRequest.getParameter("addToCart").equalsIgnoreCase("selectedMedia")) {

                if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                    shoppingCartService.addImagesToShoppingCart(
                            (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES),
                            user.getUserId());
                    lightboxService.removeImagesToLightbox((List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES),
                            user.getUserId());
                    httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                }
            } else {
                //alle
                shoppingCartService.addImagesToShoppingCart(
                        lightboxService.getLightboxImageList(user.getUserId()),
                        user.getUserId());
                lightboxService.removeImagesToLightbox(
                        lightboxService.getLightboxImageList(user.getUserId()),
                        user.getUserId());
            }
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void insert(ImageVersion image, HttpServletRequest request) {
        /* Keine Bedeutung in der Lightbox */
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {
        /* Keine Bedeutung in der Lightbox, wird von handleRequestInternal behandelt */
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "f";
    }

    protected int getImageCount(HttpServletRequest request) {
        LightboxService lightboxService = new LightboxService();
        return lightboxService.getLightboxUserCount(getUser(request).getUserId());
    }

}
