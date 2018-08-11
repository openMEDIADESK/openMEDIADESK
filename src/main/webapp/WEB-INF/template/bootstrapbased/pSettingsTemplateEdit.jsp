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
    <li><a href="<c:url value="settemplates"/>"><i class="fa fa-cubes fa-fw"></i> <spring:message code="set.template"/></a></li>
    <li class="active"><i class="fa fa-pencil fa-fw"></i> <c:out value="${command.template.name}"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.template"/></h3>
<h4><spring:message code="set.headline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
    <div class="col-sm-2">

        <h3>Dateien:</h3>

        <ul>
            <c:forEach items="${command.template.fileList}" var="templateFile">
                <li><a href="settemplateeditfile?name=<c:out value="${command.template.name}"/>&file=<c:out value="${templateFile.name}"/>" onclick="var win = window.open('settemplateeditfile?name=<c:out value="${command.template.name}"/>&file=<c:out value="${templateFile.name}"/>','templateeditor','scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=1090,height=540');win.focus();return false;"><c:out value="${templateFile.name}"/></a></li>
            </c:forEach>
        </ul>

        <form action="<c:url value="settemplateedit"/>" method="get">
            <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>
            <input type="hidden" name="name" value="<c:out value="${command.template.name}"/>"/>

                <div class="form-group">
                <label for="input">Neue Datei:</label>
                <input type="text" class="form-control input-sm" name="file" value="">
                <c:if test="${status.error}">
                  <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                  <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                </c:if>
                </div>

            <button type="submit" name="Submit" class="btn btn-default">Datei erstellen</button>
        </form>

    </div>
    <div class="col-sm-10">
	<!-- FORMS FÜR EDIT -->

    <c:if test="${command.file!=null && false}">

    <form method="post" action="<c:url value="settemplateedit"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <h3><c:out value="${command.file.name}"/>:</h3>

    <spring:bind path="command.contents">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>">&nbsp;</label>
        <textarea name="<c:out value="${status.expression}"/>" class="form-control" ng-model="templatecode" rows="25" style="font-family:courier new;font-size:12px;width:600px"><c:out value="${status.value}"/></textarea>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <button type="submit" name="delete" class="btn btn-default">Löschen</button>
    <button type="submit" class="btn btn-default">Speichern</button>

    <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <code><pre><c:out value="${errorMessage}"/></pre></code>
    </div>
    </c:if>

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

    </c:if>

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