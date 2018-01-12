package com.stumpner.mediadesk.core.database.sc;

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
 * Time: 14:51:28
 * To change this template use File | Settings | File Templates.
 */
public class MultiLanguageService {

    public static final int LNG_UNDEFINED = 0;
    public static final int LNG1 = 1;
    public static final int LNG2 = 2;

    int usedLanguage = 0;

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    protected int getUsedLanguage() {
        return usedLanguage;
    }

}
