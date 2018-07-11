package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.folder.FolderMultiLang;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.mvc.FolderEditController;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.UserFactory;

import com.stumpner.mediadesk.core.service.MediaObjectService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

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
 * Date: 07.06.2013
 * Time: 10:05:32
 *
 * /api/rest/folder/<folderid>/{medialist|child|parent|setfolderimageselected|properties|editmode}
 *   1   2      3           4                   5
 *
 * /api/rest/folder/<folderid>/removeselected
 * /api/rest/folder/<folderid>/deleteselected
 * /api/rest/folder/<folderid>/insertselected
 *
 * Verschiebt
 *   1   2    3          4           5    6      7    8
 * /api/rest/folder/<folderid>/move/{ivid}/from/{folderid}
 * /api/rest/folder/<folderid>/copy/{ivid}
 *
 * POST:
 *   1   2   3        4
 * /api/rest/folder/<folderid>
 */
public class FolderRestApi extends RestBaseServlet {

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        //System.out.println("DELETE request: "+request.getRequestURI());

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        String type = this.getUriSection(5, request);
        if (type!=null) {

            if (type.equalsIgnoreCase("removeselected")) {
                removeSelected(request, response);
            }
            if (type.equalsIgnoreCase("deleteselected")) {
                deleteSelected(request, response);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        //System.out.println("GET request: "+request.getRequestURI());
        
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        String type = this.getUriSection(5, request);
        if (type!=null) {

            if (type.equalsIgnoreCase("properties") || type.equalsIgnoreCase("editmode")) {
                boolean editmode = false;
                if (type.equalsIgnoreCase("editmode")) { editmode = true; }
                jsonFolderProperties(request, response, editmode);
            }

            if (type.equalsIgnoreCase("medialist")) {
                //Liste der Medienobjekte
                jsonFolderMedialist(request, response);
            }
            if (type.equalsIgnoreCase("child")) {
                jsonFolderChilds(request, response);
            }
            if (type.equalsIgnoreCase("child2")) {
                jsonFolderChilds2(request, response);
            }

            if (type.equalsIgnoreCase("insertselected")) {
                insertSelected(request, response);
            }
            if (type.equalsIgnoreCase("setfolderimageselected")) {
                setFolderImage(request, response);
            }
            if (type.equalsIgnoreCase("move")) {
                move(request, response);
            }
            if (type.equalsIgnoreCase("copy")) {
                copy(request, response);
            }
        }
    }

    /**
     * TODO: Wird aktuell nicht verwendet, war geplant f�r pFolderEdit.jsp mit Jackson usw, siehe https://podio.com/stumpnercom/mediadesk-dev/apps/bugs/items/99
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        System.out.println("doPost: folderRestApi");

        if (request.getParameterNames().hasMoreElements()) {
            String jsonString = request.getParameterNames().nextElement();

            System.out.println("jsonString:" +jsonString);
        }

            //System.out.println("value= "+jsonString);

        //String id = this.getUriSection(4, request);

        Map<String,String> errorMap = new HashMap<String,String>();
            JSONObject responseObj = new JSONObject();
            if (errorMap.size()>0) {
                responseObj.put("errors", errorMap);
            }
            responseObj.put("message","Form data is going well");

            PrintWriter writer = response.getWriter();
            responseObj.write(writer);
        


    }

    /**
     * Daten (Einstellungen/Settings) des Ordners per Json raus scheiben
     * @param request
     * @param response
     */
    private void jsonFolderProperties(HttpServletRequest request, HttpServletResponse response, boolean editmode) throws IOException {

        int folderId = getUriSectionInt(4, request);

        FolderService folderService = new FolderService();
        try {
            FolderMultiLang folder = null;
            if (folderId!=0) { //0 = neuer Folder, den kann es ja noch nicht geben
                folder = (FolderMultiLang)folderService.getById(folderId);
            }

            if (editmode) {

                HttpSession session = request.getSession();
                FolderMultiLang sessionObject = (FolderMultiLang)session.getAttribute(FolderEditController.class.getName()+".FORM.command");

                if (sessionObject!=null) {
                    if (sessionObject.getFolderId()==sessionObject.getFolderId()) {
                        System.out.println("using edit mode");
                        folder = sessionObject;
                    }
                }

            }

            /*
            Map<String,String> jsonValueMap = new HashMap<String,String>(); //Hier werden die Werte gespeichert!!
            jsonValueMap.put("id",String.valueOf(folder.getFolderId()));
            jsonValueMap.put("name",folder.getName());
            jsonValueMap.put("titleLng1",folder.getTitleLng1());
            jsonValueMap.put("titleLng2",folder.getTitleLng2());
            */
            /*
            if (request.getServletContext().getAttribute("splashPageValueMap")!=null) {
                splashPageValueMap = (Map<String,String>)request.getServletContext().getAttribute("splashPageValueMap");
            } */

            JSONObject responseObj = new JSONObject();

            responseObj.put("id",String.valueOf(folder.getFolderId()));
            responseObj.put("name",folder.getName());
            responseObj.put("titleLng1",folder.getTitleLng1());
            responseObj.put("titleLng2",folder.getTitleLng2());
               /*
            for (String key : splashPageValueMap.keySet()) {
                responseObj.put(key,splashPageValueMap.get(key));
            }    */

            //System.out.println("out = "+responseObj.toString());


            PrintWriter writer = response.getWriter();
            responseObj.write(writer);

            /*
            PrintWriter out = response.getWriter();
            out.println("[\n");
            out.println("  {\"status\":\""+status+"\"}\n");
            out.println("]");*/

        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    private void copy(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int folderId = getUriSectionInt(4, request);
        int ivid = getUriSectionInt(6, request);

        String status = "undefined";
        FolderService folderService = new FolderService();
        try {
            folderService.addMediaToFolder(folderId, ivid);
            status = "OK";
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            status = "DUBLICATEENTRY";
        }

        PrintWriter out = response.getWriter();
        out.println("[\n");
        out.println("  {\"status\":\""+status+"\"}\n");
        out.println("]");

    }

    private void move(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int folderId = getUriSectionInt(4, request);
        int ivid = getUriSectionInt(6, request);
        int fromFolderId = getUriSectionInt(8, request);

        if (folderId==fromFolderId) {
            response.sendError(400,"Kann nich in den selben Ordner verschieben");
        }

        String status = "undefined";
        FolderService folderService = new FolderService();
        try {
            folderService.addMediaToFolder(folderId, ivid);
            folderService.deleteMediaFromFolder(fromFolderId, ivid);
            status = "OK";
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            status = "DUBLICATEENTRY";
        }

        PrintWriter out = response.getWriter();
        out.println("[\n");
        out.println("  {\"status\":\""+status+"\"}\n");
        out.println("]");
    }

    private void setFolderImage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("set Folder BasicMediaObject");
        List<MediaObject> list = MediaObjectService.getSelectedMediaObjectList(request.getSession());
        int folderId = getUriSectionInt(4, request);

        FolderService folderService = new FolderService();
        try {
            Folder c = folderService.getFolderById(folderId);
            if (list.size()>0) {
                MediaObject mo = list.get(0);
                c.setPrimaryIvid(mo.getIvid());
                System.out.println("set Folder BasicMediaObject");
                folderService.save(c);
            }
            MediaObjectService.deselectMedia(request);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void removeSelected(HttpServletRequest request, HttpServletResponse response) {

        User user = WebHelper.getUser(request);

        if (user.getRole()>=User.ROLE_EDITOR) {

            List<MediaObject> selectedList = MediaObjectService.getSelectedMediaObjectList(request.getSession());

            int folderId = getUriSectionInt(4, request);

            FolderService folderService = new FolderService();
            for (MediaObject mo : selectedList) {
                folderService.deleteMediaFromFolder(folderId,mo.getIvid());
            }

            MediaObjectService.deselectMedia(null, request);

        } else {
            try {
                response.sendError(403, "Keine Berechtigung");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void deleteSelected(HttpServletRequest request, HttpServletResponse response) {

        User user = WebHelper.getUser(request);

        if (user.getRole()>=User.ROLE_MASTEREDITOR) {

            List<MediaObject> selectedList = MediaObjectService.getSelectedMediaObjectList(request.getSession());

            MediaService mediaService = new MediaService();
            try {
                mediaService.deleteMediaObjects(selectedList);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            MediaObjectService.deselectMedia(null, request);

        } else {
            try {
                response.sendError(403, "Keine Berechtigung");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void insertSelected(HttpServletRequest request, HttpServletResponse response) {


        List<MediaObject> selectedList = MediaObjectService.getSelectedMediaObjectList(request.getSession());

        int folderId = getUriSectionInt(4, request);

        System.out.println("insert:"+folderId);

        FolderService folderService = new FolderService();
        for (MediaObject mo : selectedList) {
            try {
                System.out.println("insert media:"+mo.getIvid());
                folderService.addMediaToFolder(folderId, mo.getIvid());
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        MediaObjectService.deselectMedia(null, request);
    }

    private void jsonFolderChilds(HttpServletRequest request, HttpServletResponse response) throws IOException {

        AclFolderService folderService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(request));

        int folderId = getUriSectionInt(4, request);
        User user = WebHelper.getUser(request);

        boolean showCustomFolderIcons = false;
        if (user.getRole()>=User.ROLE_EDITOR) {
            showCustomFolderIcons = true;
        }

        try {
            List folderList = null;
            folderList = folderService.getFolderSubTree(folderId,0);
            Iterator folders = folderList.iterator();

            PrintWriter out = response.getWriter();
            out.println("[");

            while (folders.hasNext()) {

                Folder folder = (Folder)folders.next();
                String folderTitle = folder.getTitle();

                out.println("  {");
                out.println("    \"id\":\""+ folder.getFolderId()+"\",");
                out.println("    \"name\":\""+StringEscapeUtils.escapeJson(folder.getName())+"\",");
                out.println("    \"title\":\""+StringEscapeUtils.escapeJson(folderTitle)+"\",");
                out.println("    \"pivid\":\""+ folder.getPrimaryIvid()+"\",");
                out.println("    \"parent\":\""+ folder.getParent()+"\",");
                out.println("    \"text\":\""+StringEscapeUtils.escapeJson(folderTitle)+"\"");

                /*
            id: '2',
            name: 'ebene1.2',
            title: 'ebene1.2',
            settings: 'true',
            level: 0
                 */


                if (folders.hasNext()) {
                    out.println("  },");
                } else {
                    out.println("  }");
                }
            }

            out.println("]");

        //}
        } catch (ObjectNotFoundException e) {
            response.sendError(400, "Folder id "+folderId+" not found");
        } catch (IOServiceException e) {
            response.sendError(500, "IOServiceException: "+e.getMessage());
        }
    }

    /**
     * Mit Unterordner
     * @param request
     * @param response
     * @throws IOException
     */
    private void jsonFolderChilds2(HttpServletRequest request, HttpServletResponse response) throws IOException {

        AclFolderService folderService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(request));

        int folderId = getUriSectionInt(4, request);
        User user = WebHelper.getUser(request);

        boolean showCustomFolderIcons = false;
        if (user.getRole()>=User.ROLE_EDITOR) {
            showCustomFolderIcons = true;
        }

        try {
            List folderList = null;
            folderList = folderService.getFolderSubTree(folderId,0);
            Iterator folders = folderList.iterator();

            PrintWriter out = response.getWriter();
            out.println("[");

            while (folders.hasNext()) {

                Folder folder = (Folder)folders.next();
                String folderTitle = folder.getTitle();

                out.println("  {");
                out.println("    \"id\":\""+ folder.getFolderId()+"\",");
                out.println("    \"title\":\""+StringEscapeUtils.escapeJson(folderTitle)+"\",");

                List folderList2 = folderService.getFolderSubTree(folder.getFolderId(),0);
                StringBuffer sb = new StringBuffer();

                Iterator folders2 = folderList2.iterator();
                while (folders2.hasNext()) {
                    Folder cat2 = (Folder)folders2.next();
                    sb = sb.append("{");
                    sb = sb.append("\"folder\":\""+StringEscapeUtils.escapeJson(cat2.getTitle())+"\", \"id\":\""+cat2.getFolderId()+"\", \"info\":\"nix\"");
                    sb = sb.append("}");
                    if (folders2.hasNext()) {
                        sb = sb.append(", ");
                    }
                }


                out.println("    \"items\":["+sb.toString()+"],");
                out.println("    \"text\":\""+StringEscapeUtils.escapeJson(folderTitle)+"\"");


                if (folders.hasNext()) {
                    out.println("  },");
                } else {
                    out.println("  }");
                }
            }

            out.println("]");

        //}
        } catch (ObjectNotFoundException e) {
            response.sendError(400, "Folder id "+folderId+" not found");
        } catch (IOServiceException e) {
            response.sendError(500, "IOServiceException: "+e.getMessage());
        }
    }

    private void jsonFolderMedialist(HttpServletRequest request, HttpServletResponse response) {

        LngResolver lngResolver = new LngResolver();
        MediaService mediaService = new MediaService();
        mediaService.setUsedLanguage(lngResolver.resolveLng(request));

        //Loader-Class: definieren was geladen werden soll
        int folderId = getUriSectionInt(4, request);
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(folderId);
        try {
            if (request.getParameter("sortBy")!=null) { loaderClass.setSortBy(Integer.parseInt(request.getParameter("sortBy"))); }
            if (request.getParameter("orderBy")!=null) { loaderClass.setOrderBy(Integer.parseInt(request.getParameter("orderBy"))); }
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException bei sortBy oder orderBy Parameter in jsonFolderMedialist");
            e.printStackTrace();
        }
        List<MediaObjectMultiLang> mediaList = mediaService.getFolderMediaObjects(loaderClass);

        /** Ordner-Liste für den Thumbnail-View **/
        AclFolderService folderService = new AclFolderService(request);
        folderService.setUsedLanguage(lngResolver.resolveLng(request));

        try {
            PrintWriter out = response.getWriter();

            out.println("[");
            for (MediaObjectMultiLang mo : mediaList) {

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
                out.println("  \"name\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
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
                out.println("  \"editUrl\" : \"mediadetailedit?id="+mo.getIvid()+"\",");
                out.println("  \"shoppingCartUrl\" : \""+mo.getVersionName()+"\",");
                out.println("  \"lightboxUrl\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"mimeClass\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"previewUrl\" : \"wpreview?id="+mo.getIvid()+"\",");
                out.println("  \"note\" : \""+StringEscapeUtils.escapeJson(mo.getNote())+"\",");

                out.println("  \"downloadlink\" : \"download?download=ivid&ivid="+mo.getIvid()+"\",");

                //Daten für Sitzung (Ausgewählt)
                out.println("  \"fav\" : "+ (inFav ? "true" : "false") +"," );
                out.println("  \"cart\" : "+ (inCart ? "true" : "false") +"," );
                out.println("  \"selected\" : "+ (MediaObjectService.isSelected(mo, request) ? "true" : "false") );

                if (mediaList.indexOf(mo)<mediaList.size()-1) {
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
