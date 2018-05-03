<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.image.util.CustomTextService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<script type="text/javascript">

    window.location = "<c:out value="${site}"/>/<c:out value="${lng}"/>/c?id=0";

</script>

<jsp:include page="footer.jsp"/>