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
    <li><a href="<c:url value="/${lng}/setmenu"/>"><i class="fa fa-bars"></i> <spring:message code="set.menu.headline"/></a></li>
    <li class="active"><i class="fa fa-pencil"></i> <spring:bind path="command.titleLng1"><c:out value="${status.value}"/></spring:bind></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:bind path="command.titleLng1"><c:out value="${status.value}"/></spring:bind></h3>
<h4>&nbsp;</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" action="<c:url value="edit"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.id">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.menu.id"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if> readonly="readonly">
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.titleLng1">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.menu.title"/> [DE]</label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.titleLng2">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.menu.title"/> [EN]</label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.metaData">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="set.menu.link"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                    <option value="0">URL angeben:</option>
                    <option value="1;/index/last"<c:if test="${status.value=='1;/index/last'}"> selected="true"</c:if>><spring:message code="set.menu.link.latest"/></option>
                    <option value="1;/index/"<c:if test="${status.value=='1;/index/'}"> selected="true"</c:if>><spring:message code="set.menu.link.folder"/></option>
                    <option value="1;/index/cat"<c:if test="${status.value=='1;/index/cat'}"> selected="true"</c:if>><spring:message code="set.menu.link.cat"/></option>
                    <option value="1;/login"<c:if test="${status.value=='1;/login'}"> selected="true"</c:if>><spring:message code="set.menu.link.login"/></option>
                    <option value="1;/register"<c:if test="${status.value=='1;/register'}"> selected="true"</c:if>><spring:message code="set.menu.link.register"/></option>
                            <% if (Config.pinPicEnabled) { %>
                    <option value="1;/pin"<c:if test="${status.value=='1;/pin'}"> selected="true"</c:if>><spring:message code="menu.pinpics"/></option>
                            <% } %>
                    <option value="1;/index/advancedsearch"<c:if test="${status.value=='1;/index/advancedsearch'}"> selected="true"</c:if>><spring:message code="imagesearch.advanced"/></option>
                    <option value="1;/index/popup/helpsearch"<c:if test="${status.value=='1;/index/popup/helpsearch'}"> selected="true"</c:if>><spring:message code="imagesearch.helpsearch"/></option>
                    <option value="1;/index/popup/contact"<c:if test="${status.value=='1;/index/popup/contact'}"> selected="true"</c:if>><spring:message code="menu.contact"/></option>
                    <option value="1;/index/popup/tac"<c:if test="${status.value=='1;/index/popup/tac'}"> selected="true"</c:if>><spring:message code="footer.termsconditions"/></option>
                    <option value="1;/index/popup/privacy"<c:if test="${status.value=='1;/index/popup/privacy'}"> selected="true"</c:if>><spring:message code="footer.privacy"/></option>
                    <option value="1;/index/popup/imprint"<c:if test="${status.value=='1;/index/popup/imprint'}"> selected="true"</c:if>><spring:message code="footer.imprint"/></option>
                    <option value="1;/index/popup/faq"<c:if test="${status.value=='1;/index/popup/faq'}"> selected="true"</c:if>><spring:message code="footer.faq"/></option>
                    <option value="1;/index/popup/help"<c:if test="${status.value=='1;/index/popup/help'}"> selected="true"</c:if>><spring:message code="footer.help"/></option>
            </select>
        </div>
    </spring:bind>

    <spring:bind path="command.linkUrl">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.menu.link"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="metaDataUrl" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.openAs">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="set.menu.edit.openas"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                    <option value="0"<c:if test="${status.value==0}"> selected="true"</c:if>><spring:message code="set.menu.edit.openas.normal"/></option>
                    <option value="1"<c:if test="${status.value==1}"> selected="true"</c:if>><spring:message code="set.menu.edit.openas.blank"/></option>
                    <option value="2"<c:if test="${status.value==2}"> selected="true"</c:if>><spring:message code="set.menu.edit.openas.popup"/></option>
            </select>
        </div>
    </spring:bind>

    <button type="submit" class="btn btn-default"><spring:message code="folderedit.submit"/></button>

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