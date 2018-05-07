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

        <li><a href="<c:url value="/${lng}/usermanager"/>"><i class="fa fa-users fa-fw"></i> Benutzer</a></li>
    <li class="active"><i class="fa fa-lock" aria-hidden="true"></i> Zugriff für <c:out value="${targetname}"/> auswählen</li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>
<spring:message code="${headline}" arguments="${targetname}"/>
</h3>
<h4>Wählen Sie die Zugriffsberechtigung für die Berechtigungsgruppe <c:out value="${targetname}"/> aus</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- START EDIT TREE ACL -->

<div class="row">
	<div class="col-sm-6">
	<!-- FORMS FÜR EDIT -->


    <form method="post" action="<c:url value="/${lng}/treeacl"/>">

    <c:forEach items="${command.folderList}" var="listElement" varStatus="stat">
        <div class="row">

            <div class="col-xs-1">
        <spring:bind path="command.folderList[${stat.index}].folder.folderId">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <!--<label for="name">Ordner ID</label>-->
            <c:if test="${listElement.folder.parent!=0}">&nbsp;</c:if>
        <i class="fa fa-folder" aria-hidden="true"></i>        
        <input type="hidden" class="form-control input-sm" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
        </div>
        </spring:bind>
            </div>

            <div class="col-xs-7">
        <spring:bind path="command.folderList[${stat.index}].folder.folderName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <input type="text" class="form-control input-sm" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" readonly="true">
        </div>
        </spring:bind>
            </div>

            <div class="col-xs-4">
        <spring:bind path="command.folderList[${stat.index}].permissionString">
            <select class="form-control" name="<c:out value="${status.expression}"/>">
                    <option value=""<c:if test="${status.value==''}"> selected</c:if>>Keine</option>
                    <option value="view"<c:if test="${status.value=='view'}"> selected</c:if>>Nur Zeigen</option>
                    <option value="read"<c:if test="${status.value=='read'}"> selected</c:if>>Download</option>
                    <option value="write"<c:if test="${status.value=='write'}"> selected</c:if>>Download und Upload</option>
            </select>
        </spring:bind>
            </div>

        </div>
    </c:forEach>


        <button type="submit" class="btn btn-default"><spring:message code="imageedit.submit"/></button>
    </form>






	<!-- /FORMS FÜR EDIT -->
	</div>
    <div class="col-sm-6 .hidden-xs">

        &nbsp;
        <!-- Eventuell Hilftext für die Berechtigungen?!?!

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

        -->

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