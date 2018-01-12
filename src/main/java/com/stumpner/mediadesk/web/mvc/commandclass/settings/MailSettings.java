package com.stumpner.mediadesk.web.mvc.commandclass.settings;

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
 * Date: 12.12.2005
 * Time: 20:51:47
 * To change this template use File | Settings | File Templates.
 */
public class MailSettings {

    String senderMailaddresse = "";
    String mailServer = "";
    String mailNewPasswordMailSubject = "";
    String mailNewPasswordMailBody = "";
    String mailDownloadInfoMailSubject = "";
    String mailDownloadInfoMailBody = "";

    String mailUploadInfoMailSubject = "";
    String mailUploadInfoMailBody = "";
    boolean mailUploadInfoEnabled = false;

    boolean useTls = false;
    String smtpUsername = "";
    String smtpPassword = "";

    String mailAdminEmail = "";

    public String getMailAdminEmail() {
        return mailAdminEmail;
    }

    public void setMailAdminEmail(String mailAdminEmail) {
        this.mailAdminEmail = mailAdminEmail;
    }

    public boolean isUseTls() {
        return useTls;
    }

    public void setUseTls(boolean useTls) {
        this.useTls = useTls;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getSenderMailaddresse() {
        return senderMailaddresse;
    }

    public void setSenderMailaddresse(String senderMailaddresse) {
        this.senderMailaddresse = senderMailaddresse;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getMailNewPasswordMailSubject() {
        return mailNewPasswordMailSubject;
    }

    public void setMailNewPasswordMailSubject(String mailNewPasswordMailSubject) {
        this.mailNewPasswordMailSubject = mailNewPasswordMailSubject;
    }

    public String getMailNewPasswordMailBody() {
        return mailNewPasswordMailBody;
    }

    public void setMailNewPasswordMailBody(String mailNewPasswordMailBody) {
        this.mailNewPasswordMailBody = mailNewPasswordMailBody;
    }


    public String getMailDownloadInfoMailSubject() {
        return mailDownloadInfoMailSubject;
    }

    public void setMailDownloadInfoMailSubject(String mailDownloadInfoMailSubject) {
        this.mailDownloadInfoMailSubject = mailDownloadInfoMailSubject;
    }

    public String getMailDownloadInfoMailBody() {
        return mailDownloadInfoMailBody;
    }

    public void setMailDownloadInfoMailBody(String mailDownloadInfoMailBody) {
        this.mailDownloadInfoMailBody = mailDownloadInfoMailBody;
    }

    public String getMailUploadInfoMailSubject() {
        return mailUploadInfoMailSubject;
    }

    public void setMailUploadInfoMailSubject(String mailUploadInfoMailSubject) {
        this.mailUploadInfoMailSubject = mailUploadInfoMailSubject;
    }

    public String getMailUploadInfoMailBody() {
        return mailUploadInfoMailBody;
    }

    public void setMailUploadInfoMailBody(String mailUploadInfoMailBody) {
        this.mailUploadInfoMailBody = mailUploadInfoMailBody;
    }

    public boolean isMailUploadInfoEnabled() {
        return mailUploadInfoEnabled;
    }

    public void setMailUploadInfoEnabled(boolean mailUploadInfoEnabled) {
        this.mailUploadInfoEnabled = mailUploadInfoEnabled;
    }
}
