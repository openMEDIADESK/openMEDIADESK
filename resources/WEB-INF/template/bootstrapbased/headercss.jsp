<%@ page import="java.util.Enumeration,com.stumpner.mediadesk.usermanagement.User,com.stumpner.mediadesk.core.Config,org.apache.log4j.Logger"%>
<%@ page import="com.stumpner.mediadesk.web.template.TemplateService" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@page contentType="text/html;charset=utf-8"%>
<!-- Include Ext stylesheets here: -->
<link rel="stylesheet" type="text/css" href="/js/ext-2.1/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="/js/ext-2.1/resources/css/xtheme-gray.css">
<link rel="stylesheet" type="text/css" href="/css/presets/extjs-tree.css"/>
<%
    for (String customCssFilename : TemplateService.customCssFiles) {

%><link rel="stylesheet" type="text/css" href="/css/template/<%= Config.customTemplate %>/<%= customCssFilename %>"><%
        
    }
%>
<%
    if (Config.cssTheme.equalsIgnoreCase("none")) {
%>
<% } else { %>
<link rel="stylesheet" href="/css/<%= Config.cssTheme %>?modified=<%= Config.versionDate + config.getServletContext().getAttribute("cssmod") %>" type="text/css">
<% } %>
<%
    if (Config.redirectStartPage.equalsIgnoreCase("/index/") && Config.rss ) { %>
<link rel="alternate" type="text/xml" title="RSS .92" href="/rss/folder.xml">
<%  }  %>