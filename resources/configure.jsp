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
            conf.put("maxImageSize","15102000"); // 15G
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
<html>
  <head><title>openMEDIADESK Configuration</title></head>
  <body>

  <h1>Configure your mediaDESK here:</h1>

  <form action="/configure.jsp" method="GET">

      <dt>instanceName:</dt>
      <dd>
          <input type="text" name="instanceName" size="100" value="<c:out value="${instance}"/>"/>
      </dd>

      <!--
      <dt>licId (Podio):</dt>
      <dd>
          <input type="text" name="licId" size="5" value=""/>
      </dd>-->

      <dt>db.jdbcDriver:</dt>
      <dd>
          <input type="text" name="db.jdbcDriver"  size="100"  width="200" value="com.mysql.jdbc.Driver"/>
      </dd>

      <dt>db.connectionUrl:</dt>
      <dd>
          <input type="text" name="db.connectionUrl"  size="100"  width="200" value="jdbc:mysql://localhost:3306/<c:out value="${instance}"/>?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8"/>
      </dd>

      <dt>db.username:</dt>
      <dd>
          <input type="text" name="db.username"  size="100"  value="root"/>
      </dd>

      <dt>db.password:</dt>
      <dd>
          <input type="text" name="db.password" width="200"  size="100"  value="mediadesk"/>
      </dd>

      <dt>imageStorePath:</dt>
      <dd>
          <input type="text" name="imageStorePath" width="200" size="100" value="/srv/datastore/<c:out value="${instance}"/>/repository/"/>
      </dd>

      <dt>fileSystemImportPath:</dt>
      <dd>
          <input type="text" name="fileSystemImportPath" width="200" size="100" value="/srv/datastore/<c:out value="${instance}"/>/upload/"/>
      </dd>

      <dt>lic:</dt>
      <dd>
          <input type="text" name="lic" width="200" value=""/>
      </dd>

      <dt>httpBase</dt>
      <dd>
          <input type="text" name="httpBase" width="200" size="100" value="https://<%= request.getServerName() %>"/>
      </dd>

      <input type="submit"/>

  </form>

  </body>
</html>
<%
        }
    } else {
        out.println("This mediaDESK is already configured...");
    }
%>