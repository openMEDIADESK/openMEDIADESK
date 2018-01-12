package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.MailSettings;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.util.MailWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Time: 20:45:25
 * To change this template use File | Settings | File Templates.
 */
public class MailSettingsController extends SimpleFormControllerMd {

    public MailSettingsController() {

        this.setCommandClass(MailSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        MailSettings settings = new MailSettings();
        settings.setSenderMailaddresse(Config.mailsender);
        settings.setMailServer(Config.mailserver);
        settings.setMailNewPasswordMailSubject(Config.mailNewPasswordMailSubject);
        settings.setMailNewPasswordMailBody(Config.mailNewPasswordMailBody);
        settings.setMailDownloadInfoMailSubject(Config.mailDownloadInfoMailSubject);
        settings.setMailDownloadInfoMailBody(Config.mailDownloadInfoMailBody);

        settings.setMailUploadInfoMailSubject(Config.mailUploadInfoMailSubject);
        settings.setMailUploadInfoMailBody(Config.mailUploadInfoMailBody);
        settings.setMailUploadInfoEnabled(Config.mailUploadInfoEnabled);

        settings.setUseTls(Config.smtpUseTls);
        settings.setSmtpUsername(Config.smtpUsername);
        settings.setSmtpPassword(Config.smtpPassword);
        settings.setMailAdminEmail(Config.mailReceiverAdminEmail);
        return settings;
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        this.setContentTemplateFile("settings_mail.jsp",httpServletRequest);

        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.

    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        MailSettings settings = (MailSettings)o;

        if (!Config.mailserver.equalsIgnoreCase(settings.getMailServer()) ||
            Config.smtpUseTls!=settings.isUseTls() ||
            !Config.smtpUsername.equalsIgnoreCase(settings.getSmtpUsername()) ||
            !Config.smtpPassword.equalsIgnoreCase(settings.getSmtpPassword()) ||
            !Config.mailReceiverAdminEmail.equalsIgnoreCase(settings.getMailAdminEmail()) ||
            !Config.mailsender.equalsIgnoreCase(settings.getSenderMailaddresse())) {

            //Relevante Einstellungen wurden geändert -> Mail testen
            MailWrapper.setConfig(settings.getSmtpUsername(), settings.getSmtpPassword(), settings.isUseTls());

            try {
                MailWrapper.sendMail(settings.getMailServer(), settings.getSenderMailaddresse(), settings.getMailAdminEmail(), "mediaDESK Testmail", "Das ist eine Email die automatisch vom mediaDESK System versendet wird um die Maileinstellungen zu prüfen.");

            } catch (MessagingException ex) {
                System.out.println("error: "+ex.getMessage());
                MailWrapper.setConfig(Config.smtpUsername, Config.smtpPassword, Config.smtpUseTls);
                e.reject("set.mail.servererror", new String[] { ex.getMessage() }, null);

                httpServletRequest.setAttribute("activeTab", "mailserver");
            }
        }
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        MailSettings settings = (MailSettings)o;
        Config.mailsender = settings.getSenderMailaddresse();
        Config.mailserver = settings.getMailServer();
        Config.mailNewPasswordMailSubject = settings.getMailNewPasswordMailSubject();
        Config.mailNewPasswordMailBody = settings.getMailNewPasswordMailBody();
        Config.mailDownloadInfoMailSubject = settings.getMailDownloadInfoMailSubject();
        Config.mailDownloadInfoMailBody = settings.getMailDownloadInfoMailBody();
        Config.mailUploadInfoMailSubject = settings.getMailUploadInfoMailSubject();
        Config.mailUploadInfoMailBody = settings.getMailUploadInfoMailBody();
        Config.mailUploadInfoEnabled = settings.isMailUploadInfoEnabled();
        Config.smtpUseTls = settings.isUseTls();
        Config.smtpUsername = settings.getSmtpUsername();
        Config.smtpPassword = settings.getSmtpPassword();
        Config.mailReceiverAdminEmail = settings.getMailAdminEmail();

        System.out.println("Mail Einstellungen ge�ndert:");
        System.out.println(" * mailserver   = "+Config.mailserver);
        System.out.println(" * smtpUsername = "+Config.smtpUsername);
        System.out.println(" * smtpPassword = "+Config.smtpPassword);
        System.out.println(" * mailsender   = "+Config.mailsender);
        System.out.println(" * mailReceiverAdminEmail = "+Config.mailReceiverAdminEmail);

        UserService userService = new UserService();
        User admin = (User)userService.getByName("admin");
        if (admin.getEmail().equalsIgnoreCase("office@mediadesk.net")) {
            //Admin Email auch �ndern!!!
            System.out.println(" * admin User Email ge�ndert auf "+Config.mailReceiverAdminEmail);
            admin.setEmail(Config.mailReceiverAdminEmail);
            userService.save(admin);
        }

        Config.saveConfiguration();

        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("settings")
        );
        return null;
    }

}
