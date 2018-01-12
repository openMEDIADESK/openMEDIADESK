<%@ page import="java.util.Enumeration"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@ taglib uri="/WEB-INF/cewolf.tld" prefix="cewolf" %><%@page contentType="text/html;charset=utf-8" language="java" %><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">


<div class="modal-content" ng-controller="splashPageEditController">
      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>
          <h3>Splash Page Content bearbeiten</h3>
      <div class="modal-body2">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row"><!-- row -->
              <div class="col-md-12">

                  <!-- FORM -->
             <!-- FORM -->
                <form name="contentForm" ng-submit="submitForm()">
                <c:forEach var="field" items="${fieldList}">
                <div class="form-group">
                    <label><c:out value="${field}"/></label>
                    <input type="text" name="<c:out value="${field}"/>" class="form-control" ng-model="content.<c:out value="${field}"/>">
                    <span ng-show="error<c:out value="${field}"/>">{{error<c:out value="${field}"/>}}</span>
                </div>
                </c:forEach>
                    <!--
                <div class="form-group">
                    <label>Welcome Text</label>
                    <input type="text" name="name" class="form-control" ng-model="content.welcometext">
                    <span ng-show="errorWelcometext">{{errorWelcometext}}</span>
                </div>
                <div class="form-group">
                    <label>1. Section Headline</label>
                    <input type="text" name="username" class="form-control" ng-model="content.section1headline">
                    <span ng-show="errorSection1headline">{{errorSection1headline}}</span>
                </div>
                <div class="form-group">
                    <label>1. Section Text</label>
                    <input type="text" name="email" class="form-control" ng-model="content.section1text">
                    <span ng-show="errorSection1text">{{errorSection1text}}</span>
                </div>
                -->
                <button type="submit" class="btn btn-primary">Speichern</button>
                </form>
                  <!-- ENDE FORM -->

                  {{message}}

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