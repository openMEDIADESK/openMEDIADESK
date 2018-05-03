package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.web.mvc.commandclass.SendCommand;
import com.stumpner.mediadesk.web.mvc.exceptions.Http404Exception;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.util.SendFile;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Date: 23.07.2008
 * Time: 20:12:13
 * To change this template use File | Settings | File Templates.
 */
public class SendController extends SimpleFormControllerMd {

    public SendController() {
        this.setCommandClass(SendCommand.class);
        this.setValidator(new SendControllerValidator());
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        if (!isLoggedIn(httpServletRequest)) {
            if (Config.useCaptchaSend) {
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
        }

        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        User user = this.getUser(httpServletRequest);

        if (httpServletRequest.getParameter("redirect")!=null) {
            //Nach der Eingabe auf eine Seite redirecten
            httpServletRequest.getSession().setAttribute("redirectTo",httpServletRequest.getParameter("redirect"));
            //System.out.println("Redirect: "+httpServletRequest.getParameter("redirect"));
        }

        if (httpServletRequest.getParameter("ivid")==null) {
            throw new Http404Exception("No ivid Parameter");
        }

        int ivid = 0;
        try {
            ivid = Integer.parseInt(httpServletRequest.getParameter("ivid"));
        } catch (NumberFormatException e) {
            throw new Http404Exception(e);
        }
        
        MediaService imageService = new MediaService();
        LngResolver lngResolver = new LngResolver();
        imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        MediaObject image = imageService.getMediaObjectById(ivid);
        if (image==null) { throw new Http404Exception("ivid="+ivid+" not found"); }


        StringBuffer sb = new StringBuffer();
        sb.append("Guten Tag,\n\nFolgender Link wurde Ihnen gesendet:\n");

        sb.append("http://"+httpServletRequest.getServerName()+"/"+httpServletRequest.getAttribute("lng")+"/ppreview?id="+image.getIvid());
        sb.append("\n\nKlicken Sie auf den Link um die Website anzuzeigen.");

        SendCommand sendCommand = new SendCommand();
        sendCommand.setImageVersion(image);
        sendCommand.setSubject("Bild: "+image.getVersionTitle());
        sendCommand.setMailtext(sb.toString());

        int minRoleToSendAttach = User.ROLE_MASTEREDITOR;
        if (Config.usersCanSendAttachments) {
            // Benutzer (=eingeloggte) k�nnen Attachments versenden
            minRoleToSendAttach = User.ROLE_USER;
        }

        if (user.getRole()>=minRoleToSendAttach) {
            sendCommand.setOnlyAsLink(false);
        } else {
            sendCommand.setOnlyAsLink(true);
        }




        return sendCommand;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (!Config.showSendImage) {
            httpServletResponse.sendError(404);
        }

        if (isLoggedIn(httpServletRequest)) {
            httpServletRequest.setAttribute("useCaptcha", false);
        } else {
            httpServletRequest.setAttribute("useCaptcha", Config.useCaptchaSend);
        }

        this.setContentTemplateFile("send.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        httpServletRequest.setCharacterEncoding("UTF-8");
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView processFormSubmission(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (httpServletRequest.getParameter("cancel")!=null) {
            WebStack webStack = new WebStack(httpServletRequest);
            webStack.push();
            String redirectUrl = webStack.pop();
            SendCommand sendCommand = (SendCommand)o;
            httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL(
                        redirectUrl
                )
            );
            return null;
        } else {
            httpServletRequest.setCharacterEncoding("UTF-8");
            return super.processFormSubmission(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (!Config.showSendImage) { throw new Exception("SendImage is disabled"); }

        SendCommand sendCommand = (SendCommand)o;

        httpServletRequest.setCharacterEncoding("UTF-8");

        if (sendCommand.isAsAttachment()) {
            SendFile sendFile = new SendFile();
            sendFile.setFrom(Config.mailsender);
            sendFile.setHost(Config.mailserver);
            sendFile.setTo(sendCommand.getRecipient());
            sendFile.setSubject(sendCommand.getSubject());
            sendFile.setText(sendCommand.getMailtext());
            sendFile.setAttachname(sendCommand.getImageVersion().getWebFilename());
            int imageSize = 0; //Original
            String imageFile = Config.imageStorePath+"/"+sendCommand.getImageVersion().getIvid()+"_"+imageSize;
            sendFile.setFilename(imageFile);
            //sendFile.send();

            MailWrapper.sendAsync(Config.mailserver, Config.mailsender, sendCommand.getRecipient(), sendCommand.getSubject(), sendCommand.getMailtext(), 
                    imageFile, sendCommand.getImageVersion().getWebFilename());
        } else {
            MailWrapper.sendAsync(
                    Config.mailserver,Config.mailsender,sendCommand.getRecipient(),
                    sendCommand.getSubject(),sendCommand.getMailtext()
            );
        }

        //System.out.println("versende email an: "+sendCommand.getRecipient());

        //WebStack webStack = new WebStack(httpServletRequest);
        //webStack.push();
        String redirectUrl = Config.redirectStartPage;
        if (httpServletRequest.getSession().getAttribute("redirectTo")!=null) {
            redirectUrl = (String)httpServletRequest.getSession().getAttribute("redirectTo");
        }
        httpServletRequest.getSession().removeAttribute("redirectTo");
        String url = redirectUrl.replaceAll("mark","nofunc").replaceAll("unmark","nofunc");
        httpServletRequest.setAttribute("redirectUrl",url);
        this.setContentTemplateFile("send_ok.jsp",httpServletRequest);

        return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
