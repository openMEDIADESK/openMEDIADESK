package com.stumpner.mediadesk.web.mvc.common;

import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.mvc.SuSIDEBaseController;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

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
 * Date: 15.04.2010
 * Time: 22:43:42
 * To change this template use File | Settings | File Templates.
 */
public class SimpleFormControllerMd extends SimpleFormController implements SuSIDEBaseController {


    protected Map model = new HashMap();
    //permits only logged in members to view this page /controller,
    //or a minimum of role
    protected boolean permitOnlyLoggedIn = false;
    protected int permitMinimumRole = 0;

    public SimpleFormControllerMd() {
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        httpServletRequest.setCharacterEncoding("UTF-8");
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * @deprecated Mit neuen Template system werden die Views nicht über index.jsp aufgerufen
     * @param jspFile
     * @param httpServletRequest
     */
    public void setContentTemplateFile(String jspFile, HttpServletRequest httpServletRequest) {

        GlobalRequestDataProvider.writeToRequest(httpServletRequest,this);

        if (isUserPermitted(httpServletRequest)) {
            httpServletRequest.setAttribute("contentTemplateFile",jspFile);
        } else {
            httpServletRequest.setAttribute("contentTemplateFile","notallowed.jsp");
        }
    }

    final protected ModelAndView showFormInternal(HttpServletRequest request, HttpServletResponse response, BindException e) throws Exception {
        return null;
    }

    final protected ModelAndView onSubmitInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    protected Map referenceData(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {

        GlobalRequestDataProvider.writeToRequest(httpServletRequest,this);

        return super.referenceData(httpServletRequest, o, errors);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        onSubmitReferenceData(httpServletRequest, o,e);
        String redirectToUrl = onSubmitRedirectToUrl(httpServletRequest, o,e);
        if (redirectToUrl!=null) {
            String encodedRedirectUrl = WebHelper.getServerNameUrl(httpServletRequest)+httpServletResponse.encodeURL(redirectToUrl);
            //TOSystem.out.println("RedirectURL: "+encodedRedirectUrl);
            String url = WebHelper.getRedirectUrl(httpServletRequest, httpServletResponse, redirectToUrl);
            //System.out.println("redirectURL "+url);
            //httpServletResponse.sendRedirect(url);
            httpServletResponse.sendRedirect(httpServletResponse.encodeURL(redirectToUrl));
            return null;
        } else {
            return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    /**
     * Hier können benötigte Daten für den SuccessView in die Map oder Request gespeichert werden.
     * Achtung: Map speicherung funktioniert dzt noch nicht!
     * @param httpServletRequest
     * @param o
     * @param errors
     * @return
     * @throws Exception
     */
    protected Map onSubmitReferenceData(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {

        GlobalRequestDataProvider.writeToRequest(httpServletRequest,this);
        return null;
    }

    /**
     * Mit dieser Implementierung kann statt dem SucessView auf eine andere Seite umgeleitet werden
     * @param httpServletRequest
     * @param o
     * @param errors
     * @return Url auf die umgeleitet werden soll, NULL wenn die SuccessView angezeigt werden soll
     * @throws Exception
     */
    protected String onSubmitRedirectToUrl(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {
        return null;
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

    public void denyByAcl(HttpServletResponse response) {
        try {
            response.sendError(403,"Denied By ACL");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
