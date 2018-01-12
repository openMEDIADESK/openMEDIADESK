package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.core.database.sc.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
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
 * User: franz.stumpner
 * Date: 12.01.2007
 * Time: 16:35:20
 * To change this template use File | Settings | File Templates.
 */
public class PasswordEditController extends SimpleFormControllerMd {

    public PasswordEditController() {
        this.setCommandClass(User.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_USER;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }
        this.setContentTemplateFile("admin_password.jsp",httpServletRequest);

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        WebStack webStack = new WebStack(request);
        webStack.push();

        UserService userService = new UserService();
        User user = new User();

        //userid aus querystring
        if (request.getParameter("userid")!=null) {
            int userId = Integer.parseInt(request.getParameter("userid"));
            if (this.getUser(request).getRole()==User.ROLE_ADMIN) {
                //Beim admin jeden user zurückgeben
                user = (User)userService.getById(userId);
            } else {
                if (getUser(request).getRole()==User.ROLE_HOME_EDITOR) {
                    User reqUser = (User)userService.getById(userId);
                    if (reqUser.getMandant()==getUser(request).getUserId()) {
                        //Wenn der Benutzer vom richtigen Mandanten kommt
                        user = reqUser;
                    } else {
                        throw new Exception("Userid "+reqUser.getUserId()+" ist nicht im Mandant "+getUser(request).getUserId());
                    }
                } else {
                    //Bei anderen usern nur den eigentlichen user
                    user = (User)userService.getById(this.getUser(request).getUserId());
                }
            }
        } else {
            //eigenes passwort ändern, eigenes userobject zurückgeben:
            user = (User)userService.getById(this.getUser(request).getUserId());
            //return super.formBackingObject(request);    //To change body of overridden methods use File | Settings | File Templates.
        }

        return user;
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object object, BindException bindException) throws Exception {

        String newPassword = httpServletRequest.getParameter("newPassword");
        String repeatPassword = httpServletRequest.getParameter("repeatPassword");

        if (!newPassword.contentEquals(repeatPassword)) {
            bindException.reject("password.notequal","Not Equal");
        } else {
            if (newPassword.equalsIgnoreCase("")) {
                //leeres Passwort
                bindException.reject("password.empty","Empty");
            } else {
                //speichern:
                User user = (User)object;
                Authenticator auth = new Authenticator();
                auth.setPassword(user.getUserName(),newPassword);
            }
        }

        //super.onBindAndValidate(httpServletRequest, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {


        /*
        WebStack webStack = new WebStack(httpServletRequest);
        httpServletResponse.sendRedirect(webStack.pop());
        */
        User user = getUser(httpServletRequest);
        if (user.getRole()== User.ROLE_ADMIN) {
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("usermanager"));
            return null;
        } else {
            //httpServletRequest.setAttribute("redirectTo","javascript:window.history.go(-2);");
            httpServletRequest.setAttribute("redirectTo", httpServletRequest.getAttribute("home"));
            httpServletRequest.setAttribute("headline","password.headline");
            httpServletRequest.setAttribute("info","login.succeed");
            return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);
        }

        
        //return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
