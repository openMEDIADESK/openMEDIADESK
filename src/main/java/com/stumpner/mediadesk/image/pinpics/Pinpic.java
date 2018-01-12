package com.stumpner.mediadesk.image.pinpics;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

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
 * Date: 12.05.2005
 * Time: 21:51:07
 * To change this template use File | Settings | File Templates.
 */
public class Pinpic {

    public static final int VIEW_THUMBNAILS = 1;
    public static final int VIEW_LIST = 2;

    int pinpicId = -1;
    String pinpicTitle = "";
    String pinpicName = "";
    String pin = "";
    String note = "";
    int used = -1;
    Date createDate = new Date();
    int maxUse = 999;
    boolean enabled = true;
    Date endDate = new Date(0);
    Date startDate = new Date(0);
    int imagecount = -1;
    boolean autoDelete = true; //Pin und die Bilder/Dateien werden nach Ablauf automatisch gelöscht (nur wenn die Bilder nicht einer kategorie zugewiesen sind)
    boolean directDownload = false; //Wird der Pin aufgerufen, werden automatisch die Bilder zum Download angeboten (ohne weiteren klick)
    int creatorUserId = 0; //Ersteller des Pins
    boolean uploadEnabled = false;

    int defaultview = 0;
    String emailnotification = "";

    String password = "";

    public Pinpic() {

        GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
        startDate = new Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_YEAR,30);
        endDate = new Date(calendar.getTimeInMillis());

    }

    public int getPinpicId() {
        return pinpicId;
    }

    public void setPinpicId(int pinpicId) {
        this.pinpicId = pinpicId;
    }

    public String getPinpicTitle() {
        return pinpicTitle;
    }

    public void setPinpicTitle(String pinpicTitle) {
        this.pinpicTitle = pinpicTitle;
    }

    public String getPinpicName() {
        return pinpicName;
    }

    public void setPinpicName(String pinpicName) {
        this.pinpicName = pinpicName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getImagecount() {
        return imagecount;
    }

    public void setImagecount(int imagecount) {
        this.imagecount = imagecount;
    }

    public int getMaxUse() {
        return maxUse;
    }

    public void setMaxUse(int maxUse) {
        this.maxUse = maxUse;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public boolean isDirectDownload() {
        return directDownload;
    }

    public void setDirectDownload(boolean directDownload) {
        this.directDownload = directDownload;
    }
    
    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public boolean isUploadEnabled() {
        return uploadEnabled;
    }

    public void setUploadEnabled(boolean uploadEnabled) {
        this.uploadEnabled = uploadEnabled;
    }

    public int getDefaultview() {
        return defaultview;
    }

    public void setDefaultview(int defaultview) {
        this.defaultview = defaultview;
    }

    public String getEmailnotification() {
        return emailnotification;
    }

    public void setEmailnotification(String emailnotification) {
        this.emailnotification = emailnotification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
