package com.stumpner.mediadesk.core.database.sc.loader;

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
 * Date: 09.05.2007
 * Time: 21:32:07
 * To change this template use File | Settings | File Templates.
 */
public class LastMediaLoaderClass extends SimpleLoaderClass {

    int count = 48;
    int groupPrincipal = 0;
    int userPrincipal = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGroupPrincipal() {
        return groupPrincipal;
    }

    public void setGroupPrincipal(int groupPrincipal) {
        this.groupPrincipal = groupPrincipal;
    }

    public int getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(int userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}
