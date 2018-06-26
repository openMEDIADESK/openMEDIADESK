package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.UserFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
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
 * Date: 30.03.2016
 * Time: 22:16:03
 * To change this template use File | Settings | File Templates.
 *
 * /api/rest/pin/<pinid>/{medialist|child|parent}
 *   1   2   3      4                   5
 *
 * /api/rest/pin/<pinid>/removeselected
 * /api/rest/pin/<pinid>/deleteselected
 * /api/rest/pin/<pinid>/insertselected
 *
 */
public class PinRestApi extends RestBaseServlet {

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        String type = this.getUriSection(5, request);
        if (type!=null) {

            if (type.equalsIgnoreCase("removeselected")) {
                removeSelected(request, response);
            }
            if (type.equalsIgnoreCase("deleteselected")) {
                //deleteSelected(request, response);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        String type = this.getUriSection(5, request);
        if (type!=null) {
            if (type.equalsIgnoreCase("medialist")) {
                //Liste der Medienobjekte
                jsonMedialist(request, response);
            }

            /*
            if (type.equalsIgnoreCase("removeselected")) {
                removeSelected(request, response);
            }
            if (type.equalsIgnoreCase("deleteselected")) {
                //deleteSelected(request, response);
            } */
            if (type.equalsIgnoreCase("insertselected")) {
                insertSelected(request, response);
            }
        }

    }

    private void jsonMedialist(HttpServletRequest request, HttpServletResponse response) {

        LngResolver lngResolver = new LngResolver();
        PinService pinService = new PinService();
        pinService.setUsedLanguage(lngResolver.resolveLng(request));


        //Loader-Class: definieren was geladen werden soll
        int pinId = getUriSectionInt(4, request);

        List<MediaObjectMultiLang> imageList = pinService.getPinpicImages(pinId);

        try {
            PrintWriter out = response.getWriter();

            out.println("[");
            for (MediaObjectMultiLang mo : imageList) {

                //ShoppingCart Logik (true/false) ob dieses Medienobjekt im Cart ist
                ShoppingCartService cartService = new ShoppingCartService();
                FavoriteService favoriteService = new FavoriteService();
                User user = WebHelper.getUser(request);
                boolean inCart = false;
                boolean inFav = false;
                if (user.getUserId()!= UserFactory.createVisitorUser().getUserId()) {
                    //Wenn es sich um einen eingeloggten Benutzer handelt, hat er einen Warenkorb
                    if (Config.useShoppingCart) {
                        inCart = cartService.isInCart(user.getUserId(), mo.getIvid());
                    }
                    if (Config.useLightbox) {
                        inFav = favoriteService.isInFav(user.getUserId(), mo.getIvid());
                    }
                }

                out.println(" {");
                out.println("  \"ivid\" : "+mo.getIvid()+",");
                out.println("  \"caption\" : \""+StringEscapeUtils.escapeJson(getCaption(mo))+"\",");
                out.println("  \"name\" : \""+ StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"imagenumber\" : \""+StringEscapeUtils.escapeJson(mo.getMediaNumber())+"\",");
                out.println("  \"title\" : \""+StringEscapeUtils.escapeJson(mo.getVersionTitle())+"\",");
                out.println("  \"subtitle\" : \""+StringEscapeUtils.escapeJson(mo.getVersionSubTitle())+"\",");
                out.println("  \"info\" : \""+StringEscapeUtils.escapeJson(mo.getInfo())+"\",");
                out.println("  \"photographDate\" : \""+mo.getPhotographDate().getTime()+"\",");
                out.println("  \"createDate\" : \""+mo.getCreateDate().getTime()+"\",");
                out.println("  \"site\" : \""+StringEscapeUtils.escapeJson(mo.getSite())+"\",");
                out.println("  \"photographerAlias\" : \""+StringEscapeUtils.escapeJson(mo.getPhotographerAlias())+"\",");
                out.println("  \"byline\" : \""+StringEscapeUtils.escapeJson(mo.getByline())+"\",");
                out.println("  \"kb\" : \""+mo.getKb()+"\",");
                out.println("  \"width\" : \""+mo.getWidth()+"\",");
                out.println("  \"height\" : \""+mo.getHeight()+"\",");
                out.println("  \"dpi\" : \""+mo.getDpi()+"\",");
                out.println("  \"people\" : \""+StringEscapeUtils.escapeJson(mo.getPeople())+"\",");
                out.println("  \"restrictions\" : \""+StringEscapeUtils.escapeJson(mo.getRestrictions())+"\",");
                out.println("  \"keywords\" : \""+StringEscapeUtils.escapeJson(mo.getKeywords())+"\",");
                out.println("  \"customStr1\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr1())+"\",");
                out.println("  \"customStr2\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr2())+"\",");
                out.println("  \"customStr3\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr3())+"\",");
                out.println("  \"customStr4\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr4())+"\",");
                out.println("  \"customStr5\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr5())+"\",");
                out.println("  \"customStr6\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr6())+"\",");
                out.println("  \"customStr7\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr7())+"\",");
                out.println("  \"customStr8\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr8())+"\",");
                out.println("  \"customStr9\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr9())+"\",");
                out.println("  \"customStr10\" : \""+StringEscapeUtils.escapeJson(mo.getCustomStr10())+"\",");
                out.println("  \"selivid\" : \""+mo.getIvid()+";"+(MediaObjectService.isSelected(mo, request) ? "true" : "false")+"\",");
                out.println("  \"mime\" : \""+mo.getMimeType()+"\",");
                out.println("  \"mayorMime\" : \""+mo.getMayorMime()+"\",");
                out.println("  \"minorMime\" : \""+mo.getMinorMime()+"\",");
                out.println("  \"fileExtention\" : \""+StringEscapeUtils.escapeJson(mo.getExtention())+"\",");
                out.println("  \"size\" : \""+mo.getKb()+"\",");
                out.println("  \"price\" : \""+mo.getPrice()+"\",");
                out.println("  \"editUrl\" : \"mediadetailedit?id="+mo.getIvid()+"\",");
                out.println("  \"shoppingCartUrl\" : \""+mo.getVersionName()+"\",");
                out.println("  \"lightboxUrl\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"mimeClass\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"previewUrl\" : \"wpreview?id="+mo.getIvid()+"\",");
                out.println("  \"note\" : \""+StringEscapeUtils.escapeJson(mo.getNote())+"\",");

                out.println("  \"downloadlink\" : \"/download/?pinpic=ivid&ivid="+mo.getIvid()+"\",");

                //Daten f�r Sitzung (Ausgew�hlt)
//                out.println(true ? "" : "");
                out.println("  \"fav\" : "+ (inFav ? "true" : "false") +"," );
                out.println("  \"cart\" : "+ (inCart ? "true" : "false") +"," );
                out.println("  \"selected\" : "+ (MediaObjectService.isSelected(mo, request) ? "true" : "false") );

                if (imageList.indexOf(mo)<imageList.size()-1) {
                    out.println(" },");
                } else {
                    //Letztes Element
                    out.println(" }");
                }
                /*
               {name: 'ivid'},
               {name: 'title', type: 'string'},
               {name: 'name', type: 'string'},
               {name: 'imagenumber'},
               {name: 'selivid'},
               {name: 'mime'},
               {name: 'minorMime'},
               {name: 'size'},
               {name: 'downloadUrl'},
               {name: 'editUrl'},
               {name: 'shoppingCartUrl'},
               {name: 'lightboxUrl'},
               {name: 'mimeClass'},
               {name: 'imageUrl'},
               {name: 'note'},
               {name: 'createDate'}
                 */
            }
            out.println("]");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void insertSelected(HttpServletRequest request, HttpServletResponse response) {


        List<MediaObject> selectedList = MediaObjectService.getSelectedMediaObjectList(request.getSession());

        int pinId = getUriSectionInt(4, request);

        System.out.println("insert to pin:"+pinId);

        PinService pinService = new PinService();
        for (MediaObject mo : selectedList) {
                System.out.println("insert media:"+mo.getIvid());
                pinService.addMediaToPin(mo.getIvid(), pinId);
        }

        MediaObjectService.deselectMedia(null, request);
    }

    private void removeSelected(HttpServletRequest request, HttpServletResponse response) {

        List<MediaObject> selectedList = MediaObjectService.getSelectedMediaObjectList(request.getSession());

        int pinId = getUriSectionInt(4, request);

        PinService pinService = new PinService();
        try {
            Pin pin = (Pin)pinService.getById(pinId);
            if (accessAllowed(pin,WebHelper.getUser(request))) {

                for (MediaObject mo : selectedList) {
                    pinService.deleteMediaFromPin(mo.getIvid(), pinId);
                }

                MediaObjectService.deselectMedia(null, request);

        } else {
            try {
                response.sendError(403, "Keine Berechtigung");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (IOServiceException e) {
            e.printStackTrace();
        }
    }

    private boolean accessAllowed(Pin pin, User user) {

        if (user.getRole()>=User.ROLE_MASTEREDITOR) {
            return true;
        }

        if (user.getRole()==User.ROLE_PINEDITOR || user.getRole()==User.ROLE_PINMAKLER) {

            if (pin.getCreatorUserId()==user.getUserId()) {
                return true;
            }

        }

        return false; //alles andere ist nicht berechtigt

    }

}
