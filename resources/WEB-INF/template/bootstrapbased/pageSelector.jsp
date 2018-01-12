<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!--
    number of pages <c:out value="${numberOfPages}"/>
    -->
<c:if test="${numberOfPages>1}">
<div class="pageHeader">
    <c:url var="prevPageUrl" value="${servletMapping}">
      <c:param name="id" value="${containerId}"/>
      <c:param name="page" value="${prevPage}"/>
    </c:url>
    <c:url var="nextPageUrl" value="${servletMapping}">
      <c:param name="id" value="${containerId}"/>
      <c:param name="page" value="${nextPage}"/>
    </c:url>

    <div class="pageInfo">
        <c:if test="${prevPage>0}">
                <a href="<c:out value="${prevPageUrl}"/>" class="prevPageLink"><var>[PREV PAGE]</var><img alt="prev" src="/images/bl.gif" style="border:none;"/></a>
        </c:if>

        <spring:message code="folderview.page"/> <c:out value="${pageIndex}"/> <spring:message code="folderview.of"/> <c:out value="${numberOfPages}"/>

        <c:if test="${nextPage>0}">
                <a href="<c:out value="${nextPageUrl}"/>" class="nextPageLink"><var>[NEXT PAGE]</var><img alt="next" src="/images/bl.gif" style="border:none;"/></a>
        </c:if>
    </div>

    <div class="pageSelector">
            <form name="SeitenAuswahl" method="get" action="<c:url value="${servletMapping}"/>">
                <p>
                  <input type="hidden" name="id" value="<c:out value="${containerId}"/>">
                  <spring:message code="folderview.jumptopage"/>
                  <input name="page" type="text">
                  <input name="SeiteGoOben3" type="submit" class="goButton" value="&gt;&gt;"/>
                </p>
            </form>
    </div>

</div>
</c:if>