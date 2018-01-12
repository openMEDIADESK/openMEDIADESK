<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<div id="breadcrumb">
    <p>
        <a href="<c:url value="${home}"/>">&raquo; HOME</a>
        <c:forEach items="${breadCrumb}" var="navItem">
            <c:url var="thisCatLink" value="${navItem.url}">
                <c:param name="id" value="${navItem.id}"/>
            </c:url>
            &nbsp;/
            <c:if test="${navItem.showFolder}">
                <img alt="folder" src="/img/o_folder.gif" style="border:none;vertical-align:top;"/>
            </c:if>
            <a href="<c:out value="${thisCatLink}"/>"><c:out value="${navItem.title}"/></a>
        </c:forEach>
            &nbsp;/ Bild versenden
    </p>
</div>

<div id="contentHead">
        <h1>Bild per Email versenden</h1>
        <h2>&nbsp;</h2>
</div>

<div id="sendsuccesspage">

    <p>
        Das Bild wurde versendet.
    </p>

    <c:url var="url" value="${redirectUrl}">
    </c:url>

    <form action="<c:out value="${url}"/>" method="get">
    <p>
        <input type="button" onclick="javascript:document.location='<c:out value="${url}"/>';" value="weiter"/>
    </p>
    </form>

</div>

<jsp:include page="footer.jsp"/>