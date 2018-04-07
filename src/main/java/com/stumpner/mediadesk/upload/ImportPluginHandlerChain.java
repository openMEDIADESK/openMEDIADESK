/**
 * User: stumpner
 * Date: 13.11.2015
 * Time: 09:17:03
 */
package com.stumpner.mediadesk.upload;

import com.stumpner.mediadesk.core.IMediaObject;

import java.util.List;
import java.util.ArrayList;
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

public class ImportPluginHandlerChain {

    private static ImportPluginHandlerChain ourInstance = new ImportPluginHandlerChain();

    private List<ImportPluginHandler> pluginList = new ArrayList<ImportPluginHandler>();

    public static ImportPluginHandlerChain getInstance() {
        return ourInstance;
    }

    private ImportPluginHandlerChain() {
    }

    public void add(ImportPluginHandler pluginHandler) {
        pluginList.add(pluginHandler);
    }

    /**
     * Arbeitet die Plugins in der Kette ab...
     * @param file
     * @return
     */
    public PluginResult validateFile(File file) throws FileRejectException {

        List <PluginResult> returnDataList = new ArrayList();
        PluginResult.Status returnStatus = PluginResult.Status.OK;

        for (ImportPluginHandler pluginHandler : pluginList) {
            System.out.println("Processing ImportPluginHandler [stage:validateFile] "+pluginHandler.getClass().getName()+"...");
            PluginResult returnData = pluginHandler.validateFile(file, new PluginContext() { });
            if (returnData!=null) { //returnData = null wenn das Plugin nichts zur�ckliefert (auch m�glich!)
                System.out.println("Result: "+returnData.getStatus()+" Message: "+returnData.getMessage());
                returnDataList.add(returnData);
                switch (returnData.getStatus()) {
                    case WARNING:
                        if (returnStatus!= PluginResult.Status.ERROR) returnStatus = returnData.getStatus();
                        break;
                    case ERROR:
                        returnStatus = returnData.getStatus();
                        break;
                }
            }
        }

        return new PluginResult(returnStatus, pluginList.size()+" Plugins processed", returnDataList);
    }

    /**
     * Arbeitet die Plugins in der Kette ab...
     * @param mo
     * @return
     */
    public PluginResult process(IMediaObject mo) {

        List <PluginResult> returnDataList = new ArrayList();
        PluginResult.Status returnStatus = PluginResult.Status.OK;

        for (ImportPluginHandler pluginHandler : pluginList) {
            System.out.println("Processing ImportPluginHandler [stage:process] "+pluginHandler.getClass().getName()+"...");
            PluginResult returnData = pluginHandler.process(mo, new PluginContext() { });
            if (returnData!=null) { //returnData = null wenn das Plugin nichts zur�ckliefert (auch m�glich!)
                System.out.println("Result: "+returnData.getStatus()+" Message: "+returnData.getMessage());
                returnDataList.add(returnData);
                switch (returnData.getStatus()) {
                    case WARNING:
                        if (returnStatus!= PluginResult.Status.ERROR) returnStatus = returnData.getStatus();
                        break;
                    case ERROR:
                        returnStatus = returnData.getStatus();
                        break;
                }
            }
        }

        return new PluginResult(returnStatus, pluginList.size()+" Plugins processed", returnDataList);
    }

    /**
     * Initialisiert die Plugin-Handler Chain. Dabei werden eventuell bereits hinzugef�gte Plugins wieder entfernt
     */
    public void init() {
        pluginList = new ArrayList<ImportPluginHandler>();
    }
}
