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
<div class="col-sm-10 main" ng-controller="ThumbnailViewCtrl" ng-init="initMosView('/api/rest/folder',<c:out value="${containerId}"/>,'<c:out value="${view}"/>',<c:out value="${selectedImages}"/>, <c:out value="${sortBy}"/>, <c:out value="${orderBy}"/>)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <c:forEach items="${parentFolderList}" var="navItem" varStatus="loop">
        <c:url var="thisCatLink" value="/${lng}/cat">
            <c:param name="id" value="${navItem.categoryId}"/>
        </c:url>
        <c:if test="${!loop.last}">
            <li><a href="<c:out value="${thisCatLink}"/>"><i class="fa fa-folder-open-o fa-fw"></i> <c:out value="${navItem.catTitle}"/></a></li>
        </c:if>
        <c:if test="${loop.last}">
            <li class="active"><i class="fa fa-folder-open-o fa-fw"></i> <c:out value="${folder.catTitle}"/></li>
        </c:if>
    </c:forEach>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><c:out value="${folder.catTitle}"/><small>&nbsp;<c:out value="${folder.mediaCount}"/> <spring:message code="stat.pics"/></small></h3>
<h4><c:out value="${folder.description}"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- LEISTE FÜR OPTIONEN -->
<div><!-- umgibt die leiste für optionen - KEIN CLASS! -->
  <!-- button drop ansicht -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Ansicht" ng-show="allmos.length>0 || subfolders.length>0">
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
  <jsp:include page="sharerDropdown.jsp"/>
  <!-- button drop auswahl -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Auswahl" ng-show="allmos.length>0 || selectedMedia>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <jsp:include page="iconSelect.jsp"/> <spring:message code="imagemenu.selection"/>
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <li><a href="#" ng-click="selectMediaAll()"><jsp:include page="iconSelect.jsp"/><spring:message code="categoryindex.markall"/></a></li>
        <li><a href="#" ng-click="selectMediaNone()"><jsp:include page="iconDeselect.jsp"/><spring:message code="categoryindex.unmarkall"/></a></li>
        <mediadesk:login role="<%= User.ROLE_EDITOR %>"><li><a href="#" ng-click="insertMedia()"><jsp:include page="iconCopy.jsp"/><spring:message code="copyhere"/></a></li></mediadesk:login>
        <mediadesk:login role="<%= User.ROLE_MASTEREDITOR %>"><li><a href="#" ng-click="setFolderImage()"><jsp:include page="iconImage.jsp"/>&nbsp;<spring:message code="folderview.asfolderimage"/></a></li></mediadesk:login>
        <mediadesk:login role="<%= User.ROLE_EDITOR %>">
        <li role="separator" class="divider"></li>
        <li><a href="#" ng-click="removeMedia()"><jsp:include page="iconRemove.jsp"/><spring:message code="mediamenu.removefromhere"/></a></li>
        <li role="separator" class="divider"></li>
        <li><a href="#" ng-click="deleteMediaPopup()"><jsp:include page="iconDelete.jsp"/><spring:message code="imagemenu.deletedb"/></a></li>
        </mediadesk:login>
    </ul>
  </div>
  <!-- /button drop auswahl -->
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
    </ul>
  </div>
<!-- buttongruppe Herunterladen -->
<div class="btn-group btn-group-xs" role="group" aria-label="Herunterladen" ng-show="selectedMedia>1">
  <a href="/{{properties.lng}}/shoppingcart?download=selectedMedia" class="btn btn-default"><i class="fa fa-download fa-fw"></i> <spring:message code="tm.download"/></a>
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