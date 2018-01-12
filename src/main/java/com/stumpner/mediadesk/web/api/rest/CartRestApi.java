package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.usermanagement.User;

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
 * Time: 12:14:48
 * To change this template use File | Settings | File Templates.
 *
 * /api/rest/cart/-1/{medialist|child|parent}
 *   1   2    3   4            5
 *
 * /api/rest/cart/-1/removeselected (??)
 *
 * /api/rest/cart        ==> GET-REQUEST = medialist
 * /api/rest/cart/<ivid> ==> DEL-REQUEST = aus warenkorb l�schen
 * /api/rest/cart/<ivid> ==> PUT-REQUEST = in den warenkorb geben
 *
 */
public class CartRestApi extends RestBaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        System.out.println("doGet [CartRestApi]");

        String type = this.getUriSection(5, request);
        if (type!=null) {
            if (type.equalsIgnoreCase("medialist")) {
                //Liste der Medienobjekte
                System.out.println("doGet [CartRestApi->medialist]");
                jsonCartMedialist(request, response);
            }

            /*
            if (type.equalsIgnoreCase("removeselected")) {
                removeSelected(request, response);
            } */
        } else {
            //immer medialist zur�ckgeben wenn get request und sonst nichts angegeben
            System.out.println("doGet [CartRestApi->medialist]");
            jsonCartMedialist(request, response);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPut [CartRestApi]");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        int ivid = this.getUriSectionInt(4, request);
        ShoppingCartService shoppingCartService = new ShoppingCartService();
        shoppingCartService.addImageToShoppingCart(ivid, WebHelper.getUser(request).getUserId());

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        System.out.println("doDelete [CartRestApi->medialist]");

        User user = WebHelper.getUser(request);

        if (user.getRole()>=User.ROLE_USER) {

            int ivid = this.getUriSectionInt(4, request);
            ShoppingCartService shoppingCartService = new ShoppingCartService();
            shoppingCartService.removeImageToShoppingCart(ivid, WebHelper.getUser(request).getUserId());

        } else {
            response.sendError(403, "Nicht angemeldet");

        }

    }

    private void jsonCartMedialist(HttpServletRequest request, HttpServletResponse response) {

        LngResolver lngResolver = new LngResolver();
        ImageVersionService imageService = new ImageVersionService();
        imageService.setUsedLanguage(lngResolver.resolveLng(request));

        ShoppingCartService shoppingCartService = new ShoppingCartService();
        shoppingCartService.setUsedLanguage(lngResolver.resolveLng(request));
        List<ImageVersionMultiLang> imageList = shoppingCartService.getShoppingCartImageList(getUser(request).getUserId());


        try {
            PrintWriter out = response.getWriter();

            out.println("[");
            for (ImageVersionMultiLang mediaObject : imageList) {
                out.println(" {");
                out.println("  \"ivid\" : "+mediaObject.getIvid()+",");
                out.println("  \"caption\" : \""+StringEscapeUtils.escapeJson(getCaption(mediaObject))+"\",");
                out.println("  \"title\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionTitle())+"\",");
                out.println("  \"name\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionName())+"\",");
                out.println("  \"imagenumber\" : \""+mediaObject.getImageNumber()+"\",");
                out.println("  \"title\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionTitle())+"\",");
                out.println("  \"subtitle\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionSubTitle())+"\",");
                out.println("  \"info\" : \""+StringEscapeUtils.escapeJson(mediaObject.getInfo())+"\",");
                out.println("  \"photographDate\" : \""+mediaObject.getPhotographDate().getTime()+"\",");
                out.println("  \"site\" : \""+StringEscapeUtils.escapeJson(mediaObject.getSite())+"\",");
                out.println("  \"photographerAlias\" : \""+StringEscapeUtils.escapeJson(mediaObject.getPhotographerAlias())+"\",");
                out.println("  \"byline\" : \""+StringEscapeUtils.escapeJson(mediaObject.getByline())+"\",");
                out.println("  \"selivid\" : \""+mediaObject.getIvid()+";"+(MediaObjectService.isSelected(mediaObject, request) ? "true" : "false")+"\",");
                out.println("  \"mime\" : \""+mediaObject.getMimeType()+"\",");
                out.println("  \"mayorMime\" : \""+mediaObject.getMayorMime()+"\",");
                out.println("  \"minorMime\" : \""+mediaObject.getMinorMime()+"\",");
                out.println("  \"size\" : \""+mediaObject.getKb()+"\",");
                out.println("  \"price\" : \""+mediaObject.getPrice()+"\",");
                out.println("  \"downloadUrl\" : \"download?download=ivid&ivid="+mediaObject.getIvid()+"\",");
                out.println("  \"editUrl\" : \"mediadetailedit?id="+mediaObject.getIvid()+"\",");
                out.println("  \"shoppingCartUrl\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionName())+"\",");
                out.println("  \"lightboxUrl\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionName())+"\",");
                out.println("  \"mimeClass\" : \""+StringEscapeUtils.escapeJson(mediaObject.getVersionName())+"\",");
                out.println("  \"previewUrl\" : \"wpreview?id="+mediaObject.getIvid()+"\",");
                out.println("  \"note\" : \""+StringEscapeUtils.escapeJson(mediaObject.getNote())+"\",");
                out.println("  \"createDate\" : \""+mediaObject.getVersionName()+"\",");

                //Daten f�r Sitzung (Ausgew�hlt)
//                out.println(true ? "" : "");
                out.println("  \"selected\" : "+ (MediaObjectService.isSelected(mediaObject, request) ? "true" : "false") );

                if (imageList.indexOf(mediaObject)<imageList.size()-1) {
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

    private void removeSelected(HttpServletRequest request, HttpServletResponse response) {

    }
}
