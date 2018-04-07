package com.stumpner.mediadesk.upload.plugin;

import com.stumpner.mediadesk.core.IMediaObject;
import com.stumpner.mediadesk.upload.ImportPluginHandler;
import com.stumpner.mediadesk.upload.PluginResult;
import com.stumpner.mediadesk.upload.PluginContext;
import com.stumpner.mediadesk.upload.FileRejectException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.philvarner.clamavj.ClamScan;
import com.philvarner.clamavj.ScanResult;

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
 * Verwendet https://github.com/philvarner/clamavj um die zu importierende Datei nach Viren zu prüfen
 * Homepage des Authors http://www.philvarner.com/2010/03/06/clamavj-a-java-library-for-accessing-the-clamav-clamd-daemon/
 *
 * Nach einer Idee von http://soniyj.altervista.org/blog/free-solution-for-check-infected-files-with-java-and-clamav/
 * User: stumpner
 * Date: 13.11.2015
 * Time: 11:03:41
 */
public class ClamavjImportPlugin implements ImportPluginHandler {

    private String host = "localhost";
    private int port = 3310;
    private int timeout = 10000; //Timeout in ms

    public PluginResult validateFile(File file, PluginContext ctx) throws FileRejectException {

        ClamScan clamScan = new ClamScan(host,port,timeout);

        try {
            ScanResult result = clamScan.scan(new FileInputStream(file));
            System.out.println("ClamAV Scan Host "+host);
            /* DEBUGGING
            System.out.println("result.getStatus(): "+result.getStatus());
            System.out.println("result.getResult(): "+result.getResult());
            System.out.println("result.getSignature(): "+result.getSignature());
            System.out.println("clamScan.stats(): "+clamScan.stats());*/

            switch (result.getStatus()) {
                case PASSED:
                    //Kein Virus gefunden
                    return new PluginResult(PluginResult.Status.OK, "ClamAV Scan: PASSED", result);
                case FAILED:
                    //Virus gefunden
                    if (result.getSignature().length()>0) {
                        System.out.println("ClamAV Scan: Virus Found: "+result.getSignature());
                        //return new PluginResult(PluginResult.Status.WARNING, "ClamAV Scan: Virus Found: "+result.getSignature(), result);
                        throw new FileRejectException("Clam AV Scan Virus Found: "+result.getSignature());
                    } else {
                        //Keine Virensignatur zur�ckgegeben - wom�glich Timeout beim clamav (gro�e Datei?!)
                        return new PluginResult(PluginResult.Status.ERROR, "Timeout-Error in ClamAV at host "+host, result);
                    }
                case ERROR:
                    //Fehler
                    return new PluginResult(PluginResult.Status.ERROR, "Error in ClamAV at host "+host, result);
            }
            return new PluginResult(PluginResult.Status.ERROR, "", result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Fehler - nicht bis zum Ende durchgelaufen
        return new PluginResult(PluginResult.Status.ERROR, "Fehler beim Virenscan mit Clamav", null);
    }

    public PluginResult process(IMediaObject IMediaObject, PluginContext ctx) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PluginResult check(PluginContext ctx) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParameter(String parameter) {
        this.host = parameter.trim();
    }
}
