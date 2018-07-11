package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.media.image.util.SizeExceedException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import com.stumpner.mediadesk.util.FileUtil;
import com.stumpner.mediadesk.util.UploadNotificationService;
import com.stumpner.mediadesk.upload.ImportPluginHandlerChain;
import com.stumpner.mediadesk.upload.FileRejectException;

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
 * Date: 16.10.2007
 * Time: 21:44:14
 * To change this template use File | Settings | File Templates.
 */
public class BinaryFileImportHandler implements MediaImportHandler {

    private String icon = "";

    public int processImport(File file, int userId) throws SizeExceedException, FileRejectException {

        Logger logger = Logger.getLogger(BinaryFileImportHandler.class);
        MediaObjectMultiLang mediaObject = new MediaObjectMultiLang();

        //Daten der Datei setzen
        mediaObject.setHeight(0);
        mediaObject.setWidth(0);
        mediaObject.setDpi(0);
        mediaObject.setCreateDate(new Date());
        mediaObject.setCreatorUserId(userId);
        mediaObject.setKb((int)(file.length()/1000));

        //MimeType + FileType:
        //todo: auslagern in den ImportHandler (eventuell in eine Superklasse!?)
        mediaObject.setMimeType(AbstractImportFactory.getMimeType(file));
        mediaObject.setExtention(AbstractImportFactory.getFileExtention(file));

        //Dateiname als Titel:
        mediaObject.setVersionName(file.getName());
        mediaObject.setVersionTitle(file.getName());
        mediaObject.setVersionTitleLng1(file.getName());
        mediaObject.setVersionTitleLng2(file.getName());

        //Größe Checken (mit erlaubnis in Config)
        if (mediaObject.getKb()> Config.maxFileSize) {
            logger.error("Maximal File Size reached: "+mediaObject.getKb()+" ["+Config.maxFileSize +"]");
            throw new SizeExceedException(mediaObject.getKb(),Config.maxFileSize);
        }

        //ImportPluginHandlerChain abarbeiten:
        ImportPluginHandlerChain.getInstance().validateFile(file);

        //File-Objekt in der Datenbank erstellen
        MediaService mediaService = new MediaService();
        try {
            logger.debug("Datei in der Datenbank anlegen...");
            mediaService.addMedia(mediaObject);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (Config.importImageNumberSerially) {
            logger.debug("Primary-Key als Bildnummer setzen...");
            mediaObject.setMediaNumber(Integer.toString(mediaObject.getIvid()));
        }

        logger.info("Added Media-Object to mediaDESK: "+mediaObject.getIvid()+" Number: "+mediaObject.getMediaNumber());

        //original image ablegen:
        try {
            FileUtil.copyFile(file,new File(Config.imageStorePath+File.separator+mediaObject.getIvid()+"_0"));
            //Icon für Thumbnailansicht und Preview aus den /WEB-INF/filetype/... kopieren
            //todo: womöglich wird das garnicht benötigt, wenn die anzeige über mime-types gelöst wird
//            copyFile(new File(icon+"_1.jpg"),new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1"));
//            copyFile(new File(icon+"_2.jpg"),new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2"));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        int importedIvid = mediaObject.getIvid();

        try {
            mediaService.saveMediaObject(mediaObject);
            ImportPluginHandlerChain.getInstance().process(mediaObject);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        UploadNotificationService uns = new UploadNotificationService();
        uns.triggerUpload(importedIvid);

        return importedIvid;

    }

    public void setIcon(String iconFile) {
        this.icon = iconFile;
    }

    public boolean isSupported(String mimeType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
