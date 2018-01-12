package com.stumpner.mediadesk.media.importing;

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
 * Date: 03.12.2007
 * Time: 20:58:17
 * To change this template use File | Settings | File Templates.
 */
public class ImageImportFactory extends AbstractImportFactory {


    public ImageImportFactory() {

        //Grafik-Dateien:
        ImageImportHandler imageImportHandler = new ImageImportHandler();
        registerImportHandler("image/jpeg",imageImportHandler);
        registerImportHandler("image/jpeg,image/pjpeg",imageImportHandler);
        registerImportHandler("image/tiff",imageImportHandler);
        registerImportHandler("image/tiff,image/x-tiff",imageImportHandler);
        registerImportHandler("image/gif",imageImportHandler);
        registerImportHandler("image/bmp",imageImportHandler);
        registerImportHandler("image/bmp,image/x-windows-bmp",imageImportHandler);
        registerImportHandler("image/png",imageImportHandler);

    }
}
