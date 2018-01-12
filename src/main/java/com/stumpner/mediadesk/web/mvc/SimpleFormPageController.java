package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

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
 * Date: 29.03.2005
 * Time: 22:22:43
 * To change this template use File | Settings | File Templates.
 * @deprecated Use SimpleFormControllerMd
 */
public class SimpleFormPageController extends SimpleFormController implements SuSIDEBaseController {

    protected Map model = new HashMap();
    //permits only logged in members to view this page /controller,
    //or a minimum of role
    protected boolean permitOnlyLoggedIn = false;
    protected int permitMinimumRole = 0;

    public SimpleFormPageController() {

    }

    public void denyByAcl(HttpServletResponse response) {
        try {
            response.sendError(403,"Denied By ACL");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        httpServletRequest.setCharacterEncoding("UTF-8");
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

/*
    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e, Map map) throws Exception {
        return new ModelAndView("/WEB-INF/templates/index.jsp");
        //return super.showForm(httpServletRequest, httpServletResponse, e, map);    //To change body of overridden methods use File | Settings | File Templates.
    }
    */

    /*
    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        //ModelAndView mav = super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
        //mav.getModel().putAll(model);
    }
    */
    /*
    protected void setContentTemplateFile(String jspFile, HttpServletRequest httpServletRequest) {

        this.setFormView("/WEB-INF/templates/index.jsp");
        //httpServletRequest.getSession().setAttribute("contentTemplateFile",jspFile);
        httpServletRequest.setAttribute("contentTemplateFile",jspFile);
    }
    */

    /**
     * @deprecated Mit neuen Template system werden die Views nicht über index.jsp aufgerufen
     * @param jspFile
     * @param httpServletRequest
     */
    public void setContentTemplateFile(String jspFile, HttpServletRequest httpServletRequest) {

    }

    protected boolean isUserPermitted(HttpServletRequest httpServletRequest) {

        if (permitOnlyLoggedIn==true) {
            if (isLoggedIn(httpServletRequest)) {
                if (hasMinimumRole(httpServletRequest,permitMinimumRole)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WebStack webStack = new WebStack(httpServletRequest);
        webStack.register();

        return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Checks if a user is logged in
     * @param httpServletRequest
     * @return
     */
    public boolean isLoggedIn(HttpServletRequest httpServletRequest) {

        if (httpServletRequest.getSession().getAttribute("user")!=null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUser(HttpServletRequest httpServletRequest) {

        return WebHelper.getUser(httpServletRequest);

    }

    private boolean hasMinimumRole(HttpServletRequest httpServletRequest,int minimumRole) {

        if (httpServletRequest.getSession().getAttribute("user")!=null) {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            if (user.getRole()>=minimumRole) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
