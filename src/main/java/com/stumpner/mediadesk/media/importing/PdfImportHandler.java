package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.image.util.IImageUtil;
import com.stumpner.mediadesk.image.util.ImageMagickUtil;
import com.stumpner.mediadesk.core.Config;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;
import com.stumpner.mediadesk.util.UploadNotificationService;
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
 * Date: 08.02.2012
 * Time: 20:38:30
 * To change this template use File | Settings | File Templates.
 */
public class PdfImportHandler extends BinaryFileImportHandler {

    private static String executeCommand = "";

    public int processImport(File file, int userId) throws SizeExceedException, FileRejectException {

        int ivid = super.processImport(file, userId);

        //ImageVersionService mediaService = new ImageVersionService();
        //ImageVersionMultiLang mediaObject = (ImageVersionMultiLang) mediaService.getImageVersionById(ivid);

        String filenameJpegExtract = file.getPath() + System.currentTimeMillis() + "_pdf.jpg";
        createJpegFromPdf(file, filenameJpegExtract);

        IImageUtil imageUtil = new ImageMagickUtil(true);
        //images verkleinern horizontal
        imageUtil.resizeImageVertical(
                filenameJpegExtract, Config.imageStorePath + File.separator + ivid + "_1", Config.imagesizeThumbnail);
        imageUtil.resizeImageVertical(
                filenameJpegExtract, Config.imageStorePath + File.separator + ivid + "_2", Config.imagesizePreview);
        imageUtil.overlayWatermark(Config.imageStorePath + File.separator + ivid + "_2", Config.watermarkHorizontal);

        File jpegFile = new File(filenameJpegExtract);
        jpegFile.delete();
        file.delete();

        UploadNotificationService uns = new UploadNotificationService();
        uns.triggerUpload(ivid);

        return ivid;
    }

    private void createJpegFromPdf(File file, String filenameJpegExtract) {

        Logger logger = Logger.getLogger(VideoImportHandler.class);
        try {
            Runtime rt = Runtime.getRuntime();
            String command = executeCommand + " gs -sDEVICE=jpeg -sOutputFile=" + filenameJpegExtract + " -r300 -dNOPAUSE -dBATCH -q -dFirstPage=1 -dLastPage=1 " + file.getAbsolutePath();
            //System.out.println(command);
            Process proc = rt.exec(command);
            //System.out.println("Ghostscript PDF Convert");
            InputStream is = proc.getInputStream();
            String string = new String();
            StringBuffer stdOutSb = new StringBuffer("");
            byte[] buf = new byte[32];
            int len;
            while ((len = is.read(buf)) > 0) {
                string = new String(buf);
                stdOutSb = stdOutSb.append(string);
            }
            //System.out.println(stdOutSb.toString());
            is.close();

            InputStream es = proc.getErrorStream();
            logger.error("ERROR:");
            byte[] esbuf = new byte[4096];
            int eslen;
            while ((eslen = es.read(esbuf)) > 0) {
                string = new String(esbuf);
                logger.error("ERRORBUF: " + string);
            }
            is.close();

            proc.waitFor();
            int exitVal = proc.exitValue();
            //System.out.println("Ghostscript ps: " + exitVal);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
