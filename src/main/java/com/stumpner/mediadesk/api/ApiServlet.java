package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

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
 * User: stumpner
 * Servlet, welches die API-Aufrufe entgegennimmt, abarbeitet und Daten zur�ckliefert
 * Date: 19.03.2008
 * Time: 10:30:01
 */
public class ApiServlet extends HttpServlet {

    public ApiServlet() {

        registerApiClass(new ApiTest());
        registerApiClass(new CategoryApi());
        registerApiClass(new MediaObjectApi());

    }

    private List apiClassList = new LinkedList();
    /**
     * Ein API-Servlet aufruf sieht folgenderma�en aus:
     *
     * /?USERNAME=##&PASSWORD=##&method=##&param=##&param=##
     * 
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        PrintWriter writer = httpServletResponse.getWriter();
        String method = httpServletRequest.getParameter("method");
        String[] parameter = httpServletRequest.getParameterValues("param");
        if (method==null) { falseRequest("No QS-Parameter method given.",writer); return; }
        if (parameter==null) { falseRequest("No QS-Parameter param given.",writer); return; }

        if (authenticateUser(httpServletRequest)) {

            User user = (User)httpServletRequest.getAttribute("user");
            Iterator apiClasses = apiClassList.iterator();
            boolean executed = false;
            while (apiClasses.hasNext()) {
                ApiClass apiClass = (ApiClass)apiClasses.next();
                if (apiClass.supportsMethod(method)) {
                    //Methode wird von dieser Klasse zur verf�gung gestellt

                    writer.print(
                            apiClass.call(user,method,parameter));
                    executed = true;
                }
            }

            if (!executed) {
                writer.print("Method not found.");
            }
        } else {

            //wenn nicht authentifiziert dann wird nur die versionsinfo angezeigt

            /*
            Sample:
            http://localhost/gateway/api?method=version&param=mediadesk
             */
            if (method.equalsIgnoreCase("version")) {
                writer.print(Config.versionNumbner);
            } else {

                //oder der request verweigert
                writer.print("Access Denied.");

            }
        }

    }



    /**
     * �berpr�ft die Berechtigung des Benutzers
     * @param request
     * @return
     */
    private boolean authenticateUser(HttpServletRequest request) {

        String username = request.getParameter("USERNAME");
        String password = request.getParameter("PASSWORD");

        if (username == null || password ==null) {
            //Keine Authentifzierungs-Daten �bergeben
            return false;
        } else {

            //Zugriff und Authentifizierung Pr�fen
                Authenticator auth = new Authenticator();
                boolean hasAccess = false;
                User user = new User();
                if (auth.checkPassword(username,password)) {
                    UserService userService = new UserService();
                    try {
                        user = (User)userService.getByName(username);
                        request.setAttribute("user",user);
                        if (user.getRole()>=User.ROLE_ADMIN) hasAccess = true;
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
            return hasAccess;

        }

    }

    /**
     * Registriert eine neue ApiClass die aufgerufen werden kann
     * @param apiClass
     */
    private void registerApiClass(ApiClass apiClass) {

        apiClassList.add(apiClass);

    }

    private void falseRequest(String message, PrintWriter writer) {

        writer.print("ERROR, "+message);
    }
}
