<%@ page import="com.stumpner.mediadesk.core.Config"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- Für das Modale Fenster für die Bildvorschau -->
<!-- jsp:include page="modalparking.jsp"/ -->

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="ThumbnailViewCtrl" ng-init="initMosView('/api/rest/category',<c:out value="${containerId}"/>,'<%= request.getAttribute("listView") %>',<c:out value="${selectedImages}"/>)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li class="active"><i class="fa fa-folder-open-o fa-fw"></i> <spring:message code="forbidden.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->

<div class="alert alert-danger" role="alert">

    <h3><spring:message code="forbidden.subheadline"/></h3>

    <p><strong><spring:message code="forbidden.text"/></strong></p>

    <small><spring:message code="forbidden.helptext"/></small>

</div>

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<!-- MODAL PARKING ####################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
<!-- FÜR VORSCHAUPOPUP und fehlende Zugriffsberechtigung (Workaround für endgültigelösung bug100 -->
<script type="text/ng-template" id="mediapreviewScript.html">

<div class="modal-content md-modal-preview">
      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body md-modal-body">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row" ng-swipe-left="nextPreview()" ng-swipe-right="prevPreview()"><!-- row -->
              <div class="col-md-12">
                Keine Zugriffsberechtigung für Vorschau
              </div>
          </div><!-- /row -->
          </div><!-- /container -->
      <!-- /INHALT MODAL -->
      </div>
      </div>
<!-- /modal -->
<!-- /MODAL PARKING ####################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
</script>

<jsp:include page="footer.jsp"/>