package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.MediaSearchService;
import com.stumpner.mediadesk.media.MediaObject;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.search.*;
import com.stumpner.mediadesk.web.mvc.exceptions.SearchResultExpired;
import com.stumpner.mediadesk.web.mvc.exceptions.LoadThumbnailException;
import com.stumpner.mediadesk.web.api.rest.SearchresultRestApi;
import com.stumpner.mediadesk.web.LngResolver;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.UnsupportedEncodingException;

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
 * Date: 20.05.2005
 * Time: 15:19:02
 * To change this template use File | Settings | File Templates.
 */
public class SearchResultController extends AbstractThumbnailAjaxController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        httpServletRequest.setCharacterEncoding("UTF-8");

        //im request kennzeichnen dass es sich um die such-seite handelt
        httpServletRequest.setAttribute("isSearch","true");

        UserService userService = new UserService();
        if (userService.processAutologin(httpServletRequest)) {
            System.out.println("autologin processed");
        }

        loadSearchResult(httpServletRequest);

        try {
            return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
        } catch (SearchResultExpired e) {
            logger.warn("aufruf eines abgelaufenen Suchergebnis");
            httpServletRequest.getSession().setAttribute("searchexpired",new Boolean(true));
            httpServletResponse.sendRedirect("/index/advancedsearch");
            return null;
        } catch (LoadThumbnailException e) {
            logger.warn("aufruf eines ungültigen Suchergebnis");
            httpServletRequest.getSession().setAttribute("searchexpired",new Boolean(true));
            httpServletResponse.sendRedirect("/index/advancedsearch");
            return null;
        }

    }

    protected boolean showSelect(HttpServletRequest request) {

        if (Config.quickDownload) {
            return this.getUser(request).getRole() >= User.ROLE_USER;
        } else {
            return this.getUser(request).getRole() > User.ROLE_EDITOR;
        }
    }

    protected List loadSearchResult(HttpServletRequest httpServletRequest) throws LoadThumbnailException {

        //angewählte Seite identifizieren
        int viewPage = 0;
        if (httpServletRequest.getParameter("page") != null) {
            try {
                viewPage = Integer.parseInt(httpServletRequest.getParameter("page")) - 1;
            } catch (NumberFormatException e) {
                //keine gültige nummer: nichts tun...
            }
        }

        SearchResult searchResult = null;
        if (httpServletRequest.getParameter("q") != null) {
            searchResult = getSearchResultFromPrepare(viewPage,httpServletRequest);
        } else {
            searchResult = getSearchResultFromSession(viewPage,httpServletRequest);
        }

        if (searchResult!=null) {
            httpServletRequest.setAttribute("searchString",searchResult.getSearchString());
            httpServletRequest.setAttribute("mediaCount",searchResult.getResultCount());
        } else {
            logger.warn("aufruf einer ungültigen Suche");
            throw new LoadThumbnailException();
        }

        return searchResult;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gibts das Suchergebnis zurück, das vorher mit einem Formular angestossen(erzeugt wurde)
     * @param viewPage
     * @param httpServletRequest
     * @return Suchergebnis
     */
    private SearchResult getSearchResultFromPrepare(int viewPage, HttpServletRequest httpServletRequest) {

        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        MediaSearchService mediasearch = new MediaSearchService();
        LngResolver lngResolver = new LngResolver();
        mediasearch.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        String searchString = ""; //Für die Angabe nach was gesucht wurde...
        SearchResult searchResult = new SearchResult();

        String query = "";
        String q = "";
        String people = "";
        String location = "";

            if (httpServletRequest.getParameter("q").length() != 0) {
                //keywords - suche...
                query = httpServletRequest.getParameter("q");
                q = query;

                searchString = query;

                //nullergebnis abfangen! --> aus session laden...
                if (query.equalsIgnoreCase("")) {
                    query = "";
                }

                if ((query.length() == 0) && (httpServletRequest.getSession().getAttribute("search") != null)) {
                    //suche aus session

                } else {
                    //suche durchführen...
                    if (httpServletRequest.getParameter("requery")==null) {
                        //suche durchführen...
                        KeywordSearchProperty ksp = new KeywordSearchProperty();
                        ksp.setKeywords(query);
                        searchResult = mediasearch.getImageQuery(ksp,viewPage,Integer.MAX_VALUE,this.getUser(httpServletRequest));
                        httpServletRequest.getSession().setAttribute("search", searchResult);
                    } else {
                        //requery: suche in suche
                        searchResult = (SearchResult) httpServletRequest.getSession().getAttribute("search");
                        if (searchResult==null) {
                            //kein Suchergebnis vorhaneden (möglicherweise url "blind" aufgerufen:
                            //null zurückgeben --> wird dann als "kein gültiges suchergebnis" interpretiert
                            return null;
                        }
                        ISearchProperty ksp = (ISearchProperty)searchResult.getSearchProperty();
                        ksp.setKeywords(query);
                        searchResult = mediasearch.getReQuery(ksp,viewPage,Integer.MAX_VALUE);
                    }
                }

            } else {

                if (httpServletRequest.getParameter("orphan")!=null) {

                    searchString = "Orphaned";

                    ISearchProperty osp = new SimpleSearchProperty();

                    searchResult = mediasearch.getOrphanedQuery(osp,viewPage,Integer.MAX_VALUE);
                    httpServletRequest.getSession().setAttribute("search", searchResult);

                } else {


                    //advanced
                    int filledInValues = 0;

                    MediaSearchProperty ksp = new MediaSearchProperty();
                    if (httpServletRequest.getParameter("people")!=null) {
                        ksp.setPeople(httpServletRequest.getParameter("people"));
                        if (httpServletRequest.getParameter("people").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("location")!=null) {
                        ksp.setSite(httpServletRequest.getParameter("location"));
                        if (httpServletRequest.getParameter("location").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("keywords")!=null) {
                        ksp.setKeywords(httpServletRequest.getParameter("keywords"));
                        if (httpServletRequest.getParameter("keywords").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("period")!=null) {
                        if (!httpServletRequest.getParameter("period").startsWith("?")) { //AngularJS generiert ?,siehe http://stackoverflow.com/questions/32474460/initializing-select-with-ng-options-ng-repeat-and-ng-selected-in-angularjs 
                            ksp.setPeriod(Integer.parseInt(httpServletRequest.getParameter("period")));
                            switch (ksp.getPeriod()) {
                                case 0: httpServletRequest.removeAttribute("searchMessage"); break;
                                case 1: httpServletRequest.setAttribute("searchMessage","mediasearch.24hours"); break;
                                case 2: httpServletRequest.setAttribute("searchMessage","mediasearch.lastweek"); break;
                                case 3: httpServletRequest.setAttribute("searchMessage","mediasearch.lastmonth"); break;
                                case 4: httpServletRequest.setAttribute("searchMessage","mediasearch.12month"); break;
                                case -1:
                                    //special: use dateFrom / dateTo
                                    ksp.setPeriod(0);
                                    ksp.setDateFrom(getInputDate(httpServletRequest.getParameter("dateFrom"),false));
                                    ksp.setDateTo(getInputDate(httpServletRequest.getParameter("dateTo"),true));
                                    filledInValues++;
                                    break;
                            }
                            if (httpServletRequest.getParameter("period").length()>0) filledInValues++;
                        }
                    }
                    if (httpServletRequest.getParameter("photographer")!=null) {
                        ksp.setPhotographerAlias("%"+httpServletRequest.getParameter("photographer")+"%");
                        if (httpServletRequest.getParameter("photographer").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("orientation")!=null) {
                        ksp.setOrientation(Integer.parseInt(httpServletRequest.getParameter("orientation")));
                        if (httpServletRequest.getParameter("orientation").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("perspective")!=null) {
                        ksp.setPerspective(Integer.parseInt(httpServletRequest.getParameter("perspective")));
                        if (httpServletRequest.getParameter("perspective").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("motive")!=null) {
                        ksp.setMotive(Integer.parseInt(httpServletRequest.getParameter("motive")));
                        if (httpServletRequest.getParameter("motive").length()>0) filledInValues++;
                    }
                    if (httpServletRequest.getParameter("gesture")!=null) {
                        ksp.setGesture(Integer.parseInt(httpServletRequest.getParameter("gesture")));
                        if (httpServletRequest.getParameter("gesture").length()>0) filledInValues++;
                    }
                    //CustomList Fields
                    if (httpServletRequest.getParameter("customList1")!=null) {
                        ksp.setCustomList1(Integer.parseInt(httpServletRequest.getParameter("customList1")));
                        filledInValues++;
                    }
                    if (httpServletRequest.getParameter("customList2")!=null) {
                        ksp.setCustomList2(Integer.parseInt(httpServletRequest.getParameter("customList2")));
                        filledInValues++;
                    }
                    if (httpServletRequest.getParameter("customList3")!=null) {
                        ksp.setCustomList3(Integer.parseInt(httpServletRequest.getParameter("customList3")));
                        filledInValues++;
                    }
                    if (httpServletRequest.getParameter("licValid")!=null) {
                        if (!httpServletRequest.getParameter("licValid").isEmpty()) {
                            ksp.setLicValid(getInputDate(httpServletRequest.getParameter("licValid"), false));
                            filledInValues++;
                        }
                    }



                    //prüfen ob irgendwas eingegeben wurde
                    if (filledInValues==0) {
                        //suche nach nichts!
                        ksp.setSite("123456");
                    }

                    searchString = ksp.getKeywords() + " " +ksp.getPeople()+ " " +ksp.getSite()+ " ";

                    searchResult = mediasearch.getAdvancedImageQuery(ksp,viewPage,Integer.MAX_VALUE,this.getUser(httpServletRequest));
                    httpServletRequest.getSession().setAttribute("search", searchResult);
                }
            }

        searchResult.setSearchString(searchString);

        if (Config.searchPerEmail>0) {
            if (httpServletRequest.getAttribute("searchLogged")==null) {
                //In den SearchLogger schreiben...
                SearchEntity searchEntity = new SearchEntity();
                searchEntity.setQ(searchString);
                searchEntity.setHostname(httpServletRequest.getRemoteHost());
                searchEntity.setTime(new Date());
                SearchLogger.getInstance().log(searchEntity);
                httpServletRequest.setAttribute("searchLogged",new Boolean(true));
                //System.out.println("Email wird versendet...");
            }
        }

        return searchResult;

    }

    /**
     * Gibt das Suchergebnis zurück, die such-properties (wonach gesucht wird) müssen sich in der session befinden!
     * Wird für <param>viewPage</param> der Wert -1 angegeben, wird das komplette Suchergebnis in der liste zurückgegeben.
     * Ansonsten nur die Bilder für die aktuelle Seite.
     * @param viewPage
     * @param httpServletRequest
     * @return
     * @throws SearchResultExpired
     */
    private SearchResult getSearchResultFromSession(int viewPage, HttpServletRequest httpServletRequest) throws SearchResultExpired {

        return SearchresultRestApi.getSearchResultFromSession(viewPage, Integer.MAX_VALUE, httpServletRequest);

    }

    private Date getInputDate(String dateString, boolean endday) {

        Logger logger = Logger.getLogger(SearchResultController.class);
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            date = sdf.parse(dateString);
            if (endday) {
                date.setHours(23);
                date.setMinutes(59);
            }
            logger.debug("Suche: Parse Date: "+dateString+" To: "+date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return date;
    }

    protected void insert(MediaObject image, HttpServletRequest request) {
        //nicht möglich bei einer suche!
    }

    protected void remove(MediaObject image, HttpServletRequest request) {
        //nicht möglich bei einer suche!
    }


    /**
     * String damit dieses Suchergebnis eindeutig zugewiesen werden kann...
     * @param request
     * @return Eindeutiger String für diese Datenmenge
     */
    protected String getPopupCacheEquation(HttpServletRequest request) {
        String equation = super.getPopupCacheEquation(request);    //To change body of overridden methods use File | Settings | File Templates.
        SearchResult searchResult = (SearchResult) request.getSession().getAttribute("search");
        return (equation+searchResult.getSearchProperty().getSuid());
    }

    protected List loadAllImageList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoadThumbnailException {

        SearchResult searchResult = getSearchResultFromSession(-1,httpServletRequest);
        logger.debug("SearchResultController.loadAllImageList, list-size:"+searchResult.size());
        return searchResult;
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "search";
    }

    protected int getImageCount(HttpServletRequest request) {

        Logger logger = Logger.getLogger(SearchResultController.class);
        //Anzahl der gefundenen images zurückgeben:
        if (request.getSession().getAttribute("search")!=null) {
            SearchResult searchResult = (SearchResult)request.getSession().getAttribute("search");
            return searchResult.getResultCount();
        } else {
            //Keine Suche in Session
            logger.error("getMediaCount failed, kein Suchergebnis in der Session gespeichert!?");
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
