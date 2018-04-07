package com.stumpner.mediadesk.web.mvc;

import java.util.LinkedList;
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
 * Time: 19:14:56
 * To change this template use File | Settings | File Templates.
 */
public class ImportFtpCommand {

    public final static int AUTOIMPORT_INBOX = 1;
    public final static int AUTOIMPORT_FOLDER = 2;
    public final static int AUTOIMPORT_CATEGORY = 3;
    String[] importFiles = new String[0];
    boolean autoImportEnabled = false;
    int autoImportFtpCat = 0;
    List categoryList = new LinkedList();

    List fileList = new LinkedList();

    String ftpHost = "";
    String ftpUser = "";
    String ftpPassword = "";

    public List getFileList() {
        return fileList;
    }

    public void setFileList(List fileList) {
        this.fileList = fileList;
    }

    public void setImportFiles(String[] importFiles) {
        this.importFiles = importFiles;
    }

    public boolean isAutoImportEnabled() {
        return autoImportEnabled;
    }

    public void setAutoImportEnabled(boolean autoImportEnabled) {
        this.autoImportEnabled = autoImportEnabled;
    }

    public List getCategoryList() {
        return categoryList;
    }

    public void setFolderList(List categoryList) {
        this.categoryList = categoryList;
    }

    public int getAutoImportFtpCat() {
        return autoImportFtpCat;
    }

    public void setAutoImportFtpCat(int autoImportFtpCat) {
        this.autoImportFtpCat = autoImportFtpCat;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
}
