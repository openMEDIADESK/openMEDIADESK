package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.io.IOException;
import java.math.BigDecimal;

import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.FormatSelector;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.servlet.DownloadServlet;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.image.pinpics.Pinpic;
import net.stumpner.security.acl.AclControllerContext;

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
 * Date: 16.06.2010
 * Time: 20:57:09
 * To change this template use File | Settings | File Templates.
 */
public class DownloadViewController extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {


            LngResolver lngResolver = new LngResolver();
            ShoppingCartService shoppingCartService = new ShoppingCartService();
            shoppingCartService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
            ImageVersionService imageService = new ImageVersionService();
            imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
            User user = WebHelper.getUser(httpServletRequest);

                //httpServletRequest.getSession().removeAttribute("formatSelector");
                List selectedToDownloadList = new LinkedList();
                HttpSession session = httpServletRequest.getSession();
                if (httpServletRequest.getParameter("download").equalsIgnoreCase("selectedMedia") ||
                    httpServletRequest.getParameter("download").equalsIgnoreCase("ivid")) {

                    if (httpServletRequest.getParameter("download").equalsIgnoreCase("selectedMedia")) {
                        //download selected images
                        selectedToDownloadList = (List)session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                    }
                    if (httpServletRequest.getParameter("download").equalsIgnoreCase("ivid")) {
                        //Prüfen wenn der Parameter ivid nicht übergeben ist, wird ein error 404 Error ausgegeben
                        if (httpServletRequest.getParameter("ivid")==null) { httpServletResponse.sendError(404); return null; }
                        int ivid = Integer.parseInt(httpServletRequest.getParameter("ivid"));
                        ImageVersionMultiLang media = (ImageVersionMultiLang)imageService.getImageVersionById(ivid);
                        if (Config.useShoppingCart) {
                            BigDecimal price = Config.currency.isEmpty() ? BigDecimal.valueOf(1) : media.getPrice();
                            if (user.getRole()>= User.ROLE_USER) { //nur wenn eingeloggt, denn sonst wird unterhalb redirected
                                //Bei Warenkorb-Funktion Preis prüfen
                                if (price.compareTo(BigDecimal.valueOf(0))!=0) {
                                    ShoppingCartService scs = new ShoppingCartService();
                                    scs.addImageToShoppingCart(media.getIvid(), user.getUserId());
                                    WebHelper.sendLngRedirect(httpServletRequest,httpServletResponse,"shop");
                                    return null;
                                }
                            } else {
                                //Nicht eingeloggte Benutzer zum Login Required redirecten
                                //Nicht eingeloggt ---> zur loginpage
                                httpServletRequest.getSession().setAttribute(LoginController.ATTRIBUTE_REDIRECT_AFTER_LOGIN,
                                        httpServletRequest.getRequestURI()+"?nosdl&"+httpServletRequest.getQueryString());

                                httpServletResponse.sendRedirect("login");
                                return null;
                            }

                        }
                        selectedToDownloadList.add(media);
                    }
                    session.setAttribute(Resources.SESSIONVAR_DOWNLOAD_IMAGES, selectedToDownloadList);
                } else {
                    //download all
                    //todo: prüfen ob wirklich alle Bilder/Dateien bezahlt wurden
                    shoppingCartService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
                    selectedToDownloadList = shoppingCartService.getShoppingCartImageList(user.getUserId());
                    session.setAttribute(Resources.SESSIONVAR_DOWNLOAD_IMAGES, selectedToDownloadList);
                }
                selectedToDownloadList = (selectedToDownloadList ==null) ? new LinkedList() : selectedToDownloadList;

                //Check ACL - Access of the images (Category)
                //System.out.println("shopping-cart: ausgewähtle bilder zum download: "+selectedToDownloadList.size());
                //List downloadList = new LinkedList(selectedToDownloadList);
                //List deniedList = new LinkedList();
                //if (AclController.isEnabled()) {
                    List downloadList = new LinkedList();
                    List deniedList = new LinkedList(selectedToDownloadList);

            /**
             * Prüfen der Download-Berechtigung
             */

                    AclContextFactory.createAclContext(httpServletRequest);
                    AclControllerContext aclContext = AclContextFactory.getAclContext(httpServletRequest);

                    List permittedImages = DownloadServlet.getPermittedImages(httpServletRequest, aclContext, selectedToDownloadList);
                    downloadList = permittedImages;
                    deniedList = new LinkedList(selectedToDownloadList);
                    deniedList.removeAll(permittedImages);

                //check if credits are enough
                boolean canDownload = true;
        /*
                if (Config.creditSystemEnabled) {
                    if (downloadList.size()>user.getCredits() && !(user.getCredits()==-1)) {
                        canDownload = false;
                    }
                }
                */

        //Wenn Anzahl der erlaubten Downloads > 0 oder der Benutzer ist eingeloggt -> ansonsten -> Loginpage
        if (downloadList.size() > 0 || isLoggedIn(httpServletRequest)) {

                if (canDownload) {
                    httpServletRequest.setAttribute("downloadList",downloadList);
                    httpServletRequest.setAttribute("downloadCount",new Integer(downloadList.size()));
                    httpServletRequest.setAttribute("user",this.getUser(httpServletRequest));
                    httpServletRequest.setAttribute("deniedList",deniedList);

                    //Prüfen ob die Formatauswahl angezeigt werden soll:
                    if (Config.useDownloadResolutions && isImageInDownload(downloadList) && httpServletRequest.getParameter("format")==null) {

                        FormatSelector formatSelector = new FormatSelector();
                        if (httpServletRequest.getSession().getAttribute("formatSelector")==null) {
                            formatSelector = new FormatSelector();
                        }
                        if (httpServletRequest.getSession().getAttribute("formatSelector")!=null) {
                            //Es gibt bereits einen Formatselector in der Session, trotzdem neu erstellen 
                            formatSelector = new FormatSelector();
                        }
                        formatSelector.setDownloadList(downloadList);
                        formatSelector.setDownloadCount(downloadList.size());
                        formatSelector.setDeniedList(deniedList);
                        httpServletRequest.getSession().setAttribute("formatSelector",formatSelector);
                        session.setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,downloadList);
                        httpServletResponse.sendRedirect(
                                httpServletResponse.encodeRedirectURL("formatselector")
                        );
                        return null;
                    } else {
                        //Wenn nur eine einzige Datei ausgewählt ist, Download sofort (ohne Download-Seite) starten
                        // ausnahme parameter nosdl ist angegeben, zb. bei redirect nach login!
                        if (downloadList.size()==1 && deniedList.size()==0 && httpServletRequest.getParameter("nosdl")==null && httpServletRequest.getSession().getAttribute("formatSelector")==null) {
                            ImageVersion imageVersion = (ImageVersion)downloadList.get(0);
                            httpServletResponse.sendRedirect(
                                httpServletResponse.encodeRedirectURL("/download/?sdl=true&ivid="+imageVersion.getIvid())
                            );
                            return null;
                        } else {
                            //this.setContentTemplateFile("download.jsp",httpServletRequest);
                        }
                    }
                } else {
                    WebStack webStack = new WebStack(httpServletRequest);

                    //System.out.println("Stacklist String: "+webStack.getStacklistString());

                    session.setAttribute("headline","nocredits.headline");
                    session.setAttribute("nextUrl","javascript:window.history.back();");
                    session.setAttribute("text","nocredits.info");
                    session.setAttribute("subheadline","nocredits.subheadline");
                    session.setAttribute("subheadlineArgs",user.getCredits());

                    //this.setContentTemplateFile("message.jsp",httpServletRequest);
                    httpServletResponse.sendRedirect(
                            httpServletResponse.encodeRedirectURL("message")
                    );
                    return null;
                }


            if (httpServletRequest.getParameter("format")!=null) {
                //back-"request" from formatSelector-page
                httpServletRequest.setAttribute("formatSelector",httpServletRequest.getSession().getAttribute("formatSelector"));
                httpServletRequest.setAttribute("useFormatSelector",new Boolean(true));
                //this.setContentTemplateFile("download.jsp",httpServletRequest);
            }

            return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.

        } else {
            //Nicht eingeloggt: Prüfen ob PIN
            if (httpServletRequest.getSession().getAttribute("pinid")!=null) {
                handlePinDownload(httpServletRequest, httpServletResponse);
                return null;
            } else {
                //Nicht eingeloggt ---> zur loginpage
                httpServletRequest.getSession().setAttribute(LoginController.ATTRIBUTE_REDIRECT_AFTER_LOGIN,
                        httpServletRequest.getRequestURI()+"?nosdl&"+httpServletRequest.getQueryString());

                httpServletResponse.sendRedirect("login");
                return null;
            }
        }
    }

    /**
     * Download eines Pins
     * @param request
     */
    private void handlePinDownload(HttpServletRequest request, HttpServletResponse response) {

        int pinId = ((Integer)request.getSession().getAttribute("pinid"));
        PinpicService pinService = new PinpicService();
        ImageVersionService imageService = new ImageVersionService();
        LngResolver lngResolver = new LngResolver();
        imageService.setUsedLanguage(lngResolver.resolveLng(request));

        try {
            Pinpic pinpic = (Pinpic)pinService.getById(pinId);
            List pinpicImages = pinService.getPinpicImages(pinId);

            HttpSession session = request.getSession();
            List selectedToDownloadList = new LinkedList();
                if (request.getParameter("download").equalsIgnoreCase("ivid")) {

                    //----------------- Einzelner Download
                    if (request.getParameter("download").equalsIgnoreCase("ivid")) {
                        int ivid = Integer.parseInt(request.getParameter("ivid"));

                            boolean isInPin = false;
                            Iterator pinFiles = pinpicImages.iterator();
                            while (pinFiles.hasNext()) {
                                ImageVersion imageVersionPin = (ImageVersion)pinFiles.next();
                                if (imageVersionPin.getIvid()==ivid) {
                                    isInPin = true;
                                }
                            }
                        if (isInPin) {
                            selectedToDownloadList.add(imageService.getImageVersionById(ivid));
                            try {
                                response.sendRedirect(
                                    response.encodeRedirectURL("/download/?pinpic=ivid&ivid="+ivid)
                                );
                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        } else {
                            try {
                                response.sendError(405,"Medien-Object mit der ID "+ivid+" ist nicht dem PIN zugeordnet");
                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                    session.setAttribute(Resources.SESSIONVAR_DOWNLOAD_IMAGES, selectedToDownloadList);
                }


        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Gibt true zurück wenn die angegebene ImageList ein Bild enthält
     * @param downloadList
     * @return
     */
    private boolean isImageInDownload(List downloadList) {
        for (Object obj : downloadList) {

            ImageVersionMultiLang image = (ImageVersionMultiLang)obj;
            if (image.getMayorMime().equalsIgnoreCase("image")) {
                return true;
            }

        }
        return false;  
    }
}
