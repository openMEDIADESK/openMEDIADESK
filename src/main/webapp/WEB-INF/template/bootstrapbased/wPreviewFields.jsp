<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.MediaObject" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
                <h4 ng-bind="mo.title"></h4>
                <h5 ng-bind="mo.subtitle"></h5>
                <h6 ng-bind="mo.name"></h6>
                <p ng-bind="mo.note" style="white-space: pre;"></p>
                <h5 ng-bind="mo.people"></h5>
                <p class="md-text-prim" style="white-space: pre;" ng-bind="mo.info"></p>
                <p class="text-danger" style="white-space: pre;" ng-bind="mo.restrictions"></p>
                <table class="table table-condensed">
                  <tbody>
                    <tr>
                      <th ><spring:message code="mediaedit.site"/></th>
                      <td ng-bind="mo.site"></td>
                    </tr>
                    <tr>
                      <th><spring:message code="mediaedit.photographdate"/></th>
                      <td ng-bind="mo.photographDate | date:'dd. MMM yyyy'"></td>
                    </tr>
                    <tr ng-show="mo.mayorMime=='image' || mo.mayorMime=='video'">
                      <th><spring:message code="mediaedit.photographer"/></th>
                      <td ng-bind="mo.photographerAlias"></td>
                    </tr>
                    <tr>
                      <th><spring:message code="mediaedit.byline"/></th>
                      <td ng-bind="mo.byline"></td>
                    </tr>
                    <tr>
                      <th>Größe</th>
                      <td ng-bind="mo.kb + ' kb'"></td>
                    </tr>
                    <tr>
                      <th>Pixel</th>
                      <td ng-bind="mo.width +' x '+ mo.height"></td>
                    </tr>
                    <tr ng-show="mo.mayorMime=='image'">
                      <th>DPI</th>
                      <td ng-bind="mo.dpi +' dpi'"></td>
                    </tr>
                  </tbody>
                </table>