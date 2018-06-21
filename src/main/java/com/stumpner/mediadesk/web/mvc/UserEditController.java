package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.folder.Folder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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
 * Date: 19.04.2005
 * Time: 20:58:11
 * To change this template use File | Settings | File Templates.
 */
public class UserEditController extends SimpleFormControllerMd {

    public static int USERID_NEW=-1;

    public UserEditController() {
        this.setCommandClass(User.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        this.setValidator(new UserEditValidator());
        this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        UserService userService = new UserService();
        User user = new User();
        User loggedInUser = getUser(httpServletRequest);

        if (httpServletRequest.getParameter("userid")!=null) {
            //user editieren
            int userId = Integer.parseInt(httpServletRequest.getParameter("userid"));
            user = (User)userService.getById(userId);
            if (loggedInUser.getRole()!=User.ROLE_ADMIN) {
                //Pr�fen ob es auch wirklich der Benutzer eines Mandanten ist!
                if (loggedInUser.getUserId()!=user.getMandant()) {
                    throw new Exception("Benutzer "+user.getUserName()+" ist nicht innerhalb des Mandanten "+loggedInUser.getUserName());
                }
            }
        } else {
            //user erstellen
            user.setUserId(USERID_NEW);
            user.setSecurityGroup(Config.defaultSecurityGroup);
            if (loggedInUser.getRole()!=User.ROLE_ADMIN) {
                //Mandantenbenutzer:
                user.setHomeCategoryId(loggedInUser.getHomeCategoryId());
                user.setMandant(loggedInUser.getUserId());
                user.setSecurityGroup(loggedInUser.getSecurityGroup());
                user.setRole(User.ROLE_USER); //Mandantenbenutzer erhalten nur Benutzer-Rolle
            } else {
                //Wird von einem Administrator ein Benutzer angelegt, wird die Vorgabe-Rolle ausgef�llt
                user.setRole(Config.defaultRole);
            }
        }

        return user;
    }

    protected Map referenceData(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {

        User loggedInuser = getUser(httpServletRequest);
        User user = (User)o;
        UserService userService = new UserService();
        httpServletRequest.setAttribute("securityGroupList",userService.getRealSecurityGroupList());

        if (Config.homeFolderId !=-1) {
            //Mandanten sind aktiviert
                httpServletRequest.setAttribute("showHomeCategoryCreator",true);
                if (user.getUserId()==USERID_NEW) {
                    httpServletRequest.setAttribute("homeCategoryChecked",true);
                }
        } else {
            httpServletRequest.setAttribute("showHomeCategoryCreator",false);
        }

        //F�r Mandanten und Admins verschiedene Bereiche ein/ausblenden
        if (loggedInuser.getRole()==User.ROLE_ADMIN) {
            httpServletRequest.setAttribute("showRole",true);
            httpServletRequest.setAttribute("showSecurityGroup",true);
        } else {

            httpServletRequest.setAttribute("showRole",false);
            httpServletRequest.setAttribute("showSecurityGroup",false);
            httpServletRequest.setAttribute("showHomeCategoryCreator",false);
        }

        return super.referenceData(httpServletRequest, o, errors);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (!isUserPermitted(request)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        this.setContentTemplateFile("/admin_useredit.jsp",request);

        request.setAttribute("userEmailAsUsername",Config.userEmailAsUsername);
        request.setAttribute("showUserCompanyFields",Config.showUserCompanyFields);
        request.setAttribute("showUserAddressFields",Config.showUserAddressFields);
        request.setAttribute("showUserTelFaxFields",Config.showUserTelFaxFields);

        UserService userService = new UserService();
        request.setAttribute("securityGroupList",userService.getRealSecurityGroupList());

        if (Config.wsUsersyncEnabled) {
            request.setAttribute("canSetPassword",false);
        } else {
            request.setAttribute("canSetPassword",true);
        }

        return super.showForm(request, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        User user = (User)o;
        if (Config.userEmailAsUsername) {
            if (!user.getUserName().equalsIgnoreCase("admin")) {
                user.setUserName(user.getEmail());
            }
        }
        if (Config.resetSecurityGroupWhenUserIsDisabled) {
            //Berechtigungsgruppe zur�cksetzen, wenn der Benutzer deaktiviert wird
            if (user.getUserId()!=USERID_NEW) {
                UserService userService = new UserService();
                User userBeforeEdit = (User)userService.getById(user.getUserId()); //Benutzerdaten vor der bearbeitung
                if (userBeforeEdit.isEnabled() && !user.isEnabled()) {
                    //Wenn der Benutzer deaktiviert wurde: Berechtigungsgruppe zur�cksetzen
                    user.setSecurityGroup(Config.defaultSecurityGroup);
                }
            }
        }

        //delete homedir
        if (httpServletRequest.getParameter("deleteHomeCat")!=null) {
            FolderService folderService = new FolderService();

            try {
                Folder folder = folderService.getFolderById(user.getHomeCategoryId());
                folder.setName(folder.getName()+".bak."+(new Date().getDay()));
                folderService.save(folder);
            } catch (IOServiceException e1) {
                e.printStackTrace();
            } catch (ObjectNotFoundException e2) {
                e.printStackTrace();
            }
            user.setHomeCategoryId(-1);
            this.saveUser(user);
        }

        this.saveUser(user);

        //check password resend
        if (httpServletRequest.getParameter("resetpwd")!=null) {
            Authenticator auth = new Authenticator();
            try {
                auth.renewPassword(((User)o).getUserName());
            } catch (MessagingException err) {
                err.printStackTrace();
                logger.error("Password for User "+((User)o).getUserName()+" can not be sent");
                e.reject("register.error.mailerror","Error sending Mail");
            }
        }

        //check createhomedir
        if (httpServletRequest.getParameter("createHomeCat")!=null) {
            UserService userService = new UserService();
            userService.createHomeCategory(user);
        }

        httpServletRequest.setAttribute("redirectTo","usermanager");
        httpServletRequest.setAttribute("headline","message.useredit.headline");
        httpServletRequest.setAttribute("info","message.useredit.info");
        this.setContentTemplateFile("/message_useredit.jsp",httpServletRequest);
        return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }



    private void saveUser(User user) {

        UserService userService = new UserService();
        try {
            if (user.getUserId()==USERID_NEW) {
                userService.add(user);
            } else {
                userService.save(user);
            }
        } catch (IOServiceException e) {
            e.printStackTrace();
        }

    }

    /**
     * HOME-EDITOR/Mandanten-Benutzer erlauben die Benutzerverwaltung (aber nur von Ihren Benutzern) aufzurufen
     * @param
     * @return
     */
    protected boolean isUserPermitted(HttpServletRequest request) {

        boolean permitted = super.isUserPermitted(request);
        if (!permitted) {
            User user = getUser(request);
            if (user.getRole()==User.ROLE_HOME_EDITOR) { permitted = true; }
        }
        return permitted;
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        super.initBinder(httpServletRequest, servletRequestDataBinder);
        servletRequestDataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, new DecimalFormat("0.00"),false));
    }
}
