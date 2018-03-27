package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.image.ImageVersion;

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
 * /api/rest/folder/<folderid>/move/{ivid}/from/{categoryid}
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
                jsonCategoryProperties(request, response, editmode);
            }

            if (type.equalsIgnoreCase("medialist")) {
                //Liste der Medienobjekte
                jsonCategoryMedialist(request, response);
            }
            if (type.equalsIgnoreCase("child")) {
                jsonCategoryChilds(request, response);
            }
            if (type.equalsIgnoreCase("child2")) {
                jsonCategoryChilds2(request, response);
            }

            /*
            if (type.equalsIgnoreCase("removeselected")) {
                removeSelected(request, response);
            }

            if (type.equalsIgnoreCase("deleteselected")) {
                deleteSelected(request, response);
            } */
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

        System.out.println("doPost: categoryRestApi");

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
     * Daten (Einstellungen/Settings) der Kategorie/des Ordners per Json raus scheiben
     * @param request
     * @param response
     */
    private void jsonCategoryProperties(HttpServletRequest request, HttpServletResponse response, boolean editmode) throws IOException {

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
                    if (sessionObject.getCategoryId()==sessionObject.getCategoryId()) {
                        System.out.println("using edit mode");
                        folder = sessionObject;
                    }
                }

            }

            /*
            Map<String,String> jsonValueMap = new HashMap<String,String>(); //Hier werden die Werte gespeichert!!
            jsonValueMap.put("id",String.valueOf(folder.getCategoryId()));
            jsonValueMap.put("name",folder.getCatName());
            jsonValueMap.put("titleLng1",folder.getCatTitleLng1());
            jsonValueMap.put("titleLng2",folder.getCatTitleLng2());
            */
            /*
            if (request.getServletContext().getAttribute("splashPageValueMap")!=null) {
                splashPageValueMap = (Map<String,String>)request.getServletContext().getAttribute("splashPageValueMap");
            } */

            JSONObject responseObj = new JSONObject();

            responseObj.put("id",String.valueOf(folder.getCategoryId()));
            responseObj.put("name",folder.getCatName());
            responseObj.put("titleLng1",folder.getCatTitleLng1());
            responseObj.put("titleLng2",folder.getCatTitleLng2());
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

        System.out.println("set Folder Image");
        List<ImageVersion> list = MediaObjectService.getSelectedImageList(request.getSession());
        int categoryId = getUriSectionInt(4, request);

        FolderService folderService = new FolderService();
        try {
            Folder c = folderService.getFolderById(categoryId);
            if (list.size()>0) {
                ImageVersion mo = list.get(0);
                c.setPrimaryIvid(mo.getIvid());
                System.out.println("set Folder Image");
                folderService.save(c);
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void removeSelected(HttpServletRequest request, HttpServletResponse response) {

        User user = WebHelper.getUser(request);

        if (user.getRole()>=User.ROLE_EDITOR) {

            List<ImageVersion> selectedList = MediaObjectService.getSelectedImageList(request.getSession());

            int categoryId = getUriSectionInt(4, request);

            FolderService folderService = new FolderService();
            for (ImageVersion mo : selectedList) {
                folderService.deleteMediaFromFolder(categoryId,mo.getIvid());
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

            List<ImageVersion> selectedList = MediaObjectService.getSelectedImageList(request.getSession());

            ImageVersionService imageService = new ImageVersionService();
            try {
                imageService.deleteImageVersions(selectedList);
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


        List<ImageVersion> selectedList = MediaObjectService.getSelectedImageList(request.getSession());

        int categoryId = getUriSectionInt(4, request);

        System.out.println("insert:"+categoryId);

        FolderService folderService = new FolderService();
        for (ImageVersion mo : selectedList) {
            try {
                System.out.println("insert media:"+mo.getIvid());
                folderService.addMediaToFolder(categoryId, mo.getIvid());
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        MediaObjectService.deselectMedia(null, request);
    }

    private void jsonCategoryChilds(HttpServletRequest request, HttpServletResponse response) throws IOException {

        AclFolderService categoryService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));

        int categoryId = getUriSectionInt(4, request);
        User user = WebHelper.getUser(request);

        boolean showCustomFolderIcons = false;
        if (user.getRole()>=User.ROLE_EDITOR) {
            showCustomFolderIcons = true;
        }

        try {
        List folderList = null;
        //if (user.getUserId()==-1) { //Gast - Public
            //if (application.getAttribute("publicCategoryListTime"+node)==null) {
            //    folderList = categoryService.getFolderSubTree(categoryId,0);
            //    application.setAttribute("publicCategoryList"+node, folderList);
            //    application.setAttribute("publicCategoryListTime"+node, System.currentTimeMillis());
                // System.out.println("jsonCategory.jsp: fill cache"+node);
            //} else {
                //System.out.println("jsonCategory.jsp: using cache "+node);
            //    folderList = (List)application.getAttribute("publicCategoryList"+node);
            //    long lastUpdate = (Long)application.getAttribute("publicCategoryListTime"+node);
            //    if (System.currentTimeMillis()-lastUpdate > 10000) { //Millisekunden zum cachen
            //        application.removeAttribute("publicCategoryListTime"+node);
            //    }
            //}
        //} else {
            folderList = categoryService.getFolderSubTree(categoryId,0);
            //folderList = folderList.subList(0,5);
            Iterator folders = folderList.iterator();

            PrintWriter out = response.getWriter();
            //out.println("{\"records\": [");
            out.println("[");

            while (folders.hasNext()) {

                boolean checkedValue = false;
                Folder folder = (Folder)folders.next();
                String categoryTitle = folder.getCatTitle(); //folder.getCatTitle().replace('"',' ');

                out.println("  {");
                out.println("    \"id\":\""+ folder.getCategoryId()+"\",");
                out.println("    \"name\":\""+StringEscapeUtils.escapeJson(folder.getCatName())+"\",");
                out.println("    \"title\":\""+StringEscapeUtils.escapeJson(categoryTitle)+"\",");
                out.println("    \"pivid\":\""+ folder.getPrimaryIvid()+"\",");
                out.println("    \"parent\":\""+ folder.getParent()+"\",");
                //out.println("    \"items\":[{\"folder\":\"a\", \"id\":\"1\", \"info\":\"nix\"}],");
                out.println("    \"text\":\""+StringEscapeUtils.escapeJson(categoryTitle)+"\"");

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
            response.sendError(400, "Folder id "+categoryId+" not found");
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
    private void jsonCategoryChilds2(HttpServletRequest request, HttpServletResponse response) throws IOException {

        AclFolderService categoryService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));

        int categoryId = getUriSectionInt(4, request);
        User user = WebHelper.getUser(request);

        boolean showCustomFolderIcons = false;
        if (user.getRole()>=User.ROLE_EDITOR) {
            showCustomFolderIcons = true;
        }

        try {
        List folderList = null;
        //if (user.getUserId()==-1) { //Gast - Public
            //if (application.getAttribute("publicCategoryListTime"+node)==null) {
                //folderList = categoryService.getFolderSubTree(categoryId,0);
            //    application.setAttribute("publicCategoryList"+node, folderList);
            //    application.setAttribute("publicCategoryListTime"+node, System.currentTimeMillis());
                // System.out.println("jsonCategory.jsp: fill cache"+node);
            //} else {
                //System.out.println("jsonCategory.jsp: using cache "+node);
            //    folderList = (List)application.getAttribute("publicCategoryList"+node);
            //    long lastUpdate = (Long)application.getAttribute("publicCategoryListTime"+node);
            //    if (System.currentTimeMillis()-lastUpdate > 10000) { //Millisekunden zum cachen
            //        application.removeAttribute("publicCategoryListTime"+node);
            //    }
            //}
        //} else {
            folderList = categoryService.getFolderSubTree(categoryId,0);
            //folderList = folderList.subList(0,5);
            Iterator folders = folderList.iterator();

            PrintWriter out = response.getWriter();
            //out.println("{\"records\": [");
            out.println("[");

            while (folders.hasNext()) {

                boolean checkedValue = false;
                Folder folder = (Folder)folders.next();
                String categoryTitle = folder.getCatTitle(); // folder.getCatTitle().replace('"',' ');

                out.println("  {");
                out.println("    \"id\":\""+ folder.getCategoryId()+"\",");
                out.println("    \"title\":\""+StringEscapeUtils.escapeJson(categoryTitle)+"\",");

                List folderList2 = categoryService.getFolderSubTree(folder.getCategoryId(),0);
                StringBuffer sb = new StringBuffer();

                Iterator folders2 = folderList2.iterator();
                while (folders2.hasNext()) {
                    Folder cat2 = (Folder)folders2.next();
                    sb = sb.append("{");
                    sb = sb.append("\"folder\":\""+StringEscapeUtils.escapeJson(cat2.getCatTitle())+"\", \"id\":\""+cat2.getCategoryId()+"\", \"info\":\"nix\"");
                    sb = sb.append("}");
                    if (folders2.hasNext()) {
                        sb = sb.append(", ");
                    }
                }


                out.println("    \"items\":["+sb.toString()+"],");
                out.println("    \"text\":\""+StringEscapeUtils.escapeJson(categoryTitle)+"\"");


                if (folders.hasNext()) {
                    out.println("  },");
                } else {
                    out.println("  }");
                }
            }

            out.println("]");

        //}
        } catch (ObjectNotFoundException e) {
            response.sendError(400, "Folder id "+categoryId+" not found");
        } catch (IOServiceException e) {
            response.sendError(500, "IOServiceException: "+e.getMessage());
        }
    }

    private void jsonCategoryMedialist(HttpServletRequest request, HttpServletResponse response) {

        LngResolver lngResolver = new LngResolver();
        ImageVersionService imageService = new ImageVersionService();
        imageService.setUsedLanguage(lngResolver.resolveLng(request));

        //Loader-Class: definieren was geladen werden soll
        int categoryId = getUriSectionInt(4, request);
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(categoryId);
        try {
            if (request.getParameter("sortBy")!=null) { loaderClass.setSortBy(Integer.parseInt(request.getParameter("sortBy"))); }
            if (request.getParameter("orderBy")!=null) { loaderClass.setOrderBy(Integer.parseInt(request.getParameter("orderBy"))); }
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException bei sortBy oder orderBy Parameter in jsonCategoryMedialist");
            e.printStackTrace();
        }
        List<ImageVersionMultiLang> imageList = imageService.getCategoryImages(loaderClass);

        /** Kategorie-Liste f�r den Thumbnail-View **/
        AclFolderService categoryService = new AclFolderService(request);
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));

        try {
            PrintWriter out = response.getWriter();

            out.println("[");
            for (ImageVersionMultiLang mo : imageList) {

                //ShoppingCart Logik (true/false) ob dieses Medienobjekt im Cart ist
                ShoppingCartService cartService = new ShoppingCartService();
                LightboxService lightboxService = new LightboxService();
                User user = WebHelper.getUser(request);
                boolean inCart = false;
                boolean inFav = false;
                if (user.getUserId()!= UserFactory.createVisitorUser().getUserId()) {
                    //Wenn es sich um einen eingeloggten Benutzer handelt, hat er einen Warenkorb
                    if (Config.useShoppingCart) {
                        inCart = cartService.isInCart(user.getUserId(), mo.getIvid());
                    }
                    if (Config.useLightbox) {
                        inFav = lightboxService.isInFav(user.getUserId(), mo.getIvid());
                    }
                }

                out.println(" {");
                out.println("  \"ivid\" : "+mo.getIvid()+",");
                out.println("  \"caption\" : \""+StringEscapeUtils.escapeJson(getCaption(mo))+"\",");
                out.println("  \"name\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"imagenumber\" : \""+StringEscapeUtils.escapeJson(mo.getImageNumber())+"\",");
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
