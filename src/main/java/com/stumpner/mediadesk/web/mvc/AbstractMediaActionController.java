package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.folder.Folder;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.usermanagement.User;

import java.util.*;

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
 * Abstrakter Controller der manipulationen (löschen, einfügen, kopieren, ...) einer Datei/Bildliste ermöglicht
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 05.01.2007
 * Time: 11:10:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMediaActionController extends AbstractMediaSelectController {


    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (httpServletRequest.getSession().getAttribute("message")!=null) {
            httpServletRequest.setAttribute("showMessage",1);
            httpServletRequest.setAttribute("message",httpServletRequest.getSession().getAttribute("message"));
            httpServletRequest.getSession().removeAttribute("message");
        }
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);
    }

    protected abstract void insert(MediaObject image, HttpServletRequest request) throws DublicateEntry;

    protected abstract void remove(MediaObject image, HttpServletRequest request);

    protected void move(MediaObject image, Object fromContainerObject, HttpServletRequest request) {

        Logger logger = Logger.getLogger(AbstractMediaActionController.class);
        try {
            this.insert(image,request);
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //Original Entfernen
        logger.debug("Original entfernen");
        if (fromContainerObject==null) {
            System.out.println("Original-Objekt existiert nicht");
            logger.info("Original-Objekt existiert nicht");
        } else {
            if (fromContainerObject instanceof Folder) {
                Folder folder = (Folder)fromContainerObject;
                //if (folder.getFolderId()!=-1) {
                System.out.println("Original war eine Cat: "+ folder.getFolderId()+" "+ folder.getName());
                    logger.debug("Original war eine Cat: "+ folder.getFolderId()+" "+ folder.getName());
                    FolderService folderService = new FolderService();
                    folderService.deleteMediaFromFolder(folder,image);
                //} else {
                //    logger.debug("Original war eine 00: Aktuellste Bilder - entfernen nicht moeglich");
                //}
            }
        }

    }

    private void clearPopupCache(HttpServletRequest request) {

            request.getSession().removeAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST);
            request.getSession().removeAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST_CACHER);

    }

}
