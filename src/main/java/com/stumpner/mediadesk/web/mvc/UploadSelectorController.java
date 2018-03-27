package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.LinkedList;
import java.security.acl.AclNotFoundException;

import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;

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
 * Date: 10.04.2015
 * Time: 21:08:08
 * To change this template use File | Settings | File Templates.
 */
public class UploadSelectorController extends AbstractPageController {

    public UploadSelectorController() {
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_USER;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        FolderService folderService = new FolderService();
        List<FolderMultiLang> allowedCategoryList = new LinkedList<FolderMultiLang>();
        List<FolderMultiLang> allCategoryList = folderService.getAllFolderList();
        for (FolderMultiLang c : allCategoryList) {
            if (isUserPermittetForCategory(c, request)) {
                allowedCategoryList.add(c);
            }
        }
        request.setAttribute("allowedCategoryList",allowedCategoryList);
        return super.handleRequestInternal(request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private boolean isUserPermittetForCategory(FolderMultiLang c, HttpServletRequest request) {

        try {
            AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
            return aclCtx.checkPermission(new AclPermission("write"), c);
        } catch (AclNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
