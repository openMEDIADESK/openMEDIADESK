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
    <li><a href="<c:url value="/${lng}/search"/>"><i class="fa fa-search fa-fw"></i> <spring:message code="searchresult.headline"/></a></li>
    <li class="active"><i class="fa fa-magic"></i> <spring:message code="bulkmod.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="bulkmod.headline"/></h3>
<h4><spring:message code="bulkmod.subheadline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form action="bulkmodification" method="post">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.reimportMetadata">
    <div class="form-group">
        <div class="checkbox">
          <label>
            <input type="checkbox" name="reimportMetadata"<c:if test="${status.value==true}"> checked="true"</c:if>> <spring:message code="bulkmod.meta"/>
          </label>
        </div>
    </div>
    </spring:bind>

    <spring:bind path="command.redrawWatermark">
    <div class="form-group">
        <div class="checkbox">
          <label>
            <input type="checkbox" name="redrawWatermark"<c:if test="${status.value==true}"> checked="true"</c:if>> <spring:message code="bulkmod.watermark"/>
          </label>
        </div>
    </div>
    </spring:bind>

    <c:if test="${command.inProgress==true}">
    <button type="submit" name="docancel" class="btn btn-default"><spring:message code="password.reset"/></button>
    </c:if>
    <c:if test="${command.inProgress==false}">
    <button type="submit" class="btn btn-default">starten</button>
    </c:if>

    </form>

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

    <c:if test="${command.inProgress==false && command.halted==true}">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> Abgebrochen!
    </div>
    </c:if>

        <div class="alert <c:if test="${command.inProgress==false}">alert-info</c:if><c:if test="${command.inProgress==true}">alert-warning</c:if>">

        <spring:bind path="command.imageCount">
            <spring:message code="bulkmod.imagetomod" arguments="${status.value}"/>
        </spring:bind>
        <spring:bind path="command.imageProcessed">
            <spring:message code="bulkmod.imageprocessed" arguments="${status.value}"/>
        </spring:bind>

        </div>

        <p class="help-block">
            <spring:message code="searchresuld.bulkmod.attention"/>
        </p>


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