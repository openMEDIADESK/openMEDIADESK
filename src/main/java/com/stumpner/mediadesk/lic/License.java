package com.stumpner.mediadesk.lic;

import com.stumpner.mediadesk.util.IniFile;

import java.util.Calendar;

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
 * Date: 27.11.2006
 * Time: 18:56:31
 * To change this template use File | Settings | File Templates.
 */
public class License {

    private static boolean enabled = false;
    private static String domain1 = "";
    private static String domain2 = "";
    private static String ip = "";
    private static String to = "";
    private static Calendar calendar = Calendar.getInstance();

    public static void initLic(IniFile ini) {

        if (enabled) {
            System.out.println("Reading Licence...");
            String lic = ini.getProperty("lic");
            System.out.println("Licencse-KEY: " + lic);
            try {
                Crypter crypt = Crypter.getInstance();
                String encrypted = crypt.decrypt(lic, "franzismediaDESKpassword-3.1");
                String[] license = encrypted.split(";");

                to = license[0];
                ip = license[1];
                domain1 = license[2];
                domain2 = license[3];
                String[] dateToken = license[4].split("-");
                //dateToken //todo:
                calendar.set(Integer.parseInt(dateToken[0]),
                        Integer.parseInt(dateToken[1]) - 1,
                        Integer.parseInt(dateToken[2]));

                System.out.println("Licence registered to: ");
                System.out.println(" Name: " + to);
                System.out.println(" IP  : " + ip);
                System.out.println(" Dom1: " + domain1);
                System.out.println(" Dom1: " + domain2);
                System.out.println(" Expi: " + calendar.getTime());
                
            } catch (Exception e) {
                System.out.println("No valid Licence - Keine gültige Lizenz");
                System.out.println("Message: " + e.getMessage());
                throw new RuntimeException("Lizenz-Key Korrupt");
            }
        }

    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static String getDomain1() {
        return domain1;
    }

    public static String getDomain2() {
        return domain2;
    }

    public static String getIp() {
        return ip;
    }

    public static String getTo() {
        return to;
    }

    public static Calendar getDate() {
        return calendar;
    }
}
