package com.stumpner.mediadesk.list;

import com.stumpner.mediadesk.image.IMultiLangObject;
import com.stumpner.mediadesk.core.database.sc.MultiLanguageService;

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
 * Date: 22.03.2010
 * Time: 21:04:32
 * To change this template use File | Settings | File Templates.
 */
public class CustomListEntry implements IMultiLangObject {

    int usedLanguage = 0;

    int id = 0;
    int clid = 0;
    String name = "";
    String titleLng1 = "";
    String titleLng2 = "";

    public int getUsedLanguage() {
        return usedLanguage;
    }

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClid() {
        return clid;
    }

    public void setClid(int clid) {
        this.clid = clid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {

        switch (getUsedLanguage()) {
            case MultiLanguageService.LNG1: return getTitleLng1();
            case MultiLanguageService.LNG2: return getTitleLng2();
            default: return "";
        }        
    }

    public String getTitleLng1() {
        return titleLng1;
    }

    public void setTitleLng1(String titleLng1) {
        this.titleLng1 = titleLng1;
    }

    public String getTitleLng2() {
        return titleLng2;
    }

    public void setTitleLng2(String titleLng2) {
        this.titleLng2 = titleLng2;
    }
}
