<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
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
    <mediadesk:login role="<%= User.ROLE_ADMIN %>">
    <li><a href="<c:url value="/${lng}/usermanager"/>"><i class="fa fa-users fa-fw"></i> <spring:message code="userlist.headline"/></a></li>
    </mediadesk:login>
    <li class="active"><i class="fa fa-user fa-fw"></i> <spring:message code="password.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="password.headline"/></h3>
<h4><spring:message code="password.info" arguments="${command.userName}"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->


    <form method="post" id="passwordedit" action="<c:url value="/${lng}/password"/>">

        <div class="form-group">
        <label for="inputnewPassword"><spring:message code="password.new"/></label>
        <input type="password" class="form-control input-sm" id="inputnewPassword" name="newPassword" value="">
        </div>


        <div class="form-group">
        <label for="inputrepeatPassword"><spring:message code="password.repeat"/></label>
        <input type="password" class="form-control input-sm" id="inputrepeatPassword" name="repeatPassword" value="">
        </div>


    <button type="button" class="btn btn-default" onclick="history.go(-1);"><spring:message code="password.reset"/></button>
    <button type="submit" class="btn btn-default"><spring:message code="password.submit"/></button>

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