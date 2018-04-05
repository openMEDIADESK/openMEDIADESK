package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.pinpics.Pin;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;

import java.util.*;

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
 * Date: 25.11.2005
 * Time: 16:57:07
 * To change this template use File | Settings | File Templates.
 */
public class PinListController extends AbstractPageController {

    public PinListController() {
        this.permitOnlyLoggedIn = true;
        this.permitMinimumRole = User.ROLE_PINMAKLER;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setHeader("Cache-Control", "no-cache");
        Config.putDmsConfigToRequest(httpServletRequest);
        if (this.checkPermission(httpServletRequest)) {

            PinpicService folderService = new PinpicService();

            if (httpServletRequest.getParameter("func")!=null) {
                if (httpServletRequest.getParameter("func").equalsIgnoreCase("add")) {
                    GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();

                    Pin pin = new Pin();
                    pin.setMaxUse(999);
                    pin.setCreateDate(new Date());
                    pin.setCreatorUserId(this.getUser(httpServletRequest).getUserId());
                    pin.setEmailnotification(this.getUser(httpServletRequest).getEmail());
                    pin.setDefaultview(Folder.VIEW_UNDEFINED);
                    folderService.add(pin);
                }
            }

            List<Pin> pinList = null;
            User loggedInUser = getUser(httpServletRequest);
            if (loggedInUser.getRole()==User.ROLE_ADMIN) {
                //Admin sieht alle
                pinList = folderService.getPinpicList();
            } else {
                //Andere User sehen nur die eigenen Pins
                pinList = folderService.getPinpicList(loggedInUser);
            }

            httpServletRequest.setAttribute("pinList", pinList);
            //model.put("pinList",pinList);

        } else {
            //Keine Berechtigung
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);
    }
}