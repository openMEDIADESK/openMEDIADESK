package com.stumpner.mediadesk.image.folder;

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
 * Date: 22.08.2007
 * Time: 07:42:51
 * To change this template use File | Settings | File Templates.
 */
public class FolderMultiLang extends Folder implements IMultiLangObject {

    int usedLanguage = 0;

    String folderTitleLng1 = "";
    String folderTitleLng2 = "";
    String folderSubTitleLng1 = "";
    String folderSubTitleLng2 = "";

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    public int getUsedLanguage() {
        return this.usedLanguage;
    }

    public String getFolderTitle() {

        switch (getUsedLanguage()) {
            case MultiLanguageService.LNG1: return getFolderTitleLng1();
            case MultiLanguageService.LNG2: return getFolderTitleLng2();
            default: return super.getFolderTitle();
        }
    }

    public String getFolderSubTitle() {

        switch (getUsedLanguage()) {
            case MultiLanguageService.LNG1: return getFolderSubTitleLng1();
            case MultiLanguageService.LNG2: return getFolderSubTitleLng2();
            default: return super.getFolderSubTitle();
        }
    }

    public String getFolderTitleLng1() {
        return folderTitleLng1;
    }

    public void setFolderTitleLng1(String folderTitleLng1) {
        this.folderTitleLng1 = folderTitleLng1;
    }

    public String getFolderTitleLng2() {
        return folderTitleLng2;
    }

    public void setFolderTitleLng2(String folderTitleLng2) {
        this.folderTitleLng2 = folderTitleLng2;
    }

    public String getFolderSubTitleLng1() {
        return folderSubTitleLng1;
    }

    public void setFolderSubTitleLng1(String folderSubTitleLng1) {
        this.folderSubTitleLng1 = folderSubTitleLng1;
    }

    public String getFolderSubTitleLng2() {
        return folderSubTitleLng2;
    }

    public void setFolderSubTitleLng2(String folderSubTitleLng2) {
        this.folderSubTitleLng2 = folderSubTitleLng2;
    }
}
