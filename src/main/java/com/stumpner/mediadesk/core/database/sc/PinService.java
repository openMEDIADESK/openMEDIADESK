package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.util.Crypt;
import com.stumpner.mediadesk.pin.PinHolder;
import com.stumpner.mediadesk.usermanagement.User;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.common.util.PaginatedList;
import com.stumpner.mediadesk.web.mvc.commandclass.PinLogin;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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
 * Date: 12.05.2005
 * Time: 22:00:29
 * To change this template use File | Settings | File Templates.
 */
public class PinService extends MultiLanguageService {

    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {
        // getPinpicById
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        Pin pin = new Pin();

        try {
            pin = (Pin)smc.queryForObject("getPinById",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (pin ==null) {
            //user does not exist:
            throw new ObjectNotFoundException();
        }

        return pin;
    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return this.getPinpicByPin(name);
    }

    public void save(Object object) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Pin pin = (Pin)object;

        try {
            handlePasswort(pin);

            smc.update("savePin", pin);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     //Passwort-Handling:
     //+ Wenn Passwort String = db Passwort verschlüsselt --> lassen, passwort wurde nicht geändert
     //+ Wenn Passwort String befüllt != db  Passwort lerr --> passwort verschlüsseln und setzen
     //+ Wenn Passwort String befüllt != db Passwort verschlüsselt --> passwort  verschlüsseln und setzen
     //+ Wenn Passwort String leer != db Passwort gefüllt --> passwort leeren
     * @param pin
     */
    private void handlePasswort(Pin pin) throws IOServiceException, ObjectNotFoundException, UnsupportedEncodingException {

        Pin oldPin = (Pin)this.getById(pin.getPinId());
        //System.out.println("clear password: "+pin.getPassword());
        String formPasswordEncrypted = Crypt.getHashSHA256(pin.getPassword());//Crypt.getHash(pin.getPassword());

        if (pin.getPassword().isEmpty()) {
            //System.out.println("Setting empty password");
            pin.setPassword("");
        } else if (oldPin.getPassword().equalsIgnoreCase(pin.getPassword())) {
            //Passwort gleich... nichts machen
            //System.out.println("Passwort gleich");
        } else {
            //Passwort setzen
            //System.out.println("Setting password to "+formPasswordEncrypted);
            pin.setPassword(formPasswordEncrypted);
        }
    }

    /**
     * Überprüft das angegebene Passwort. Via pinForm kommt das unverschlüsselte Passwort daher. Muss dann verschlüsselt werden und mit dem
     * verschlüsselten Passwort in der Datenbank verglichen werden
     *
     * @param pinLogin
     * @param pin
     * @return
     */
    public boolean checkPassword(PinLogin pinLogin, Pin pin) {

        //System.out.println("clear password: "+pinLogin.getPassword());
        String passwortEncodedForm = Crypt.getHashSHA256(pinLogin.getPassword());//Crypt.getHash(pinLogin.getPassword());
        //System.out.println("checkPINpassword "+passwortEncodedForm+" -> "+pin.getPassword());

        if (passwortEncodedForm.equalsIgnoreCase(pin.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public Pin add(Object object) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Pin pin = (Pin)object;
        //Neue Logik, Pins zu generieren:
        String pinCode = generatePinCode();
        pin.setPin(pinCode);
        //String tmpPin = Long.toString(System.currentTimeMillis());
        //pin.setPin(tmpPin.substring(tmpPin.length()-5,tmpPin.length()-1));

        try {
            this.getByName(pin.getPin());
            //sorry pin exists, throw DublicateEntry Exception
            throw new DublicateEntry("PinService.add(): DublicateEntry");
        } catch (ObjectNotFoundException e) {
            //okay - pin does not exist, go on...
        }

        try {
            smc.insert("addPin", pin);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            object = smc.queryForObject("getPinByPin", pin.getPin());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return (Pin)object;
    }

    private String generatePinCode() {

        String generatedCode = "";

        final int keyGenMode = Config.pinCodeKeyGen;
        int rSection1 = 0;
        int rSection2 = 0;
        String randomChars = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
        switch (keyGenMode) {
            case Config.PINCODEKEYGEN_4NUM:

                while (rSection1<1000) {
                    rSection1 = (int)(1+Math.random()*9999);
                }
                generatedCode = String.valueOf(rSection1);
                break;
            case Config.PINCODEKEYGEN_8NUM:
                while (rSection1<1000) {
                    rSection1 = (int)(1+Math.random()*9999);
                }
                while (rSection2<1000) {
                    rSection2 = (int)(1+Math.random()*9999);
                }
                generatedCode = String.valueOf(rSection1)+"-"+String.valueOf(rSection2);
                break;
            case Config.PINCODEKEYGEN_4NUMLETTERS:
                String randomString = "";
                for (int a=0;a<4;a++) {
                    int useChar = (int)(1+Math.random()*randomChars.length());
                    randomString = randomString + randomChars.charAt(useChar);
                }
                generatedCode = randomString;
                break;
            case Config.PINCODEKEYGEN_8NUMLETTERS:
                String randomString1 = "";
                for (int a=0;a<4;a++) {
                    int useChar = (int)(1+Math.random()*randomChars.length());
                    randomString1 = randomString1 + randomChars.charAt(useChar);
                }
                String randomString2 = "";
                for (int a=0;a<4;a++) {
                    int useChar = (int)(1+Math.random()*randomChars.length());
                    randomString2 = randomString2 + randomChars.charAt(useChar);
                }
                generatedCode = randomString1+"-"+randomString2;
                break;
        }

        return generatedCode;

    }

    public void deleteById(int id) throws IOServiceException {
        //To change body of implemented methods use File | Settings | File Templates.

        //Zuerst die Medienobjekte des Pins löschen        
        List<MediaObjectMultiLang> pinImageList = this.getPinpicImages(id);
        for (MediaObjectMultiLang image : pinImageList) {
            this.deleteMediaFromPin(image.getIvid(),id);
        }

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("deletePin",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public Pin getPinpicByPin(String pin) throws ObjectNotFoundException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        Pin pinpic = new Pin();

        try {
            pinpic = (Pin)smc.queryForObject("getPinByPin",pin);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (pinpic==null) {
            //user does not exist:
            throw new ObjectNotFoundException();
        }

        return pinpic;
    }

    public List<MediaObjectMultiLang> getPinpicImages(int pinpicId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List imageList = new LinkedList();

        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(pinpicId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForList("getPinMediaObjects",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;
    }

    public PaginatedList getPinpicImagesPaginated(int pinpicId, int itemsPerPage) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(pinpicId);
        loaderClass.setUsedLanguage(getUsedLanguage());
        PaginatedList imageList = null;

        try {
            imageList = smc.queryForPaginatedList("getPinMediaObjects",loaderClass,itemsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;
    }

    public void addMediaToPin(int ivid, int pinpicId) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PinHolder pinHolder = new PinHolder();
        pinHolder.setIvid(ivid);
        pinHolder.setPinId(pinpicId);

        try {
            smc.insert("addMediaToPin", pinHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteMediaFromPin(int ivid, int pinpicId) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PinHolder pinHolder = new PinHolder();
        pinHolder.setIvid(ivid);
        pinHolder.setPinId(pinpicId);

        try {
            smc.delete("deleteMediaFromPin", pinHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public List getPinList() {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List pinpicList = new LinkedList();

        try {
            pinpicList = smc.queryForList("getPinList",null);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return pinpicList;
    }


    /**
     * Gibt die Pin-Liste eines Benutzers zurück
     * @param user
     * @return
     */
    public List getPinList(User user) {

        List<Pin> pinList = this.getPinList();
            List filteredPinList = new LinkedList();
            for (Pin pin : pinList) {
                if (pin.getCreatorUserId()==user.getUserId()) {
                    filteredPinList.add(pin);
                }
            }
            pinList = filteredPinList;
        return filteredPinList;
    }

    /**
     * Löscht alle Records im Pinpicholder für die es keinen PIN mehr gibt
     *
     * Kann auch mit dem SQL-Statement:
     * <code>
     * SELECT * FROM pinpicholder
     *  LEFT OUTER JOIN pinpic ON pinpic.pinpicid = pinpicholder.pinpicid
     * #WHERE pinpic.pinpicid IS NULL
     * </code>
     * angezeigt werden
     */
    public void deleteOrphanedHoler() {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteOrphanedPinHolder",null);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
