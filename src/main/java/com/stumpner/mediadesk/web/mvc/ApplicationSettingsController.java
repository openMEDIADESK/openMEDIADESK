package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.ApplicationSettings;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

import java.util.List;

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
 * Time: 12:48:30
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationSettingsController extends SimpleFormControllerMd {

    public ApplicationSettingsController() {

        this.setCommandClass(ApplicationSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        ApplicationSettings settings = new ApplicationSettings();
        settings.setStartPage(Config.redirectStartPage);

        settings.setCreditSystemEnabled(Config.creditSystemEnabled);

        settings.setActivateNewUsers(Config.activateNewUsers);
        settings.setInformOfNewUsers(Config.informOfNewUsers);
        settings.setPassmailUser(Config.passmailUser);
        settings.setPassmailCopyAdmin(Config.passmailCopyAdmin);
        settings.setInformDownloadAdmin(Config.informDownloadAdmin);
        settings.setDefaultCredits(Config.defaultCredits);

        settings.setFolderLatestOnRoot(Config.folderLatestOnRoot);

        settings.setDownloadImageFilename(Config.downloadImageFilename);

        settings.setPopupIvidShowVersionTitle(Config.popupIvidShowVersionTitle);
        settings.setPopupIvidShowVersionSubTitle(Config.popupIvidShowVersionSubTitle);
        settings.setPopupIvidShowInfo(Config.popupIvidShowInfo);
        settings.setPopupIvidShowImagenumber(Config.popupIvidShowImagenumber);
        settings.setPopupIvidShowPhotographdate(Config.popupIvidShowPhotographdate);
        settings.setPopupIvidShowSite(Config.popupIvidShowSite);
        settings.setPopupIvidShowPhotographerAlias(Config.popupIvidShowPhotographerAlias);
        settings.setPopupIvidShowByline(Config.popupIvidShowByline);
        settings.setPopupIvidShowKbPixelDpi(Config.popupIvidShowKbPixelDpi);
        settings.setPopupIvidShowPeople(Config.popupIvidShowPeople);
        settings.setPopupIvidShowNote(Config.popupIvidShowNote);
        settings.setPopupIvidShowRestrictions(Config.popupIvidShowRestrictions);
        settings.setPopupIvidShowKeywords(Config.popupIvidKeywords);
        settings.setPopupIvidPageNavTop(Config.popupIvidPageNavTop);
        settings.setPopupIvidPageNavBottom(Config.popupIvidPageNavBottom);

        settings.setInlinePreview(Config.inlinePreview);

        settings.setUpstreamingStartpageUrl(Config.upstreamingStartpageUrl);
        settings.setQuickDownload(Config.quickDownload);

        settings.setSearchAnd(Config.searchAnd);

        settings.setSortByFolder(Config.sortByFolder);
        settings.setOrderByFolder(Config.orderByFolder);

        settings.setEditCopyTitle(Config.editCopyTitle);
        settings.setEditCopyTitleLng1(Config.editCopyTitleLng1);
        settings.setEditCopyTitleLng2(Config.editCopyTitleLng2);

        settings.setEditCopySubTitle(Config.editCopySubTitle);
        settings.setEditCopySubTitleLng1(Config.editCopySubTitleLng1);
        settings.setEditCopySubTitleLng2(Config.editCopySubTitleLng2);

        settings.setEditCopyInfo(Config.editCopyInfo);
        settings.setEditCopyInfoLng1(Config.editCopyInfoLng1);
        settings.setEditCopyInfoLng2(Config.editCopyInfoLng2);

        settings.setEditCopySite(Config.editCopySite);
        settings.setEditCopyPhotographDate(Config.editCopyPhotographDate);
        settings.setEditCopyPhotographer(Config.editCopyPhotographer);
        settings.setEditCopyByline(Config.editCopyByline);
        settings.setEditCopyKeywords(Config.editCopyKeywords);

        settings.setEditCopyPeople(Config.editCopyPeople);
        settings.setEditCopyOrientation(Config.editCopyOrientation);
        settings.setEditCopyPerspective(Config.editCopyPerspective);
        settings.setEditCopyMotive(Config.editCopyMotive);
        settings.setEditCopyGesture(Config.editCopyGesture);
        settings.setEditCopyNote(Config.editCopyNote);
        settings.setEditCopyRestrictions(Config.editCopyRestrictions);
        settings.setEditCopyFlag(Config.editCopyFlag);

        settings.setEditCopySiteLng1(Config.editCopySiteLng1);
        settings.setEditCopySiteLng2(Config.editCopySiteLng2);
        settings.setEditCopyNoteLng1(Config.editCopyNoteLng1);
        settings.setEditCopyNoteLng2(Config.editCopyNoteLng2);
        settings.setEditCopyRestrictionsLng1(Config.editCopyRestrictionsLng1);
        settings.setEditCopyRestrictionsLng2(Config.editCopyRestrictionsLng2);

        settings.setEditCopyPrice(Config.editCopyPrice);
        settings.setEditCopyLicValid(Config.editCopyLicValid);

        settings.setSearchPerEmail(Config.searchPerEmail);
        settings.setShowFormOnEmptySearch(Config.showFormOnEmptySearch);

        settings.setUseShoppingCart(Config.useShoppingCart);
        settings.setUseLightbox(Config.useLightbox);

        settings.setUseDownloadResolutions(Config.useDownloadResolutions);
        settings.setDownloadResOrig(Config.downloadResOrig);
        settings.setShowDownloadToVisitors(Config.showDownloadToVisitors);
        settings.setPodcastEnabled(Config.podcastEnabled);

        settings.setComplexPasswords(Config.complexPasswords);
        settings.setDefaultSecurityGroup(Config.defaultSecurityGroup);
        settings.setDefaultRole(Config.defaultRole);

        settings.setShowSendImage(Config.showSendImage);
        settings.setUseCaptchaRegister(Config.useCaptchaRegister);
        settings.setUseCaptchaSend(Config.useCaptchaSend);
        settings.setUseCaptchaPin(Config.useCaptchaPin);
        settings.setPinCodeKeyGen(Config.pinCodeKeyGen);
        settings.setRobotsAllow(Config.robotsAllow);
        settings.setFolderSort(Config.folderSort);

        settings.setWording(Config.wording);

        settings.setHomeFolderId(Config.homeFolderId);
        settings.setHomeFolderAsRoot(Config.homeFolderAsRoot);
        settings.setHomeFolderAutocreate(Config.homeFolderAutocreate);

        settings.setItemCountPerPage(Config.itemCountPerPage);

        settings.setForbiddenDomains(Config.forbiddenDomains);

        settings.setFolderDefaultViewOnRoot(Config.folderDefaultViewOnRoot);

        settings.setUsersCanSendAttachments(Config.usersCanSendAttachments);

        settings.setWsUsersyncEnabled(Config.wsUsersyncEnabled);
        settings.setWsUsersyncUrl(Config.wsUsersyncUrl);
        settings.setWsUsersyncUsername(Config.wsUsersyncUsername);
        settings.setWsUsersyncPassword(Config.wsUsersyncPassword);
        settings.setWsUsersyncGroupnameFilter(Config.wsUsersyncGroupnameFilter);
        settings.setWsUsersyncTrustAllCerts(Config.wsUsersyncTrustAllCerts);

        settings.setStreamEnabled(Config.streamEnabled);
        settings.setStreamToVisitors(Config.streamToVisitors);
        settings.setStreamConvertToKbitSek(Config.streamConvertToKbitSek);

        settings.setWebdavEnabled(Config.webdavEnabled);

        settings.setBlankWhenFieldEmpty(Config.blankWhenFieldEmpty);

        settings.setUserEmailAsUsername(Config.userEmailAsUsername);
        settings.setShowUserCompanyFields(Config.showUserCompanyFields);
        settings.setShowUserAddressFields(Config.showUserAddressFields);
        settings.setShowUserTelFaxFields(Config.showUserTelFaxFields);

        settings.setAllowRegister(Config.allowRegister);

        settings.setAdvancedSearchEnabled(Config.advancedSearchEnabled);
        settings.setResetSecurityGroupWhenUserIsDisabled(Config.resetSecurityGroupWhenUserIsDisabled);

        settings.setOnlyLoggedinUsers(Config.onlyLoggedinUsers);

        settings.setConfigParam(Config.configParam);

        settings.setPaymillKeyPublic(Config.paymillKeyPublic);
        settings.setPaymillKeyPrivate(Config.paymillKeyPrivate);
        settings.setVatPercent(Config.vatPercent);

        settings.setCurrency(Config.currency);

        return settings;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        Config.putDmsConfigToRequest(httpServletRequest);

        LngResolver lngResolver = new LngResolver();

        UserService userService = new UserService();
        httpServletRequest.setAttribute("securityGroupList",userService.getRealSecurityGroupList());
        FolderService folderService = new FolderService();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        List rootCategoryList = folderService.getFolderSubTree(0,0);
        httpServletRequest.setAttribute("rootCategoryList",rootCategoryList);

        this.setContentTemplateFile("settings_programm.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        ApplicationSettings settings = (ApplicationSettings)o;
        Config.redirectStartPage = settings.getStartPage();
        Config.wording = settings.getWording();

        this.getServletContext().setAttribute("home",Config.redirectStartPage);
        this.getServletContext().setAttribute("redirectStartPage",Config.redirectStartPage);
        Config.upstreamingStartpageUrl = settings.getUpstreamingStartpageUrl();
        if (Config.upstreamingStartpageUrl.length()>0) {
            this.getServletContext().setAttribute("home",Config.upstreamingStartpageUrl);
        }

        Config.creditSystemEnabled = settings.isCreditSystemEnabled();

        Config.activateNewUsers = settings.isActivateNewUsers();
        Config.informOfNewUsers = settings.isInformOfNewUsers();
        Config.passmailUser = settings.isPassmailUser();
        Config.passmailCopyAdmin = settings.isPassmailCopyAdmin();
        Config.informDownloadAdmin = settings.isInformDownloadAdmin();
        Config.defaultCredits = settings.getDefaultCredits();

        Config.folderLatestOnRoot = settings.isFolderLatestOnRoot();

        Config.downloadImageFilename = settings.getDownloadImageFilename();
        Config.quickDownload = settings.isQuickDownload();

        // Anzeige

        Config.popupIvidShowVersionTitle = settings.isPopupIvidShowVersionTitle();
        Config.popupIvidShowVersionSubTitle = settings.isPopupIvidShowVersionSubTitle();
        Config.popupIvidShowInfo = settings.isPopupIvidShowInfo();
        Config.popupIvidShowImagenumber = settings.isPopupIvidShowImagenumber();
        Config.popupIvidShowPhotographdate = settings.isPopupIvidShowPhotographdate();
        Config.popupIvidShowSite = settings.isPopupIvidShowSite();
        Config.popupIvidShowPhotographerAlias = settings.isPopupIvidShowPhotographerAlias();
        Config.popupIvidShowByline = settings.isPopupIvidShowByline();
        Config.popupIvidShowKbPixelDpi = settings.isPopupIvidShowKbPixelDpi();
        Config.popupIvidShowPeople = settings.isPopupIvidShowPeople();
        Config.popupIvidShowNote = settings.isPopupIvidShowNote();
        Config.popupIvidShowRestrictions = settings.isPopupIvidShowRestrictions();
        Config.popupIvidKeywords = settings.isPopupIvidShowKeywords();
        Config.popupIvidPageNavTop = settings.isPopupIvidPageNavTop();
        Config.popupIvidPageNavBottom = settings.isPopupIvidPageNavBottom();

        Config.inlinePreview = settings.isInlinePreview();

        Config.searchAnd = settings.isSearchAnd();
        Config.sortByFolder = settings.getSortByFolder();
        Config.orderByFolder = settings.getOrderByFolder();

        Config.editCopyTitle = settings.isEditCopyTitle();
        Config.editCopyTitleLng1 = settings.isEditCopyTitleLng1();
        Config.editCopyTitleLng2 = settings.isEditCopyTitleLng2();

        Config.editCopySubTitle = settings.isEditCopySubTitle();
        Config.editCopySubTitleLng1 = settings.isEditCopySubTitleLng1();
        Config.editCopySubTitleLng2 = settings.isEditCopySubTitleLng2();

        Config.editCopyInfo = settings.isEditCopyInfo();
        Config.editCopyInfoLng1 = settings.isEditCopyInfoLng1();
        Config.editCopyInfoLng2 = settings.isEditCopyInfoLng2();

        Config.editCopySite = settings.isEditCopySite();
        Config.editCopyPhotographDate = settings.isEditCopyPhotographDate();
        Config.editCopyPhotographer = settings.isEditCopyPhotographer();
        Config.editCopyByline = settings.isEditCopyByline();
        Config.editCopyKeywords = settings.isEditCopyKeywords();

        Config.editCopyPeople = settings.isEditCopyPeople();
        Config.editCopyOrientation = settings.isEditCopyOrientation();
        Config.editCopyPerspective = settings.isEditCopyPerspective();
        Config.editCopyMotive = settings.isEditCopyMotive();
        Config.editCopyGesture = settings.isEditCopyGesture();
        Config.editCopyNote = settings.isEditCopyNote();
        Config.editCopyRestrictions = settings.isEditCopyRestrictions();
        Config.editCopyFlag = settings.isEditCopyFlag();

        Config.editCopySiteLng1 = settings.isEditCopySiteLng1();
        Config.editCopySiteLng2 = settings.isEditCopySiteLng2();
        Config.editCopyNoteLng1 = settings.isEditCopyNoteLng1();
        Config.editCopyNoteLng2 = settings.isEditCopyNoteLng2();
        Config.editCopyRestrictionsLng1 = settings.isEditCopyRestrictionsLng1();
        Config.editCopyRestrictionsLng2 = settings.isEditCopyRestrictionsLng2();

        Config.editCopyPrice = settings.isEditCopyPrice();
        Config.editCopyLicValid = settings.isEditCopyLicValid();

        Config.searchPerEmail = settings.getSearchPerEmail();
        Config.showFormOnEmptySearch = settings.isShowFormOnEmptySearch();

        Config.useShoppingCart = settings.isUseShoppingCart();
        Config.useLightbox = settings.isUseLightbox();

        Config.useDownloadResolutions = settings.isUseDownloadResolutions();
        Config.downloadResOrig = settings.isDownloadResOrig();

        Config.showDownloadToVisitors = settings.isShowDownloadToVisitors();
        Config.podcastEnabled = settings.isPodcastEnabled();
        Config.complexPasswords = settings.isComplexPasswords();

        Config.defaultSecurityGroup = settings.getDefaultSecurityGroup();
        Config.defaultRole = settings.getDefaultRole();
        Config.showSendImage = settings.isShowSendImage();

        Config.homeFolderId = settings.getHomeFolderId();
        Config.homeFolderAsRoot = settings.isHomeFolderAsRoot();
        Config.homeFolderAutocreate = settings.isHomeFolderAutocreate();
        
        Config.useCaptchaSend = settings.isUseCaptchaSend();
        Config.useCaptchaRegister = settings.isUseCaptchaRegister();
        Config.useCaptchaPin = settings.isUseCaptchaPin();
        Config.pinCodeKeyGen = settings.getPinCodeKeyGen();
        Config.robotsAllow = settings.isRobotsAllow();
        Config.folderSort = settings.getFolderSort();

        Config.itemCountPerPage = settings.getItemCountPerPage();

        Config.forbiddenDomains = settings.getForbiddenDomains();

        Config.folderDefaultViewOnRoot = settings.getFolderDefaultViewOnRoot();

        Config.usersCanSendAttachments = settings.isUsersCanSendAttachments();

        Config.wsUsersyncEnabled = settings.isWsUsersyncEnabled();
        Config.wsUsersyncUrl = settings.getWsUsersyncUrl();
        Config.wsUsersyncUsername = settings.getWsUsersyncUsername();
        Config.wsUsersyncPassword = settings.getWsUsersyncPassword();
        Config.wsUsersyncGroupnameFilter = settings.getWsUsersyncGroupnameFilter();
        Config.wsUsersyncTrustAllCerts = settings.isWsUsersyncTrustAllCerts();

        Config.streamEnabled = settings.isStreamEnabled();
        Config.streamToVisitors = settings.isStreamToVisitors();
        Config.streamConvertToKbitSek = settings.getStreamConvertToKbitSek();

        Config.webdavEnabled = settings.isWebdavEnabled();

        Config.blankWhenFieldEmpty = settings.isBlankWhenFieldEmpty();

        //Email-Benutzer �ndern
        if (Config.userEmailAsUsername!=settings.isUserEmailAsUsername()) {
            UserService userService = new UserService();

            if (settings.isUserEmailAsUsername()) {
                //Email als Benutzername (Alle Emailadresse in den Usernamen kopieren)
                userService.setEmailAsUsername();
            } else {
                //Benutzername als Benutzername
                userService.setUsernameAsUsername();
            }

        }

        Config.userEmailAsUsername = settings.isUserEmailAsUsername();
        Config.showUserCompanyFields = settings.isShowUserCompanyFields();
        Config.showUserAddressFields = settings.isShowUserAddressFields();
        Config.showUserTelFaxFields = settings.isShowUserTelFaxFields();

        Config.allowRegister = settings.isAllowRegister();
        
        Config.advancedSearchEnabled = settings.isAdvancedSearchEnabled();
        Config.resetSecurityGroupWhenUserIsDisabled = settings.isResetSecurityGroupWhenUserIsDisabled();

        Config.onlyLoggedinUsers = settings.isOnlyLoggedinUsers();

        Config.configParam = settings.getConfigParam();

        Config.paymillKeyPublic = settings.getPaymillKeyPublic();
        Config.paymillKeyPrivate = settings.getPaymillKeyPrivate();
        Config.vatPercent = settings.getVatPercent();

        Config.currency = settings.getCurrency();

        Config.saveConfiguration();

        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("settings")
        );

        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBind(HttpServletRequest httpServletRequest, Object object) throws Exception {

        ApplicationSettings as = (ApplicationSettings)object;

        if (httpServletRequest.getParameter("folderLatestOnRoot")==null) {
            as.setFolderLatestOnRoot(false);
        } else {
            as.setFolderLatestOnRoot(true);
        }

        if (httpServletRequest.getParameter("editCopyTitle")==null) {
            as.setEditCopyTitle(false);
        } else {
            as.setEditCopyTitle(true);
        }
        if (httpServletRequest.getParameter("editCopyTitleLng1")==null) {
            as.setEditCopyTitleLng1(false);
        } else {
            as.setEditCopyTitleLng1(true);
        }
        if (httpServletRequest.getParameter("editCopyTitleLng2")==null) {
            as.setEditCopyTitleLng2(false);
        } else {
            as.setEditCopyTitleLng2(true);
        }

        if (httpServletRequest.getParameter("editCopySubTitle")==null) {
            as.setEditCopySubTitle(false);
        } else {
            as.setEditCopySubTitle(true);
        }
        if (httpServletRequest.getParameter("editCopySubTitleLng1")==null) {
            as.setEditCopySubTitleLng1(false);
        } else {
            as.setEditCopySubTitleLng1(true);
        }
        if (httpServletRequest.getParameter("editCopySubTitleLng2")==null) {
            as.setEditCopySubTitleLng2(false);
        } else {
            as.setEditCopySubTitleLng2(true);
        }

        if (httpServletRequest.getParameter("editCopyInfo")==null) {
            as.setEditCopyInfo(false);
        } else {
            as.setEditCopyInfo(true);
        }
        if (httpServletRequest.getParameter("editCopyInfoLng1")==null) {
            as.setEditCopyInfoLng1(false);
        } else {
            as.setEditCopyInfoLng1(true);
        }
        if (httpServletRequest.getParameter("editCopyInfoLng2")==null) {
            as.setEditCopyInfoLng2(false);
        } else {
            as.setEditCopyInfoLng2(true);
        }

        if (httpServletRequest.getParameter("editCopySite")==null) {
            as.setEditCopySite(false);
        } else {
            as.setEditCopySite(true);
        }
        if (httpServletRequest.getParameter("editCopySiteLng1")==null) {
            as.setEditCopySiteLng1(false);
        } else {
            as.setEditCopySiteLng1(true);
        }
        if (httpServletRequest.getParameter("editCopySiteLng2")==null) {
            as.setEditCopySiteLng2(false);
        } else {
            as.setEditCopySiteLng2(true);
        }

        if (httpServletRequest.getParameter("editCopyNote")==null) {
            as.setEditCopyNote(false);
        } else {
            as.setEditCopyNote(true);
        }
        if (httpServletRequest.getParameter("editCopyNoteLng1")==null) {
            as.setEditCopyNoteLng1(false);
        } else {
            as.setEditCopyNoteLng1(true);
        }
        if (httpServletRequest.getParameter("editCopyNoteLng2")==null) {
            as.setEditCopyNoteLng2(false);
        } else {
            as.setEditCopyNoteLng2(true);
        }

        if (httpServletRequest.getParameter("editCopyPhotographDate")==null) {
            as.setEditCopyPhotographDate(false);
        } else {
            as.setEditCopyPhotographDate(true);
        }
        if (httpServletRequest.getParameter("editCopyPhotographer")==null) {
            as.setEditCopyPhotographer(false);
        } else {
            as.setEditCopyPhotographer(true);
        }
        if (httpServletRequest.getParameter("editCopyByline")==null) {
            as.setEditCopyByline(false);
        } else {
            as.setEditCopyByline(true);
        }
        if (httpServletRequest.getParameter("editCopyKeywords")==null) {
            as.setEditCopyKeywords(false);
        } else {
            as.setEditCopyKeywords(true);
        }

        if (httpServletRequest.getParameter("editCopyPeople")==null) {
            as.setEditCopyPeople(false);
        } else {
            as.setEditCopyPeople(true);
        }
        if (httpServletRequest.getParameter("editCopyOrientation")==null) {
            as.setEditCopyOrientation(false);
        } else {
            as.setEditCopyOrientation(true);
        }
        if (httpServletRequest.getParameter("editCopyPerspective")==null) {
            as.setEditCopyPerspective(false);
        } else {
            as.setEditCopyPerspective(true);
        }
        if (httpServletRequest.getParameter("editCopyMotive")==null) {
            as.setEditCopyMotive(false);
        } else {
            as.setEditCopyMotive(true);
        }
        if (httpServletRequest.getParameter("editCopyGesture")==null) {
            as.setEditCopyGesture(false);
        } else {
            as.setEditCopyGesture(true);
        }
        if (httpServletRequest.getParameter("editCopyRestrictions")==null) {
            as.setEditCopyRestrictions(false);
        } else {
            as.setEditCopyRestrictions(true);
        }
        if (httpServletRequest.getParameter("editCopyFlag")==null) {
            as.setEditCopyFlag(false);
        } else {
            as.setEditCopyFlag(true);
        }
        if (httpServletRequest.getParameter("editCopyPrice")==null) {
            as.setEditCopyPrice(false);
        } else {
            as.setEditCopyPrice(true);
        }
        if (httpServletRequest.getParameter("editCopyLicValid")==null) {
            as.setEditCopyLicValid(false);
        } else {
            as.setEditCopyLicValid(true);
        }

        super.onBind(httpServletRequest, object);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
