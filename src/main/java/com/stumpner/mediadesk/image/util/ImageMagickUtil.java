package com.stumpner.mediadesk.image.util;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.ImageInfo;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import com.drew.metadata.MetadataException;
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
 * Date: 28.04.2005
 * Time: 22:26:04
 * To change this template use File | Settings | File Templates.
 */
public class ImageMagickUtil implements IImageUtil {

    private String executeCommand = "";//"cmd /c ";
    //private String executeCommand = "cmd /c ";

    public ImageMagickUtil(boolean strip) {

        this.strip = strip;
        if (System.getenv("OS")!=null) {
            if (System.getenv("OS").indexOf("Windows")!=-1) {
                this.executeCommand = "cmd /c";
            }
        }
    }

    private boolean strip = false;

    public void resizeImageHorizontal(String originalFileName, String resizeFileName, int width) {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        logger.info("Resize Horizontal");
        this.resize(originalFileName,width,0,resizeFileName);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void resizeImageVertical(String originalFileName, String resizeFileName, int height) {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        logger.info("Resize Vertical");
        this.resize(originalFileName,0,height,resizeFileName);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void overlayWatermark(String originalFileName, String watermarkFile) {

        this.imageOverlay(0,0,this.getImageWidth(watermarkFile),this.getImageHeight(watermarkFile),originalFileName,watermarkFile);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public synchronized List getImageBulkData(String fileName) throws MetadataReadException {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);

        List metadataList = new LinkedList();
        JpegMetadataReader jpegReader;
        File jpegFile = new File(fileName);


        try {
        //InputStream is = (InputStream)new


                Metadata metadata = new Metadata();
                try {
                    if (Config.fileEncoding.equalsIgnoreCase("")) {
                        logger.debug("Read JPEG Metadata [mit standard Fileencoding]");
                        metadata = JpegMetadataReader.readMetadata(jpegFile);
                    } else {
                        logger.debug("Read JPEG Metadata [mit Fileencoding: "+Config.fileEncoding+"]");
                        metadata = JpegMetadataReader.readMetadata(jpegFile,Config.fileEncoding);
                    }



                } catch (ArrayIndexOutOfBoundsException e) {
                    logger.error("Probleme beim laden der Metadaten!");
                    e.printStackTrace();
                    throw new MetadataReadException();
                } 
                Iterator directories = metadata.getDirectoryIterator();
            logger.debug("Exif, IPTC Directory durchsuchen...");
                while(directories.hasNext()) {
                    Directory directory = (Directory)directories.next();
                    Iterator tags = directory.getTagIterator();
                    while(tags.hasNext()) {
                        Tag tag = (Tag)tags.next();
                        com.stumpner.mediadesk.image.Metadata susideMetadata = new com.stumpner.mediadesk.image.Metadata();
                        susideMetadata.setMetaKey(tag.getTagName());
                        //new: directory wird abgespeichert... (aber nicht in der db!)
                        susideMetadata.setDirectory(directory.getName());
                        try {
                            susideMetadata.setMetaValue(tag.getDescription());
                            metadataList.add(susideMetadata);

//                            System.out.println("TAG: "+tag.getDirectoryName());
//                            System.out.println("KEY: "+tag.getTagName());
//                            System.out.println("VAL: "+tag.getDescription());
                        } catch (MetadataException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        /*
                        try {
                            System.out.println("TagNAME: "+tag.getTagName()+", TagDESCRIPTION: "+tag.getDescription()+", TagDIRECTORY: "+tag.getDirectoryName());
                        } catch (MetadataException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        */
                    }
                }
            } catch (JpegProcessingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        return metadataList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageSize(String fileName) {
        return 0;
    }

    public int getImageWidth(String fileName) {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        int width = -1;
        ImageInfo imageInfo = new ImageInfo();
        try {
            InputStream is = new FileInputStream(fileName);
            imageInfo.setInput(is);
            if (imageInfo.check()) {
                width = imageInfo.getWidth();

                logger.info("Imagewidth: "+width);
            } else {
                logger.error("Not a supported image file format.");
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return width;
    }

    public int getImageHeight(String fileName) {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        int height = -1;
        ImageInfo imageInfo = new ImageInfo();
        try {
            InputStream is = new FileInputStream(fileName);
            imageInfo.setInput(is);
            if (imageInfo.check()) {
                height = imageInfo.getHeight();
                logger.info("Imageheight: "+height);
            } else {
                logger.error("Not a supported image file format.");
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return height;
    }

    public int getImageDpi(String fileName) {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        int dpi = -1;
        ImageInfo imageInfo = new ImageInfo();
        try {
            InputStream is = new FileInputStream(fileName);
            imageInfo.setInput(is);
            if (imageInfo.check()) {
                dpi = imageInfo.getPhysicalHeightDpi();
                logger.info("BasicMediaObject-dpi: "+dpi);
            } else {
                logger.error("Not a supported image file format.");
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dpi;
    }

    public boolean convertToJpeg(String originalFileName, String jpegFilename) {
        this.convert(originalFileName,jpegFilename);
        return true;
    }

    public void convert(String srcFile, String dstFile) {

        //System.out.println("[mediaDESK-"+Config.instanceName+"] ImageMagickUtil.convert(srcFile="+srcFile+", dstFile="+dstFile+")");
        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        try
        {
            String cmd = "convert "+srcFile+" "+dstFile;
            int exitVal = executeSystemCommand(cmd);
            System.out.println("[mediaDESK-"+Config.instanceName+"] imagemagick-proc.exitValue()="+exitVal);
            logger.info("BasicMediaObject Magick Process exitValue: " + exitVal);
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
    }

    public void resize(String srcFile, int pixelX, int pixelY, String dstFile) {

        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        String widthX = "";
        String heightY = "";

        widthX =  (pixelX>0) ? Integer.toString(pixelX) : "";
        heightY = (pixelY>0) ? Integer.toString(pixelY) : "";

        String stripCmd = "";
        if (strip) {
            //stripCmd = "-strip ";
        }

        try
        {
            //Neu seit 2.5 sr3: vorschau-resize immer in RGB Konvertieren
            //String cmd = "convert "+srcFile+" -resize "+widthX+"x"+heightY+" "+stripCmd+"-colorspace sRGB "+dstFile;
            //Testweise ohne Strip und ohne colorspace
            String cmd = "convert "+srcFile+" -thumbnail "+widthX+"x"+heightY+" "+stripCmd+"-colorspace RGB jpg:"+dstFile;
            //System.out.println(cmd);
            int exitVal = executeSystemCommand(cmd);
            logger.info("BasicMediaObject Magick convert: "+srcFile+" --> "+dstFile);
            //System.out.println("BasicMediaObject-Magick returns: "+exitVal);
            logger.info("Process exitValue: " + exitVal);
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
    }

    private int executeSystemCommand(String cmd) throws IOException, InterruptedException {

        Runtime rt = Runtime.getRuntime();
        String[] env = new String[] { "MAGICK_THREAD_LIMIT=1" };
        //System.out.println("[mediaDESK-"+Config.instanceName+"] executeSystemCommand: "+cmd);
        Process proc = rt.exec(executeCommand+cmd,env);

            InputStream is = proc.getErrorStream();

                int nbytes = -1;
                  byte b[] = new byte[10000];
                  while((nbytes = is.read(b,0,10000)) != -1) {
                      System.out.write(b,0,nbytes);
                  }
            is.close();

            InputStream is2 = proc.getInputStream();

                int nbytes2 = -1;
                  byte b2[] = new byte[10000];
                  while((nbytes2 = is2.read(b2,0,10000)) != -1) {
                      System.out.write(b2,0,nbytes2);
                  }
            is.close();

        proc.waitFor();
        return proc.exitValue();
    }

    public void imageOverlay(int startX, int startY, int overlayWidth, int overlayHeight, String srcFilename, String overlayFilename) {

        //todo: method ändern da ja nur watermakfile und srcfile benötigt wird


        Logger logger = Logger.getLogger(ImageMagickUtil.class);
        logger.info("BasicMediaObject overlay: "+srcFilename);
        try
        {   //String comm = executeCommand+"convert -draw "+((char)34)+"image Over "+x+","+y+" 80,40 "+((char)39)+""+overlayFilename+""+((char)39)+""+((char)34)+" "+ srcFilename +" "+srcFilename;
            //### use shell-script-WRAPPER
            //String comm = executeCommand+"watermark.sh "+startX+" "+startY+" "+overlayWidth+" "+overlayHeight+" "+overlayFilename+" "+srcFilename;
            String comm = "composite -watermark "+Config.watermarkIntensity+" -gravity "+getGravity()+" "+overlayFilename+" "+srcFilename+" "+srcFilename;
            int exitVal = executeSystemCommand(comm);
            logger.info("Process exitValue: " + exitVal);
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
          
    }

    private String getGravity() {

        String gravity = "Center";
        switch(Config.gravity) {
            case 1:
                gravity = "NorthWest";
                break;
            case 2:
                gravity = "North";
                break;
            case 3:
                gravity = "NorthEast";
                break;
            case 4:
                gravity = "West";
                break;
            case 5:
                gravity = "Center";
                break;
            case 6:
                gravity = "East";
                break;
            case 7:
                gravity = "SouthWest";
                break;
            case 8:
                gravity = "South";
                break;
            case 9:
                gravity = "SouthEast";
                break;
        }
        return gravity;
    }
}
