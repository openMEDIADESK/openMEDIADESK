package com.stumpner.mediadesk.web.mvc.commandclass.settings;

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
 * Time: 13:41:07
 * To change this template use File | Settings | File Templates.
 */
public class LanguageSettings {

    boolean langUseDefault = true; // Standardsprache verwenden
    boolean langAutoFill = true;   // Leere Felder automatisch befüllen
    boolean multiLang = true;      // Mehrsprachigkeit ein/aus

    String primaryLang = "de";

    String[] availableLang = new String[] {"de","en","es"};

    String[] langList =      new String[] {"de","en","es"};

    public boolean isLangUseDefault() {
        return langUseDefault;
    }

    public void setLangUseDefault(boolean langUseDefault) {
        this.langUseDefault = langUseDefault;
    }

    public boolean isLangAutoFill() {
        return langAutoFill;
    }

    public void setLangAutoFill(boolean langAutoFill) {
        this.langAutoFill = langAutoFill;
    }

    public boolean isMultiLang() {
        return multiLang;
    }

    public void setMultiLang(boolean multiLang) {
        this.multiLang = multiLang;
    }

    public String[] getAvailableLang() {
        return availableLang;
    }

    public void setAvailableLang(String[] availableLang) {
        this.availableLang = availableLang;
    }

    public String[] getLangList() {
        return langList;
    }

    public void setLangList(String[] langList) {
        this.langList = langList;
    }

    public String getPrimaryLang() {
        return primaryLang;
    }

    public void setPrimaryLang(String primaryLang) {
        this.primaryLang = primaryLang;
    }
}
