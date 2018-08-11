<%@ page import="net.stumpner.security.acl.AclPermission" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.stumpner.security.acl.AclPrincipal" %>
<%@ page import="net.stumpner.security.acl.Acl" %>
<%@ page import="com.stumpner.mediadesk.core.Config" %>
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
    <c:forEach items="${parentFolderList}" var="navItem" varStatus="loop">
        <c:url var="thisFolderLink" value="/${lng}/cat">
            <c:param name="id" value="${navItem.folderId}"/>
        </c:url>
        <li><a href="<c:out value="${thisFolderLink}"/>"><i class="fa fa-folder-open-o fa-fw"></i> <c:out value="${navItem.title}"/></a></li>
    </c:forEach>
    <li class="active"><i class="fa fa-pencil-square-o fa-fw"></i>
        <c:if test="${command.folderId!=0}"><spring:message code="folderedit.headline"/></c:if>
        <c:if test="${command.folderId==0}"><spring:message code="foldernew.headline"/></c:if>
        <spring:bind path="command.name"><c:out value="${status.value}"/></spring:bind></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>
<c:if test="${command.folderId!=0}"><spring:message code="folderedit.headline"/></c:if>
<c:if test="${command.folderId==0}"><spring:message code="foldernew.headline"/></c:if>
</h3>
<h4></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- START EDIT FOLDER -->

<div class="row">
	<div class="col-sm-6">
	<!-- FORMS FÜR EDIT -->

    <form method="post" action="<c:url value="/${lng}/folderedit"/>" ng-controller="EditCtrl" ng-init="init('/api/rest/folder/<c:out value="${command.folderId}" escapeXml="true"/>/editmode')">
    <!-- form ng-submit="submitForm()" ng-controller="EditCtrl" ng-init="init('/api/rest/folder/<c:out value="${command.folderId}" escapeXml="true"/>/editmode')"-->

    <spring:bind path="command.folderId">
        <div ng-show="showMoreAndMore==true" class="form-group">
        <label for="ID"><spring:message code="folderedit.folderid"/></label>
        <input type="text" class="form-control input-sm" id="ID" ng-model="data.id" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" readonly="true">
        </div>
    </spring:bind>

    <spring:bind path="command.name">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="name"><spring:message code="folderedit.name"/></label>
        <input type="text" ng-model="data.name" ng-change="nameChanged()" class="form-control input-sm" id="name" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="Neuer Ordner"<c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.parent">
        <c:if test="${command.folderId!=0 || command.parent!=0}">
        <input type="hidden" class="form-control input-sm" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" readonly="true"/>
        </c:if>
        <c:if test="${command.folderId==0 && command.parent==0}">
        <div ng-show="showMore==true" class="form-group">
        <label for="ID">Unterordner von</label>
        <select class="form-control" id="selDefaultview" name="<c:out value="${status.expression}"/>">
                <option value="0"<c:if test="${status.value==0}"> selected</c:if>>Hauptordner</option>
            <c:forEach items="${parentList}" var="parentListItem">
                <option value="<c:out value="${parentListItem.folderId}"/>"<c:if test="${status.value==parentListItem.folderId}"> selected</c:if>><c:out value="${parentListItem.name}"/></option>
            </c:forEach>
        </select>

        </div>
        </c:if>
    </spring:bind>

    <div class="form-group text-right">
        <button ng-show="showMore==false" ng-click="doShowMore()" type="button" class="btn btn-sm btn-default"><span class="glyphicon glyphicon-chevron-right"></span> Mehr Einstellungen</button>

        <div ng-show="showMore==true" class="btn-group">
            <label class="btn btn-primary" ng-model="showDe" uib-btn-checkbox>DE</label>
            <label class="btn btn-primary" ng-model="showEn" uib-btn-checkbox>EN</label>
        </div>
    </div>

    <spring:bind path="command.titleLng1">
        <div ng-show="showMore==true && showDe==true" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng1"><spring:message code="folderedit.title"/> [DE]</label>
        <input type="text" ng-model="data.titleLng1" ng-change="title1Changed()" class="form-control input-sm" id="textTitleLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="Titel auf Deutsch"<c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>     
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.titleLng2">
        <div ng-show="showMore==true && showEn==true" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textTitleLng2"><spring:message code="folderedit.title"/> [EN]</label>
        <input type="text" ng-model="data.titleLng2" ng-change="title2Changed()" class="form-control input-sm" id="textTitleLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="Titel auf Englisch"<c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>
    </c:if>

    <spring:bind path="command.descriptionLng1">
        <div ng-show="showMore==true && showDe==true" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textDescriptionLng1"><spring:message code="folderedit.description"/> [DE]</label>
        <input type="text" class="form-control input-sm" id="textDescriptionLng1" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="Beschreibung eingeben"<c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <c:if test="${config.multiLang}">
        <spring:bind path="command.descriptionLng2">            
        <div ng-show="showMore==true && showEn==true" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textDescriptionLng2"><spring:message code="folderedit.description"/> [EN]</label>
        <input type="text" class="form-control input-sm" id="textDescriptionLng2" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="Insert description here"<c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>
    </c:if>

    <spring:bind path="command.folderDate">
        <div ng-show="showMore==true" class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>" ng-controller="DatepickerCtrl">
        <label for="textFolderDate"><spring:message code="folderedit.folderdate"/></label>
            <p class="input-group">
            <input type="text" class="form-control" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" ng-init="setDate(<fmt:formatDate value="${command.folderDate}" pattern="yyyy,M,dd"/>)" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="true" alt-input-formats="altInputFormats" id="textFolderDate" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="Neuer Ordner"<c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
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

    <spring:bind path="command.createDate">
        <div ng-show="showMore==true" class="form-group">
        <label><spring:message code="folderedit.creationdate"/></label>
        <input type="text" class="form-control input-sm" value="<c:out value="${status.value}"/>" placeholder="Neuer Ordner" readonly="true">
        </div>
    </spring:bind>

    <div ng-show="showMore==true" class="row">
        <div class="col-xs-12"><label for="selSortBy"><spring:message code="set.application.sort.folder"/></label></div>
        <div class="col-xs-6">
            <spring:bind path="command.sortBy">
            <select class="form-control" id="selSortBy" name="sortBy">
                    <option value="0"<c:if test="${status.value==0}"> selected</c:if>>Standardsortierung verwenden</option>
                    <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.sort.latestDate"/></option>
                    <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="set.application.sort.latestImport"/></option>
                    <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.sort.latestTitle"/></option>
                    <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.sort.latestPeople"/></option>
                    <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.application.sort.latestSite"/></option>
                    <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="set.application.sort.latestByline"/></option>
            </select>
            </spring:bind>
        </div>

        <div class="col-xs-6">
            <spring:bind path="command.orderBy">
            <select class="form-control" id="selOrderByFolder" name="orderBy">
                    <option value="0"<c:if test="${status.value==0}"> selected</c:if>>-</option>
                    <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.sort.asc"/></option>
                    <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.sort.desc"/></option>
            </select>
            </spring:bind>
        </div>
    </div>


    <div class="form-group">
        <label for="selDefaultview"><spring:message code="folderedit.defaultview"/></label>
        <spring:bind path="command.defaultview">
        <select class="form-control" id="selDefaultview" name="defaultview">
                <option value="0"<c:if test="${status.value==0}"> selected</c:if>>auto</option>
                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="folderedit.thumbview"/></option>
                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="folderedit.listview"/></option>
        </select>
        </spring:bind>
    </div>

    <spring:bind path="command.inheritAcl">
        <c:if test="${command.parent!=0}">
    <div ng-show="showMore==true" class="checkbox">
        <label>
          <input type="checkbox" value="1" name="<c:out value="${status.expression}"/>"<c:if test="${status.value}"> checked="true"</c:if>> Berechtigungen vom übergeordneten Ordner erben
        </label>
    </div>
        </c:if>
    </spring:bind>

    <spring:bind path="command.childInheritAcl">
    <div ng-show="showMore==true" class="checkbox">
        <label>
          <input type="checkbox" value="1" name="<c:out value="${status.expression}"/>"> Berechtigungen in allen untergeordneten Ordner kopieren
        </label>
    </div>
    </spring:bind>

    <spring:bind path="command.primaryIvid">
        <c:if test="${status.value!=0}">
    <div class="checkbox">
        <label>
          <input type="checkbox" value="0" name="<c:out value="${status.expression}"/>"> Ordnerbild entfernen
        </label>
    </div>
        </c:if>
    </spring:bind>

  <button type="submit" class="btn btn-default"><spring:message code="mediaedit.submit"/></button>
  <button type="submit" name="acl" value="true" class="btn btn-default"<c:if test="${command.folderId==0}"> ng-disabled="{{true}}"</c:if>><spring:message code="folderedit.acl"/></button>
</form>






	<!-- /FORMS FÜR EDIT -->
	</div>
    <div class="col-sm-6 .hidden-xs">

        <c:if test="${command.folderId!=0}">
        <label>Berechtigt sind</label>

        <ul class="list-group">
        <%
            Acl acl2 = (Acl)request.getAttribute("aclInfo");
        %>
        <c:forEach items="${securityMap}" var="securityGroup">
            <%
                //todo: in einen tag verpacken!
                Map.Entry entry = (Map.Entry) pageContext.findAttribute("securityGroup");
                boolean permission = acl2.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("view"));
                boolean read = acl2.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("read"));
                boolean write = acl2.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("write"));
                String accessDescription = "view";
                if (read) { accessDescription = "download"; }
                if (write) {  accessDescription = "up and down"; }
                if (permission) {
            %>
            <li class="list-group-item">
                <span class="badge"><%= accessDescription %></span>
                <c:out value="${securityGroup.value.name}"/>
            </li>
            <%
                }
            %>
        </c:forEach>
        </ul>

        <ul class="list-group">
        <c:forEach items="${userMap}" var="suser">
            <%
                //todo: in einen tag verpacken!
                Map.Entry entry1 = (Map.Entry) pageContext.findAttribute("suser");
                boolean permission1 = acl2.checkPermission((AclPrincipal) entry1.getValue(), new AclPermission("read"));
                if (permission1) {
            %>
            <li class="list-group-item">
                <span class="badge">user</span>
                <c:out value="${suser.value.name}"/>
            </li>
            <%
                }
            %>
        </c:forEach>
        </ul>
        </c:if>

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
