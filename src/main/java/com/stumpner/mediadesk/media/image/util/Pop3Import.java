package com.stumpner.mediadesk.media.image.util;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.usermanagement.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import java.io.*;

import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import com.stumpner.mediadesk.upload.FileRejectException;

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
 * Date: 07.09.2009
 * Time: 21:05:39
 * To change this template use File | Settings | File Templates.
 */
public class Pop3Import {

    public String host = "pop3.stumpner.net";
    public String username = "franz.stumpner@james.stumpner.net";
    public String password = "xxx";
    public String error = "";

    public void checkMail() {

        //Get POP3 Session

        Session pop3Session = Session.getInstance(System.getProperties(), null);

        pop3Session.setDebug(false);
        String pop3host = host;

        //Get POP3 Store

        try {
        Store pop3Store = pop3Session.getStore("pop3");

        pop3Store.connect(pop3host, username, password);

        //Get Folder

        Folder folder = pop3Store.getFolder("INBOX");

        folder.open(Folder.READ_WRITE);



        //Get Messages

        Message message[] = folder.getMessages();
        //System.out.println("mailpopper connecting...");
        //System.out.println("messages: "+message.length);
        for (int i=0;i<message.length;i++) {

            Message mess = message[i];
            //System.out.println("Message: "+mess.getMessageNumber());
            Address[] adress = mess.getAllRecipients();
            for (int a=0;a<adress.length;a++) {
                //konvertieren der adresse in eine emailadresse
                InternetAddress ia = new InternetAddress(adress[a].toString());
                String email = ia.getAddress();
                String sender = "";
                for (int s=0;s<mess.getFrom().length;s++) {
                    InternetAddress ad = new InternetAddress(mess.getFrom()[s].toString());
                    sender = ad.getAddress();
                }

                //System.out.println("Email: from="+email);
                //System.out.println("SubjecT: "+mess.getSubject());
                //System.out.println("Sender: "+sender);

                try {
                    Object content = mess.getContent();

                    if (content instanceof java.lang.String) {
                        String body = (String)content;
                    } else if (content instanceof Multipart) {
                        Multipart mp = (Multipart)content;

                        for (int j=0; j < mp.getCount(); j++) {
                            Part part = mp.getBodyPart(j);
                            String disposition = part.getDisposition();

                            if (disposition==null) {
                                //Check if plain
                                MimeBodyPart mbp = (MimeBodyPart)part;
                                if (mbp.isMimeType("text/plain")) {
                                    //Mime type is plain:
                                    String body = (String)mbp.getContent();
                                } else {
                                    //Special non-attachment cases here of image/gif, text/html
                                    //System.out.println("Non-Attachm Filename: "+part.getFileName());
                                }
                            } else if ((disposition !=null) &&
                                    (disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE)) ) {

                                    // Check if plain
                                    MimeBodyPart mbp = (MimeBodyPart)part;
                                    if (mbp.isMimeType("text/plain")) {
                                            String body = (String)mbp.getContent();
                                    } else {
                                        //System.out.println("Attachm Filename: "+part.getFileName());

                                        File uFile = new File(Config.getTempPath()+File.separator+part.getFileName());
                                        try {
                                            saveFile(uFile,part);
                                            importFile(uFile.getName(),sender);
                                            mess.setFlag(Flags.Flag.DELETED,true);
                                        } catch (MimeTypeNotSupportedException e) {
                                            uFile.delete();
                                            error = "MimeTypeNotSupportedException when processing attachment from mail: "+mess.getSubject()+", "+sender;
                                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                        } catch (ObjectNotFoundException e) {
                                            uFile.delete();
                                            error = "Sender not in user database/not allowed for import. Mail: "+mess.getSubject()+", "+sender;
                                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                        } catch (SizeExceedException e) {
                                            uFile.delete();
                                            error = "Attachment-Size (MB) exceeded. Mail: "+mess.getSubject()+", "+sender;
                                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                        } catch (FileRejectException e) {
                                            uFile.delete();
                                            error = "File rejected by Plugin or Policy. Mail: "+mess.getSubject()+", "+sender;
                                            System.out.println("FileRejectException in Pop3Import.java");
                                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                        }
                                    }

                            }
                        }
                    }
                } catch (IOException e) {
                    error = "IOException when processing mail: "+mess.getSubject()+", "+sender;
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                /*
             //checken ob @123partynet.com im mail
             if (email.indexOf("@123partynet.com")>0) {
                 String emailuser = email.replaceAll("@123partynet.com","");
                 //System.out.println("Message: for "+email);
                 /*
                 try {
                     User user = CommunityService.getInstance().getUser(emailuser);
                     Mail mail = new Mail();
                     mail.setFromUserId(Config.adminUserId);
                     mail.setToUserId(user.getId());
                     mail.setSubject("EMAIL: "+message[i].getSubject());
                     try {
                         String mailtext = ((MimeMessage)message[i]).getContent().toString();
                         mail.setMailbody("Du hast eine Email von "+message[i].getFrom()[0].toString()+" bekommen:\n\n"+mailtext);
                         this.sendMail(mail,false);
                         //System.out.println("mail an user "+user.getUserName()+" gesendet");

                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 } catch (NoSuchUserException e) {
                     //e.printStackTrace();
                     //System.out.println("Message: could not be delivered: "+email);
                 } finally {
                     message[i].setFlag(Flags.Flag.DELETED, true);
                 }



             }   */
            }
        }

        folder.close(true);
        pop3Store.close();

        } catch(NoSuchProviderException e) {
            error = "NoSuchProvider "+e.getMessage();
            e.printStackTrace();
        } catch(MessagingException e) {
            error = "MessagingException "+e.getMessage();
            e.printStackTrace();
        }

    }

    protected int saveFile(File saveFile, Part part) throws IOException, MessagingException {

        BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(saveFile) );

        byte[] buff = new byte[2048];
        InputStream is = part.getInputStream();
        int ret = 0, count = 0;
        while( (ret = is.read(buff)) > 0 ){
                bos.write(buff, 0, ret);
                count += ret;
        }
        bos.close();
        is.close();
        return count;
    }

    protected void importFile(String importfile, String sender) throws MimeTypeNotSupportedException, SizeExceedException, ObjectNotFoundException, FileRejectException {

        UserService userService = new UserService();
        User user = userService.getByEmail(sender);
        if (user.getRole()<User.ROLE_IMPORTER) {
            throw new ObjectNotFoundException();
        }

        //dateinamen-umkonvertieren-auf-ohne-leerzeichen...
        String olFileName = importfile.replaceAll(" ","-");
        File uFile = new File(Config.getTempPath()+File.separator+importfile);
        uFile.renameTo(new File(Config.getTempPath()+File.separator+olFileName));

        //int importF = ImageImport.processImage(Config.fileSystemImportPath+File.separator+olFileName,userId);
        //ivid = ImageImport.getImportedIvid();

        ImportFactory importFactory = Config.importFactory;
        File importFile = new File(Config.getTempPath()+File.separator+olFileName);
        MediaImportHandler importHandler =
                importFactory.createMediaImportHandler(
                        importFile);
        int ivid = importHandler.processImport(importFile,user.getUserId()); //todo: benutzerid herausfinden

        //todo Import-Fehler behandeln!
        //importFailure = (importFailure==0) ? importF : importFailure;


        File file = new File(Config.getTempPath()+File.separator+olFileName);
        file.delete();

        FolderService folderService = new FolderService();
        try {
            folderService.addMediaToFolder(Config.autoImportFtpCat,ivid);
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //System.out.println("Importiert nach ivid = "+ivid);
    }


}
