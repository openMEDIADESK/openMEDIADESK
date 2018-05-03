package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.commandclass.Contact;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.media.image.util.CustomTextService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import com.stumpner.mediadesk.web.mvc.common.GlobalRequestDataProvider;
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
 * Date: 07.09.2007
 * Time: 11:17:58
 * To change this template use File | Settings | File Templates.
 */
public class ContactFormController extends SimpleFormControllerMd {

    public ContactFormController() {
        this.setSessionForm(true);
        this.setCommandClass(Contact.class);
        //this.setFormView("/WEB-INF/template/"+ Config.templatePath+"contactform.jsp");
        //this.setSuccessView("/WEB-INF/template/"+ Config.templatePath+"contactform_success.jsp");
        this.setValidator(new ContactFormValidator());
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        httpServletRequest.setCharacterEncoding("UTF-8");
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected boolean isFormSubmission(HttpServletRequest httpServletRequest) {

        if (httpServletRequest.getMethod().equalsIgnoreCase("POST")) {
            return true;
        } else {
            return super.isFormSubmission(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        if (request.getParameter("notice")!=null || request.getParameter("email")!=null) {
            Contact contact = new Contact();
            if (request.getParameter("notice")!=null) contact.setNotice(request.getParameter("notice"));
            if (request.getParameter("email")!=null) contact.setEmail(request.getParameter("email"));
            return contact;
        } else {
            return super.formBackingObject(request);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map model = new HashMap();
        request.setAttribute("text", CustomTextService.getCustomText("contact",request));
        GlobalRequestDataProvider.writeToRequest(request,this);
        return model;
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        request.setAttribute("text", CustomTextService.getCustomText("contact",request));
        return super.onSubmit(request, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(Object object) throws Exception {

        Contact contact = (Contact)object;
        StringBuffer mailbody = new StringBuffer();

        mailbody.append("Sie haben eine Kontaktanfrage über mediaDESK ["+Config.instanceName+"] erhalten:\n");
        mailbody.append("\nName: "+contact.getName());
        mailbody.append("\nEmail: "+contact.getEmail());
        mailbody.append("\nNotiz: \n"+contact.getNotice());

        //System.out.println(mailbody.toString());

        MailWrapper.sendAsync(Config.mailserver,Config.mailsender,Config.mailReceiverAdminEmail,"mediaDESK-Kontaktanfrage",mailbody.toString());

        return super.onSubmit(object);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
        if (request.getParameter("captcharesponse")==null) {
            e.reject("register.error.nocaptcha","No Captcharesponse in request");
        } else {
            if (!request.getParameter("captcharesponse").equalsIgnoreCase(
                    (String)request.getSession().getAttribute("captcha")
            )) {
                e.reject("login.error.captchafailed","Captcha Check Failed");
            }
        }
    }
}
