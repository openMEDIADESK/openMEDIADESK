<%@ page import="java.util.Enumeration,
                 java.util.List"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.MediaObject" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="EditCtrl" ng-init="init('/api/rest/mo/<c:out value="${command.mediaObject.ivid}"/>/editmode')"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-tag fa-fw"></i> <spring:message code="mediaedit.headline"/> <c:out value="${command.mediaObject.versionName}"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>
    <spring:message code="mediaedit.headline"/>
        <div class="btn-group">
            <label class="btn btn-primary" ng-model="showDe" uib-btn-checkbox>DE</label>
            <label class="btn btn-primary" ng-model="showEn" uib-btn-checkbox>EN</label>
        </div>
</h3>
<spring:bind path="command.mediaObject.versionName">
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
        <a href="#" data-toggle="modal" data-target="#meinModal"><img src="/imageservlet/<c:out value="${command.mediaObject.ivid}"/>/1/image.jpg"></a>
        <div ng-controller="TimeAgoCtrl" ng-init="setDate(<c:out value="${command.mediaObject.createDate.time}"/>)">
            <spring:message code="mediaedit.by"/> <c:out value="${command.creator.userName}"/>
            <a href="#" tooltip-animation="true" uib-tooltip="<dt:format pattern="dd MMMM yyyy, HH:mm" default=""><c:out value="${command.mediaObject.createDate.time}"/></dt:format>">{{dt | timeago}}</a>
            <!--<a href="#" ng-click="infoMediaPopup();"><i class="fa fa-info-circle" aria-hidden="true"></i></a>-->
        </div>
        <div>

        Media Number #: <c:out value="${command.mediaObject.mediaNumber}"/><br/>

        </div>
    </div>
	<div class="col-sm-8">
	<!-- FORMS FÜR EDIT -->


    <form id="imageedit" method="post" action="<c:url value="mediadetailedit"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.mediaObject.versionName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textName"><spring:message code="mediaedit.name"/></label>
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
    <spring:bind path="command.mediaObject.versionTitleLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng1"><spring:message code="mediaedit.title"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionTitleLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitleLng1}"> checked="true"</c:if>>
        <input type="text" ng-model="data.titleLng1" ng-change="title1Changed()" class="form-control input-sm" id="textTitleLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.mediaObject.versionTitleLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="mediaedit.title"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionTitleLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitleLng2}"> checked="true"</c:if>>
        <input type="text" ng-model="data.titleLng2" ng-change="title2Changed()" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>
    </c:if>
    </div>

    <c:if test="${command.mediaObject.mayorMime=='audio' || command.mediaObject.mayorMime=='video'}">
        <!-- audio video daten -->

        <spring:bind path="command.mediaObject.artist">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="mediaedit.artist"/></label> &nbsp;<input type="checkbox" name="copyfield" value="artist" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitle}"> checked="true"</c:if>>
        <input type="text" ng-model="title2" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

        <spring:bind path="command.mediaObject.album">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="mediaedit.album"/></label> &nbsp;<input type="checkbox" name="copyfield" value="album" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitle}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

        <spring:bind path="command.mediaObject.genre">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="mediaedit.genre"/></label> &nbsp;<input type="checkbox" name="copyfield" value="genre" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyTitle}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

    </c:if>

    <div class="row">
    <spring:bind path="command.mediaObject.versionSubTitleLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textSubTitleLng1"><spring:message code="mediaedit.subtitle"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionSubTitleLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySubTitleLng1}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="textSubTitleLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.mediaObject.versionSubTitleLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textSubTitleLng2"><spring:message code="mediaedit.subtitle"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="versionSubTitleLng2" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySubTitleLng2}"> checked="true"</c:if>>
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
    <spring:bind path="command.mediaObject.infoLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textInfoLng1"><spring:message code="mediaedit.info"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="infoLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyInfoLng1}"> checked="true"</c:if>>
        <!--<input type="text" class="form-control input-sm" id="textInfoLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>-->
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.mediaObject.infoLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textInfoLng2"><spring:message code="mediaedit.info"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="infoLng2" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyInfoLng2}"> checked="true"</c:if>>
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
    <spring:bind path="command.mediaObject.siteLng1">
        <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.site"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="siteLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySiteLng1}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.mediaObject.siteLng2">
        <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.site"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="siteLng2" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopySiteLng2}"> checked="true"</c:if>>
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
    <spring:bind path="command.mediaObject.photographDate">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.photographdate"/></label> &nbsp;<input type="checkbox" name="copyfield" value="photographDate" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPhotographDate}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    -->

    <spring:bind path="command.mediaObject.photographDate">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>" ng-controller="DatepickerCtrl">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.photographdate"/></label> &nbsp;<input type="checkbox" name="copyfield" value="photographDate" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPhotographDate}"> checked="true"</c:if>>
            <p class="input-group">
        <input type="text" class="form-control" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" ng-init="setDate(<fmt:formatDate value="${command.mediaObject.photographDate}" pattern="yyyy,M,dd"/>)" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="true" alt-input-formats="altInputFormats" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
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

    <spring:bind path="command.mediaObject.photographerAlias">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.photographer"/></label> &nbsp;<input type="checkbox" name="copyfield" value="photographerAlias" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPhotographer}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.mediaObject.byline">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.byline"/></label> &nbsp;<input type="checkbox" name="copyfield" value="byline" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyByline}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.mediaObject.keywords">
        <div class="form-group">
            <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.keywords"/></label> &nbsp;<input type="checkbox" name="copyfield" value="keywords" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyKeywords}"> checked="true"</c:if>>
            <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
        </div>
    </spring:bind>

    <spring:bind path="command.mediaObject.people">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.people"/></label> &nbsp;<input type="checkbox" name="copyfield" value="people" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPeople}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <div class="form-group">
        <label for="selOrientation"><spring:message code="mediaedit.orientation"/></label> &nbsp;<input type="checkbox" name="copyfield" value="orientation" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyOrientation}"> checked="true"</c:if>>
        <spring:bind path="command.mediaObject.orientation">
        <select class="form-control" id="selOrientation" name="mediaObject.orientation">
                          <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="mediaedit.orientation.undefined"/></option>
                          <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="mediaedit.orientation.horizontal"/></option>
                          <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="mediaedit.orientation.panoramic"/></option>
                          <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="mediaedit.orientation.square"/></option>
                          <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="mediaedit.orientation.vertical"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="form-group">
        <label for="selPerspective"><spring:message code="mediaedit.perspective"/></label> &nbsp;<input type="checkbox" name="copyfield" value="perspective" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPerspective}"> checked="true"</c:if>>
        <spring:bind path="command.mediaObject.perspective">
        <select class="form-control" id="selPerspective" name="mediaObject.perspective">
                        <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="mediaedit.perspective.undefined"/></option>
                        <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="mediaedit.perspective.topview"/></option>
                        <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="mediaedit.perspective.bottomview"/></option>
                        <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="mediaedit.perspective.sideview"/></option>
                        <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="mediaedit.perspective.frontview"/></option>
                        <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="mediaedit.perspective.backview"/></option>
                        <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="mediaedit.perspective.aerialview"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="form-group">
        <label for="selMotive"><spring:message code="mediaedit.motive"/></label> &nbsp;<input type="checkbox" name="copyfield" value="motive" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyMotive}"> checked="true"</c:if>>
        <spring:bind path="command.mediaObject.motive">
        <select class="form-control" id="selMotive" name="mediaObject.motive">
                         <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="mediaedit.motive.undefined"/></option>
                         <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="mediaedit.motive.portrait"/></option>
                         <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="mediaedit.motive.group"/></option>
                         <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="mediaedit.motive.overview"/></option>
                         <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="mediaedit.motive.feature"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="form-group">
        <label for="selGesture"><spring:message code="mediaedit.gesture"/></label> &nbsp;<input type="checkbox" name="copyfield" value="gesture" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyGesture}"> checked="true"</c:if>>
        <spring:bind path="command.mediaObject.gesture">
        <select class="form-control" id="selGesture" name="mediaObject.gesture">
                            <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="mediaedit.gesture.undefined"/></option>
                            <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="mediaedit.gesture.jubilate"/></option>
                            <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="mediaedit.gesture.bright"/></option>
                            <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="mediaedit.gesture.sad"/></option>
                            <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="mediaedit.gesture.sceptic"/></option>
                            <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="mediaedit.gesture.screaming"/></option>
                            <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="mediaedit.gesture.neutral"/></option>
                            <option value="7"<c:if test="${status.value==7}"> selected</c:if>><spring:message code="mediaedit.gesture.friendly"/></option>
                            <option value="8"<c:if test="${status.value==8}"> selected</c:if>><spring:message code="mediaedit.gesture.unfriendly"/></option>
                            <option value="9"<c:if test="${status.value==9}"> selected</c:if>><spring:message code="mediaedit.gesture.talking"/></option>
        </select>
        </spring:bind>
    </div>

    <div class="row">
    <spring:bind path="command.mediaObject.noteLng1">
    <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.note"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="noteLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyNoteLng1}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>

    <spring:bind path="command.mediaObject.noteLng2">
    <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.note"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="noteLng2" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyNoteLng2}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>
    </div>

    <div class="row">
    <spring:bind path="command.mediaObject.restrictionsLng1">
    <div ng-show="showDe==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.restrictions"/> [DE]</label> &nbsp;<input type="checkbox" name="copyfield" value="restrictionsLng1" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyRestrictionsLng1}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>

    <spring:bind path="command.mediaObject.restrictionsLng2">
    <div ng-show="showEn==true" ng-class="{ 'col-xs-6': showDandE(), 'col-xs-12': !showDandE() }" class="form-group">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.restrictions"/> [EN]</label> &nbsp;<input type="checkbox" name="copyfield" value="restrictionsLng2" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyRestrictionsLng2}"> checked="true"</c:if>>
        <textarea class="form-control input-sm" name="<c:out value="${status.expression}"/>" id="text<c:out value="${status.expression}"/>" rows="5"><c:out value="${status.value}"/></textarea>
    </div>
    </spring:bind>
    </div>

    <div class="form-group">
        <label for="selFlag"><spring:message code="mediaedit.flag"/></label> &nbsp;<input type="checkbox" name="copyfield" value="gesture" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyGesture}"> checked="true"</c:if>>
        <spring:bind path="command.mediaObject.flag">
        <select class="form-control" id="selFlag" name="mediaObject.flag">
                        <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="mediaedit.flag.undefined"/></option>
                        <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="mediaedit.flag.red"/></option>
                        <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="mediaedit.flag.yellow"/></option>
                        <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="mediaedit.flag.green"/></option>
        </select>
        </spring:bind>
    </div>

    <spring:bind path="command.mediaObject.price">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.price"/></label> &nbsp;<input type="checkbox" name="copyfield" value="price" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyPrice}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.mediaObject.licValid">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.licvalid"/></label> &nbsp;<input type="checkbox" name="copyfield" value="licValid" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyLicValid}"> checked="true"</c:if>>
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
        <spring:bind path="command.mediaObject.customList1">
        <select class="form-control" id="selCustomList1" name="mediaObject.customList1">
            <c:if test="${command.mediaObject.customList1==0}"><option value="0">nicht angegeben</option></c:if>
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
        <spring:bind path="command.mediaObject.customList2">
        <select class="form-control" id="selCustomList2" name="mediaObject.customList2">
            <c:if test="${command.mediaObject.customList2==0}"><option value="0">nicht angegeben</option></c:if>
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
        <spring:bind path="command.mediaObject.customList3">
        <select class="form-control" id="selCustomList3" name="mediaObject.customList3">
            <c:if test="${command.mediaObject.customList3==0}"><option value="0">nicht angegeben</option></c:if>
            <c:forEach items="${customList[2]}" var="listEntry">
            <option value="<c:out value="${listEntry.id}"/>"<c:if test="${status.value==listEntry.id}"> selected</c:if>><c:out value="${listEntry.title}"/></option>
            </c:forEach>
        </select>
        </spring:bind>
    </div>
    </c:if>

    <c:if test="${config.customStr1!=''}">
    <spring:bind path="command.mediaObject.customStr1">
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
    <spring:bind path="command.mediaObject.customStr2">
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
    <spring:bind path="command.mediaObject.customStr3">
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
    <spring:bind path="command.mediaObject.customStr4">
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
    <spring:bind path="command.mediaObject.customStr5">
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
    <spring:bind path="command.mediaObject.customStr6">
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
    <spring:bind path="command.mediaObject.customStr7">
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
    <spring:bind path="command.mediaObject.customStr8">
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
    <spring:bind path="command.mediaObject.customStr9">
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
    <spring:bind path="command.mediaObject.customStr10">
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
    
    <spring:bind path="command.mediaObject.masterdataId">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="mediaedit.masterdata"/></label> &nbsp;<input type="checkbox" name="copyfield" value="masterdataId" alt="copy" title="<spring:message code="mediaedit.copycbx"/>"<c:if test="${command.applicationSettings.editCopyMasterdata}"> checked="true"</c:if>>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${!empty folderList}">
    <div class="checkbox">
        <fieldset id="replaceFolder"><legend><spring:message code="mediaedit.replacecat"/></legend>
        <c:forEach items="${folderList}" var="folder">
        <label>
          <input type="checkbox" name="replaceFolder" id="cbxCat<c:out value="${folder.folderId}"/>" value="<c:out value="${folder.folderId}"/>"> <c:out value="${folder.name}"/> (<c:out value="${folder.title}"/>)
        </label>
        </c:forEach>
        </fieldset>
    </div>
    </c:if>

    <c:if test="${selectedList>0}">
    <div class="checkbox">
        <fieldset id="replaceSelectedMedia">
        <label>
          <input type="checkbox"  name="replaceSelected" id="replaceSelected" value="1"> <spring:message code="mediaedit.replaceselect"/> (<c:out value="${selectedList}"/>)
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
        ID: <c:out value="${command.mediaObject.ivid}"/><br/>
        Media Number #: <c:out value="${command.mediaObject.mediaNumber}"/><br/>
        <spring:message code="mediaedit.createdate"/> <dt:format pattern="dd MMMM yyyy, HH:mm" default=""><c:out value="${command.mediaObject.createDate.time}"/></dt:format><br/>
        <spring:message code="mediaedit.by"/> <c:out value="${command.creator.userName}"/><br/>
        Mime: <c:out value="${command.mediaObject.primaryMimeType}"/><br/>
        kb: <c:out value="${command.mediaObject.kb}"/><br/>
        <c:if test="${command.mediaObject.mayorMime=='image'}">
        width: <c:out value="${command.mediaObject.width}"/>
        height: <c:out value="${command.mediaObject.height}"/>
        dpi: <c:out value="${command.mediaObject.dpi}"/>
        </c:if>
        <c:if test="${command.mediaObject.mayorMime=='audio' || command.mediaObject.mayorMime=='video'}">
        <c:if test="${command.mediaObject.mayorMime=='video'}">
        Video-Codec: <c:out value="${command.mediaObject.videocodec}"/><br/>
        </c:if>
        Bitrate: <c:out value="${command.mediaObject.bitrate}"/> kb/s<br/>
        Samplerate: <c:out value="${command.mediaObject.samplerate}"/> Hz<br/>
        Channels: <c:out value="${command.mediaObject.channels}"/><br/>
        Länge: <fmt:formatDate value="${command.mediaObject.durationDate}" pattern="mm:ss" timeZone="GMT"/>
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