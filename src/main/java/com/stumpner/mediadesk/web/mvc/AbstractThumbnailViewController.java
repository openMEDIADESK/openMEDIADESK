package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;

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
public abstract class AbstractThumbnailViewController extends AbstractMediaActionController {

    protected static String TEMPLATEFILE_THUMBNAILS = "listThumbnails.jsp";
    protected static String TEMPLATEFILE_LISTVIEW = "listFiles.jsp";

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ModelAndView modelAndView = super.handleRequestInternal(httpServletRequest, httpServletResponse);

        httpServletRequest.setAttribute("servletMapping",getServletMapping(httpServletRequest));
        httpServletRequest.setAttribute("containerId",Integer.toString(getContainerId(httpServletRequest)));
        httpServletRequest.setAttribute("showSendImage",new Boolean(Config.showSendImage));
        httpServletRequest.setAttribute("inlinePreview",new Boolean(Config.inlinePreview));

        getSortBy(httpServletRequest);
        getOrderBy(httpServletRequest);


        //Neue GUI verwende nur mehr view (1=
        httpServletRequest.setAttribute("view", getView(httpServletRequest));
        httpServletRequest.setAttribute("showSelect", new Boolean(showSelect(httpServletRequest)));

                //Preis anzeigen wenn Bezahlsystem aktiv
        if (!Config.currency.isEmpty()) {
            httpServletRequest.setAttribute("thumbnailShowPrice", true);
            httpServletRequest.setAttribute("currency", Config.currency);
        }

        //ThumbnailListObject speichern
        //httpServletRequest.getSession().setAttribute("thumbnailListObject",this.getThumbnailListObject(httpServletRequest));

        return modelAndView;    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected boolean showSelect(HttpServletRequest request) {
        if (Config.quickDownload) {
            return this.getUser(request).getRole() >= User.ROLE_USER;
        } else {
            return this.getUser(request).getRole() > User.ROLE_EDITOR;
        }
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
    protected String getListViewCustom(HttpServletRequest request) {
        return null;
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
