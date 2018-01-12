package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.UserService;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AccessObject;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Iterator;

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
 * Date: 16.09.2008
 * Time: 19:59:32
 * To change this template use File | Settings | File Templates.
 */
public class FormatEditController extends SimpleFormControllerMd {

    public FormatEditController() {

        this.setCommandClass(Acl.class);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        //WebStack webStack = new WebStack(httpServletRequest);
        //webStack.push();

        int index = Integer.parseInt(httpServletRequest.getParameter("index"));
        Acl acl = AclController.getAcl(
                (AccessObject)Config.downloadRes.get(index)
        );

        httpServletRequest.getSession().setAttribute("accessObject",Config.downloadRes.get(index));

        return acl;
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        //SecurityGroups in das Request Objekt stellen:
        HashMap securityMap = new HashMap(3);
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            securityMap.put(new Integer(sg.getId()),sg);
        }
        httpServletRequest.setAttribute("securityMap",securityMap);
        httpServletRequest.setAttribute("aclEnabled", AclController.isEnabled());

        this.setContentTemplateFile("/admin_formatedit.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        Acl acl = (Acl)object;

        //Alle ACLs loeschen:
        HashMap securityMap = new HashMap(3);
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            acl.removePermission(sg,new AclPermission(AclPermission.READ));
            securityMap.put(new Integer(sg.getId()),sg);
        }

        String acls[] = httpServletRequest.getParameterValues("acl");
        if (acls!=null) {
            for (int p=0;p<acls.length;p++) {
                SecurityGroup sg = (SecurityGroup)securityMap.get(new Integer(acls[p]));
                acl.addPermission(sg,new AclPermission(AclPermission.READ));
            }
        }

        AccessObject accessObject = (AccessObject)httpServletRequest.getSession().getAttribute("accessObject");
        AclController.setAcl(accessObject,acl);
        httpServletRequest.getSession().removeAttribute("accessObject");

        //WebStack webStack = new WebStack(httpServletRequest);
        //httpServletResponse.sendRedirect(webStack.pop());

        //httpServletResponse.encodeRedirectURL("setformat");
        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("setformat")
        );

        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
