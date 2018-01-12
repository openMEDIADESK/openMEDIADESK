package com.stumpner.mediadesk.stats;

import java.util.Date;
import java.awt.*;

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
 * Time: 22:04:14
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDownloadLogger {

    int userId = -1;
    int ivid = -1;
    Date downloadDate = new Date();
    int formatx = 0;
    int formaty = 0;
    int downloadtype = 1;
    int pinid = 0;

    String payTransactionId = "";
    String name = "";

    final public static int DTYPE_DOWNLOAD = 1;
    final public static int DTYPE_PIN = 2;
    final public static int DTYPE_VIEW = 3;
    final public static int DTYPE_PODCAST = 4;
    final public static int DTYPE_STREAM_START = 5;
    final public static int DTYPE_STREAM_END = 6; //ganzen stream angesehen
    final public static int DTYPE_WEBDAV = 7;

    String ip = "";
    String dns = "";
    int bytes = 0;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIvid() {
        return ivid;
    }

    public void setIvid(int ivid) {
        this.ivid = ivid;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public void setFormat(Rectangle format) {
        formatx = (int)format.getWidth();
        formaty = (int)format.getHeight();
    }

    public int getFormatx() {
        return formatx;
    }

    public void setFormatx(int formatx) {
        this.formatx = formatx;
    }

    public int getFormaty() {
        return formaty;
    }

    public void setFormaty(int formaty) {
        this.formaty = formaty;
    }

    public int getDownloadtype() {
        return downloadtype;
    }

    public void setDownloadtype(int downloadtype) {
        this.downloadtype = downloadtype;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getPinid() {
        return pinid;
    }

    public void setPinid(int pinid) {
        this.pinid = pinid;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
