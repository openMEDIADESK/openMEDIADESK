package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Iterator;
import java.util.HashMap;

import com.stumpner.mediadesk.media.Format;
import net.stumpner.security.acl.AccessObject;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.exceptions.PermissionAlreadyExistException;

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
 * Date: 09.09.2008
 * Time: 20:54:37
 * To change this template use File | Settings | File Templates.
 */
public class FormatListController extends AbstractPageController {

    public FormatListController() {

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (httpServletRequest.getParameter("delindex")!=null) {
            int delindex = Integer.parseInt(httpServletRequest.getParameter("delindex"));
            if (delindex==0) { throw new Exception("Original Format not deleteable"); }
            Format format = (Format)Config.downloadRes.get(delindex);
            Config.downloadRes.remove(delindex);
            Config.saveConfiguration();
            removeAccessFromAllGroups(format);
        }
        if (httpServletRequest.getParameter("add")!=null) {
            int width = Integer.parseInt(httpServletRequest.getParameter("width"));
            int height = Integer.parseInt(httpServletRequest.getParameter("height"));
            if (validateNewFormat(width,height)) {
                Format format = new Format(width, height);
                Config.downloadRes.add(format);
                Config.saveConfiguration();
                addAccessToAllGroups(format);
            } else {
                httpServletRequest.setAttribute("alreadyExists",true);
            }
        }

        List formatList = Config.downloadRes;
        httpServletRequest.setAttribute("formatList",formatList);

        this.setContentTemplateFile("admin_formatlist.jsp",httpServletRequest);
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Überprüft ob das neue Format gültig ist (ob es bereits existiert, ob die Werte ungültig sind,...
     * @param width
     * @param height
     * @return
     */
    private boolean validateNewFormat(int width, int height) {
        if (formatExists(width,height)) {
            return false;
        }
        if (width<0 || height<0) {
            return false;
        }
        if (width>10000 || height>10000) {
            return false;
        }

        return true;  //To change body of created methods use File | Settings | File Templates.
    }

    private boolean formatExists(int width, int height) {
        Iterator formats = Config.downloadRes.iterator();
        while (formats.hasNext()) {
            Format format = (Format)formats.next();
            if (format.getWidth()==width && format.getHeight()==height) {
                return true;
            }
        }
        return false;
    }

    public static void addAccessToAllGroups(AccessObject accessObject) {

        //Alle ACLs loeschen:
        Acl acl = new Acl();
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            try {
                acl.addPermission(sg,new AclPermission(AclPermission.READ));
            } catch (PermissionAlreadyExistException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        AclController.setAcl(accessObject,acl);

    }

    static void removeAccessFromAllGroups(AccessObject accessObject) {

        //Alle ACLs loeschen:
        HashMap securityMap = new HashMap(3);
        Acl acl = AclController.getAcl(accessObject);
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            if (acl.checkPermission(sg,new AclPermission(AclPermission.READ))) {
                acl.removePermission(sg, new AclPermission(AclPermission.READ));
            }
        }

        AclController.setAcl(accessObject,acl);

    }
}

