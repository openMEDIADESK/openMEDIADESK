<%@ page import="java.util.Enumeration,
                 java.util.List,
                 com.stumpner.mediadesk.image.folder.Folder"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.image.ImageVersion" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="EditCtrl" ng-init="init('/api/rest/mo/<c:out value="${command.imageVersion.ivid}"/>/editmode')"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-tag fa-fw"></i> <spring:message code="imageedit.headline"/> <c:out value="${command.imageVersion.versionName}"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>
    <spring:message code="imageedit.headline"/>
        <div class="btn-group">
            <label class="btn btn-primary" ng-model="showDe" uib-btn-checkbox>DE</label>
            <label class="btn btn-primary" ng-model="showEn" uib-btn-checkbox>EN</label>
        </div>
</h3>
<spring:bind path="command.imageVersion.versionName">
<h4><c:out value="${status.value}"/> <a href="#" ng-click="infoMediaPopup();"><i class="fa fa-info-circle fa-lg" aria-hidden="true"></i></a></h4>
</spring:bind>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- START EDIT MEDIA OBJECT -->

<div class="row">
	<div class="col-sm-3 md-keywording-img">
        <a href="#" data-toggle="modal" data-target="#meinModal"><img src="/imageservlet/<c:out value="${command.imageVersion.ivid}"/>/1/image.jpg"></a>
        <div ng-controller="TimeAgoCtrl" ng-init="setDate(<c:out value="${command.imageVersion.createDate.time}"/>)">
            <spring:message code="imageedit.by"/> <c:out value="${command.creator.userName}"/>
            <a href="#" tooltip-animation="true" uib-tooltip="<dt:format pattern="dd MMMM yyyy, HH:mm" default=""><c:out value="${command.imageVersion.createDate.time}"/></dt:format>">{{dt | timeago}}</a>
            <!--<a href="#" ng-click="infoMediaPopup();"><i class="fa fa-info-circle" aria-hidden="true"></i></a>-->
        </div>
        <div>

        Media Number #: <c:out value="${command.imageVersion.imageNumber}"/><br/>

        </div>
    </div>
	<div class="col-sm-8">
	<!-- FORMS FÜR EDIT -->


    <form id="imageedit" method="post" action="<c:url value="mediadetailedit"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.imageVersion.versionName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textName"><spring:message code="imageedit.name"/></label>
        <input type="text" ng-model="data.name" ng-change="nameChanged()" class="form-control input-sm" id="textName" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <div class="form-group text-right">
        <!--
        <div class="btn-group">
            <label class="btn btn-primary" ng-model="showDe" uib-btn-checkbox>DE</label>
            <label class="btn btn-primary" ng-model="showEn" uib-btn-checkbox>EN</label>
        </div>
        -->
    </div>

    <!--
    ng-class="$variableToEvaluate ? 'class-if-true' : 'class-if-false'"
    -->
    <div class="row">
    <spring:bind path="command.imageVersion.versionTitleLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng1"><spring:message code="imageedit.title"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionTitleLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitleLng1}"> checked="true"</c:if>>
        <input type="text" ng-model="data.titleLng1" ng-change="title1Changed()" class="form-control input-sm" id="textTitleLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.imageVersion.versionTitleLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="imageedit.title"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionTitleLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitleLng2}"> checked="true"</c:if>>
        <input type="text" ng-model="data.titleLng2" ng-change="title2Changed()" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>
    </c:if>
    </div>

    <c:if test="${command.imageVersion.mayorMime=='audio' || command.imageVersion.mayorMime=='video'}">
        <!-- audio video daten -->

        <spring:bind path="command.imageVersion.artist">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="imageedit.artist"/></label> &nbsp;<input type="checkbox" name="copyfield" value="artist" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitle}"> checked="true"</c:if>>
        <input type="text" ng-model="title2" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

        <spring:bind path="command.imageVersion.album">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="imageedit.album"/></label> &nbsp;<input type="checkbox" name="copyfield" value="album" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitle}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

        <spring:bind path="command.imageVersion.genre">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="imageedit.genre"/></label> &nbsp;<input type="checkbox" name="copyfield" value="genre" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitle}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

    </c:if>

    <div class="row">
    <spring:bind path="command.imageVersion.versionSubTitleLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textSubTitleLng1"><spring:message code="imageedit.subtitle"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionSubTitleLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySubTitleLng1}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textSubTitleLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.imageVersion.versionSubTitleLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textSubTitleLng2"><spring:message code="imageedit.subtitle"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionSubTitleLng2" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySubTitleLng2}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textSubTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>        
    </c:if>
    </div>

    <div class="row">
    <spring:bind path="command.imageVersion.infoLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textInfoLng1"><spring:message code="imageedit.info"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="infoLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyInfoLng1}"> checked="true"</c:if>>
        <!--<input type="text" class="form-control input-sm" id="textInfoLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>-->
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.imageVersion.infoLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textInfoLng2"><spring:message code="imageedit.info"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="infoLng2" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyInfoLng2}"> checked="true"</c:if>>
        <!-- <input type="text" class="form-control input-sm" id="textInfoLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>> -->
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>
    </c:if>
    </div>

    <div class="row">
    <spring:bind path="command.imageVersion.siteLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.site"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="siteLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySiteLng1}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.imageVersion.siteLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.site"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="siteLng2" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySiteLng2}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>        
    </c:if>
    </div>

    <!--
    <spring:bind path="command.imageVersion.photographDate">              
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.photographdate"/></label> &nbsp;<input type="checkbox" name="copyfield" value="photographDate" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPhotographDate}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    -->

    <spring:bind path="command.imageVersion.photographDate">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>" ng-controller="DatepickerCtrl">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.photographdate"/></label> &nbsp;<input type="checkbox" name="copyfield" value="photographDate" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPhotographDate}"> checked="true"</c:if>>
            <p class="input-group">
        <input type="text" class="form-control" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" ng-init="setDate(<fmt:formatDate value="${command.imageVersion.photographDate}" pattern="yyyy,M,dd"/>)" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="true" alt-input-formats="altInputFormats" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <span class="input-group-btn">
            <button type="button" class="btn btn-default" ng-click="openDatepicker()"><i class="glyphicon glyphicon-calendar"></i></button>
        </span>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
            </p>
        </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.photographerAlias">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.photographer"/></label> &nbsp;<input type="checkbox" name="copyfield" value="photographerAlias" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPhotographer}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.byline">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.byline"/></label> &nbsp;<input type="checkbox" name="copyfield" value="byline" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyByline}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.keywords">
        <div class="form-group">
            <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.keywords"/></label> &nbsp;<input type="checkbox" name="copyfield" value="keywords" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyKeywords}"> checked="true"</c:if>>
            <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
        </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.people">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.people"/></label> &nbsp;<input type="checkbox" name="copyfield" value="people" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPeople}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <div class="form-group">
        <label for="selOrientation"><spring:message code="imageedit.orientation"/></label> &nbsp;<input type="checkbox" name="copyfield" value="orientation" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyOrientation}"> checked="true"</c:if>>
        <spring:bind path="command.imageVersion.orientation">
        <select class="form-control" id="selOrientation" name="imageVersion.orientation">
                          <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="imageedit.orientation.undefined"/></option>
                          <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="imageedit.orientation.horizontal"/></option>
                          <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="imageedit.orientation.panoramic"/></option>
                          <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="imageedit.orientation.square"/></option>
                          <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="imageedit.orientation.vertical"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="form-group">
        <label for="selPerspective"><spring:message code="imageedit.perspective"/></label> &nbsp;<input type="checkbox" name="copyfield" value="perspective" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPerspective}"> checked="true"</c:if>>
        <spring:bind path="command.imageVersion.perspective">
        <select class="form-control" id="selPerspective" name="imageVersion.perspective">
                        <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="imageedit.perspective.undefined"/></option>
                        <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="imageedit.perspective.topview"/></option>
                        <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="imageedit.perspective.bottomview"/></option>
                        <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="imageedit.perspective.sideview"/></option>
                        <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="imageedit.perspective.frontview"/></option>
                        <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="imageedit.perspective.backview"/></option>
                        <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="imageedit.perspective.aerialview"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="form-group">
        <label for="selMotive"><spring:message code="imageedit.motive"/></label> &nbsp;<input type="checkbox" name="copyfield" value="motive" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyMotive}"> checked="true"</c:if>>
        <spring:bind path="command.imageVersion.motive">
        <select class="form-control" id="selMotive" name="imageVersion.motive">
                         <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="imageedit.motive.undefined"/></option>
                         <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="imageedit.motive.portrait"/></option>
                         <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="imageedit.motive.group"/></option>
                         <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="imageedit.motive.overview"/></option>
                         <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="imageedit.motive.feature"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="form-group">
        <label for="selGesture"><spring:message code="imageedit.gesture"/></label> &nbsp;<input type="checkbox" name="copyfield" value="gesture" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyGesture}"> checked="true"</c:if>>
        <spring:bind path="command.imageVersion.gesture">
        <select class="form-control" id="selGesture" name="imageVersion.gesture">
                            <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="imageedit.gesture.undefined"/></option>
                            <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="imageedit.gesture.jubilate"/></option>
                            <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="imageedit.gesture.bright"/></option>
                            <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="imageedit.gesture.sad"/></option>
                            <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="imageedit.gesture.sceptic"/></option>
                            <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="imageedit.gesture.screaming"/></option>
                            <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="imageedit.gesture.neutral"/></option>
                            <option value="7"<c:if test="${status.value==7}"> selected</c:if>><spring:message code="imageedit.gesture.friendly"/></option>
                            <option value="8"<c:if test="${status.value==8}"> selected</c:if>><spring:message code="imageedit.gesture.unfriendly"/></option>
                            <option value="9"<c:if test="${status.value==9}"> selected</c:if>><spring:message code="imageedit.gesture.talking"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="row">
    <spring:bind path="command.imageVersion.noteLng1">
    <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.note"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="noteLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyNoteLng1}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.noteLng2">
    <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.note"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="noteLng2" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyNoteLng2}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>
    </div>

    <div class="row">
    <spring:bind path="command.imageVersion.restrictionsLng1">
    <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.restrictions"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="restrictionsLng1" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyRestrictionsLng1}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.restrictionsLng2">
    <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.restrictions"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="restrictionsLng2" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyRestrictionsLng2}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>
    </div>

    <div class="form-group">
        <label for="selFlag"><spring:message code="imageedit.flag"/></label> &nbsp;<input type="checkbox" name="copyfield" value="gesture" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyGesture}"> checked="true"</c:if>>
        <spring:bind path="command.imageVersion.flag">
        <select class="form-control" id="selFlag" name="imageVersion.flag">
                        <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="imageedit.flag.undefined"/></option>
                        <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="imageedit.flag.red"/></option>
                        <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="imageedit.flag.yellow"/></option>
                        <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="imageedit.flag.green"/></option>
        </select>
        </spring:bind>
    </div>

    <spring:bind path="command.imageVersion.price">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.price"/></label> &nbsp;<input type="checkbox" name="copyfield" value="price" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPrice}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.imageVersion.licValid">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.licvalid"/></label> &nbsp;<input type="checkbox" name="copyfield" value="licValid" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyLicValid}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${not empty customList[0]}">
    <div class="form-group">
        <label for="selFlag"><c:out value="${customLists[0].title}"/></label> 
        <spring:bind path="command.imageVersion.customList1">
        <select class="form-control" id="selCustomList1" name="imageVersion.customList1">
            <c:if test="${command.imageVersion.customList1==0}"><option value="0">nicht angegeben</option></c:if>
            <c:forEach items="${customList[0]}" var="listEntry">
            <option value="<c:out value="${listEntry.id}"/>"<c:if test="${status.value==listEntry.id}"> selected</c:if>><c:out value="${listEntry.title}"/></option>
            </c:forEach>
        </select>
        </spring:bind>
    </div>
    </c:if>

    <c:if test="${not empty customList[1]}">
    <div class="form-group">
        <label for="selFlag"><c:out value="${customLists[1].title}"/></label>
        <spring:bind path="command.imageVersion.customList2">
        <select class="form-control" id="selCustomList2" name="imageVersion.customList2">
            <c:if test="${command.imageVersion.customList2==0}"><option value="0">nicht angegeben</option></c:if>
            <c:forEach items="${customList[1]}" var="listEntry">
            <option value="<c:out value="${listEntry.id}"/>"<c:if test="${status.value==listEntry.id}"> selected</c:if>><c:out value="${listEntry.title}"/></option>
            </c:forEach>
        </select>
        </spring:bind>
    </div>
    </c:if>

    <c:if test="${not empty customList[2]}">
    <div class="form-group">
        <label for="selFlag"><c:out value="${customLists[2].title}"/></label>
        <spring:bind path="command.imageVersion.customList3">
        <select class="form-control" id="selCustomList3" name="imageVersion.customList3">
            <c:if test="${command.imageVersion.customList3==0}"><option value="0">nicht angegeben</option></c:if>
            <c:forEach items="${customList[2]}" var="listEntry">
            <option value="<c:out value="${listEntry.id}"/>"<c:if test="${status.value==listEntry.id}"> selected</c:if>><c:out value="${listEntry.title}"/></option>
            </c:forEach>
        </select>
        </spring:bind>
    </div>
    </c:if>

    <c:if test="${config.customStr1!=''}">
    <spring:bind path="command.imageVersion.customStr1">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr1}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr2!=''}">
    <spring:bind path="command.imageVersion.customStr2">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr2}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr3!=''}">
    <spring:bind path="command.imageVersion.customStr3">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr3}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr4!=''}">
    <spring:bind path="command.imageVersion.customStr4">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr4}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr5!=''}">
    <spring:bind path="command.imageVersion.customStr5">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr5}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr6!=''}">
    <spring:bind path="command.imageVersion.customStr6">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr6}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr7!=''}">
    <spring:bind path="command.imageVersion.customStr7">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr7}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr8!=''}">
    <spring:bind path="command.imageVersion.customStr8">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr8}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr9!=''}">
    <spring:bind path="command.imageVersion.customStr9">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr9}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>

    <c:if test="${config.customStr10!=''}">
    <spring:bind path="command.imageVersion.customStr10">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><c:out value="${config.customStr10}"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    </c:if>
    
    <spring:bind path="command.imageVersion.masterdataId">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="imageedit.masterdata"/></label> &nbsp;<input type="checkbox" name="copyfield" value="masterdataId" alt="copy" title="<spring:message code="imageedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyMasterdata}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${!empty categoryList}">
    <div class="checkbox">
        <fieldset id="replaceCategory"><legend><spring:message code="imageedit.replacecat"/></legend>
        <c:forEach items="${categoryList}" var="category">
        <label>
          <input type="checkbox" name="replaceCategory" id="cbxCat<c:out value="${category.categoryId}"/>" value="<c:out value="${category.categoryId}"/>"> <c:out value="${category.catName}"/> (<c:out value="${category.catTitle}"/>)
        </label>
        </c:forEach>
        </fieldset>
    </div>
    </c:if>

    <c:if test="${selectedList>0}">
    <div class="checkbox">
        <fieldset id="replaceSelectedMedia">
        <label>
          <input type="checkbox"  name="replaceSelected" id="replaceSelected" value="1"> <spring:message code="imageedit.replaceselect"/> (<c:out value="${selectedList}"/>)
        </label>
        </fieldset>
    </div>
    </c:if>

    <button type="submit" class="btn btn-default">Speichern</button>
    </form>






	<!-- /FORMS FÜR EDIT -->
	</div>
</div>      <!-- /row -->
<!-- ENDE EDIT BILD -->
<!-- ###################################################################################################################################################### -->



<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<!-- Media Info POPUP -->
<script type="text/ng-template" id="infoMediaPopup.html">

<div class="modal-content">

      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>
      <span class="modal-title" id="meinModalLabel"><h3>META-Daten</h3></span>
      </div>

      <div class="md-modal-body">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row"><!-- row -->
              <div class="col-md-12">
                <form>
                  <div class="form-group">
        ID: <c:out value="${command.imageVersion.ivid}"/><br/>
        Media Number #: <c:out value="${command.imageVersion.imageNumber}"/><br/>
        <spring:message code="imageedit.createdate"/> <dt:format pattern="dd MMMM yyyy, HH:mm" default=""><c:out value="${command.imageVersion.createDate.time}"/></dt:format><br/>
        <spring:message code="imageedit.by"/> <c:out value="${command.creator.userName}"/><br/>
        Mime: <c:out value="${command.imageVersion.primaryMimeType}"/><br/>
        kb: <c:out value="${command.imageVersion.kb}"/><br/>
        <c:if test="${command.imageVersion.mayorMime=='image'}">
        width: <c:out value="${command.imageVersion.width}"/>
        height: <c:out value="${command.imageVersion.height}"/>
        dpi: <c:out value="${command.imageVersion.dpi}"/>
        </c:if>
        <c:if test="${command.imageVersion.mayorMime=='audio' || command.imageVersion.mayorMime=='video'}">
        <c:if test="${command.imageVersion.mayorMime=='video'}">
        Video-Codec: <c:out value="${command.imageVersion.videocodec}"/><br/>
        </c:if>
        Bitrate: <c:out value="${command.imageVersion.bitrate}"/> kb/s<br/>
        Samplerate: <c:out value="${command.imageVersion.samplerate}"/> Hz<br/>
        Channels: <c:out value="${command.imageVersion.channels}"/><br/>
        Länge: <fmt:formatDate value="${command.imageVersion.durationDate}" pattern="mm:ss" timeZone="GMT"/>
        </c:if>
                  </div>
                  <h4>IPTC und EXIF Data</h4>
                  <div>
<c:forEach items="${metadataList}" var="metadata">
                     <c:out value="${metadata.metaKey}"/>: <c:out value="${metadata.metaValue}"/></br>
</c:forEach>
                  </div>
                </form>
              </div>
          </div><!-- /row -->
          </div><!-- /container -->
      <!-- /INHALT MODAL -->
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary" type="button" ng-click="$close();">OK</button>
      </div>
      </div>
<!-- /modal -->
<!-- /MODAL PARKING ####################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
</script>


<!-- Ende Media Info POPUP -->

<jsp:include page="footer.jsp"/>