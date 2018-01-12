package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.web.LngResolver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.mail.MessagingException;
import java.io.IOException;

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
 * Dieses Servlet nimmt von der URL /mfb/... Anfragen entgegen um dann ein Funktion auzul�sen:
 * + z.b. Benutzer freizuschalten
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 22.05.2012
 * Time: 20:12:08
 * To change this template use File | Settings | File Templates.
 */
public class MailFunctionBroker extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LngResolver lngResolver = new LngResolver();
        lngResolver.resolveLng(request);
        try {
            //mfb/
            System.out.println("MFB Request: "+request.getRequestURI());
            if (request.getRequestURI().equalsIgnoreCase("/mfb/au")) {

                activateUser(request, response);
            }

        } catch (ObjectNotFoundException e) {
            throw new ServletException("Benutzer konnte nicht gefunden werden");
        } catch (IOServiceException e) {
            throw new ServletException("IOService Exception",e);
        } catch (MessagingException e) {
            throw new ServletException(e);
        }

    }

    /**
     * Freischalten eines Benutzers und neugenerieren des Passwortes
     * @param request
     * @param response
     */
    private void activateUser(HttpServletRequest request, HttpServletResponse response) throws ObjectNotFoundException, IOServiceException, MessagingException, IOException {

        UserService userService = new UserService();
        int userId = Integer.parseInt(request.getParameter("id"));
        String activateCode = request.getParameter("code");

        if (activateCode.length()>0) {
            User user = (User)userService.getById(userId);
            if (!user.isEnabled()) {
                if (user.getActivateCode().equalsIgnoreCase(activateCode)) {
                    //User freischalten:
                    user.setActivateCode("");
                    user.setEnabled(true);
                    userService.save(user);
                    Authenticator auth = new Authenticator();
                    auth.renewPassword(user.getUserName());

                    HttpSession session = request.getSession();
                    session.setAttribute("headline","mfb.au.headline");
                    session.setAttribute("nextUrl","/");
                    session.setAttribute("text","mfb.au.text");
                    session.setAttribute("subheadline","mfb.au.subheadline");

                    response.sendRedirect(response.encodeRedirectURL("/de/message"));
                } else {
                    //Activate Code falsch
                    HttpSession session = request.getSession();
                    session.setAttribute("headline","message.headline.error");
                    session.setAttribute("nextUrl","/");
                    session.setAttribute("text","mfb.au.textacfalse");
                    session.setAttribute("subheadline","mfb.au.subheadline");

                    response.sendRedirect(response.encodeRedirectURL("/"+request.getAttribute("lng")+"/message"));
                }
            } else {
                //Benutzer bereits freigeschalten
                HttpSession session = request.getSession();
                session.setAttribute("headline","message.headline.error");
                session.setAttribute("nextUrl","/");
                session.setAttribute("text","mfb.au.textalreadyenabled");
                session.setAttribute("subheadline","mfb.au.subheadline");

                response.sendRedirect(response.encodeRedirectURL("/de/message"));
            }
        } else {
            //Kein Activate Code angegeben
            HttpSession session = request.getSession();
            session.setAttribute("headline","message.headline.error");
            session.setAttribute("nextUrl","/");
            session.setAttribute("text","mfb.au.textacfalse");
            session.setAttribute("subheadline","mfb.au.subheadline");

            response.sendRedirect(response.encodeRedirectURL("/de/message"));
        }

    }
}
