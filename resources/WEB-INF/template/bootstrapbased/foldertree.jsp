<%@ page import="java.util.Enumeration,com.stumpner.mediadesk.usermanagement.User,com.stumpner.mediadesk.core.Config,org.apache.log4j.Logger"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@page contentType="text/html;charset=utf-8"%><%  response.setHeader("Pragma", "no-cache"); %><%  response.setHeader("Cache-Control", "no-cache"); %><%  response.setHeader("Cache-Control","no-store" ); %><%  response.setDateHeader("Expires", 0); %>

<!-- BAUM -->

<!-- Template für das Popover -->
<script type="text/ng-template" id="folderEditPopover.html">
    <div><a ng-href="/{{properties.lng}}/folderedit?parent={{node.id}}" ng-show="node.level<9" class="md-treelinkcolor md-treenewcolor"><jsp:include page="iconAdd.jsp"/> <spring:message code="sm.cat.createnew"/></a></div>
    <div><a ng-href="/{{properties.lng}}/folderedit?id={{node.id}}&redirect=<c:out value="${url}"/>" class="md-treelinkcolor"><jsp:include page="iconEdit.jsp"/> <spring:message code="mediamenu.edit"/></a></div>
    <div><a ng-href="/{{properties.lng}}/folderbreakup?id={{node.id}}" class="md-treelinkcolor md-treedelcolor"><jsp:include page="iconDelete.jsp"/> <spring:message code="imagemenu.delete"/></a></div>
</script>

<!-- START BAUM EIGENBAU UND EIGENBAU css ###################################################################################### -->

<div id="balloon">
    <!-- Positioningballon damit sich der Baum nach unten schiebt wenn gescrollt wird -->
</div>

<jsp:include page="logoLeft.jsp"/>

<div ng-controller="TreeViewCtrl" ng-init="openTreePath(<c:out value="${folderPathArray}"/>);<c:if test="${folderId!=null}">folderId=<c:out value="${folderId}"/>;</c:if>" id="foldertree"> <!-- umschliessender Div für den Baum (Controller)
<!-- div falls wir da oben noch eine Baum-Navi brauchen - sonst weg -->
<!-- <div>Baum Navi</div> -->
<!-- /div falls wir da oben noch eine Baum-Navi brauchen - sonst weg -->
<!-- Neuen Root Ordner erstellen -->
<div ng-show="properties.role>=10" class="md-treelevel md-tree-level0"><a ng-href="/{{properties.lng}}/folderedit?parent=0" class="md-treelinkcolor md-treenewcolor"><jsp:include page="iconAddFolder.jsp"/>&nbsp;<spring:message code="sm.cat.createnew"/></a></div>

<div class="md-tree-droproot" ui-on-drop="onTreeNodeDrop(null, $data, $event)">&nbsp;</div>

<div ng-repeat="node in nodes" class="md-treelevel md-tree-level{{node.level}}" ui-draggable="true" drag="node" ui-on-drop="onTreeNodeDrop(node, $data, $event)">
    <a tabindex="0" href="#folderpopover" ng-show="properties.role>=10" uib-popover-template="'folderEditPopover.html'" popover-append-to-body="true" popover-trigger="outsideClick" popover-placement="bottom-left" class="md-treeiconedit md-iconsettings-sm" title="EDIT2" alt="EDIT1"><i class="fa fa-cog fa-fw"></i></a>
    <a href="#folderopen" ng-click="onFolderNodeClick(node,$index)" class="md-treeiconcolor">
        <!-- i class="fa {{node.arrowcssclass}} fa-fw"></i> -->
        <jsp:include page="iconFoldertree.jsp"/>
        <!--<div ng-show="node.isLoading"><i class="fa fa-spinner fa-spin"></i></div>-->
    </a>
    <a ng-href="/{{properties.lng}}/c?id={{node.id}}" class="md-treelinkcolor" ng-bind="node.title"></a>
</div>

<div class="md-treeloadspinner" ng-show="treeloading"><jsp:include page="iconSpinner.jsp?size=3"/><!--<i class="fa fa-spinner fa-3x fa-spin"></i>--></div>

<!-- ENDE BAUM EIGENBAU UND EIGENBAU css ###################################################################################### -->
</div>
<!-- /BAUM -->