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
 * Date: 16.04.2009
 * Time: 20:45:07
 * To change this template use File | Settings | File Templates.
 */
public class MediaImportFactory extends AbstractImportFactory {

    public MediaImportFactory() {

        //Bilder + Grafiken:
        MediaImportHandler imageImportHandler = new ImageImportHandler();
        registerImportHandler("image/jpeg",imageImportHandler);
        registerImportHandler("image/jpeg,image/pjpeg",imageImportHandler);
        registerImportHandler("image/tiff",imageImportHandler);
        registerImportHandler("image/tiff,image/x-tiff",imageImportHandler);
        registerImportHandler("image/gif",imageImportHandler);
        registerImportHandler("image/bmp",imageImportHandler);
        registerImportHandler("image/bmp,image/x-windows-bmp",imageImportHandler);
        registerImportHandler("image/png",imageImportHandler);
        registerImportHandler("image/psd",imageImportHandler);
        //todo: registerImportHandler("image/x-eps",imageImportHandler);
        //nicht als Bild behandeln registerImportHandler("application/pdf",imageImportHandler); (als default/binary)

        //Videos
        VideoImportHandler videoImportHandler = new VideoImportHandler();
        registerImportHandler("video/mp4",videoImportHandler);
        registerImportHandler("video/3gpp",videoImportHandler);
        registerImportHandler("video/mpeg",videoImportHandler);
        registerImportHandler("video/avi",videoImportHandler);
        registerImportHandler("video/mov",videoImportHandler);
        registerImportHandler("video/webm",videoImportHandler);

        //MP3s
        AudioImportHandler audioImportHandler = new AudioImportHandler();
        registerImportHandler("audio/mpeg",audioImportHandler);
        registerImportHandler("audio/mpeg3",audioImportHandler);
        registerImportHandler("audio/mpeg2",audioImportHandler);
        registerImportHandler("audio/wav",audioImportHandler);

        //PDF
        PdfImportHandler pdfImportHandler = new PdfImportHandler();
        registerImportHandler("application/pdf", pdfImportHandler);

        //Alle anderen:
        MediaImportHandler binaryImportHandler = new BinaryFileImportHandler();
        setDefaultImportHandler(binaryImportHandler);
    }
}
