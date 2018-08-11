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
    <h3 class="panel-title"><i class="fa fa-sign-in fa-fw"></i> <spring:message code="pinlogin.headline"/></h3>
    </div>
	<div class="panel-body">
	<!-- panel inhalt -->
<!-- form beispiele -->
<form method="post" action="<c:url value="pin"/>">
  <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <div class="form-group">

        <p class="help-block">
        <spring:message code="pinlogin.info"/>
        </p>
    </div>

  <spring:bind path="command.pin">
  <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
    <label for="textPinCode"><spring:message code="pinlogin.pinfield"/></label>
    <c:if test="${status.value==''}">
    <input type="password" focus="true" class="form-control" id="textPinCode" name="pin" value="">
    </c:if>
    <c:if test="${status.value!=''}">
    <input type="text" class="form-control" id="textPinCode" name="pin" value="<c:out value="${status.value}"/>">
    </c:if>
  </div>
  </spring:bind>

  <c:if test="${command.usePassword}">
      <spring:bind path="command.password">
      <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="textPinPass"><spring:message code="pinlogin.password"/></label>
        <input type="password" class="form-control" id="textPinPass" name="password" value="<c:out value="${status.value}"/>">
      </div>
      </spring:bind>
  </c:if>

  <% if (Config.useCaptchaPin) { %>
    <c:if test="${!command.captchaOk}">
    <div class="form-group">
        <p class="help-block"><img src="/captcha.jsp;jsessionid=<%= session.getId() %>" alt="Chaptcha"/></p>

        <label for="captcharesponse"><spring:message code="login.captcha"/></label>
        <input type="text" class="form-control" name="captcharesponse" id="captcharesponse" value=""<c:if test="${command.pin!=''}"> focus="true"</c:if>>

    </div>
    </c:if>
  <% } %>

  <button type="submit" class="btn btn-default">Login</button>

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