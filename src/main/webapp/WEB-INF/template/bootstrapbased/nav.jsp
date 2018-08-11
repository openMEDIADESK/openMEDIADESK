<%@ page import="java.util.Enumeration,com.stumpner.mediadesk.usermanagement.User,com.stumpner.mediadesk.core.Config,org.apache.log4j.Logger"%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@page contentType="text/html;charset=utf-8"%><%  response.setHeader("Pragma", "no-cache"); %><%  response.setHeader("Cache-Control", "no-cache"); %><%  response.setHeader("Cache-Control","no-store" ); %><%  response.setDateHeader("Expires", 0); %>
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
              <c:if test="${fn:contains(config.param, '-LOGOL')}"><img src="/img/logo2016.png<c:out value="${cacheFix}"/>" alt="mediaDESK" title="mediaDESK" border="0" class="img-responsive"/></c:if>
              <c:if test="${!fn:contains(config.param, '-LOGOL')}"><img src="/logo2.png<c:out value="${cacheFix}"/>" alt="mediaDESK" title="mediaDESK" border="0" class="img-responsive"/></c:if>
          </a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <mediadesk:login>
          <ul class="nav navbar-nav">
            <c:if test="${showLightbox}">
            <li><a href="<c:url value="/${lng}/f"/>"><i class="fa fa-star fa-lg fa-fw"></i> <span class="label label-success" ng-bind="properties.favCount"></span></a></li>
            </c:if>
            <c:if test="${useShoppingCart}">
            <li><a href="<c:url value="/${lng}/shop"/>"><i class="fa fa-shopping-cart fa-lg fa-fw"></i> <span class="label label-success" ng-bind="properties.cartCount"></span></a></li>
            </c:if>
          </ul>
          </mediadesk:login>
          <ul class="nav navbar-nav navbar-right">
                <!-- Nicht angemeldet -->
            <mediadesk:login notLoggedIn="true">
            <c:forEach items="${navLinks}" var="menue">
                <c:url value="${menue.linkUrl}" var="linkUrl">
                    <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
                </c:url>
                <li><a href="<c:out value="${linkUrl}"/>" class="text-uppercase"><c:out value="${menue.title}"/></a></li>
            </c:forEach>
                <li><a href="<c:url value="/${lng}/login"/>" class="text-uppercase"><spring:message code="menu.login"/></a></li>
                <% if (Config.allowRegister) { %>
                <li><a href="<c:url value="/${lng}/register"/>" class="text-uppercase"><spring:message code="menu.register"/></a></li>
                <% } %>
            </mediadesk:login>
                <!-- ENDE Nicht angemeldet -->
            <c:if test="${showMenuUpload}">
            <c:url value="/${lng}/uploadweb" var="uploadUrl">
                <c:param name="catid" value="${folder.folderId}"/>
            </c:url>
            <li><a href="<c:out value="${uploadUrl}"/>"><i class="fa fa-cloud-upload fa-lg fa-fw md-text-prim"></i></a></li>
            </c:if>
            <c:if test="${showMenuPinmanager&&!showMenuUsermanager}"> <!-- Nur Pin-Manager Link zeigen, nicht Einstellungen -->
            <li><a href="<c:url value="/${lng}/pinlist"/>" title="Pin-Freigaben"><jsp:include page="iconPin.jsp"/></a></li>
            </c:if>
            <c:if test="${showMenuUsermanager}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="fa fa-cog fa-lg fa-fw md-nav-icon-setting"></i> <span class="caret"></span> <c:if test="${notifySettings}"><span class="badge md-badge-notify">!</span></c:if></a>
              <ul class="dropdown-menu">
                <c:if test="${showMenuPinmanager}"><li><a href="<c:url value="/${lng}/pinlist"/>"><jsp:include page="iconPin.jsp"/> <spring:message code="menu.pinlist"/></a></li></c:if>
                <c:if test="${showMenuSettings}"><li><a href="<c:url value="/${lng}/stat"/>"><i class="fa fa-bar-chart fa-fw"></i> <spring:message code="menu.stats"/></a></li></c:if>
                <c:if test="${showMenuUsermanager}"><li><a href="<c:url value="/${lng}/usermanager"/>"><i class="fa fa-users fa-fw"></i> <spring:message code="menu.usermanager"/></a></li></c:if>
                <c:if test="${showMenuSettings}"><li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/> <c:if test="${notifySettings}"><span class="badge">!</span></c:if></a></li></c:if>
                  <!--
                <li role="separator" class="divider"></li>
                <li><a href="#"><i class="fa fa-newspaper-o fa-fw"></i> Systemnews</a></li>
                  -->
              </ul>
            </li>
            </c:if>
            <mediadesk:login>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="fa fa-user fa-lg fa-fw md-nav-icon-user"></i>&nbsp;<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li class="dropdown-header"><spring:message code="loggedin.as"/></li>
              	<li class="disabled"><a href="#"><i class="fa fa-user fa-lg fa-fw md-nav-icon-user"></i> <span class="text-success" ng-bind="properties.username"></span></a></li>
                <li role="separator" class="divider"></li>
                <!-- <li><a href="<c:url value="password"/>"><i class="fa fa-pencil fa-fw"></i> Daten ändern</a></li>-->
                <li><a href="<c:url value="password"/>"><i class="fa fa-key fa-fw"></i> <spring:message code="menu.setpassword"/></a></li>
                <li role="separator" class="divider"></li>
                <li><a href="<c:url value="login?logout="/>"><jsp:include page="iconLogout.jsp"/> <span class="text-danger"><spring:message code="menu.logout"/></span></a></li>
              </ul>
            </li>
            </mediadesk:login>
            <form class="navbar-form navbar-left" role="search" method="post" action="<c:url value="/${lng}/search"/>">
        	<div class="input-group">
          	<input type="text" class="form-control md-searchfield-nav" name="q" id="sTextKeyword" placeholder="<spring:message code="mediasearch.imagesearch"/>" accesskey="s">
            <span class="input-group-btn">
        	<button type="submit" class="btn btn-default"><i class="fa fa-search fa-fw"></i></button>
            </span>
            </div>
      		</form>
            <%
                if (Config.multiLang==true) {
            %>
                <c:url var="setLangDe" value="${dePage}">
                </c:url>
                <c:url var="setLangEn" value="${enPage}">
                </c:url>
            <!-- sprache -->
          	<li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="text-uppercase"><c:out value="${lng}"/></span>&nbsp;<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="<c:out value="${setLangDe}"/>">Deutsch</a></li>
                <li role="separator" class="divider"></li>
                <li><a href="<c:out value="${setLangEn}"/>">English</a></li>
              </ul>
            </li>
            <!-- /sprache -->
            <%
                }
            %>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
    <!-- /Fixierte Navbar -->
<!-- NAVBAR ############################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
<!-- SUCHE BEI SCHMALEM BILDSCHIRM ######################################################################################################################## -->
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 hidden-sm hidden-md hidden-lg hidden-print">

        <c:if test="${empty searchString}">
    <!-- SUCHFORM -->
    <form role="search" method="post" action="<c:url value="/${lng}/search"/>">
	<div class="input-group">
        <!--
          <span class="input-group-btn">
            <button class="btn btn-default input-lg" type="button"><i class="fa fa-cogs fa-fw"></i></button>
          </span> -->
      <input type="text" class="form-control input-lg" name="q" id="sTextKeyword" placeholder="<spring:message code="mediasearch.imagesearch"/>" accesskey="s">
          <span class="input-group-btn">
            <button class="btn btn-default input-lg" type="submit"><i class="fa fa-search fa-fw"></i></button>
          </span>      
    </div>    
    </form>        
    <!-- /SUCHFORM -->
        <!-- mediadesk abstand -->
        <div class="md-space">&nbsp;</div>
        <!-- /mediadesk abstand -->

    </c:if>
        
        </div>
    </div>
</div>

<!-- /SUCHE BEI SCHMALEM BILDSCHIRM ####################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
