package com.stumpner.mediadesk.web;

import com.stumpner.mediadesk.core.database.sc.MultiLanguageService;
import com.stumpner.mediadesk.core.Config;

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
 * Date: 17.08.2007
 * Time: 15:45:47
 * To change this template use File | Settings | File Templates.
 *
 * Diese Klasse Resolves die zu benutzende Lng (wird aus der gesetzten Locale "erreichnet") und setzt
 * auch den Parameter/Attribut ${lng}
 * 0 = undefiniert
 * 1 = Deutsch     (meist)
 * 2 = Englisch           (meist)
 *
 */
public class LngResolver {

    public int resolveLng(HttpServletRequest httpServletRequest) {

        LocaleResolver localeResolver = new LocaleResolver();
        //Locale ermitterln und gleichzeitig auch in die PageVariable ${lng} schreiben
        Locale locale = localeResolver.resolveLocale(httpServletRequest);

        if (Config.upstreamingStartpageUrl.length()==0) {
            httpServletRequest.setAttribute("home", Config.redirectStartPage.replaceFirst("index",locale.getLanguage()));
        } else {
            httpServletRequest.setAttribute("home", Config.upstreamingStartpageUrl);
        }

        if (locale.equals(Locale.GERMAN)) {
            return MultiLanguageService.LNG1;
        }
        if (locale.equals(Locale.ENGLISH)) {
            return MultiLanguageService.LNG2;
        }

        //Englisch immer als Sprache wenn nicht Deutsch (oder Englisch)
        return MultiLanguageService.LNG2;
    }

}
