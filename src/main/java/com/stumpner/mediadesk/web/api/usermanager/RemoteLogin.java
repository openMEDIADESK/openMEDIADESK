package com.stumpner.mediadesk.web.api.usermanager;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.UserAuthentication;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;

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
 * Login via Webservice
 * Optional kann noch überall der Wert redirect angegeben werden, um nach dem OK umzuleiten
 * http://server/api/remotelogin/?username=&password=&redirect=
 * http://server/api/remotelogin/?action=logout
 * User: stumpner
 * Date: 10.10.2011
 * Time: 09:34:46
 */
public class RemoteLogin extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (isLoginRequest(request)) {
            if (this.remoteLogin(request, request.getParameter("username"), request.getParameter("password"))) {

                /** AUTOLOGIN **/
                if (Config.useAutoLogin) {
                    //Prüfen ob autologin=true
                    if (request.getParameter("autologin")!=null) {
                        if (request.getParameter("autologin").equalsIgnoreCase("true")) {
                            /**
                             * Sessionvariable setzen, damit beim nächsten CategoryIndex-Request das Autologin-Cookie geprüft oder ggf nochmals gesetzt wird
                             */
                            request.getSession().setAttribute("autologin","true");
                            request.getSession().setAttribute("username",request.getParameter("username"));
                            request.getSession().setAttribute("password",request.getParameter("password"));
                            WebHelper.setAutologinCookie(request, response);
                        }
                    }
                }

                /** AUTOLOGIN ENDE **/

                if (request.getParameter("redirect")!=null) {
                    response.sendRedirect(request.getParameter("redirect"));
                } else {
                    writeJsonReply("result","ok",request,response);
                }
                return;
            } else {
                writeJsonReply("result","error",request,response);
                return;
            }
        }
        if (isLogoutRequest(request)) {
            if (this.remoteLogout(request)) {
                if (request.getParameter("redirect")!=null) {
                    response.sendRedirect(request.getParameter("redirect"));
                } else {
                    writeJsonReply("result","ok",request,response);
                }
                return;
            } else {
                writeJsonReply("result","error",request,response);
                return;
            }
        }
    }

    private void writeJsonReply(String key, String value, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Writer writer = response.getWriter();
        String callbackStart = "";
        String callbackEnd = "";
        if (request.getParameter("callback")!=null) {
            callbackStart = request.getParameter("callback")+"(";
            callbackEnd = ")";
        }
        String sessionId = request.getSession().getId();
        writer.write(callbackStart+"{\""+key+"\" : \""+value+"\"}, {\"JSESSIONID\" : \""+sessionId+"\"}"+callbackEnd);
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        if (request.getParameter("action")!=null) {
            if (request.getParameter("action").equalsIgnoreCase("logout")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Es handelt sich um einen Login-Request
     * + wenn der parameter username und der parameter password übergeben wurde
     * @param request
     * @return
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        if (request.getParameter("username")!=null && request.getParameter("password")!=null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean remoteLogin(HttpServletRequest request, String username, String password) {

        HttpSession session = request.getSession();
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUserName(username);
        userAuthentication.setPassword(password);
        UserService userService = new UserService();
        User user = null;

        try {
            user = (User) userService.getByName(userAuthentication.getUserName());

            if (user.isEnabled()) {
                Authenticator auth = new Authenticator();
                if (auth.checkPassword(userAuthentication.getUserName(),userAuthentication.getPassword())) {
                    //password is okay
                    System.out.println("Userlogin (REMOTE): user="+user.getUserName()+" from sessionid="+session.getId());
                    session.setAttribute("user",user);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
            
        } catch (ObjectNotFoundException e) {
            System.out.println("Userlogin (REMOTE): user="+username+" nicht gefunden");
            return false;
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
    }

    private boolean remoteLogout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.removeAttribute("user");
        request.getSession().removeAttribute("pinid");
        return true;
    }

}
