package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.FavoriteService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.UserFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.stumpner.mediadesk.core.service.MediaObjectService;
import org.apache.commons.lang3.StringEscapeUtils;

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
 * Date: 12.10.2015
 * Time: 12:14:58
 * To change this template use File | Settings | File Templates.
 *
 *
 * /api/rest/fav/-1/{medialist|child|parent}
 *   1   2    3   4            5
 *
 * /api/rest/fav/-1/removeselected (??)
 *
 * /api/rest/fav        ==> GET-REQUEST = medialist
 * /api/rest/fav/{<ivid>|deleteselected} ==> DEL-REQUEST = aus warenkorb l�schen
 * /api/rest/fav/<ivid> ==> PUT-REQUEST = in den warenkorb geben
 */
public class FavRestApi extends RestBaseServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        System.out.println("doGet [FavRestApi]");

        jsonCartMedialist(request, response);

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPut [FavRestApi]");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        FavoriteService shoppingCartService = new FavoriteService();
        int userId = WebHelper.getUser(request).getUserId();

        if (request.getRequestURI().equalsIgnoreCase("/api/rest/fav/deleteselected")) {
            System.out.println("deleteselected - wird nicht mehr verwendet");
            //ausgew�hlte von den favoriten l�schen
                               /*
            List<MediaObject> list = MediaObjectService.getSelectedMediaObjectList(request.getSession());
            System.out.println("list");
            for (MediaObject mo : list) {
                System.out.println("for");
                shoppingCartService.removeMediaFromFav(mo.getIvid(), userId);
                //MediaObjectService.deselectMedia(mo.getIvid(), request);
            }
            MediaObjectService.deselectMedia(null, request);
            */
            /*
            response.getWriter().println("OK");
            response.getWriter().flush();
            response.setStatus(200);
            System.out.println("ende");*/
        } else {
            System.out.println("add image to lightbox");
            int ivid = this.getUriSectionInt(4, request);
            shoppingCartService.addMediaToFav(ivid, userId);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        User user = WebHelper.getUser(request);

        if (user.getRole()>=User.ROLE_USER) {

            System.out.println("DELETE request at Fav ");

            if (request.getRequestURI().equalsIgnoreCase("/api/rest/fav/deleteselected")) {
                //ausgew�hlte l�schen

                FavoriteService shoppingCartService = new FavoriteService();
                List<MediaObject> list = MediaObjectService.getSelectedMediaObjectList(request.getSession());
                System.out.println("list");
                for (MediaObject mo : list) {
                    System.out.println("for");
                    shoppingCartService.removeMediaFromFav(mo.getIvid(), user.getUserId());
                    //MediaObjectService.deselectMedia(mo.getIvid(), request);
                }
                MediaObjectService.deselectMedia(null, request);

                System.out.println("ausgew�hlte l�schen ");

            } else {
                int ivid = this.getUriSectionInt(4, request);
                FavoriteService shoppingCartService = new FavoriteService();
                shoppingCartService.removeMediaFromFav(ivid, WebHelper.getUser(request).getUserId());
            }

        } else {
            response.sendError(403, "Nicht angemeldet");

        }
    }

    private void jsonCartMedialist(HttpServletRequest request, HttpServletResponse response) {

        LngResolver lngResolver = new LngResolver();
        MediaService mediaService = new MediaService();
        mediaService.setUsedLanguage(lngResolver.resolveLng(request));

        FavoriteService shoppingCartService = new FavoriteService();
        shoppingCartService.setUsedLanguage(lngResolver.resolveLng(request));
        List<MediaObjectMultiLang> imageList = shoppingCartService.getMediaObjectList(getUser(request).getUserId());


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
                out.println("  \"downloadUrl\" : \"download?download=ivid&ivid="+mo.getIvid()+"\",");
                out.println("  \"downloadlink\" : \"download?download=ivid&ivid="+mo.getIvid()+"\",");
                out.println("  \"editUrl\" : \"mediadetailedit?id="+mo.getIvid()+"\",");
                out.println("  \"shoppingCartUrl\" : \""+mo.getVersionName()+"\",");
                out.println("  \"lightboxUrl\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"mimeClass\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"previewUrl\" : \"wpreview?id="+mo.getIvid()+"\",");
                out.println("  \"note\" : \""+mo.getNote()+"\",");

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
}
