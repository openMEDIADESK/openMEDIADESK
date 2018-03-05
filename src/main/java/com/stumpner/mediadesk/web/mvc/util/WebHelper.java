package com.stumpner.mediadesk.web.mvc.util;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.UserFactory;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.usermanagement.UserAuthentication;
import com.stumpner.mediadesk.image.pinpics.Pinpic;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.PinpicService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.web.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 28.11.2007
 * Time: 21:13:46
 * To change this template use File | Settings | File Templates.
 */
public class WebHelper {

    /**
     * Gibt das User-Objekt zurück, das sich im Request befindet (eingeloggter user).
     * Ist der Benutzer nicht eingeloggt, wird ein User-Objekt mit id=-1 zurückgegeben.
     * @param httpServletRequest
     * @return
     */
    public static User getUser(HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);

        if (session!=null) {
            if (httpServletRequest.getSession().getAttribute("user")!=null) {
                return (User)httpServletRequest.getSession().getAttribute("user");
            } else {
                return UserFactory.createVisitorUser();
            }
        } else {
            return UserFactory.createVisitorUser();
        }
    }

    public static Pinpic getPinFromContext(HttpServletRequest request) {

        int pinId = 0;
        if (request.getSession().getAttribute("pinid")!=null) {
            pinId = ((Integer)request.getSession().getAttribute("pinid")).intValue();
        } else if (request.getParameter("pinid")!=null) {
            pinId = Integer.parseInt(request.getParameter("pinid"));
        }

        Pinpic pin = null;
        if (pinId!=0) {
            PinpicService pinpicService = new PinpicService();
            try {
                pin = (Pinpic)pinpicService.getById(pinId);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            } catch (IOServiceException e) {
                e.printStackTrace();
            }
        }

        return pin;
    }

    public static boolean isFromPinUploadContext(HttpServletRequest request) {

        if (getUser(request).getRole()==User.ROLE_UNDEFINED) {
            //Besucher/GÃ¤ste (nicht angemeldet) die in einen PIN eingeloggt sind
            if (request.getSession().getAttribute("pinid")!=null) {
                int pinpicId = ((Integer)request.getSession().getAttribute("pinid")).intValue();
                PinpicService pinpicService = new PinpicService();
                try {
                    Pinpic pinpic = (Pinpic)pinpicService.getById(pinpicId);
                    if (pinpic.isUploadEnabled()) {
                        return true;
                    }
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (IOServiceException e) {
                    e.printStackTrace();
                }
            }
        }
        if (getUser(request).getRole()>=User.ROLE_PINEDITOR &&
                (request.getParameter("pinid")!=null || request.getSession().getAttribute("pinid")!=null)) {
            //Engeloggte User mit mind. PINEDITOR-Rolle
            return true;
        }
        return false;
    }

    /**
     * Setzt das Autologin-Cookie im Browser
     */
    public static void setAutologinCookie(String username, String password, HttpServletResponse response) {

        System.out.println("Autologin Cookie wird gesetzt: "+username+":"+password);

        Cookie cookie = new Cookie("username",username);
        cookie.setMaxAge(Integer.MAX_VALUE);
        Cookie cookie2 = new Cookie("password",password);
        response.addCookie(cookie);
        response.addCookie(cookie2);
    }

    public static void setAutologinCookie(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        setAutologinCookie(username, password, response);
    }


    public static UserAuthentication getAutologinCookies(HttpServletRequest request) {

        UserAuthentication user = null;
        if (request.getCookies()!=null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase("username") ||
                    cookie.getName().equalsIgnoreCase("password")) {

                    if (user==null) { user = new UserAuthentication(); }

                    if (cookie.getName().equalsIgnoreCase("username")) {
                        user.setUserName(cookie.getValue()); }
                    if (cookie.getName().equalsIgnoreCase("password")) {
                        user.setPassword(cookie.getValue()); }
                }
            }
        }
        return user;
    }

    public static void processAutologin(HttpServletRequest request) throws ObjectNotFoundException, IOServiceException {

        UserAuthentication userAuth = getAutologinCookies(request);
        if (userAuth!=null) {
            System.out.println("FolderIndexController: Autologin Cookies: "+userAuth.getUserName());
            HttpSession session = request.getSession();
            UserService userService = new UserService();
            User user = (User) userService.getByName(userAuth.getUserName());
            if (user.isEnabled()) {
                System.out.println("Userlogin[Autologin]: user="+user.getUserName()+" from sessionid="+session.getId());
                Authenticator auth = new Authenticator();
                boolean passOk = auth.checkPassword(userAuth.getUserName(), userAuth.getPassword());
                if (passOk) {
                    System.out.println("autologin ok");
                    //logger.debug("Userlogin[Autologin]: user="+user.getUserName()+" from sessionid="+session.getId());
                    session.setAttribute("user",user);
                } else {
                    System.err.print("autologin false");
                }
            }
        } else {
            System.out.println("FolderIndexController: NO AUTOLOGIN Cookies");
        }

    }

    /** Hilfsfunktionen **/


    /**
     * Checks if a user is logged in
     * @param httpServletRequest
     * @return
     */
    public static boolean isLoggedIn(HttpServletRequest httpServletRequest) {

        if (httpServletRequest.getSession().getAttribute("user")!=null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasMinimumRole(HttpServletRequest httpServletRequest,int minimumRole) {

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


    public static void denyByAcl(HttpServletResponse response) {
        try {
            response.sendError(403,"Denied by ACL");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void sendLngRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

        LocaleResolver localeResolver = new LocaleResolver();
        Locale locale = localeResolver.resolveLocale(request);
        response.sendRedirect(response.encodeRedirectURL("/"+locale.getLanguage()+"/"+url));
    }

    public static String getServerNameUrl(HttpServletRequest request) {

        String portString = "";
        if (request.getScheme().equalsIgnoreCase("http") && request.getServerPort()!=80) {
            portString = ":"+request.getServerPort();
        } else if (request.getScheme().equalsIgnoreCase("https") && request.getServerPort()!=443) {
            portString = ":"+request.getServerPort();
        }

        String serverName = request.getServerName();
        if (request.getHeader("X-MEDIADESK-HOST")!=null) {
            serverName = request.getHeader("X-MEDIADESK-HOST");
        }

        String url = request.getScheme()+"://"+serverName+portString;

        return url;
    }

    public static String getServerNameUrlPath(HttpServletRequest request) {

        String url = getServerNameUrl(request)+request.getRequestURI();

        return url;
    }

    public static String getServerNameUrlPathWithQueryString(HttpServletRequest request) {

        String url = getServerNameUrlPath(request);
        if (request.getQueryString()!=null) {
            url = url+"?"+request.getQueryString();
        }

        return url;
    }

    /**
     * Gibt die Redirect Url zusammen mit dem Hostnamen zurück
     * @param request
     * @param redirectToUrl
     */
    public static String getRedirectUrl(HttpServletRequest request, HttpServletResponse response, String redirectToUrl) {
        return WebHelper.getServerNameUrl(request)+response.encodeURL(redirectToUrl);
    }
}
