<%@ page import="com.stumpner.mediadesk.core.Config"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@page contentType="text/html;charset=utf-8"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<div class="row"><!-- login/register row -->
<!-- LOGIN IM 10breiten INHALT -->
<!-- spalte 1 leer -->
<div class="col-xs-2">
<!-- leer -->
</div>
<!-- /spalte 1 leer -->
<!-- spalte 2 form -->
<div class="col-xs-6">
<!-- hier inhalt 6er spalte mittig -->
<div class="panel panel-default"> <!-- panel -->
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-envelope fa-fw"></i> <spring:message code="contact.headline"/></h3>
                    </div>
	<div class="panel-body">

        <p><c:out value="${text}" escapeXml="no"/></p>

    <!-- panel inhalt -->
<!-- form beispiele -->
<form action="./contact" method="POST">
  <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

                    <spring:bind path="command.name">

                      <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                        <label for="text<c:out value="${status.expression}"/>"><spring:message code="contact.name"/></label>
                        <input type="text" class="form-control" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                      </div>

                    </spring:bind>

                    <spring:bind path="command.email">

                      <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                        <label for="text<c:out value="${status.expression}"/>"><spring:message code="contact.email"/></label>
                        <input type="text" class="form-control" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                      </div>

                    </spring:bind>

                    <spring:bind path="command.telephone">

                      <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                        <label for="text<c:out value="${status.expression}"/>"><spring:message code="contact.telephone"/>:</label>
                        <input type="text" class="form-control" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                      </div>

                    </spring:bind>

                    <spring:bind path="command.notice">

                      <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                        <label for="text<c:out value="${status.expression}"/>"><spring:message code="contact.notice"/>:</label>
                        <textarea type="text" class="form-control" rows="5" cols="5" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                      </div>

                    </spring:bind>

                    <div class="form-group">
                        <p class="help-block"><img src="/captcha.jsp;jsessionid=<%= session.getId() %>" alt="Chaptcha"/></p>
                        <label for="captcharesponse"><spring:message code="login.captcha"/>:</label>
                        <input type="text" class="form-control" name="captcharesponse" id="captcharesponse" value="">
                    </div>

                    <button type="submit" class="btn btn-default"><spring:message code="contact.send"/></button>

                    <spring:hasBindErrors name="command">
                    <spring:bind path="command">
                    <div class="alert alert-danger" role="alert">
                      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                      <span class="sr-only">Fehler</span>
                              <c:forEach items="${status.errorMessages}" var="error">
                                <c:out value="${error}"/><br>
                              </c:forEach>
                    </div>
                    </spring:bind>
                    </spring:hasBindErrors>

</form>


    <!-- /panel inhalt -->
	</div>
	</div>
</div> <!-- /panel -->
</div><!-- /spalte 2 form -->
<!-- spalte 3 leer -->
<div class="col-xs-2">
<!-- leer -->
</div>
<!-- /spalte 3 leer -->
<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->
</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->

<jsp:include page="footer.jsp"/>