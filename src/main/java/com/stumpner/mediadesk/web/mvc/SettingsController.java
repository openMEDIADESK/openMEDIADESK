package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

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
 * Time: 12:04:43
 * To change this template use File | Settings | File Templates.
 */
public class SettingsController extends AbstractPageController {

    public SettingsController() {
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Config.putDmsConfigToRequest(httpServletRequest);

        boolean todoWebtitle = false; //Web Titel ist noch nicht konfiguriert
        boolean todoLogo = false; //Logo ist noch nicht konfiguriert
        boolean todoEmail = false; //Empf�nger Emailadresse noch nicht konfiguriert

        if (Config.webTitle.equalsIgnoreCase("mediaDESK (R)")) {
            todoWebtitle = true;
            System.out.println("todo Webtitle");
        }

        String icoFilename = Config.imageStorePath+ File.separator + "logo2.png";
        File icoFile = new File(icoFilename);
        if (!icoFile.exists()) {
            System.out.println("todo Logo "+icoFile.getAbsolutePath());
            todoLogo = true;
        }
        if (Config.mailReceiverAdminEmail.equalsIgnoreCase("office@openmediadesk.org") || Config.mailReceiverAdminEmail.equalsIgnoreCase("")) {
            todoEmail = true;
        }


        httpServletRequest.setAttribute("todoWebtitle",todoWebtitle);
        httpServletRequest.setAttribute("todoLogo",todoLogo);
        httpServletRequest.setAttribute("todoEmail",todoEmail);

        this.setContentTemplateFile("admin_settings.jsp",httpServletRequest);
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
