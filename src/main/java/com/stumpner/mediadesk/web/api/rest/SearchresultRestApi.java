package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.core.database.sc.MediaSearchService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.search.*;
import com.stumpner.mediadesk.web.mvc.exceptions.SearchResultExpired;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;
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
 * Date: 08.10.2015
 * Time: 15:55:14
 * To change this template use File | Settings | File Templates.
 *
 * /api/rest/searchresult/...
 *   1   2   3              4
 */
public class SearchresultRestApi extends RestBaseServlet {

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("delete request on SearchresultRestApi: "+request.getRequestURI()+"?"+request.getQueryString());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String type = this.getUriSection(5, request);
        if (type!=null) {

            if (type.equalsIgnoreCase("deleteselected")) {
                deleteSelected(request, response);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("get request on SearchresultRestApi: "+request.getRequestURI()+"?"+request.getQueryString());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String type = this.getUriSection(5, request);
        if (type!=null) {

            if (type.equalsIgnoreCase("medialist")) {
                //Liste der Medienobjekte
                jsonFolderMedialist(request, response);
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
                e.printStackTrace();
            }

            MediaObjectService.deselectMedia(null, request);

        } else {
            try {
                response.sendError(403, "Keine Berechtigung");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void jsonFolderMedialist(HttpServletRequest request, HttpServletResponse response) {

        try {
            SearchResult searchResult = getSearchResultFromSession(0,Integer.MAX_VALUE, request);
            SearchResult imageList = searchResult;

            try {
                PrintWriter out = response.getWriter();

                out.println("[");
                for (MediaObjectMultiLang mo : imageList) {
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
                out.println("  \"editUrl\" : \"mediadetailedit?id="+mo.getIvid()+"\",");
                out.println("  \"shoppingCartUrl\" : \""+mo.getVersionName()+"\",");
                out.println("  \"lightboxUrl\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"mimeClass\" : \""+StringEscapeUtils.escapeJson(mo.getVersionName())+"\",");
                out.println("  \"previewUrl\" : \"wpreview?id="+mo.getIvid()+"\",");
                out.println("  \"note\" : \""+StringEscapeUtils.escapeJson(mo.getNote())+"\",");

                out.println("  \"downloadlink\" : \"download?download=ivid&ivid="+mo.getIvid()+"\",");

                    //Daten f�r Sitzung (Ausgew�hlt)
    //                out.println(true ? "" : "");
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

        } catch (SearchResultExpired searchResultExpired) {
            searchResultExpired.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static SearchResult getSearchResultFromSession(int viewPage, int pageSize, HttpServletRequest httpServletRequest) throws SearchResultExpired {
        /*
        Suche aus der Session Laden (suchergebnis muss bereits vorliegen)
        */
        MediaSearchService mediasearch = new MediaSearchService();
        LngResolver lngResolver = new LngResolver();
        mediasearch.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        String searchString = "";
        SearchResult searchResult = (SearchResult) httpServletRequest.getSession().getAttribute("search");
        if (searchResult==null) {
            throw new SearchResultExpired();
        }
        ISearchProperty sp = searchResult.getSearchProperty();
        if (sp instanceof MediaSearchProperty) {
            /* Erweiterte Suche */
            MediaSearchProperty ivsp = (MediaSearchProperty)sp;
            searchString = ivsp.getKeywords() + " " +ivsp.getPeople()+ " " +ivsp.getSite()+ " ";
            if (ivsp.getDateFrom()!=null) {
                searchString = searchString + " > " +
                        SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).format(ivsp.getDateFrom());
            }
            if (ivsp.getDateTo()!=null) {
                searchString = searchString + " - " +
                        SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).format(ivsp.getDateTo());
            }
        } else {
            searchString = sp.getKeywords();
        }

        if (searchResult.getSearchProperty() instanceof KeywordSearchProperty) {
            /* Keyword Suche */
            searchResult = mediasearch.getImageQuery(
                    (KeywordSearchProperty)searchResult.getSearchProperty(),viewPage,pageSize, WebHelper.getUser(httpServletRequest));

        } else if (searchResult.getSearchProperty() instanceof SimpleSearchProperty) {
            //simple search property (orphaned)
            searchResult = mediasearch.getOrphanedQuery(
                    (SimpleSearchProperty)searchResult.getSearchProperty(),viewPage,pageSize);
        } else {
            searchResult = mediasearch.getAdvancedImageQuery(
                    (MediaSearchProperty)searchResult.getSearchProperty(),viewPage,pageSize,WebHelper.getUser(httpServletRequest));
        }

        searchResult.setSearchString(searchString);
        return searchResult;

    }
}
