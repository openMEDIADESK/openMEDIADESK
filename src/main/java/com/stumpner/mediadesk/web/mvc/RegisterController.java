package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

import java.security.SecureRandom;
import java.math.BigInteger;

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
 * Time: 00:22:25
 * To change this template use File | Settings | File Templates.
 */
public class RegisterController extends SimpleFormControllerMd {

    public RegisterController() {
        //this.setCommandName("simpleuser");
        this.setCommandClass(User.class);
        //this.setSessionForm(true);
        //this.setBindOnNewForm(true);
        this.setValidator(new RegisterValidator());
        this.setValidateOnBinding(true);
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        
        if (!Config.allowRegister) {
            httpServletResponse.sendError(404,"Registration not enabled");
            return null;
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        this.setContentTemplateFile("register.jsp",httpServletRequest);
        User entity = new User();
        return entity;
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse httpServletResponse, BindException e) throws Exception {
        request.setAttribute("userEmailAsUsername",Config.userEmailAsUsername);
        request.setAttribute("showUserCompanyFields",Config.showUserCompanyFields);
        request.setAttribute("showUserAddressFields",Config.showUserAddressFields);
        request.setAttribute("showUserTelFaxFields",Config.showUserTelFaxFields);

        return super.showForm(request, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {
        this.setContentTemplateFile("register.jsp",httpServletRequest);

        //check ob die AGB aktiviert sind:
        if (httpServletRequest.getParameter("agreeTAC")==null) {
            e.reject("register.error.tac","TAC not checked");
        }
        if (Config.useCaptchaRegister) {
            if (httpServletRequest.getParameter("captcharesponse")==null) {
                e.reject("register.error.nocaptcha","No Captcharesponse in request");
            } else {
                if (!httpServletRequest.getParameter("captcharesponse").equalsIgnoreCase(
                        (String)httpServletRequest.getSession().getAttribute("captcha")
                )) {
                    e.reject("login.error.captchafailed","Captcha Check Failed");
                }
            }
        }

        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }




    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        User user = (User)o;
        if (!Config.activateNewUsers && Config.informOfNewUsers) {
            user.setActivateCode(generateActivateCode());
        }
        //ActivateCode generieren
        this.addUser(user);

        if (Config.activateNewUsers) {
            //Neue Benutzer sofort aktivieren + Passwort setzen:
            this.activateUser(user,e);
            /*
            UserService userService = new UserService();
            user = (User)userService.getByName(((User)o).getUserName());
            user.setEnabled(true);
            userService.save(user);
            Authenticator auth = new Authenticator();
            try {
                auth.renewPassword(((User)o).getUserName());
            } catch (MessagingException err) {
                logger.error("Password for User "+((User)o).getUserName()+" can not be sent");
                e.reject("register.error.mailerror","Error sending Mail");
            }
            */
        }

        if (Config.informOfNewUsers) {
            UserService userService = new UserService();
            User admin = (User) userService.getByName("admin");
            String mailbody = "Ein neuer Benutzer ["+user.getUserName()+"] hat sich registriert: "+Config.instanceName+"\n\nNachfolgend finden Sie die Benutzerdetails:\n\n";
            mailbody = mailbody + "\nBenutzername: "+user.getUserName();
            mailbody = mailbody + "\nVorname: "+user.getFirstName();
            mailbody = mailbody + "\nNachname: "+user.getLastName();
            mailbody = mailbody + "\nFirma: "+user.getCompany();
            mailbody = mailbody + "\nTelefon: "+user.getPhone();
            mailbody = mailbody + "\nEmail: "+user.getEmail();
            if (Config.activateNewUsers) {
                mailbody = mailbody + "\n\nDer Benutzer wurde laut den Einstellungen bereits freigeschalten. ";
                if (Config.passmailUser) {
                    mailbody = mailbody + "Ein Passwort wurde generiert und dem Benutzer via Email zugesendet.";
                } else {
                    mailbody = mailbody + "Dem Benutzer wurde jedoch noch kein Passwort zugesendet, da diese Funktion in den Einstellungen deaktiviert ist.";
                }
            } else {
                mailbody = mailbody + "\n\nDer Benutzer ist noch nicht freigeschalten! �ffnen Sie diesen Link um den Benutzer freizuschalten und ein Passwort zu versenden:\n";
                mailbody = mailbody + httpServletRequest.getScheme() + "://"+httpServletRequest.getServerName() + "/mfb/au?id="+user.getUserId()+"&code="+user.getActivateCode();
                //mailbody = mailbody +"\n\nDer Benutzer ist noch nicht freigeschalten! Loggen Sie sich als Administrator in mediaDESK ein und schalten Sie den Benutzer frei. Editieren Sie den Benutzer und aktivieren Sie Passwort zuruecksetzen und per Email zusenden.";
            }
            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,admin.getEmail(),"Neue Benutzerregistrierung",mailbody);
        }

        return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private String generateActivateCode() {

        SecureRandom random = new SecureRandom();
        String code = new BigInteger(130, random).toString(32);
        return code;
    }

    private void activateUser(User user, BindException e) throws IOServiceException, ObjectNotFoundException {

            //Neue Benutzer sofort aktivieren + Passwort setzen:
            UserService userService = new UserService();
            user = (User)userService.getByName((user).getUserName());
            user.setEnabled(true);
            userService.save(user);
            Authenticator auth = new Authenticator();
            try {
                auth.renewPassword((user).getUserName());
            } catch (MessagingException err) {
                logger.error("Password for User "+(user).getUserName()+" can not be sent");
                e.reject("register.error.mailerror","Error sending Mail");
            }
        

    }

    private void addUser(User user) {

        Logger logger = Logger.getLogger(RegisterController.class);
        UserService userService = new UserService();
        try {
            user.setSecurityGroup(Config.defaultSecurityGroup);
            user.setRole(Config.defaultRole);
            if (Config.userEmailAsUsername) {
                user.setUserName(user.getEmail());
            }
            userService.add(user);
            if (Config.homeCategoryId!=-1) {
                if (Config.homeCategoryAutocreate) {
                    userService.createHomeCategory(user);
                }
            }
        } catch (DublicateEntry exception) {
            logger.debug("User not added, dublicate");
        } catch (IOServiceException e) {
            e.printStackTrace();
        }
    }




}
