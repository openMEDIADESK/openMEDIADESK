package com.stumpner.mediadesk.web.tags;

import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;

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
 * Date: 10.05.2005
 * Time: 22:11:44
 * To change this template use File | Settings | File Templates.
 */
public class LoginCheckTag extends TagSupport {

    private int role = -1;
    private int andRoleExact = -1;
    private int roleNot = -1;
    private boolean notLoggedIn = false;

    public void setRole(int role) {
        this.role = role;
    }

    public void setRoleNot(int roleMax) {
        this.roleNot = roleMax;
    }

    public void setNotLoggedIn(boolean notLoggedIn) {
        this.notLoggedIn = notLoggedIn;
    }

    public void setAndRoleExact(int andRoleExact) {
        this.andRoleExact = andRoleExact;
    }

    public int doStartTag() throws JspException {

        if (pageContext.getSession().getAttribute("user")!=null) {
            //eingeloggt
            if (notLoggedIn==true) {
                //content der beim eingeloggten user nicht angezeigt werden soll
                return SKIP_BODY;
            }

            User user = (User)pageContext.getSession().getAttribute("user");
            if (role!=-1) {
                //rolle muss einen bestimmten wert haben
                if (user.getRole()>=role) {
                    return EVAL_BODY_INCLUDE;
                } else {
                    if (user.getRole()==andRoleExact) {
                        //Wenn die Rolle bei andRoleExact angegeben ist dann auswerten
                        return EVAL_BODY_INCLUDE;
                    } else {
                        return SKIP_BODY;
                    }
                }
            } else {
                if (roleNot!=-1) {
                    //rolle muss unter einem bestimmten wert sein
                    if (user.getRole()<roleNot) {
                        return EVAL_BODY_INCLUDE;
                    } else {
                        return SKIP_BODY;
                    }
                } else {
                    return EVAL_BODY_INCLUDE;
                }
            }
        } else {
            //night eingeloggt
            if (notLoggedIn==true) {
                return EVAL_BODY_INCLUDE;
            }
            return SKIP_BODY;
        }

    }
}
