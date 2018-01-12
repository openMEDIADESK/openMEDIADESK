package com.stumpner.mediadesk.web;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.text.SimpleDateFormat;

import com.stumpner.mediadesk.core.Config;

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
 * Date: 13.09.2006
 * Time: 05:32:46
 * To change this template use File | Settings | File Templates.
 */
public class LocaleResolver extends SessionLocaleResolver {

    public Locale resolveLocale(HttpServletRequest httpServletRequest) {

        Logger log = Logger.getLogger(LocaleResolver.class);

        Locale locale = Locale.GERMAN;

        if (Config.multiLang) {
            log.debug("Multi-Language Support: Using your locale");

            locale = getLocaleFromAlgorithm(httpServletRequest);
        } else {
            log.debug("No Multi-Language Support: Only German available");
        }

        //Lng im Request setzen um mit <c:out value="${lng}"/> darauf zugreifen zu können
        httpServletRequest.setAttribute("lng",locale.getLanguage());
        httpServletRequest.setAttribute("locale",locale);
        return locale;
    }

    private Locale getLocaleFromAlgorithm(HttpServletRequest request) {

        //System.out.println(request.getRequestURI());

        if (request.getRequestURI().startsWith("/en/")) {
            //System.out.println("returned english");
            request.getSession().setAttribute("MediaDeskLocale",Locale.ENGLISH);
            return Locale.ENGLISH;
        }

        if (request.getRequestURI().startsWith("/de/")) {
            //System.out.println("returned german");
            request.getSession().setAttribute("MediaDeskLocale",Locale.GERMAN);
            return Locale.GERMAN;
        }

        //Verfügbare Locales prüfen
        //returns array of all locales
        Locale locales[] = SimpleDateFormat.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {

            if (request.getRequestURI().startsWith("/"+locales[i].toString()+"/")) {
                request.getSession().setAttribute("MediaDeskLocale",locales[i]);
                return locales[i];
            }
        }

        //try to retrieve Session from Parameter lng
        if (request.getParameter("lng")!=null) {
            if (request.getParameter("lng").equalsIgnoreCase("en")) {
                request.getSession().setAttribute("MediaDeskLocale",Locale.ENGLISH);
                return Locale.ENGLISH;
            }
            if (request.getParameter("lng").equalsIgnoreCase("de")) {
                request.getSession().setAttribute("MediaDeskLocale",Locale.GERMAN);
                return Locale.GERMAN;
            }
        }

        //retrieve from Session
        if (request.getSession().getAttribute("MediaDeskLocale")!=null) {
            return (Locale)request.getSession().getAttribute("MediaDeskLocale");
        }

        //retrieve from Superclass
        return super.resolveLocale(request);
        /*
            if (super.resolveLocale(request).getLanguage().equalsIgnoreCase(
                    Locale.GERMAN.getLanguage())) {
                return Locale.GERMAN;
            } else {
                return Locale.ENGLISH;
            }
            */

    }

}
