<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.image.util.CustomTextService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="ThumbnailViewCtrl" ng-init="initMosView('/api/rest/pin',<c:out value="${pin.pinId}"/>,'<c:out value="${view}"/>',<c:out value="${selectedImages}"/>, <c:out value="${sortBy}"/>, <c:out value="${orderBy}"/>)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <mediadesk:login role="<%= User.ROLE_PINMAKLER %>">
    <li><a href="<c:url value="/${lng}/pinlist"/>"><i class="fa fa-ticket fa-fw"></i> <spring:message code="pinmanager.headline"/></a></li>
    </mediadesk:login>
    <li class="active"><i class="fa fa-ticket fa-fw"></i> <spring:message code="pin.headline"/> </li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><c:out value="${pin.pinTitle}"/>   <c:if test="${empty pin.pinTitle}">PIN-Download</c:if></h3>
<h4><c:out value="${pin.note}"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- LEISTE FÜR OPTIONEN -->
<div><!-- umgibt die leiste für optionen - KEIN CLASS! -->
  <!-- button drop ansicht -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Ansicht" ng-show="allmos.length>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <jsp:include page="iconView.jsp"/> <spring:message code="categoryedit.defaultview"/>
      <span class="caret"></span>
    </button>
            <c:url value="/${lng}/${servletMapping}" var="urlAuto">
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:url value="/${lng}/${servletMapping}t" var="urlThumbnail">
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:url value="/${lng}/${servletMapping}l" var="urlList">
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
    <ul class="dropdown-menu">
        <li><a href="#" ng-click="switchToView('auto')"><jsp:include page="iconViewAuto.jsp"/> <spring:message code="categoryedit.viewauto"/></a></li>
        <li><a href="#" ng-click="switchToView('thumbnails')"><jsp:include page="iconViewThumb.jsp"/> <spring:message code="categoryedit.thumbview"/></a></li>
        <li><a href="#" ng-click="switchToView('list')"><jsp:include page="iconViewList.jsp"/> <spring:message code="categoryedit.listview"/></a></li>
    </ul>
  </div>
  <!-- /button drop ansicht -->
  <!-- button drop auswahl -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Auswahl" ng-show="allmos.length>0 || selectedMedia>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <jsp:include page="iconSelect.jsp"/> <spring:message code="imagemenu.selection"/>
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <!--<li><a href="#" ng-click="underConstruction()"><i class="fa fa-ticket fa-fw"></i> als Pin freigeben</a></li>-->
        <li><a href="#" ng-click="selectMediaAll()"><jsp:include page="iconSelect.jsp"/><spring:message code="categoryindex.markall"/></a></li>
        <!-- <li><a href="#"><i class="fa fa-check fa-fw"></i>ganze Seite markieren</a></li> -->
        <li><a href="#" ng-click="selectMediaNone()"><jsp:include page="iconDeselect.jsp"/><spring:message code="categoryindex.unmarkall"/></a></li>
        <li><a href="#" ng-click="insertMedia()"><jsp:include page="iconCopy.jsp"/><spring:message code="copyhere"/></a></li>
        <li role="separator" class="divider"></li>
        <li><a href="#" ng-click="removeMedia()"><jsp:include page="iconRemove.jsp"/><spring:message code="mediamenu.removefromhere"/></a></li>
        <mediadesk:login role="<%= User.ROLE_EDITOR %>">
        <li role="separator" class="divider"></li>
        <li><a href="#" ng-click="deleteMedia()"><jsp:include page="iconDelete.jsp"/><spring:message code="imagemenu.deletedb"/></a></li>
        </mediadesk:login>
    </ul>
  </div>
  <!-- /button drop auswahl -->
  <!-- button drop bearbeiten // vorübergehend herausgenommen
  <div class="btn-group btn-group-xs" role="group" aria-label="Bearbeiten">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <i class="fa fa-wrench fa-fw"></i> Bearbeiten
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
    <li><a href="#" ng-click="underConstruction()"><span class="fa-stack fa-fw"><i class="fa fa-folder-o fa-stack-1x"></i><i class="fa fa-plus fa-stack-1x text-success"></i></span> Neuen Ordner erstellen</a></li>
    <li><a href="#" ng-click="underConstruction()"><span class="fa-stack fa-fw"><i class="fa fa-folder-o fa-stack-1x"></i><i class="fa fa-pencil fa-stack-1x"></i></span> Ordner bearbeiten</a></li>
    <li><a href="#" ng-click="underConstruction()"><span class="fa-stack fa-fw"><i class="fa fa-folder-o fa-stack-1x"></i><i class="fa fa-times fa-stack-1x text-danger"></i></span> Ordner löschen</a></li>
    <li role="separator" class="divider"></li>
    <li><a href="#"><i class="fa fa-cloud-upload fa-fw md-text-prim"></i> Datei hochladen</a></li>
    </ul>
  </div>
  <!-- /button drop bearbeiten -->
  <!-- button drop sortierung -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Sortierung" ng-show="allmos.length>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <jsp:include page="iconSort.jsp"/> <spring:message code="sort"/>
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
    <li><a href="?id=<c:out value="${containerId}"/>&sortBy=2&orderBy=1"><i class="fa fa-sort-alpha-asc fa-fw"></i> A - Z</a></li>
    <li><a href="?id=<c:out value="${containerId}"/>&sortBy=2&orderBy=2"><i class="fa fa-sort-alpha-desc fa-fw"></i> Z - A</a></li>
    <li><a href="?id=<c:out value="${containerId}"/>&sortBy=6&orderBy=1"><i class="fa fa-sort-numeric-asc fa-fw"></i> Datum Upload ansteigend</a></li>
    <li><a href="?id=<c:out value="${containerId}"/>&sortBy=6&orderBy=2"><i class="fa fa-sort-numeric-desc fa-fw"></i> Datum Upload absteigend</a></li>
    <li><a href="?id=<c:out value="${containerId}"/>&sortBy=1&orderBy=1"><i class="fa fa-sort-numeric-asc fa-fw"></i> Aufnahmedatum ansteigend</a></li>
    <li><a href="?id=<c:out value="${containerId}"/>&sortBy=1&orderBy=2"><i class="fa fa-sort-numeric-desc fa-fw"></i> Aufnahmedatum absteigend</a></li>
        <!--
    <li><a href="#"><i class="fa fa-sort-amount-asc fa-fw"></i> aufwärts</a></li>
    <li><a href="#"><i class="fa fa-sort-amount-desc fa-fw"></i> abwärts</a></li>
    -->
    </ul>
  </div>
<!-- buttongruppe Herunterladen -->
<div class="btn-group btn-group-xs" role="group" aria-label="Herunterladen" ng-show="selectedMedia>0">
                    <c:url var="downloadUrl" value="/download">
                      <c:param name="pin" value="DTHCVBNCFG75GHDXC34XFGS346554345462345234523452GDFVGRTZUERETZE34232345SDFSGZTJ766456537FWERFASDFYX234"/>
                    </c:url>
  <a href="<c:out escapeXml="false" value="${downloadUrl}"/>" class="btn btn-default"><i class="fa fa-download fa-fw"></i> <spring:message code="tm.download"/></a>
</div>
<!-- /buttongruppe Herunterladen -->
<!-- buttongruppe Ausgewaehlt -->
<div class="btn-group btn-group-xs" role="group" aria-label="Ausgewaehlt" ng-show="selectedMedia>0">
  <button type="button" class="btn btn-default disabled"><span class="md-text-moview-selected" ng-bind="selectedMedia + ' <spring:message code="items.selected"/>'"></span></button>
</div>
<!-- /buttongruppe Ausgewaehlt -->

<!-- /LEISTE FÜR OPTIONEN -->
</div><!-- /umgibt die leiste für optionen -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- Liste/elemente anzeigen -->
<jsp:include page="list.jsp" flush="true"/>
<!-- \elemente anzeigen -->

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- MEHR LADEN -->
<button type="button" ng-show="allmos.length>mos.length" ng-click="appendMos()" class="btn btn-default btn-lg btn-block"><small><i class="fa fa-chevron-circle-down"></i>&nbsp;&nbsp;<spring:message code="loadmore"/>&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i></small></button>
<!-- /MEHR LADEN -->

<!-- /verschachtelung -->
<!-- /row thumbs -->

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>