package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.web.mvc.sites.MessageSite;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;

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
 * Date: 18.01.2007
 * Time: 19:06:41
 * To change this template use File | Settings | File Templates.
 */
public class LoginForgotController extends SimpleFormControllerMd {

    private static final String ATTRIBUTE_LOADED_USER = "loadedUser";

    public LoginForgotController() {

        this.setCommandClass(User.class);

    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        this.setContentTemplateFile("loginforgot.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object object, BindException bindException) throws Exception {

        User user = (User)object;
        UserService userService = new UserService();
        User loadedUser = null;

        try {
            loadedUser = (User) userService.getByName(user.getUserName());
            user = loadedUser;
            object = user;

            if (!user.isEnabled()) {
                bindException.reject("loginforgot.error.notenabled");
                logger.debug("LoginForgotController: Benutzer gesperrt ["+user.getUserName()+"]");
            }

        } catch (ObjectNotFoundException e) {

            try {
                loadedUser = (User) userService.getByEmail(user.getUserName());
                user = loadedUser;
                object = user;

                if (!user.isEnabled()) {
                    bindException.reject("loginforgot.error.notenabled");
                    logger.debug("LoginForgotController: Benutzer gesperrt ["+user.getUserName()+"]");
                }
                
            } catch (ObjectNotFoundException ex) {
                //User nicht gefunden
                logger.debug("LoginForgotController: Benutzer nicht gefunden");
                bindException.reject("loginforgot.error.notfound");
            }
        }

        httpServletRequest.setAttribute(ATTRIBUTE_LOADED_USER,loadedUser);

        super.onBindAndValidate(httpServletRequest, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        //this.setContentTemplateFile("message.jsp",httpServletRequest);

        //User user = (User)object;
        User user = (User)httpServletRequest.getAttribute(ATTRIBUTE_LOADED_USER);
        Authenticator auth = new Authenticator();


        MessageSite messageSite = new MessageSite(this,model,httpServletRequest);
        messageSite.setHeadline("loginforgot.headline");
        messageSite.setSubHeadline("loginforgot.subheadline");
        messageSite.setText("loginforgot.success");
        messageSite.setNextUrl("/login");

        /*
        httpServletRequest.setAttribute("headline","loginforgot.headline");
        httpServletRequest.setAttribute("subheadline","loginforgot.subheadline");
        httpServletRequest.setAttribute("text","loginforgot.success");
        httpServletRequest.setAttribute("nextUrl","/login");
        httpServletRequest.setAttribute("htmlCode","");
        httpServletRequest.setAttribute("subheadlineArgs",new String[] {""});
        */

            httpServletRequest.setAttribute("redirectTo","login");
        httpServletRequest.setAttribute("subheadline","loginforgot.subheadline");
            httpServletRequest.setAttribute("headline","loginforgot.headline");
            httpServletRequest.setAttribute("info","loginforgot.success");

        try {
            auth.renewPassword(user.getUserName());
        } catch (MessagingException e) {
            httpServletRequest.setAttribute("text","register.error.mailerror");
        }

        //httpServletRequest.setAttribute("model",model);

        return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }


}
