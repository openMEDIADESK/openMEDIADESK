package com.stumpner.mediadesk.image;

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
 * Date: 10.04.2005
 * Time: 22:56:13
 * To change this template use File | Settings | File Templates.
 */
public class BasicMediaObject extends SimpleMediaObject {

    private String photographerAlias = "";
    private int photographerUserId = 0;
    private String byline = "";
    private Date photographDate = new Date();
    private String restrictions = "";
    private Date lastDataChange;
    private String note = "";
    private String keywords = "";
    private String site = "";
    private String type = "";
    private String people = "";
    private String info = "";

    private int orientation = 0;
    /*
    0 - undefined
    1 - horizontal
    2 - panoramic
    3 - square
    4 - vertical
    */
    private int perspective = 0;
    /*
    0 - undefined
    1 - topview
    2 - bottom view
    3 - side view
    4 - front view
    5 - back view
    6 - aerial view
    */
    private int motive = 0;
    /*
    0 - undefined
    1 - action
    2 - portrait
    3 - group
    4 - overview
    5 - feature
    */
    private int gesture = 0;
    /*
    0 - undefined
    1 - jubilate
    2 - bright
    3 - sad
    4 - skeptic
    5 - screaming
    6 - neutral
    7 - friendly
    8 - unfriendly
    9 - talking
    */
    String mimeType = "";
    String extention = "";

    /**
     * Gibt zurück ob die angegebene Medien-Datei ein Thumbnail/Vorschaubild hat. Ist Abhängig vom Mime-Type.
     * @return
     */
    public boolean hasThumbnail() {

        if (mimeType.startsWith("image")) {
            return true;
        } else {
            return false;
        }

    }

    public String getPhotographerAlias() {
        return photographerAlias;
    }

    public void setPhotographerAlias(String photographerAlias) {
        this.photographerAlias = photographerAlias;
    }

    public int getPhotographerUserId() {
        return photographerUserId;
    }

    public void setPhotographerUserId(int photographerUserId) {
        this.photographerUserId = photographerUserId;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public Date getPhotographDate() {
        return photographDate;
    }

    public void setPhotographDate(Date photographDate) {
        this.photographDate = photographDate;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public Date getLastDataChange() {
        return lastDataChange;
    }

    public void setLastDataChange(Date lastDataChange) {
        this.lastDataChange = lastDataChange;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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


    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    /**
     * getMimeType kann eine mit beistrich getrennte Liste von Mimetypes enhalten,
     * diese Methode gibt den primären (ersten) Mimetype Zurück
     * @return
     */
    public String getPrimaryMimeType() {

        String mime[] = mimeType.split(",");
        if (mime.length>0) {
            return mime[0];
        }
        
        return mimeType;
    }

    /**
     * Gibt die bezeichnung des MimeTypes vor dem / zurück (Hauptteil)
     * @return
     */
    public String getMayorMime() {

        String mime[] = getPrimaryMimeType().split("/");
        if (mime.length>0) {
            return mime[0];
        }
        return "";
    }

    /**
     * Gibt die bezeichnung des MimeTypes vor dem / zurück (Nebenteil)
     * @return
     */
    public String getMinorMime() {

        String mime[] = getPrimaryMimeType().split("/");
        if (mime.length>1) {
            return mime[1];
        }
        return "";
    }
}
