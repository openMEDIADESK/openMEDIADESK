<%@ page import="java.util.Enumeration,
                 com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config,
                 org.apache.log4j.Logger"%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@page contentType="text/html;charset=utf-8"%><%  response.setHeader("Pragma", "no-cache"); %><%  response.setHeader("Cache-Control", "no-cache"); %><%  response.setHeader("Cache-Control","no-store" ); %><%  response.setDateHeader("Expires", 0); %>


</div> <!-- row in der SPALTE 1 und SPALTE 2 sind -->
</div><!-- container fluid der die row für spalte 1 und spalte 2 trägt -->

<!-- ##### /SEITENINHALT MIT container und row für die 2 spalten ##### -->
<!-- ##################################################################################################################################################### -->

<!-- ##################################################################################################################################################### -->
<!-- ##### Footer container  ##### -->

<div class="container-fluid md-footer-container"><!-- container fluid -->
<!-- FOOTER 1 LOGOS -->
<div class="row"><!-- row -->
<footer>
   <c:if test="${!fn:contains(licFunc, '-noads')}">
       <!--
   <ul class="md-footer">
       	<li><a href="#" target="_blank" alt="" title=""><img src="/i1.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i2.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i3.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i4.png" alt="" border="0"/></a></li>
        <li><a href="#" target="_blank" alt="" title=""><img src="/i5.png" alt="" border="0"/></a></li>
    	<li><a href="#" target="_blank" alt="" title=""><img src="/i6.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i7.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i8.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i9.png" alt="" border="0"/></a></li>
   		<li><a href="#" target="_blank" alt="" title=""><img src="/i10.png" alt="" border="0"/></a></li>
        <li><a href="#" target="_blank" alt="" title=""><img src="/i11.png" alt="" border="0"/></a></li>
        <li><a href="#" target="_blank" alt="" title=""><img src="/i12.png" alt="" border="0"/></a></li>
        <li><a href="#" target="_blank" alt="" title=""><img src="/i13.png" alt="" border="0"/></a></li>
        <li><a href="#" target="_blank" alt="" title=""><img src="/i14.png" alt="" border="0"/></a></li>
        <li><a href="#" target="_blank" alt="" title=""><img src="/i15.png" alt="" border="0"/></a></li>
       <mediadesk:login role="<%= User.ROLE_ADMIN %>">
        <li>
            <span class="help-text">Sie möchten hier keine Werbung sehen?</span>
            <a href="<c:url value="https://openmediadesk.org/signup/premium.do?id=${licId}&url=${url}"/>" target="_blank" class="btn btn-warning">Upgrade!</a>
        </li>
       </mediadesk:login>
       </ul>
       -->
       </c:if>
</footer>
</div><!-- /row -->
</div><!-- /container fluid -->
<!-- /FOOTER 1 LOGOS -->


<!-- MEDIDADESK FOOTER MIT HINTERGRUND -->

<div class="container-fluid md-footer-bg"><!-- container fluid -->
<!-- ZEILE 1 -->
<!-- FOOTER MIT NAVIGATION - die div spalten mit col-cx-2 stehen lassen, nur die UL für nicht angemeldete ausblenden -->
<div class="row">
    <div class="col-sm-2">
    	<i class="fa fa-copyright"></i> <c:out value="${config.footerCopyright}"/>
    </div>
    <div class="col-sm-2">
    	<ul class="list-unstyled">
        <li><a href="<c:url value="/${lng}/contact"/>"><spring:message code="menu.contact"/></a></li>
        <li><a href="<c:url value="/${lng}/contact"/>"><spring:message code="footer.imprint"/></a></li>
        <!--<li><a href="#">Datenschutz</a></li>-->
        <li><a href="#" ng-click="openContentPopup('<c:url value="/${lng}/popup/tac"/>')"><spring:message code="footer.termsconditions"/></a></li>
        <li><a href="<c:out value="${config.footerCorpLink}"/>"<c:if test="${home!=config.footerCorpLink}"> target="_blank"</c:if>><c:out value="${config.footerCorpSite}"/></a></li>

        <c:forEach items="${footer2Links}" var="menue">
            <c:url value="${menue.linkUrl}" var="linkUrl">
                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
            </c:url>
            <li><a href="<c:url value="${linkUrl}"/>"><c:out value="${menue.title}"/></a></li>
        </c:forEach>

        </ul>
    </div>
    <div class="col-sm-2">
    	<ul class="list-unstyled">
        <mediadesk:login>
        <c:if test="${showLightbox}"><li><a href="<c:url value="/${lng}/f"/>"><spring:message code="left.lightbox"/> <span class="label label-success" ng-bind="properties.favCount"></span></a></li></c:if>
        <c:if test="${useShoppingCart}"><li><a href="<c:url value="/${lng}/shop"/>"><spring:message code="left.shoppingcart"/> <span class="label label-success" ng-bind="properties.cartCount"></span></a></li></c:if>
        <li ng-show="properties.role>=10"><a href="<c:url value="/${lng}/folderedit?parent=${folder.folderId}"/>"><spring:message code="sm.cat.createnew"/></a></li>
        </mediadesk:login>
        <c:forEach items="${footer3Links}" var="menue">
            <c:url value="${menue.linkUrl}" var="linkUrl">
                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
            </c:url>
            <li><a href="<c:url value="${linkUrl}"/>"><c:out value="${menue.title}"/></a></li>
        </c:forEach>
        </ul>
    </div>
    <div class="col-sm-2">
    	<ul class="list-unstyled">
        <c:if test="${!fn:contains(licFunc, '-noads')}">
        <li><a href="https://openmediadesk.org" target="_blank">openMEDIADESK.org</a></li>
        <li><a href="http://www.stumpner.com" target="_blank">stumpner.com</a></li>
        </c:if>
        <mediadesk:login role="<%= User.ROLE_ADMIN %>">
        <li><a href="<c:url value="/${lng}/pinlist"/>"><spring:message code="menu.pinlist"/></a></li>
        <li><a href="<c:url value="/${lng}/stat"/>"><spring:message code="menu.stats"/></a></li>
        <li><a href="<c:url value="/${lng}/settings"/>"><spring:message code="menu.settings"/></a></li>
        <li><a href="<c:url value="/${lng}/usermanager"/>"><spring:message code="menu.usermanager"/></a></li>
        </mediadesk:login>
        <c:forEach items="${footer4Links}" var="menue">
            <c:url value="${menue.linkUrl}" var="linkUrl">
                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
            </c:url>
            <li><a href="<c:url value="${linkUrl}"/>"><c:out value="${menue.title}"/></a></li>
        </c:forEach>
        </ul>
    </div>
    <div class="col-sm-2">
    	<ul class="list-unstyled">
        <mediadesk:login notLoggedIn="true">
            <li><a href="<c:url value="/login"/>"><spring:message code="menu.login"/></a></li>
            <c:if test="${config.allowRegister}">
            <li><a href="/register"><spring:message code="menu.register"/></a></li>
            </c:if>
        </mediadesk:login>
        <mediadesk:login>
            <li><a href="<c:url value="/${lng}/password"/>"><spring:message code="menu.setpassword"/></a></li>
        </mediadesk:login>
        <c:forEach items="${footer5Links}" var="menue">
            <c:url value="${menue.linkUrl}" var="linkUrl">
                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
            </c:url>
            <li><a href="<c:url value="${linkUrl}"/>"><c:out value="${menue.title}"/></a></li>
        </c:forEach>
        </ul>
    </div>
    <div class="col-sm-2"><p class="pull-right"><a href="#top"><spring:message code="goup"/> <i class="fa fa-arrow-circle-up"></i></a></p></div>
</div>
<!-- /ZEILE 1 -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ZEILE 2 -->
<div class="row">

	<div class="col-xs-12">

      <footer>
      <p class="text-center"><span class="text-info"><small><i class="fa fa-copyright"></i>  <c:out value="${config.footerCopyright}"/> - <spring:message code="footer.copyright"/></small></span></p>
      </footer>

	</div>

</div>

<!-- /ZEILE 2 -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- ZEILE 3 -->
<div class="row">

	<div class="col-xs-12">

      <footer class="text-muted">
      <small>We use Bootstrap to support the latest versions of <i class="fa fa-chrome"></i> <i class="fa fa-internet-explorer"></i> <i class="fa fa-firefox"></i> <i class="fa fa-opera"></i> <i class="fa fa-safari"></i></span> on different plattforms. Please use a modern browser!</small>
      <!-- top link als anker checken! -->
      </footer>

	</div>

</div>
<!-- /ZEILE 3 -->

</div> <!-- /container fluid -->
<!-- ##### /Footer container  ##### -->
<!-- ##################################################################################################################################################### -->
<!-- Toaster Container -->
<toaster-container toaster-options="{'position-class': 'toast-bottom-right'}"></toaster-container>
<!-- Ende Toaster Container -->
<!-- Bootstrap-JavaScript -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- salvattore-JavaScript -->
<!-- script src="/js/salvattore.js"></script> -->
<!-- AngularJS 1.4.7  vormals 1.4.x Minified (IT/Sf) -->
<!-- <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.min.js"></script> -->
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-animate.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-sanitize.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-touch.js"></script>
<!-- Bootstrap components written in pure AngularJS by the AngularUI Team https://angular-ui.github.io/bootstrap/ -->
<script src="//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-1.3.2.js"></script>
<!-- AngularJS Modul ng-Dialog -->
<script type="text/javascript" src="/app/lib/angular/ngDialog.js"></script>
<!-- flow.js + ng-flow libraries https://github.com/flowjs/ng-flow -->
<script type="text/javascript" src="/app/lib/angular/ng-flow-standalone.min.js"></script>
<!-- http://www.chartjs.org/docs/ -->
<script src="/app/lib/angular/Chart.js"></script>
<!-- http://jtblin.github.io/angular-chart.js/ -->
<script src="/app/lib/angular/angular-chart.js"></script>
<!-- videogular http://www.videogular.com/tutorials/how-to-start/ -->
<script src="/app/lib/videogular/videogular.js"></script>
<script src="/app/lib/videogular-controls/vg-controls.js"></script>
<script src="/app/lib/videogular-overlay-play/vg-overlay-play.js"></script>
<script src="/app/lib/videogular-poster/vg-poster.js"></script>
<script src="/app/lib/videogular-buffering/vg-buffering.js"></script>
<!-- angular JS Toaster https://github.com/jirikavi/AngularJS-Toaster -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/angularjs-toaster/1.1.0/toaster.min.js"></script>
<!-- ng-timago http://ngmodules.org/modules/ngtimeago -->
<script src="/app/lib/ng-timeago/ngtimeago.js"></script>
<!-- mediaDESK AngularJS App -->
<script type="text/javascript" src="/app/scripts/mediadeskangular.js<c:out value="${cacheFix}"/>"></script>
<!-- ng-wig WYSIWYG Editor https://github.com/stevermeister/ngWig -->
<script type="text/javascript" src="/app/lib/ng-wig/ng-wig.min.js"></script>
<!-- drag&Drop http://marceljuenemann.github.io/angular-drag-and-drop-lists -->
<!--
<script type="text/javascript" src="/app/lib/angular-drag-and-drop-lists/angular-drag-and-drop-lists.min.js"></script>
-->
<!-- drag&Drop http://angular-dragdrop.github.io/angular-dragdrop/ -->
<script type="text/javascript" src="/app/lib/angular-dragdrop/draganddropfork.js"></script>
<!-- Masonry -->
<!--
<script src="https://cdnjs.cloudflare.com/ajax/libs/masonry/3.3.2/masonry.pkgd.min.js"></script>
-->
<!-- Angular-masonry-directive -->
<!--
<script src="/js/angular-masonry-directive.js"></script>
-->

<script type="text/javascript" src="/js/affix.js"></script>
<c:if test="${fn:contains(config.param, '-SLIDE')}">
<!-- fancybox lightbox -->
<script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.4.2/jquery.fancybox.js"></script>
</c:if>

<script type="text/javascript">



</script>

<!--
mediaDESK Version: <%= Config.versionNumbner %>
mediaDESK VerDate: <%= Config.versionDate %>
-->

<%= Config.statCounterCode %>
<%= Config.googleAnalytics %>

</body>
</html>