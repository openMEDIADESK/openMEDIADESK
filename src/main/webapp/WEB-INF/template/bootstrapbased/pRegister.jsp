<%@ page import="com.stumpner.mediadesk.core.Config" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>

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
                    <h3 class="panel-title"><i class="fa fa-user-plus fa-fw"></i> <spring:message code="register.headline"/></h3>
                </div>
                <div class="panel-body">
                    <!-- panel inhalt -->
                    <!-- form beispiele -->
                    <form method="post" action="<c:url value="register"/>">
                        <spring:bind path="command.firstName">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.firstname"/></label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.lastName">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.lastname"/></label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.email">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.email"/>*</label>
                            <input type="email" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <c:if test="${showUserTelFaxFields}">
                        <spring:bind path="command.phone">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.phone"/></label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.fax">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.fax"/></label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->    
                        </c:if>
                        <c:if test="${showUserCompanyFields}">
                        <spring:bind path="command.company">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.company"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.companyType">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.companytype"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->  
                        </c:if>
                        <c:if test="${showUserAddressFields}">
                        <spring:bind path="command.street">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.street"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.city">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.city"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.zipCode">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.zipcode"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->
                        <spring:bind path="command.country">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.contry"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- -->  
                        </c:if>
                        <c:if test="${!userEmailAsUsername}">
                        <spring:bind path="command.userName">
                        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                            <label for="frminput<c:out value="${status.expression}"/>"><spring:message code="register.username"/>*</label>
                            <input type="text" class="form-control" id="frminput<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        </div>
                        </spring:bind>
                        <!-- --> 
                        </c:if>
                        <div class="checkbox"> <!-- AGB + Links -->
                            <label>
                                <input type="checkbox" name="agreeTAC" id="agreeTAC"> <spring:message code="register.tac.agree1"/>
                                <a href="#" ng-click="openContentPopup('<c:url value="/${lng}/popup/tac"/>')"><spring:message code="register.tac"/></a> <spring:message code="register.tac.agree2"/>
                            </label>
                        </div>
                        <!-- -->
                        <% if (Config.useCaptchaRegister) { %>
                        <div class="form-group">
                            <p class="help-block"><img src="/captcha.jsp;jsessionid=<%= session.getId() %>" alt="Chaptcha"/></p>

                            <label for="beispielFeldDatei"><spring:message code="login.captcha"/></label>
                            <input type="text" name="captcharesponse" value="">


                        </div>
                        <!-- -->
                        <% } %>

                        <button type="submit" class="btn btn-default"><spring:message code="register.submit"/></button>

                        <div class="form-group">
                            <!--
                            <label for="beispielFeldDatei">Anhang</label>
                            <input type="file" id="beispielFeldDatei"> -->

                            <p class="help-block"><spring:message code="register.login1"/> <a href="login"><spring:message code="register.login"/></a> <spring:message code="register.login2"/></p>
                        </div>

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
        </div>
        <!-- /panel -->
    </div>
    <!-- /spalte 2 form -->
    <!-- mediadesk abstand -->
    <div class="md-space-lg">&nbsp;</div>
    <!-- /mediadesk abstand -->
    <!-- ###################################################################################################################################################### -->
    <!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->
</div>
<!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->

<jsp:include page="footer.jsp"/>