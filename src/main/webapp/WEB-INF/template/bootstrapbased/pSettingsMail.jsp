<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>
    <li class="active"><i class="fa fa-envelope"></i> <spring:message code="set.mail"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.mail"/></h3>
<h4><spring:message code="set.headline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" action="<c:url value="setmail"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <div>

          <!-- Nav tabs -->
          <ul class="nav nav-tabs" role="tablist">
            <li role="presentation"<c:if test="${activeTab!='mailserver'}"> class="active"</c:if>><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Allgemein</a></li>
            <li role="presentation"><a href="#passwordmail" aria-controls="text" role="tab" data-toggle="tab">Passwort-Mail</a></li>
            <li role="presentation"><a href="#uploadmail" aria-controls="messages" role="tab" data-toggle="tab">Upload-Mail</a></li>
            <li role="presentation"><a href="#downloadmail" aria-controls="settings" role="tab" data-toggle="tab">Download-Mail</a></li>
            <li role="presentation"<c:if test="${activeTab=='mailserver'}"> class="active"</c:if>><a href="#mailserver" aria-controls="server" role="tab" data-toggle="tab">Mailserver</a></li>
          </ul>

          <!-- Tab panes -->
          <div class="tab-content">
            <div role="tabpanel" class="tab-pane<c:if test="${activeTab!='mailserver'}"> active</c:if>" id="home">
                <!-- Allgemein Tab content -->
                <spring:bind path="command.senderMailaddresse">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.mail.senderMailaddresse"/></label>
                    <input type="text" class="form-control input-sm" id="xxtext<c:out value="${status.expression}"/>" name="xx<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if> readonly="true">
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.mailAdminEmail">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Emailempfänger für Benachrichtigungen</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:hasBindErrors name="command">
                <div class="alert alert-danger" role="alert">
                  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>&nbsp;

                    <spring:bind path="command">
                        <div class="formErrorSummary">
                        <c:forEach items="${status.errorMessages}" var="error">
                            <c:out value="${error}"/><br>
                        </c:forEach>
                        </div>
                    </spring:bind>

                </div>
                </spring:hasBindErrors>

            </div>

            <div role="tabpanel" class="tab-pane" id="passwordmail">
                <!-- Passwordmail -->
                <spring:bind path="command.mailNewPasswordMailSubject">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.mail.pw.subject"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.mailNewPasswordMailBody">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.mail.pw.body"/></label>
                    <textarea name="<c:out value="${status.expression}"/>" class="form-control" rows="5" id="comment"><c:out value="${status.value}"/></textarea>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">
                        <spring:message code="set.mail.pw.variables"/>
                    </p>
                        <pre>
<spring:message code="set.mail.pw.varinfo"/>
                        </pre>
                </div>

            </div>

            <div role="tabpanel" class="tab-pane" id="uploadmail">
                <!-- Uploadmail -->
                <spring:bind path="command.mailUploadInfoMailSubject">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.mail.upload.subject"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.mailUploadInfoMailBody">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.mail.upload.body"/></label>
                    <textarea name="<c:out value="${status.expression}"/>" class="form-control" rows="5" id="comment"><c:out value="${status.value}"/></textarea>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">
                        <spring:message code="set.mail.pw.variables"/>
                    </p>
                        <pre>
<spring:message code="set.mail.upload.varinfo"/>
                        </pre>
                </div>

                <spring:bind path="command.mailUploadInfoEnabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.mail.upload.enabled"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                          <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                          <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                        </select>
                    </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="downloadmail">
                <!-- Downloadmail -->
                <spring:bind path="command.mailDownloadInfoMailSubject">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.mail.dl.subject"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.mailDownloadInfoMailBody">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.mail.dl.body"/></label>
                    <textarea name="<c:out value="${status.expression}"/>" class="form-control" rows="5" id="comment"><c:out value="${status.value}"/></textarea>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">
                        <spring:message code="set.mail.pw.variables"/>
                    </p>
                        <pre>
<spring:message code="set.mail.dl.varinfo"/>
                        </pre>
                </div>

            </div>

            <div role="tabpanel" class="tab-pane<c:if test="${activeTab=='mailserver'}"> active</c:if>" id="mailserver">

                <!-- Mailserver Einstellungen -->


                <spring:bind path="command.senderMailaddresse">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.mail.senderMailaddresse"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.mailServer">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.mail.mailServer"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.useTls">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">TLS/SSL Verschüsselung verwenden</label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control"<c:if test="${!fn:contains(licFunc, '-mailserver')}"> readonly="true"</c:if>>
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                        </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.smtpUsername">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">SMTP Username</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="command.smtpPassword">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">SMTP Password</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                </spring:bind>

                <spring:hasBindErrors name="command">
                <div class="alert alert-danger" role="alert">
                  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>&nbsp;

                    <spring:bind path="command">
                        <div class="formErrorSummary">
                        <c:forEach items="${status.errorMessages}" var="error">
                            <c:out value="${error}"/><br>
                        </c:forEach>
                        </div>
                    </spring:bind>

                </div>
                </spring:hasBindErrors>

            </div>

          </div>

        </div>

    <button type="submit" class="btn btn-default"><spring:message code="mediaedit.submit"/></button>

    </form>

    <!--
    <spring:hasBindErrors name="command">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <spring:bind path="command">
                <div class="formErrorSummary">
                <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br>
                </c:forEach>
                </div>
            </spring:bind>
    </div>
    </spring:hasBindErrors>

    -->

    <!-- /FORMS FÜR EDIT -->
	</div>
</div>      <!-- /row -->
<!-- ENDE EDIT BILD -->
<!-- ###################################################################################################################################################### -->



<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>