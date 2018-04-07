<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.image.MediaObject" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<!-- Anfang Modal HTML -->
<script type="text/ng-template" id="mediapreviewScript.html">

<div class="modal-content md-modal-preview">
      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>
        <!-- <span class="modal-title" id="meinModalLabel"><img src="logo.png" alt="MEDIADESK" title="MEDIADESK" border="0"/></span> -->
        <!-- BUTTONS OPTIONEN -->
        <div class="btn-toolbar" role="toolbar" aria-label="Optionen">
            <div class="btn-group btn-group-sm" ng-show="properties.role>=<%= User.ROLE_EDITOR %>" role="group" aria-label="Editieren">
            <button type="button" class="btn btn-default" title="Editieren" ng-click="openMediaEdit(mo.ivid)"><i class="fa fa-pencil-square-o fa-fw md-icon-edit"></i></button>
            </div>
            <c:if test="${showLightbox}">
            <div class="btn-group btn-group-sm" ng-show="properties.loggedin && !mo.fav" role="group" aria-label="Favoriten">
            <button type="button" class="btn btn-default" title="zu Favoriten" ng-click="toFav(mo)"><i class="fa fa-star fa-fw md-icon-fav"></i></button>
            </div>
            </c:if>
            <c:if test="${useShoppingCart}">
            <div class="btn-group btn-group-sm" ng-show="properties.loggedin && !mo.cart" role="group" aria-label="Warenkorb">
            <button type="button" class="btn btn-default" title="zum Warenkorb" ng-click="toCart(mo)"><i class="fa fa-shopping-cart fa-fw md-icon-cart"></i></button>
            </div>
            </c:if>
            <!--
            <div class="btn-group btn-group-sm" role="group" aria-label="Pin">
            <button type="button" class="btn btn-default" title="Pin-Pics" ng-click="underConstruction()"><i class="fa fa-ticket fa-fw text-info"></i></button>
            </div>
            -->
            <div class="btn-group btn-group-sm" role="group" aria-label="Sofortdownload" ng-show="{{mo.downloadlink}}">
            <a ng-href="{{mo.downloadlink}}" class="btn btn-default" title="Sofortdownload"><i class="fa fa-download fa-fw md-icon-download"></i></a>
            </div>
            <!--
            <div class="btn-group btn-group-sm" role="group" aria-label="Drucken">
            <button type="button" class="btn btn-default" title="Drucken" ng-click="underConstruction()"><i class="fa fa-print fa-fw text-info"></i></button>
            </div>
            -->
           </div>
        <!-- /BUTTONS OPTIONEN -->
      </div>
      <div class="modal-body md-modal-body">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row" ng-swipe-left="nextPreview()" ng-swipe-right="prevPreview()"><!-- row -->
              <div class="col-md-6" ng-show="mo.mayorMime=='image' || mo.mayorMime=='video' || mo.mayorMime=='audio'" ng-mouseenter="showcarousel=true" ng-mouseleave="showcarousel=false">

            <!-- carousel -->
            <!--<div class="carousel slide">-->

            <div class="md-modal-loader" ng-hide="moloaded">
                <h4>Daten werden geladen...</h4>
                <!-- Daten werden geladen -->
                <jsp:include page="iconSpinner.jsp?size=5"/>
            </div>

              <img ng-show="mo.mayorMime=='image'" ng-src="/imageservlet/{{mo.ivid}}/2/{{mo.name}}" imageonload="onPreviewImageLoaded()">

              <!-- Controls -->

            <!--</div>-->



                          <div ng-show="mo.mayorMime=='video' || mo.mayorMime=='audio'" ng-controller="VideoCtrl as controller" class="videogular-container">
                            <videogular vg-theme="controller.config.theme">
                                <vg-media vg-src="controller.config.sources"
                                        vg-tracks="controller.config.tracks">
                                </vg-media>

                                <vg-controls>
                                    <vg-play-pause-button></vg-play-pause-button>
                                    <vg-time-display>{{ currentTime | date:'mm:ss' }}</vg-time-display>
                                    <vg-scrub-bar>
                                        <vg-scrub-bar-current-time></vg-scrub-bar-current-time>
                                    </vg-scrub-bar>
                                    <vg-time-display>{{ timeLeft | date:'mm:ss' }}</vg-time-display>
                                    <vg-volume>
                                        <vg-mute-button></vg-mute-button>
                                        <vg-volume-bar></vg-volume-bar>
                                    </vg-volume>
                                    <vg-fullscreen-button></vg-fullscreen-button>
                                </vg-controls>

                                <vg-overlay-play></vg-overlay-play>
                                <vg-poster vg-url='controller.config.plugins.poster'></vg-poster>
                            </videogular>
                          </div>
                                                  <!-- Carousel controls also for video player -->
                  <a class="carousel-control left noshadow hidden-xs" ng-click="prevPreview()" ng-show="previewIndex>0 && showcarousel" href="#"><span class="glyphicon glyphicon-chevron-left"></span></a>
                  <a class="carousel-control right noshadow hidden-xs" ng-click="nextPreview()" ng-show="previewIndex<mos.length-1 && showcarousel" href="#"><span class="glyphicon glyphicon-chevron-right"></span></a>
              </div>
              <div ng-class="{'col-md-6': mo.mayorMime=='image' || mo.mayorMime=='video' || mo.mayorMime=='audio', 'col-md-12': mo.mayorMime!='image' && mo.mayorMime!='video' && mo.mayorMime!='audio'}">

<jsp:include page="wPreviewFields.jsp"/>

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