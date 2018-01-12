package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.image.util.ImageImport;

import java.io.File;

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
 * Import-Handler zum importieren von Grafik-Dateien
 * User: franz.stumpner
 * Date: 15.10.2007
 * Time: 05:12:21
 */
public class ImageImportHandler implements MediaImportHandler {
    
    public int processImport(File file, int userId) throws SizeExceedException, FileRejectException {
        ImageImport.processImage(file.getAbsolutePath(),userId);
        UploadNotificationService uns = new UploadNotificationService();
        uns.triggerUpload(ImageImport.getImportedIvid());
        return ImageImport.getImportedIvid();
    }

    public boolean isSupported(String mimeType) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
