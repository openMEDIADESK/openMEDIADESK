package com.stumpner.mediadesk.media.image.util;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

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
 * Date: 23.05.2007
 * Time: 20:40:17
 * To change this template use File | Settings | File Templates.
 */
public class CustomTextService {

    static public boolean hasCustomText(String code, HttpServletRequest request) {
        return getCustomText(code, request).length() != 0;
    }

    static public String getCustomText(String code, HttpServletRequest request) {

        Locale locale = getLocale(request);

        if (code.startsWith("folder")) {
            //Folder
            if (locale.getLanguage().startsWith("de")) { return Config.textFolderLng1; }
            else { return Config.textFolderLng2; }
        }
        if (code.startsWith("folder")) {
            //Cat
            if (locale.getLanguage().startsWith("de")) { return Config.textCatLng1; }
            else { return Config.textCatLng2; }
        }
        if (code.startsWith("last")) {
            //Last
            if (locale.getLanguage().startsWith("de")) { return Config.textLastLng1; }
            else { return Config.textLastLng2; }
        }

        /** Popup Texte **/
        if (code.startsWith("help")) {
            //Help
            if (locale.getLanguage().startsWith("de")) { return Config.textHelpLng1; }
            else { return Config.textHelpLng2; }
        }
        if (code.startsWith("helpsearch")) {
            //Helpsearch
            if (locale.getLanguage().startsWith("de")) { return Config.textHelpSearchLng1; }
            else { return Config.textHelpSearchLng2; }
        }
        if (code.startsWith("tac")) { //equalsIgnoreCase("tac")) {
            //AGB
            if (locale.getLanguage().startsWith("de")) { return Config.textAgbLng1; }
            else { return Config.textAgbLng2; }
        }
        if (code.startsWith("privacy")) {
            //Privacy
            if (locale.getLanguage().startsWith("de")) { return Config.textPrivacyLng1; }
            else { return Config.textPrivacyLng2; }
        }
        if (code.startsWith("contact")) {
            //contact
            if (locale.getLanguage().startsWith("de")) { return Config.textContactLng1; }
            else { return Config.textContactLng2; }
        }
        if (code.startsWith("imprint")) {
            //imprint
            if (locale.getLanguage().startsWith("de")) { return Config.textImprintLng1; }
            else { return Config.textImprintLng2; }
        }
        if (code.startsWith("faq")) {
            //faq
            if (locale.getLanguage().startsWith("de")) { return Config.textFaqLng1; }
            else { return Config.textFaqLng2; }
        }
        return "";
    }

    static public boolean getCustomBool(String code, HttpServletRequest request) {

        Locale locale = getLocale(request);

        if (code.equalsIgnoreCase("folder")) {
            //Folder

        }
        if (code.equalsIgnoreCase("folder")) {
            //Cat

        }
        if (code.equalsIgnoreCase("last")) {
            //Last

        }

        /** Popup Texte **/
        if (code.equalsIgnoreCase("help")) {
            //Help

        }
        if (code.equalsIgnoreCase("helpsearch")) {
            //Helpsearch

        }
        if (code.equalsIgnoreCase("tac")) {
            //AGB

        }
        if (code.equalsIgnoreCase("privacy")) {
            //Privacy

        }
        if (code.equalsIgnoreCase("contact")) {
            //contact
            return Config.useContactForm;
        }
        if (code.equalsIgnoreCase("imprint")) {
            //imprint

        }
        if (code.equalsIgnoreCase("faq")) {
            //faq

        }
        return false;
    }

    static public Locale getLocale(HttpServletRequest request) {

        LocaleResolver localeResolver = new LocaleResolver();
        Locale locale = localeResolver.resolveLocale(request);
        return locale;

    }





}
