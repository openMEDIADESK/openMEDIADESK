<%@ page import="com.stumpner.mediadesk.core.Config"%>
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
    <li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>
    <li class="active"><i class="fa fa-wrench"></i> <spring:message code="menu.maintenance"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="menu.maintenance"/></h3>
<h4>&nbsp;</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <h3>Berechtigungen zurücksetzen <a href="https://wiki.openmediadesk.net/en/Berechtigungen" target="_blank">[?]</a></h3>

    <form action="<c:url value="setmaintenance"/>" method="POST">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <p>Mit dieser Wartung werden bei allen Ordnern die Berechtigungen zurückgesetzt: Öffentlich zeigen, alle anderen Download und Upload.</p>

    <c:if test="${command.resetAclActive}">
        <p>
            <i class="fa fa-spinner fa-1x fa-spin"></i> Wartung läuft: <c:out value="${command.resetAclStatus}"/><br/>Sie können das Fenster schliessen und die Wartung läuft im Hintergrund weiter.
        </p>
    </c:if>

    <button type="submit" name="acl" class="btn btn-default">Wartung starten</button>

    </form>

    <h3>Alle Ordner auf Ansicht: AUTO stellen <a href="https://wiki.openmediadesk.net/en/Wartung" target="_blank">[?]</a></h3>

    <form action="<c:url value="setmaintenance"/>" method="POST">

        <p>Mit dieser Wartung werden alle Ordner auf Ansicht AUTO gestellt. Je nachdem ob sich Bilder im Ordner befinden wird die Miniaturansicht oder die Listenansicht angezeigt.</p>

        <p class="explain">Nachdem diese Wartung gestartet wurde bitte warten und Seite nicht schliessen</p>

    <button type="submit" name="catviewauto" class="btn btn-default">Wartung starten</button>

    </form>

    <spring:hasBindErrors name="command">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <spring:bind path="command">
                <div class="formErrorSummary">
                <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br>
                </c:forEach>
                </div>
            </spring:bind>
    </div>
    </spring:hasBindErrors>

    <!-- /FORMS FÜR EDIT -->
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