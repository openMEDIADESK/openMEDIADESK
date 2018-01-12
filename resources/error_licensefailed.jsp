<%@ page isErrorPage="true"%>
<%@ page import="com.stumpner.mediadesk.core.Config,
                 com.stumpner.mediadesk.util.MailWrapper,
                 java.io.PrintWriter,
                 java.io.StringWriter,
                 java.util.Date"%>
<%@ page import="com.stumpner.mediadesk.web.stack.WebStack"%>
<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="javax.mail.MessagingException"%>
<%@ page import="com.stumpner.mediadesk.lic.License" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<html>
  <head><title>mediaDESK - License Failure</title></head>
  <body>

  <h1>Lizenzfehler</h1>

  <p>
      Diese mediaDESK&reg;-Datenbank ist nicht lizenziert:
  </p>

  <pre><code>
      Lizenziert an: <%= License.getTo() %>
      IP: <%= License.getIp() %>
      Domain1: <%= License.getDomain1() %>
      Domain2: <%= License.getDomain2() %>
      Expires: <%= License.getDate().getTime() %>

      Mehr Informationen unter http://www.stumpner.net
  </code></pre>

  </body>
</html>