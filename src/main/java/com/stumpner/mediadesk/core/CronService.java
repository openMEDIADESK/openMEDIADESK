package com.stumpner.mediadesk.core;

import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.search.SearchLogger;
import com.stumpner.mediadesk.search.SearchEntity;
import com.stumpner.mediadesk.media.image.util.AutoImporter;

import javax.mail.MessagingException;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.stumpner.mediadesk.web.api.usermanager.SyncProcessor;
import com.stumpner.mediadesk.util.UploadNotificationService;
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
 * User: franz.stumpner
 * Date: 02.01.2007
 * Time: 12:24:32
 * To change this template use File | Settings | File Templates.
 */
public class CronService {

    static int minCount = 0;
    static int hourCount = 0;

    /* Ob sich die Sitemap verändert hat und ein SitemapXml Ping stattfinden soll. */
    public static boolean triggerSitemapXmlPost = true;

    static void minutely() {
        
        DatabaseService.maintenanceStage1();
        minCount++;
        if (minCount==60) {
            minCount=0;
            hourCount++;
            hourly();
        }
        if (hourCount==24) {
            hourCount=0;
            //nightly();
            //-->Daily wird in Zukunft von einem eigenen Timer aufgerufen
        }
        if (minCount%3==0) {
            //Alle 3 Minuten:
            //  SearchLogger auswerten:
            if (Config.searchPerEmail==1) { processSearchLogger(); }
            //  die Mails versenden
            //OLDSendmail.getInstance().sendQueue();
            DatabaseService.checkEmailImport();
            //Usersync
            if (Config.wsUsersyncEnabled) {
                SyncProcessor sync = new SyncProcessor();
                sync.start();
            }
        }
        if (minCount%10==0) {
            //Alle 10 Minuten: hochgeladene Bilder der letzten 15 Minuten
            UploadNotificationService.notify(10);
        }
        if (Config.autoimportFtp) {
            AutoImporter.getInstance(Config.fileSystemImportPath).check();
        }
    }

    static void hourly() {
        DatabaseService.imageSearchReset();
        if (Config.robotsAllow) { submitSitemapXml(); }
        if (Config.searchPerEmail==2) { processSearchLogger(); }
    }

    static void nightly() {

        System.out.println("Doing nightly cron Now: "+new Date());

        deleteOldPins();
        //Quota Check
        quotyCheck();
        //Beschlagwortung: Lizenz abgelaufen
        mediaLicValidCheck();
        //
        if (Config.configParam.contains("-MAIL")) {
            System.out.println("Sending Ping-Mail");
            CronService.pingMail();
        }
        if (Config.searchPerEmail==3) { processSearchLogger(); }
    }

    public static void pingMail() {

            StumpnerMailer mailer = new StumpnerMailer("", "", "mailgate.stumpner.net", "robot@media-desk.net", false);

        String mailbody = new String();
        mailbody = mailbody + "Version: "+Config.versionNumbner+ "\n"+ new Date();

        System.out.println("Sending Ping Mail");


            try {
                mailer.send("error@mediadesk.net", "mediaDESK Ping Mail "+Config.instanceName, mailbody);
            } catch (MessagingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

    }

    private static void mediaLicValidCheck() {


        StringBuffer mailBody = new StringBuffer();
        mailBody = mailBody.append("Bei diesen Dateien/Bildern laeuft die Lizenz spaetestens morgen ab:\n\n");
        Calendar c = GregorianCalendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);

        MediaService is = new MediaService();
        List<MediaObjectMultiLang> list = is.getLicValidDue(c.getTime());
        for (MediaObjectMultiLang m : list) {
            mailBody = mailBody.append(m.getVersionName()+" "+m.getLicValid()+"\n");
        }

        if (list.size()>0) {
            System.out.println(mailBody.toString());
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,Config.mailReceiverAdminEmail,"mediaDESK: Lizenz lauft ab",mailBody.toString());
        }
    }

    /**
     * Informiert diverse Suchroboter über eine aktualisierte
     * Sitemap.xml:
     *
     * http://www.sitemaps.org/protocol.php#submit_ping
     *
     * http://www.google.at/ping?sitemap=http://demo.suside.net/sitemap.xml
     */
    public static void submitSitemapXml() {

        if (triggerSitemapXmlPost) {

                String urlSitemap = Config.httpBase;
                if (!urlSitemap.endsWith("/")) { urlSitemap = urlSitemap + "/"; }
                urlSitemap = urlSitemap + "sitemap.xml";

                String pingUrl = "httP://www.google.com/ping?sitemap="+urlSitemap;

            try {
                URL url = new URL(pingUrl);
                URLConnection  conn = url.openConnection();
                //conn.setDoOutput(true);

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while (((line = rd.readLine())) != null) {
                    //Processline
                    //System.out.println(line);
                }
                rd.close();

                System.out.println("Sitemap - Ping to Google: "+Config.httpBase);
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            triggerSitemapXmlPost = false;

        }

    }

    static void processSearchLogger() {

        List searchList = SearchLogger.getInstance().clearLog();
        if (searchList.size()>0) {
            Iterator searchIt = searchList.iterator();
            StringBuffer mailBody = new StringBuffer();
            mailBody.append("-------------------------------------------------\n");
            while (searchIt.hasNext()) {
                SearchEntity searchEntity = (SearchEntity)searchIt.next();
                mailBody.append("Suche nach: "+searchEntity.getQ()+"\n");
                mailBody.append("Zeit      : "+searchEntity.getTime()+"\n");
                mailBody.append("Host      : "+searchEntity.getHostname()+"\n");
                mailBody.append("-------------------------------------------------\n");
            }
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,Config.mailReceiverAdminEmail,"mediaDESK-Suche",mailBody.toString());
        }
    }

    static void deleteOldPins() {

        //alte PINS löschen
        MediaService mediaService = new MediaService();
        PinService pinService = new PinService();
        FolderService folderService = new FolderService();
        Iterator pins = pinService.getPinList().iterator();
        while (pins.hasNext()) {
            Pin pin = (Pin)pins.next();
            System.out.println("Checking for Pin Auto-Delete for: "+pin.getPin());
            if (pin.isAutoDelete()) {
                //System.out.println("+ AutoDelete is enabled");
                if (pin.getUsed()>=pin.getMaxUse() || (pin.getEndDate().before(new Date()))) {
                    //Bilder des Pins laden:
                    Iterator mos = pinService.getPinpicImages(pin.getPinId()).iterator();
                    while (mos.hasNext()) {
                        MediaObject mo = (MediaObject)mos.next();
                        List folderList = folderService.getFolderListFromMediaObject(mo.getIvid());
                                //todo: auch prüfen ob das medienobject in einem anderen pin vorkommt
                        if (folderList.size()==0) {

                            //kommt in keinem Folder vor, kann gelöscht werden
                            pinService.deleteMediaFromPin(mo.getIvid(),pin.getPinId());
                            try {
                                mediaService.deleteMediaObject(mo);
                            } catch (IOServiceException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Medienobject kommt in einem Folder vor");
                        }
                    }

                    //Pin löschen (immer löschen, in ordner verwendete medienobjekte verbeleiben in der db, aber pin wird gelöscht
                    try {
                        pinService.deleteById(pin.getPinId());
                    } catch (IOServiceException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    private static void quotyCheck() {

        //Quota Check and Email
        MediaService mediaSevice = new MediaService();
        BigDecimal quotaTotal = mediaSevice.getQuotaTotalMb();
        BigDecimal quotaAvailable = mediaSevice.getQuotaAvailableMb();

        BigDecimal fivePercent = quotaTotal.divide(BigDecimal.valueOf(100), BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(5));

        //System.out.println("Quota: "+quotaTotal);
        //System.out.println("Used: "+mediaSevice.getQuotaUsed());
        //System.out.println("Available: "+mediaSevice.getQuotaAvailable());
        //System.out.println("FiveP: "+fivePercent);

        if (quotaAvailable.compareTo(fivePercent) == -1) {
            NumberFormat df = DecimalFormat.getInstance(Locale.GERMAN);
            df.setMaximumFractionDigits(2);
            df.setMinimumIntegerDigits(2);
            StringBuffer mailBody = new StringBuffer();
            mailBody.append("Instanz: "+Config.instanceName+" / "+Config.webTitle);
            mailBody.append("\nURL: "+Config.httpBase);
            mailBody.append("\nFreier Speicherplatz ist unterhalb 5 Prozent!\n");
            BigDecimal quotaTotalGb = quotaTotal.divide(BigDecimal.valueOf(1000));
            BigDecimal quotaAvailGb = quotaAvailable.divide(BigDecimal.valueOf(1000));
            BigDecimal quotaUsedGb = quotaTotalGb.subtract(quotaAvailGb);
            mailBody.append("\nSpeicherkapazitaet GB: "+df.format(quotaTotalGb));
            mailBody.append("\nBelegter Speicher GB:  "+df.format(quotaUsedGb));
            mailBody.append("\nFreier Speicher GB:    "+df.format(quotaAvailGb));
            mailBody.append("\n---------------------------------");
            /*
            mailBody.append("\nDetailwerte in KB"+quotaTotal);
            mailBody.append("\nQuotaTotal:         "+quotaTotal);
            mailBody.append("\nQuotaUsed:          "+mediaSevice.getQuotaUsed());
            mailBody.append("\nQuotaAvailable:     "+mediaSevice.getQuotaAvailable());
            */

            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,"noc@stumpner.com","mediaDESK Speicherplatz-Alert",mailBody.toString());
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,Config.mailReceiverAdminEmail,"mediaDESK Speicherplatz-Alert",mailBody.toString());
        }

    }

}
