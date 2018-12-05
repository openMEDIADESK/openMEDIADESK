<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.MediaObject" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
    <!-- hochladen in Tabellenansicht -->
    <c:if test="${uploadEnabled}">
        <!-- Upload -->
        <div ng-show="mosView=='list'" type="button" class="btn btn-default btn-lg btn-block"
             flow-init="{target: getUploadTarget, chunkSize:201073741824, simultaneousUploads:1}"
             flow-files-submitted="$flow.upload()"
             flow-file-success="uploadFileSuccess($file, $message, $flow)"
             flow-file-error="uploadFileError($file, $message, $flow)"
             flow-complete="uploadComplete($flow)"
             flow-error="uploadError($file, $message, $flow)"
             flow-btn
             flow-drop><i class="fa fa-cloud-upload md-text-prim md-thumb-item-upload"></i>
            <div class="md-item-text" ng-show="$flow.files.length==0"><span class="lead"><strong>UPLOAD</strong></span><br /><small><spring:message code="dropfileshere"/></small></div>
            <!-- statuszeilen -->
            <div>
                <div class="md-item-text" ng-show="hasUploadError"><span class="text-danger"><small>{{errorMessage}} <i class="fa fa-exclamation-triangle"></i></small></span></div>
                <div class="md-item-text" ng-repeat="file in $flow.files">
                    <span class="text-primary" ng-show="file.progress(true)==0"><small>{{file.name}} {{file.msg}}</small></span>
                    <span class="text-info" ng-show="file.isUploading() && file.progress(true)!=0"><small>{{file.progress(true)*100 | number:0}}% {{$index+1}} {{file.name}} {{file.msg}} <i class="fa fa-spinner fa-spin"></i></small></span>
                    <span class="text-success" ng-show="file.isComplete() && !file.error"><small>100% {{file.name}} {{file.msg}}<i class="fa fa-thumbs-o-up"></i></small></span>
                    <span class="text-danger" ng-show="file.error"><small>{{file.name}} {{file.msg}}<i class="fa fa-exclamation-triangle"></i></small></span>
                </div>
            </div>
            <!-- / statuszeilen -->
        </div>
        <!-- /Upload -->
    </c:if>
<!-- TABELLE -->
    <table class="table table-striped" ng-show="mosView=='list' && (allmos.length>0 || subfolders.length>0)">
      <thead>
        <tr>
          <th class="md-table-col-icon">&nbsp;</th>
          <th>&nbsp;</th>
          <th ng-show="allmos.length>0"><spring:message code="fileview.grid.title"/>&nbsp;<a ng-href="?id={{containerId}}&sortBy=2&orderBy={{orderBy}}" ng-show="{{sortBy!=2 && containerId!=-1}}" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>&nbsp;
                         <a ng-href="?id={{containerId}}&sortBy=2&orderBy=1" ng-show="{{sortBy==2 && orderBy==2}}" title="Sortierung runter"><i class="fa fa-sort-desc fa-fw"></i></a>&nbsp;
                         <a ng-href="?id={{containerId}}&sortBy=2&orderBy=2" ng-show="{{sortBy==2 && orderBy==1}}" title="Sortierung rauf"><i class="fa fa-sort-asc fa-fw"></i></a></th>
          <th ng-show="allmos.length>0"><spring:message code="tm.sortby.date"/>&nbsp;<a ng-href="?id={{containerId}}&sortBy=6&orderBy={{orderBy}}" ng-show="{{sortBy!=6 && containerId!=-1}}" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>&nbsp;
                         <a ng-href="?id={{containerId}}&sortBy=6&orderBy=1" ng-show="{{sortBy==6 && orderBy==2}}" title="Sortierung runter"><i class="fa fa-sort-desc fa-fw"></i></a>&nbsp;
                         <a ng-href="?id={{containerId}}&sortBy=6&orderBy=2" ng-show="{{sortBy==6 && orderBy==1}}" title="Sortierung rauf"><i class="fa fa-sort-asc fa-fw"></i></a></th>
          <th ng-show="allmos.length>0"><spring:message code="fileview.grid.type"/>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th ng-show="allmos.length>0"><spring:message code="fileview.grid.size"/>&nbsp;<!-- <a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <c:if test="${showImageActionMenu}">
          <th class="text-right">&nbsp;</th>
          </c:if>
        </tr>
      </thead>
      <tbody>
        <tr ng-repeat="mo in mos" ui-draggable="true" drag="mo" drop-effect="copyMove" on-drop-success="dropSuccessHandler($event,$index,mo)">
          <td scope="row"><i class="fa fa-file-{{mo.mime | mimeIconClass}}-o" ng-click="openPreview($index)"></i></td>
		  <td>
        <label>
          <c:if test="${showSelect==true}"><input type="checkbox" ng-model="mo.selected" ng-Change="onSelectChange($index)"></c:if>
        </label>
          </td>
          <td class="text-left"><a tabindex="0" ng-click="openPreview($index)" ng-href="#/{{mo.ivid}}" ng-bind="mo.title"></a></td>
          <td ng-bind="mo.createDate | date:'dd. MMM yyyy'"></td>
          <td ng-bind="mo.mime"></td>
          <td ng-bind="mo.size | fileSizeH"></td>
          <c:if test="${showImageActionMenu}">
          <td class="text-right">
              <mediadesk:login role="<%= User.ROLE_EDITOR %>"><a tabindex="0" ng-click="openMediaEdit(mo.ivid,'<c:out value="${url}"/>')" ng-href="" title="Edit"><i class="fa fa-pencil fa-fw md-icon-edit"></i></a>&nbsp;</mediadesk:login>
              <mediadesk:login><c:if test="${useLightbox}"><a tabindex="0" ng-click="toFav(mo)" href="" title="Favoriten" ng-show="apiUriPrefix!='/api/rest/fav'"><i class="fa fa-star fa-fw md-icon-fav"></i></a>&nbsp;</c:if></mediadesk:login>
              <mediadesk:login><c:if test="${useShoppingCart}"><a tabindex="0" ng-click="toCart(mo)" href="" title="Warenkorb"><i class="fa fa-shopping-cart fa-fw md-icon-cart"></i></a>&nbsp;</c:if></mediadesk:login>
              <c:if test="${showDownload}"><a ng-href="{{mo.downloadlink}}" title="Download"><i class="fa fa-download fa-fw md-icon-download"></i></a>&nbsp;</c:if></td>
          </c:if>
        </tr>
        <tr ng-repeat="sub in subfolders" ng-class="{'': mos.length==0, 'visible-xs': mos.length>0}">
          <td scope="row" class="md-table-col-icon"><i class="fa fa fa-folder"></i></td>
		  <td ng-show="allmos.length>0">
        <label>
          &nbsp;
        </label>
          </td>
          <td class="text-left"><a tabindex="0" ng-href="?id={{sub.id}}" ng-bind="sub.title"></a></td>
          <td ng-show="allmos.length>0">&nbsp;</td>
          <td ng-show="allmos.length>0">&nbsp;</td>
          <td ng-show="allmos.length>0">&nbsp;</td>
          <c:if test="${showImageActionMenu}">
          <td>&nbsp;</td>
          </c:if>
        </tr>
      </tbody>
    </table>
<!-- /TABELLE -->