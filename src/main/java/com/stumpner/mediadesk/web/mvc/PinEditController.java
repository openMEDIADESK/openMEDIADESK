package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.pinpics.Pin;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.validation.BindException;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.util.Date;
import java.text.SimpleDateFormat;

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
 * Date: 09.12.2005
 * Time: 10:02:55
 * To change this template use File | Settings | File Templates.
 */
public class PinEditController extends SimpleFormControllerMd {

    public PinEditController() {
        this.setCommandClass(Pin.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole = User.ROLE_PINMAKLER;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        if (request.getParameter("redirectTo")!=null) { request.getSession().setAttribute("redirectTo", request.getParameter("redirectTo")); }
        Pin pin = new Pin();
        if (request.getParameter("pinid")!=null) {
            PinpicService pinpicService = new PinpicService();
            pin = (Pin)pinpicService.getById(Integer.parseInt(request.getParameter("pinid")));
        }
        return pin;
        //return super.formBackingObject(request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.validation.BindException e) throws Exception {

        this.setContentTemplateFile("/admin_pinedit.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.

    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        Pin pin = (Pin)o;
        if (pin.getEmailnotification().length()>0) {
            if (pin.getEmailnotification().indexOf("@")<1) {
                e.rejectValue("emailnotification","",null,"Value Required");
            }
        }
        if (pin.isUploadEnabled() && pin.isDirectDownload()) {
            e.rejectValue("directDownload","",null,"Not Possible");
            e.reject("pinedit.upanddownloaderror","!!UPLOAD AND DOWNLOAD NOT ALLOWED!!");
        }
        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {

        servletRequestDataBinder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"),true));
        servletRequestDataBinder.registerCustomEditor(Date.class, "endDate", new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"),true));
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {

        this.save((Pin)o);
        if (request.getSession().getAttribute("redirectTo")!=null) {
            response.sendRedirect(response.encodeRedirectURL(
                    (String)request.getSession().getAttribute("redirectTo")));
        } else {
            response.sendRedirect(
                    response.encodeRedirectURL("pinlist")
            );
        }
        return null;
        //return super.onSubmit(request, response, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void save(Pin pin) {

        PinpicService pinpicService = new PinpicService();
        try {
            if (pin.getPinpicTitle().length()==0) {
                pin.setPinpicTitle(pin.getPinpicName());
            }

            pinpicService.save(pin);
        } catch(IOServiceException e) {
            e.printStackTrace();
        }
    }

}
