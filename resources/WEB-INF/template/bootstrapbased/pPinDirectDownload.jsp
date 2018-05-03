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
<h3><c:out value="${pin.pinpicTitle}"/>   <c:if test="${empty pin.pinpicTitle}">PIN-Download</c:if></h3>
<h4><c:out value="${pin.note}"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<div>
    <p class="text-center">
    <a href="<%= response.encodeURL("/download?pin=all&DTHCVBNCFG75GHDXC34XFGS346554345462345234523452GDFVGR") %>"><i class="fa fa-download fa-5x"></i></a>
    </p>
</div>

<!-- LEISTE FÜR OPTIONEN -->
<div><!-- umgibt die leiste für optionen - KEIN CLASS! -->

    <p class="text-center">
    <spring:message code="pin.dd.info"/>
    <a href="<%= response.encodeURL("/download?pin=all&DTHCVBNCFG75GHDXC34XFGS346554345462345234523452GDFVGR") %>"><spring:message code="pin.dd.next"/></a>.
    </p>

<!-- /LEISTE FÜR OPTIONEN -->
</div><!-- /umgibt die leiste für optionen -->

<script language="javascript">

    setTimeout(function() {
        window.location.href='<%= response.encodeURL("/download?pin=all&DTHCVBNCFG75GHDXC34XFGS346554345462345234523452GDFVGR") %>';
    }, 5000);

</script>

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
