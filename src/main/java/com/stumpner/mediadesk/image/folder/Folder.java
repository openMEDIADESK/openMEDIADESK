package com.stumpner.mediadesk.image.folder;

import net.stumpner.security.acl.AccessObject;

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
 * Date: 13.04.2005
 * Time: 21:26:39
 * To change this template use File | Settings | File Templates.
 */
public class Folder implements AccessObject {

    private static int FOLDER_ACCESS_OBJECT_TYPEID = 11;

    int folderId = -1;
    String folderTitle = "";
    String folderName = "";
    String folderSubTitle = "";
    Date createDate = new Date();
    Date folderDate = new Date();
    int createUserId = -1;
    int primaryIvid = -1;
    int sortBy = 0;
    int orderBy = 0;
    int imageCount = 0;

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderTitle() {
        return folderTitle;
    }

    public void setFolderTitle(String folderTitle) {
        this.folderTitle = folderTitle;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderSubTitle() {
        return folderSubTitle;
    }

    public void setFolderSubTitle(String folderSubTitle) {
        this.folderSubTitle = folderSubTitle;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public int getPrimaryIvid() {
        return primaryIvid;
    }

    public void setPrimaryIvid(int primaryIvid) {
        this.primaryIvid = primaryIvid;
    }

    public String getFolderNumber() {

        StringBuffer folderNumber = new StringBuffer();

        if (this.getFolderId()==-1) {
            folderNumber = new StringBuffer("NEW");
        } else {
                //vorhandener Ordner
            String folderId = Integer.toString(this.getFolderId());
            folderNumber = new StringBuffer("000000"); 

            folderNumber.replace(5-folderId.length(),folderNumber.length(),folderId);
        }

        return folderNumber.toString();
    }

    /**
     * Das datum welches auch angezeigt wird im frontend
     * @return
     */
    public Date getFolderDate() {
        return folderDate;
    }

    public void setFolderDate(Date folderDate) {
        this.folderDate = folderDate;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
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

    public int getAclObjectSerialId() {
        return getFolderId();
    }

    public int getAclObjectTypeId() {
        return FOLDER_ACCESS_OBJECT_TYPEID;
    }
}
