package com.stumpner.mediadesk.upload.plugin;

import com.stumpner.mediadesk.upload.ImportPluginHandler;
import com.stumpner.mediadesk.upload.PluginResult;
import com.stumpner.mediadesk.upload.PluginContext;
import com.stumpner.mediadesk.core.IMediaObject;

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
 * User: stumpner
 * Date: 13.11.2015
 * Time: 09:50:29
 */
public class EmptyImportPlugin implements ImportPluginHandler {

    public PluginResult validateFile(File file, PluginContext ctx) {
        return new PluginResult(PluginResult.Status.OK, "EmptyImportPlugin validateFile with "+file.getAbsolutePath(), null);
    }

    public PluginResult process(IMediaObject mediaObject, PluginContext ctx) {
        return new PluginResult(PluginResult.Status.OK, "EmptyImportPlugin process with "+mediaObject.getIvid(), null);
    }

    public PluginResult check(PluginContext ctx) throws Exception {
        return null;
    }

    public void setParameter(String parameter) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
