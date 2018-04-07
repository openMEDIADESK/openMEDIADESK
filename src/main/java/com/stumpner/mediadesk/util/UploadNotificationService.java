package com.stumpner.mediadesk.util;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.usermanagement.User;

import java.util.List;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date;
import java.text.MessageFormat;

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
 * Date: 25.11.2014
 * Time: 22:27:29
 * To change this template use File | Settings | File Templates.
 */
public class UploadNotificationService {

    public void triggerUpload(int ivid) {

        if (Config.mailUploadInfoEnabled) {
            if (false) { //wird nicht ausgef�hrt, weil alle 15 Minuten die Datenbank ausgewertet wird und nicht via trigger
                String mailbody = Config.mailUploadInfoMailBody;

                UserService userService = new UserService();
                List userList = userService.getUserList();
                Iterator users = userList.iterator();

                //todo: emailtext

                while (users.hasNext()) {
                    User user = (User)users.next();
                    if (user.isUploadNotification()) {
                        String emailRecipient = user.getEmail();
                        MailWrapper.sendAsync(Config.mailserver,Config.mailsender,emailRecipient,Config.mailUploadInfoMailSubject,mailbody);
                    }
                }
            }
        }
    }

    /**
     * Pr�ft welche Dateien in den letzten x minuten hochgeladen wurden und sendet eine Benachrichtigung
     * @param minutes
     */
    public static void notify(int minutes) {

        if (Config.mailUploadInfoEnabled) {

            Calendar end = Calendar.getInstance();
            Calendar start = Calendar.getInstance();
            start.add(Calendar.MINUTE, minutes*-1);

            MediaService is = new MediaService();
            is.setUsedLanguage(1);
            List<MediaObjectMultiLang> mediaList = is.getByCreateDate(start.getTime(), end.getTime());

            StringBuffer uploadedListString = new StringBuffer();
            for (MediaObjectMultiLang m : mediaList) {
                System.out.println("notifier: uploaded: "+m.getIvid()+" "+m.getVersionName());
                String name = m.getVersionName();
                if (m.getVersionName().equalsIgnoreCase(m.getVersionTitle())) {
                    name = m.getVersionName();
                } else {
                    name = name + " ("+m.getVersionTitle()+")";
                }
                if (m.getVersionName().isEmpty()) {
                    name = m.getVersionTitle();
                }
                uploadedListString = uploadedListString.append(name+"\n");
            }

            MessageFormat mf = new MessageFormat(Config.mailUploadInfoMailBody);
            Object[] testArgs = {mediaList.size(),Config.webTitle,Config.httpBase,"",uploadedListString.toString(),new Date()};
            String mailBody = mf.format(testArgs);

            UserService userService = new UserService();
            List userList = userService.getUserList();
            Iterator users = userList.iterator();

            //todo: emailtext

            if (mediaList.size()>0) {

                System.out.println("Mailbody: "+mailBody);


                while (users.hasNext()) {
                    User user = (User)users.next();
                    if (user.isUploadNotification()) {
                        String emailRecipient = user.getEmail();
                        MailWrapper.sendAsync(Config.mailserver,Config.mailsender,emailRecipient,Config.mailUploadInfoMailSubject,mailBody);
                    }
                }

            }
        }
    }
}
