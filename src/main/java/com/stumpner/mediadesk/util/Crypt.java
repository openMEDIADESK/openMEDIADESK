package com.stumpner.mediadesk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
 * Time: 18:34:50
 * To change this template use File | Settings | File Templates.
 */
public class Crypt {

    public static String getHash(String msg) {
        byte buf[] = msg.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(buf);
            byte[] digest = algorithm.digest();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(pad(Integer.toHexString(0xFF & digest[i]), 2));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    private static String pad(String i, int l) {
        while (i.length() < l) {
            i = '0' + i;
        }
        return i;
    }

    public static String getHashSHA256(String msg) {

        StringBuffer sb = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //System.out.println("Hex format : " + sb.toString());

        return sb.toString();
    }

}
