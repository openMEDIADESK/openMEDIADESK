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
 * Date: 08.05.2007
 * Time: 18:21:38
 * To change this template use File | Settings | File Templates.
 */
public class SimpleLoaderClass {

    int sortBy = 0;
    int orderBy = 0; //1 = aufsteigend / 2 = absteigend, nichts = aufsteigend
    int id = 0;

    int itemsPerPage = -1;
    int page = -1;

    public static int ORDER_ASC = 1;
    public static int ORDER_DESC = 2;

    public static int SORT_PHOTOGRAPHDATE = 1;
    public static int SORT_VERSIONTITLE = 2;
    public static int SORT_PEOPLE = 3;
    public static int SORT_SITE = 4;
    public static int SORT_BYLINE = 5;
    public static int SORT_CREATEDATE = 6;

    int usedLanguage = 0;

    public SimpleLoaderClass() {
    }

    public SimpleLoaderClass(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getUsedLanguage() {
        return usedLanguage;
    }

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTo(int value) {

    }

    public int getStart() {

        return (page-1)*itemsPerPage;
    }

}
