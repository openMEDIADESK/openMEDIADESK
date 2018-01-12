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
    <li class="active"><i class="fa fa-picture-o"></i> <spring:message code="set.format"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.format"/></h3>
<h4><spring:message code="set.format.info"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <table class="table table-bordered">

      <thead>
        <tr>
          <th>#</th>
          <th><spring:message code="set.format.format"/></th>
          <th>&nbsp;</th>
          <th>&nbsp;</th>
        </tr>
      <tbody>
     <% int i = 1; %>
     <c:forEach items="${formatList}" var="format" varStatus="stat">
     <% i++; %>
        <tr>
            <td><c:out value="${stat.index}"/></td>
            <td>
                <c:if test="${stat.index==0}"><spring:message code="set.format.original"/></c:if>
                <c:if test="${stat.index>0}"><fmt:formatNumber value="${format.width}" maxFractionDigits="0"/> x <fmt:formatNumber value="${format.height}" maxFractionDigits="0"/> </c:if>
            </td>
            <td class="text-right">
                <a href="<c:url value="setformatedit?index=${stat.index}"/>"><i class="fa fa-lock" aria-hidden="true"></i></a>&nbsp;
                <a href="<c:url value="?delindex=${stat.index}"/>"><i class="fa fa-trash" aria-hidden="true"></i></a>
            </td>
            <td>&nbsp;</td>
        </tr>
      </c:forEach>
          <form method="post" action="<c:url value="${url}"/>">
        <tr>
            <td><i class="fa fa-plus-circle" aria-hidden="true"></i></td>
            <td></td>
            <td><spring:message code="set.format.new"/>:<input type="text" size="5" class="form-control" name="width"> x <input type="text" size="5" class="form-control" name="height"></td>
            <td><button type="submit" name="add" class="btn btn-default">anlegen</button></td>
        </tr>
          </form>
      </tbody>

    </table>

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