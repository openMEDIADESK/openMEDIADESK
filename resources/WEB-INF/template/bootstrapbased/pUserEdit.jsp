<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
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
    <li><a href="<c:url value="/${lng}/usermanager"/>"><i class="fa fa-users fa-fw"></i> <spring:message code="userlist.headline"/></a></li>
    <li class="active"><i class="fa fa-user fa-fw"></i>
        <c:if test="${command.userId==-1}"><spring:message code="usercreate.headline"/></c:if>
        <c:if test="${command.userId!=-1}"><spring:message code="useredit.headline"/></c:if>

    </li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<c:if test="${command.userId==-1}">
<h3><spring:message code="usercreate.headline"/></h3>
<h4><spring:message code="usercreate.subheadline"/></h4>    
</c:if>
<c:if test="${command.userId!=-1}">
<h3><spring:message code="useredit.headline"/></h3>
<h4><spring:message code="useredit.subheadline"/></h4>
</c:if>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->


    <form id="imageedit" method="post" action="<c:url value="/${lng}/useredit"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <% if (!Config.userEmailAsUsername) { %>
    <spring:bind path="command.userName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.username"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if> <c:if test="${command.userId!=-1}">readonly="true"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    <% } %>

    <spring:bind path="command.firstName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.firstname"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.lastName">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.lastname"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.email">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.email"/></label>
        <input type="email" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.phone">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.phone"/></label>
        <input type="tel" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.company">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.company"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.companyType">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.companytype"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.street">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.street"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.city">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.city"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.zipCode">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.zipcode"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.country">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.contry"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <%
        if (!Config.currency.isEmpty()) {
            System.out.println(Config.currency);
    %>
    <spring:bind path="command.vatPercent">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.vat"/></label>
        <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>
    <%
        }
    %>

      <%
          if (Config.creditSystemEnabled) {
      %>
        <spring:bind path="command.credits">
            <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
            <label for="input<c:out value="${status.expression}"/>"><spring:message code="register.credits"/></label>
            <input type="text" class="form-control input-sm" id="input<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
            <c:if test="${status.error}">
              <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
              <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
            </c:if>
            </div>
        </spring:bind>
      <%
          }
      %>

        <div class="form-group">
            <spring:bind path="command.enabled">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="useredit.loginstate"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="useredit.loginstate.disabled"/></option>
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="useredit.loginstate.enabled"/></option>
            </select>
            </spring:bind>
        </div>

        <div class="form-group">
            <spring:bind path="command.uploadNotification">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="set.mail.upload"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
            </select>
            </spring:bind>
        </div>

        <c:if test="${showRole}">
        <div class="form-group">
            <spring:bind path="command.role">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="useredit.role"/> <jsp:include page="wikiLink.jsp?url=https://wiki.openmediadesk.net/de/Rollen"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="useredit.role.user"/></option>
                      <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="useredit.role.importer"/></option>
                      <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="useredit.role.pinmakler"/></option>
                      <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="useredit.role.pineditor"/></option>
                      <option value="8"<c:if test="${status.value==8}"> selected</c:if>><spring:message code="useredit.role.editor"/></option>
                      <!-- <option value="9"<c:if test="${status.value==9}"> selected</c:if>><spring:message code="useredit.role.homeeditor"/></option> -->
                      <option value="10"<c:if test="${status.value==10}"> selected</c:if>><spring:message code="useredit.role.mastereditor"/></option>
                      <option value="99"<c:if test="${status.value==99}"> selected</c:if>><spring:message code="useredit.role.admin"/></option>
            </select>

            </spring:bind>
        </div>
        </c:if>

        <c:if test="${showSecurityGroup}">
        <div class="form-group">
            <spring:bind path="command.securityGroup">
            <label for="sel<c:out value="${status.expression}"/>"><spring:message code="userlist.securitygroup"/></label>

            <select class="form-control" id="sel<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                    <c:forEach items="${securityGroupList}" var="securityGroup">
                    <option value="<c:out value="${securityGroup.id}"/>"<c:if test="${status.value==securityGroup.id}"> selected</c:if>><c:out value="${securityGroup.name}"/></option>
                    </c:forEach>
            </select>
            </spring:bind>
        </div>
        </c:if>

        <c:if test="${canSetPassword}">
        <div class="checkbox">
            <label>
              <input type="checkbox" name="resetpwd" value="1"> <spring:message code="useredit.resetpass"/>
            </label>
        </div>
        </c:if>

        <c:if test="${showHomeCategoryCreator}">
            
            <div class="checkbox">
                <label>
                  <input type="checkbox" name="createHomeCat" value="1"<c:if test="${command.homeFolderId!=-1}"> disabled="true"</c:if><c:if test="${homeCategoryChecked}"> checked="true"</c:if>> Mandanten-Kategorie erstellen <a href="https://wiki.openmediadesk.net/en/Mandant" target="_blank">[?]</a>
                </label>
            </div>

            <c:if test="${command.homeFolderId!=-1}">
            <div class="checkbox">
                <label>
                  <input type="checkbox" name="deleteHomeCat" value="1"> Mandanten-Kategorie zurücksetzen <a href="https://wiki.openmediadesk.net/en/Mandant" target="_blank">[?]</a>
                </label>
            </div>
            </c:if>

        </c:if>


    <button type="submit" class="btn btn-default">Speichern</button>
    <c:if test="${showRole && command.role==9}">
        <button type="button" class="btn btn-default" onclick="javascript:document.location.href='<c:url value="usermanager?mandant=${command.userId}"/>';">Benutzer des Mandanten anzeigen</button>
    </c:if>
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