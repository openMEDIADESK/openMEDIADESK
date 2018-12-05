<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.MediaObject" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
    <!-- hochladen -->
    <c:if test="${uploadEnabled}">
            <!-- flow.js + ng-flow libraries https://github.com/flowjs/ng-flow -->
    <!-- ######################################################################################################################################## -->
    <!-- ITEMS ################################################################################################################################## -->
    <!-- MEDIADESK UPLOAD ITEM PIN ONLY -->
    <div class="md-thumb-item"
         flow-init="{target: getUploadTarget, chunkSize:201073741824, simultaneousUploads:1}"
         flow-files-submitted="$flow.upload()"
         flow-file-success="uploadFileSuccess($file, $message, $flow)"
         flow-file-error="uploadFileError($file, $message, $flow)"
         flow-complete="uploadComplete($flow)"
         flow-btn
         flow-drop><!-- item -->
    <!-- BG -->
    <div class="md-thumb-item-upload-bg">
    <!-- WOLKE -->
    <div class="md-thumb-item-upload text-center">
        <i class="fa fa-cloud-upload md-text-prim"></i>
    </div>
    <!-- /WOLKE -->
    <!-- TEXT -->
    <div class="md-thumb-item-info text-center">

        <div class="md-item-text" ng-show="$flow.files.length==0"><span class="lead"><strong>UPLOAD</strong></span><br /><small><spring:message code="dropfileshere"/></small></div>
        <div class="md-item-text" ng-repeat="file in $flow.files" ng-show="hasUploadError">
            <span class="text-danger" ng-show="file.error"><small ng-bind="file.name file.msg">{{file.name}} {{file.msg}}<i class="fa fa-exclamation-triangle"></i></small></span>
        </div>
        <div class="md-item-text" ng-show="$flow.isUploading()"><span class="lead">{{$flow.progress()*100 | number:0}}% <i class="fa fa-spinner fa-spin"></i></span></div>

    </div>
    <!-- /TEXT -->
    </div>
    <!-- /BG -->
    </div><!-- /item -->
    <!-- ^^^ /MEDIADESK UPLOAD ITEM ^^^ -->
    <!-- ######################################################################################################################################## -->
    </c:if>

<!-- element -->
    <div ng-repeat="mo in mos" class="md-thumb-item" ui-draggable="true" drag="mo" drop-effect="copyMove" on-drop-success="dropSuccessHandler($event,$index,mo)">
    <!-- leiste mit optionen -->
    <div class="md-thumb-item-op">
        <c:if test="${showImageActionMenu}">
        <div class="btn-toolbar" role="toolbar" aria-label="Optionen">
            <mediadesk:login role="<%= User.ROLE_EDITOR %>">
           	<div class="btn-group btn-group-xs" role="group" aria-label="Editieren">
            <a tabindex="0" href="" title="Edit" ng-click="openMediaEdit(mo.ivid,'<c:out value="${url}"/>')"><i class="fa fa-pencil-square-o fa-fw md-icon-edit"></i></a>
    		</div>
            </mediadesk:login>
            <mediadesk:login><c:if test="${useLightbox}">
            <div class="btn-group btn-group-xs" role="group" aria-label="Favoriten" ng-show="apiUriPrefix!='/api/rest/fav'">
            <a tabindex="0" href="" title="zu Favoriten" ng-click="toFav(mo)"><i class="fa fa-star fa-fw md-icon-fav"></i></a>
            </div>
            </c:if></mediadesk:login>
            <mediadesk:login><c:if test="${useShoppingCart}">
            <div class="btn-group btn-group-xs" role="group" aria-label="zum Warenkorb">
            <a tabindex="0" href="" title="Warenkorb" ng-click="toCart(mo)"><i class="fa fa-shopping-cart fa-fw md-icon-cart"></i></a>
            </div>
            </c:if></mediadesk:login>
            <c:if test="${showDownload}">
            <div class="btn-group btn-group-xs" role="group" aria-label="Sofortdownload">
            <a tabindex="0" href="" ng-href="{{mo.downloadlink}}" title="Sofortdownload"><i class="fa fa-download fa-fw md-icon-download"></i></a>
                <!--
            <button type="button" class="btn btn-default" title="Sofortdownload" ng-click="underConstruction()"><i class="fa fa-download fa-fw text-info"></i></button>
                -->
            </div>
            </c:if>
        </div>
        </c:if>
    </div>
    <!-- /leiste mit optionen -->
    <!-- BG -->
    <div class="md-thumb-item-bg">
    <!-- BILD -->
	<!-- box rund ums bild als test -->
    <div class="md-thumb-item-img-box" ng-show="mo.mayorMime=='image' || mo.mayorMime=='video'">

        <div class="md-thumb-item-img">
            <c:if test="${fn:contains(config.param, '-SLIDE')}">
            <a ng-href="/imageservlet/{{mo.ivid}}/0/{{mo.name}}" data-fancybox="gallery" data-caption="Caption for single image">
                <img ng-src="/imageservlet/{{mo.ivid}}/1/{{mo.name}}"/>

            </a>
            </c:if>
            <c:if test="${!fn:contains(config.param, '-SLIDE')}">
            <a ng-href="#/{{mo.ivid}}" ng-click="openPreview($index)">
                <img ng-src="/imageservlet/{{mo.ivid}}/1/{{mo.name}}"/>
            </a>
            </c:if>
        </div>

    </div>
    <!-- /box rund ums bild als test -->
    <!-- FILE ICON STATT BILD -->
    <div class="md-thumb-item-icon text-center md-iconcolor-mimetype" ng-hide="mo.mayorMime=='image' || mo.mayorMime=='video'">
        <a ng-href="#/{{mo.ivid}}" ng-click="openPreview($index)"><i class="fa fa-file-{{mo.mime | mimeIconClass}}-o"></i></a>
    </div>
    <!-- /FILE ICON STATT BILD --

    <!-- /BILD -->
    <!-- INFO -->
        <div class="md-thumb-item-info">
            <div class="pull-right text-muted"><div class="md-item-text-name"><small ng-bind="mo.caption"></small></div></div>
            <div class="checkbox">
                <label>
                <c:if test="${showSelect==true}">
                <input type="checkbox" id="blankoCheckbox" value="option1" aria-label="ausw�hlen"
                       ng-model="mo.selected" ng-Change="onSelectChange($index)"/>
                </c:if>
                </label>
            </div>
            <div class="md-item-text" ng-bind="mo.title"></div>
            <div class="md-item-text" ng-bind="mo.site"></div>
            <div class="md-item-text" ng-bind="mo.people"></div>
            <div class="md-item-text"><small class="text-muted" ng-bind="mo.byline"></small></div>
            <div class="md-item-text"><small class="text-muted" ng-bind="mo.photographDate | date:'dd. MMM yyyy'"></small></div>
        </div>
    <!-- /INFO -->
    </div>
    <!-- /BG -->

    </div><!-- /item -->

    <!-- SUB-FOLDER element -->
    <div ng-repeat="sub in subfolders" ng-class="{'': mos.length==0, 'visible-xs': mos.length>0}" class="md-thumb-item">
    <!-- leiste mit optionen -->
    <div class="md-thumb-item-op">
        <c:if test="${showImageActionMenu}">
        <div class="btn-toolbar" role="toolbar" aria-label="Optionen">

            <mediadesk:login role="<%= User.ROLE_EDITOR %>">
           	<div class="btn-group btn-group-xs" role="group" aria-label="Editieren">
                   &nbsp;
    		</div>
            </mediadesk:login>

        </div>
        </c:if>
    </div>
    <!-- /leiste mit optionen -->
    <!-- BG -->
    <div class="md-thumb-item-bg">
    <!-- BILD -->
	<!-- box rund ums bild als test -->
    <div class="md-thumb-item-img-box" ng-show="sub.pivid!=0">

        <div class="md-thumb-item-img">
            <a ng-href="?id={{sub.id}}"><img ng-src="/imageservlet/{{sub.pivid}}/1/{{sub.name}}"/></a>
        </div>

    </div>
    <!-- /box rund ums bild als test -->
    <!-- FILE ICON STATT BILD -->
    <!-- FOLDER ICON STATT BILD -->
    <div class="md-thumb-item-icon text-center md-iconcolor-folder" ng-show="sub.pivid==0">
        <a ng-href="?id={{sub.id}}"><i class="fa fa fa-folder"></i></a>
    </div>
    <!-- /FODLER ICON STATT BILD -->
    <!-- INFO -->
    <div class="md-thumb-item-info">
        <div class="pull-right text-muted"><small><!--A1-02082015-04--></small></div>
        <div class="checkbox">
            <label>
            <!--<input type="checkbox" id="blankoCheckbox" value="option1" aria-label="ausw�hlen"> -->
            </label>
        </div>
        <div class="md-item-text"><span class="lead"><a ng-href="?id={{sub.id}}" ng-bind="sub.title"></a></span></div>
    </div>
    <!-- /INFO -->
    </div>
    <!-- /BG -->

    </div><!-- SUBFOLDER /item -->