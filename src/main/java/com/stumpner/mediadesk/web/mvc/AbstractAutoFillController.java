package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Date: 31.08.2007
 * Time: 16:33:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAutoFillController extends SimpleFormControllerMd {

    protected String doAutoFillField(String checkIfEmpty,String copy1, String copy2) {

        if (checkIfEmpty.length()>0) {
            return checkIfEmpty;
        } else {
            //Auto-Fill tritt in kraft:
            if (copy1.length()>0) {
                return copy1;
            }
            if (copy2.length()>0) {
                return copy2;
            }
        }
        return checkIfEmpty;
    }

    protected void onBind(HttpServletRequest httpServletRequest, Object o) throws Exception {
        /* Felder kopieren, wenn gewünscht */
        if (Config.langAutoFill) { doAutoFill(o); }
        doNameAsTitle(o);

        super.onBind(httpServletRequest, o);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Abgeleitete Klassen müssen das hier überschreiben damit der Name in den Titel übernommen wird,
     * wenn Titel leer ist
     * Seit 3.1.2012 anders:
     *  - Bei Kategorien wird der Deutsche Titel auch automatisch der Name (für Webdav usw)
     *  - Bei Dateien ist der Name = Dateiname und der Titel bleibt
     * @param o
     */
    abstract void doNameAsTitle(Object o);

    abstract void doAutoFill(Object o);



}
