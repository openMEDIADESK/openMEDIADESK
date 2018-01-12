<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${og!=null}">
    <meta property="og:url"                content="<c:out value="${og.url}"/>" />
    <meta property="og:type"               content="<c:out value="${og.type}"/>" />
    <meta property="og:title"              content="<c:out value="${og.title}"/>" />
    <meta property="og:description"        content="<c:out value="${og.description}"/>" />
    <c:if test="${not empty og.image}">
    <meta property="og:image"              content="<c:out value="${og.image}"/>" />
    </c:if>
    <meta property="og:site_name"          content="<c:out value="${og.site_name}"/>" />
</c:if>
<c:if test="${og==null}">
    <meta property="og:url"                content="<c:out value="${site}"/>" />
    <meta property="og:type"               content="website" />
    <meta property="og:site_name"          content="<c:out value="${webTitle}"/>" />
</c:if>