package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.commandclass.settings.WebSettings;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
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
 * Date: 09.12.2005
 * Time: 13:49:20
 * To change this template use File | Settings | File Templates.
 */
public class WebSettingsController extends SimpleFormControllerMd {

    public WebSettingsController() {

        this.setCommandClass(WebSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;


    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WebSettings settings = new WebSettings();
        settings.setTitle(Config.webTitle);
        settings.setCopyright(Config.footerCopyright);
        settings.setCorporateSiteName(Config.footerCorpSite);
        settings.setCorporateSiteLink(Config.footerCorpLink);
        settings.setInstanceLogoURL(Config.instanceLogo);
        settings.setStaticUrlPrefix(Config.staticContentUrlPrefix);
        if (Config.instanceLogo.equalsIgnoreCase("logo.gif")) {
            settings.setShowLogoUrl(false);
        }

        return settings;
        //
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {
        this.setContentTemplateFile("settings_web.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        WebSettings settings = (WebSettings)o;
        Config.webTitle = settings.getTitle();
        Config.footerCopyright = settings.getCopyright();
        Config.footerCorpSite = settings.getCorporateSiteName();
        Config.footerCorpLink = settings.getCorporateSiteLink();
        Config.instanceLogo = settings.getInstanceLogoURL();
        Config.staticContentUrlPrefix = settings.getStaticUrlPrefix();
        Config.saveConfiguration();

        if (httpServletRequest.getParameter("favicon")!=null) {
            //favicon-Aufruf
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("setfavicon")
            );
        } else if (httpServletRequest.getParameter("logo")!=null) {
            //logo-Aufruf
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("setlogo")
            );
        } else if (httpServletRequest.getParameter("logo2")!=null) {
            //favicon-Aufruf
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("setlogo2")
            );
        } else {

            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("settings")
            );
        }
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
        return null;
    }

}
