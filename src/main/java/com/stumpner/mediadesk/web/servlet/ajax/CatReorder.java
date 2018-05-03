package com.stumpner.mediadesk.web.servlet.ajax;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

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
 * Date: 03.10.2012
 * Time: 18:07:27
 * To change this template use File | Settings | File Templates.
 */
public class CatReorder extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request,response);
    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = WebHelper.getUser(request);

        if (user.getRole()== User.ROLE_ADMIN) {
            //System.out.println("ajaxrequest");
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String eno = (String)enumeration.nextElement();
                String pval = request.getParameter(eno);
                //System.out.println("Request: eno: "+eno+", pval: "+pval);
            }

            String movingNode = request.getParameter("node");
            String newParent = request.getParameter("parent");

            if (movingNode.equalsIgnoreCase(newParent)) {
                response.sendError(400, "Zyklus: Parent Node ist er selbst");
            }

            FolderService cs = new FolderService();

            try {
                Folder cat = cs.getFolderById(Integer.parseInt(movingNode));
                cat.setParent(Integer.parseInt(newParent));
                cs.save(cat);

                if (request.getParameter("json")==null) {
                    response.getWriter().write("1");
                } else {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html");

                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    response.setHeader("Expires", "0"); // Proxies.

                    PrintWriter out = response.getWriter();
            //out.println("{\"records\": [");
                    out.println("[\n");
                    out.println("  {\"status\":\"OK\"}\n");
                    out.println("]");
                }
            } catch (ObjectNotFoundException e) {
                response.sendError(404, "Objekt nicht gefunden");
            } catch (IOServiceException e) {
                response.sendError(500, e.getMessage());
                //response.getWriter().write("ERR:"+e.getMessage());
            }
        } else {
            response.sendError(403, "Access Denied");
            //response.getWriter().write("ERR:AccessDenied");
        }

    }


}
