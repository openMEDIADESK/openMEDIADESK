package com.stumpner.mail;

import com.stumpner.mediadesk.core.Config;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.util.Properties;

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
 * Date: 04.08.2016
 * Time: 16:42:42
 * To change this template use File | Settings | File Templates.
 */
public class StumpnerMailer {

    private String smtpUsername = "";
    private String smtpPassword = "";

    private String emailServer = "";
    private String emailFrom = "";

    private boolean useTls = true;
    private boolean useAuth = true;

    private String smtpPort = "587";

    public StumpnerMailer(String smtpUsername, String smtpPassword, String emailServer, String emailFrom) {
        
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.emailServer = emailServer;
        this.emailFrom = emailFrom;

        this.useAuth = !smtpUsername.isEmpty(); //Wenn ein Username angegeben wurde, dann authentifizierung aktivieren
        setUseTls(!smtpUsername.isEmpty());
    }

    public StumpnerMailer(String smtpUsername, String smtpPassword, String emailServer, String emailFrom, boolean useTls) {

        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.emailServer = emailServer;
        this.emailFrom = emailFrom;

        this.useAuth = !smtpUsername.isEmpty(); //Wenn ein Username angegeben wurde, dann authentifizierung aktivieren
        setUseTls(useTls);        
    }

    public StumpnerMailer(String emailServer, String emailFrom) {

        this.emailServer = emailServer;
        this.emailFrom = emailFrom;

        this.useAuth = false;
        setUseTls(false);
    }

    public void send(String emailTo, String emailSubject, String text) throws MessagingException {
        send(emailServer, emailFrom, emailTo, null, null, emailSubject, text, null,null);
    }

    /*
    public void send(String emailServer, String emailFrom, String emailReceiver, String cc, String bcc, String emailSubject, String text, String filename, String attachname) throws MessagingException {
        send(emailServer, emailFrom, emailReceiver, cc, bcc, emailSubject, text, filename, attachname);
    } */

    public void send(String emailServer, String emailFrom, String emailReceiver, String cc, String bcc, String emailSubject, String text, String filename, String attachname) throws MessagingException {

        if (true) {





            /*

                Properties props = new Properties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "mail.hostallapps.com");
                //props.put("mail.smtp.socketFactory.port", "587");
                //props.put("mail.smtp.socketFactory.class",
				//"javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.port", "587");

            System.out.println("username: "+username);
            System.out.println("password: "+password);

            System.out.println("receiver: "+emailReceiver);

                Session session = Session.getInstance(props,
                  new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                  });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailFrom));
                    message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(emailReceiver));
                    message.setSubject(emailSubject);
                    message.setText(text);

                    Transport.send(message);

                    System.out.println("Done");

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

             */

            //M�glichkeit 2
                              /*
        Properties props = System.getProperties();
        props.put("mail.smtp.host",emailServer);
        props.put("mail.smtp.auth","true");
        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailFrom));
        msg.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(emailReceiver, false));
        msg.setSubject(emailSubject);
        msg.setText(text);
        msg.setHeader("X-Mailer", "JavaMailer");
        msg.setSentDate(new Date());
        SMTPTransport t =
            (SMTPTransport)session.getTransport("smtp");
        t.connect(emailServer, username, password);
        t.sendMessage(msg, msg.getAllRecipients());

        //    t.
        //System.out.println("Response: " + t.getLastServerResponse());
        t.close();
                                            */


            //M�glichkeit 3

            Properties props = new Properties();
            if (useAuth) { props.put("mail.smtp.auth", "true"); }
            if (useTls)  { props.put("mail.smtp.starttls.enable", "true"); }
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServer);
            props.put("mail.smtp.port", smtpPort);

            Session mailSession = null;

            if (useAuth) {
                Authenticator auth = new SMTPAuthenticator();
                mailSession = Session.getInstance(props, auth);
            } else {
                mailSession = Session.getInstance(props, null);   
            }
            // uncomment for debugging infos to stdout
            if (Config.configParam.contains("-MAIL")) {
                mailSession.setDebug(true);
            }
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(emailSubject,"UTF-8");
            //message.setContent(text, "text/plain");
            message.setText(text, "UTF8");
            //System.out.println(text);
            message.setFrom(new InternetAddress(emailFrom));
            message.addRecipient(Message.RecipientType.TO,
                 new InternetAddress(emailReceiver));

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(text,"UTF-8");

            MimeBodyPart mbp2 = null;
            if (filename!=null) {
                // create the second message part
                mbp2 = new MimeBodyPart();
                // attach the file to the message
                FileDataSource fds = new FileDataSource(filename);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(attachname);

            }

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
           if (mbp2!=null) { mp.addBodyPart(mbp2); }

            // add the Multipart to the message
            message.setContent(mp);

            transport.connect();
            transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
            transport.close();

            mailSession = null;

        } else {
            Properties props = new Properties();
            props.put("mail.smtp.host", emailServer);
            Session s = Session.getInstance(props,null);
            MimeMessage message = new MimeMessage(s);
            InternetAddress from = new InternetAddress(
                    emailFrom
            );
            message.setFrom(from);
            InternetAddress to = new InternetAddress(emailReceiver);
            message.addRecipient(Message.RecipientType.TO, to);
            message.setSubject(emailSubject);
            message.setText(text);

            Transport.send(message);
        }


    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           String username = smtpUsername;
           String password = smtpPassword;
           return new PasswordAuthentication(username, password);
        }
    }

    public void setUseTls(boolean useTls) {
        this.useTls = useTls;

        if (useTls) {
            this.smtpPort = "587";
        } else {
            this.smtpPort = "25";
        }
    }

    public void setUseAuth(boolean useAuth) {
        this.useAuth = useAuth;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }
}
