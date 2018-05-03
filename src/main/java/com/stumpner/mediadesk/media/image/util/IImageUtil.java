package com.stumpner.mediadesk.media.image.util;

import java.util.List;

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
 * Time: 19:36:28
 * To change this template use File | Settings | File Templates.
 */
public interface IImageUtil {

    /**
     * Resizes the BasicMediaObject to the given Horizontal value
     * The BasicMediaObject is propertional resized, getting the exact value given by width, height is variable
     * @param originalFileName
     * @param resizeFileName
     * @param width
     */
    public void resizeImageHorizontal(String originalFileName, String resizeFileName, int width);

    /**
     * The Opposite of resizeImageHorizontal, here the height value is exactly resized, width
     * is proportional
     * @param originalFileName
     * @param resizeFileName
     * @param height
     */
    public void resizeImageVertical(String originalFileName, String resizeFileName, int height);

    /**
     * Overlays the given image with an watermark, the image and the watermark is not resized
     * @param originalFileName
     * @param watermarkFile
     */
    public void overlayWatermark(String originalFileName, String watermarkFile);

    public List getImageBulkData(String fileName) throws MetadataReadException;

    public int getImageSize(String fileName);

    public int getImageWidth(String fileName);

    public int getImageHeight(String fileName);

    public int getImageDpi(String fileName);

    /**
     * Konvertiert die Datei in jedem fall in ein JPEG
     * @param originalFileName
     * @param jpegFilename
     * @return
     */
    public boolean convertToJpeg(String originalFileName, String jpegFilename);

}
