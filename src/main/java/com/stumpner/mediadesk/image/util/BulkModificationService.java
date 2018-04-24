package com.stumpner.mediadesk.image.util;

import com.stumpner.mediadesk.core.database.sc.MediaMetadataService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.image.Metadata;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.User;

import java.util.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.log4j.Logger;

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
 * Date: 21.02.2007
 * Time: 22:06:09
 * To change this template use File | Settings | File Templates.
 */
public class BulkModificationService {

    static int processed = 0;
    static List imageVersionList = null;
    static List imageErrorList = null; //Liste mit Bildfehlern...
    static Timer timer = null;
    static boolean processWatermark = false;
    static boolean processMetadata = false;
    static int errorRetryCounter = 0;
    static String informEmail = ""; //Emailadresse die über den Status der Massenänderunge informiert wird

    public static boolean inProgress() {
        if (imageVersionList!=null) {
            return true;
        } else {
            return false;
        }
    }

    public static void setProcessWatermark(boolean processWatermark) {
        BulkModificationService.processWatermark = processWatermark;
    }

    public static void setProcessMetadata(boolean processMetadata) {
        BulkModificationService.processMetadata = processMetadata;
    }

    public static boolean isProcessWatermark() {
        return processWatermark;
    }

    public static boolean isProcessMetadata() {
        return processMetadata;
    }

    public static List getImageVersionList() {
        return imageVersionList;
    }

    public static int getProcessed() {
        return processed;
    }

    private static synchronized void setProcessed(int processed) {
        BulkModificationService.processed = processed;
    }

    static public void startModification(List imageVersionListLocal) {

        Logger logger = Logger.getLogger(BulkModificationService.class);
        setProcessed(0);
        imageVersionList = imageVersionListLocal;
        imageErrorList = new ArrayList();

        //inform-Email
        try {
            UserService userService = new UserService();
            User admin = (User) userService.getByName("admin");
            informEmail = admin.getEmail();
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                //nach jeder abgelaufenen "pause" einen Bild-Bearbeitungs-Schritt anstossen:
                Logger logger = Logger.getLogger(BulkModificationService.class);

                try {

                    if (processWatermark) {
                        processRedrawWatermark();
                    }
                    if (processMetadata) {
                        processMetadata();
                    }
                            setProcessed(getProcessed() + 1);
                            errorRetryCounter=0;

                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    MediaObject imageVersion = (MediaObject)imageVersionList.get(getProcessed());
                    if (errorRetryCounter<3) {
                        logger.error("BuldModification: Error on processing ["+getProcessed()+"/"+imageVersionList.size()+"] ivid="+imageVersion.getIvid()+" ["+e.getMessage()+"]");
                        errorRetryCounter++;
                    } else {
                        imageErrorList.add(imageVersion);
                            String mailbody = "Fehler bei der Massenaenderung mit Bild ivid="+imageVersion.getIvid()+", ich gebe auf und mache mit der naechsten Datei weiter...";
                            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,informEmail,"[Massen-Aenderung FEHLER] - "+Config.webTitle,mailbody);
                            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,"franz@stumpner.net","[Massen-Änderung FEHLER] - "+Config.webTitle,mailbody);
                            setProcessed(getProcessed() + 1);
                            errorRetryCounter=0;
                    }
                }

                if (processed==imageVersionList.size()) {
                    this.cancel();
                    logger.debug("BulkModification: redrawWatermark abgeschlossen.");
                    imageVersionList = null;
                    //Meldung über abgeschlossen per Email versenden:
                            String mailbody = "Massenaenderung in der mediaDESK-Instanz ["+Config.instanceName+"] wurde abgeschlossen, "+processed+" Dateien bearbeitet.";
                            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,informEmail,"[Massen-Aenderung BEENDET] - "+Config.webTitle,mailbody);
                            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,"franz@stumpner.net","[Massen-Aenderung BEENDET] - "+Config.webTitle,mailbody);
                }
            }
        },3000,10000);

        logger.debug("BulkModification: timerTask created...");

    }

    static public void cancel() {

        Logger logger = Logger.getLogger(BulkModificationService.class);

        if (timer!=null) {
            timer.cancel();
            imageVersionList = null;
            //Meldung über abgeschlossen per Email versenden:
                    String mailbody = "Massenaenderung durch Benutzer beendet, "+processed+" Dateien bearbeitet, "+imageErrorList.size()+" Bilder mit Fehler.";
                    MailWrapper.sendAsync(Config.mailserver,Config.mailsender,informEmail,"[Massen-Aenderung ABGEBROCHEN] - "+Config.webTitle,mailbody);
                    MailWrapper.sendAsync(Config.mailserver,Config.mailsender,"franz@stumpner.com","[Massen-Aenderung ABGEBROCHEN] - "+Config.webTitle,mailbody);
        }

    }

    /**
     * Bearbeitet ein Bild (Schritt)
     */
    private static synchronized void processRedrawWatermark() {

        ImageToolbox it = new ImageToolbox();
        Logger logger = Logger.getLogger(BulkModificationService.class);
        MediaObject imageVersion = (MediaObject)imageVersionList.get(processed);
        if (imageVersion.getMayorMime().equalsIgnoreCase("image")) {
            logger.debug("BulkModification: ["+(processed+1)+"/"+imageVersionList.size()+"] redrawWatermark ImageIVID="+imageVersion.getIvid());
            it.generateThumbnail(imageVersion);
        }

    }

    private static synchronized void processMetadata() throws MetadataReadException, IOServiceException {

        ImageMagickUtil imageUtil = new ImageMagickUtil(true);
        MediaObject imageVersion = (MediaObject)imageVersionList.get(processed);

        String fileName = Config.imageStorePath+ File.separator+imageVersion.getIvid()+"_0";

        Logger logger = Logger.getLogger(BulkModificationService.class);
        logger.debug("BulkModification: ["+(processed+1)+"/"+imageVersionList.size()+"] reimportMetadata ImageIVID="+imageVersion.getIvid());

        MediaMetadataService mediaMetadataService = new MediaMetadataService();
        //data auslesen (exif, iptc, bulk)
        List metadataList = null;
        metadataList = imageUtil.getImageBulkData(fileName);


        Iterator metadatas = metadataList.iterator();
        while (metadatas.hasNext()) {
            Metadata metadata = (Metadata)metadatas.next();
            metadata.setImageId(imageVersion.getImageId());
            metadata.setIvid(imageVersion.getIvid());
            metadata.setLang("");
            metadata.setVersionId(imageVersion.getVersion());
            //Metadaten nicht speichern (wird ja nur ausgelesen)
            // mediaMetadataService.addMetadata(metadata);

            //logger.debug("Meta-Key: "+metadata.getMetaKey()+" | Meta-Value: "+metadata.getMetaValue()+ " | ");

            if (metadata.getMetaKey().equalsIgnoreCase(Config.importName)) {
                imageVersion.setVersionName(metadata.getMetaValue());
                imageVersion.setImageName(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importTitle)) {
                imageVersion.setVersionTitle(metadata.getMetaValue());
                imageVersion.setImageTitle(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importSubtitle)) {
                imageVersion.setVersionSubTitle(metadata.getMetaValue());
                imageVersion.setImageSubTitle(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importKeywords)) {
                imageVersion.setKeywords(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importNote)) {
                imageVersion.setNote(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importRestrictions)) {
                imageVersion.setRestrictions(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importNumber)) {
                imageVersion.setImageNumber(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importByline)) {
                imageVersion.setByline(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importPhotographerAlias)) {
                imageVersion.setPhotographerAlias(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importSite)) {
                imageVersion.setSite(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importPeople)) {
                imageVersion.setPeople(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importInfo)) {
                imageVersion.setInfo(metadata.getMetaValue());
            }


            //datum parsen
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importDate)) {

                Date date = new Date();
               // try {
               //     dateFormat.setLenient(true);
               //     date = dateFormat.parse(metadata.getMetaValue());
               // } catch (ParseException e) {
                    //e.printStackTrace();
                    //Datum konnte nicht erkannt werden...
                    //datum speziell auseinandernehmen:
                    String stringDate = metadata.getMetaValue();
                    //logger.debug("String-Format Datum: "+stringDate+" Feld: "+Config.importDate);
                    //logger.debug("Directory: "+metadata.getDirectory());
                    String[] dateToken = stringDate.split(" ");
                    //logger.debug("Token-Length: "+dateToken.length);
                switch (dateToken.length) {
                    case 2: // Standard IPTC
                            String[] dayToken = dateToken[0].split(":");
                            String[] timeToken = dateToken[1].split(":");
                        //logger.debug("dayToken[0]:"+dayToken[0]);
                        //logger.debug("dayToken[1]:"+dayToken[1]);
                        //logger.debug("dayToken[2]:"+dayToken[2]);
                        //logger.debug("timeToken[0]:"+timeToken[0]);
                        //logger.debug("timeToken[1]:"+timeToken[1]);
                        //logger.debug("-dayToken.length: "+dayToken.length);
                        //logger.debug("-timeToken.length: "+timeToken.length);
                            if (dayToken.length>2 && timeToken.length>2) {
                                Calendar cal = GregorianCalendar.getInstance();
                                cal.set((new Integer(dayToken[0])).intValue(),
                                        (new Integer(dayToken[1])).intValue()-1,
                                        (new Integer(dayToken[2])).intValue(),
                                        (new Integer(timeToken[0])).intValue(),
                                        (new Integer(timeToken[1])).intValue());
                                date = cal.getTime();
                                //logger.debug("Datum gesetzt(2):"+date);
                            } else {
                                //logger.debug("Token mismatch");
                            }
                        break;
                    case 6: //Old "Date Created Field"
                        String dateString = dateToken[1]+" "+dateToken[2]+" "+dateToken[5];
                        //logger.debug("Date Parse String: "+dateString);
                        /*DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");*/
                        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy",Locale.US);
                        dateFormat.setLenient(true);
                        try {
                            date = dateFormat.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        break;
                }


                imageVersion.setPhotographDate(date);
            }
        }

        MediaService imageVersionService = new MediaService();

        imageVersionService.saveMediaObject(imageVersion);
    }

}
