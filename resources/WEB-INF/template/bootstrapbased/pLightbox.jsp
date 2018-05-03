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
<div class="col-sm-10 main" ng-controller="ThumbnailViewCtrl" ng-init="initMosView('/api/rest/fav',-1,'<c:out value="${view}"/>',<c:out value="${selectedImages}"/>)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-folder-open-o fa-fw"></i> <spring:message code="lightbox.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="lightbox.headline"/><small>&nbsp;{{mos.length}} <spring:message code="stat.pics"/></small></h3>
<h4></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- LEISTE FÜR OPTIONEN -->
<div><!-- umgibt die leiste für optionen - KEIN CLASS! -->
  <!-- button drop ansicht -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Ansicht">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <i class="fa fa-th fa-fw"></i> Ansicht
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
        <li><a href="#" ng-click="switchToView('auto')"><i class="fa fa-th fa-fw"></i> <spring:message code="categoryedit.viewauto"/></a></li>
        <li><a href="#" ng-click="switchToView('thumbnails')"><i class="fa fa-th-large fa-fw"></i> <spring:message code="categoryedit.thumbview"/></a></li>
        <li><a href="#" ng-click="switchToView('list')"><i class="fa fa-list fa-fw"></i> <spring:message code="categoryedit.listview"/></a></li>
    </ul>
  </div>
  <!-- /button drop ansicht -->
  <!-- button drop auswahl -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Auswahl">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <i class="fa fa-check-square-o fa-fw"></i> Auswahl
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <li><a href="#" ng-click="selectMediaAll()"><i class="fa fa-check-square-o fa-fw"></i>alle markieren</a></li>
        <li><a href="#" ng-click="selectMediaNone()"><i class="fa fa-square-o fa-fw text-success"></i>abwählen</a></li>
        <li role="separator" class="divider"></li>
        <li><a href="#" ng-click="deleteFromFav()"><jsp:include page="iconRemove.jsp"/> von hier entfernen</a></li>
    </ul>
  </div>
  <!-- /button drop auswahl -->
<!-- buttongruppe Herunterladen -->
<div class="btn-group btn-group-xs" role="group" aria-label="Herunterladen">
  <!--<button type="button" class="btn btn-default"><i class="fa fa-download fa-fw"></i> Herunterladen</button>-->
</div>
<!-- /buttongruppe Herunterladen -->
<!-- buttongruppe Ausgewaehlt -->
<div class="btn-group btn-group-xs" role="group" aria-label="Ausgewaehlt">
  <button type="button" class="btn btn-default disabled"><span class="md-text-moview-selected" ng-bind="selectedMedia + ' Objekte ausgewält'"></span></button>
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

</div><!-- /thumb row ende und zu -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- MEHR LADEN -->
<button type="button" ng-show="allmos.length>mos.length" ng-click="appendMos()" class="btn btn-default btn-lg btn-block"><small><i class="fa fa-chevron-circle-down"></i>&nbsp;&nbsp;MEHR LADEN&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i></small></button>
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