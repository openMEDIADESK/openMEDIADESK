package com.stumpner.mediadesk.media;

import com.stumpner.mediadesk.core.database.sc.MultiLanguageService;
import com.stumpner.mediadesk.util.MultiLanguageString;

import java.math.BigDecimal;
import java.util.Date;

import com.stumpner.mediadesk.core.IMediaObject;

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
 * Time: 14:10:18
 * To change this template use File | Settings | File Templates.
 */
public class MediaObjectMultiLang extends MediaObject implements IMultiLangObject, IMediaObject {

    String versionTitleLng1 = "";
    String versionTitleLng2 = "";

    String versionSubTitleLng1 = "";
    String versionSubTitleLng2 = "";

    String infoLng1 = "";
    String infoLng2 = "";

    String siteLng1 = "";
    String siteLng2 = "";

    String noteLng1 = "";
    String noteLng2 = "";

    String restrictionsLng1 = "";
    String restrictionsLng2 = "";

    BigDecimal price = BigDecimal.valueOf(0);

    Date licValid = null;

    String masterdataId = ""; //Stamdaten-ID

    int usedLanguage = 0;

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    public int getUsedLanguage() {
        return usedLanguage;
    }

    public String getVersionTitle() {

        switch (usedLanguage) {
            case MultiLanguageService.LNG1: return getVersionTitleLng1();
            case MultiLanguageService.LNG2: return getVersionTitleLng2();
            default: super.getVersionTitle();
        }
        return super.getVersionTitle();
    }

    public String getVersionSubTitle() {

        switch (usedLanguage) {
            case MultiLanguageService.LNG1: return getVersionSubTitleLng1();
            case MultiLanguageService.LNG2: return getVersionSubTitleLng2();
            default: super.getVersionSubTitle();
        }
        return super.getVersionSubTitle();

    }

    public String getInfo() {

        switch (usedLanguage) {
            case MultiLanguageService.LNG1: return getInfoLng1();
            case MultiLanguageService.LNG2: return getInfoLng2();
            default: super.getInfo();
        }
        return super.getInfo();

    }

    public String getSite() {

        return MultiLanguageString.getString(usedLanguage,
                getSiteLng1(),getSiteLng2(),super.getSite());
    }

    public String getRestrictions() {
        return MultiLanguageString.getString(usedLanguage,
                getRestrictionsLng1(),getRestrictionsLng2(),super.getRestrictions());
    }

    public String getNote() {
        return MultiLanguageString.getString(usedLanguage,
                getNoteLng1(),getNoteLng2(),super.getNote());
    }

    public String getSiteLng1() {
        return siteLng1;
    }

    public void setSiteLng1(String siteLng1) {
        this.siteLng1 = siteLng1;
    }

    public String getSiteLng2() {
        return siteLng2;
    }

    public void setSiteLng2(String siteLng2) {
        this.siteLng2 = siteLng2;
    }

    public String getNoteLng1() {
        return noteLng1;
    }

    public void setNoteLng1(String noteLng1) {
        this.noteLng1 = noteLng1;
    }

    public String getNoteLng2() {
        return noteLng2;
    }

    public void setNoteLng2(String noteLng2) {
        this.noteLng2 = noteLng2;
    }

    public String getRestrictionsLng1() {
        return restrictionsLng1;
    }

    public void setRestrictionsLng1(String restrictionsLng1) {
        this.restrictionsLng1 = restrictionsLng1;
    }

    public String getRestrictionsLng2() {
        return restrictionsLng2;
    }

    public void setRestrictionsLng2(String restrictionsLng2) {
        this.restrictionsLng2 = restrictionsLng2;
    }

    public String getVersionTitleLng1() {
        return versionTitleLng1;
    }

    public void setVersionTitleLng1(String versionTitleLng1) {
        this.versionTitleLng1 = versionTitleLng1;
    }

    public String getVersionTitleLng2() {
        return versionTitleLng2;
    }

    public void setVersionTitleLng2(String versionTitleLng2) {
        this.versionTitleLng2 = versionTitleLng2;
    }

    public String getVersionSubTitleLng1() {
        return versionSubTitleLng1;
    }

    public void setVersionSubTitleLng1(String versionSubTitleLng1) {
        this.versionSubTitleLng1 = versionSubTitleLng1;
    }

    public String getVersionSubTitleLng2() {
        return versionSubTitleLng2;
    }

    public void setVersionSubTitleLng2(String versionSubTitleLng2) {
        this.versionSubTitleLng2 = versionSubTitleLng2;
    }

    public String getInfoLng1() {
        return infoLng1;
    }

    public void setInfoLng1(String infoLng1) {
        this.infoLng1 = infoLng1;
    }

    public String getInfoLng2() {
        return infoLng2;
    }

    public void setInfoLng2(String infoLng2) {
        this.infoLng2 = infoLng2;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMasterdataId() {
        return masterdataId;
    }

    public void setMasterdataId(String masterdataId) {
        this.masterdataId = masterdataId;
    }

    public Date getLicValid() {
        return licValid;
    }

    public void setLicValid(Date licValid) {
        this.licValid = licValid;
    }
}
