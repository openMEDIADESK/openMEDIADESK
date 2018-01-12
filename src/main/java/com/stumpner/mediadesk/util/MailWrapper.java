package com.stumpner.mediadesk.util;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.StringWriter;
import java.io.PrintWriter;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mail.StumpnerMailer;

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
 * Date: 20.05.2005
 * Time: 17:21:06
 * To change this template use File | Settings | File Templates.
 */
public class MailWrapper {

    private static boolean smtpUseTls = false;
    private static String smtpUsername = "";
    private static String smtpPassword = "";

    public static String lastError = "";

    public static void sendErrorReport(HttpServletRequest request, Throwable exception, String info) {

    String mailbody = "Ein Fehler trat in einer mediaDESK Instanz auf:\n\n";
    mailbody = mailbody + "\nInstanz: "+ Config.instanceName+ " ["+Config.httpBase+"]";
    mailbody = mailbody + "\nDate: "+(new Date());
    mailbody = mailbody + "\nURL:"+request.getRequestURL();
    mailbody = mailbody + "\nrequest.attribute.uri:"+request.getAttribute("uri");
    mailbody = mailbody + "\nrequest.attribute.url:"+request.getAttribute("url");
    mailbody = mailbody + "\nURI:"+request.getAttribute("javax.servlet.error.request_uri");
    mailbody = mailbody + "\nException: "+request.getAttribute("javax.servlet.error.exception");
    mailbody = mailbody + "\nType:"+request.getAttribute("javax.servlet.error.exception_type");
    mailbody = mailbody + "\nMessage:"+request.getAttribute("javax.servlet.error.message");
    mailbody = mailbody + "\nServlet:"+request.getAttribute("javax.servlet.error.servlet_name");
    mailbody = mailbody + "\nQs:"+request.getQueryString();
    mailbody = mailbody + "\nIP:"+request.getRemoteAddr();
    mailbody = mailbody + "\nUser-Agent:"+request.getHeader("User-Agent");
    mailbody = mailbody + "\nReferer:"+request.getHeader("Referer");
    mailbody = mailbody + "\nFrom:"+request.getHeader("From");
    mailbody = mailbody + "\nVersion:"+ Config.versionNumbner+" ["+Config.versionDate+"]";
    mailbody = mailbody + "\nWeitere Information: "+ info;

    /* PrÃ¼fen auf angemeldeten User */

    if (request.getSession().getAttribute("user")!=null) {
        User user = (User)request.getSession().getAttribute("user");
        mailbody = mailbody + "\nUser:"+user.getUserName();
    } else {
        mailbody = mailbody + "\nUser: - NICHT ANGEMELDET -";
    }

    /* Pin Pic Ã¼berprÃ¼fung */

    if (request.getSession().getAttribute("pinid")!=null) {
        Integer pin = (Integer)request.getSession().getAttribute("pinid");
        mailbody = mailbody + "\nPIN:"+pin;
    }

    if (exception!=null) {
        StringWriter s = new StringWriter();
        PrintWriter p = new PrintWriter(s);
        exception.printStackTrace(p);

        mailbody = mailbody + "\nKlasse: "+exception.getClass();
        mailbody = mailbody + "\nKlassname: "+exception.getClass().getName();
        mailbody = mailbody + "\n\nStacktrace: "+s.toString();
    } else {
        mailbody = mailbody + "\n\nKein Stacktrace verfuegbar";
    }

            StumpnerMailer mailer = new StumpnerMailer("", "", "mailgate.stumpner.net", "robot@media-desk.net", false);

            try {
                mailer.send("error@mediadesk.net", "HTTP500 - Report "+Config.versionNumbner, mailbody);
            } catch (MessagingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    /*
    try {
        MailWrapper.sendMail("mailgate.stumpner.net","robot@media-desk.net","error@mediadesk.net","HTTP500 - Report "+Config.versionNumbner,mailbody);
    } catch (MessagingException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } */

    }



    /**
     * Gilt nicht mehr als @deprecated {@link net.stumpner.util.mailproxy.MailProxy} sollte verwendet werden.
     * @param mailServer
     * @param sender
     * @param receiver
     * @param subject
     * @param body
     * @throws MessagingException
     */
    public static void sendMail(String mailServer, String sender, String receiver, String subject, String body) throws MessagingException {


        //if (!Config.smtpUseTls) {
            /**
             * Mails in die Queue Schicken damit dann versendet werden kann...
             * (übernimmt der TimerTask)
             */
        //    System.out.println("Sending Mail via Proxy: "+mailServer);
        //    MailProxy sendmail = MailProxy.getInstance();
        //    sendmail.saveSend(mailServer,sender,receiver,subject,body);
        //} else {
            /* Mails über den neuen Stumpner Mailer versenden */
            System.out.println("Sending Mail with StumpnerMailer: "+mailServer);
            StumpnerMailer mailer = new StumpnerMailer(smtpUsername, smtpPassword, mailServer, sender, smtpUseTls);

            mailer.send(receiver, subject, body);
        //}



        /*
        Logger logger = Logger.getLogger(MailWrapper.class);

        String host = mailServer;
        String absender = sender;
        String empfaenger = receiver;

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        Session mysession = Session.getDefaultInstance(properties, null);
        MimeMessage message = new MimeMessage(mysession);
        try {
            message.setFrom(new InternetAddress(absender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(empfaenger));
            message.setSubject(subject);
            message.setContent(body,"text/plain");
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {

            e.printStackTrace();
            logger.equals("Sending Mail failed.");
        }
        */
    }

    public static void sendAsync(final String mailServer, final String sender, final String receiver, final String subject, final String body) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {

            public void run() {

                System.out.println("Sending Mail with StumpnerMailer Executor: "+mailServer);
                StumpnerMailer mailer = new StumpnerMailer(smtpUsername, smtpPassword, mailServer, sender, smtpUseTls);

                try {
                    mailer.send(receiver, subject, body);
                    lastError = "";
                } catch (MessagingException e) {
                    System.err.println("Error Sending mail "+mailServer);
                    e.printStackTrace();
                    lastError = "MessagingException: "+e.getMessage();
                }
            }

        });
    }

    public static void sendAsync(final String mailServer, final String sender, final String receiver, final String subject, final String body, final String filename, final String attachname) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {

            public void run() {

                System.out.println("Sending Mail with StumpnerMailer Executor (ext): "+mailServer);
                StumpnerMailer mailer = new StumpnerMailer(smtpUsername, smtpPassword, mailServer, sender, smtpUseTls);

                try {
                    mailer.send(mailServer, sender, receiver, null, null, subject, body, filename, attachname);
                    lastError = "";
                } catch (MessagingException e) {
                    System.err.println("Error Sending mail "+mailServer);
                    e.printStackTrace();
                    lastError = "MessagingException: "+e.getMessage();
                }
            }

        });
    }

    public static void setConfig(String user, String password, boolean useTls) {
        smtpUsername = user;
        smtpPassword = password;
        smtpUseTls = useTls;
    }
}
