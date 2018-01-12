package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.Config;

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
 * Date: 13.11.2005
 * Time: 22:24:33
 * To change this template use File | Settings | File Templates.
 */
public class ThumbnailModuleController extends AbstractController {

    public static final int USE_THUMBNAIL = 1;
    public static final int USE_FILELIST = 2;

    private static int defaultViewMode = 1;


    /**
     * Einstellen des Anzeige-Modus der Thumbnail-Anzeige:
     * bei USE_THUMBNAIL: wird die Bild-Vorschau-Ansicht angezeigt
     * bei USE_FILELIST: wird die Explorer-Dateiansicht angezeigt 
     * @param mode
     */
    public static void setDefaultViewMode(int mode) {
        defaultViewMode = mode;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //todo: thumnailmodule oder filelistmodule auswahl

        if (Config.inlinePreview) {
            httpServletRequest.setAttribute("inlinePreview",true);
        } else {
            httpServletRequest.setAttribute("inlinePreview",false);
        }

        switch (defaultViewMode) {
            case USE_THUMBNAIL:
                return new ModelAndView("/WEB-INF/template/"+Config.templatePath+"thumbnailmodule.jsp");

            case USE_FILELIST:
                return new ModelAndView("/WEB-INF/template/"+Config.templatePath+"filelistmodule.jsp");

            default:
                return new ModelAndView("/WEB-INF/template/"+Config.templatePath+"thumbnailmodule.jsp");
        }

    }

}
