<%@ page isErrorPage="true"%>
<%@ page import="com.stumpner.mediadesk.core.Config,
                 com.stumpner.mediadesk.util.MailWrapper,
                 java.io.PrintWriter,
                 java.io.StringWriter,
                 java.util.Date"%>
<%@ page import="com.stumpner.mediadesk.web.stack.WebStack"%>
<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="javax.mail.MessagingException"%>
<%
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
%>
<%

    String mailbody = "Ein Fehler trat in einer mediaDESK Instanz auf:\n\n";
    mailbody = mailbody + "\nInstanz: "+Config.instanceName+ " ["+Config.httpBase+"]";
    mailbody = mailbody + "\nDate: "+(new Date());
    mailbody = mailbody + "\nURL:"+request.getAttribute("javax.servlet.error.request_uri");
    mailbody = mailbody + "\nException: "+request.getAttribute("javax.servlet.error.exception");
    mailbody = mailbody + "\nType:"+request.getAttribute("javax.servlet.error.exception_type");
    mailbody = mailbody + "\nMessage:"+request.getAttribute("javax.servlet.error.message");
    mailbody = mailbody + "\nServlet:"+request.getAttribute("javax.servlet.error.servlet_name");
    mailbody = mailbody + "\nQs:"+request.getQueryString();
    mailbody = mailbody + "\nIP:"+request.getRemoteAddr();
    mailbody = mailbody + "\nUser-Agent:"+request.getHeader("User-Agent");
    mailbody = mailbody + "\nReferer:"+request.getHeader("Referer");
    mailbody = mailbody + "\nFrom:"+request.getHeader("From");

    /* Prüfen auf angemeldeten User */

    if (request.getSession().getAttribute("user")!=null) {
        User user = (User)request.getSession().getAttribute("user");
        mailbody = mailbody + "\nUser:"+user.getUserName();
    } else {
        mailbody = mailbody + "\nUser: - NICHT ANGEMELDET -";
    }

    /* Pin Pic überprüfung */

    if (request.getSession().getAttribute("pinid")!=null) {
        Integer pin = (Integer)request.getSession().getAttribute("pinid");
        mailbody = mailbody + "\nPIN:"+pin;
    }

    WebStack webStack = new WebStack(request);

    if (exception!=null) {
        StringWriter s = new StringWriter();
        PrintWriter p = new PrintWriter(s);
        exception.printStackTrace(p);

        mailbody = mailbody + "\nKlasse: "+exception.getClass();
        mailbody = mailbody + "\nKlassname: "+exception.getClass().getName();
        mailbody = mailbody + "\n\nStacktrace: "+s.toString();
        mailbody = mailbody + "\n\nStacklistString: "+webStack.getStacklistString();
    } else {
        mailbody = mailbody + "\n\nKein Stacktrace verfuegbar";
    }

    /*
    try {
        MailWrapper.sendMail("mailgate.stumpner.net","franz@stumpner.net","franz@stumpner.net","HTTP500 - Report",mailbody);
    } catch (MessagingException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    System.out.print(mailbody);
       */

    //response.sendRedirect("/error/500");
%>
<jsp:include page="/error/undefinedWebState" flush="true"/>