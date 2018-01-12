<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${fn:contains(config.param, '-LOGOL')}">
<!-- Logo seitlich -->

<div class="md-logo-tree">
<img src="/logo2.png<c:out value="${cacheFix}"/>" class="img-responsive" alt="mediaDESK" title="mediaDESK" border="0"/>
</div>

<div class="md-space">&nbsp;</div>

<!-- /Logo seitlich -->
</c:if>