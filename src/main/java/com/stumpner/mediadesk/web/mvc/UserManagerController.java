package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.usermanagement.UserAtom;

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
 * Date: 18.04.2005
 * Time: 20:46:15
 * To change this template use File | Settings | File Templates.
 */
public class UserManagerController extends AbstractPageController {

    public final static String TAB_USER = "0";
    public final static String TAB_GROUP = "1";

    public UserManagerController() {
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        UserService userService = new UserService();
        if (userService.processAutologin(httpServletRequest)) {
            System.out.println("autologin processed");
        }

        User loggedInUser = getUser(httpServletRequest);
        httpServletRequest.setAttribute("tabView",TAB_USER);
        if (httpServletRequest.getParameter("filter")!=null) {

            List allUserList = getUserList(loggedInUser, httpServletRequest);
            List filteredUserList = new LinkedList();

            Iterator allUsers = allUserList.iterator();
            while (allUsers.hasNext()) {
                UserAtom user = (UserAtom)allUsers.next();

                if (httpServletRequest.getParameter("filter").equalsIgnoreCase("enabled")) {
                    if (user.isEnabled()) { filteredUserList.add(user); }
                }

                if (httpServletRequest.getParameter("filter").equalsIgnoreCase("disabled")) {
                    if (!user.isEnabled()) { filteredUserList.add(user); }
                }
            }
            httpServletRequest.setAttribute("userAtomList",filteredUserList);

        } else {
            httpServletRequest.setAttribute("userAtomList",getUserList(loggedInUser, httpServletRequest));
        }

        if (httpServletRequest.getParameter("addgroup")!=null) {
            SecurityGroup group = new SecurityGroup();
            group.setName(httpServletRequest.getParameter("addgroup"));
            userService.addSecurityGroup(group);
            httpServletRequest.setAttribute("tabView",TAB_GROUP);
        }
        if (httpServletRequest.getParameter("delgroup")!=null) {
            int groupId = Integer.parseInt(httpServletRequest.getParameter("delgroup"));
            userService.deleteSecurityGroup(groupId);
            httpServletRequest.setAttribute("tabView",TAB_GROUP);
            //todo: check ob noch berechtigungen auf diese Gruppe laufen...
        }
        if (httpServletRequest.getParameter("editacl")!=null) {
            int groupId = Integer.parseInt(httpServletRequest.getParameter("editacl"));
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("folderselector?type=acl&redirectTo=usermanager&id="+groupId));
            return null;
        }

        //Tabview
        if (httpServletRequest.getParameter("tab")!=null) {
            if (httpServletRequest.getParameter("tab").equalsIgnoreCase("group")) {
                httpServletRequest.setAttribute("tabView",TAB_GROUP);
            }
        }

        List securitygroupList = userService.getSecurityGroupList();
        httpServletRequest.setAttribute("securitygroupList",securitygroupList);
        httpServletRequest.setAttribute("securityGroupResolver", new SecurityGroupResolver(securitygroupList));

        if (loggedInUser.getRole()==User.ROLE_ADMIN) {
            httpServletRequest.setAttribute("showSecurityGroupList",true);
        } else {
            //F�r Mandanten die Security Groups ausblenden
            httpServletRequest.setAttribute("showSecurityGroupList",false);
        }

        if (Config.wsUsersyncEnabled) {
            httpServletRequest.setAttribute("canSetPassword",false);
        } else {
            httpServletRequest.setAttribute("canSetPassword",true);
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public class SecurityGroupResolver {

        List<SecurityGroup> list = null;

        public SecurityGroupResolver(List<SecurityGroup> list) {
            this.list = list;
        }

        public SecurityGroup get(int id) {

            for (SecurityGroup s : list) {
                if (s.getId()==id) { return s; }
            }

            return null;
        }
    }

    /**
     * Gibt die Userliste zur�ck:
     * + Wenn aufruf von einem Admin = ALLE USER
     * + Wenn aufruf von einem Mandant dann nur Benutzer innerhalb diesen mandants
     * @param loggedInUser
     * @return
     */
    private List getUserList(User loggedInUser, HttpServletRequest request) {

        UserService userService = new UserService();
        List allUserList = null;
            if (loggedInUser.getRole()==User.ROLE_ADMIN) {
                if (request.getParameter("mandant")!=null) {
                    //Filter auf Mandant
                    try {
                        User mandantUser = (User)userService.getById(Integer.parseInt(request.getParameter("mandant")));
                        allUserList = userService.getUserList(mandantUser);
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOServiceException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    allUserList = userService.getUserList();
                }
            } else {
                //Bei anderen = Mandanten-Benutzer nur die Benutzer des selben Mandanten auflisten
                allUserList = userService.getUserList(loggedInUser);
            }
        return allUserList;

    }

    /**
     * HOME-EDITOR/Mandanten-Benutzer erlauben die Benutzerverwaltung (aber nur von Ihren Benutzern) aufzurufen
     * @param request
     * @return
     */
    protected boolean checkPermission(HttpServletRequest request) {

        boolean permitted = super.checkPermission(request);
        if (!permitted) {
            User user = getUser(request);
            if (user.getRole()==User.ROLE_HOME_EDITOR) { permitted = true; }
        }
        return permitted;
    }
}
