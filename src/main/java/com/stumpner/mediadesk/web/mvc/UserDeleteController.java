package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.log4j.Logger;
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
 * Time: 22:05:19
 * To change this template use File | Settings | File Templates.
 */
public class UserDeleteController extends SimpleFormControllerMd {

    public UserDeleteController() {

        this.setCommandClass(User.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        UserService userService = new UserService();
        int userId = Integer.parseInt(httpServletRequest.getParameter("userid"));
        User user = (User)userService.getById(userId);

        return user;
    }



    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        User user = (User)e.getTarget();
        if (user.getRole()>=User.ROLE_ADMIN) {
            //meldung ausgeben dass der admin nicht gelöscht werden kann
            //user löschen formular/messagebox anzeigen

            httpServletRequest.setAttribute("headline","userdelete.headline");
            httpServletRequest.setAttribute("subheadline","userdelete.subheadline");
            httpServletRequest.setAttribute("info","userdelete.text");
            httpServletRequest.setAttribute("text","userdelete.noadmin");
            httpServletRequest.setAttribute("nextUrl","usermanager");
            httpServletRequest.setAttribute("redirectTo","");

            return new ModelAndView("/WEB-INF/template/current/pMessage.jsp");
            
        } else {
            //user löschen formular/messagebox anzeigen
            this.setContentTemplateFile("/message_yes_no.jsp",httpServletRequest);

            httpServletRequest.setAttribute("headline","userdelete.headline");
            httpServletRequest.setAttribute("subheadline","userdelete.subheadline");
            httpServletRequest.setAttribute("info","userdelete.text");
            httpServletRequest.setAttribute("redirectTo","");

            if (user.getHomeCategoryId()!=-1) {

                httpServletRequest.setAttribute("useCbx",true);
                httpServletRequest.setAttribute("cbxChecked",true);
                httpServletRequest.setAttribute("cbxText","useredit.deletecat");
            } else {
                httpServletRequest.setAttribute("useCbx",false);
                httpServletRequest.setAttribute("cbxChecked",true);
                httpServletRequest.setAttribute("cbxText","useredit.deletecat");
            }
        }
        return super.showForm(httpServletRequest,e,this.getFormView(),model);
        //return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        User user = (User)o;
        if (httpServletRequest.getParameter("yes")!=null) { //Prüfen ob bei der Abfrage auf JA geklickt wurde
            //Benutzerkategorie löschen
            if (httpServletRequest.getParameter("cbx")!=null) {
                FolderService folderService = new FolderService();
                folderService.deleteById(user.getHomeCategoryId());
            }
            this.deleteUser(user);
        }

        this.setContentTemplateFile("/message.jsp",httpServletRequest);
        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("usermanager"));
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void deleteUser(User user) {

        Logger logger = Logger.getLogger(UserDeleteController.class);
        UserService userService = new UserService();
        logger.info("Delete UserId: "+user.getUserId());
        try {
            userService.deleteById(user.getUserId());
        } catch (IOServiceException e) {
            e.printStackTrace();
        }
    }

}
