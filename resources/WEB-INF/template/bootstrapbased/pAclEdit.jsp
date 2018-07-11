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
    <li><a href="<c:url value="${redirectTo}"/>"><i class="fa fa-folder-open-o fa-fw"></i> <c:out value="${accessObject.title}"/></a></li>
    <li class="active"><i class="fa fa-lock fa-fw"></i> <spring:message code="acl.headline" arguments=""/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="acl.headline" arguments=""/> <c:out value="${accessObject.title}"/></h3>
<h4><spring:message code="acl.permittedare"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- START EDIT BILD -->

<div class="row">
    <div class="col-sm-6">
	<!-- FORMS FÜR EDIT -->


    <form action="<c:url var="postLink" value="/${lng}/acl"/><c:out value="${postLink}"/>" method="post">

<%
    Acl acl = (Acl)request.getAttribute("command");
%>

            <c:forEach items="${securityMap}" var="securityGroup" varStatus="forStatus">
                <%
                    //todo: in einen tag verpacken!
                    pageContext.setAttribute("permission",""); //Reset: Standardmässig auf leere permission falls nichts zutrifft
                    Map.Entry entry = (Map.Entry) pageContext.findAttribute("securityGroup");
                    boolean writePerm = acl.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("write"));
                    pageContext.setAttribute("permitted",new Boolean(writePerm));
                    if (writePerm) { pageContext.setAttribute("permission","write"); }
                    if (!writePerm) {
                        boolean readPerm = acl.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("read"));
                        pageContext.setAttribute("permitted",new Boolean(readPerm));
                        if (readPerm) { pageContext.setAttribute("permission","read"); }
                        else {
                            //NEU write perm
                            boolean viewPerm = acl.checkPermission((AclPrincipal) entry.getValue(), new AclPermission("view"));
                            if (viewPerm) {
                                pageContext.setAttribute("permitted",new Boolean(viewPerm));
                                pageContext.setAttribute("permission","view");
                            }
                        }
                    }
                %>
                <!--
                [<c:out value="${securityGroup.key}"/>,'<c:out value="${securityGroup.value.name}" escapeXml="true"/>','<c:if test="${permitted}"><c:out value="${permission}"/></c:if>',false,5]<c:if test="${!forStatus.last}">,</c:if>
                -->
                <%
                %>

                <div class="form-group">
                    <label for="acl"><c:out value="${securityGroup.value.name}" escapeXml="true"/></label>
                    <select class="form-control" id="<c:out value="${securityGroup.key}"/>" name="acl">
                            <option value=""<c:if test="${permission==''}"> selected</c:if>>Keine</option>
                            <option value="<c:out value="${securityGroup.key}"/>.view"<c:if test="${permission=='view'}"> selected</c:if>>Nur Zeigen</option>
                            <option value="<c:out value="${securityGroup.key}"/>.read"<c:if test="${permission=='read'}"> selected</c:if>>Download</option>
                            <option value="<c:out value="${securityGroup.key}"/>.write"<c:if test="${permission=='write'}"> selected</c:if>>Download und Upload</option>
                    </select>
                </div>

            </c:forEach>

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