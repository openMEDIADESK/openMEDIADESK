package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.LngResolver;

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
 * Date: 25.04.2005
 * Time: 20:22:46
 * To change this template use File | Settings | File Templates.
 */
public class FolderListController extends AbstractPageController {

    public FolderListController() {
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        FolderService folderService = new FolderService();
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        model.put("folderList",folderService.getFolderList(1000));

        this.setContentTemplateFile("admin_folderlist.jsp",httpServletRequest);
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);
    }
}
