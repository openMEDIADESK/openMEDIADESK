package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.Folder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.core.database.sc.LightboxService;
import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.pinpics.Pinpic;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.LngResolver;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

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
public class PinPicViewController extends AbstractThumbnailViewController {

    public String getViewNameDirectDownload() {
        return viewNameDirectDownload;
    }

    public void setViewNameDirectDownload(String viewNameDirectDownload) {
        this.viewNameDirectDownload = viewNameDirectDownload;
    }

    String viewNameDirectDownload = "";

    protected int getContainerId(HttpServletRequest request) {
        return getPin(request).getPinpicId();
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {


        httpServletRequest.setAttribute("pinmenu",new Boolean(true));

        int pinId = -1;

        if (getUser(httpServletRequest).getRole()==User.ROLE_UNDEFINED) {
            //PIN-Besucher (in Pin eingeloggt)
            if (httpServletRequest.getSession().getAttribute("pinid")!=null) {
                pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
                logger.debug("picview: PIN: "+pinId);
            }
        }

        if (getUser(httpServletRequest).getRole()>=User.ROLE_PINMAKLER) {
            //Benutzer der eingeloggt ist
            if (httpServletRequest.getParameter("pinid")!=null) {
                pinId = Integer.parseInt(httpServletRequest.getParameter("pinid"));
                httpServletRequest.getSession().setAttribute("pinid",new Integer(pinId));
            } else {
                //Aus session holen
                pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
            }
        }


        if (pinId == -1) {
            //keine pin angegeben...
            //zu /pin redirecten:
            logger.debug("Redirect:  "+httpServletResponse.encodeRedirectURL("pin"));
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("pin")
            );
            return null;
        }

        PinpicService pinpicService = new PinpicService();
        LngResolver lngResolver = new LngResolver();
        pinpicService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        Folder folder = new Folder();
        folder.setCatTitle("Pin");
        User user = (User)httpServletRequest.getSession().getAttribute("user");

        if (httpServletRequest.getParameter("lightbox")!=null) {

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user")!=null) {
                //user eingeloggt
                user = (User)session.getAttribute("user");
                LightboxService lightboxService = new LightboxService();
                if (httpServletRequest.getParameter("lightbox").equals("add")) {
                    //hinzufÃ¼gen
                    lightboxService.addImageToLightbox(
                            Integer.parseInt((String)httpServletRequest.getParameter("ivid")), user.getUserId()
                    );
                } else {
                    //lÃ¶schen
                    lightboxService.removeImageToLightbox(
                            Integer.parseInt((String)httpServletRequest.getParameter("ivid")), user.getUserId()
                    );
                }
            }
        }

        //PaginatedList imageList = lightboxService.getLightboxImagesPaginatedList(user.getUserId(),12);
        pinpicService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        PaginatedList imageList = pinpicService.getPinpicImagesPaginated(pinId, Config.itemCountPerPage);

        int numberOfPages = 1;
        while (imageList.nextPage()) {
            imageList.gotoPage(numberOfPages);
            numberOfPages++;
        }

        int viewPage = 0;
        if (httpServletRequest.getParameter("page")!=null) {
            if (httpServletRequest.getParameter("page").length()>0) {
                try {
                    viewPage = Integer.parseInt(
                            httpServletRequest.getParameter("page"))-1;
                } catch (NumberFormatException e) {
                    //keine gÃ¼ltige nummer: nichts tun...
                }
            }
        }
        imageList.gotoPage(viewPage);

        List imageListLine = new LinkedList();
        List imageListLines = new LinkedList();

        Iterator images = imageList.iterator();
        for (int a=0;a<4;a++) {
            //jeweils 4 lines
            imageListLine = new LinkedList();
            int foldersInLine = 0;
            for (int b=0;b<3;b++) {
                //jeweils 3 folder in einer line
                if (images.hasNext()) {
                    ImageVersion imageVersion = (ImageVersion)images.next();
                    imageListLine.add(imageVersion);
                    foldersInLine++;
                } else {
                    imageListLine.add(new ImageVersion());
                }
            }
            if (foldersInLine>0)
                imageListLines.add(new LinkedList(imageListLine));
        }

        httpServletRequest.setAttribute("folder",folder);
        httpServletRequest.setAttribute("imageListLines",imageListLines);
        httpServletRequest.setAttribute("imageList",imageList);

        httpServletRequest.setAttribute("pageSize",Integer.toString(imageList.size()));
        httpServletRequest.setAttribute("pageIndex",Integer.toString(imageList.getPageIndex()+1));

        String nextPage = (!imageList.isLastPage()) ? Integer.toString(viewPage+1+1) :"0";
        String prevPage = (!imageList.isFirstPage()) ? Integer.toString(viewPage-1+1) :"0";
        httpServletRequest.setAttribute("nextPage",nextPage);
        httpServletRequest.setAttribute("prevPage",prevPage);

        //for thumbnailmodule
        httpServletRequest.setAttribute("servletMapping","pinpic");

        if ("noimages".equalsIgnoreCase(httpServletRequest.getParameter("error"))) {
            httpServletRequest.setAttribute("error","download.empty");
            //httpServletRequest.setAttribute("showMessage",showMessage);
            //httpServletRequest.setAttribute("message","download.empty"); 
        } else {
            //model.remove("error");
        }

        Pinpic pin = (Pinpic)pinpicService.getById(pinId);
        httpServletRequest.setAttribute("pin",pin);


        httpServletRequest.setAttribute("uploadEnabled",isUploadEnabled(pin, httpServletRequest));

        ModelAndView mav = null;
        if (pin.isDirectDownload() && this.getUser(httpServletRequest).getRole()==User.ROLE_UNDEFINED) {
            //Direct-Download Seite anzeigen
            mav = super.handleRequestInternal(httpServletRequest, httpServletResponse);
            mav.setViewName(this.getViewNameDirectDownload());
        } else {
            mav = super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
        }

        httpServletRequest.setAttribute("thumbnailShowPrice", false); //Bei PIN keinen Preis zeigen
        return mav;
    }

    private boolean isUploadEnabled(Pinpic pin, HttpServletRequest request) {
        //Upload Erlaubt: Wenn im Pin aktiviert oder der User Mindestens PIN-Editor
        boolean uploadEnabled = false;
        if (this.getUser(request).getRole()>=User.ROLE_PINEDITOR) {
            uploadEnabled = true;
        }
        if (pin.isUploadEnabled()) {
            uploadEnabled = true;
        }

        return uploadEnabled;
    }

    public Pinpic getPin(HttpServletRequest request) {

        if (request.getAttribute("pin")!=null) {
            //Load PIN from Cache
            return (Pinpic)request.getAttribute("pin");
        } else {
            //Load PIN from DB
            if (getPinId(request)!=-1) {
                try {
                    request.getSession().setAttribute("pinid",getPinId(request));
                    PinpicService pinpicService = new PinpicService();
                    Pinpic pin = (Pinpic)pinpicService.getById(getPinId(request));
                    request.setAttribute("pin",pin);
                    return pin;

                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return null;
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private int getPinId(HttpServletRequest request) {

        if (request.getParameter("pinid")!=null) {
            return Integer.parseInt(request.getParameter("pinid"));
        }
        return -1;
    }

    protected void insert(ImageVersion image, HttpServletRequest request) throws DublicateEntry {
        PinpicService pinpicService = new PinpicService();
        pinpicService.addImageToPinpic(image.getIvid(),getPin(request).getPinpicId());
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {

        PinpicService pinpicService = new PinpicService();
        pinpicService.deleteImageFromPinpic(image.getIvid(),getPin(request).getPinpicId());

    }

    protected String getServletMapping(HttpServletRequest request) {
        return "p";
    }

    protected int getImageCount(HttpServletRequest request) {
        PinpicService pinpicService = new PinpicService();
        List images = pinpicService.getPinpicImages(getPinId(request));
        int imageCount = images.size();
        //System.out.println("Image-Count in PIN: "+imageCount);
        return imageCount;
    }

}

