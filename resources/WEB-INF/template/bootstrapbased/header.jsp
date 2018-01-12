<%@ page import="java.util.Enumeration,com.stumpner.mediadesk.usermanagement.User,com.stumpner.mediadesk.core.Config,org.apache.log4j.Logger"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@page contentType="text/html;charset=utf-8"%><%  response.setHeader("Pragma", "no-cache"); %><%  response.setHeader("Cache-Control", "no-cache"); %><%  response.setHeader("Cache-Control","no-store" ); %><%  response.setDateHeader("Expires", 0); %><!DOCTYPE html>
<html lang="de" ng-app="ui.mediadesk">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Die 3 Meta-Tags oben *müssen* zuerst im head stehen; jeglicher sonstiger head-Inhalt muss *nach* diesen Tags kommen -->
    <meta name="keywords" content="<%= Config.webKeywords %>">
    <meta name="description" content="<%= Config.webDescription %>">
    <meta name="generator" content="openMEDIADESK - https://github.com/stumpner/openMEDIADESK version 8b6e4d18672ae6b97ba68dda2b69031a506ee457">
    <jsp:include page="headerOpenGraph.jsp"/>
    <!-- favicons -->
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png<c:out value="${cacheFix}"/>">
    <link rel="icon" type="image/png" href="/favicon-32x32.png<c:out value="${cacheFix}"/>" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png<c:out value="${cacheFix}"/>" sizes="16x16">
    <link rel="manifest" href="/manifest.json<c:out value="${cacheFix}"/>">
    <link rel="mask-icon" href="/safari-pinned-tab.svg<c:out value="${cacheFix}"/>" color="#5bbad5">
    <link rel="shortcut icon" href="/favicon.ico<c:out value="${cacheFix}"/>">
    <meta name="apple-mobile-web-app-title" content="mediaDESK">
    <meta name="application-name" content="mediaDESK">
    <meta name="theme-color" content="#ffffff">
    <!-- \favicons -->
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <!-- bei buttons die nur icons enthalten noch: <span class="sr-only">Text</span> einfügen als hilfe -->
    <title><c:if test="${webSiteTitle==''}"><c:out value="${config.webTitle}"/></c:if><c:out value="${webSiteTitle}"/></title>
    <!-- Bootstrap-CSS -->
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/normalize.css" rel="stylesheet">
    <!-- mediaDESK CSS -->
    <link href="/css/mediadesk.css<c:out value="${cacheFix}"/>" rel="stylesheet">
    <!-- ngDialog CSS http://likeastore.github.io/ngDialog/ -->
    <link rel="stylesheet" href="/css/ngDialog.css">
    <link rel="stylesheet" href="/css/ngDialog-theme-plain.css">
    <!-- Chart -->
    <link rel="stylesheet" href="/app/lib/angular/angular-chart.css">
    <!-- angular JS Toaster https://github.com/jirikavi/AngularJS-Toaster -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/angularjs-toaster/1.1.0/toaster.min.css" rel="stylesheet" />
    <!-- ng-wig WYSIWYG Editor https://github.com/stevermeister/ngWig -->
    <link href="/app/lib/ng-wig/css/ng-wig.min.css" rel="stylesheet"/>
<style type="text/css">
<%= Config.cssAdd %>
</style>
<%= Config.googleWebmasters %>
</head>

<!--
Debug cacheFix = <c:out value="${cacheFix}"/>
-->

<body ng-controller="MainCtrl" ng-init="properties={lng:'<c:out value="${lng}"/>',loggedin:<mediadesk:login>true</mediadesk:login><mediadesk:login notLoggedIn="true">false</mediadesk:login>,username:'<c:out value="${loggedInUser.userName}"/>',role:<c:out value="${loggedInUser.role}"/>,cartCount:<c:out value="${shoppingCartCount}"/>,favCount:<c:out value="${lightboxCount}"/>}" ng-cloak>

<jsp:include page="nav.jsp"/>

<!-- ################################################################ -->
<!-- ##### SEITENINHALT MIT container und row für die 2 spalten ##### -->
<div class="container-fluid"> <!-- container mit der row für spalte 1 und spalte 2 -->

<div class="row"> <!-- row in der SPALTE 1 und SPALTE 2 sind -->
<!-- spalte1 -->
<div class="col-sm-2 hidden-xs hidden-print">
<!-- BAUM -->
<jsp:include page="foldertree.jsp"/>
<!-- /BAUM -->
</div>
<!-- /spalte1 -->