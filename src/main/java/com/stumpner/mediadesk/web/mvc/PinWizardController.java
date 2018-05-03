package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.PinWizard;
import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.Resources;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Iterator;

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
 * Date: 03.01.2008
 * Time: 10:17:31
 * To change this template use File | Settings | File Templates.
 */
public class PinWizardController extends SimpleFormControllerMd {


    public PinWizardController() {

        this.setCommandClass(PinWizard.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole = User.ROLE_PINEDITOR;    

    }


    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        PinWizard pinWizard = new PinWizard();
        PinpicService pinpicService = new PinpicService();
        User loggedInUser = getUser(httpServletRequest);

        if (loggedInUser.getRole()==User.ROLE_ADMIN) {
            //Admin sieht alle
            pinWizard.setPinList( pinpicService.getPinpicList() );
        } else {
            //Andere User sehen nur die eigenen Pins
            pinWizard.setPinList( pinpicService.getPinpicList(loggedInUser) );
        }
        pinWizard.setImageList(((List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)));
        return pinWizard;
    }


    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object object, BindException bindException) throws Exception {

        PinWizard pinWizard = (PinWizard)object;
        if (pinWizard.getPinType()==PinWizard.TYPE_EXISTING && pinWizard.getSelectedPinId()==0) {
            bindException.reject("selectedPinId");
        }
        /*
        if (((List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)).size()==0) {
            bindException.reject("noimages");
        }
        */

        super.onBindAndValidate(httpServletRequest, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        this.setContentTemplateFile("/pinwizard.jsp",httpServletRequest);        
        return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }


    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        PinWizard pinWizard = (PinWizard)object;
        User user = this.getUser(httpServletRequest);

        if (pinWizard.getPinType()==PinWizard.TYPE_NEW) {
            PinpicService pinpicService = new PinpicService();
            Pin pin = new Pin();
            pin.setCreatorUserId(user.getUserId());
            pin.setEmailnotification(this.getUser(httpServletRequest).getEmail());
            pin.setDefaultview(Folder.VIEW_UNDEFINED);
            pin = pinpicService.add(pin);
            addImagesToPin(pinWizard.getImageList(), pin.getPinId());
            httpServletResponse.sendRedirect("pinedit?pinid="+ pin.getPinId());
        }
        if (pinWizard.getPinType()==PinWizard.TYPE_EXISTING) {

            addImagesToPin(pinWizard.getImageList(),pinWizard.getSelectedPinId());
            httpServletResponse.sendRedirect("pinedit?pinid="+pinWizard.getSelectedPinId());
        }

        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void addImagesToPin(List selectedImageList, int pinId) {

        PinpicService pinpicService = new PinpicService();
            Iterator selectedImages = selectedImageList.iterator();
            while (selectedImages.hasNext()) {
                MediaObject imageVersion = (MediaObject)selectedImages.next();
                pinpicService.addImageToPinpic(imageVersion.getIvid(),pinId);
            }

    }
}
