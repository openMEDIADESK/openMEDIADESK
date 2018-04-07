package com.stumpner.mediadesk.image.util;

import com.stumpner.mediadesk.core.database.sc.MediaMetadataService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.image.Metadata;
import com.stumpner.mediadesk.image.inbox.InboxService;
import com.stumpner.mediadesk.media.importing.AbstractImportFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.text.*;

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
 * User: franzstumpner
 * Date: 27.04.2005
 * Time: 20:08:44
 * To change this template use File | Settings | File Templates.
 */
public class ImageImport {

    static public final int IMPORT_OK = 0;
    static public final int IMPORT_METADATA_ERROR = 1;

    static int importedIvid = -1;

    protected static synchronized int processImage(String fileName, IImageUtil imageUtil, int userId) throws SizeExceedException {

        int returnValue = IMPORT_OK;

        Logger logger = Logger.getLogger(ImageImport.class);
        MediaObjectMultiLang imageVersion = new MediaObjectMultiLang();

        //Neu seit 2.6: Bild vor dem bearbeiten/verkleinern auf JPEG konvertieren.
        String jpegFilename = fileName+".jpg";
        //System.out.println("Converting original file to jpeg(filename="+fileName+", jpegFilename="+jpegFilename);
        imageUtil.convertToJpeg(fileName,jpegFilename);

        //data auslesen (exif, iptc, bulk)
        List metadataList = null;
        try {
            logger.debug("Reading ImageBulkData");
            metadataList = imageUtil.getImageBulkData(jpegFilename);
        } catch (MetadataReadException e) {
            logger.error("IMPORT_METADATA_ERROR");
            returnValue = IMPORT_METADATA_ERROR;
            metadataList = new ArrayList();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        imageVersion.setHeight(imageUtil.getImageHeight(jpegFilename));
        imageVersion.setWidth(imageUtil.getImageWidth(jpegFilename));
        imageVersion.setDpi(imageUtil.getImageDpi(jpegFilename));
        imageVersion.setCreateDate(new Date());
        imageVersion.setCreatorUserId(userId);
        File file = new File(fileName);
        imageVersion.setKb((int)(file.length()/1000));

        //MimeType + FileType:
        //todo: auslagern in den ImportHandler (eventuell in eine Superklasse!?)
        imageVersion.setMimeType(AbstractImportFactory.getMimeType(file));
        //System.out.println("Extention in ImageImport: "+AbstractImportFactory.getFileExtention(file));
        imageVersion.setExtention(AbstractImportFactory.getFileExtention(file));

        //Größe Checken (mit erlaubnis in Config)
        if (imageVersion.getKb()>Config.maxImageSize) {
            logger.error("Maximal BasicMediaObject Size reached"+imageVersion.getKb());
            throw new SizeExceedException(imageVersion.getKb(),Config.maxImageSize);
        }

        //image in db erstellen
        MediaService imageService = new MediaService();
        try {
            logger.debug("Bild in der Datenbank anlegen...");
            imageService.addImage(imageVersion);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (Config.importImageNumberSerially) {
            logger.debug("Primary-Key als Bildnummer setzen...");
            imageVersion.setImageNumber(Integer.toString(imageVersion.getIvid()));
        }

        logger.info("Added Media-Objekt to mediaDESK: "+imageVersion.getImageId()+" Number: "+imageVersion.getImageNumber());

        //orientation:
        // 0 - square
        // 1 - vertical
        // 2 - horizontal
        int orientation = 0;
        if (imageVersion.getHeight()>imageVersion.getWidth()) {
            //vertical
            orientation = 1;
        }
        if (imageVersion.getWidth()>imageVersion.getHeight()) {
            orientation = 2;
        }

        //images verkleinern vertical
        if (orientation == 1 || orientation == 0) {
            imageUtil.resizeImageVertical(
                    fileName,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1",Config.imagesizeThumbnail);
            imageUtil.resizeImageVertical(
                    fileName,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.imagesizePreview);
            imageUtil.overlayWatermark(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.watermarkVertical);
        }


        //images verkleinern horizontal
        if (orientation == 2) {
            imageUtil.resizeImageHorizontal(
                    fileName,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1",Config.imagesizeThumbnail);
            imageUtil.resizeImageHorizontal(
                    fileName,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.imagesizePreview);
            imageUtil.overlayWatermark(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.watermarkHorizontal);
        }

        //das konvertierte Jpeg-File löschen
        File jpegFile = new File(jpegFilename);
        jpegFile.delete();

        //original image ablegen:
        try {
            copyFile(new File(fileName),new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_0"));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //watermark

        //import to database

        //add to inbox
        InboxService inboxService = new InboxService();
        inboxService.addImage(imageVersion.getIvid());
        importedIvid = imageVersion.getIvid();

        //import metadata

        //System.out.println("Import Metadata");

        MediaMetadataService mediaMetadataService = new MediaMetadataService();

        //Dateiname im Import-Filesytem als Meta-Data
        Metadata md = new Metadata();
        md.setExifTag(false);
        md.setImageId(imageVersion.getImageId());
        md.setIvid(imageVersion.getIvid());
        md.setVersionId(imageVersion.getVersion());
        md.setMetaKey("Filename");
        md.setMetaValue(file.getName());
        metadataList.add(md);

        Iterator metadatas = metadataList.iterator();
        while (metadatas.hasNext()) {
            Metadata metadata = (Metadata)metadatas.next();
            metadata.setImageId(imageVersion.getImageId());
            metadata.setIvid(imageVersion.getIvid());
            metadata.setLang("");
            metadata.setVersionId(imageVersion.getVersion());
            mediaMetadataService.addMetadata(metadata);

            logger.debug("Meta-Key: "+metadata.getMetaKey()+" | Meta-Value: "+metadata.getMetaValue()+ " | ");

            if (metadata.getMetaKey().equalsIgnoreCase(Config.importName)) {
                imageVersion.setVersionName(metadata.getMetaValue());
                imageVersion.setImageName(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importTitle)) {
                imageVersion.setVersionTitle(metadata.getMetaValue());
                imageVersion.setVersionTitleLng1(metadata.getMetaValue());
                imageVersion.setVersionTitleLng2(metadata.getMetaValue());
                imageVersion.setImageTitle(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importSubtitle)) {
                imageVersion.setVersionSubTitle(metadata.getMetaValue());
                imageVersion.setVersionSubTitleLng1(metadata.getMetaValue());
                imageVersion.setVersionSubTitleLng2(metadata.getMetaValue());
                imageVersion.setImageSubTitle(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importKeywords)) {
                imageVersion.setKeywords(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importNote)) {
                imageVersion.setNote(metadata.getMetaValue());
                imageVersion.setNoteLng1(metadata.getMetaValue());
                imageVersion.setNoteLng2(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importRestrictions)) {
                imageVersion.setRestrictions(metadata.getMetaValue());
                imageVersion.setRestrictionsLng1(metadata.getMetaValue());
                imageVersion.setRestrictionsLng2(metadata.getMetaValue());
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
                imageVersion.setSiteLng1(metadata.getMetaValue());
                imageVersion.setSiteLng2(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importPeople)) {
                imageVersion.setPeople(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase(Config.importInfo)) {
                imageVersion.setInfo(metadata.getMetaValue());
                imageVersion.setInfoLng1(metadata.getMetaValue());
                imageVersion.setInfoLng2(metadata.getMetaValue());
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
                    logger.debug("String-Format Datum: "+stringDate+" Feld: "+Config.importDate);
                    logger.debug("Directory: "+metadata.getDirectory());
                    String[] dateToken = stringDate.split(" ");
                    logger.debug("Token-Length: "+dateToken.length);
                switch (dateToken.length) {
                    case 2: // Standard IPTC
                            String[] dayToken = dateToken[0].split(":");
                            String[] timeToken = dateToken[1].split(":");
                        logger.debug("dayToken[0]:"+dayToken[0]);
                        logger.debug("dayToken[1]:"+dayToken[1]);
                        logger.debug("dayToken[2]:"+dayToken[2]);
                        logger.debug("timeToken[0]:"+timeToken[0]);
                        logger.debug("timeToken[1]:"+timeToken[1]);
                        logger.debug("-dayToken.length: "+dayToken.length);
                        logger.debug("-timeToken.length: "+timeToken.length);
                            if (dayToken.length>2 && timeToken.length>2) {
                                Calendar cal = GregorianCalendar.getInstance();
                                cal.set((new Integer(dayToken[0])).intValue(),
                                        (new Integer(dayToken[1])).intValue()-1,
                                        (new Integer(dayToken[2])).intValue(),
                                        (new Integer(timeToken[0])).intValue(),
                                        (new Integer(timeToken[1])).intValue());
                                date = cal.getTime();
                                logger.debug("Datum gesetzt(2):"+date);
                            } else {
                                logger.debug("Token mismatch");
                            }
                        break;
                    case 6: //Old "Date Created Field"
                        String dateString = dateToken[1]+" "+dateToken[2]+" "+dateToken[5];
                        logger.debug("Date Parse String: "+dateString);
                        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy",Locale.US);
                        dateFormat.setLenient(true);
                        try {
                            date = dateFormat.parse(dateString);
                            logger.debug("Datum gesetzt(6):"+date);
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        break;
                }


                imageVersion.setPhotographDate(date);
            }
        }

        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            //Name und Titel aus Filename verwenden, wenn leer
            if (imageVersion.getVersionName().length()==0) { imageVersion.setVersionName(file.getName()); }
            if (imageVersion.getVersionTitle().length()==0) { imageVersion.setVersionTitle(file.getName()); }
            if (imageVersion.getVersionTitleLng1().length()==0) {imageVersion.setVersionTitleLng1(file.getName()); }
            if (imageVersion.getVersionTitleLng2().length()==0) {imageVersion.setVersionTitleLng2(file.getName()); }
        }

        try {
            imageService.saveImageVersion(imageVersion);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return returnValue;
    }

    /**
     * Importiert ein Bild und gibt zurück ob dabei ein fehler aufgetreten ist
     * @param fileName
     * @param userId
     * @return
     * @throws SizeExceedException
     */
    public static int processImage(String fileName,int userId) throws SizeExceedException {

        //todo: MetadataException werfen und dafür importedIvid zurückgeben
        IImageUtil imageUtil = new ImageMagickUtil(true);
        return processImage(fileName, imageUtil,userId);
    }

    public static void copyFile(File in, File out) throws Exception {

        //todo: in eine Tool oder Util Klasse exportieren

        FileInputStream fis  = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        byte[] buf = new byte[1024];
        int i = 0;
        while((i=fis.read(buf))!=-1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
    }

    /**
     * Gibt die ID des Bildes zurück das importiert wurde...
     * @return
     */
    public static int getImportedIvid() {
        //todo: ändern damit processImage das zurückgibt...
        return importedIvid;
    }

}
