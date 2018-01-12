<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <!--<li><a href="#"><i class="fa fa-folder-open-o fa-fw"></i> Benutzer</a></li>-->
    <li class="active"><i class="fa fa-ticket fa-fw"></i> <spring:message code="pinmanager.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="pinmanager.headline"/></h3>
<h4><spring:message code="pinmanager.subheadline"/></h4>
<!-- /ordnertitel und infos -->

<c:if test="${pinList.size()<2}">
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<div class="alert alert-warning" role="alert">

    <strong>Mit den PIN-Freigeban k&ouml;nnen Sie Dateien und Bildsammlungen mit einem Link oder Code an beliebig viele Empf&uuml;nger senden</strong> <br/>

    Wie man PIN-Freigaben anlegt, erfahren Sie in unserem <a href="https://youtu.be/QgzfOODGiSc" target="_blank" class="alert-link">Video</a> oder im Wiki unter <a href="https://wiki.openmediadesk.net/de/PIN-Freigaben" target="_blank" class="alert-link">https://wiki.openmediadesk.net/de/PIN-Freigaben</a>! <br/>

</div>
</c:if>

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- hier startet mit ROW eine neue zeile mit den div als thumbcontainer -->
<!-- verschachtelung -->
<div class="row"> <!-- thumb row -->

<!-- #################################################################### -->
<!-- ###################### THUMB ELEMENTE AB HIER ######################-->

</div><!-- /thumb row ende und zu -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<a href="<c:url value="/${lng}/pinlist?func=add"/>" class="btn btn-default"><spring:message code="pinmanager.add"/></a>


<!-- TABELLE -->

    <table class="table table-striped">
      <thead>
        <tr>
          <th>PIN-Code&nbsp;</th>
          <!--<th>&nbsp;</th>-->
          <th><spring:message code="fileview.grid.title"/>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung runter"><i class="fa fa-sort-desc fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung rauf"><i class="fa fa-sort-asc fa-fw"></i></a>--></th>
          <th><spring:message code="pinmanager.used"/>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th><spring:message code="pinedit.createdate"/>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
        </tr>
      </thead>
      <tbody>

        <c:forEach items="${pinList}" var="pin" varStatus="stat">


        <tr>
          <td scope="row"><c:out value="${pin.pin}"/></td>
          <td><c:out value="${pin.pinpicTitle}"/></td>
          <td><c:out value="${pin.used}"/></td>
          <td><dt:format pattern="yyyy-MM-dd" default=""><c:out value="${pin.createDate.time}"/></dt:format></td>
          <td>
              <a href="#" ng-click="openPinPopup('<c:out value="${site}"/>/<c:out value="${lng}"/>/pin?pin=<c:out value="${pin.pin}"/>')"><i class="fa fa-link"></i></a>&nbsp;
              <a href="<c:url value="pinview"/>?pinid=<c:out value="${pin.pinpicId}"/>" title="zeigen"><i class="fa fa-database"></i></a>&nbsp;
              <a href="<c:url value="pinedit"/>?pinid=<c:out value="${pin.pinpicId}"/>" title="bearbeiten"><i class="fa fa-pencil fa-fw"></i></a>&nbsp;
              <a href="<c:url value="pindelete"/>?pinid=<c:out value="${pin.pinpicId}"/>" title="löschen"><i class="fa fa-trash"></i></a>&nbsp;</td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
<!-- /TABELLE -->

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- /TABELLE -->

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

    </div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<!-- PIN POPUP -->
<script type="text/ng-template" id="pinPopup.html">

<div class="modal-content">

      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>
      <span class="modal-title" id="meinModalLabel">PIN-Link</span>
      </div>

      <div class="md-modal-body">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row"><!-- row -->
              <div class="col-md-12">
                <form ng-submit="ok()">
                  <div class="form-group">
                    <label><spring:message code="mediamenu.pinlink"/> </label>
                    <input onfocus="this.select()" type="text" class="form-control" ng-model="pinlink" autofocus>
                  </div>
                </form>
              </div>
          </div><!-- /row -->
          </div><!-- /container -->
      <!-- /INHALT MODAL -->
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary" type="button" ng-click="$close();">OK</button>
      </div>
      </div>
<!-- /modal -->
<!-- /MODAL PARKING ####################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
</script>


<!-- ENDE PIN POPUP -->

<jsp:include page="footer.jsp"/>
