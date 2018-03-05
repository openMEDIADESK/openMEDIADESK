package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.Folder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.pinpics.Pinpic;
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

                    Pinpic pinpic = new Pinpic();
                    pinpic.setMaxUse(999);
                    pinpic.setCreateDate(new Date());
                    pinpic.setCreatorUserId(this.getUser(httpServletRequest).getUserId());
                    pinpic.setEmailnotification(this.getUser(httpServletRequest).getEmail());
                    pinpic.setDefaultview(Folder.VIEW_UNDEFINED);
                    folderService.add(pinpic);
                }
            }

            List<Pinpic> pinpicList = null;
            User loggedInUser = getUser(httpServletRequest);
            if (loggedInUser.getRole()==User.ROLE_ADMIN) {
                //Admin sieht alle
                pinpicList = folderService.getPinpicList();
            } else {
                //Andere User sehen nur die eigenen Pins
                pinpicList = folderService.getPinpicList(loggedInUser);
            }

            httpServletRequest.setAttribute("pinList",pinpicList);
            //model.put("pinList",pinpicList);

        } else {
            //Keine Berechtigung
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);
    }
}