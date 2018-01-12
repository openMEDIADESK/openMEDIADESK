package com.stumpner.mediadesk.image.category;

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
 * Date: 15.01.2013
 * Time: 18:12:08
 * To change this template use File | Settings | File Templates.
 */

/**
 * Pr�ft ob der Dateiname g�ltig ist (Keine Sonderzeichen)
 */
public class NameValidator {

    private static String notAllowedChars = "\\/:*?\"<>|";

    private static String quoteChars = "'\"";

    public static boolean validate(String name) {

        for (int a=0;a<notAllowedChars.length();a++) {
            if (name.indexOf(notAllowedChars.charAt(a))>=0) return false;
        }

        return true;
    }

    /**
     * �berpr�ft ob Anf�hrungszeichen oder G�nsef��chen verwendet wurden
     * @param name
     * @return
     */
    public static boolean validateQuotes(String name) {

        for (int a=0;a<quoteChars.length();a++) {
            if (name.indexOf(quoteChars.charAt(a))>=0) return false;
        }

        return true;
    }

    public static String normalize(String name) {

        for (int a=0;a<notAllowedChars.length();a++) {
            name = name.replace(notAllowedChars.charAt(0),'_');
        }

        return name;        
    }

    public static String getNotAllowedChars() {
        return notAllowedChars;
    }
}
