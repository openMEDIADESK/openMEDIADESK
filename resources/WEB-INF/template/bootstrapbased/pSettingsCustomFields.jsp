<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <li class="active"><i class="fa fa-list-alt"></i> <spring:message code="set.customfields"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.customfields"/></h3>
<h4><spring:message code="set.customfields.info"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" action="<c:url value="setcustomfields"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <div>

          <!-- Nav tabs -->
          <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#text" aria-controls="home" role="tab" data-toggle="tab">Text-Felder</a></li>
            <li role="presentation"><a href="#aufz" aria-controls="iptc" role="tab" data-toggle="tab">Aufzählungen</a></li>
              <!--
            <li role="presentation"><a href="#ftp" aria-controls="messages" role="tab" data-toggle="tab">FTP</a></li>
            <li role="presentation"><a href="#email" aria-controls="settings" role="tab" data-toggle="tab">E-Mail</a></li>
            -->
          </ul>

          <!-- Tab panes -->
          <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="text">
                <!-- Text-Felder Tab content -->

                <spring:bind path="command.customStr1">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 1: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr2">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 2: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr3">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 3: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr4">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 4: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr5">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 5: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr6">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 6: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr7">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 7: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr8">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 8: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr9">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 9: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customStr10">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Textfeld 10: Bezeichnung</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="aufz">
                <!-- Aufzählung content -->

                <h3>Aufzählung 1</h3>

                <spring:bind path="command.customList1Lng1">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Bezeichnung [DE]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customList1Lng2">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Bezeichnung [EN]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <h4>Werte</h4>

                <c:forEach items="${command.customList1}" var="listEntry" varStatus="stat">

                <div class="form-group">
                    <spring:bind path="command.customList1[${stat.index}].titleLng1">
                    <div class="col-sm-6">
                    <label for="text<c:out value="${status.expression}"/>">Wert [DE]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.customList1[${stat.index}].titleLng2">
                    <div class="col-sm-6">
                    <label for="text<c:out value="${status.expression}"/>">Wert [EN]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                    </spring:bind>
                </div>

                </c:forEach>

                <div class="form-group">
                    <p class="help-block">Tragen Sie hier die Werte für die Aufzählung ein. Die Anzahl der Elemente erhöht sicht automatisch nach dem speichern.</p>
                </div>

                <h3>Aufzählung 2</h3>

                <spring:bind path="command.customList2Lng1">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Bezeichnung [DE]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customList2Lng2">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Bezeichnung [EN]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <h4>Werte</h4>

                <c:forEach items="${command.customList2}" var="listEntry" varStatus="stat">

                <div class="form-group">
                    <spring:bind path="command.customList2[${stat.index}].titleLng1">
                    <div class="col-sm-6">
                    <label for="text<c:out value="${status.expression}"/>">Wert [DE]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.customList2[${stat.index}].titleLng2">
                    <div class="col-sm-6">
                    <label for="text<c:out value="${status.expression}"/>">Wert [EN]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                    </spring:bind>
                </div>

                </c:forEach>

                <div class="form-group">
                    <p class="help-block">Tragen Sie hier die Werte für die Aufzählung ein. Die Anzahl der Elemente erhöht sicht automatisch nach dem speichern.</p>
                </div>

                <h3>Aufzählung 3</h3>

                <spring:bind path="command.customList3Lng1">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Bezeichnung [DE]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.customList3Lng2">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Bezeichnung [EN]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <h4>Werte</h4>

                <c:forEach items="${command.customList3}" var="listEntry" varStatus="stat">

                <div class="form-group">
                    <spring:bind path="command.customList3[${stat.index}].titleLng1">
                    <div class="col-sm-6">
                    <label for="text<c:out value="${status.expression}"/>">Wert [DE]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.customList3[${stat.index}].titleLng2">
                    <div class="col-sm-6">
                    <label for="text<c:out value="${status.expression}"/>">Wert [EN]</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                    </div>
                    </spring:bind>
                </div>

                </c:forEach>

                <div class="form-group">
                    <p class="help-block">Tragen Sie hier die Werte für die Aufzählung ein. Die Anzahl der Elemente erhöht sicht automatisch nach dem speichern.</p>
                </div>

            </div>

          </div>

        </div>

    <button type="submit" class="btn btn-default"><spring:message code="mediaedit.submit"/></button>

    </form>

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