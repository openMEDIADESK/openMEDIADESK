package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.util.WebFileUploadBean;
import com.stumpner.mediadesk.core.Config;

import java.io.*;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

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
 * Date: 14.04.2010
 * Time: 21:15:29
 * To change this template use File | Settings | File Templates.
 */
public class TemplateUploadController extends FileUploadController {

    protected String getDestinationPath(String originalFilename, WebFileUploadBean upload) {
        return Config.getTemplateArchivePath() + File.separator + originalFilename;
    }

    protected void doAfterFileUpload(File file) {

        try {
            //Template-Pfad anlegen:
            File templateArchivePath = new File(Config.getTemplateArchivePath() + File.separator + file.getName().replaceAll(".ZIP","").replaceAll(".zip",""));
            templateArchivePath.mkdirs();
            //Zip-Datei entpacken
            ZipFile templateArchive = new ZipFile(file);
            Enumeration enumeration = templateArchive.entries();

            byte[] buffer = new byte[16384];
            int len;
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
                String entryFileName = zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(new File(templateArchivePath,entryFileName))
                    );

                    BufferedInputStream bis = new BufferedInputStream(templateArchive.getInputStream(zipEntry));

                    while ((len = bis.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                    }

                    bos.flush();
                    bos.close();
                    bis.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    protected String getTextHeadline() {
        return "set.templateupload.headline";  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected String getTextSubheadline() {
        return "set.templateupload.subheadline";  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected String getTextInfo() {
        return "set.templateupload.info";  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected String getNextUrl() {
        return "settemplates";
    }

    protected String getTextSuccess() {
        return "set.templateupload.success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected String getHtmlCode() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
