<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
    <li class="active"><i class="fa fa-download fa-fw"></i> <spring:message code="download.formatselector.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="download.formatselector.headline"/></h3>
<h4><spring:message code="download.formatselector.info"/></h4>
<!-- /ordnertitel und infos -->
<c:url value="/${lng}/formatselector" var="submiturl"/>

    <form action="<c:out value="${submiturl}"/>" method="post">

        <c:if test="${empty command.downloadList && empty command.deniedList}">
            <div class="alert alert-danger" role="alert">
                <spring:message code="download.empty"/>
            </div>
        </c:if>
        <c:if test="${not empty command.deniedList}">
            <div class="alert alert-danger" role="alert">

                <c:if test="${command.allFormatsDenied}"><strong>Ihr Benutzerkonto ist f&uuml;r keine Aufl&ouml;sung berechtigt.</strong> <br/></c:if>
                <spring:message code="download.denied"/>
            </div>                
        </c:if>

        <c:if test="${empty command.downloadList}">
            <button class="btn btn-success" onClick="javascript:history.back();"><spring:message code="download.back"/></button>
        </c:if>

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- Liste/elemente anzeigen -->
<!-- jsp:include page="list.jsp" flush="true"/ -->
<!-- WARENKORB ################################################################################################################################## -->


<!-- warenkorb -->
<!-- EIN WARENKORB ELEMENT -->
<% int i = 1; %>
<c:forEach var="downloadList" items="${command.downloadList}" varStatus="counter">
<% i++; %>
<div class="media md-border-bottom">
  <div class="media-left">
    <c:if test="${downloadList.mayorMime=='image'}">
    <a href="#" ng-click="openPreview($index)"><img class="media-object md-img-thumb" src="/imageservlet/<c:out value="${downloadList.ivid}"/>/1/image.jpg"/></a><br>
    </c:if>
  </div>
  <div class="media-body">
    <!-- inhalt -->
    <div class="md-download-info">
    <!-- NEU IN DER ANZEIGE WARENKORB -->
        <div class="md-item-text"><span class="lead"><c:out value="${downloadList.versionTitle}"/></span></div>
        <div class="md-item-text"># <c:out value="${downloadList.imageNumber}"/></div>
        <div class="md-item-text"><c:out value="${downloadList.versionSubTitle}"/></div>
		<div class="md-item-text"><c:out value="${downloadList.info}"/></div>
        <div class="md-item-text"><c:out value="${downloadList.byline}"/></div>
        <div class="md-item-text"><dt:format pattern="dd MMMM yyyy" default=""><c:out value="${downloadList.photographDate.time}"/></dt:format></div>
        <div class="md-item-text"><c:out value="${downloadList.note}"/></div>
        <div class="md-item-text"><small class="text-muted"><c:out value="${downloadList.restrictions}"/></small></div>
        <div class="md-item-text"><small class="text-muted"><c:out value="${downloadList.site}"/></small></div>
        <div class="md-item-text">

                Auflösung:
                <select name="selectedFormat[<c:out value="${counter.index}"/>]">
                <c:forEach var="format" items="${command.availableFormatList}" varStatus="formatCounter">
                    <c:if test="${format.width==0 && format.height==0}">
                        <option value="-1"><c:out value="${downloadList.width}"/> x <c:out value="${downloadList.height}"/> (Original)</option>
                    </c:if>
                    <c:if test="${format.width>0 && format.height>0}">
                        <option value="<c:out value="${formatCounter.index}"/>"><fmt:formatNumber value="${format.width}" maxFractionDigits="0"/> x <fmt:formatNumber value="${format.height}" maxFractionDigits="0"/></option>
                    </c:if>
                </c:forEach>
                </select>

        </div>

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
    <c:if test="${not empty command.downloadList}">
        <input type="submit" class="btn btn-success" value="<spring:message code="pinwizard.next"/>"/>
    </c:if>
   <!--<button type="button" class="btn btn-link">Doch nicht, retour oder was auch immer</button>-->
<!-- /RUNTERLADEN BUTTON -->

</form>

<!-- /warenkorb -->

<!-- /WARENKORB ################################################################################################################################## -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- /verschachtelung -->
<!-- /row thumbs -->

<c:if test="${not empty command.deniedList}">

<div class="alert alert-danger" role="alert">
<spring:message code="download.notallowed"/>
</div>

<!-- Access Denied Objekte -->
<c:forEach var="deniedImage" items="${command.deniedList}" varStatus="counter">
<div class="media md-border-bottom">
  <div class="media-left">
    <c:if test="${deniedImage.mayorMime=='image'}">
    <a href="#" ng-click="openPreview($index)"><img class="media-object md-img-thumb" src="/imageservlet/<c:out value="${deniedImage.ivid}"/>/1/image.jpg"/></a><br>
    </c:if>
  </div>
  <div class="media-body">
    <!-- inhalt -->
    <div class="md-download-info">
    <!-- NEU IN DER ANZEIGE WARENKORB -->
        <div class="md-item-text"><span class="lead"><c:out value="${deniedImage.versionTitle}"/></span></div>
        <div class="md-item-text"># <c:out value="${deniedImage.imageNumber}"/></div>
        <div class="md-item-text"><c:out value="${deniedImage.versionSubTitle}"/></div>
		<div class="md-item-text"><c:out value="${deniedImage.info}"/></div>
        <div class="md-item-text"><c:out value="${deniedImage.byline}"/></div>
        <div class="md-item-text"><dt:format pattern="dd MMMM yyyy" default=""><c:out value="${deniedImage.photographDate.time}"/></dt:format></div>
        <div class="md-item-text"><c:out value="${deniedImage.note}"/></div>
        <div class="md-item-text"><small class="text-muted"><c:out value="${deniedImage.restrictions}"/></small></div>
        <div class="md-item-text"><small class="text-muted"><c:out value="${deniedImage.site}"/></small></div>

    </div>
    <!-- inhalt -->
  </div>
</div>
</c:forEach>
</c:if>


<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>