<%@ page import="java.util.Enumeration"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/cewolf.tld" prefix="cewolf" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>
<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="StatViewCtrl" ng-init="loadStat(1)"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="#"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li><a href="#"><i class="fa fa-folder-open-o fa-fw"></i> Statistik</a></li>
    <li class="active"><i class="fa fa-folder-open-o fa-fw"></i> Download-Statistik</li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>Statistik</h3>
<h4>Downloads</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<button ng-click="loadStat(1)" class="btn btn-default" ng-class="{'btn-primary': interval==1}">Dieses Jahr</button>
<button ng-click="loadStat(2)" class="btn btn-default hidden-xs" ng-class="{'btn-primary': interval==2}">Dieses Monat</button>
<button ng-click="loadStat(3)" class="btn btn-default hidden-xs" ng-class="{'btn-primary': interval==3}">Diese Woche</button>
<button ng-click="loadStat(4)" class="btn btn-default" ng-class="{'btn-primary': interval==4}">Heute</button>
<!--<button type="button" class="btn btn-primary btn-xs" ng-model="showDetails" uib-btn-checkbox btn-checkbox-true="1" btn-checkbox-false="0">Details</button>-->
<a href="/export/statistic" class="btn btn-default"><i class="fa fa-download" aria-hidden="true"></i> Excel Export</a>

<div class="row">

    <div class="col-lg-8 col-sm-12">
        
    <!-- TABELLE -->

        <table class="table table-striped">
          <thead>
            <tr>
              <th>Download-Zeit&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung runter"><i class="fa fa-sort-desc fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung rauf"><i class="fa fa-sort-asc fa-fw"></i></a>--></th>
              <th>Benutzer&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
              <th>Datei / Bild&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
              <th>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
              <th class="hidden-xs">&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
              <th class="hidden-xs">&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
              <th class="hidden-xs">&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
              <th>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
            </tr>
          </thead>
          <tbody>
            <tr ng-repeat="statEntry in stat">
              <td ng-bind="statEntry.downloaddate | date:'dd. MMM yyyy, HH:mm'"></td>
              <td><a tabindex="0" ng-href="<c:url value="/${lng}/useredit?userid="/>{{statEntry.userid}}" ng-bind="statEntry.username"></a></td>
              <td><a tabindex="0" ng-click="openStatsPreview(statEntry)" ng-href="#/{{statEntry.ivid}}" tooltip-animation="true" uib-tooltip="IVID {{statEntry.ivid}}" ng-bind="statEntry.medianame"></a></td>
              <td>&nbsp;</td>
              <td class="hidden-xs">Media# {{statEntry.medianumber}}</td>
              <td class="hidden-xs">IP: {{statEntry.ip}}</td>
              <td class="hidden-xs"><span ng-show="statEntry.formatx>0">Format: {{statEntry.formatx}} x {{statEntry.formaty}}</span>&nbsp;</td>
              <td><a tabindex="0" ng-click="openMediaEdit(statEntry.ivid,'<c:out value="${url}"/>')" href="#"><i class="fa fa-pencil fa-fw md-icon-edit"></i></a></td>
            </tr>
          </tbody>
        </table>
    <!-- /TABELLE -->

    </div>

    <div class="col-lg-4 col-sm-12">

        <div class="panel panel-default">
            <div class="panel-heading">Chart</div>
            <div class="panel-body">
                <canvas id="bar" class="chart chart-bar" chart-data="data" chart-labels="labels"
                                      chart-series="series" chart-options="options" chart-colors=""></canvas>
            </div>
        </div>

        <h4>Speicherplatz: <c:if test="${quotaUsedPercent>90}">Achtung erweitern!</c:if></h4>

        <!-- <p>Used <fmt:formatNumber value="${quotaUsedPercent}"/>% (<c:out value="${quotaUsedPercent}"/>)</p> -->
        <!-- <p>Total <c:out value="${quotaTotalGb}"/>)</p> -->
        <!-- <p>Used <c:out value="${quotaUsedGb}"/>)</p> -->
        <p>Bereitgestellt <fmt:formatNumber value="${quotaTotalGb}"/> GB</p>
        <p>Benutzt <fmt:formatNumber value="${quotaUsedGb}"/> GB</p>
        <p>Verfügbar <fmt:formatNumber value="${quotaAvailGb}"/> GB</p>

    <script type="text/ng-template" id="tooltipQuota.html">
        <p>Bereitgestellt <fmt:formatNumber value="${quotaTotalGb}"/> GB</p>
        <p>Benutzt <fmt:formatNumber value="${quotaUsedGb}"/> GB</p>
        <p>Verfügbar <fmt:formatNumber value="${quotaAvailGb}"/> GB</p>
    </script>

        <div class="progress" uib-tooltip-template="'tooltipQuota.html'">
          <div class="progress-bar" role="progressbar" aria-valuenow="<fmt:formatNumber value="${quotaUsedPercent}" maxFractionDigits="0"/>" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em; width: <fmt:formatNumber value="${quotaUsedPercent}" maxFractionDigits="0"/>%;">
            <fmt:formatNumber value="${quotaUsedPercent}" maxFractionDigits="0"/>%
          </div>
        </div>

    </div>

</div>

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->




    </div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<!-- Hier wird per include der HTML Code fuer Modal Windows eingefuegt -->
<jsp:include page="wPreview.jsp"/>

<jsp:include page="footer.jsp"/>