package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.WebContextListener;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.stats.DownloadLogger;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.Writer;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.SQLException;

import com.stumpner.mail.StumpnerMailer;
import org.apache.commons.io.FileUtils;
import com.stumpner.sql.installer.SqlInstallerBase;

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
 * Date: 28.04.2015
 * Time: 20:30:44
 * To change this template use File | Settings | File Templates.
 */
public class StatusServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        MediaService imageService = new MediaService();

        Writer w = response.getWriter();

        if (request.getParameter("reloadconfig")!=null) {
            Config.reloadMaxMb(getServletContext());
            w.write("Max-MB reloaded from "+ Config.iniFilename+"\n");
        }

        if (request.getParameter("reset")!=null) {

            if (Config.reset) {

                //Resetten/Zur�cksetzen/leeren der gesamten mediaDESK Instanz
                w.write("resetting...\n");
                try {
                    doReset();
                    w.write("OK, done...\n");
                    w.write("Tomcat Context herunterfahren\n");
                    w.write("neu starten (z.b. mit touch web.xml) und\n");
                    w.write(WebHelper.getServerNameUrl(request)+"/configure.jsp aufrufen\n");
                } catch (SQLException e) {
                    w.write("error!\n");
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            } else {
                w.write("reset=true in mediaDESK.conf eintragen!\n");
            }
        }

        if (request.getParameter("mailtest")!=null || request.getParameter("mailteststumpner")!=null) {

            w.write("Parameter: \n");
            w.write("+ mailserver: "+Config.mailserver+"\n");
            w.write("+ mailsender: "+Config.mailsender+"\n");
            w.write("+ tls: "+Config.smtpUseTls+"\n");

            String receiver = Config.mailReceiverAdminEmail;
            if (request.getParameter("mailteststumpner")!=null) {
                receiver = "office@stumpner.com";
            }
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,receiver,"mediaDESK Testmail","Das ist die erste Testmail die manuell weg gesendet wurde um die Funktionalit�t zu testen.");

            StumpnerMailer mailer = new StumpnerMailer(Config.smtpUsername, Config.smtpPassword, Config.mailserver, Config.mailsender, Config.smtpUseTls);

            try {
                w.write("Mail wird gesendet an: "+ receiver+"\n");
                mailer.send(receiver, "mediaDESK Testmail2", "Das ist die zweite Testmail die manuell weg gesendet wurde um die Funktionalit�t zu testen. Wenn die erste Testmail nicht angekommen ist gibt es mit dem asynchronen Mailversand Probleme");
            } catch (MessagingException e) {
                w.write("MessagingException: "+e.getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }

        if (request.getParameter("postmastertest")!=null) {

            System.out.println("Sending Mail with StumpnerMailer: ");
            //String postmaster = "franz@stumpner.com";
            w.write("Mail gesendet an Postmaster: error@mediadesk.net via mailgate.stumpner.net\n");

            StumpnerMailer mailer = new StumpnerMailer("", "", "mailgate.stumpner.net", "robot@media-desk.net", false);

            try {
                mailer.send("error@mediadesk.net", "mediaDESK Testmail (Postmaster)", "Das ist eine Testmail die manuell weg gesendet wurde um die Funktionalit�t mit dem Postmaster zu testen.");
            } catch (MessagingException e) {
                w.write("MessagingException: "+e.getNextException().getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            //MailWrapper.sendAsync("mailgate.stumpner.net","robot@media-desk.net","error@mediadesk.net","mediaDESK Testmail (Postmaster)","Das ist eine Testmail die manuell weg gesendet wurde um die Funktionalit�t mit dem Postmaster zu testen.");

        }

        /*
        if (request.getParameter("renewpublicprotectedstatus")!=null) {
            WebContextListener.renewCategoryPublicProtectedStatusRekursiv(0);
            w.write("Public Protected Status renewed for all folders\n");
        } */

        List userList = new ArrayList();
        DownloadLoggerService dlls = new DownloadLoggerService();
        List<DownloadLogger> downloads = dlls.getLog(4,1,userList);

        w.write("mediaDESK Status\n");
        w.write("Instanz: "+ Config.instanceName+"\n");
        w.write("Version: "+ Config.versionNumbner+"\n");
        w.write("V-Datum: "+ Config.versionDate+"\n");
        w.write("SeriUID: "+ Config.SERIAL_UID+"\n");
        w.write("Manifst: "+WebContextListener.class.getPackage().getImplementationVersion()+"\n");
        w.write("Templte: "+ Config.customTemplate+"\n");
        w.write("ConfPar: "+ Config.configParam+"\n");
        w.write("LtLogin: "+ Config.lastLogin+"\n");
        w.write("HTTP500: "+ Config.lastError500+"\n");
        w.write("HTTP400: "+ Config.lastError400+"\n");
        w.write("Mail   : "+ MailWrapper.lastError+"\n");
        w.write("UpSince: "+ Config.uptime+"\n");
        w.write("DoToday: "+ downloads.size()+"\n");
        w.write("= virtual hosting ===================================\n");
        w.write("request.getServerName():              "+ request.getServerName()+"\n");
        w.write("request.getHeader(X-MEDIADESK-HOST):  "+request.getHeader("X-MEDIADESK-HOST")+"\n");
        w.write("getServerNameUrl:                     "+ WebHelper.getServerNameUrl(request)+"\n");
        w.write("=====================================================\n");
        w.write("Max-MB : "+ new Integer(Config.licMaxMb)+"\n");
        w.write("Used-MB: "+ new Integer(imageService.getImageMb())+"\n");
        w.write("licId  : "+ Config.licId+"\n");
        w.write("licFunc: "+ Config.licFunc+"\n");
        w.write("=====================================================\n");
        w.write("Objekte: "+ new Integer(imageService.getImageCount())+"\n");
        w.write("FreeMem: "+ new Long(Runtime.getRuntime().freeMemory())+"\n");
        w.write("= scheduler: ========================================\n");
        if (WebContextListener.nightly !=null) {
            w.write("daily.delay: "+ WebContextListener.nightly.getDelay(TimeUnit.SECONDS)+" sek\n");
            w.write("done: "+ WebContextListener.nightly.isDone()+"\n");
            w.write("canc: "+ WebContextListener.nightly.isCancelled()+"\n");
            w.write("obj : "+ WebContextListener.nightly.toString()+"\n");
        } else {
            w.write("No schedulers\n");
        }

        w.write("\n\nCommands: \n");
        w.write("?reloadconfig: \n");
        //w.write("?renewpublicprotectedstatus: \n");

        /*
        httpServletRequest.setAttribute("instanceName",Config.instanceName);
        httpServletRequest.setAttribute("versionNumber",Config.versionNumbner);
        httpServletRequest.setAttribute("versionDate",Config.versionDate);
        httpServletRequest.setAttribute("licExpires",Config.licExpireDate);
        httpServletRequest.setAttribute("licMaxImages",new Integer(Config.licMaxImages));
        httpServletRequest.setAttribute("licMaxMb",new Integer(Config.licMaxMb));
        httpServletRequest.setAttribute("licMaxUsers",new Integer(Config.licMaxUsers));

        httpServletRequest.setAttribute("imageCount",new Integer(imageService.getMediaCount()));
        httpServletRequest.setAttribute("imageMb",new Integer(imageService.getImageMb()));
         */
        //super.doGet(request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void doReset() throws IOException, SQLException {

        //Imagestore Pfad l�schen
        System.out.println("Imagestore Pfad l�schen "+Config.imageStorePath);
        File fileImageStorePath = new File(Config.imageStorePath);
        FileUtils.deleteDirectory(fileImageStorePath);
        System.out.println("...gel�scht");

        //Tempor�rer fileSystemImportPath l�schen
        System.out.println("Imagestore Pfad l�schen "+Config.fileSystemImportPath);
        File fileFileSystemImportPath = new File(Config.fileSystemImportPath);
        FileUtils.deleteDirectory(fileFileSystemImportPath);
        System.out.println("...gel�scht");

        //Datenbank l�schen
        //reset.sql!!!
        System.out.println("SQL Datenbank l�schen");
        Connection connection = AppSqlMap.getSqlMapInstance().getDataSource().getConnection();
        SqlInstallerBase sqlInstaller =
                new SqlInstallerBase(
                        new File(getServletContext().getRealPath("/WEB-INF/reset.sql")),
                        connection);

        sqlInstaller.run();
        System.out.println("...gel�scht");

        //mediaDESK.conf l�schen
        File fileConfig = new File(getServletContext().getRealPath(WebContextListener.configFile));
        if (fileConfig.exists()) {
            System.out.println("mediaDESK.conf l�schen");
            fileConfig.delete();
            System.out.println("...gel�scht");
        }
        
    }
}
