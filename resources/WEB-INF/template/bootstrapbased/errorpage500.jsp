<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="javax.mail.MessagingException" %>
<%@ page import="com.stumpner.mediadesk.usermanagement.User" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="com.stumpner.mediadesk.web.stack.WebStack" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.stumpner.mediadesk.util.MailWrapper" %>
<%@ page import="java.util.Date" %>
<%@page isErrorPage="true" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title></title>
<script	type="text/javascript" src="/center.js"></script>
<link rel="stylesheet" href="/css/errorpage.css" type="text/css">
</head>
<body>

<div id="errorpage">

    <div class="errorLogo">
        <a href="/"><img src="/logo2.png" alt="instancename" border="0"></a>
    </div>

    <div class="errorMessage">
        <h1>HTTP STATUS 500 - Server Error!</h1>
        <p><spring:message code="error.500"/></p>
        <p><a href="/"><spring:message code="error.startpage"/></a></p>
        <p>Error-Codes: <%= request.getAttribute("javax.servlet.error.exception") %></p>
        <a href="mailto:<%= Config.mailsender %>"><%= Config.mailsender %></a>
    </div>

</div>

<!-- BEGIN Podio web form -->
<a href="https://podio.com/webforms/11630320/810315">Hier klicken um den Fehler an die Entwickler zu senden!</a>
<!-- END Podio web form -->

</body>
</html>
<%

    MailWrapper.sendErrorReport(request,exception,"");

%>