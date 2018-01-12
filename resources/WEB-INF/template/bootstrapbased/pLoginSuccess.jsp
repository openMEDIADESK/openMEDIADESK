<%@ page import="java.util.Enumeration,
                 com.stumpner.mediadesk.core.Config"%>
<%@ page import="java.util.Locale" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<div id="breadcrumb">
    <p>
        <a href="<c:url value="${home}"/>">&raquo; HOME</a>
        <c:forEach items="${model.navItems}" var="navItem">
            &nbsp;/ <a href="<c:out value="${navItem.url}"/>"><c:out value="${navItem.name}"/></a>
        </c:forEach>
        &nbsp;/ <spring:message code="login.headline"/>
    </p>
</div>

<div id="contentHead">
        <h1><spring:message code="login.headline"/></h1>
        <h2><spring:message code="login.succeed"/></h2>
</div>

<div id="loginsuccesspage">

    <p>
        <spring:message code="login.successmsg"/>
    </p>
    <c:if test="${isAutoRedirect}">
        <a href="<c:url value="${home}"/>"><spring:message code="login.redirect"/></a>
    </c:if>
</div>

<jsp:include page="footer.jsp"/>