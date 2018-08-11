<%@ page import="com.stumpner.mediadesk.core.Config" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<div class="row"><!-- login/register row -->
<!-- LOGIN IM 10breiten INHALT -->
<!-- spalte 2 form -->
<div class="col-xs-12 col-xs-offset-0 col-sm-6 col-sm-offset-2">
<!-- hier inhalt 6er spalte mittig -->
<div class="panel panel-default"> <!-- panel -->
    <div class="panel-heading">
    <h3 class="panel-title"><i class="fa fa-user fa-fw"></i> <spring:message code="loginforgot.headline"/></h3>
    </div>
	<div class="panel-body">
	<!-- panel inhalt -->
<!-- form beispiele -->
<form method="post" action="<c:url value="loginforgot"/>">
  <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>
  <div class="form-group">
      <p class="help-block">
          <spring:message code="loginforgot.subheadline"/>
      </p>
  </div>
  <spring:bind path="command.userName">
  <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
    <label for="textUsername"><spring:message code="loginforgot.username"/></label>
    <input type="text" focus="true" class="form-control" id="textUsername" name="userName" value="<c:out value="${status.value}"/>">
  </div>
  </spring:bind>

  <button type="submit" class="btn btn-default"><spring:message code="message.ok"/></button>

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

</form>


    <!-- /panel inhalt -->
	</div>
	</div>
</div> <!-- /panel -->
</div><!-- /spalte 2 form -->
<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->
</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->

<jsp:include page="footer.jsp"/>