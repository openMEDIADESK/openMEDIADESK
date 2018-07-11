<%@ page import="net.stumpner.security.acl.AclPermission" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.stumpner.security.acl.AclPrincipal" %>
<%@ page import="net.stumpner.security.acl.Acl" %>
<%@ page import="com.stumpner.mediadesk.core.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
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
    <li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>
    <li><a href="<c:url value="/${lng}/setformat"/>"><i class="fa fa-picture-o"></i> <spring:message code="set.format"/></a></li>
    <li class="active"><i class="fa fa-lock fa-fw"></i> Bild Auflösung: <c:out value="${accessObject.width}"/> x <c:out value="${accessObject.height}"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="acl.headline" arguments=""/></h3>
<h4><spring:message code="acl.permittedare"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- START EDIT FORMAT -->

<div class="row">
    <div class="col-sm-6">
	<!-- FORMS FÜR EDIT -->


    <form action="<c:url value="setformatedit"/>" method="post">

<%
    Acl acl = (Acl)request.getAttribute("command");
%>



                <div class="form-group">
                    <label for="acl">&nbsp;</label>
                    <select class="selectpicker" id="acl" name="acl" multiple="true">
    <c:forEach items="${securityMap}" var="securityGroup">
        <%
            //todo: in einen tag verpacken!
            Map.Entry entry = (Map.Entry) pageContext.findAttribute("securityGroup");
            boolean permission = acl.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("read"));
            pageContext.setAttribute("permitted",new Boolean(permission));
        %>
        <option value="<c:out value="${securityGroup.key}"/>"<c:if test="${permitted}"> selected="true"</c:if>><c:out value="${securityGroup.value.name}"/></option>
    </c:forEach>
                    </select>
                </div>


  <button type="submit" class="btn btn-default"><spring:message code="mediaedit.submit"/></button>
</form>






	<!-- /FORMS FÜR EDIT -->
	</div>
    <div class="col-sm-6 .hidden-xs">
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