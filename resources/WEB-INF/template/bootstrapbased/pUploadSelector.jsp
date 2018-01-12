<%@page contentType="text/html;charset=utf-8" language="java" %>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>

<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-cloud-upload fa-fw"></i> <spring:message code="menu.imageimport"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="uploadselector.headline"/></h3>
<!-- /ordnertitel und infos -->

<c:if test="${empty allowedCategoryList}">
<!-- mediadesk abstand -->
<div class="md-space-sm">&nbsp;</div>
<!-- /mediadesk abstand -->
<div class="alert alert-danger" role="alert">

    <strong>Keine Berechtigung zum Upload</strong> <br/>

    Ihnen wurde für keinen Ordner eine Berechtigung zum Upload erteilt! Fragen Sie beim Administrator nach.



</div>
</c:if>

<div class="list-group">

    <c:forEach items="${allowedCategoryList}" var="c">
    <a href="<c:url value="uploadweb?catid=${c.categoryId}"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><c:out value="${c.catTitle}"/></h4>
        <p class="list-group-item-text"><c:out value="${c.description}"/></p>
    </a>
    </c:forEach>

</div>
<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>