package com.stumpner.mediadesk.usermanagement;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.util.Crypt;
import com.stumpner.mediadesk.util.MD5Crypt;
import com.stumpner.mediadesk.core.Config;

import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;

import com.stumpner.mediadesk.web.webdav.auth.DigestAuthUtils;

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
 * Time: 16:50:14
 * To change this template use File | Settings | File Templates.
 */
public class Authenticator {

    private final static int CRYPT_MODE_MD5 = 1;
    private final static int CRYPT_MODE_MD5SALTED = 2;

    private final static int CRYPT_MODE = CRYPT_MODE_MD5;

    private String SHA_SALT = "";

    /**
     * Give username in real and password in real!
     * @param userName
     * @param password
     * @return
     */
    public boolean checkPassword(String userName, String password) {

        int PASSWD_CRYPT_MODE = CRYPT_MODE;

        if (userName.equalsIgnoreCase("")) { return false; }
        Logger logger = Logger.getLogger(Authenticator.class);
        UserService userService = new UserService();
        String md5pass = Crypt.getHash(password);

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        UserAuthentication passChk = null;

            try {
                passChk = (UserAuthentication)smc.queryForObject("getPassword",userName);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        if (passChk!=null) {
            if (passChk.getPassword().startsWith("$1")) { PASSWD_CRYPT_MODE = CRYPT_MODE_MD5SALTED; }
        }

        switch (PASSWD_CRYPT_MODE) {

            case CRYPT_MODE_MD5:
                //System.out.println("Passwort Auth: Check MD5pass: ["+password+"] = "+passChk);
                logger.info("Passwort Auth: Check MD5pass: [xxx] = "+passChk);
                md5pass = Crypt.getHash(password);
                break;

            case CRYPT_MODE_MD5SALTED:
                //System.out.println("Passwort Auth: Check MD5pass (SALTED): ["+password+"] = "+passChk);
                logger.info("Passwort Auth: Check MD5pass (SALTED): [xxx] = "+passChk);
                try {
                    md5pass = MD5Crypt.crypt(password,passChk.getPassword());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;

        }

        logger.info("Passwort Auth: CRYPT_MODE= "+PASSWD_CRYPT_MODE+": ["+md5pass+"] = "+passChk);
        if (md5pass.equals(passChk.getPassword())) {

            if (passChk.getDigestpassword().equalsIgnoreCase("")) {
                //Digest-Passwort ergänzen
                logger.info("Digest Password saved");
                try {
                    setPassword(userName,password);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            return true;
        } else {
            return false;
        }

    }

    public String getDigestPassword(String userName) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        UserAuthentication passChk = null;

            try {
                passChk = (UserAuthentication)smc.queryForObject("getPassword",userName);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        return passChk.getDigestpassword();

    }

    public boolean checkPasswortBASE64(String userpassencoded) {

        //todo base64 check for auth
        return true;
    }

    public User setPassword(String userName, String password) throws ObjectNotFoundException, IOServiceException {

        try {

            //Digest Passwort berechnen
        String digestPassword = DigestAuthUtils.encodePasswordInA1Format(userName,UserService.REALM,password);


        if (CRYPT_MODE==CRYPT_MODE_MD5SALTED) {
            return setEncodedPassword(userName, MD5Crypt.crypt(password), digestPassword);
        } else {
            return setEncodedPassword(userName, Crypt.getHash(password), digestPassword);
        }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User setEncodedPassword(String userName, String encodedPassword) throws ObjectNotFoundException, IOServiceException {
        return setEncodedPassword(userName, encodedPassword, "xx");
    }

    public User setEncodedPassword(String userName, String encodedPassword, String digestPassword) throws ObjectNotFoundException, IOServiceException {

        UserService userService = new UserService();
        User user = (User)userService.getByName(userName);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        UserAuthentication userAuth = new UserAuthentication();
        userAuth.setUserName(userName);
        userAuth.setPassword(encodedPassword);
        if (digestPassword!=null) { userAuth.setDigestpassword(digestPassword); }

        try {
            smc.update("setPassword",userAuth);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return user;

    }

    public void setPasswordAndSend(String userName, String password) throws MessagingException, IOServiceException, ObjectNotFoundException {

        UserService userService = new UserService();
        User user = this.setPassword(userName,password);

        String httpBase = Config.httpBase;
        if (Config.upstreamingStartpageUrl.length()>0) {
            httpBase = Config.upstreamingStartpageUrl;
        }

        //Passwort Text:
        // {0} = Web Title
        // {1} = Username
        // {2} = Password
        // {3} = httpBase

        MessageFormat mf = new MessageFormat(Config.mailNewPasswordMailBody);
        Object[] testArgs = {Config.webTitle,userName,password,Config.httpBase};
        String mailBody = mf.format(testArgs);

        /*
        String passText = "Ihre Zugangsdaten fuer "+Config.webTitle+":\n\nUsername: "+userName+"\n"+"Password: "+password+"\n\nSie koennen sich nun unter "+httpBase+" einloggen";
        passText = passText + "\n\n--------------------------------------------------------------------\n\n";
        passText = passText + "You Accountdata for "+Config.webTitle+":\n\nUsername: "+userName+"\n"+"Password: "+password+"\n\nYou are now able to login at "+httpBase+"";
        */

        Logger log = Logger.getLogger(Authenticator.class);
        log.info("Password Renewed: "+userName+" ["+password+"]");

        if (Config.passmailUser) {
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,user.getEmail(),Config.mailNewPasswordMailSubject,mailBody);
        }

        if (Config.passmailCopyAdmin) {
            User admin = (User) userService.getByName("admin");
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,admin.getEmail(),Config.mailNewPasswordMailSubject,mailBody);
        }

    }

    public void renewPassword(String userName) throws MessagingException, IOServiceException, ObjectNotFoundException {

        Date date = new Date();
        String password = "large23";

        if (!Config.complexPasswords) {

            if (date.getSeconds()>5) {
                password = "telefon"+date.getMinutes();
            }
            if (date.getSeconds()>10) {
                password = "gemini"+date.getMinutes();
            }
            if (date.getSeconds()>15) {
                password = "telefon"+date.getMinutes();
            }
            if (date.getSeconds()>20) {
                password = "pluto"+date.getMinutes();
            }
            if (date.getSeconds()>25) {
                password = "start"+date.getMinutes();
            }
            if (date.getSeconds()>30) {
                password = "nero"+date.getMinutes();
            }
            if (date.getSeconds()>35) {
                password = "bild"+date.getMinutes();
            }
            if (date.getSeconds()>40) {
                password = "shop"+date.getMinutes();
            }
            if (date.getSeconds()>45) {
                password = "ente"+date.getMinutes();
            }
            if (date.getSeconds()>50) {
                password = "formel"+date.getMinutes();
            }
        } else {
            //todo: Komplexe passwörter
            final int passwordLength = 8;
            final String useableChars = "abcdefghijkmnpqrstuvwxyz123456789";
            Random ran = new Random();
            StringBuffer generatedPassword = new StringBuffer(passwordLength);

            for (int p=0;p<passwordLength;p++) {

                int aNumber = ran.nextInt(useableChars.length());
                boolean aBoolean = ran.nextBoolean();
                if (aBoolean) {
                    generatedPassword.append(useableChars.charAt(aNumber));
                } else {
                    generatedPassword.append(String.valueOf(useableChars.charAt(aNumber)).toUpperCase());
                }

                password = generatedPassword.toString();

            }
        }

        //System.out.println("PAAAAAAAAAAAAAAAAAAAAAAAAASSWORT: "+password);
        this.setPasswordAndSend(userName,password);
    }

}
