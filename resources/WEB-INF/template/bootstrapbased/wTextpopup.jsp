<%@ page import="com.stumpner.mediadesk.core.Config"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@page contentType="text/html;charset=utf-8"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">


<div class="modal-content">
      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>

      <div class="modal-body2">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row"><!-- row -->
              <div class="col-md-12">
              <c:out value="${text}" escapeXml="no"/>
              </div>
          </div><!-- /row -->


          </div><!-- /container -->

      <!-- /INHALT MODAL -->
      </div>
      </div>
    </div>
<!-- /modal -->
<!-- /MODAL PARKING ####################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->