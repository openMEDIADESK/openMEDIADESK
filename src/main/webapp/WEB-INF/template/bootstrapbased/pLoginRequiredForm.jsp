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
    <h3 class="panel-title"><i class="fa fa-sign-in fa-fw"></i> <spring:message code="loginrequired.headline"/></h3>
    </div>
	<div class="panel-body">
    <!-- panel inhalt -->

    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>&nbsp;<spring:message code="loginrequired.subheadline"/>
    </div>

<!-- form beispiele -->
<form method="post" action="<c:url value="login"/>">
  <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>
  <spring:bind path="command.userName">
  <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
    <label for="textUsername"><spring:message code="${usernameCaptionMessage}"/>:</label>
    <input type="text" focus="true" class="form-control" id="textUsername" placeholder="<spring:message code="${usernameCaptionMessage}"/>" name="userName" value="<c:out value="${status.value}"/>">
  </div>
  </spring:bind>
<!-- -->
  <spring:bind path="command.password">
  <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
    <label for="textPassword"><spring:message code="login.password"/>:</label>
    <input type="password" class="form-control" id="textPassword" placeholder="<spring:message code="login.password"/>" name="password">
  </div>
  </spring:bind>
  <!-- -->

  <div class="checkbox">
    <label>
      <input type="checkbox" name="autologin" value="true" > angemeldet bleiben
    </label>
  </div>

  <button type="submit" class="btn btn-default">Login</button>

    <div class="form-group">
        <!--
        <label for="beispielFeldDatei">Anhang</label>
        <input type="file" id="beispielFeldDatei"> -->

        <p class="help-block">
        <% if (Config.allowRegister) { %>
            <spring:message code="login.logintext"/> <a href="<c:url value="/register"/>"><spring:message code="login.register"/></a>.
        <% } %>
        <a href="loginforgot" class="forgotPassword"><spring:message code="login.forgotpwd"/></a>
        </p>
    </div>

    <spring:hasBindErrors name="command">

    <div class="form-group">
      &nbsp;
    </div>

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