<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li><a href="<c:url value="/${lng}/pinlist"/>"><i class="fa fa-users fa-fw"></i> <spring:message code="pinmanager.headline"/></a></li>
    <li class="active"><i class="fa fa-user fa-fw"></i> <spring:message code="pinedit.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="pinedit.headline"/></h3>
<h4><spring:message code="pinedit.subheadline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->


    <form method="post" action="<c:url value="pinedit"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.pin">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.pin"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if> readonly="readonly">
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.pinName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.name"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.pinTitle">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.title"/></label>
        <input type="tel" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.note">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.note"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.maxUse">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.maxUse"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.startDate">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>" ng-controller="DatepickerCtrl">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.startDate"/></label>
            <p class="input-group">
            <input type="text" class="form-control" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" ng-init="setDate(<fmt:formatDate value="${command.startDate}" pattern="yyyy,M,dd"/>)" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="true" alt-input-formats="altInputFormats" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
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

    <spring:bind path="command.endDate">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>" ng-controller="DatepickerCtrl">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.endDate"/></label>
            <p class="input-group">
            <input type="text" class="form-control" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" ng-init="setDate(<fmt:formatDate value="${command.endDate}" pattern="yyyy,M,dd"/>)" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="true" alt-input-formats="altInputFormats" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
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

        <div class="form-group">
            <spring:bind path="command.enabled">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="pinedit.enabled"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
            </select>
            </spring:bind>
        </div>

        <div class="form-group">
            <spring:bind path="command.autoDelete">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="pinedit.autodelete"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
            </select>
            </spring:bind>
        </div>

        <div class="form-group">
            <spring:bind path="command.directDownload">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="pinedit.directdownload"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
            </select>
            </spring:bind>
        </div>

        <div class="form-group">
            <spring:bind path="command.uploadEnabled">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="pinedit.upload"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
            </select>
            </spring:bind>
        </div>

        <div class="form-group">
            <spring:bind path="command.defaultview">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="folderedit.defaultview"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                    <option value="0"<c:if test="${status.value==1}"> selected</c:if>>auto</option>
                    <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="folderedit.thumbview"/></option>
                    <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="folderedit.listview"/></option>
            </select>
            </spring:bind>
        </div>
    
        <spring:bind path="command.emailnotification">
            <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
            <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.notification"/></label>
            <input type="email" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
            <c:if test="${status.error}">
              <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
              <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
            </c:if>
            </div>
        </spring:bind>

        <spring:bind path="command.password">
            <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
            <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.password"/></label>
            <input type="<c:if test="${status.value==''}">text</c:if><c:if test="${status.value!=''}">password</c:if>" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
            <c:if test="${status.error}">
              <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
              <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
            </c:if>
            </div>
        </spring:bind>

        <spring:bind path="command.createDate">
            <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
            <label for="input<c:out value="${status.expression}"/>"><spring:message code="pinedit.createdate"/></label>
            <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" value="<dt:format pattern="dd MMMM yyyy" default=""><c:out value="${command.createDate.time}"/></dt:format>, <c:out value="${command.creatorUsername}"/>" readonly="readonly">
            </div>
        </spring:bind>

    <button type="button" class="btn btn-default" onclick="history.go(-1);"><spring:message code="folderedit.reset"/></button>
    <button type="submit" class="btn btn-default"><spring:message code="folderedit.submit"/></button>

    <spring:hasBindErrors name="command">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <spring:bind path="command">
                <div class="formErrorSummary">
                <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br>
                </c:forEach>
                </div>
            </spring:bind>
    </div>
    </spring:hasBindErrors>

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

<jsp:include page="footer.jsp"/>