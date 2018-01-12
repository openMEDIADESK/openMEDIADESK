package com.stumpner.mediadesk.web.mvc;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import com.stumpner.mediadesk.usermanagement.UserAuthentication;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

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
 * Date: 05.04.2005
 * Time: 21:41:51
 * To change this template use File | Settings | File Templates.
 */
public class LoginValidator implements Validator {
    public boolean supports(Class aClass) {
        return aClass.equals(UserAuthentication.class);
    }

    public void validate(Object o, Errors errors) {

        UserAuthentication userAuth = (UserAuthentication)o;

        //check if user exists:
        UserService userService = new UserService();
        try {
            User user = (User)userService.getByName(userAuth.getUserName());
            //check if enabled
            if (!user.isEnabled()) {
                errors.reject("login.error.disabled","DM::!!!DISABLED!!!");
            }
            //check if password is okay
            Authenticator auth = new Authenticator();
            if (auth.checkPassword(userAuth.getUserName(),userAuth.getPassword())) {
                //password is okay
            } else {
                errors.reject("login.error.password","DM::!!!PASSWORD!!!");
                //renew password
                //auth.renewPassword(userAuth.getUserName());
            }
        } catch (ObjectNotFoundException e) {
            errors.reject("login.error.usernotexist","DM::!!!USER NOT EXIST!!!");
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
