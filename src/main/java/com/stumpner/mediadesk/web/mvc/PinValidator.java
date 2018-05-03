package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.pin.Pin;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.web.mvc.commandclass.PinLogin;

import java.util.GregorianCalendar;
import java.util.Calendar;

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
 * Date: 30.11.2005
 * Time: 21:17:25
 * To change this template use File | Settings | File Templates.
 */
public class PinValidator implements Validator {

    public boolean supports(Class aClass) {
        if (aClass.equals(PinLogin.class)) {
            return true;
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validate(Object o, Errors errors) {

        PinLogin pinLogin = (PinLogin)o;
        PinpicService pinpicService = new PinpicService();
        try {
            Pin pin = pinpicService.getPinpicByPin(((Pin)o).getPin());
            int imageCount = pinpicService.getPinpicImages(pin.getPinId()).size();
            if (imageCount<1 && !pin.isUploadEnabled()) {
                errors.reject("pinlogin.error.noimage","!!!DM PIN EMPTY!!!");
            }

            if (!pin.getPassword().isEmpty()) {
                //Passwortabfrage
                if (pinLogin.getPassword().isEmpty()) {
                    pinLogin.setUsePassword(true);
                    errors.reject("pinlogin.enterpassword","!!!ENTER PIN PASSWORD!!!");
                } else {
                    if (!pinpicService.checkPassword(pinLogin, pin)) {
                        errors.reject("pinlogin.falsepassword","!!!PIN PASSWORD FALSE!!!");
                    } else {
                        //Passwort ok
                    }
                }

            }

            GregorianCalendar today = (GregorianCalendar)GregorianCalendar.getInstance();
            GregorianCalendar tomorrow = (GregorianCalendar)GregorianCalendar.getInstance();
            today.add(Calendar.DAY_OF_YEAR,-1);

            if (pin.isEnabled()) {
                if (pin.getUsed()< pin.getMaxUse()) {
                    if (pin.getStartDate().getTime() <= (tomorrow.getTime().getTime())) {
                        //wenn in der startzeit
                        if (pin.getEndDate().getTime() >= (today.getTime().getTime())) {
                            //wenn in der endzeit
                        } else {
                            errors.reject("pinlogin.error.tolate","!!!DM PIN TOLATE!!!");
                        }
                    } else {
                        errors.reject("pinlogin.error.tosoon","!!!DM PIN TOSOON!!!");
                    }
                } else {
                    errors.reject("pinlogin.error.maxreached","!!!DM PIN MAX REACHED!!!");
                }
            } else {
                errors.reject("pinlogin.error.notenabled","!!!DM PIN NOT ENABLED!!!");
            }

        } catch (ObjectNotFoundException e) {
            errors.reject("pinlogin.error.false","!!!DM PIN NOT EXIST!!!");
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
