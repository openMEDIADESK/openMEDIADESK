package com.stumpner.mediadesk.web.mvc;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;
import com.stumpner.mediadesk.core.service.MediaObjectService;

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
 * Date: 05.01.2007
 * Time: 10:50:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMediaSelectController extends AbstractMediaLoaderController {


    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Logger logger = Logger.getLogger(AbstractMediaSelectController.class);

        //Anzahl der selektierten (markierten) Medienobjekte in den Request schreiben
        List imageList = this.getSelectedImageList(httpServletRequest.getSession());
        httpServletRequest.setAttribute("selectedImages",new Integer(imageList.size()));

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Liste mit ausgewählten Dateien (MedienObjekte) zurückgeben
     * @param session
     * @return Liste von <MediaObject> Dateien die ausgewählt sind.
     */
    public static List getSelectedImageList(HttpSession session) {

        return MediaObjectService.getSelectedMediaObjectList(session);

    }

    /**
     * Gibt das Object (folder,folder) zurück, der aktuellen Ansicht
     * @return gibt <null> zurück wenn es kein Object gibt, ansonsten das Object (folder, folder)
     */
    protected Object getContainerObject(HttpServletRequest request) {
        return null;
    }



}
