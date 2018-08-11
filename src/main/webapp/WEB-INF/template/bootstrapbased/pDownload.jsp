<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="#"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-download fa-fw"></i> <spring:message code="download.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="download.headline"/></h3>
<h4><spring:message code="download.subheadline"/></h4>
<!-- /ordnertitel und infos -->

<!-- WARNUNG/FEHLER keine Dateien -->
   <c:if test="${downloadCount==0 && empty deniedList}">
   <div class="alert alert-danger"><spring:message code="download.empty"/></div>
   </c:if>
   <c:if test="${not empty deniedList}">
   <div class="alert alert-danger"><spring:message code="download.denied"/></div>    
   </c:if>

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- Liste/elemente anzeigen -->
<!-- jsp:include page="list.jsp" flush="true"/ -->
<!-- WARENKORB ################################################################################################################################## -->
<c:if test="${downloadCount>0}">

        <%
            if (Config.creditSystemEnabled && Config.currency.isEmpty()) {
        %>
        <c:if test="${user.credits>0}">
        <div class="well"><spring:message code="download.creditinfo" arguments="${user.credits},${downloadCount}"  /></div>
        </c:if>
        <%
            }
            request.setAttribute("downloadUrl","/download/"+System.currentTimeMillis());
        %>

<!-- warenkorb -->
<!-- EIN WARENKORB ELEMENT -->
<% int i = 1; %>
<c:forEach items="${downloadList}" var="image" varStatus="imageStatus">
<% i++; %>
<c:url var="singleDownloadUrl" value="/download/">
  <c:param name="sdl" value="true"/>
  <c:param name="ivid" value="${image.ivid}"/>
</c:url>
<div class="media md-border-bottom">
  <div class="media-left">
    <a href="#" ng-click="openPreview($index)"><img class="media-object md-img-thumb" src="/imageservlet/<c:out value="${image.ivid}"/>/1/image.jpg"/></a><br>
    <a href="<c:out value="${singleDownloadUrl}"/>" class="btn btn-success btn-xs"><i class="fa fa-download"></i>&nbsp;download</a>

  </div>
  <div class="media-body">
    <!-- inhalt -->
    <div class="md-download-info">
    <!-- NEU IN DER ANZEIGE WARENKORB -->
        <div class="md-item-text"><span class="lead"><c:out value="${image.versionTitle}"/></span></div>
		<div class="md-item-text"># <c:out value="${image.mediaNumber}"/></div>
        <div class="md-item-text"><c:out value="${image.versionSubTitle}"/></div>
        <c:if test="${useFormatSelector}">
        <div class="md-item-text">Format:</div>
        <div class="md-item-text">
          <c:if test="${formatSelector.selectedFormat[imageStatus.index]!=-1}">
              <c:out value="${formatSelector.availableFormatList[formatSelector.selectedFormat[imageStatus.index]].width}"/> x
              <c:out value="${formatSelector.availableFormatList[formatSelector.selectedFormat[imageStatus.index]].height}"/>
          </c:if>
          <c:if test="${formatSelector.selectedFormat[imageStatus.index]==-1}">
              <c:out value="${image.width}"/> x <c:out value="${image.height}"/>
          </c:if>
        </div>
        </c:if>

        <!-- NEU -->
		<div class="md-item-text"><c:out value="${image.info}"/></div>
        <div class="md-item-text"><c:out value="${image.byline}"/></div>
        <div class="md-item-text"><dt:format pattern="dd MMMM yyyy" default=""><c:out value="${image.photographDate.time}"/></dt:format></div>
        <div class="md-item-text"><c:out value="${image.note}"/></div>
        <div class="md-item-text"><small class="text-muted"><c:out value="${image.restrictions}"/></small></div>
        <div class="md-item-text"><small class="text-muted"><c:out value="${image.site}"/></small></div>

        <!-- alt --
        <div class="md-item-text" ng-bind="mo.ivid"></div>
        <div class="md-item-text"><span class="lead" ng-bind="mo.title"></span></div>
        <div class="md-item-text"><span class="lead">Untertitel</span></div>
        <div class="md-item-text">Ort</div>
        <div class="md-item-text"><small class="text-muted">Fotograf</small></div>
        <div class="md-item-text"><small class="text-muted">Datum</small></div>
        -->
    </div>
    <!-- inhalt -->
  </div>
</div>
</c:forEach>
<!-- /EIN WARENKORB ELEMENT -->

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- RUNTERLADEN BUTTON -->
   <a href="<c:url value="${downloadUrl}"/>" class="btn btn-success"><i class="fa fa-file-archive-o"></i>&nbsp;&nbsp;<spring:message code="download.downloadzip"/>&nbsp;&nbsp;</a>
   <!--<button type="button" class="btn btn-link">Doch nicht, retour oder was auch immer</button>-->
<!-- /RUNTERLADEN BUTTON -->

<!-- /warenkorb -->
</c:if>
<!-- /WARENKORB ################################################################################################################################## -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- /verschachtelung -->
<!-- /row thumbs -->


<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>