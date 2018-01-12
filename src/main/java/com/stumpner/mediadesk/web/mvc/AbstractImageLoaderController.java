package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.stumpner.mediadesk.web.mvc.exceptions.LoadThumbnailException;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;

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
 * Date: 08.01.2007
 * Time: 17:34:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageLoaderController extends AbstractPageController {

    static int ORDERTYPE_DATE = 1;
    static int ORDERTYPE_NAME = 1;

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Gibt alle Bilder dieser Ansicht zurück,
     * also nicht nur jene die auf dieser Seite zu sehen sind!
     * @param httpServletRequest
     * @param httpServletResponse
     * @return Alle Bilder
     */
    protected List loadAllImageList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoadThumbnailException {
        return this.loadThumbnailImageList(
                getSortBy(httpServletRequest),
                getOrderBy(httpServletRequest),
                httpServletRequest,
                httpServletResponse);
    }

    /**
     * Gibt eine Liste von Bildern für diese Seite zurück
     * Es muss also nicht sein, dass hier alle Bilder zurück gegeben werden
     * @param httpServletRequest
     * @param httpServletResponse
     * @return Bilder dieser Seite
     * @deprecated use loadThumbnailImageListSorted
     */
    protected List loadThumbnailImageList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoadThumbnailException {
        return this.loadThumbnailImageList(
                getSortBy(httpServletRequest),
                getOrderBy(httpServletRequest),
                httpServletRequest,
                httpServletResponse);
    }

    /**
     * Gibt nur die Bilder der aktuellen Seite zurück
     * @param sortBy
     * @param orderBy
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws LoadThumbnailException
     */
    protected abstract List loadThumbnailImageList(int sortBy, int orderBy, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoadThumbnailException;


    protected String getDependendKey(HttpServletRequest request) {
        return this.getClass().getName();
    }
    /**
     * Aus dem Request laden wonach sortiert werden soll
     * @param request
     * @return ein Integer wonach sortiert werden soll
     */
    protected int getSortBy(HttpServletRequest request) {

        String sortkey = "sortBy"+getDependendKey(request);
        //System.out.println("sortkey="+sortkey);

        if (request.getParameter("sortBy")!=null) {
            int sortBy = Integer.parseInt(request.getParameter("sortBy"));
            //System.out.println(" aus request="+sortBy);
            //Zusätzlich in Session speichern (damit die sort order beibehalten wird!)
            request.getSession().setAttribute(sortkey,new Integer(sortBy));
            request.setAttribute("sortBy",new Integer(sortBy));
            return sortBy;
        } else {
            //Versuch aus Session zu laden
            if (request.getSession().getAttribute(sortkey)!=null) {
                int sortBy = ((Integer)request.getSession().getAttribute(sortkey)).intValue();
                //System.out.println(" aus session="+sortBy);
                request.setAttribute("sortBy",new Integer(sortBy));
                return sortBy;
            } else {
                //request.getSession().setAttribute(sortkey,new Integer(getDefaultSort(request)));
                request.setAttribute("sortBy",new Integer(getDefaultSort(request)));
                //System.out.println(" aus default="+getDefaultSort(request));
                return getDefaultSort(request);
            }
        }

    }

    /**
     * Aus dem Request laden ob aufsteigend oder absteigend sortiert werden soll
     * @param request
     * @return 1 = aufsteigend / 2 = absteigend, nichts = aufsteigend
     */
    protected int getOrderBy(HttpServletRequest request) {

        String orderkey = "orderBy"+getDependendKey(request);
        //System.out.println("orderby="+orderkey);

        if (request.getParameter("orderBy")!=null) {
            int orderBy = Integer.parseInt(request.getParameter("orderBy"));
            //System.out.println(" aus request="+orderBy);
            //Zusätzlich in Session speichern (damit die sort order beibehalten wird!)
            request.getSession().setAttribute(orderkey,new Integer(orderBy));
            request.setAttribute("orderBy",new Integer(orderBy));
            return orderBy;
        } else {
            //Versuch aus Session zu laden
            if (request.getSession().getAttribute(orderkey)!=null) {
                int orderBy = ((Integer)request.getSession().getAttribute(orderkey)).intValue();
                //System.out.println(" aus session="+orderBy);
                request.setAttribute("orderBy",new Integer(orderBy));
                return orderBy;
            } else {
                //request.getSession().setAttribute(orderkey,new Integer(getDefaultOrder(request)));
                request.setAttribute("orderBy",new Integer(getDefaultOrder(request)));
                //System.out.println(" aus default="+getDefaultOrder(request));
                return getDefaultOrder(request);
            }
        }

    }

    /**
     * Standard-Sortierung wenn nichts angegeben wurde
     * @param request
     * @return
     */
    protected int getDefaultSort(HttpServletRequest request) {
        return 0;
    }

    /**
     * Standard-Sortierung wenn nichts angegeben wurde
     * @param request
     * @return
     */
    protected int getDefaultOrder(HttpServletRequest request) {
        return 0;
    }

    protected MediaMenu getMediaMenu(HttpServletRequest request) {

        MediaMenu properties = new MediaMenu();
        if (request.getAttribute("mediamenu")!=null) {
            properties = (MediaMenu)request.getAttribute("mediamenu");
        } else {
            request.setAttribute("mediamenu",properties);
        }
        return properties;

    }


}
