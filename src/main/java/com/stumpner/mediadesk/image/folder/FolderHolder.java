package com.stumpner.mediadesk.image.folder;

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
 * Date: 13.04.2005
 * Time: 21:27:01
 * To change this template use File | Settings | File Templates.
 */
public class FolderHolder {

    int fhid = -1;
    int folderId = -1;
    int ivid = -1;
    String imageTitleAlias = "";

    public int getFhid() {
        return fhid;
    }

    public void setFhid(int fhid) {
        this.fhid = fhid;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getIvid() {
        return ivid;
    }

    public void setIvid(int ivid) {
        this.ivid = ivid;
    }

    public String getImageTitleAlias() {
        return imageTitleAlias;
    }

    public void setImageTitleAlias(String imageTitleAlias) {
        this.imageTitleAlias = imageTitleAlias;
    }

}
