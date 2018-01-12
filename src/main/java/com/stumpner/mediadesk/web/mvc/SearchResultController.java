package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.core.database.sc.ImageSearchService;
import com.stumpner.mediadesk.core.database.sc.LightboxService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.search.*;
import com.stumpner.mediadesk.web.mvc.exceptions.SearchResultExpired;
import com.stumpner.mediadesk.web.mvc.exceptions.LoadThumbnailException;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;
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

    protected MediaMenu mediaMenuBaker(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MediaMenu mediaMenu = getMediaMenu(request);

        mediaMenu.setVisible(true);
        mediaMenu.setView(true);

        if (getUser(request).getRole()>=User.ROLE_USER) {

            mediaMenu.setSelection(true);
            mediaMenu.setSelectionMarkAll(true);
            mediaMenu.setSelectionMarkSite(true);
            mediaMenu.setSelectionUnmarkAll(true);
            if (Config.quickDownload) mediaMenu.setDownloadSelected(true);

            if (getUser(request).getRole()>=User.ROLE_EDITOR) {
                mediaMenu.setSelectionDeleteMedia(true);
                if (Config.pinPicEnabled) { mediaMenu.setSelectionToPin(true); }
            }

            if (getUser(request).getRole()>=User.ROLE_ADMIN) {
                mediaMenu.setBulkModification(true);
            }

        }

        return mediaMenu;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        httpServletRequest.setCharacterEncoding("UTF-8");

        //todo: für diese klasse eine gemeinsame basisklass mit FolderViewController machen
        //im request kennzeichnen dass es sich um die such-seite handelt
        httpServletRequest.setAttribute("isSearch","true");

        UserService userService = new UserService();
        if (userService.processAutologin(httpServletRequest)) {
            System.out.println("autologin processed");
        }

        String searchString = ""; //Für die Angabe nach was gesucht wurde...

        String query = "";
        String q = "";
        String people = "";
        String location = "";

        ImageSearchService imageSearch = new ImageSearchService();

        if (httpServletRequest.getParameter("lightbox") != null) {

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user") != null) {
                //user eingeloggt
                User user = (User) session.getAttribute("user");
                LightboxService lightboxService = new LightboxService();
                if (httpServletRequest.getParameter("lightbox").equals("add")) {
                    //hinzufügen
                    lightboxService.addImageToLightbox(Integer.parseInt((String) httpServletRequest.getParameter("ivid")), user.getUserId());
                } else {
                    //löschen
                    lightboxService.removeImageToLightbox(Integer.parseInt((String) httpServletRequest.getParameter("ivid")), user.getUserId());
                }
            }
        }
        if (httpServletRequest.getParameter("shoppingCart") != null) {

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user") != null) {
                //user eingeloggt
                User user = (User) session.getAttribute("user");
                ShoppingCartService shoppingCartService = new ShoppingCartService();
                if (httpServletRequest.getParameter("shoppingCart").equals("add")) {
                    //hinzufügen
                    shoppingCartService.addImageToShoppingCart(Integer.parseInt((String) httpServletRequest.getParameter("ivid")), user.getUserId());
                }
            }
        }

        this.setContentTemplateFile("searchresult.jsp", httpServletRequest);

        //boolean expired = false;

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

    protected List loadThumbnailImageList(int sortBy, int orderBy, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoadThumbnailException {

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

        if (searchResult.getResultCount()==1000) {
            httpServletRequest.setAttribute("reduced",true);
        } else {
            httpServletRequest.setAttribute("reduced",false);
        }

        if (searchResult!=null) {
            httpServletRequest.setAttribute("searchString",searchResult.getSearchString());
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
        ImageSearchService imageSearch = new ImageSearchService();
        LngResolver lngResolver = new LngResolver();
        imageSearch.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
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
                        searchResult = imageSearch.getImageQuery(ksp,viewPage,getImageCountPerPage(),this.getUser(httpServletRequest));
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
                        searchResult = imageSearch.getReQuery(ksp,viewPage,getImageCountPerPage());
                    }
                }

            } else {

                if (httpServletRequest.getParameter("orphan")!=null) {

                    searchString = "Orphaned";

                    ISearchProperty osp = new SimpleSearchProperty();

                    searchResult = imageSearch.getOrphanedQuery(osp,viewPage,getImageCountPerPage());
                    httpServletRequest.getSession().setAttribute("search", searchResult);

                } else {


                    //advanced
                    int filledInValues = 0;

                    ImageVersionSearchProperty ksp = new ImageVersionSearchProperty();
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
                                case 1: httpServletRequest.setAttribute("searchMessage","imagesearch.24hours"); break;
                                case 2: httpServletRequest.setAttribute("searchMessage","imagesearch.lastweek"); break;
                                case 3: httpServletRequest.setAttribute("searchMessage","imagesearch.lastmonth"); break;
                                case 4: httpServletRequest.setAttribute("searchMessage","imagesearch.12month"); break;
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
                        ksp.setLicValid(getInputDate(httpServletRequest.getParameter("licValid"),false));
                        filledInValues++;
                    }



                    //prüfen ob irgendwas eingegeben wurde
                    if (filledInValues==0) {
                        //suche nach nichts!
                        ksp.setSite("123456");
                    }

                    searchString = ksp.getKeywords() + " " +ksp.getPeople()+ " " +ksp.getSite()+ " ";

                    searchResult = imageSearch.getAdvancedImageQuery(ksp,viewPage,getImageCountPerPage(),this.getUser(httpServletRequest));
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

        return SearchresultRestApi.getSearchResultFromSession(viewPage, getImageCountPerPage(), httpServletRequest);

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

    protected void insert(ImageVersion image, HttpServletRequest request) {
        //nicht möglich bei einer suche!
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {
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
            logger.error("getImageCount failed, kein Suchergebnis in der Session gespeichert!?");
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    /*
    protected int getNumberOfPages(List imageList) {

        Logger logger = Logger.getLogger(SearchResultController.class);
        logger.debug("Anzahl der Ergebnis-Seiten bereichnen: ");

        SearchResult result = (SearchResult)imageList;
        int imageCount = result.getResultCount();
        int pages = imageCount / getImageCountPerPage();
        logger.debug("getNumberOfPages: "+imageCount+" / "+getImageCountPerPage()+" = "+pages);
        if (imageCount%getImageCountPerPage()>0) { pages++; }
        return pages;
    }

    protected int getNextPage(List imageList, HttpServletRequest request) {
        int actualPage = this.getPageIndex(request);
        if (actualPage<getNumberOfPages(imageList)) {
            //nächste seite existiert
            return actualPage+1;
        } else {
            return 0;
        }
    }

    protected int getPrevPage(List imageList, HttpServletRequest request) {
        int actualPage = this.getPageIndex(request);
        if (actualPage>0) {
            return actualPage-1;
        } else {
            return actualPage;
        }
    }

        */

}
