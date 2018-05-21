<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="ThumbnailViewCtrl" ng-init="initMosView('/api/rest/searchresult',-1,'<c:out value="${view}"/>',<c:out value="${selectedImages}"/>)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="#"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-search fa-fw"></i>
        <c:if test="${!empty searchString}"><spring:message code="searchresult.headline"/></c:if>
        <c:if test="${empty searchString}"><spring:message code="imagesearch.imagesearch"/></c:if>
    </li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<c:if test="${!empty searchString}">
<h3><c:out value="${searchString}"/> <c:if test="${!empty searchMessage}"><spring:message code="${searchMessage}"/></c:if><small>&nbsp; ergab <c:out value="${mediaCount}"/> Treffer</small></h3>
</c:if>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- START SUCHE -->

<div class="row">
	<div class="col-md-6">
    <!-- SUCHFORM -->
    <form method="post" action="<c:url value="/${lng}/search"/>">
        <input name="q" type="hidden" value="">
    <div class="input-group">
          <span class="input-group-btn">
            <button ng-click="advancedsearch = !advancedsearch" class="btn btn-default input-lg" type="button"><i class="fa fa-cogs fa-fw"></i></button>
          </span>
      <input type="text" name="keywords" class="form-control input-lg" placeholder="<spring:message code="imagesearch.imagesearch"/>" value="<c:out value="${searchString}"/>">
          <span class="input-group-btn">
            <button class="btn btn-default input-lg" type="submit"><i class="fa fa-search fa-fw"></i></button>
          </span>
    </div>
    <!-- mediadesk abstand -->
    <div class="md-space-sm">&nbsp;</div>
    <!-- /mediadesk abstand -->
    <input type="checkbox" name="withinresults" id="withinresults" value="">&nbsp;in den Resultaten | <a href="#" ng-click="advancedsearch = !advancedsearch">Suchoptionen</a>

    <div class="form-group" ng-show="advancedsearch">
    <label for="people"><spring:message code="advancedsearch.people"/></label>
    <input name="people" type="text" class="form-control input-sm" id="people" placeholder="">
    </div>
    <div class="form-group" ng-show="advancedsearch">
    <label for="photographer"><spring:message code="advancedsearch.photographer"/></label>
    <input name="photographer" type="text" id="photographer" class="form-control input-sm" placeholder="">
    </div>
    <div class="form-group" ng-show="advancedsearch">
    <label for="location"><spring:message code="advancedsearch.location"/></label>
    <input name="location" type="text" id="location" class="form-control input-sm" placeholder="">
    </div>
    <div class="form-group" ng-show="advancedsearch">
        <label for="DatumDrop"><spring:message code="advancedsearch.date"/></label>
        <select name="period" class="form-control" id="DatumDrop" ng-model="periodeselected">
          <option value="0" selected><spring:message code="imagesearch.anydate"/></option>
          <option value="1"><spring:message code="imagesearch.24hours"/></option>
          <option value="2"><spring:message code="imagesearch.lastweek"/></option>
          <option value="3"><spring:message code="imagesearch.lastmonth"/></option>
          <option value="4"><spring:message code="imagesearch.12month"/></option>
          <option value="-1"><spring:message code="imagesearch.more"/></option>
        </select>
    </div>

    <div class="form-group" ng-show="advancedsearch && periodeselected=='-1'" ng-controller="DatepickerCtrl">
    <label for="dateFrom"><spring:message code="advancedsearch.datefrom"/></label>
        <p class="input-group">
        <input name="dateFrom" id="dateFrom" type="text" class="form-control" placeholder="" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="periodeselected==-1" close-text="Close" alt-input-formats="altInputFormats">
        <span class="input-group-btn">
            <button type="button" class="btn btn-default" ng-click="openDatepicker()"><i class="glyphicon glyphicon-calendar"></i></button>
        </span>
        </p>
    </div>

    <div class="form-group" ng-show="advancedsearch && periodeselected=='-1'" ng-controller="DatepickerCtrl">
    <label for="dateTo"><spring:message code="advancedsearch.dateto"/></label>
        <p class="input-group">
        <input name="dateTo" id="dateTo" type="text" class="form-control" placeholder="" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" ng-required="periodeselected==-1" close-text="Close" alt-input-formats="altInputFormats">
        <span class="input-group-btn">
            <button type="button" class="btn btn-default" ng-click="openDatepicker()"><i class="glyphicon glyphicon-calendar"></i></button>
        </span>
        </p>
    </div>

    <!-- TODO!! geture, motive,.. fehlt -->

    <c:if test="${not empty customList1}">
    <div class="form-group" ng-show="advancedsearch">
		<label><c:out value="${customList1Caption}"/></label>
        <c:forEach items="${customList1}" var="listEntry">
        <div class="checkbox">
          <label>
            <input name="customList1" type="radio" id="customList1id<c:out value="${listEntry.id}"/>" value="<c:out value="${listEntry.id}"/>"><c:out value="${listEntry.title}"/>
          </label>
        </div>
        </c:forEach>
    </div>
    </c:if>

    <c:if test="${not empty customList2}">
    <div class="form-group" ng-show="advancedsearch">
		<label><c:out value="${customList2Caption}"/></label>
        <c:forEach items="${customList2}" var="listEntry">
        <div class="checkbox">
          <label>
            <input name="customList2" type="radio" id="customLis21id<c:out value="${listEntry.id}"/>" value="<c:out value="${listEntry.id}"/>"><c:out value="${listEntry.title}"/>
          </label>
        </div>
        </c:forEach>
    </div>
    </c:if>

    <c:if test="${not empty customList3}">
    <div class="form-group" ng-show="advancedsearch">
		<label><c:out value="${customList3Caption}"/></label>
        <c:forEach items="${customList3}" var="listEntry">
        <div class="checkbox">
          <label>
            <input name="customList3" type="radio" id="customList3id<c:out value="${listEntry.id}"/>" value="<c:out value="${listEntry.id}"/>"><c:out value="${listEntry.title}"/>
          </label>
        </div>
        </c:forEach>
    </div>
    </c:if>

    <mediadesk:login role="<%= User.ROLE_ADMIN %>">

    <div class="form-group" ng-show="advancedsearch">
		<label><spring:message code="imagesearch.findorphan"/></label>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="orphan" name="orphan" id="cbxOrphan"> <spring:message code="imagesearch.findorphan"/>
          </label>
        </div>
    </div>

    <div class="form-group" ng-show="advancedsearch" ng-controller="DatepickerCtrl">
    <label for="licValid"><spring:message code="imageedit.licvalid"/></label>
        <p class="input-group">
        <input name="licValid" id="licValid" type="text" class="form-control" placeholder="" uib-datepicker-popup="dd.MM.yyyy" ng-model="dt" is-open="popupDatepicker.opened" type="html5Types" current-text="Heute" close-text="Fertig" datepicker-options="dateOptions" close-text="Close" alt-input-formats="altInputFormats">
        <span class="input-group-btn">
            <button type="button" class="btn btn-default" ng-click="openDatepicker()"><i class="glyphicon glyphicon-calendar"></i></button>
        </span>
        </p>
    </div>
    </mediadesk:login>
    
    <button type="submit" class="btn btn-default" ng-show="advancedsearch">Suchen</button>

    </form>
    <!-- /SUCHFORM -->
    <!-- mediadesk abstand -->
    <div class="md-space">&nbsp;</div>
    <!-- /mediadesk abstand -->
	</div>
<div class="col-md-6 col-xs-0">
<!-- der abstand rechts als 2. spalte - leer -->
&nbsp;
</div>
</div>      <!-- /row -->
<!-- /ENDE SUCHE -->


<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
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
  <mediadesk:login role="<%= User.ROLE_PINMAKLER %>">
  <!-- button drop teilen -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Teilen" ng-show="allmos.length>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <i class="fa fa-share-alt fa-fw"></i> <spring:message code="mediamenu.share"/>
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <li><a ng-href="/{{properties.lng}}/pinwizard"><jsp:include page="iconPin.jsp"/>&nbsp;&nbsp;<spring:message code="imagemenu.aspin"/></a></li>
        <!-- Nicht möglich via suchurl
        <li><a href="#" ng-click="underConstruction()"><i class="fa fa-facebook fa-fw"></i>&nbsp;&nbsp;facebook</a></li>
        <li><a href="#" ng-click="underConstruction()"><i class="fa fa-twitter fa-fw"></i>&nbsp;&nbsp;twitter</a></li>
        <li><a href="#" ng-click="underConstruction()"><i class="fa fa-google-plus fa-fw"></i>&nbsp;&nbsp;google+</a></li>
        <li><a href="#" ng-click="underConstruction()"><i class="fa fa-tumblr fa-fw"></i>&nbsp;&nbsp;tumblr</a></li>
        <li><a href="#" ng-click="underConstruction()"><i class="fa fa-pinterest-p fa-fw"></i>&nbsp;&nbsp;pinterest</a></li>
        -->
    </ul>
  </div>
  </mediadesk:login>
  <!-- /button drop teilen -->
  <!-- button drop auswahl -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Auswahl" ng-show="allmos.length>0 || selectedMedia>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <jsp:include page="iconSelect.jsp"/> <spring:message code="imagemenu.selection"/>
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <!--<li><a ng-href="/{{properties.lng}}/pinwizard"><jsp:include page="iconPin.jsp"/>&nbsp;&nbsp;<spring:message code="imagemenu.aspin"/></a></li>-->
        <li><a href="#" ng-click="selectMediaAll()"><jsp:include page="iconSelect.jsp"/><spring:message code="categoryindex.markall"/></a></li>
        <li><a href="#" ng-click="selectMediaNone()"><jsp:include page="iconDeselect.jsp"/><spring:message code="categoryindex.unmarkall"/></a></li>
        <mediadesk:login role="<%= User.ROLE_EDITOR %>">
        <li role="separator" class="divider"></li>
        <li><a href="#" ng-click="deleteMediaPopup()"><jsp:include page="iconDelete.jsp"/><spring:message code="imagemenu.deletedb"/></a></li>
        </mediadesk:login>
    </ul>
  </div>
  <!-- /button drop auswahl -->
<mediadesk:login andRoleExact="<%= User.ROLE_ADMIN %>">
<!-- buttongruppe Ausgewaehlt -->
<div class="btn-group btn-group-xs" role="group" aria-label="Massenänderung" ng-show="allmos.length>0">
  <a href="/{{properties.lng}}/bulkmodification" class="btn btn-default"><i class="fa fa-magic" aria-hidden="true"></i> Massenänderung</a>
</div>
</mediadesk:login>
<!-- /buttongruppe Ausgewaehlt -->
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