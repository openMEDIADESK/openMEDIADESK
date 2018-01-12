package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Iterator;

import com.stumpner.mediadesk.web.mvc.exceptions.LoadThumbnailException;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.category.CategoryTreeElement;
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
 * Kontroller der die Dateiauflistung bzw. Thumbnailauflistung bereitstellt. Hier wird auch das Bildmenü abgebildet
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 08.01.2007
 * Time: 17:45:01
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractThumbnailViewController extends AbstractImageActionController {

    protected static String TEMPLATEFILE_THUMBNAILS = "listThumbnails.jsp";
    protected static String TEMPLATEFILE_LISTVIEW = "listFiles.jsp";

    protected static String TEMPLATEFILE_DETAILVIEW = "listThumbnailDetail.jsp";

    protected MediaMenu mediaMenuBaker(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new MediaMenu();
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ModelAndView modelAndView = super.handleRequestInternal(httpServletRequest, httpServletResponse);
        //Image-Menü erstellen
        httpServletRequest.setAttribute("mediaMenu", mediaMenuBaker(this.getUser(httpServletRequest),httpServletRequest,httpServletResponse));

        List imageList = null;
        imageList = this.loadThumbnailImageList(httpServletRequest,httpServletResponse);
        httpServletRequest.setAttribute("mediaObjectList",imageList);        

        int pageIndex = getPageIndex(httpServletRequest);
        httpServletRequest.setAttribute("pageSize",Integer.toString(getPageSize(imageList)));
        httpServletRequest.setAttribute("pageIndex",Integer.toString(pageIndex));

        int numberOfPages = getNumberOfPages(loadAllImageList(httpServletRequest,httpServletResponse));
        httpServletRequest.setAttribute("numberOfPages",Integer.toString(numberOfPages));

        /*String nextPage = Integer.toString(getNextPage(imageList,httpServletRequest));
        String prevPage = Integer.toString(getPrevPage(imageList,httpServletRequest));*/
        String nextPage = "0";
        String prevPage = "0";
        if (pageIndex>0) { prevPage = Integer.toString(pageIndex-1); }
        if (pageIndex<numberOfPages) { nextPage = Integer.toString(pageIndex+1); }
        httpServletRequest.setAttribute("nextPage",nextPage);
        httpServletRequest.setAttribute("prevPage",prevPage);

        httpServletRequest.setAttribute("imageCount", Integer.toString(getImageCount(httpServletRequest)));

        httpServletRequest.setAttribute("servletMapping",getServletMapping(httpServletRequest));
        httpServletRequest.setAttribute("containerId",Integer.toString(getContainerId(httpServletRequest)));
        httpServletRequest.setAttribute("showSelect",new Boolean(showSelect(httpServletRequest)));
        httpServletRequest.setAttribute("showSendImage",new Boolean(Config.showSendImage));
        httpServletRequest.setAttribute("inlinePreview",new Boolean(Config.inlinePreview));

        //Damit im Popup ein WEITER / ZURÜCK-BUTTON dargestellt werden kann,
        //müssen hier die Bilder in dieser ansicht in de Session gespeichert werden:
        this.organizePopupAllImageList(httpServletRequest,httpServletResponse);

        //Listen-Ansicht festlegen
        httpServletRequest.setAttribute("listView",getListViewJsp(httpServletRequest));
        httpServletRequest.setAttribute("detailView",getDetailViewJsp(httpServletRequest));
        //Neue GUI verwende nur mehr view (1=
        httpServletRequest.setAttribute("view", getView(httpServletRequest));

                //Preis anzeigen wenn Bezahlsystem aktiv
        if (!Config.currency.isEmpty()) {
            httpServletRequest.setAttribute("thumbnailShowPrice", true);
            httpServletRequest.setAttribute("currency", Config.currency);
        }

        //ThumbnailListObject speichern
        //httpServletRequest.getSession().setAttribute("thumbnailListObject",this.getThumbnailListObject(httpServletRequest));

        return modelAndView;    //To change body of overridden methods use File | Settings | File Templates.
    }

    private String getView(HttpServletRequest request) {

        if (getListViewCustom(request)!=null) {
            if (getListViewCustom(request).equalsIgnoreCase(TEMPLATEFILE_LISTVIEW)) {
                return "list";
            } else if (getListViewCustom(request).equalsIgnoreCase(TEMPLATEFILE_THUMBNAILS)) {
                return "thumbnails";
            } else {
                return getListViewCustom(request);
            }
        }

        return "auto";
    }

    protected String getDetailViewJsp(HttpServletRequest request) {
        return TEMPLATEFILE_DETAILVIEW;
    }

    /**
     * Gibt das JSP-Files für die Auflistung zurück:
     * <ul>
     * <li> listFiles.jsp: Dateiliste</li>
     * <li> listThumbnails.jsp: Bildanzeige/Thumbnails</li>
     * </ul>
     * @return
     */
    protected String getListViewJsp(HttpServletRequest request) {

        /*
        System.out.println("ServletMapping: "+this.getServletMapping(request));
        System.out.println("getPathInfo: "+request.getPathInfo());
        System.out.println("getPathTranslated: "+request.getPathTranslated());
        System.out.println("getRequestURI: "+request.getRequestURI());
        System.out.println("getRequestURL: "+request.getRequestURL());  */
        
        if (request.getPathInfo().toUpperCase().endsWith("L")) {
            //List-View
            return TEMPLATEFILE_LISTVIEW;
        }
        if (request.getPathInfo().toUpperCase().endsWith("T") && !request.getPathInfo().toUpperCase().endsWith("CAT")) {
            //Thumbnail-View
            return TEMPLATEFILE_THUMBNAILS;
        }

        if (getListViewCustom(request)!=null) {
            return getListViewCustom(request);
        }

        return getAutoDetectedView(request);
    }

    protected String getListViewCustom(HttpServletRequest request) {
        return null;
    }

    private String getAutoDetectedView(HttpServletRequest request) {

        List mediaList = (List)request.getAttribute("mediaObjectList");
        Iterator medias = mediaList.iterator();
        //Wenn alle MimeType = image dann TEMPLATEFILE_THUMBNAILS sonst TEMPLATEFILE_LISTVIEW
        boolean showThumbnails = true;
        int mediaObjectCount = 0; //Anzahl der Medienobjekte (ohne Unterordner)
        int imagesCount = 0; //Anzahl der Bilder oder Videos
        int catWithPrimaryIvidCount = 0; //Anzahl Unterordner mit Vorschaubild
        while (medias.hasNext()) {
            Object obj = medias.next();
            if (obj instanceof ImageVersion) {
                ImageVersion media = (ImageVersion)obj;
                if (media.getMayorMime().equalsIgnoreCase("image") || media.getMayorMime().equalsIgnoreCase("video")) {
                    //Medienobjekt = Bild
                    imagesCount = imagesCount+1;
                    mediaObjectCount = mediaObjectCount+1;
                } else {
                    mediaObjectCount = mediaObjectCount+1;
                }
            }
            if (obj instanceof CategoryTreeElement) {
                CategoryTreeElement cte = (CategoryTreeElement)obj;
                if (cte.getPrimaryIvid()!=0) {
                    catWithPrimaryIvidCount = catWithPrimaryIvidCount+1;
                }
            }
        }

        if ( ((imagesCount==mediaObjectCount) && (mediaObjectCount!=0)) || catWithPrimaryIvidCount>0) {
            //Nur Bilder (ic=mc abder nicht 0) oder mindestens ein Unterordner mit Bild
            showThumbnails = true;
        } else {
            showThumbnails = false;
        }

        if (showThumbnails) {
            return TEMPLATEFILE_THUMBNAILS;
        } else {
            return TEMPLATEFILE_LISTVIEW;
        }

    }

    /**
     * Überprüft ob die Image-Liste dieser ansicht für das Popup WEITER bereits
     * in der Session gespeichert ist, wenn nicht wird es in das Session-Objekt
     * unter dem Key thumbnailList gespeichert.
     * @param request
     * @param response
     */
    private void organizePopupAllImageList(HttpServletRequest request, HttpServletResponse response) {

        Logger logger = Logger.getLogger(AbstractThumbnailViewController.class);
        HttpSession session = request.getSession();
        String cacheEquation = getPopupCacheEquation(request);
        List allImageListSession = (List)session.getAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST);
        String allImageListCacher = (String)session.getAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST_CACHER);
        boolean writeAllImageList = false;
        if (allImageListCacher!=null) {
            if (!allImageListCacher.equalsIgnoreCase(cacheEquation)) {
                //es befindet sich veraltete Daten im Cache:
                writeAllImageList = true;
                logger.debug("[allImageList] veraltet, aktuelle Liste speichern");
            }
        } else {
            //Caching-Objekt ist leer, also wurde noch keine Liste gespeichert
            writeAllImageList = true;
            logger.debug("[allImageList] existiert noch nicht, daher Liste speichern");
        }

        if (writeAllImageList) {
            List allImageList = null;
            try {
                allImageList = this.loadAllImageList(request,response);
                logger.debug("[allImageList] wird gespeichert unter: "+cacheEquation+", size: "+allImageList.size());
                session.setAttribute("allImageList",allImageList);
                session.setAttribute("allImageListCacher",cacheEquation);
            } catch (LoadThumbnailException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Wert damit dieser Request für die anzuzeigende Datenmenge (eine kategorie, ein ordner, ein suchergebnis,..)
     * eindeutig ist
     * @param request
     * @return
     */
    protected String getPopupCacheEquation(HttpServletRequest request) {
        return this.getClass().getName()+this.getContainerId(request)+"sortBy"+getSortBy(request)+"orderBy"+getOrderBy(request);
    }

    /**
     * Gibt das Mapping URI des Servlets zurück, wird für verlinkungen verwendet!
     * z.b. "/index/lightbox"
     * @param request
     * @return URI des Mappings
     */
    protected abstract String getServletMapping(HttpServletRequest request);

    /**
     * Anzahl der Bilder auf dieser Seite
     * Nicht die Anzahl der Bilder pro Seite, sondern nur auf der aktuellen Seite.
     * Das kann je nachdem ob es die letzte Seite ist kleiner als die Anzahl der Bilder Pro Seite sein
     * @param imageList
     * @return Anzahl der Bilder auf dieser Seite
     */
    protected int getPageSize(List imageList) {
        if (imageList!=null) {
            return imageList.size();
        } else {
            return 0;
        }
    }

    /**
     * Anzahl der Bilder pro Seite
     * @return Anzahl der Bilder pro Seite (Einstellung)
     */
    protected int getImageCountPerPage() {
        return Config.itemCountPerPage;
    }

    /**
     * Anzahl der Bilder in dieser Ansicht zurückgegeben
     * @param request
     * @return Totale anzahl der Bilder (z.b. in der Lightbox, Kategorie,...)
     */
    protected abstract int getImageCount(HttpServletRequest request);

    /**
     * Aktuelle Seiten-Nummer berechnen
     * @param request
     * @return Aktuelle (Ausgewählte) Seiten-Nummer
     */
    protected int getPageIndex(HttpServletRequest request) {

        try {
            int page = Integer.parseInt(request.getParameter("page"));
            return page;
        } catch (Exception e) {
            return 1;
        }

    }

    /**
     * Anzahl der Seiten berechnen
     * @param imageList
     * @return Anzahl der Seiten (Gesamt)
     */
    protected int getNumberOfPages(List imageList) {

        //Anzahl der Seiten kann nur berechnet werden wenn die Liste eine Paginated Liste ist
        if (imageList instanceof PaginatedList) {
            //System.out.println("getNumberOfPages mit paginatedList");
            PaginatedList paginatedImageList = (PaginatedList)imageList;

            int numberOfPages = 0;
            paginatedImageList.gotoPage(0);
            if (paginatedImageList.size()>0) numberOfPages=1;


            while (paginatedImageList.nextPage()) {
                paginatedImageList.gotoPage(numberOfPages);
                numberOfPages++;
            }
            return numberOfPages;

        } else {

            int imageCount = imageList.size();
            int pages = imageCount / getImageCountPerPage();
            if (imageCount%getImageCountPerPage()>0) { pages++; }
            return pages;
        }

    }

    /**
     * Gibt die nächste Seiten-Nummer zurück (Next-page)
     * @param imageList
     * @param request
     * @return oder 0 wenn keine nachfolgende Seite mehr extistiert
     */
    protected int getNextPage(List imageList, HttpServletRequest request) {

        if (imageList instanceof PaginatedList) {

            PaginatedList paginatedImageList = (PaginatedList)imageList;
            paginatedImageList.gotoPage(getPageIndex(request)-1);

            return (paginatedImageList.nextPage() ? getPageIndex(request)+1 : 0);

        } else {
            return 0;
        }

    }

    /**
     * gibt die vorherige Seiten-Nummer zurück (Prev-page)
     * @param imageList
     * @param request
     * @return oder 0 wenn es keine vorherige seite (0) gibt
     */
    protected int getPrevPage(List imageList, HttpServletRequest request) {

        if (imageList instanceof PaginatedList) {

            PaginatedList paginatedImageList = (PaginatedList)imageList;
            paginatedImageList.gotoPage(getPageIndex(request));

            return (paginatedImageList.previousPage() ? getPageIndex(request)-1 : 0);

        } else {
            return 0;
        }

    }

    /**
     * Ob die Select (Bild markieren) auswahl gezeigt werden soll
     * @param request
     * @return true, wenn sie gezeigt werden soll
     */
    protected boolean showSelect(HttpServletRequest request) {
        return false;
    }

    /**
     * gibt die Id zurück die diesen Container eindeutig identifiziert (PrimaryID)
     * Z.b. Id der Kategorie, Ordner,...
     * @return Id
     */
    protected int getContainerId(HttpServletRequest request) {

        try {
            int containerId = Integer.parseInt(request.getParameter("id"));
            return containerId;
        } catch (NullPointerException e) {
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
