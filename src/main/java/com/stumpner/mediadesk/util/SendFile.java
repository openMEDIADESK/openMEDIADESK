package com.stumpner.mediadesk.util;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.util.Properties;
import java.util.Date;

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
 * Date: 23.07.2008
 * Time: 21:08:37
 * To change this template use File | Settings | File Templates.
 */
public class SendFile {

    private String to = "";
    private String from = "";
    private String host = "";
    private String filename = "";
    private String attachname = "";
    private String text = "";
    private String subject = "";

    public void send() {

        // create some properties and get the default Session
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);

        Session session = Session.getInstance(props, null);

        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject,"UTF-8");

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(text,"UTF-8");

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            FileDataSource fds = new FileDataSource(filename);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(attachname);

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            msg.setContent(mp);

            // set the Date: header
            msg.setSentDate(new Date());

            // send the message
            Transport.send(msg);

        }
        catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }

    }


    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setAttachname(String attachname) {
        this.attachname = attachname;
    }

    /**
     * Schickt ein mail sofort über den Mailserver
     * @param mailServer Mailserver über den das Mail versendet werden soll
     * @param sender Absender der Email
     * @param receiver Empfänger der Email
     * @param subject Betreff der Email
     * @param body Text/mailbody der Email
     */
    public void send(String mailServer, String sender, String receiver, String subject, String body) {

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", mailServer);
        Session mysession = Session.getDefaultInstance(properties, null);
        MimeMessage message = new MimeMessage(mysession);
        try {
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(subject);
            message.setContent(body,"text/plain");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
