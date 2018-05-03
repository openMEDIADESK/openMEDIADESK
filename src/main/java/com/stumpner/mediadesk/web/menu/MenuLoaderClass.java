package com.stumpner.mediadesk.web.menu;

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
 * Time: 18:08:51
 * To change this template use File | Settings | File Templates.
 */
public class MenuLoaderClass {

    int type = 0;
    int usedLanguage = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUsedLanguage() {
        return usedLanguage;
    }

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

}
