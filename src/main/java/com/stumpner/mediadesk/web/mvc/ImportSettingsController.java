package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.ImportSettings;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Date: 27.12.2005
 * Time: 11:48:40
 * To change this template use File | Settings | File Templates.
 */
public class ImportSettingsController extends SimpleFormControllerMd {

    ArrayList metadataHeaders = new ArrayList();

    public ImportSettingsController() {

        this.contructMetaFields();
        this.model.put("metadataHeaders",metadataHeaders);
        this.setCommandClass(ImportSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        ImportSettings set = (ImportSettings)o;
        if (set.isEmailImportEnabled()) {
            if (!set.getEmailImportPassword().equals(set.getEmailImportPasswordAgain())) {
                e.rejectValue("emailImportPassword","set.import.error.passwordagain");
                e.rejectValue("emailImportPasswordAgain","set.import.error.passwordagain");
                e.reject("set.import.error.passwordagain","DM::!!password do not match!!");
                //System.out.println("Email stimmt nicht überein");
            }
        }
        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        httpServletRequest.getSession().setAttribute("metadataHeaders",metadataHeaders);
        ImportSettings set = new ImportSettings();
        /*
    String importName = "Job Name";
    String importTitle = "Job Name";
    String importNumber = "Object Name";
    String importByline = "Copyright Notice";
    String importPhotographerAlias = "By-line";
    String importSite = "By-line Title";
    String importPeople = "Writer/Editor";
    String importInfo = "Caption/Abstract";
        */

        set.setImportName(Config.importName);
        set.setImportTitle(Config.importTitle);
        set.setImportNumber(Config.importNumber);
        set.setImportByline(Config.importByline);
        set.setImportPhotographerAlias(Config.importPhotographerAlias);
        set.setImportSite(Config.importSite);
        set.setImportPeople(Config.importPeople);
        set.setImportInfo(Config.importInfo);

        set.setImportKeywords(Config.importKeywords);
        set.setImportSubtitle(Config.importSubtitle);
        set.setImportNote(Config.importNote);
        set.setImportRestrictions(Config.importRestrictions);

        set.setImportDate(Config.importDate);
        set.setImportImageNumberSerially(Config.importImageNumberSerially);
        set.setFileEncoding(Config.fileEncoding);

        set.setEmailImportEnabled(Config.emailImportEnabled);
        set.setEmailImportHost(Config.emailImportHost);
        set.setEmailImportPassword(Config.emailImportPassword);
        set.setEmailImportPasswordAgain(Config.emailImportPassword);
        set.setEmailImportUsername(Config.emailImportUsername);
        set.setEmailImportLastError(Config.emailImportLastError);
        set.setAutoImportEmailCat(Config.autoImportEmailCat);

        set.setFtpHost(Config.ftpHost);
        set.setFtpUser(Config.ftpUser);
        set.setFtpPassword(Config.ftpPassword);
        set.setAutoImportFtpCat(Config.autoImportFtpCat);
        set.setAutoImportFtp(Config.autoimportFtp);

        /*
        Config.emailImportEnabled = cs.isEmailImportEnabled();
        Config.emailImportHost = cs.getEmailImportHost();
        Config.emailImportUsername = cs.getEmailImportUsername();
        Config.emailImportPassword = cs.getEmailImportPassword();
        Config.autoImportEmailCat = cs.getAutoImportEmailCat();

        Config.ftpHost = cs.getFtpHost();
        Config.ftpUser = cs.getFtpUser();
        Config.ftpPassword = cs.getFtpPassword();
        Config.autoImportFtpCat = cs.getAutoImportFtpCat();
        Config.autoimportFtp = cs.isAutoImportFtp();
         */

        set.setImagesizePreview(Config.imagesizePreview);
        set.setImagesizeThumbnail(Config.imagesizeThumbnail);

        return set;
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBind(HttpServletRequest httpServletRequest, Object object) throws Exception {

        ImportSettings is = (ImportSettings)object;
        if (httpServletRequest.getParameter("importImageNumberSerially")==null) {
            is.setImportImageNumberSerially(false);
            logger.debug("Imagenumber Serially = false");
        } else {
            is.setImportImageNumberSerially(true);
            logger.debug("Imagenumber Serially = true");
        }
        if (httpServletRequest.getParameter("emailImportEnabled")==null) {
            is.setEmailImportEnabled(false);
        } else {
            is.setEmailImportEnabled(true);
        }
        if (httpServletRequest.getParameter("autoImportFtp")==null) {
            is.setAutoImportFtp(false);
        } else {
            is.setAutoImportFtp(true);
        }


        super.onBind(httpServletRequest, object);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        Config.putDmsConfigToRequest(httpServletRequest);

        FolderService folderService = new FolderService();
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        List categoryList = folderService.getFolderList(0);
        //Wenn ausgewählte Kategorie keine Root Kategorie, dann zusätzlich laden
        ImportSettings settings = (ImportSettings)e.getTarget();
        if (settings.getAutoImportFtpCat()!=0) {
            try {
                Folder cat = folderService.getFolderById(settings.getAutoImportFtpCat());
                if (cat.getParent()!=0) {
                    categoryList.add(cat);
                }
            } catch (ObjectNotFoundException e2) {
                //e2.printStackTrace();
                System.err.println("ImportSettingsController: getFolderById "+settings.getAutoImportFtpCat()+" Ordner existiert nicht");
            }
        }
        
        httpServletRequest.setAttribute("folderList",categoryList);


        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        ImportSettings cs = (ImportSettings)o;

        Config.importName=cs.getImportName();
        Config.importTitle=cs.getImportTitle();
        Config.importNumber=cs.getImportNumber();
        Config.importByline=cs.getImportByline();
        Config.importPhotographerAlias=cs.getImportPhotographerAlias();
        Config.importSite=cs.getImportSite();
        Config.importPeople=cs.getImportPeople();
        Config.importInfo=cs.getImportInfo();

        Config.importSubtitle=cs.getImportSubtitle();
        Config.importKeywords=cs.getImportKeywords();
        Config.importNote=cs.getImportNote();
        Config.importRestrictions=cs.getImportRestrictions();
        Config.importDate=cs.getImportDate();
        Config.importImageNumberSerially=cs.isImportImageNumberSerially();
        Config.fileEncoding=cs.getFileEncoding();

        Config.emailImportEnabled = cs.isEmailImportEnabled();
        Config.emailImportHost = cs.getEmailImportHost();
        Config.emailImportUsername = cs.getEmailImportUsername();
        Config.emailImportPassword = cs.getEmailImportPassword();
        Config.autoImportEmailCat = cs.getAutoImportEmailCat();

        Config.ftpHost = cs.getFtpHost();
        Config.ftpUser = cs.getFtpUser();
        Config.ftpPassword = cs.getFtpPassword();
        Config.autoImportFtpCat = cs.getAutoImportFtpCat();
        Config.autoimportFtp = cs.isAutoImportFtp();

        Config.imagesizePreview = cs.getImagesizePreview();
        Config.imagesizeThumbnail = cs.getImagesizeThumbnail();

        Config.saveConfiguration();

        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("settings")
        );

        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void contructMetaFields() {

        metadataHeaders.add("-------------");
        metadataHeaders.add("Job Name");
        metadataHeaders.add("Object Name");
        metadataHeaders.add("Copyright Notice");
        metadataHeaders.add("By-line");
        metadataHeaders.add("By-line Title");
        metadataHeaders.add("Writer/Editor");
        metadataHeaders.add("Caption/Abstract");
        metadataHeaders.add("Headline");
        metadataHeaders.add("Special Instructions");
        metadataHeaders.add("Credit");
        metadataHeaders.add("Source");
        metadataHeaders.add("Date Created");
        metadataHeaders.add("Date/Time Original");
        metadataHeaders.add("Date/Time");
        metadataHeaders.add("City");
        metadataHeaders.add("Province/State");
        metadataHeaders.add("Country/Primary Location");
        metadataHeaders.add("Original Transmission Reference");
        metadataHeaders.add("Folder");
        metadataHeaders.add("Supplemental Folder(s)");
        metadataHeaders.add("Urgency");
        metadataHeaders.add("Keywords");
        metadataHeaders.add("Filename");

    }

}
