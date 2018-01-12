package com.stumpner.mediadesk.image.category;

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
 * Date: 05.10.2005
 * Time: 22:37:21
 * To change this template use File | Settings | File Templates.
 */
public class Category implements AccessObject {

    private static int CATEGORY_ACCESS_OBJECT_TYPEID = 10;

    public static final int VIEW_UNDEFINED = 0;
    public static final int VIEW_THUMBNAILS = 1;
    public static final int VIEW_LIST = 2;

    private int categoryId = -1;
    private String catTitle = "";
    private String catName = "";
    private String description = "";
    private int parent = 0;
    int sortBy = 0;
    int orderBy = 0;
    int defaultview = 0;

    private Date changedDate = new Date();
    private int imageCount = 0; //Anzahl der Bilder in dieser Kategorie (ohne Unterkategorien)
    private int imageCountS = 0; //Anzahl der Bilder in dieser Kategorie + Unterkategorien

    private String icon = "";

    private Date createDate = new Date();
    private int primaryIvid = 0; //Icon/Ordnerbild f�r diese Kategorie
    private Date categoryDate = new Date();
    private int creatorUserId = 0;
    private String fid = ""; //Foreign ID in Foreign Systems (Sync)

    private boolean publicAcl = false; //�ber die ACL ist die Kategorie als �ffentlich gesetzt
    private boolean inheritAcl = false;
    private boolean childInheritAcl = false;
    private boolean protectedAcl = false;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = NameValidator.normalize(catName);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getImageCountS() {
        return imageCountS;
    }

    public void setImageCountS(int imageCountS) {
        this.imageCountS = imageCountS;
    }

    public int getAclObjectSerialId() {
        return getCategoryId();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getAclObjectTypeId() {
        return CATEGORY_ACCESS_OBJECT_TYPEID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public int getDefaultview() {
        return defaultview;
    }

    public void setDefaultview(int defaultview) {
        this.defaultview = defaultview;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getPrimaryIvid() {
        return primaryIvid;
    }

    public void setPrimaryIvid(int primaryIvid) {
        this.primaryIvid = primaryIvid;
    }

    public Date getCategoryDate() {
        return categoryDate;
    }

    public void setCategoryDate(Date categoryDate) {
        this.categoryDate = categoryDate;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public boolean isPublicAcl() {
        return publicAcl;
    }

    public void setPublicAcl(boolean publicAcl) {
        this.publicAcl = publicAcl;
    }

    public boolean isInheritAcl() {
        return inheritAcl;
    }

    public void setInheritAcl(boolean inheritAcl) {
        this.inheritAcl = inheritAcl;
    }

    public boolean isChildInheritAcl() {
        return childInheritAcl;
    }

    public void setChildInheritAcl(boolean childInheritAcl) {
        this.childInheritAcl = childInheritAcl;
    }

    public boolean isProtectedAcl() {
        return protectedAcl;
    }

    public void setProtectedAcl(boolean protectedAcl) {
        this.protectedAcl = protectedAcl;
    }
}
