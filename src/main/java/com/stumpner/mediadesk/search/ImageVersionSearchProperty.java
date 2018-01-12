package com.stumpner.mediadesk.search;

import java.util.Date;

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
 * Time: 19:48:17
 * To change this template use File | Settings | File Templates.
 */
public class ImageVersionSearchProperty extends SearchProperty {

    int startItem = 0;
    int itemCount = 12;
    int period = 0;
    int suid = 0;
    int requery = 0;
    public static final int PERIOD_NONE = 0;
    public static final int PERIOD_DAY = 1;
    public static final int PERIOD_WEEK = 2;
    public static final int PERIOD_MONTH = 3;
    public static final int PERIOD_YEAR = 4;

    //suchvariablen:    gesetzte orientation
    boolean orientation1 = false;
    boolean orientation2 = false;
    boolean orientation3 = false;
    boolean orientation4 = false;

    Date dateFrom = null;
    Date dateTo = null;

    //String keywords = "";
    String people = "";
    String site = "";
    String photographerAlias = "";
    int orientation = 0;
    int perspective = 0;
    int motive = 0;
    int gesture = 0;

    int customList1 = 0;
    int customList2 = 0;
    int customList3 = 0;

    Date licValid = null;

    public String getPhotographerAlias() {
        return photographerAlias;
    }

    public void setPhotographerAlias(String photographerAlias) {
        this.photographerAlias = photographerAlias;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getPerspective() {
        return perspective;
    }

    public void setPerspective(int perspective) {
        this.perspective = perspective;
    }

    public int getMotive() {
        return motive;
    }

    public void setMotive(int motive) {
        this.motive = motive;
    }

    public int getGesture() {
        return gesture;
    }

    public void setGesture(int gesture) {
        this.gesture = gesture;
    }

    public String getPeople() {
        return getSearchTypeString(people);
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getPeriodInterval() {

        switch (period) {
            case PERIOD_DAY:
                return "2 DAY";
            case PERIOD_WEEK:
                return "7 DAY";
            case PERIOD_MONTH:
                return "1 MONTH";
            case PERIOD_YEAR:
                return "1 YEAR";
        }

        return "";
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

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
        return this.suid;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setSuid(int suid) {
        this.suid = suid;
    }

    public int getRequery() {
        return requery;
    }

    public void setRequery(int requery) {
        this.requery = requery;
    }

    public int getCustomList1() {
        return customList1;
    }

    public void setCustomList1(int customList1) {
        this.customList1 = customList1;
    }

    public int getCustomList2() {
        return customList2;
    }

    public void setCustomList2(int customList2) {
        this.customList2 = customList2;
    }

    public int getCustomList3() {
        return customList3;
    }

    public void setCustomList3(int customList3) {
        this.customList3 = customList3;
    }

    public Date getLicValid() {
        return licValid;
    }

    public void setLicValid(Date licValid) {
        this.licValid = licValid;
    }
}
