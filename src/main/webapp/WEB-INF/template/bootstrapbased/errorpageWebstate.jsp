<%@ page import="com.stumpner.mediadesk.core.Config"%><%@page isErrorPage="true" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title></title>
<script	type="text/javascript" src="/center.js"></script>
<link rel="stylesheet" href="/css/errorpage.css" type="text/css">
<BASE href="<%= Config.httpBase %>/">
</head>
<body>

<div id="errorpage">

    <div class="errorLogo">
        <a href="/"><img src="<%= Config.instanceLogo %>" alt="instancename" border="0"></a>
    </div>

    <div class="errorMessage">
        <h1><spring:message code="error.webstatehead"/> </h1>
        <p>
        <spring:message code="error.webstate"/>
        </p>
        <p><a href="/"><spring:message code="error.startpage"/></a></p>
        <a href="mailto:<%= Config.mailsender %>"><%= Config.mailsender %></a>
    </div>

</div>

</body>
</html>