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

        <li><a href="<c:out value="${thisCatLink}"/>"><i class="fa fa-folder-open-o fa-fw"></i> <spring:message code="${headline}" arguments="${targetname}"/></a></li>
    <li class="active"><i class="fa fa-pencil-square-o fa-fw"></i>
        Zugriff auswählen</li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>
<spring:message code="${headline}" arguments="${targetname}"/>
</h3>
<h4><spring:message code="${subheadline}" arguments="${targetname}"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- START FOLDER SELECT -->

<div class="row">
	<div class="col-sm-6">
	<!-- FORMS FÜR EDIT -->


    <form method="post" action="<c:url value="/${lng}/categoryselector"/>">

    <c:forEach items="${command.categoryList}" var="listElement" varStatus="stat">
        <spring:bind path="command.categoryList[${stat.index}].category.categoryId">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="name">Ordner ID</label>
        <input type="text" class="form-control input-sm" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>

        <spring:bind path="command.categoryList[${stat.index}].selected">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="name">Selected</label>
        <input type="text" class="form-control input-sm" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
        </spring:bind>
    </c:forEach>


        <button type="submit" class="btn btn-default"><spring:message code="imageedit.submit"/></button>
    </form>






	<!-- /FORMS FÜR EDIT -->
	</div>
    <div class="col-sm-6 .hidden-xs">

        <label>Berechtigt sind</label>

        <ul class="list-group">
            <li class="list-group-item">
                <span class="badge">access</span>
                name
            </li>
        </ul>

        <ul class="list-group">
            <li class="list-group-item">
                <span class="badge">user</span>
                irgendwas
            </li>
        </ul>

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