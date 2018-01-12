package com.stumpner.mediadesk.web.api.usermanager;

import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;

import javax.net.ssl.*;

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
 * User: stumpner
 * Date: 20.09.2011
 * Time: 10:47:57
 */
public class SyncProcessor {

    /*
    public static boolean wsUsersyncEnabled = false;
    public static String wsUsersyncUrl = "";
    public static String wsUsersyncUsername = "";
    public static String wsUsersyncPassword = "";
    public static String wsUsersyncGroupnameFilter = "";
     */
    private final String URL_USERSYNC = Config.wsUsersyncUrl;
    private final String USER = Config.wsUsersyncUsername;
    private final String PASSWORD = Config.wsUsersyncPassword;
    private final String FILTER_GROUPNAME = Config.wsUsersyncGroupnameFilter;

    private final boolean trustAllCerts = Config.wsUsersyncTrustAllCerts;

    public void start() {

        System.out.println("WS User-Sync started: "+URL_USERSYNC);

        /*
        Quellen: http://www.devdaily.com/java/edu/pj/pj010011
        https://rz-static.uni-hohenheim.de/anw/programme/prg/java/tutorials/javainsel4/javainsel_16_002.htm#Rxx365java160020400062E1F028100
        http://www.javaworld.com/javaworld/javatips/jw-javatip47.html

        Für https: http://www.javaworld.com/javaworld/javatips/jw-javatip96.html

         */

        if (trustAllCerts) {
            //speziellen Trust-Manager erstellen
            this.createAllCertsTrustManager();
        }

        URL u;

        StringBuffer content = new StringBuffer();

        try {
            u = new URL(URL_USERSYNC);

            URLConnection  c = u.openConnection();
            c.setRequestProperty( "Authorization", userNamePasswordBase64(USER,PASSWORD));
            c.connect();
            //System.out.println("Request ContentEncoding: "+c.getContentEncoding());
            String encoding = "UTF8";
            if (c.getContentEncoding()!=null) { encoding = c.getContentEncoding(); }
            BufferedReader in = new BufferedReader( new InputStreamReader(c.getInputStream(),encoding));

            String line = in.readLine();
            while (line != null) {
                content.append(line.replaceAll("&"," "));
                content.append("\n");
                line = in.readLine();
            }
            in.close();
        } catch (SSLHandshakeException e) {
            System.err.println("SSL Handshake Exception: "+e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //XLM Datei parsen:

        try {
            List<SyncedUser> userList = new ArrayList();
            XMLUserHandler contentHandler = new XMLUserHandler(userList,FILTER_GROUPNAME);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            InputSource inputSource = new InputSource(new StringReader(content.toString()));
            xmlReader.setContentHandler(contentHandler);
            xmlReader.parse(inputSource);

            userList = contentHandler.getUserListDistinct();

            for (SyncedUser sUser : userList) {

                //System.out.println("Benutzer: "+sUser.getUserName());

                UserService userService = new UserService();
                try {
                    User user = (User)userService.getByName(sUser.getUserName());
                    if (!sUser.isDeleted()) {
                        //System.out.println(" + existiert: ID="+user.getUserId()+": abgleich... enabeld:"+sUser.isEnabled());
                        sUser.setUserId(user.getUserId());
                        //Daten schreiben
                        //todo: eventuell über eine Factory Class realisieren
                        user.setEmail(sUser.getEmail());
                        user.setFirstName(sUser.getFirstName());
                        user.setLastName(sUser.getLastName());
                        user.setStreet(sUser.getStreet());
                        user.setPhone(sUser.getPhone());
                        user.setCity(sUser.getCity());
                        user.setZipCode(sUser.getZipCode());
                        user.setFax(sUser.getFax());
                        user.setCompany(sUser.getCompany());
                        
                        user.setCompanyType(sUser.getCompanyType());
                        user.setCountry(sUser.getCountry());

                        if (Config.resetSecurityGroupWhenUserIsDisabled) {
                            //Berechtigungsgruppe zurücksetzen, wenn der Benutzer deaktiviert wird
                                if (user.isEnabled() && !sUser.isEnabled()) {
                                    //Wenn der Benutzer deaktiviert wurde: Berechtigungsgruppe zurücksetzen
                                    user.setSecurityGroup(Config.defaultSecurityGroup);
                                    //System.out.println("   ...securityGroupReset="+user.getUserId());
                                }
                        }

                        user.setEnabled(sUser.isEnabled());

                        userService.save(user);
                        Authenticator auth = new Authenticator();
                        auth.setEncodedPassword(sUser.getUserName(),sUser.getEncryptedPassword());
                    } else {
                        //System.out.println(" + existiert und als gelöscht markiert: ID="+user.getUserId());
                        //Wenn der Benutzer gelöscht ist, hier auch löschen (ausgenommen Benutzer admin)
                        if (!sUser.getUserName().equalsIgnoreCase("admin")) {
                            userService.deleteById(user.getUserId());
                            //System.out.println(" + ...gelöscht ID="+user.getUserId());
                        }
                    }

                } catch (ObjectNotFoundException e) {

                    try {
                        //Nicht gefunden: anlegen (wenn nicht gelöscht)
                        if (!sUser.isDeleted()) {
                            //System.out.println(" + NEU, anlegen...");
                            addNewUser(sUser);
                            Authenticator auth = new Authenticator();
                            auth.setEncodedPassword(sUser.getUserName(),sUser.getEncryptedPassword());
                        }
                    } catch (ObjectNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOServiceException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }

        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void createAllCertsTrustManager() {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addNewUser(SyncedUser sUser) {

        UserService userService = new UserService();
        try {
            sUser.setSecurityGroup(Config.defaultSecurityGroup);
            userService.add(sUser);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private static String userNamePasswordBase64( String username, String password )
  {
    String s = username + ":" + password;

    String encs = new sun.misc.BASE64Encoder().encode(s.getBytes());

    return "Basic " + encs;
  }

}
