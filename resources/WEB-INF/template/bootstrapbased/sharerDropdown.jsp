<%@ page import="com.stumpner.mediadesk.usermanagement.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<c:if test="${!fn:contains(config.param, '-DSx')}">
<!-- button drop teilen -->
  <div class="btn-group btn-group-xs" role="group" aria-label="Teilen" ng-show="allmos.length>0">
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <i class="fa fa-share-alt fa-fw"></i> <spring:message code="mediamenu.share"/>
      <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">

        <mediadesk:login role="<%= User.ROLE_PINMAKLER %>"><li><a ng-href="/{{properties.lng}}/pinwizard"><jsp:include page="iconPin.jsp"/>&nbsp;&nbsp;<spring:message code="imagemenu.aspin"/></a></li></mediadesk:login>
        <c:if test="${podcastEnabled}"><li><a href="<c:out value="${podcastUrl}"/>" target="_blank"><i class="fa fa-rss fa-fw"></i>&nbsp;&nbsp;Podcast</a></li></c:if>
        <li><a href="#" ng-click="shareFb('<c:out value="${fqurlEncoded}"/>')"><i class="fa fa-facebook fa-fw"></i>&nbsp;&nbsp;facebook</a></li>

        <li><a href="https://twitter.com/intent/tweet/?text=<c:out value="${sharerTitle}"/>&url=<c:out value="${fqurlEncoded}"/>&via=mediaDESK&hashtags=web,mediaDESK" target="_blank"><i class="fa fa-twitter fa-fw"></i>&nbsp;&nbsp;twitter</a></li>
        <li><a href="https://www.tumblr.com/widgets/share/tool?canonicalUrl=<c:out value="${fqurlEncoded}"/>&title=<c:out value="${sharerTitle}"/>&caption=<c:out value="${sharerTitle}"/>" target="_blank"><i class="fa fa-tumblr fa-fw"></i>&nbsp;&nbsp;tumblr</a></li>
        <li><a href="https://www.pinterest.com/pin/create/button/?url=<c:out value="${fqurlEncoded}"/>" target="_blank"><i class="fa fa-pinterest-p fa-fw"></i>&nbsp;&nbsp;Pinterest</a></li>
        <li><a href="https://buffer.com/add?text=<c:out value="${sharerTitle}"/>&url=<c:out value="${fqurlEncoded}"/>" target="_blank"><i class="fa fa-share-alt-square fa-fw"></i>&nbsp;&nbsp;buffer</a></li>
        <li><a href="https://delicious.com/save?v=5&provider=mediaDESK&noui&jump=close&url=<c:out value="${fqurlEncoded}"/>&title=<c:out value="${sharerTitle}"/>" target="_blank"><i class="fa fa-delicious fa-fw"></i>&nbsp;&nbsp;delicious</a></li>
        <li><a href="http://digg.com/submit?url=<c:out value="${fqurlEncoded}"/>&title=<c:out value="${sharerTitle}"/>" target="_blank"><i class="fa fa-digg fa-fw"></i>&nbsp;&nbsp;digg</a></li>

        <li><a href="https://plus.google.com/share?url=<c:out value="${fqurlEncoded}"/>" target="_blank"><i class="fa fa-google-plus fa-fw"></i>&nbsp;&nbsp;google+</a></li>
        <!--
        Sharer Links siehe https://github.com/bradvin/social-share-urls und https://jonsuh.com/blog/social-share-links/
        -->
    </ul>
  </div>
  <!-- /button drop teilen -->
</c:if>