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
    <li class="active"><i class="fa fa-globe"></i> <spring:message code="set.lang"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.lang"/></h3>
<h4><spring:message code="set.lang.headline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->


    <form method="post" action="<c:url value="setlanguage"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.multiLang">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.lang.multiLang"/></label>
                <select name="multiLang" id="input<c:out value="${status.expression}"/>" class="form-control">
                    <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                    <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                </select>
        </div>
    </spring:bind>

    <spring:bind path="command.langAutoFill">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.lang.autoFill"/></label>
                <select name="langAutoFill" id="input<c:out value="${status.expression}"/>" class="form-control">
                    <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                    <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                </select>
        </div>
    </spring:bind>

    <spring:bind path="command.primaryLang">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>">Primäre Sprache</label>
                <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control" disabled="true">
                    <c:forEach items="${command.langList}" var="lang">
                    <option value="${lang}"<c:if test="${status.value==lang}"> selected</c:if>><c:out value="${lang}"/></option>
                    </c:forEach>
                </select>
        </div>
    </spring:bind>

    <spring:bind path="command.availableLang">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>">Verfügbare Sprachen</label>
                <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control multiselect" multiple="multiple" disabled="true">
                    <c:forEach items="${command.langList}" var="lang">
                    <option value="${lang}"<c:if test="${status.value==lang}"> selected</c:if>><c:out value="${lang}"/></option>
                    </c:forEach>
                </select>
        </div>
    </spring:bind>

    <button type="submit" class="btn btn-default"><spring:message code="imageedit.submit"/></button>

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