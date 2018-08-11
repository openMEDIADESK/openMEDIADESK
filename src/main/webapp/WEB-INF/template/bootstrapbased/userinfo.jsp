<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<mediadesk:login>
            <div id="username">
            <c:out value="${user.userName}"/> <c:if test="${creditSystemEnabled}">| Guthaben: <c:out value="${user.credits}"/></c:if>
            </div>
</mediadesk:login>