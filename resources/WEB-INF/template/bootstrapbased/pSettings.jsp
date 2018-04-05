<%@page contentType="text/html;charset=utf-8" language="java" %>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>

<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="ThumbnailViewCtrl" ng-init="initMosView('/api/rest/folder',<c:out value="${containerId}"/>,'<%= request.getAttribute("listView") %>',<c:out value="${selectedImages}"/>)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.system"/></h3>
<!-- /ordnertitel und infos -->

<c:if test="${todoWebtitle || todoLogo || todoEmail}">
<div class="alert alert-warning" role="alert">

    <strong>Grundeinstellungen noch nicht abgeschlossen!</strong> <br/>

    <c:if test="${todoEmail}">Stellen Sie eine Emailadresse f&uuml;r Benachrichtigungen ein: gehen Sie jetzt zu den  <a href="<c:url value="setmail"/>" class="alert-link">E-Mail Einstellungen</a> um das jetzt zu tun.</c:if>

    <c:if test="${todoLogo}">Sie haben noch kein Logo hochgeladen: <a href="<c:url value="setlogo2"/>" class="alert-link">klicken Sie hier</a> um das jetzt zu tun! <br/></c:if>

    <c:if test="${todoWebtitle}">Stellen Sie einen Web-Titel ein - das kann auch Ihr Firmenname sein - um ihn auf der Seite und im Footer anzuzeigen: <a href="<c:url value="setweb"/>" class="alert-link">klicken Sie hier</a> um das jetzt zu tun!</c:if>

</div>
</c:if>

<div class="list-group">
    <a href="<c:url value="setapplication"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.application"/></h4>
        <p class="list-group-item-text"><spring:message code="set.application.info"/></p>
    </a>

    <a href="<c:url value="setcustomfields"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.customfields"/></h4>
        <p class="list-group-item-text"><spring:message code="set.customfields.info"/></p>
    </a>

    <a href="<c:url value="setformat"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.format"/></h4>
        <p class="list-group-item-text"><spring:message code="set.format.info"/></p>
    </a>

    <a href="<c:url value="settext"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.text"/></h4>
        <p class="list-group-item-text"><spring:message code="set.text.info"/></p>
    </a>

    <a href="<c:url value="setmail"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.mail"/></h4>
        <p class="list-group-item-text"><spring:message code="set.mail.info"/></p>
    </a>

    <a href="<c:url value="setwatermark"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.watermark"/></h4>
        <p class="list-group-item-text"><spring:message code="set.watermark.info"/></p>
    </a>

    <a href="<c:url value="setweb"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.web"/></h4>
        <p class="list-group-item-text"><spring:message code="set.web.info"/></p>
    </a>

    <a href="<c:url value="setmeta"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.meta"/></h4>
        <p class="list-group-item-text"><spring:message code="set.meta.info"/></p>
    </a>

    <a href="<c:url value="setimport"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.import"/></h4>
        <p class="list-group-item-text"><spring:message code="set.import.info"/></p>
    </a>

    <a href="<c:url value="setlanguage"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.lang"/></h4>
        <p class="list-group-item-text"><spring:message code="set.lang.info"/></p>
    </a>

    <a href="<c:url value="setmenu"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.menu"/></h4>
        <p class="list-group-item-text"><spring:message code="set.menu.info"/></p>
    </a>

    <c:if test="${fn:contains(config.param, '-PLG')}">
    <a href="<c:if test="${fn:contains(licFunc, '-plugin')}"><c:url value="setplugin"/></c:if><c:if test="${!fn:contains(licFunc, '-plugin')}"><c:url value="https://openmediadesk.net/signup/premium.do?id=${licId}&url=${url}"/>" target="_blank</c:if>" class="list-group-item">
        <h4 class="list-group-item-heading">Plugin Einstellungen <c:if test="${!fn:contains(licFunc, '-plugin')}"><button class="btn btn-warning">Upgrade!</button></c:if></h4>
        <p>Schnittstellen zu Dienste und Services wie Virenscanner, ...</p>
    </a>
    </c:if>

    <a href="<c:url value="setmaintenance"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="menu.maintenance"/></h4>
        <p class="list-group-item-text"><spring:message code="set.maintenance"/></p>
    </a>

    <a href="<c:if test="${fn:contains(licFunc, '-noads')}"><c:url value="setformat"/></c:if><c:if test="${!fn:contains(licFunc, '-noads')}"><c:url value="https://openmediadesk.net/signup/premium.do?id=${licId}&url=${url}"/>" target="_blank</c:if>" class="list-group-item">
        <h4 class="list-group-item-heading">Werbung im Footer entfernen <c:if test="${!fn:contains(licFunc, '-noads')}"><button class="btn btn-warning">Upgrade!</button></c:if></h4>
        <p class="list-group-item-text">Die Links auf openMEDIADESK.org im Footer und in den Emails entfernen.</p>
    </a>

</div>
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<h3><spring:message code="set.layout"/></h3>
<!-- /ordnertitel und infos -->
<div class="list-group">

    <a href="<c:if test="${fn:contains(licFunc, '-template')}"><c:url value="settemplates"/></c:if><c:if test="${!fn:contains(licFunc, '-template')}"><c:url value="https://openmediadesk.net/signup/premium.do?id=${licId}&url=${url}"/>" target="_blank</c:if>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.template"/> <c:if test="${!fn:contains(licFunc, '-template')}"><button class="btn btn-warning">Upgrade!</button></c:if> </h4>
        <p class="list-group-item-text"><spring:message code="set.template.info"/></p>
    </a>

    <a href="<c:url value="setcss"/>" class="list-group-item">
        <h4 class="list-group-item-heading">Logo, <spring:message code="set.css"/></h4>
        <p class="list-group-item-text"><spring:message code="set.css.info"/></p>
    </a>
</div>
<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>