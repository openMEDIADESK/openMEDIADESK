package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;

import java.io.File;

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
 * Time: 21:10:40
 * To change this template use File | Settings | File Templates.
 */
public interface ImportFactory {
    MediaImportHandler createMediaImportHandler(File file) throws MimeTypeNotSupportedException;

    boolean isMediaSupported(File file);

    void registerImportHandler(String mimeType, MediaImportHandler importHandler);
}
