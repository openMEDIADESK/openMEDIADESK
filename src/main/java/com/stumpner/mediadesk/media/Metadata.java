package com.stumpner.mediadesk.media;

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
 * Date: 08.05.2005
 * Time: 16:28:49
 * To change this template use File | Settings | File Templates.
 */
public class Metadata {

    private int imdid = -1;
    private int imageId = -1;
    private int versionId = -1;
    private String metaKey = "";
    private String metaValue = "";
    private boolean exifTag = false;
    private String lang = "";
    private int ivid = -1;
    private String directory = "";

    public int getImdid() {
        return imdid;
    }

    public void setImdid(int imdid) {
        this.imdid = imdid;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public boolean isExifTag() {
        return exifTag;
    }

    public void setExifTag(boolean exifTag) {
        this.exifTag = exifTag;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getIvid() {
        return ivid;
    }

    public void setIvid(int ivid) {
        this.ivid = ivid;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

}
