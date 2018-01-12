package com.stumpner.mediadesk.search;

import com.stumpner.mediadesk.image.ImageVersionMultiLang;

import java.util.ArrayList;

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
 * User: franzstumpner
 * Date: 01.09.2005
 * Time: 19:57:35
 * To change this template use File | Settings | File Templates.
 */
public class SearchResult extends ArrayList<ImageVersionMultiLang> {

    ISearchProperty searchProperty;

    int resultCount = 0; //anzahl der gefundenen Elemente
    int thisPage = 0; //welche Seite dieses Suchergebnis identifiziert
    int pageCount = 0; //anzahl der seiten
    int itemsPerPage = 0; //Maximale Items pro seite

    String searchString = ""; //Infostring der angibt wonach gesucht wurde

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public ISearchProperty getSearchProperty() {
        return searchProperty;
    }

    public void setSearchProperty(ISearchProperty searchProperty) {
        this.searchProperty = searchProperty;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getThisPage() {
        return thisPage;
    }

    public void setThisPage(int thisPage) {
        this.thisPage = thisPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public boolean nextPage() {
        if ((this.thisPage+1) >= this.getPageCount()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean prevPage() {
        if ((this.thisPage==0)) {
            return false;
        } else {
            return true;
        }
    }

}
