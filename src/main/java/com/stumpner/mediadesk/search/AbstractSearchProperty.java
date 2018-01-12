package com.stumpner.mediadesk.search;

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
 * Date: 18.01.2007
 * Time: 21:45:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSearchProperty extends SearchProperty {

    int startItem=0;
    int itemCount=12;
    int suid=0;
    int requery=0;

    public int getStartItem() {
        return startItem;
    }

    public void setStartItem(int startItem) {
        this.startItem = startItem;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getSuid() {
        return this.suid;
    }

    public void setSuid(int suid) {
        this.suid = suid;
    }

    public String getKeywords() {
        return super.getKeywords();
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getRequery() {
        return requery;
    }

    public void setRequery(int requery) {
        this.requery = requery;
    }

}
