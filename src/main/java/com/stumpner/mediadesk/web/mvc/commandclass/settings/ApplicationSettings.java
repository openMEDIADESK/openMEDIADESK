package com.stumpner.mediadesk.web.mvc.commandclass.settings;

import com.stumpner.mediadesk.core.Config;

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
 * Date: 09.12.2005
 * Time: 12:52:11
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationSettings {


    String startPage = "/index/";
    boolean creditSystemEnabled = false;

    boolean activateNewUsers = false; //Neue User nach der registrierung automatisch freischalten.
    boolean informOfNewUsers = false; //Administrator per Email über neue User informieren.
    boolean passmailCopyAdmin = false; //Passwort-Mail Kopie auch an den Administrator senden.
    boolean passmailUser = true; //Ob des User überhaupt ein Passwort per Email zugesendet wird

    boolean informDownloadAdmin = false; //Administrator eine Email schicken wenn Bilder heruntergeladen wurden
    int defaultCredits = 0; //Anzahl der Credits die für neue Benutzer gesetzt werden
    int defaultSecurityGroup = 0; //Security Group für neue Benutzer
    int defaultRole = 0;
    boolean useCaptchaRegister = true;
    boolean useCaptchaSend = true;
    boolean useCaptchaPin = false;
    int pinCodeKeyGen = 1;

    boolean folderLatestOnRoot = true; // true = Die letzten (neusten) Bilder werden in der Hauptkategorie angezeigt (root)

    String downloadImageFilename = "";

    // Anzeige-Einstellungen BasicMediaObject Ivid Popup

    boolean popupIvidShowVersionTitle = true;
    boolean popupIvidShowVersionSubTitle = true;
    boolean popupIvidShowInfo = true;
    boolean popupIvidShowImagenumber = true;
    boolean popupIvidShowPhotographdate = true;
    boolean popupIvidShowSite = true;
    boolean popupIvidShowPhotographerAlias = true;
    boolean popupIvidShowByline = true;
    boolean popupIvidShowKbPixelDpi = true;
    boolean popupIvidShowPeople = true;
    boolean popupIvidShowNote = true;
    boolean popupIvidShowRestrictions = true;
    boolean popupIvidShowKeywords = true;
    boolean popupIvidPageNavTop = false;
    boolean popupIvidPageNavBottom = true;

    boolean inlinePreview = true;

    String upstreamingStartpageUrl = "";
    boolean quickDownload = false;
    boolean showSendImage = true;
    boolean searchAnd = false; // Ob die Suche UND oder ODER verknüpft ist

    int sortByFolder = 0;
    int orderByFolder = 0;

    //Edit-Copy Fields

    boolean editCopyTitle = true;
    boolean editCopyTitleLng1 = true;
    boolean editCopyTitleLng2 = true;

    boolean editCopySubTitle = true;
    boolean editCopySubTitleLng1 = true;
    boolean editCopySubTitleLng2 = true;

    boolean editCopyInfo = false;
    boolean editCopyInfoLng1 = false;
    boolean editCopyInfoLng2 = false;

    boolean editCopySite = true;
    boolean editCopyPhotographDate = true;
    boolean editCopyPhotographer = true;
    boolean editCopyByline = true;
    boolean editCopyKeywords = false;

    boolean editCopyPeople = false;
    boolean editCopyOrientation = false;
    boolean editCopyPerspective = false;
    boolean editCopyMotive = false;
    boolean editCopyGesture = false;
    boolean editCopyNote = false;
    boolean editCopyRestrictions = false;
    boolean editCopyFlag = false;

    boolean editCopySiteLng1 = true;
    boolean editCopySiteLng2 = true;
    boolean editCopyNoteLng1 = false;
    boolean editCopyNoteLng2 = false;
    boolean editCopyRestrictionsLng1 = false;
    boolean editCopyRestrictionsLng2 = false;

    boolean editCopyLicValid = false;

    boolean editCopyPrice = false;
    boolean editCopyMasterdata = false;

    int searchPerEmail = 2; //0 - nein, 1 - ja, sofort, 2 - stündlich, 3 - täglich
    boolean showFormOnEmptySearch = true;

    public boolean useShoppingCart = true;
    public boolean useLightbox = true;

    boolean useDownloadResolutions = false;
    boolean downloadResOrig = true;

    boolean showDownloadToVisitors = true;

    boolean podcastEnabled = false;
    boolean complexPasswords = false;

    boolean robotsAllow = true;
    int folderSort = 1;

    //Home-Folder (Folder per User) Function
    int homeFolderId = -1;
    boolean homeFolderAsRoot = false;

    boolean homeFolderAutocreate = true;

    int wording = Config.WORDING_IMAGE;

    int itemCountPerPage = 12;

    String forbiddenDomains = "";

    int folderDefaultViewOnRoot = 0;

    boolean usersCanSendAttachments = false;

    public boolean wsUsersyncEnabled = false;
    public String wsUsersyncUrl = "";
    public String wsUsersyncUsername = "";
    public String wsUsersyncPassword = "";
    public String wsUsersyncGroupnameFilter = "";
    public boolean wsUsersyncTrustAllCerts = false;

    public boolean streamEnabled = true;
    public boolean streamToVisitors = true;
    public int streamConvertToKbitSek = 0;

    public boolean webdavEnabled = true;

    public boolean blankWhenFieldEmpty = true;

    public boolean userEmailAsUsername = false;
    public boolean showUserCompanyFields = false;
    public boolean showUserAddressFields = false;
    public boolean showUserTelFaxFields = false;

    public boolean allowRegister = true;

    public boolean advancedSearchEnabled = true;

    public boolean resetSecurityGroupWhenUserIsDisabled = false;

    public boolean onlyLoggedinUsers = false;

    public String configParam = "";

    public String paymillKeyPublic = "";
    public String paymillKeyPrivate = "";
    public int vatPercent = 0;
    public String currency = "";

    public boolean isWebdavEnabled() {
        return webdavEnabled;
    }

    public void setWebdavEnabled(boolean webdavEnabled) {
        this.webdavEnabled = webdavEnabled;
    }

    public int getWording() {
        return wording;
    }

    public void setWording(int wording) {
        this.wording = wording;
    }

    public boolean isEditCopySiteLng1() {
        return editCopySiteLng1;
    }

    public void setEditCopySiteLng1(boolean editCopySiteLng1) {
        this.editCopySiteLng1 = editCopySiteLng1;
    }

    public boolean isEditCopySiteLng2() {
        return editCopySiteLng2;
    }

    public void setEditCopySiteLng2(boolean editCopySiteLng2) {
        this.editCopySiteLng2 = editCopySiteLng2;
    }

    public boolean isEditCopyNoteLng1() {
        return editCopyNoteLng1;
    }

    public void setEditCopyNoteLng1(boolean editCopyNoteLng1) {
        this.editCopyNoteLng1 = editCopyNoteLng1;
    }

    public boolean isEditCopyNoteLng2() {
        return editCopyNoteLng2;
    }

    public void setEditCopyNoteLng2(boolean editCopyNoteLng2) {
        this.editCopyNoteLng2 = editCopyNoteLng2;
    }

    public boolean isEditCopyRestrictionsLng1() {
        return editCopyRestrictionsLng1;
    }

    public void setEditCopyRestrictionsLng1(boolean editCopyRestrictionsLng1) {
        this.editCopyRestrictionsLng1 = editCopyRestrictionsLng1;
    }

    public boolean isEditCopyRestrictionsLng2() {
        return editCopyRestrictionsLng2;
    }

    public void setEditCopyRestrictionsLng2(boolean editCopyRestrictionsLng2) {
        this.editCopyRestrictionsLng2 = editCopyRestrictionsLng2;
    }

    public boolean isEditCopyRestrictions() {
        return editCopyRestrictions;
    }

    public void setEditCopyRestrictions(boolean editCopyRestrictions) {
        this.editCopyRestrictions = editCopyRestrictions;
    }

    public boolean isEditCopyPeople() {
        return editCopyPeople;
    }

    public void setEditCopyPeople(boolean editCopyPeople) {
        this.editCopyPeople = editCopyPeople;
    }

    public boolean isEditCopyOrientation() {
        return editCopyOrientation;
    }

    public void setEditCopyOrientation(boolean editCopyOrientation) {
        this.editCopyOrientation = editCopyOrientation;
    }

    public boolean isEditCopyPerspective() {
        return editCopyPerspective;
    }

    public void setEditCopyPerspective(boolean editCopyPerspective) {
        this.editCopyPerspective = editCopyPerspective;
    }

    public boolean isEditCopyMotive() {
        return editCopyMotive;
    }

    public void setEditCopyMotive(boolean editCopyMotive) {
        this.editCopyMotive = editCopyMotive;
    }

    public boolean isEditCopyGesture() {
        return editCopyGesture;
    }

    public void setEditCopyGesture(boolean editCopyGesture) {
        this.editCopyGesture = editCopyGesture;
    }

    public boolean isEditCopyNote() {
        return editCopyNote;
    }

    public void setEditCopyNote(boolean editCopyNote) {
        this.editCopyNote = editCopyNote;
    }

    public boolean isEditCopyFlag() {
        return editCopyFlag;
    }

    public void setEditCopyFlag(boolean editCopyFlag) {
        this.editCopyFlag = editCopyFlag;
    }


    public boolean isEditCopyInfo() {
        return editCopyInfo;
    }

    public void setEditCopyInfo(boolean editCopyInfo) {
        this.editCopyInfo = editCopyInfo;
    }

    public boolean isEditCopyInfoLng1() {
        return editCopyInfoLng1;
    }

    public void setEditCopyInfoLng1(boolean editCopyInfoLng1) {
        this.editCopyInfoLng1 = editCopyInfoLng1;
    }

    public boolean isEditCopyInfoLng2() {
        return editCopyInfoLng2;
    }

    public void setEditCopyInfoLng2(boolean editCopyInfoLng2) {
        this.editCopyInfoLng2 = editCopyInfoLng2;
    }

    public boolean isEditCopySite() {
        return editCopySite;
    }

    public void setEditCopySite(boolean editCopySite) {
        this.editCopySite = editCopySite;
    }

    public boolean isEditCopyPhotographDate() {
        return editCopyPhotographDate;
    }

    public void setEditCopyPhotographDate(boolean editCopyPhotographDate) {
        this.editCopyPhotographDate = editCopyPhotographDate;
    }

    public boolean isEditCopyPhotographer() {
        return editCopyPhotographer;
    }

    public void setEditCopyPhotographer(boolean editCopyPhotographer) {
        this.editCopyPhotographer = editCopyPhotographer;
    }

    public boolean isEditCopyByline() {
        return editCopyByline;
    }

    public void setEditCopyByline(boolean editCopyByline) {
        this.editCopyByline = editCopyByline;
    }

    public boolean isEditCopyKeywords() {
        return editCopyKeywords;
    }

    public void setEditCopyKeywords(boolean editCopyKeywords) {
        this.editCopyKeywords = editCopyKeywords;
    }

    public boolean isEditCopyTitleLng1() {
        return editCopyTitleLng1;
    }

    public void setEditCopyTitleLng1(boolean editCopyTitleLng1) {
        this.editCopyTitleLng1 = editCopyTitleLng1;
    }

    public boolean isEditCopyTitleLng2() {
        return editCopyTitleLng2;
    }

    public void setEditCopyTitleLng2(boolean editCopyTitleLng2) {
        this.editCopyTitleLng2 = editCopyTitleLng2;
    }

    public boolean isEditCopySubTitle() {
        return editCopySubTitle;
    }

    public void setEditCopySubTitle(boolean editCopySubTitle) {
        this.editCopySubTitle = editCopySubTitle;
    }

    public boolean isEditCopySubTitleLng1() {
        return editCopySubTitleLng1;
    }

    public void setEditCopySubTitleLng1(boolean editCopySubTitleLng1) {
        this.editCopySubTitleLng1 = editCopySubTitleLng1;
    }

    public boolean isEditCopySubTitleLng2() {
        return editCopySubTitleLng2;
    }

    public void setEditCopySubTitleLng2(boolean editCopySubTitleLng2) {
        this.editCopySubTitleLng2 = editCopySubTitleLng2;
    }

    public boolean isSearchAnd() {
        return searchAnd;
    }

    public void setSearchAnd(boolean searchAnd) {
        this.searchAnd = searchAnd;
    }

    public String getUpstreamingStartpageUrl() {
        return upstreamingStartpageUrl;
    }

    public void setUpstreamingStartpageUrl(String upstreamingStartpageUrl) {
        this.upstreamingStartpageUrl = upstreamingStartpageUrl;
    }

    public boolean isFolderLatestOnRoot() {
        return folderLatestOnRoot;
    }

    public void setFolderLatestOnRoot(boolean folderLatestOnRoot) {
        this.folderLatestOnRoot = folderLatestOnRoot;
    }

    public int getStartPageInt() {

        if (this.startPage.equalsIgnoreCase("/index/"))
            return 0;
        if (this.startPage.equalsIgnoreCase("/index/c"))
            return 1;
        if (this.startPage.equalsIgnoreCase("/index/advancedsearch"))
            return 2;
        if (this.startPage.equalsIgnoreCase("/login"))
            return 3;
        if (this.startPage.equalsIgnoreCase("/index/last"))
            return 4;

        return 0;
    }

    public void setStartPageInt(int startPageInt) {

        final int startInt = startPageInt;

        switch(startInt) {
            case 0: startPage = "/index/"; break;
            case 1: startPage = "/index/c"; break;
            case 2: startPage = "/index/advancedsearch"; break;
            case 3: startPage = "/login"; break;
            case 4: startPage = "/index/last"; break;
        }
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public boolean isCreditSystemEnabled() {
        return creditSystemEnabled;
    }

    public void setCreditSystemEnabled(boolean creditSystemEnabled) {
        this.creditSystemEnabled = creditSystemEnabled;
    }

    public boolean isActivateNewUsers() {
        return activateNewUsers;
    }

    public void setActivateNewUsers(boolean activateNewUsers) {
        this.activateNewUsers = activateNewUsers;
    }

    public boolean isInformOfNewUsers() {
        return informOfNewUsers;
    }

    public void setInformOfNewUsers(boolean informOfNewUsers) {
        this.informOfNewUsers = informOfNewUsers;
    }

    public boolean isPassmailCopyAdmin() {
        return passmailCopyAdmin;
    }

    public void setPassmailCopyAdmin(boolean passmailCopyAdmin) {
        this.passmailCopyAdmin = passmailCopyAdmin;
    }

    public boolean isInformDownloadAdmin() {
        return informDownloadAdmin;
    }

    public void setInformDownloadAdmin(boolean informDownloadAdmin) {
        this.informDownloadAdmin = informDownloadAdmin;
    }

    public boolean isPassmailUser() {
        return passmailUser;
    }

    public void setPassmailUser(boolean passmailUser) {
        this.passmailUser = passmailUser;
    }

    public String getDownloadImageFilename() {
        return downloadImageFilename;
    }

    public void setDownloadImageFilename(String downloadImageFilename) {
        this.downloadImageFilename = downloadImageFilename;
    }

    public int getDownloadImageFilenameInt() {

        if (downloadImageFilename.equalsIgnoreCase("imageNumber")) {
            return 0;
        }
        if (downloadImageFilename.equalsIgnoreCase("versionTitle")) {
            return 1;
        }
        if (downloadImageFilename.equalsIgnoreCase("versionName")) {
            return 2;
        }
        return 0;
    }

    public void setDownloadImageFilenameInt(int downloadImageFilenameInt) {

        switch (downloadImageFilenameInt) {
            case 0: downloadImageFilename = "imageNumber";
                break;
            case 1: downloadImageFilename = "versionTitle";
                break;
            case 2: downloadImageFilename = "versionName";
                break;
            default:
                downloadImageFilename = "";
        }


    }

    public boolean isPopupIvidShowVersionTitle() {
        return popupIvidShowVersionTitle;
    }

    public void setPopupIvidShowVersionTitle(boolean popupIvidShowVersionTitle) {
        this.popupIvidShowVersionTitle = popupIvidShowVersionTitle;
    }

    public boolean isPopupIvidShowVersionSubTitle() {
        return popupIvidShowVersionSubTitle;
    }

    public void setPopupIvidShowVersionSubTitle(boolean popupIvidShowVersionSubTitle) {
        this.popupIvidShowVersionSubTitle = popupIvidShowVersionSubTitle;
    }

    public boolean isPopupIvidShowInfo() {
        return popupIvidShowInfo;
    }

    public void setPopupIvidShowInfo(boolean popupIvidShowInfo) {
        this.popupIvidShowInfo = popupIvidShowInfo;
    }

    public boolean isPopupIvidShowImagenumber() {
        return popupIvidShowImagenumber;
    }

    public void setPopupIvidShowImagenumber(boolean popupIvidShowImagenumber) {
        this.popupIvidShowImagenumber = popupIvidShowImagenumber;
    }

    public boolean isPopupIvidShowPhotographdate() {
        return popupIvidShowPhotographdate;
    }

    public void setPopupIvidShowPhotographdate(boolean popupIvidShowPhotographdate) {
        this.popupIvidShowPhotographdate = popupIvidShowPhotographdate;
    }

    public boolean isPopupIvidShowSite() {
        return popupIvidShowSite;
    }

    public void setPopupIvidShowSite(boolean popupIvidShowSite) {
        this.popupIvidShowSite = popupIvidShowSite;
    }

    public boolean isPopupIvidShowPhotographerAlias() {
        return popupIvidShowPhotographerAlias;
    }

    public void setPopupIvidShowPhotographerAlias(boolean popupIvidShowPhotographerAlias) {
        this.popupIvidShowPhotographerAlias = popupIvidShowPhotographerAlias;
    }

    public boolean isPopupIvidShowByline() {
        return popupIvidShowByline;
    }

    public void setPopupIvidShowByline(boolean popupIvidShowByline) {
        this.popupIvidShowByline = popupIvidShowByline;
    }

    public boolean isPopupIvidShowKbPixelDpi() {
        return popupIvidShowKbPixelDpi;
    }

    public void setPopupIvidShowKbPixelDpi(boolean popupIvidShowKbPixelDpi) {
        this.popupIvidShowKbPixelDpi = popupIvidShowKbPixelDpi;
    }

    public boolean isPopupIvidShowNote() {
        return popupIvidShowNote;
    }

    public void setPopupIvidShowNote(boolean popupIvidShowNote) {
        this.popupIvidShowNote = popupIvidShowNote;
    }

    public boolean isPopupIvidShowRestrictions() {
        return popupIvidShowRestrictions;
    }

    public void setPopupIvidShowRestrictions(boolean popupIvidShowRestrictions) {
        this.popupIvidShowRestrictions = popupIvidShowRestrictions;
    }

    public boolean isPopupIvidShowPeople() {
        return popupIvidShowPeople;
    }

    public void setPopupIvidShowPeople(boolean popupIvidShowPeople) {
        this.popupIvidShowPeople = popupIvidShowPeople;
    }

    public boolean isPopupIvidShowKeywords() {
        return popupIvidShowKeywords;
    }

    public void setPopupIvidShowKeywords(boolean popupIvidShowKeywords) {
        this.popupIvidShowKeywords = popupIvidShowKeywords;
    }

    public boolean isQuickDownload() {
        return quickDownload;
    }

    public void setQuickDownload(boolean quickDownload) {
        this.quickDownload = quickDownload;
    }

    public int getSortByFolder() {
        return sortByFolder;
    }

    public void setSortByFolder(int sortByFolder) {
        this.sortByFolder = sortByFolder;
    }

    public int getOrderByFolder() {
        return orderByFolder;
    }

    public void setOrderByFolder(int orderByFolder) {
        this.orderByFolder = orderByFolder;
    }

    public int getDefaultCredits() {
        return defaultCredits;
    }

    public void setDefaultCredits(int defaultCredits) {
        this.defaultCredits = defaultCredits;
    }

    public boolean isPopupIvidPageNavTop() {
        return popupIvidPageNavTop;
    }

    public void setPopupIvidPageNavTop(boolean popupIvidPageNavTop) {
        this.popupIvidPageNavTop = popupIvidPageNavTop;
    }

    public boolean isPopupIvidPageNavBottom() {
        return popupIvidPageNavBottom;
    }

    public void setPopupIvidPageNavBottom(boolean popupIvidPageNavBottom) {
        this.popupIvidPageNavBottom = popupIvidPageNavBottom;
    }

    public boolean isEditCopyTitle() {
        return editCopyTitle;
    }

    public void setEditCopyTitle(boolean editCopyTitle) {
        this.editCopyTitle = editCopyTitle;
    }

    public int getSearchPerEmail() {
        return searchPerEmail;
    }

    public void setSearchPerEmail(int searchPerEmail) {
        this.searchPerEmail = searchPerEmail;
    }

    public boolean isShowFormOnEmptySearch() {
        return showFormOnEmptySearch;
    }

    public void setShowFormOnEmptySearch(boolean showFormOnEmptySearch) {
        this.showFormOnEmptySearch = showFormOnEmptySearch;
    }


    public boolean isUseShoppingCart() {
        return useShoppingCart;
    }

    public void setUseShoppingCart(boolean useShoppingCart) {
        this.useShoppingCart = useShoppingCart;
    }

    public boolean isUseLightbox() {
        return useLightbox;
    }

    public void setUseLightbox(boolean useLightbox) {
        this.useLightbox = useLightbox;
    }

    public boolean isUseDownloadResolutions() {
        return useDownloadResolutions;
    }

    public void setUseDownloadResolutions(boolean useDownloadResolutions) {
        this.useDownloadResolutions = useDownloadResolutions;
    }

    public boolean isDownloadResOrig() {
        return downloadResOrig;
    }

    public void setDownloadResOrig(boolean downloadResOrig) {
        this.downloadResOrig = downloadResOrig;
    }

    public boolean isShowDownloadToVisitors() {
        return showDownloadToVisitors;
    }

    public void setShowDownloadToVisitors(boolean showDownloadToVisitors) {
        this.showDownloadToVisitors = showDownloadToVisitors;
    }

    public boolean isPodcastEnabled() {
        return podcastEnabled;
    }

    public void setPodcastEnabled(boolean podcastEnabled) {
        this.podcastEnabled = podcastEnabled;
    }


    public boolean isComplexPasswords() {
        return complexPasswords;
    }

    public void setComplexPasswords(boolean complexPasswords) {
        this.complexPasswords = complexPasswords;
    }

    public boolean isInlinePreview() {
        return inlinePreview;
    }

    public void setInlinePreview(boolean inlinePreview) {
        this.inlinePreview = inlinePreview;
    }

    public int getDefaultSecurityGroup() {
        return defaultSecurityGroup;
    }

    public void setDefaultSecurityGroup(int defaultSecurityGroup) {
        this.defaultSecurityGroup = defaultSecurityGroup;
    }

    public boolean isShowSendImage() {
        return showSendImage;
    }

    public void setShowSendImage(boolean showSendImage) {
        this.showSendImage = showSendImage;
    }

    public boolean isUseCaptchaRegister() {
        return useCaptchaRegister;
    }

    public void setUseCaptchaRegister(boolean useCaptchaRegister) {
        this.useCaptchaRegister = useCaptchaRegister;
    }

    public boolean isUseCaptchaPin() {
        return useCaptchaPin;
    }

    public void setUseCaptchaPin(boolean useCaptchaPin) {
        this.useCaptchaPin = useCaptchaPin;
    }

    public boolean isRobotsAllow() {
        return robotsAllow;
    }

    public void setRobotsAllow(boolean robotsAllow) {
        this.robotsAllow = robotsAllow;
    }

    public int getFolderSort() {
        return folderSort;
    }

    public void setFolderSort(int folderSort) {
        this.folderSort = folderSort;
    }

    public int getHomeFolderId() {
        return homeFolderId;
    }

    public void setHomeFolderId(int homeFolderId) {
        this.homeFolderId = homeFolderId;
    }

    public boolean isHomeFolderAsRoot() {
        return homeFolderAsRoot;
    }

    public void setHomeFolderAsRoot(boolean homeFolderAsRoot) {
        this.homeFolderAsRoot = homeFolderAsRoot;
    }

    public boolean isHomeFolderAutocreate() {
        return homeFolderAutocreate;
    }

    public void setHomeFolderAutocreate(boolean homeFolderAutocreate) {
        this.homeFolderAutocreate = homeFolderAutocreate;
    }

    public int getItemCountPerPage() {
        return itemCountPerPage;
    }

    public void setItemCountPerPage(int itemCountPerPage) {
        this.itemCountPerPage = itemCountPerPage;
    }

    public boolean isUseCaptchaSend() {
        return useCaptchaSend;
    }

    public void setUseCaptchaSend(boolean useCaptchaSend) {
        this.useCaptchaSend = useCaptchaSend;
    }

    public int getPinCodeKeyGen() {
        return pinCodeKeyGen;
    }

    public void setPinCodeKeyGen(int pinCodeKeyGen) {
        this.pinCodeKeyGen = pinCodeKeyGen;
    }

    public String getForbiddenDomains() {
        return forbiddenDomains;
    }

    public void setForbiddenDomains(String forbiddenDomains) {
        this.forbiddenDomains = forbiddenDomains;
    }

    public int getFolderDefaultViewOnRoot() {
        return folderDefaultViewOnRoot;
    }

    public void setFolderDefaultViewOnRoot(int folderDefaultViewOnRoot) {
        this.folderDefaultViewOnRoot = folderDefaultViewOnRoot;
    }

    public boolean isUsersCanSendAttachments() {
        return usersCanSendAttachments;
    }

    public void setUsersCanSendAttachments(boolean usersCanSendAttachments) {
        this.usersCanSendAttachments = usersCanSendAttachments;
    }


    public boolean isWsUsersyncEnabled() {
        return wsUsersyncEnabled;
    }

    public void setWsUsersyncEnabled(boolean wsUsersyncEnabled) {
        this.wsUsersyncEnabled = wsUsersyncEnabled;
    }

    public String getWsUsersyncUrl() {
        return wsUsersyncUrl;
    }

    public void setWsUsersyncUrl(String wsUsersyncUrl) {
        this.wsUsersyncUrl = wsUsersyncUrl;
    }

    public String getWsUsersyncUsername() {
        return wsUsersyncUsername;
    }

    public void setWsUsersyncUsername(String wsUsersyncUsername) {
        this.wsUsersyncUsername = wsUsersyncUsername;
    }

    public String getWsUsersyncPassword() {
        return wsUsersyncPassword;
    }

    public void setWsUsersyncPassword(String wsUsersyncPassword) {
        this.wsUsersyncPassword = wsUsersyncPassword;
    }

    public String getWsUsersyncGroupnameFilter() {
        return wsUsersyncGroupnameFilter;
    }

    public void setWsUsersyncGroupnameFilter(String wsUsersyncGroupnameFilter) {
        this.wsUsersyncGroupnameFilter = wsUsersyncGroupnameFilter;
    }

    public boolean isWsUsersyncTrustAllCerts() {
        return wsUsersyncTrustAllCerts;
    }

    public void setWsUsersyncTrustAllCerts(boolean wsUsersyncTrustAllCerts) {
        this.wsUsersyncTrustAllCerts = wsUsersyncTrustAllCerts;
    }

    public boolean isStreamEnabled() {
        return streamEnabled;
    }

    public void setStreamEnabled(boolean streamEnabled) {
        this.streamEnabled = streamEnabled;
    }

    public boolean isStreamToVisitors() {
        return streamToVisitors;
    }

    public void setStreamToVisitors(boolean streamToVisitors) {
        this.streamToVisitors = streamToVisitors;
    }

    public int getStreamConvertToKbitSek() {
        return streamConvertToKbitSek;
    }

    public void setStreamConvertToKbitSek(int streamConvertToKbitSek) {
        this.streamConvertToKbitSek = streamConvertToKbitSek;
    }

    public boolean isBlankWhenFieldEmpty() {
        return blankWhenFieldEmpty;
    }

    public void setBlankWhenFieldEmpty(boolean blankWhenFieldEmpty) {
        this.blankWhenFieldEmpty = blankWhenFieldEmpty;
    }

    public boolean isUserEmailAsUsername() {
        return userEmailAsUsername;
    }

    public void setUserEmailAsUsername(boolean userEmailAsUsername) {
        this.userEmailAsUsername = userEmailAsUsername;
    }

    public boolean isShowUserCompanyFields() {
        return showUserCompanyFields;
    }

    public void setShowUserCompanyFields(boolean showUserCompanyFields) {
        this.showUserCompanyFields = showUserCompanyFields;
    }

    public boolean isShowUserAddressFields() {
        return showUserAddressFields;
    }

    public void setShowUserAddressFields(boolean showUserAddressFields) {
        this.showUserAddressFields = showUserAddressFields;
    }

    public boolean isShowUserTelFaxFields() {
        return showUserTelFaxFields;
    }

    public void setShowUserTelFaxFields(boolean showUserTelFaxFields) {
        this.showUserTelFaxFields = showUserTelFaxFields;
    }

    public boolean isAllowRegister() {
        return allowRegister;
    }

    public void setAllowRegister(boolean allowRegister) {
        this.allowRegister = allowRegister;
    }

    public boolean isAdvancedSearchEnabled() {
        return advancedSearchEnabled;
    }

    public void setAdvancedSearchEnabled(boolean advancedSearchEnabled) {
        this.advancedSearchEnabled = advancedSearchEnabled;
    }

    public boolean isResetSecurityGroupWhenUserIsDisabled() {
        return resetSecurityGroupWhenUserIsDisabled;
    }

    public void setResetSecurityGroupWhenUserIsDisabled(boolean resetSecurityGroupWhenUserIsDisabled) {
        this.resetSecurityGroupWhenUserIsDisabled = resetSecurityGroupWhenUserIsDisabled;
    }

    public boolean isOnlyLoggedinUsers() {
        return onlyLoggedinUsers;
    }

    public void setOnlyLoggedinUsers(boolean onlyLoggedinUsers) {
        this.onlyLoggedinUsers = onlyLoggedinUsers;
    }

    public int getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(int defaultRole) {
        this.defaultRole = defaultRole;
    }

    public String getConfigParam() {
        return configParam;
    }

    public void setConfigParam(String configParam) {
        this.configParam = configParam;
    }

    public String getPaymillKeyPublic() {
        return paymillKeyPublic;
    }

    public void setPaymillKeyPublic(String paymillKeyPublic) {
        this.paymillKeyPublic = paymillKeyPublic;
    }

    public String getPaymillKeyPrivate() {
        return paymillKeyPrivate;
    }

    public void setPaymillKeyPrivate(String paymillKeyPrivate) {
        this.paymillKeyPrivate = paymillKeyPrivate;
    }

    public int getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(int vatPercent) {
        this.vatPercent = vatPercent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isEditCopyPrice() {
        return editCopyPrice;
    }

    public void setEditCopyPrice(boolean editCopyPrice) {
        this.editCopyPrice = editCopyPrice;
    }

    public boolean isEditCopyMasterdata() {
        return editCopyMasterdata;
    }

    public void setEditCopyMasterdata(boolean editCopyMasterdata) {
        this.editCopyMasterdata = editCopyMasterdata;
    }

    public boolean isEditCopyLicValid() {
        return editCopyLicValid;
    }

    public void setEditCopyLicValid(boolean editCopyLicValid) {
        this.editCopyLicValid = editCopyLicValid;
    }
}
