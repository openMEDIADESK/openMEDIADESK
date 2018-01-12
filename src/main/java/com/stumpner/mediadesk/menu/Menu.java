package com.stumpner.mediadesk.menu;

import com.stumpner.mediadesk.image.IMultiLangObject;

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
 * Date: 11.09.2007
 * Time: 17:44:59
 * To change this template use File | Settings | File Templates.
 */
public class Menu implements IMultiLangObject {

    int id = 0;
    int type = 0;
        public static final int TOP = 1;
        public static final int SIDE = 2;
    int position = 0;
    int visibleForRole = 0;
    int openAs = 0;
        public static final int OPENAS_NORMAL = 0;
        public static final int OPENAS_BLANK = 0;
        public static final int OPENAS_POPUP = 0;
    String title = "";
    String titleLng1 = "";
    String titleLng2 = "";
    String metaData = "1;http://";

    int usedLanguage = 0;

    public String getLinkType() {

        String[] linkType = metaData.split(";");
        return linkType[0];

    }

    public String getLinkUrl() {

        String[] linkType = metaData.split(";");
        final char lType = linkType[0].charAt(0);
        switch(lType) {
            case '1': //http-link;
                break;
            case '2': //latest
                return "/index/last";
            case '3': //folderübersicht
                return "/index/";
            case '4': //folder
                return "/index/folder";
            case '5': //category
                return "/index/cat";
        }
        if (linkType.length>1) {
            return linkType[1];
        } else {
            return "";
        }
    }

    public String getLinkParam() {

        String[] linkType = metaData.split(";");
        final char lType = linkType[0].charAt(0);
        switch(lType) {
            case '1': //http-link;
                break;
            case '2': //latest
                break;
            case '3': //folderübersicht
                break;
            case '4': //folder
                return "id";
            case '5': //category
                return "id";
        }
        return "";
    }

    public String getLinkValue() {

        String[] linkType = metaData.split(";");
        final char lType = linkType[0].charAt(0);
        switch(lType) {
            case '1': //http-link;
                break;
            case '2': //latest
                break;
            case '3': //folderübersicht
                break;
            case '4': //folder
                return linkType[1];
            case '5': //category
                return linkType[1];
        }
        return "";
    }

    public int getOpenAs() {
        return openAs;
    }

    public void setOpenAs(int openAs) {
        this.openAs = openAs;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getVisibleForRole() {
        return visibleForRole;
    }

    public void setVisibleForRole(int visibleForRole) {
        this.visibleForRole = visibleForRole;
    }

    public String getTitle() {

        final int lang = this.usedLanguage;
        switch (lang) {
            case 1: return getTitleLng1();
            case 2: return getTitleLng2();
        }

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public int getUsedLanguage() {
        return usedLanguage;
    }

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }
}
