package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.FavoriteService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.MediaDetailEditCommand;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.MediaDetailEditController;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.UserFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.stumpner.mediadesk.core.service.MediaObjectService;
import org.json.JSONObject;

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
 * Date: 07.10.2015
 * Time: 13:12:55
 * To change this template use File | Settings | File Templates.
 *
 * /api/rest/mo/<ivid>
 *   1   2   3    4
 *
 * * /api/rest/mo/<ivid>/editmode
 *
 */
public class MediaobjectRestApi extends RestBaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        int ivid = getUriSectionInt(4, request);
        LngResolver lngResolver = new LngResolver();
        MediaService mos = new MediaService();
        mos.setUsedLanguage(lngResolver.resolveLng(request));
        MediaObjectMultiLang mo = (MediaObjectMultiLang)mos.getMediaObjectById(ivid);

        int uriSectionCount = getUriSectionCount(request);
        //System.out.println("urisectioncount="+uriSectionCount);
        switch (uriSectionCount) {
            case 5: getData(request, response, mo);
                break;
            case 6: getDataExtended(request, response, mo);
        }

    }

    private void getDataExtended(HttpServletRequest request, HttpServletResponse response, MediaObjectMultiLang mo) throws IOException {

        String mode = getUriSection(5,request);
        if (mode.equalsIgnoreCase("editmode")) {

            MediaDetailEditCommand ivmeta = (MediaDetailEditCommand)request.getSession().getAttribute(MediaDetailEditController.class.getName()+".FORM.command");
            MediaObjectMultiLang sessionMo = (MediaObjectMultiLang)ivmeta.getImageVersion();

            writeData(response, request, sessionMo);
        }
    }

    private void getData(HttpServletRequest request, HttpServletResponse response, MediaObjectMultiLang mo) throws IOException {

        //System.out.println("/api/rest/mo/"+ivid+" request");

        writeData(response, request, mo);

    }

    private void writeData(HttpServletResponse response, HttpServletRequest request, MediaObjectMultiLang mo) throws IOException {

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

        /* NEU */

        JSONObject responseObj = new JSONObject();

        responseObj.put("idvid",String.valueOf(mo.getIvid()));
        responseObj.put("ivid",String.valueOf(mo.getIvid()));
        responseObj.put("caption",getCaption(mo));
        responseObj.put("name",mo.getVersionName());
        responseObj.put("imagenumber",mo.getImageNumber());
        responseObj.put("title",mo.getVersionTitle());

        responseObj.put("titleLng1",mo.getVersionTitleLng1());
        responseObj.put("titleLng2",mo.getVersionTitleLng2());

        responseObj.put("subtitle",mo.getVersionSubTitle());

        responseObj.put("info",mo.getInfo());
        responseObj.put("photographDate",mo.getPhotographDate().getTime());
        responseObj.put("createDate",mo.getCreateDate().getTime());
        responseObj.put("site",mo.getSite());
        responseObj.put("photographerAlias",mo.getPhotographerAlias());
        responseObj.put("byline",mo.getByline());
        responseObj.put("kb",String.valueOf(mo.getKb()));
        responseObj.put("width",String.valueOf(mo.getWidth()));
        responseObj.put("height",String.valueOf(mo.getHeight()));
        responseObj.put("dpi",String.valueOf(mo.getDpi()));
        responseObj.put("people",mo.getPeople());
        responseObj.put("restrictions",mo.getRestrictions());
        responseObj.put("keywords",mo.getKeywords());
        responseObj.put("customStr1",mo.getCustomStr1());
        responseObj.put("customStr2",mo.getCustomStr2());
        responseObj.put("customStr3",mo.getCustomStr3());
        responseObj.put("customStr4",mo.getCustomStr4());
        responseObj.put("customStr5",mo.getCustomStr5());
        responseObj.put("customStr6",mo.getCustomStr6());
        responseObj.put("customStr7",mo.getCustomStr7());
        responseObj.put("customStr8",mo.getCustomStr8());
        responseObj.put("customStr9",mo.getCustomStr9());
        responseObj.put("customStr10",mo.getCustomStr10());
        responseObj.put("selivid",String.valueOf(mo.getIvid()+";"+(MediaObjectService.isSelected(mo, request) ? "true" : "false")));
        responseObj.put("mime",mo.getMimeType());
        responseObj.put("mayorMime",mo.getMayorMime());
        responseObj.put("minorMime",mo.getMinorMime());
        responseObj.put("fileExtention",mo.getExtention());
        responseObj.put("size",mo.getKb());
        responseObj.put("price",mo.getPrice());
        responseObj.put("downloadUrl","download?download=ivid&ivid="+mo.getIvid());
        responseObj.put("editUrl","mediadetailedit?id="+mo.getIvid());
        responseObj.put("shoppingCartUrl",mo.getVersionName());
        responseObj.put("lightboxUrl",mo.getVersionName()); //TODO: pr�fen
        responseObj.put("mimeClass",mo.getVersionName()); //TODO: pr�fen
        responseObj.put("previewUrl","wpreview?id=\"+mo.getIvid()");
        responseObj.put("note",mo.getNote());

        responseObj.put("fav",  (inFav ? "true" : "false"));
        responseObj.put("cart", (inCart ? "true" : "false"));
        responseObj.put("selected",(MediaObjectService.isSelected(mo, request) ? "true" : "false"));

               /*
            for (String key : splashPageValueMap.keySet()) {
                responseObj.put(key,splashPageValueMap.get(key));
            }    */

            //System.out.println("out = "+responseObj.toString());


            PrintWriter writer = response.getWriter();
            responseObj.write(writer);


        /* ALT
        PrintWriter out = response.getWriter();

                out.println(" {");
                out.println("  \"ivid\" : "+mo.getIvid()+",");
                out.println("  \"caption\" : \""+StringEscapeUtils.escapeJson(getCaption(mo))+"\",");
                out.println("  \"name\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"imagenumber\" : \""+StringEscapeUtils.escapeJson(mo.getImageNumber())+"\",");
                out.println("  \"title\" : \""+StringEscapeUtils.escapeJson(mo.getVersionTitle())+"\",");
                out.println("  \"subtitle\" : \""+StringEscapeUtils.escapeJson(mo.getVersionSubTitle())+"\",");
                out.println("  \"info\" : \""+StringEscapeUtils.escapeJson(mo.getInfo())+"\",");
                out.println("  \"photographDate\" : \""+mo.getPhotographDate().getTime()+"\",");
                out.println("  \"createDate\" : "+mo.getCreateDate().getTime()+",");
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
                out.println(" }");

                */

    }
}
