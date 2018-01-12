package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.IOException;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.lic.License;
import com.stumpner.mediadesk.lic.LicenseException;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.mvc.util.BreadCrumbItem;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.web.mvc.common.GlobalRequestDataProvider;

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
 * Date: 25.03.2005
 * Time: 00:00:39
 * To change this template use File | Settings | File Templates.
 */
public class AbstractPageController extends ParameterizableViewController implements SuSIDEBaseController {

    /**
     * @deprecated Daten sollten in das Request-Objekt geschrieben werden
     */
    protected Map model = new HashMap();

    //permits only logged in members to view this page /controller,
    //or a minimum of role
    protected boolean permitOnlyLoggedIn = false;
    protected int permitMinimumRole = 0;

    public AbstractPageController() {
        //org.springframework.web.servlet.mvc.
        this.setCacheSeconds(-1);

    }

    /**
     * Mit dieser Methode können die Model-Daten in den Request geschrieben werden.
     * Achtung rückzugebende Map wird derzeit nicht ausgewertet!!!
     * @param request
     * @param response
     * @return wird derzeit nicht ausgewertet!!
     * @throws Exception
     */
    protected Map getModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        boolean serve = false;

        if (License.isEnabled()) {
            //Lizenz-Check
            String serverName = httpServletRequest.getServerName();
            serve = true;
            if (serverName.equalsIgnoreCase(License.getDomain1()) || serverName.equalsIgnoreCase("www."+License.getDomain1())
                    ||
                    serverName.equalsIgnoreCase(License.getDomain2()) || serverName.equalsIgnoreCase("www."+License.getDomain2())
                    ||
                    serverName.equalsIgnoreCase(License.getIp())
                    ) { serve=true; } else { serve=false; }
            if (!License.getDate().after(Calendar.getInstance())) { serve=false; }
        } else {
            serve = true;
        }

        if (serve) {

            GlobalRequestDataProvider.writeToRequest(httpServletRequest,this);

            httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            httpServletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            httpServletResponse.setDateHeader("Expires", 0); // Proxies.
            //Map model = getModel(httpServletRequest, httpServletResponse);
            //WebStack webStack = new WebStack(httpServletRequest);
            //stackCall(webStack,httpServletRequest);
            //Logger log = Logger.getLogger(getClass());

            //Prüfen ob dieser User Zugriff hat
            if (checkPermission(httpServletRequest)) {

                //ACL-Context erstellen für das jeweilige Principal
                AclContextFactory.createAclContext(httpServletRequest,this);

                //AJAX-Request
                if (httpServletRequest.getParameter("ajax")!=null) {

                    ModelAndView mav = super.handleRequestInternal(httpServletRequest,httpServletResponse);
                    //System.out.println("AJAX Request: "+httpServletRequest.getQueryString());
                    httpServletResponse.getWriter().write("OK");
                    return null;
                } else {
                    //"Normaler Webrequest"
                    return super.handleRequestInternal(httpServletRequest,httpServletResponse);
                }

            } else {
                //Keine Berechtigung für diese Seite
                httpServletResponse.sendError(403);
                return null;
            }

        } else {
            String serverName = httpServletRequest.getServerName();
            logger.error("Lizenz-Check fehlgeschlagen.");
            logger.error("Domain is: "+serverName);
            logger.error("License is: "+License.getDomain1()+" or "+License.getDomain2());
            logger.error("Expires on: "+License.getDate().getTime());

            throw new LicenseException(new License());
        }
    }

    /**
     * @deprecated Es wird kein Mastertemplate mehr verwendet
     * @param request
     * @return
     */
    protected String getMasterTemplate(HttpServletRequest request) {
        return "index.jsp";
    }

    /**
     * Call-Back Function that is used by stack to register the current page. if it should be
     * popped, this function should be overwritten
     */
    public void stackCall(WebStack webStack, HttpServletRequest request) {
        //webStack.register();
    }

    protected boolean checkPermission(HttpServletRequest request) {

        if (permitOnlyLoggedIn==true) {
            if (isLoggedIn(request)) {
                if (hasMinimumRole(request,permitMinimumRole)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    /**
     * @deprecated Template files werden nichtmehr benötigt
     * @param jspFile
     * @param httpServletRequest
     */
    public void setContentTemplateFile(String jspFile, HttpServletRequest httpServletRequest) {

        if (permitOnlyLoggedIn==true) {
            if (isLoggedIn(httpServletRequest)) {
                if (hasMinimumRole(httpServletRequest,permitMinimumRole)) {
                    httpServletRequest.setAttribute("contentTemplateFile",jspFile);
                } else {
                    httpServletRequest.setAttribute("contentTemplateFile","notallowed.jsp");
                }
            } else {
                httpServletRequest.setAttribute("contentTemplateFile","notallowed.jsp");
            }
        } else {
            httpServletRequest.setAttribute("contentTemplateFile",jspFile);
        }

        //ACL-Context erstellen für das jeweilige Principal
        AclContextFactory.createAclContext(httpServletRequest,this);

    }

    /**
     * Checks if a user is logged in
     * @param httpServletRequest
     * @return
     */
    public boolean isLoggedIn(HttpServletRequest httpServletRequest) {

        if (httpServletRequest.getSession().getAttribute("user")!=null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasMinimumRole(HttpServletRequest httpServletRequest,int minimumRole) {

        if (getUser(httpServletRequest)!=null) {
            User user = getUser(httpServletRequest);
            if (user.getRole()>=minimumRole) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public User getUser(HttpServletRequest httpServletRequest) {

        return WebHelper.getUser(httpServletRequest);
    }

    public void denyByAcl(HttpServletResponse response) {
        try {
            response.sendError(403,"Denied by ACL");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected void setBreadCrumb(Object breadCrumb, HttpServletRequest httpServletRequest) {

        /* Save Breadcrumb */
        List finalBreadCrumb = new LinkedList();

        if (breadCrumb instanceof List) {
            List breadCrumbList = (List)breadCrumb;
            Iterator it = breadCrumbList.iterator();
            while (it.hasNext()) {
                finalBreadCrumb.add(new BreadCrumbItem(it.next()));
            }
            httpServletRequest.getSession().setAttribute("breadCrumb",finalBreadCrumb);
        }
        if (breadCrumb instanceof Folder) {
            finalBreadCrumb.add(new BreadCrumbItem(breadCrumb));
        }
    }


}
