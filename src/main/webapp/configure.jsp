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
<%@ page import="com.stumpner.mediadesk.core.Config" %>
<%@ page import="com.stumpner.mediadesk.core.WebContextListener" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.FileWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (!Config.isConfigured) {

        if (request.getParameter("instanceName")!=null) {
             // Konfiguration schreiben:
            File cFile = new File(WebContextListener.configFile);
            FileWriter confWriter = new FileWriter(cFile);
            Properties conf = new Properties();

            conf.put("instanceName",request.getParameter("instanceName"));
            conf.put("db.jdbcDriver",request.getParameter("db.jdbcDriver"));
            conf.put("db.connectionUrl",request.getParameter("db.connectionUrl").replaceAll("#instanceName#",request.getParameter("instanceName")));
            conf.put("db.username",request.getParameter("db.username").replaceAll("#instanceName#",request.getParameter("instanceName")));
            conf.put("db.password",request.getParameter("db.password").replaceAll("#instanceName#",request.getParameter("instanceName")));
            conf.put("imageStorePath",request.getParameter("imageStorePath").replaceAll("#instanceName#",request.getParameter("instanceName")));
            conf.put("fileSystemImportPath",request.getParameter("fileSystemImportPath").replaceAll("#instanceName#",request.getParameter("instanceName")));
            conf.put("lic",request.getParameter("lic"));
            conf.put("licMaxMb","150102000"); //150G
            conf.put("httpBase",request.getParameter("httpBase"));
            conf.put("rss","true");
            conf.put("maxFileSize","15102000"); // 15G
            conf.put("pinPicEnabled","true");
            conf.put("multiLang","true");

            conf.store(confWriter,"mediaDESK-Config File");

            confWriter.close();

            WebContextListener wcl = new WebContextListener();
            wcl.setupMediaDESK(conf,pageContext.getServletContext());
            //wcl.doAndReloadConfiguration(pageContext.getServletContext());

            out.println("Config initalized... <a href=\"/\">start...</a>");

        } else {

            String instance = "j8xxx";

            if (request.getServerName().indexOf(".")!=-1) {
                instance = request.getServerName().substring(0,request.getServerName().indexOf("."));
                try {
                    Integer.valueOf(instance);
                    //Wenn der erste Teil nur aus einer Zahl besteht, handelt es sich womöglich um die IP-Adresse, dann mediadesk als Instanzname verwenden
                    instance = "mediadesk";
                } catch (NumberFormatException e) {

                }
            }

            request.setAttribute("instance", instance);

%>
<html lang="de">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Die 3 Meta-Tags oben *müssen* zuerst im head stehen; jeglicher sonstiger head-Inhalt muss *nach* diesen Tags kommen -->
    <meta name="keywords" content="<%= Config.webKeywords %>">
    <meta name="description" content="<%= Config.webDescription %>">
    <link rel="icon" href="/favicon.ico"><!-- TODO -->
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <!-- bei buttons die nur icons enthalten noch: <span class="sr-only">Text</span> einfügen als hilfe -->
    <title><c:if test="${webSiteTitle==''}"><c:out value="${config.webTitle}"/></c:if><c:out value="${webSiteTitle}"/></title>
    <!-- Bootstrap-CSS -->
    <!-- !!ACHTUNG: verwende hier .min.css da das andere file mit padding im body arbeitet! -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
    <style type="text/css">
        <%= Config.cssAdd %>
    </style>
    <%= Config.googleWebmasters %>
    <title>openMEDIADESK Setup</title>
</head>
  <body>

  <!-- ###################################################################################################################################################### -->
  <!-- NAVBAR ############################################################################################################################################### -->
  <!-- Fixierte Navbar -->
  <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container-fluid">
          <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                  <span class="sr-only">Navigation ein-/ausblenden</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="<c:url value="${home}"/>">
                  <img src="/logo2.png<c:out value="${cacheFix}"/>" alt="mediaDESK" title="mediaDESK" border="0" class="img-responsive"/>
              </a>
          </div>
          <div id="navbar" class="navbar-collapse collapse">
              <ul class="nav navbar-nav navbar-right">

                  <li><a href="https://openmediadesk.org" target="_blank">openMEDIADESK.org</a></li>

                  <!-- sprache -->
                  <li class="dropdown">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="text-uppercase"><c:out value="${lng}"/></span>&nbsp;<span class="caret"></span></a>
                      <ul class="dropdown-menu">
                          <li><a href="https://wiki.openmediadesk.net" target="_blank" class="text-uppercase">Wiki &amp; How-Tos</a></li>
                          <li><a href="http://get.openmediadesk.net" target="_blank" class="text-uppercase">Downloads</a></li>
                          <li><a href="https://github.com/openMEDIADESK/openMEDIADESK" class="text-uppercase">GIT Repository</a></li>
                          <li role="separator" class="divider"></li>
                          <li><a href="https://github.com/openMEDIADESK/openMEDIADESK/releases/tag/<%= Config.versionNumbner %>" target="_blank">v<%= Config.versionNumbner %></a></li>
                      </ul>
                  </li>
                  <!-- /sprache -->
              </ul>
          </div><!--/.nav-collapse -->
      </div>
  </nav>
  <!-- /Fixierte Navbar -->

  <h1>&nbsp;</h1>

  <div class="container-fluid">

      <div class="row">

          <div class="col-lg-12">



  <h1>Setup openMEDIADESK</h1>

  <form action="/configure.jsp" method="POST">

      <div class="form-group">
          <label for="instanceName">Instance-Name</label>
          <input id="instanceName" type="text" class="form-control" name="instanceName" size="100" value="<c:out value="${instance}"/>"/>
      </div>

      <div class="form-group">
          <label for="jdbcDriver">db.jdbcDriver</label>
          <input id="jdbcDriver" type="text" class="form-control" name="db.jdbcDriver"  size="100"  width="200" value="com.mysql.jdbc.Driver"/>
      </div>

      <div class="form-group">
          <label for="connectionUrl">db.connectionUrl</label>
          <input id="connectionUrl" type="text" class="form-control" name="db.connectionUrl"  size="100"  width="200" value="jdbc:mysql://localhost:3306/<c:out value="${instance}"/>?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8"/>
      </div>

      <div class="form-group">
          <label for="dbUsername">db.username</label>
          <input id="dbUsername" class="form-control" type="text" name="db.username"  size="100"  value="root"/>
      </div>

      <div class="form-group">
          <label for="dbPassword">db.password</label>
          <input id="dbPassword" type="text" class="form-control" name="db.password" width="200"  size="100"  value="mediadesk"/>
      </div>

      <div class="form-group">
          <label for="imageStorePath">storePath</label>
          <input id="imageStorePath" type="text" class="form-control" name="imageStorePath" width="200" size="100" value="/srv/datastore/<c:out value="${instance}"/>/repository/"/>
      </div>

      <div class="form-group">
          <label for="fileSystemImportPath">fileSystemImportPath</label>
          <input id="fileSystemImportPath" type="text" class="form-control" name="fileSystemImportPath" width="200" size="100" value="/srv/datastore/<c:out value="${instance}"/>/upload/"/>
      </div>

      <div class="form-group">
          <label for="lic">lic</label>
          <input id="lic" type="text" class="form-control" name="lic" width="200" value=""/>
      </div>

      <div class="form-group">
          <label for="lic">httpBase</label>
          <input type="text" name="httpBase" class="form-control" width="200" size="100" value="https://<%= request.getServerName() %>"/>
      </div>

      <input type="submit" value="Start installation" class="btn btn-primary"/>

  </form>

          </div>

      </div> <!-- //ROW -->

  </div>


<!-- Bootstrap-JavaScript -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<!--
mediaDESK Version: <%= Config.versionNumbner %>
mediaDESK VerDate: <%= Config.versionDate %>
-->

<%= Config.statCounterCode %>
<%= Config.googleAnalytics %>

</body>
</html>


<%
        }
    } else {
        out.println("This mediaDESK is already configured...");
    }
%>