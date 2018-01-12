package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.usermanagement.UserAuthentication;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

import java.util.Map;
import java.util.Date;

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
 * Time: 00:57:03
 * To change this template use File | Settings | File Templates.
 */
public class LoginController extends SimpleFormControllerMd {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserService userService = new UserService();
        if (userService.processAutologin(request)) {
            String url = response.encodeRedirectURL(getRedirectUrl(request));
            System.out.println("redirect autologin URL: "+url);
            if (url!=null) {
                response.sendRedirect(url);
                return null;
            }
        }
        return super.handleRequestInternal(request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static String ATTRIBUTE_REDIRECT_AFTER_LOGIN = "redirectAfterLogin";
    public String requiredView = "";

    public LoginController() {
        this.setCommandClass(UserAuthentication.class);
        this.setValidateOnBinding(true);
        this.setValidator(new LoginValidator());
    }

    public String getRequiredView() {
        return requiredView;
    }

    public void setRequiredView(String requiredView) {
        this.requiredView = requiredView;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {
        //return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
        HttpSession session = httpServletRequest.getSession();

        if (httpServletRequest.getParameter("logout")!=null) {
            //user ausloggen (wenn logout übergeben wurde)
            UserService userService = new UserService();
            userService.deactivateAutologin(getUser(httpServletRequest), httpServletResponse);
            session.removeAttribute("user");
            httpServletRequest.getSession().removeAttribute("pinid");
        }

        if (session.getAttribute("user")!=null) {
            //wenn der Benutzer eingeloggt ist, login-success form anzeigen
            /*
            ModelAndView mav = super.showForm(httpServletRequest, httpServletResponse, e);
            mav.setViewName(this.getSuccessView());
            return mav;
            */
            String url = getRedirectUrl(httpServletRequest);
            httpServletResponse.sendRedirect(url);
            return null;
        } else {
            //wenn der Benutzer nicht eingeloggt ist,...
            if (!Config.userEmailAsUsername) {
                //Benutzername als Benutzername
                httpServletRequest.setAttribute("usernameCaptionMessage","login.username");
            } else {
                //Emailadresse als Benutzername
                httpServletRequest.setAttribute("usernameCaptionMessage","register.email");
            }

            //Prüfen ob von einer Download-Seite aufgerufen wurde:
            if (httpServletRequest.getSession().getAttribute(ATTRIBUTE_REDIRECT_AFTER_LOGIN)!=null) {
                //Spezielle Login-Form anzeigen:
                ModelAndView mav = super.showForm(httpServletRequest, httpServletResponse, e);
                mav.setViewName(this.getRequiredView());
                return mav;
            } else {
                //Standard Login-Form anzeigen:
                return super.showForm(httpServletRequest, httpServletResponse, e);
            }
        }
    }

    /**
     *
     * @param httpServletRequest
     * @param o
     * @param errors
     * @return
     * @throws Exception
     */
    protected String onSubmitRedirectToUrl(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute(ATTRIBUTE_REDIRECT_AFTER_LOGIN)!=null) {
            String redirectUrl = (String)session.getAttribute(ATTRIBUTE_REDIRECT_AFTER_LOGIN);
            session.removeAttribute(ATTRIBUTE_REDIRECT_AFTER_LOGIN);
            return redirectUrl;
        } else {

            return getRedirectUrl(httpServletRequest);
            /* else {
                return null;
            }  */
        }
    }

    private String getRedirectUrl(HttpServletRequest request) {

        LngResolver lngResolver = new LngResolver();
        lngResolver.resolveLng(request);

            //Nach dem Login sofort die "Hauptseite" öffnen
            if (Config.redirectStartPage.endsWith("/pin")) {
                //Bei PIN als startseite, gleich auf /index/cat weiterleiten
                return "/"+((String)request.getAttribute("lng"))+"/cat";
            }

            if (Config.redirectStartPage.endsWith("/login")) {
                //Startseite -->
                return "/"+((String)request.getAttribute("lng"))+"/cat";
            }

            String redirectToUrl = Config.redirectStartPage.replaceAll("index", ((String)request.getAttribute("lng")));
            return redirectToUrl;
    }

    /**
     * Benötigte Daten für SuccessView in den Request schreiben
     * @param httpServletRequest
     * @param o
     * @param errors
     * @return
     * @throws Exception
     */
    protected Map onSubmitReferenceData(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {

        //Prüfen/Setzen dass nach dem Login, nach ca 3 Sekunden auf die Startseite umgeleitet werden soll
        boolean showEventMigrateInfo = false;
        if (this.getUser(httpServletRequest).getRole()==User.ROLE_ADMIN) {
            FolderService folderService = new FolderService();
            if (folderService.getFolderList(10000).size()>0) {
                showEventMigrateInfo = true;
            }
        }

        if (!showEventMigrateInfo) {
            httpServletRequest.setAttribute("isAutoRedirect",
                    !Config.redirectStartPage.equalsIgnoreCase("/login"));
        } else {
            //Info für Event-Migrieren zeigen
            httpServletRequest.setAttribute("showEventMigrateInfo",true);
        }

        return super.onSubmitReferenceData(httpServletRequest, o, errors);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        HttpSession session = httpServletRequest.getSession();
        UserAuthentication userAuthentication = (UserAuthentication)o;
        processLogin(userAuthentication, session);

        if (httpServletRequest.getParameter("autologin")!=null) {
            UserService userService = new UserService();
            User user = getUser(httpServletRequest);
            userService.activateAutologin(user, httpServletResponse);
            System.out.println("autologin="+httpServletRequest.getParameter("autologin"));
        }

        return super.onSubmit(httpServletRequest,httpServletResponse,o,e);
    }

    private void processLogin(UserAuthentication userAuthentication, HttpSession session) throws Exception {

        UserService userService = new UserService();
        User user = (User) userService.getByName(userAuthentication.getUserName());
        logger.debug("Userlogin: user="+user.getUserName()+" from sessionid="+session.getId());
        Config.lastLogin = new Date();
        session.setAttribute("user",user);

    }

}
