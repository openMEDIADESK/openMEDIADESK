package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;
import com.stumpner.mediadesk.core.service.MediaObjectService;

import java.util.List;
import java.math.BigDecimal;
import java.io.IOException;

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
 * Date: 17.05.2005
 * Time: 21:57:50
 * To change this template use File | Settings | File Templates.
 */
public class ShoppingCartViewController extends AbstractThumbnailAjaxController {

    public ShoppingCartViewController() {

        this.permitOnlyLoggedIn=true;

    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (!this.isLoggedIn(httpServletRequest)) {
            //Redirected auf Message, dass nur eingeloggte User sich Bilder downloaden können
            httpServletRequest.getSession().setAttribute(LoginController.ATTRIBUTE_REDIRECT_AFTER_LOGIN,
                    httpServletRequest.getRequestURI()+"?"+httpServletRequest.getQueryString()
                    );
            httpServletResponse.sendRedirect("/login?loginrequired=true");
            return null;
        }

        if (!Config.useShoppingCart) {
            //Zum Download redirecten
            checkRedirectToDownload(httpServletRequest, httpServletResponse);
            return null;
        }

        //WebStack webStack = new WebStack(httpServletRequest);
        //webStack.push();

        model.remove("formatSelector");
        model.remove("useFormatSelector");

        LngResolver lngResolver = new LngResolver();
        ShoppingCartService shoppingCartService = new ShoppingCartService();
        shoppingCartService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        ImageVersionService imageService = new ImageVersionService();
        imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        Folder folder = new Folder();
        folder.setFolderTitle("Shopping Cart");

        //Settings/Config
        httpServletRequest.setAttribute("currency", Config.currency);
        if (Config.currency.isEmpty()) {
            httpServletRequest.setAttribute("showPrice",false);
        } else {
            httpServletRequest.setAttribute("showPrice",true);
        }

        if (Config.creditSystemEnabled) {
            //todo: bei download selected alle ausgewählten in den ShoppingCart
            checkAndHandleDownloadSelected(shoppingCartService, httpServletRequest);

            //Guthabensystem aktiv
            ShoppingCartService.Checkout checkout = shoppingCartService.createCheckoutInfo(httpServletRequest);
            httpServletRequest.setAttribute("credits",checkout.user.getCredits());
            httpServletRequest.setAttribute("subtotalbeforedeposit",checkout.subtotalbeforedeposit);
            httpServletRequest.setAttribute("subtotal",checkout.subtotal);

            httpServletRequest.setAttribute("showPriceCreditInfo",true);

            if (checkout.subtotal.compareTo(BigDecimal.valueOf(0))!=0) {
                //Credits (Guthaben) reichen nicht aus


                if (getImageCount(httpServletRequest)>0) {
                    //Folgende Möglichkeiten
                    if (!Config.currency.isEmpty() && Config.paymillKeyPrivate.length()>0) {
                        //Wenn eine Währung verwendet wird (+Bezahlmodul aktiv), dann Kreditkartenbezahlung
                        httpServletRequest.setAttribute("needCheckout",true);
                        httpServletRequest.setAttribute("cartDownload",false);
                        if (checkout.user.getCredits().compareTo(BigDecimal.valueOf(0))>0) {
                            //Wenn Guthaben existiert, dann anzeigen
                            httpServletRequest.setAttribute("showBalanceInfo",true);
                        } else {
                            //Wenn kein Guthaben existiert, nicht anzeigen
                            httpServletRequest.setAttribute("showBalanceInfo",false);
                        }
                    } else {
                        //Wenn keine Währung verwendet wird, dann kein Download möglich
                        httpServletRequest.setAttribute("needCheckout",false);
                        httpServletRequest.setAttribute("notEnoughtCredits",true);
                        httpServletRequest.setAttribute("cartDownload",false);
                        httpServletRequest.setAttribute("showBalanceInfo",true);
                    }
                }

            } else {
                //Guthaben reicht aus: Zum Download anzeigen
                httpServletRequest.setAttribute("showBalanceInfo",true);
                if (getImageCount(httpServletRequest)>0) {
                    cartDownload(httpServletRequest, httpServletResponse, shoppingCartService, checkout);
                }
            }
        } else {

            //Guthabensystem ausgeschaltet --> Download immer möglich
            httpServletRequest.setAttribute("showPriceCreditInfo",false);
            if (getImageCount(httpServletRequest)>0) {
                cartDownload(httpServletRequest, httpServletResponse, shoppingCartService, null);
            }
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void cartDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ShoppingCartService shoppingCartService, ShoppingCartService.Checkout checkout) throws IOException {

        httpServletRequest.setAttribute("needCheckout",false);
        httpServletRequest.setAttribute("cartDownload",true);

        if (checkout!=null) {
            shoppingCartService.setPayTransactionId(checkout.user.getUserId(), "mdcredits-"+checkout.user.getCredits()+"-"+checkout.subtotalbeforedeposit);
        } else {
            shoppingCartService.setPayTransactionId(getUser(httpServletRequest).getUserId(), "credits-disabled");
        }

        //Zum Download redirecten
        checkRedirectToDownload(httpServletRequest, httpServletResponse);
    }

    /**
     * Zum Download redirecten wenn der Parameter gesetzt ist
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    private void checkRedirectToDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        if (httpServletRequest.getParameter("download")!=null) {
            //webStack.pop();
            httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("download?ref=/index/shoppingcart&"+httpServletRequest.getQueryString())
            );
        }

    }

    private void checkAndHandleDownloadSelected(ShoppingCartService shoppingCartService, HttpServletRequest httpServletRequest) {

        if (httpServletRequest.getParameter("download")!=null) {
            if (httpServletRequest.getParameter("download").equalsIgnoreCase("selectedMedia")) {
                List<ImageVersion> selectedMediaList = MediaObjectService.getSelectedImageList(httpServletRequest.getSession());
                for (ImageVersion m : selectedMediaList) {
                    shoppingCartService.addImageToShoppingCart(m.getIvid(), getUser(httpServletRequest).getUserId());
                }
            }
        }

    }

    protected MediaMenu mediaMenuBaker(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MediaMenu mediaMenu = getMediaMenu(request);

        mediaMenu.setVisible(true);

        mediaMenu.setSelection(false);
        mediaMenu.setSelectionMarkAll(false);
        mediaMenu.setSelectionMarkSite(false);
        mediaMenu.setSelectionUnmarkAll(false);

        mediaMenu.setDownloadSelected(false);

        mediaMenu.setSelectionRemoveMedia(false);

        /*
        mediaMenu.setDeleteAll(true);
        mediaMenu.setDeleteFromLightbox(true);
        mediaMenu.setSelection(true);
        mediaMenu.setSelectionMarkAll(true);
        mediaMenu.setSelectionUnmarkAll(true);
        mediaMenu.setSelectionToShoppingcart(true);
        mediaMenu.setAllToShoppingcart(true);
        mediaMenu.setDownloadSelected(true);
        */

        return mediaMenu;
    }

    protected void insert(ImageVersion image, HttpServletRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {
        ShoppingCartService shoppingCartService = new ShoppingCartService();
        shoppingCartService.removeImageToShoppingCart(image.getIvid(), WebHelper.getUser(request).getUserId());
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "shop";
    }

    protected int getImageCount(HttpServletRequest request) {

        ShoppingCartService shoppingCartService = new ShoppingCartService();
        return shoppingCartService.getShoppingCartUserCount(getUser(request).getUserId());
    }

    protected List loadThumbnailImageList(int sortBy, int orderBy, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        ShoppingCartService shoppingCartService = new ShoppingCartService();
        LngResolver lngResolver = new LngResolver();
        shoppingCartService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        List imageList = shoppingCartService.getShoppingCartImageList(getUser(httpServletRequest).getUserId());
        return imageList;
    }

    protected boolean showSelect(HttpServletRequest request) {
        return true;
    }

    protected String getDetailViewJsp(HttpServletRequest request) {
        return "";
    }

    /*
    protected String getListViewJsp(HttpServletRequest request) {
        System.out.println("getListViewJsp: listCart.jsp");
        return "listCart.jsp";
    }
    */
}
