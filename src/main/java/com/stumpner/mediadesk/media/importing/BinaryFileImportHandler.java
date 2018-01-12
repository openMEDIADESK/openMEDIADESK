package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.image.inbox.InboxService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
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
        ImageVersionMultiLang imageVersion = new ImageVersionMultiLang();

        //Daten der Datei setzen
        imageVersion.setHeight(0);
        imageVersion.setWidth(0);
        imageVersion.setDpi(0);
        imageVersion.setCreateDate(new Date());
        imageVersion.setCreatorUserId(userId);
        imageVersion.setKb((int)(file.length()/1000));

        //MimeType + FileType:
        //todo: auslagern in den ImportHandler (eventuell in eine Superklasse!?)
        imageVersion.setMimeType(AbstractImportFactory.getMimeType(file));
        imageVersion.setExtention(AbstractImportFactory.getFileExtention(file));

        //Dateiname als Titel:
        imageVersion.setVersionName(file.getName());
        imageVersion.setVersionTitle(file.getName());
        imageVersion.setVersionTitleLng1(file.getName());
        imageVersion.setVersionTitleLng2(file.getName());

        //Größe Checken (mit erlaubnis in Config)
        if (imageVersion.getKb()> Config.maxImageSize) {
            logger.error("Maximal File Size reached: "+imageVersion.getKb()+" ["+Config.maxImageSize+"]");
            throw new SizeExceedException(imageVersion.getKb(),Config.maxImageSize);
        }

        //ImportPluginHandlerChain abarbeiten:
        ImportPluginHandlerChain.getInstance().validateFile(file);

        //File-Objekt in der Datenbank erstellen
        ImageVersionService imageService = new ImageVersionService();
        try {
            logger.debug("Datei in der Datenbank anlegen...");
            imageService.addImage(imageVersion);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (Config.importImageNumberSerially) {
            logger.debug("Primary-Key als Bildnummer setzen...");
            imageVersion.setImageNumber(Integer.toString(imageVersion.getIvid()));
        }

        logger.info("Added Media-Object to mediaDESK: "+imageVersion.getImageId()+" Number: "+imageVersion.getImageNumber());

        //original image ablegen:
        try {
            FileUtil.copyFile(file,new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_0"));
            //Icon für Thumbnailansicht und Preview aus den /WEB-INF/filetype/... kopieren
            //todo: womöglich wird das garnicht benötigt, wenn die anzeige über mime-types gelöst wird
//            copyFile(new File(icon+"_1.jpg"),new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1"));
//            copyFile(new File(icon+"_2.jpg"),new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2"));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        //add to inbox
        InboxService inboxService = new InboxService();
        inboxService.addImage(imageVersion.getIvid());
        int importedIvid = imageVersion.getIvid();

        try {
            imageService.saveImageVersion(imageVersion);
            ImportPluginHandlerChain.getInstance().process(imageVersion);
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
