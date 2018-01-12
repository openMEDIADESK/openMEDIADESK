package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.PluginSettings;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.commons.lang3.StringUtils;

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
 * User: stumpner
 * Date: 18.11.2015
 * Time: 08:48:05
 */
public class PluginSettingsController extends SimpleFormControllerMd {

    public PluginSettingsController() {

        this.setCommandClass(PluginSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        PluginSettings p = new PluginSettings();
        p.setImportPluginClass(StringUtils.join(Config.importPluginClass, "\n"));

        if (request.getParameter("saved")!=null) {
            p.setMessage("Gespeichert");
        }
        return p;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        PluginSettings p = (PluginSettings)o;

        System.out.println("PluginSettings: "+p.getImportPluginClass());
        Config.importPluginClass = p.getImportPluginClass().split("\n");
        
        Config.saveConfiguration();
        Config.initPlugins();

        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("setplugin?saved")
        );
        
        return null;
    }

}
