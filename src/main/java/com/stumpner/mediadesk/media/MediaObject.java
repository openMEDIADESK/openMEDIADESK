package com.stumpner.mediadesk.media;

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
 * Time: 23:06:13
 * To change this template use File | Settings | File Templates.
 */
public class MediaObject extends BasicMediaObject {

    protected int ivid = -1;
    protected String versionTitle = "";
    protected String versionSubTitle = "";
    protected String versionName = "";
    protected Date createDate;
    protected int creatorUserId = -1;
    protected String note = "";
    protected int kb = 0;
    protected int width = 0;
    protected int height = 0;
    protected int dpi = 0;
    protected int flag = 0;
    protected int customList1 = 0;
    protected int customList2 = 0;
    protected int customList3 = 0;

    protected long duration = 0;
    protected String artist = "";

    protected String album = "";
    protected String genre = "";
    protected int bitrate = 0;
    protected int channels = 0;
    protected int samplerate = 0;

    protected String videocodec = "";

    protected String fid = "";

    protected String customStr1 = "";
    protected String customStr2 = "";
    protected String customStr3 = "";
    protected String customStr4 = "";
    protected String customStr5 = "";

    protected String customStr6 = "";
    protected String customStr7 = "";
    protected String customStr8 = "";
    protected String customStr9 = "";
    protected String customStr10 = "";

    public int getIvid() {
        return ivid;
    }

    public void setIvid(int ivid) {
        this.ivid = ivid;
    }

    public String getVersionTitle() {
        return versionTitle;
    }

    /**
     * Gibt den Versionstitel ohne CR LF zurück
     * @return
     */
    public String getVersionTitleStripped() {
        return getVersionTitle().replaceAll("\n"," ").replaceAll("\r"," ").replaceAll("\f"," ");
    }

    public void setVersionTitle(String versionTitle) {
        this.versionTitle = versionTitle;
    }

    public String getVersionSubTitle() {
        return versionSubTitle;
    }

    public void setVersionSubTitle(String versionSubTitle) {
        this.versionSubTitle = versionSubTitle;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getKb() {
        return kb;
    }

    public void setKb(int kb) {
        this.kb = kb;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getDurationDate() {
        return new Date(getDuration());
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getSamplerate() {
        return samplerate;
    }

    public void setSamplerate(int samplerate) {
        this.samplerate = samplerate;
    }

    public String getVideocodec() {
        return videocodec;
    }

    public void setVideocodec(String videocodec) {
        this.videocodec = videocodec;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCustomStr1() {
        return customStr1;
    }

    public void setCustomStr1(String customStr1) {
        this.customStr1 = customStr1;
    }

    public String getCustomStr2() {
        return customStr2;
    }

    public void setCustomStr2(String customStr2) {
        this.customStr2 = customStr2;
    }

    public String getCustomStr3() {
        return customStr3;
    }

    public void setCustomStr3(String customStr3) {
        this.customStr3 = customStr3;
    }

    public String getCustomStr4() {
        return customStr4;
    }

    public void setCustomStr4(String customStr4) {
        this.customStr4 = customStr4;
    }

    public String getCustomStr5() {
        return customStr5;
    }

    public void setCustomStr5(String customStr5) {
        this.customStr5 = customStr5;
    }

    public String getCustomStr6() {
        return customStr6;
    }

    public void setCustomStr6(String customStr6) {
        this.customStr6 = customStr6;
    }

    public String getCustomStr7() {
        return customStr7;
    }

    public void setCustomStr7(String customStr7) {
        this.customStr7 = customStr7;
    }

    public String getCustomStr8() {
        return customStr8;
    }

    public void setCustomStr8(String customStr8) {
        this.customStr8 = customStr8;
    }

    public String getCustomStr9() {
        return customStr9;
    }

    public void setCustomStr9(String customStr9) {
        this.customStr9 = customStr9;
    }

    public String getCustomStr10() {
        return customStr10;
    }

    public void setCustomStr10(String customStr10) {
        this.customStr10 = customStr10;
    }

    /**
     * Gibt einen Dateinamen zurück der Datas Bild beschreibt (hilfreich für verlinkungen, damit
     * von den Suchmaschinen gut indiziert wird
     */
    public String getWebFilename() {
        String name = this.getVersionTitle().replaceAll(" ","_");
        if (!name.endsWith(this.getExtention())) {
            name = name + "." + getExtention();
        }
        return name;

    }
}
