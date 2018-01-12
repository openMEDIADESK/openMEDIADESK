package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.TextEdit;
import com.stumpner.mediadesk.core.Config;
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
 * User: franz.stumpner
 * Date: 23.05.2007
 * Time: 14:03:35
 * To change this template use File | Settings | File Templates.
 */
public class TextEditSettingsController extends SimpleFormControllerMd {

    public TextEditSettingsController() {

        //this.setCommandName("simpleuser");
        this.setCommandClass(TextEdit.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;
        this.setValidateOnBinding(true);

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        TextEdit textEdit = new TextEdit();
        String type = httpServletRequest.getRequestURI();
        if (type.indexOf("settext/folder")>0) {
            textEdit.setMessageHeadline("set.text.folder");
            textEdit.setHtml(Config.textFolderLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textFolderLng2); }
        }
        if (type.indexOf("settext/cat")>0) {
            textEdit.setMessageHeadline("set.text.cat");
            textEdit.setHtml(Config.textCatLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textCatLng2); }
        }
        if (type.indexOf("settext/last")>0) {
            textEdit.setMessageHeadline("set.text.last");
            textEdit.setHtml(Config.textLastLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textLastLng2); }
        }

        if (type.indexOf("settext/help")>0) {
            textEdit.setMessageHeadline("set.text.help");
            textEdit.setHtml(Config.textHelpLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textHelpLng2); }
        }
        if (type.indexOf("settext/searchhelp")>0) {
            textEdit.setMessageHeadline("set.text.helpsearch");
            textEdit.setHtml(Config.textHelpSearchLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textHelpSearchLng2); }
        }
        if (type.indexOf("settext/agb")>0) {
            textEdit.setMessageHeadline("set.text.agb");
            textEdit.setHtml(Config.textAgbLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textAgbLng2); }
        }
        if (type.indexOf("settext/privacy")>0) {
            textEdit.setMessageHeadline("set.text.privacy");
            textEdit.setHtml(Config.textPrivacyLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textPrivacyLng2); }
        }
        if (type.indexOf("settext/contact")>0) {
            textEdit.setMessageHeadline("set.text.contact");
            textEdit.setHtml(Config.textContactLng1);
            textEdit.setUseExtraBoolValue(true);
            textEdit.setBooleanValue(Config.useContactForm);
            textEdit.setMessageBoolean("set.text.contact.useform");
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textContactLng2); }
        }
        if (type.indexOf("settext/imprint")>0) {
            textEdit.setMessageHeadline("set.text.imprint");
            textEdit.setHtml(Config.textImprintLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textImprintLng2); }
        }
        if (type.indexOf("settext/faq")>0) {
            textEdit.setMessageHeadline("set.text.faq");
            textEdit.setHtml(Config.textFaqLng1);
            if (type.indexOf("-en")>0) { textEdit.setHtml(Config.textFaqLng2); }
        }

        return textEdit;//super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        this.setContentTemplateFile("admin_textedit.jsp",request);
        return super.showForm(request, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        this.setContentTemplateFile("admin_textedit.jsp",request);

        TextEdit textEdit = (TextEdit)object;
        String type = request.getRequestURI();
        //System.out.println("type: "+type);
        //System.out.println("indexof type: "+type.indexOf("settext/help"));
        if (type.indexOf("settext/folder")>0) {
            if (type.indexOf("-de")>0) { Config.textFolderLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textFolderLng2 = textEdit.getHtml(); }
            else { Config.textFolderLng1 = textEdit.getHtml(); Config.textFolderLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/cat")>0) {
            if (type.indexOf("-de")>0) { Config.textCatLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textCatLng2 = textEdit.getHtml(); }
            else { Config.textCatLng1 = textEdit.getHtml(); Config.textCatLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/last")>0) {
            if (type.indexOf("-de")>0) { Config.textLastLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textLastLng2 = textEdit.getHtml(); }
            else { Config.textLastLng1 = textEdit.getHtml(); Config.textLastLng2 = textEdit.getHtml(); }
        }

        if (type.indexOf("settext/help")>0) {
            if (type.indexOf("-de")>0) { Config.textHelpLng1 = textEdit.getHtml(); System.out.println("HELP-DE: "); }
            else if (type.indexOf("-en")>0) { Config.textHelpLng2 = textEdit.getHtml(); System.out.println("HELP-EN: "); }
            else { Config.textHelpLng1 = textEdit.getHtml(); Config.textHelpLng2 = textEdit.getHtml(); System.out.println("HELP-ALL: "); }
        }
        if (type.indexOf("settext/searchhelp")>0) {
            if (type.indexOf("-de")>0) { Config.textHelpSearchLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textHelpSearchLng2 = textEdit.getHtml(); }
            else { Config.textHelpSearchLng1 = textEdit.getHtml(); Config.textHelpSearchLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/searchhelp")>0) {
            if (type.indexOf("-de")>0) { Config.textHelpSearchLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textHelpSearchLng2 = textEdit.getHtml(); }
            else { Config.textHelpSearchLng1 = textEdit.getHtml(); Config.textHelpSearchLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/agb")>0) {
            if (type.indexOf("-de")>0) { Config.textAgbLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textAgbLng2 = textEdit.getHtml(); }
            else { Config.textAgbLng1 = textEdit.getHtml(); Config.textAgbLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/privacy")>0) {
            if (type.indexOf("-de")>0) { Config.textPrivacyLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textPrivacyLng2 = textEdit.getHtml(); }
            else { Config.textPrivacyLng1 = textEdit.getHtml(); Config.textPrivacyLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/contact")>0) {
            Config.useContactForm = textEdit.isBooleanValue();
            if (type.indexOf("-de")>0) { Config.textContactLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textContactLng2 = textEdit.getHtml(); }
            else { Config.textContactLng1 = textEdit.getHtml(); Config.textContactLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/imprint")>0) {
            if (type.indexOf("-de")>0) { Config.textImprintLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textImprintLng2 = textEdit.getHtml(); }
            else { Config.textImprintLng1 = textEdit.getHtml(); Config.textImprintLng2 = textEdit.getHtml(); }
        }
        if (type.indexOf("settext/faq")>0) {
            if (type.indexOf("-de")>0) { Config.textFaqLng1 = textEdit.getHtml(); }
            else if (type.indexOf("-en")>0) { Config.textFaqLng2 = textEdit.getHtml(); }
            else { Config.textFaqLng1 = textEdit.getHtml(); Config.textFaqLng2 = textEdit.getHtml(); }
        }

        Config.saveConfiguration();

        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("../settext"));
        return null;
        //return super.onSubmit(request, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBind(HttpServletRequest request, Object object) throws Exception {
        super.onBind(request, object);
        if (request.getParameter("booleanValue")==null) {
            TextEdit textEdit = (TextEdit)object;
            textEdit.setBooleanValue(false);
        }
    }

}
