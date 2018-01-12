package com.stumpner.mediadesk.web.mvc.commandclass.settings;

import java.util.List;
import java.util.LinkedList;

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
 * Date: 17.10.2006
 * Time: 05:01:43
 * To change this template use File | Settings | File Templates.
 */
public class ImportSettings {

    String importName = "Job Name";
    String importTitle = "Job Name";
    String importNumber = "Object Name";
    String importByline = "Copyright Notice";
    String importPhotographerAlias = "By-line";
    String importSite = "By-line Title";
    String importPeople = "Writer/Editor";
    String importInfo = "Caption/Abstract";

    String importSubtitle = "----------";
    String importKeywords = "----------";
    String importNote = "--------";
    String importRestrictions = "-----------";

    String importDate = "Date/Time";
    boolean importImageNumberSerially = false;
    String fileEncoding = "";

    boolean emailImportEnabled = false;
    String emailImportHost = "";
    String emailImportUsername = "";
    String emailImportPassword = "";
    String emailImportPasswordAgain = "";
    String emailImportLastError = "";

    List folderList = new LinkedList();
    List categoryList = new LinkedList();

    int autoImportEmailCat = 0;

    boolean autoImportFtp = false;
    String ftpHost = "";
    String ftpUser = "";
    String ftpPassword = "";
    int autoImportFtpCat = 0;

    public int imagesizeThumbnail = 170;
    public int imagesizePreview = 590;

    public ImportSettings() {

        /*
                    <option value="Job Name"<c:if test="${status.value=='Job Name'}"> selected</c:if>><spring:message code="set.import.headers.jobname"/></option>
                    <option value="Object Name"<c:if test="${status.value=='Object Name'}"> selected</c:if>><spring:message code="set.import.headers.objectname"/></option>
                    <option value="Copyright Notice"<c:if test="${status.value=='Copyright Notice'}"> selected</c:if>><spring:message code="set.import.headers.copyrightnotice"/></option>
                    <option value="By-line"<c:if test="${status.value=='By-line'}"> selected</c:if>><spring:message code="set.import.headers.byline"/></option>
                    <option value="By-line Title"<c:if test="${status.value=='By-line Title'}"> selected</c:if>><spring:message code="set.import.headers.bylinetitle"/></option>
                    <option value="Writer/Editor"<c:if test="${status.value=='Writer/Editor'}"> selected</c:if>><spring:message code="set.import.headers.writer"/></option>
                    <option value="Caption/Abstract"<c:if test="${status.value=='Caption/Abstract'}"> selected</c:if>><spring:message code="set.import.headers.caption"/></option>

        */


    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public String getImportTitle() {
        return importTitle;
    }

    public void setImportTitle(String importTitle) {
        this.importTitle = importTitle;
    }

    public String getImportNumber() {
        return importNumber;
    }

    public void setImportNumber(String importNumber) {
        this.importNumber = importNumber;
    }

    public String getImportByline() {
        return importByline;
    }

    public void setImportByline(String importByline) {
        this.importByline = importByline;
    }

    public String getImportPhotographerAlias() {
        return importPhotographerAlias;
    }

    public void setImportPhotographerAlias(String importPhotographerAlias) {
        this.importPhotographerAlias = importPhotographerAlias;
    }

    public String getImportSite() {
        return importSite;
    }

    public void setImportSite(String importSite) {
        this.importSite = importSite;
    }

    public String getImportPeople() {
        return importPeople;
    }

    public void setImportPeople(String importPeople) {
        this.importPeople = importPeople;
    }

    public String getImportInfo() {
        return importInfo;
    }

    public void setImportInfo(String importInfo) {
        this.importInfo = importInfo;
    }

    public String getImportSubtitle() {
        return importSubtitle;
    }

    public void setImportSubtitle(String importSubtitle) {
        this.importSubtitle = importSubtitle;
    }

    public String getImportKeywords() {
        return importKeywords;
    }

    public void setImportKeywords(String importKeywords) {
        this.importKeywords = importKeywords;
    }

    public String getImportNote() {
        return importNote;
    }

    public void setImportNote(String importNote) {
        this.importNote = importNote;
    }

    public String getImportRestrictions() {
        return importRestrictions;
    }

    public void setImportRestrictions(String importRestrictions) {
        this.importRestrictions = importRestrictions;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public boolean isImportImageNumberSerially() {
        return importImageNumberSerially;
    }

    public void setImportImageNumberSerially(boolean importImageNumberSerially) {
        this.importImageNumberSerially = importImageNumberSerially;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public boolean isEmailImportEnabled() {
        return emailImportEnabled;
    }

    public void setEmailImportEnabled(boolean emailImportEnabled) {
        this.emailImportEnabled = emailImportEnabled;
    }

    public String getEmailImportHost() {
        return emailImportHost;
    }

    public void setEmailImportHost(String emailImportHost) {
        this.emailImportHost = emailImportHost;
    }

    public String getEmailImportUsername() {
        return emailImportUsername;
    }

    public void setEmailImportUsername(String emailImportUsername) {
        this.emailImportUsername = emailImportUsername;
    }

    public String getEmailImportPassword() {
        return emailImportPassword;
    }

    public void setEmailImportPassword(String emailImportPassword) {
        this.emailImportPassword = emailImportPassword;
    }

    public String getEmailImportPasswordAgain() {
        return emailImportPasswordAgain;
    }

    public void setEmailImportPasswordAgain(String emailImportPasswordAgain) {
        this.emailImportPasswordAgain = emailImportPasswordAgain;
    }

    public String getEmailImportLastError() {
        return emailImportLastError;
    }

    public void setEmailImportLastError(String emailImportLastError) {
        this.emailImportLastError = emailImportLastError;
    }

    public List getFolderList() {
        return folderList;
    }

    public void setFolderList(List folderList) {
        this.folderList = folderList;
    }

    public List getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List categoryList) {
        this.categoryList = categoryList;
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

    public int getAutoImportEmailCat() {
        return autoImportEmailCat;
    }

    public void setAutoImportEmailCat(int autoImportEmailCat) {
        this.autoImportEmailCat = autoImportEmailCat;
    }

    public int getAutoImportFtpCat() {
        return autoImportFtpCat;
    }

    public void setAutoImportFtpCat(int autoImportFtpCat) {
        this.autoImportFtpCat = autoImportFtpCat;
    }

    public boolean isAutoImportFtp() {
        return autoImportFtp;
    }

    public void setAutoImportFtp(boolean autoImportFtp) {
        this.autoImportFtp = autoImportFtp;
    }

    public int getImagesizeThumbnail() {
        return imagesizeThumbnail;
    }

    public void setImagesizeThumbnail(int imagesizeThumbnail) {
        this.imagesizeThumbnail = imagesizeThumbnail;
    }

    public int getImagesizePreview() {
        return imagesizePreview;
    }

    public void setImagesizePreview(int imagesizePreview) {
        this.imagesizePreview = imagesizePreview;
    }
}
