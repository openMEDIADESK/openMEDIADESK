<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>

<div id="logo">
    <a href="<c:url value="${home}"/>"><img src="<%= Config.instanceLogo %>" alt="<%= Config.instanceName %>"></a>
</div>
<div id="topsearch">
    <jsp:include page="topsearch.jsp" flush="true" />
</div>
