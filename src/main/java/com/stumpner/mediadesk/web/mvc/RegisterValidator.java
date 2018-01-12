package com.stumpner.mediadesk.web.mvc;

import org.springframework.validation.Errors;
import com.stumpner.mediadesk.usermanagement.User;
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
 * Date: 04.04.2005
 * Time: 21:03:40
 * To change this template use File | Settings | File Templates.
 */
public class RegisterValidator extends UserEditValidator {

    public boolean supports(Class aClass) {
        return aClass.equals(User.class);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validate(Object o, Errors errors) {
        //To change body of implemented methods use File | Settings | File Templates.
        super.validate(o,errors);

        User user = (User)o;
        if (Config.forbiddenDomains.length()>0) {
            String[] forbiddenDomains = Config.forbiddenDomains.split(";");
            for (String forbiddenDomain : forbiddenDomains) {
                if (user.getEmail().toUpperCase().endsWith(forbiddenDomain.toUpperCase())) {
                    errors.rejectValue("email","",null,"Value Forbidden");
                    errors.reject("register.error.forbiddenDomain", new String[] { getDomainFromEmail(user.getEmail()) }, "forbiddenDomains");
                }
            }
        }

    }

    private String stringJoin(String[] string, String separator) {

        StringBuffer joinedString = new StringBuffer("");
        for (String forbiddenDomain : string) {
            joinedString = joinedString.append(forbiddenDomain);
            joinedString = joinedString.append(separator);
        }
        return joinedString.toString();

    }

    private String getDomainFromEmail(String email) {

        String[] array = email.split("@");
        if (array.length>1) {
            return array[1];
        }

        return "";

    }
}
