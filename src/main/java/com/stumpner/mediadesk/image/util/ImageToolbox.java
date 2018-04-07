package com.stumpner.mediadesk.image.util;

import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.core.Config;

import java.io.File;

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
 * Date: 22.05.2007
 * Time: 17:40:29
 * To change this template use File | Settings | File Templates.
 *
 * Diese Klasse stellt Funktionen für die Bildmanipulation zur Verfügung:
 * - Bilder resizen, Wasserzeichen neu machen usw
 */
public class ImageToolbox {

    public void generateThumbnail(MediaObject imageVersion) {

        IImageUtil imageUtil = new ImageMagickUtil(true);
        Logger logger = Logger.getLogger(ImageToolbox.class);
        /**
         * Dateiname des "Original-Files"
         */
        String fileName = Config.imageStorePath+File.separator+imageVersion.getIvid()+"_0";
        String fileNameJpg = Config.imageStorePath+File.separator+imageVersion.getIvid()+"_0.jpg";
        imageUtil.convertToJpeg(fileName, fileNameJpg);

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

        //bereits existierende Thumbnails löschen:
        File previewFile = new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2");
        if (previewFile.exists()) {
            previewFile.delete();
        }
        File thumbnailFile = new File(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1");
        if (thumbnailFile.exists()) {
            thumbnailFile.delete();
        }

        //images verkleinern vertical
        if (orientation == 1 || orientation == 0) {
            imageUtil.resizeImageVertical(
                    fileNameJpg,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.imagesizePreview);
            imageUtil.overlayWatermark(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.watermarkVertical);

            imageUtil.resizeImageVertical(
                    fileNameJpg,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1",Config.imagesizeThumbnail);
        }

        //images verkleinern horizontal
        if (orientation == 2) {
            imageUtil.resizeImageHorizontal(
                    fileNameJpg,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.imagesizePreview);
            imageUtil.overlayWatermark(Config.imageStorePath+File.separator+imageVersion.getIvid()+"_2",Config.watermarkHorizontal);

            imageUtil.resizeImageHorizontal(
                    fileNameJpg,Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1",Config.imagesizeThumbnail);
        }

        //JPG Konvertierte File löschen
        File fileJpg = new File(fileNameJpg);
        fileJpg.delete();

    }

}
