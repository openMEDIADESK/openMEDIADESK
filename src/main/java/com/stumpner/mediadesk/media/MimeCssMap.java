package com.stumpner.mediadesk.media;

import java.util.HashMap;

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
 * Date: 21.10.2007
 * Time: 20:23:37
 */
public class MimeCssMap extends HashMap {


    public MimeCssMap() {

        //alle Mimetypes und CSS-Stile erstellen:

        //put("vnd.oasis.opendocument.text","mimeOpenDocumentText");  //Open Office Dokument
        put("application/pdf","mimePdf");
        put("application/exe","mimeExe");
        put("image/jpg","mimePicture");
        put("image/jpeg","mimePicture");
        put("image/tiff","mimePicture");
        put("image/gif","mimePicture");
        put("image/bmp","mimePicture");
        put("image/png","mimePicture");
        put("application/vnd.ms-powerpoint","mimePpt");
        put("application/mspowerpoint","mimePpt");
        put("application/vnd.ms-excel","mimeXls");
        put("application/vndms-excel","mimeXls");
        put("text/csv","mimeCsv");
        put("application/msword","mimeDoc");
        put("application/vnd.oasis.opendocument.text","mimeDoc");
        put("application/zip","mimeZip");
        //Video
        put("video/mp4","mimeVideo");
        put("video/3gp","mimeVideo");
        put("video/mpeg","mimeVideo");
        put("video/quicktime","mimeVideo");
        //Audio
        put("audio/mpeg3","mimeAudio");
        put("audio/mpeg2","mimeAudio");
        put("audio/mpeg","mimeAudio");
    }
}
