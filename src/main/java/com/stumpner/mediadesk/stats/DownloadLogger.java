package com.stumpner.mediadesk.stats;

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
 * Date: 18.05.2005
 * Time: 21:59:11
 * To change this template use File | Settings | File Templates.
 */
public class DownloadLogger extends SimpleDownloadLogger {

    String imageNumber = "";
    String userName = "";
    String mediaName = "";
    String pin = "";

    public boolean isDeleted() {
        if (mediaName==null) {
            return true;
        } else {
            return false;
        }
    }

    public String getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMediaName() {
        if (mediaName==null) {
            return name;
        } else {
            return mediaName;
        }
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
