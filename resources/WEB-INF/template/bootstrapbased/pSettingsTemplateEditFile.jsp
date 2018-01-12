<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

    <c:if test="${command.file!=null}">

    <form method="post" action="<c:url value="settemplateeditfile"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <h3><c:out value="${command.file.name}"/>:</h3>

    <spring:bind path="command.contents">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>">&nbsp;</label>
        <textarea name="<c:out value="${status.expression}"/>" class="form-control" ng-model="templatecode" rows="25" style="font-family:courier new;font-size:12px;width:100%;"><c:out value="${status.value}"/></textarea>
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