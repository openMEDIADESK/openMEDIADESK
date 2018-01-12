package com.stumpner.mediadesk.media.importing;

import eu.medsea.mimeutil.MimeUtil;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;

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
 * Eine Fabrik, die MediaImportHandler erstellt.
 * Mit ImportHandler können verschieden Filetypen in die Datenbank importiert werden
 */
public class AbstractImportFactory implements ImportFactory {

    private Map importHandlerMap = new HashMap(); // <mimeType:importHandler>
    private MediaImportHandler defaultImportHandler = null;

    public AbstractImportFactory() {

        //todo hier müssen die Handler registriert werden:
    }

    /**
     * Gibt ein MediaImportHandler-Objekt zurück, mit dem dann die Datei importiert werden kann
     * @param file die zu importierende Datei
     * @return einen MediaImportHandler für diese Datei
     */
    public MediaImportHandler createMediaImportHandler(File file) throws MimeTypeNotSupportedException {

        //Prüfen um welche Datei es sich Handelt:

        String mimeType = getMimeType(file);
        //System.out.println("File: "+file.getName());
        //System.out.println("MimeType: "+mimeType);

        MediaImportHandler importHandler = (MediaImportHandler)importHandlerMap.get(mimeType);

        if (importHandler==null) {
            if (defaultImportHandler==null) {
                throw new MimeTypeNotSupportedException(mimeType,file);
            } else {
                return defaultImportHandler;
            }
        } else { return importHandler; }
    }

    public static String getMimeType(File file) {

        final String UNKNOWN_MIME_TYPE="application/x-unknown-mime-type";
        //System.out.println("Extention: "+ getExtension(file));
        String mimeType = getMimeTypeFromExt(getExtension(file));
        //System.out.println("MimeType: "+ mimeType);
        
        if(UNKNOWN_MIME_TYPE.equalsIgnoreCase(mimeType) || mimeType == null) {
            //Wenn der Mime-Type aufgrund der Extension nicht ermittelt werden kann,
            //muss er aufgrund des File-Headers/Bytes gelesen werden
            //System.out.println("Mime-Type aus den Byte-Header lesen:");
            mimeType = MimeUtil.getMimeTypes(file).toString();
        }
        if(mimeType == null) {
            mimeType = UNKNOWN_MIME_TYPE;
        }
        return mimeType;

    }

    private static String getExtension(File file) {
        //Wird nichtmehr verwendet, weil diese Methode alles nach dem ersten Punkt zurück gab
        //return MimeUtil.getExtension(file);

        String filename = file.getName();
        int indexOfExtentionSeperator = filename.lastIndexOf(".");
        if (indexOfExtentionSeperator==-1) {
            return "";
        } else {
            return filename.substring(indexOfExtentionSeperator+1);
        }
    }

    public static String getMimeTypeFromExt(String extension) {

        Map mimeTypeMap = new HashMap();
        mimeTypeMap.put("JPG","image/jpeg");
        mimeTypeMap.put("JPEG","image/jpeg");
        mimeTypeMap.put("TIF","image/tiff");
        mimeTypeMap.put("TIFF","image/tiff");
        mimeTypeMap.put("GIF","image/gif");
        mimeTypeMap.put("BMP","image/bmp");
        mimeTypeMap.put("PNG","image/png");
        mimeTypeMap.put("EPS","image/x-eps");

        mimeTypeMap.put("ZIP","application/zip");
        mimeTypeMap.put("PDF","application/pdf");
        mimeTypeMap.put("EXE","application/exe");
        mimeTypeMap.put("PPT","application/mspowerpoint");
        mimeTypeMap.put("PPTX","application/mspowerpoint");
        mimeTypeMap.put("XLS","application/vnd.ms-excel");
        mimeTypeMap.put("XLSX","application/vnd.ms-excel");
        mimeTypeMap.put("CSV","text/csv");
        mimeTypeMap.put("DOC","application/msword");
        mimeTypeMap.put("DOCX","application/msword");
        //mimeTypeMap.put("MPG","audio/mpeg");
        mimeTypeMap.put("HTML","text/html");
        mimeTypeMap.put("HTM","text/html");
        mimeTypeMap.put("XML","text/xml");
        mimeTypeMap.put("GZIP","application/x-gzip");
        mimeTypeMap.put("MOV","video/quicktime");
        mimeTypeMap.put("PSD","application/octet-stream");
        mimeTypeMap.put("DWF","model/vnd.dwf");
        mimeTypeMap.put("DWG","image/vnd.dwg");
        mimeTypeMap.put("DXF","image/vnd.dwg");
        //Audio
        mimeTypeMap.put("MP2","audio/mpeg");
        mimeTypeMap.put("MP3","audio/mpeg");
        mimeTypeMap.put("WAV","audio/wav");
        //Video
        mimeTypeMap.put("MP4","video/mp4");
        mimeTypeMap.put("MPG","video/mpeg");
        mimeTypeMap.put("MPEG","video/mpeg");
        mimeTypeMap.put("MPGA","video/mpeg");
        mimeTypeMap.put("AVI","video/avi");
        mimeTypeMap.put("MOV","video/quicktime");
        mimeTypeMap.put("3GP","video/3gpp");
        mimeTypeMap.put("M4V","video/mp4");
        mimeTypeMap.put("WEBM","video/webm");

        //plain
        mimeTypeMap.put("TXT", "text/plain");

        //todo: more mime types

        String mimeType = (String)mimeTypeMap.get(extension.toUpperCase());
        if (mimeType==null) {
            mimeType = "application/x-unknown-mime-type";
        }

        return mimeType;
    }

    public static String getFileExtention(File file) {
        return getExtension(file);
    }

    public static String getFileExtention(String file) {
        return getExtension(new File(file));
    }

    /**
     * Gibt zurück ob dieser Medientyp von der MediaFactory unterstützt wird
     * @param file Datei die geprüft werden soll
     * @return true wenn diese Factory einen Handler für diese Datei zurückliefern kann
     */
    public boolean isMediaSupported(File file) {
        return false;
    }

    public void registerImportHandler(String mimeType, MediaImportHandler importHandler) {
        importHandlerMap.put(mimeType,importHandler);
    }

    public void setDefaultImportHandler(MediaImportHandler defaultImportHandler) {
        this.defaultImportHandler = defaultImportHandler;
    }
}
