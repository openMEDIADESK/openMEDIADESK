package com.stumpner.mediadesk.web.mvc;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import org.apache.log4j.Logger;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;

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
 * Time: 21:46:40
 * To change this template use File | Settings | File Templates.
 * //todo: �berpr�fen ob es einen User mit der selben EMailadresse gibt
 */
public class UserEditValidator implements Validator{

    public boolean supports(Class aClass) {
        return aClass.equals(User.class);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validate(Object o, Errors errors) {
        Logger logger = Logger.getLogger(UserEditValidator.class);
        //To change body of implemented methods use File | Settings | File Templates.
        boolean mandatoryFilled = true;
        User user = (User)o;
        if (user.getFirstName().length()==0) {
            errors.rejectValue("firstName","",null,"Value Required");
            mandatoryFilled = false;
        }
        if (user.getLastName().length()==0) {
            errors.rejectValue("lastName","",null,"Value Required");
            mandatoryFilled = false;
        }
        if (user.getEmail().indexOf("@")<1) {
            errors.rejectValue("email","",null,"Value Required");
            mandatoryFilled = false;
        }
        if (Config.showUserCompanyFields) {
            if (user.getCompany().length()==0) {
                errors.rejectValue("company","",null,"Value Required");
                mandatoryFilled = false;
            }
            if (user.getCompanyType().length()==0) {
                errors.rejectValue("companyType","",null,"Value Required");
                mandatoryFilled = false;
            }
        }
        if (Config.showUserAddressFields) {
            if (user.getStreet().length()==0) {
                errors.rejectValue("street","",null,"Value Required");
                mandatoryFilled = false;
            }
            if (user.getCity().length()==0) {
                errors.rejectValue("city","",null,"Value Required");
                mandatoryFilled = false;
            }
            if (user.getZipCode().length()==0) {
                errors.rejectValue("zipCode","",null,"Value Required");
                mandatoryFilled = false;
            }
            if (user.getCountry().length()==0) {
                errors.rejectValue("country","",null,"Value Required");
                mandatoryFilled = false;
            }
        }
        if (!Config.userEmailAsUsername) {
            if (user.getUserName().length()==0) {
                errors.rejectValue("userName","",null,"Value Required");
                mandatoryFilled = false;
            }
        }

        if (!mandatoryFilled) {
            errors.reject("register.error.mandatory","DM::!!mandatory not filled!!");
        }

        //check if username exists
        UserService userService = new UserService();
        if (user.getUserId()==UserEditController.USERID_NEW) {

            if (!hasOnlyAllowedChars(user.getUserName())) {
                errors.rejectValue("userName","",null,"Value Required");
                errors.reject("register.error.usernamenotallowed", "DM:: username not allowed");
            }

            try {
                userService.getByName(user.getUserName());
                logger.debug("UserEdit: cannot change username, user already registered");
                errors.reject("register.error.userexist","DM::!!user already registered!!");
            } catch (ObjectNotFoundException e) {
                logger.info("UserEdit: username changed");
            } catch (IOServiceException e) {
                logger.fatal("RegisterValidator.validate(): Database not Ready");
            }

            try {
                userService.getByEmail(user.getEmail());
                errors.reject("register.error.useremailexist","DM::!!useremail already registered!!");
            } catch (ObjectNotFoundException e) {
                //ok - einen benutzer mit dieser emailadresse gibt es nicht
            }
        }
    }

    private boolean hasOnlyAllowedChars(String userName) {

        for (int a=0;a<userName.length();a++) {
            char charcode = userName.charAt(a);
            if (!isAllowedChar(charcode)) { return false; }
        }

        return true;
    }

    private String getAllowedChars() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._-";
    }

    private boolean isAllowedChar(char c) {

        for (int b=0;b<getAllowedChars().length();b++) {
            char allowedChar = getAllowedChars().charAt(b);
            if (c==allowedChar) { return true; }
        }
        return false;
    }
}
