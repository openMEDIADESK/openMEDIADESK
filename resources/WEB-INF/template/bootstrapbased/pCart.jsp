<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.image.util.CustomTextService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="CartViewCtrl"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-shopping-cart fa-fw"></i> <spring:message code="shoppingcart.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="shoppingcart.headline"/><small>&nbsp;{{mos.length}} <spring:message code="stat.pics"/></small></h3>
<h4>
<c:if test="${showPriceCreditInfo}">
    <c:if test="${showBalanceInfo}">
        Ihr Guthaben betr&auml;gt: <c:out value="${credits}"/> <c:out value="${config.currency}"/>
    </c:if>
    Gesamt: <c:out value="${subtotalbeforedeposit}"/> <c:out value="${config.currency}"/>
</c:if>
</h4>
<!-- /ordnertitel und infos -->

<!-- WARNUNG/FEHLER nicht genügend Credits -->
   <c:if test="${notEnoughtCredits}">
    <!-- mediadesk abstand -->
    <div class="md-space">&nbsp;</div>
    <!-- /mediadesk abstand -->
   <div class="alert alert-danger">Nicht genügend Guthaben, entfernen Sie einige Dateien aus dem Warenkorb.</div>
   </c:if>
<!-- /nicht genügend Credits

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- Liste/elemente anzeigen -->
<!-- jsp:include page="list.jsp" flush="true"/ -->
<div ng-show="loading"><i class="fa fa-spinner fa-spin"></i></div>
<!-- WARENKORB ################################################################################################################################## -->
<!-- warenkorb -->
<!-- EIN WARENKORB ELEMENT -->
<div ng-repeat="mo in mos" class="media md-border-bottom">
  <div class="media-left">
    <a href="#" ng-click="openPreview($index)"><img class="media-object md-img-thumb" ng-src="/imageservlet/{{mo.ivid}}/1/{{mo.name}}"/></a><br>
    <button ng-click="deleteFromCart(mo.ivid)" type="button" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>&nbsp;entfernen</button>

  </div>
  <div class="media-body">
    <!-- inhalt -->
    <div class="md-download-info">
    <!-- NEU IN DER ANZEIGE WARENKORB -->
        <!-- BILDGROESSE FESTLEGEN DROPDOWN -->
        <!-- Todo: erst in den nächsten versionen
           <div class="form-group form-inline">
        <label for="DatumDrop">Bildgrösse</label>
        <select class="form-control input-sm" id="WarenkorbBildgroesse">
          <option>3000 x 2000 Pixel</option>
          <option>Wert aus Datenbank</option>
          <option>Wert aus Datenbank</option>
        </select>
   		</div>
   		-->
        <!-- BILDGROESSE FESTLEGEN DROPDOWN -->
        <!-- mediadesk abstand -->
        <!-- Todo: erst in den nächsten versionen
        <div class="mediadesk-abstand">&nbsp;</div>
        -->
        <!-- /mediadesk abstand -->
    	<!-- /NEU IN DER ANZEIGE WARENKORB -->
        <!-- Todo: erst in den nächsten versionen
        <div>
            <label>
            <input type="checkbox" id="b1lankoCheckbox" value="option1" aria-label="auswählen">&nbsp;irgendwas
            </label>
        </div>
        -->
        <div class="md-item-text"><span class="lead" ng-bind="mo.title"></span></div>
        <div class="md-item-text" ng-bind="mo.subtitle"></div>
        <c:if test="${showPriceCreditInfo}">
            <div class="md-item-text" ng-bind="'<spring:message code="mediaedit.price"/>: ' + mo.price + ' <c:out value="${config.currency}"/>'"></div>
        </c:if>
        <div class="md-item-text"><small class="text-muted" ng-bind="'# ' + mo.imagenumber"></small></div>
        <div class="md-item-text" ng-show="mo.mayorMime=='image'"><span class="lead" ng-bind="mo.byline"></span></div>
        <div class="md-item-text" ng-show="mo.mayorMime=='image'">Ort: {{mo.site}}</div>
        <div class="md-item-text" ng-show="mo.mayorMime=='image'"><small class="text-muted">Fotograf: {{mo.photographerAlias}}</small></div>
        <div class="md-item-text" ng-show="mo.mayorMime=='image'"><small class="text-muted">Datum: {{mo.photographDate | date:'dd. MMM yyyy'}}</small></div>
        <!-- Todo: erst in den nächsten versionen
        <div>
            <form class="form-inline">
              <div class="form-group">
                <label for="beispielFeldEmail2">Bild als E-Mail versenden:</label>
                <input type="email" class="form-control input-sm" id="beispielFeldEmail2" placeholder="@">
              </div>
              <button type="submit" class="btn btn-default btn-sm"><i class="fa fa-share"></i></button>
            </form>
    	</div>
		<div>
            <label>
            <input type="checkbox" id="b2lankoCheckbox" value="option1" aria-label="AGB">&nbsp;<a href="#">AGB</a> kapiert
            </label>
        </div>
        -->
    </div>
    <!-- inhalt -->
  </div>
    <!-- mediadesk abstand -->
    <div class="md-space">&nbsp;</div>
    <!-- /mediadesk abstand -->

</div>
<!-- /EIN WARENKORB ELEMENT -->

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- MEHR LADEN -->
<button type="button" ng-show="allmos.length>mos.length" ng-click="appendMos()" class="btn btn-default btn-lg btn-block"><small><i class="fa fa-chevron-circle-down"></i>&nbsp;&nbsp;MEHR LADEN&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i></small></button>
<!-- /MEHR LADEN -->

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- WARNUNG/FEHLER nicht genügend Credits -->
   <c:if test="${notEnoughtCredits}">
   <div class="alert alert-danger">Nicht genügend Guthaben, entfernen Sie einige Dateien aus dem Warenkorb.</div>
   </c:if>
<!-- /nicht genügend Credits
<!-- RUNTERLADEN BUTTON -->
   <c:if test="${needCheckout}"><a href="<c:url value="checkout"/>" class="btn btn-success"><i class="fa fa-credit-card"></i>&nbsp;&nbsp;ZUR KASSA&nbsp;&nbsp;<i class="fa fa-credit-card"></i></a></c:if>
   <c:if test="${cartDownload}"><a href="<c:url value="?download=cart"/>" class="btn btn-success"><i class="fa fa-download"></i>&nbsp;&nbsp;DOWNLOAD&nbsp;&nbsp;<i class="fa fa-download"></i></a></c:if>
   <!-- todo idee fuer nächste version <button type="button" class="btn btn-link">Doch nicht, retour oder was auch immer</button> -->
<!-- /RUNTERLADEN BUTTON -->

<!-- /warenkorb -->
<!-- /WARENKORB ################################################################################################################################## -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- /verschachtelung -->
<!-- /row thumbs -->

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>