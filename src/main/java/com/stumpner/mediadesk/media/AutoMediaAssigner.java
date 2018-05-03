package com.stumpner.mediadesk.media;

import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.io.IOException;

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
 * Gibt ein bestimmtes (oder mehrere Bilder) in eine Kategorie, Folder,...
 * User: franz.stumpner
 * Date: 21.05.2009
 * Time: 14:31:01
 * To change this template use File | Settings | File Templates.
 */
public class AutoMediaAssigner {

    public void clear(HttpServletRequest request) {

        request.getSession().removeAttribute("autoImportTo");

    }

    public void setDestination(HttpServletRequest request, Object object) {

        request.getSession().setAttribute("autoImportTo",object);
    }

    public void assign(HttpServletRequest request,int ivid) {
        assign(
                getAutoImportObject(request),ivid
        );
    }

    public void assign(Object autoImportObject,int ivid) {

        if (autoImportObject!=null) {
            if (isAutoImportCategory(autoImportObject)) {
                FolderService folderService = new FolderService();
                Folder folder = (Folder)autoImportObject;
                try {
                    folderService.addMediaToFolder(folder.getCategoryId(),ivid);
                } catch (DublicateEntry dublicateEntry) {
                    dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            if (isAutoImportPin(autoImportObject)) {
                PinService pinService = new PinService();
                Pin pin = (Pin)autoImportObject;
                pinService.addImageToPinpic(ivid, pin.getPinId());

                //Informieren, wenn aktiviert
                if (pin.getEmailnotification().length()>0) {

                    MediaService imageService = new MediaService();
                    MediaObjectMultiLang image = (MediaObjectMultiLang)imageService.getMediaObjectById(ivid);

                    String mailsubject = "PIN Upload: "+ pin.getPin() + " "+ pin.getPinName();
                    String mailbody = "Eine oder mehrere Dateien wurden in den PIN "+ pin.getPin() + " hochgeladen.";
                    mailbody = mailbody + "\n\nWeitere Informationen: ";
                    mailbody = mailbody + "\n\n + Dateiname: "+image.getVersionName()+" ("+image.getVersionTitle()+")";
                    mailbody = mailbody + "\n\n + Datum: "+(new Date());

                   MailWrapper.sendAsync(Config.mailserver,Config.mailsender, pin.getEmailnotification(),mailsubject,mailbody);
                }

            }
        } else {

            //Wenn keinem Ziel zugewiesen, dann in den Arbeitsplat/Favoriten (Lightbox laden)
            MediaService mediaService = new MediaService();
            MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)mediaService.getMediaObjectById(ivid);

            FavoriteService workspaceService = new FavoriteService();
            //System.out.println("CreatorUserId: "+mediaObject.getCreatorUserId()+" Lightbox: "+ivid);
            workspaceService.addImageToLightbox(ivid, mediaObject.getCreatorUserId());
        }

    }

    public Object getAutoImportObject(HttpServletRequest request) {

        if (checkAutoImport(request)) {

            boolean isPin = false;
            if (request.getParameter("pinid")!=null) {
                if (request.getParameter("pinid").length()>0) {
                    isPin = true;
                }
            }

            if (isPin) {
                PinService pinService = new PinService();
                try {
                    Pin pin = (Pin) pinService.getById(Integer.parseInt(request.getParameter("pinid")));
                    return pin;
                } catch (ObjectNotFoundException e) {
                    return null;
                } catch (IOServiceException e) {
                    return null;
                }
            } else {
                return request.getSession().getAttribute("autoImportTo");
            }
        } else {
            return null;
        }
    }

    public boolean isAutoImportCategory(Object o) {

        if (o instanceof Folder) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAutoImportPin(Object o) {

        if (o instanceof Pin) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAutoImport(HttpServletRequest request) {

        if (request.getSession().getAttribute("autoImportTo")!=null) {
            return true;
        } else {
            if (request.getParameter("pinid")!=null) {
                return true;
            }
            return false;
        }

    }

    public void sendRedirectOfAutoImport(Object autoImportObject, HttpServletResponse response) throws IOException {

        response.sendRedirect(response.encodeRedirectURL(getRedirectOfAutoImport(autoImportObject)));

    }

    public String getRedirectOfAutoImport(Object autoImportObject) throws IOException {

            if (isAutoImportCategory(autoImportObject)) {
                Folder folder = (Folder)autoImportObject;
                return "c?id="+ folder.getCategoryId();
            }
            if (isAutoImportPin(autoImportObject)) {
                Pin pin = (Pin)autoImportObject;
                return "pinview";
            }

        return null;

    }

}
