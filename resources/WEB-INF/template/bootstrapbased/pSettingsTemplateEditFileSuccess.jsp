<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title></title></head>
  <body>

    <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <code><pre><c:out value="${errorMessage}"/></pre></code>
    </div>
    </c:if>

<script type="text/javascript">

    window.opener.location.reload();
<c:if test="${empty errorMessage}">
    window.close();
</c:if>

</script>

  </body>
</html>