<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<div id="breadcrumb">
    <p>
        <a href="<c:url value="${home}"/>">&raquo; HOME</a>
        <c:forEach items="${model.navItems}" var="navItem">
            &nbsp;/ <a href="<c:out value="${navItem.url}"/>"><c:out value="${navItem.name}"/></a>
        </c:forEach>
        &nbsp;/ <spring:message code="imagedelete.headline"/>
    </p>
</div>

<div id="contentHead">
        <h1><spring:message code="imagedelete.headline"/></h1>
        <h2><spring:message code="imagedelete.subheadline"/></h2>
</div>

<div id="deletepage">

    <c:if test="${deleteCount==0}">
        <p><spring:message code="imagedelete.empty"/></p>
        <form action="#" method="post">
            <input id="ok" onClick="history.go(-1);" name="reset" type="submit" class="actionButton resetButton" value="<spring:message code="message.ok"/>">
        </form>
    </c:if>

    <c:if test="${deleteCount>0}">

        <h3><spring:message code="imagedelete.info"/></h3>

        <form action="<c:url value="deletemedia"/>" method="post">
            <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>
            <input id="resetButtonTop" name="reset" type="submit" class="actionButton resetButton" value="<spring:message code="imagedelete.no"/>">
            <input id="downloadButtonTop" name="submit" type="submit" class="actionButton submitButton" value="<spring:message code="imagedelete.yes"/>">
        </form>

            <div class="deleteList">
                <% int i = 1; %>
                <c:forEach items="${deleteList}" var="image">
                <% i++; %>
                    <div class="deleteEntry">
                      <div class="downloadImage">
                          <c:if test="${image.mayorMime=='image'}"><a href="/<c:out value="${lng}"/>/image?id=<c:out value="${image.ivid}"/>" target="001" onClick="window.open('','001','scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=650,height=540');"><img alt="image" src="/imageservlet/<c:out value="${image.ivid}"/>/1/image.jpg" border="0"></a></c:if>
                      </div>
                      <div class="deleteData">
                          <span class="mediaNumber"><small># <c:out value="${image.mediaNumber}"/></small></span>
                          <span class="versionTitle"><c:out value="${image.versionTitle}"/></span>
                      </div>
                    </div>
                </c:forEach>
            </div>

        <form action="<c:url value="deletemedia"/>" method="post">
            <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>
            <input id="resetButtonTop2" name="reset" type="submit" class="actionButton resetButton" value="<spring:message code="imagedelete.no"/>">
            <input id="downloadButtonTop2" name="submit" type="submit" class="actionButton submitButton" value="<spring:message code="imagedelete.yes"/>">
        </form>

    </c:if>

</div>

<jsp:include page="footer.jsp"/>