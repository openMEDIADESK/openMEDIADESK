<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.usermanagement.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <li><a href="<c:url value="/${lng}/uploadweb"/>"><i class="fa fa-cloud-upload fa-fw"></i> <spring:message code="imageimport.choose.web"/></a></li>
    <li class="active"><i class="fa fa-exchange fa-fw"></i> <spring:message code="imageimport.choose.ftp"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="imageimport.choose.ftp"/><!--<spring:message code="ftp.headline"/>--></h3>
<c:if test="${ftpConfigured && !ftpConnectError}">
<h4><spring:message code="ftp.subheadline"/> <c:out value="${command.ftpHost}"/></h4>
</c:if>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- ######### 3er teilung upload optionen - direkt in die inhaltsspalte vor dem /div /col-sm-10 main SPALTE 2 FÜR INHALT ZU  ######### -->
<div class="row"> <!-- 3er row -->
	<!-- erklärung -->
    <div class="col-xs-12">




    </div>
    <!-- /erklärung -->

</div> <!-- /3er row -->

    <c:if test="${!ftpConfigured}">
        <div class="alert alert-warning" role="alert">
        <strong>FTP Import ist derzeit nicht eingerichtet.</strong>
        <mediadesk:login role="<%= User.ROLE_ADMIN %>">
        <a href="<c:url value="setimport#ftp"/>" class="alert-link">Hier k&ouml;nnen Sie die FTP-Zugangsdaten eingeben</a>.
        </mediadesk:login>
        </div>
    </c:if>

<c:if test="${null!=errorstr}">
    <div class="alert alert-danger" role="alert">
    
        <strong>Fehler</strong>

                 <c:if test="${null==errorstrArgs}"><spring:message code="${errorstr}"/></c:if>
                 <c:if test="${null!=errorstrArgs}"><spring:message code="${errorstr}" arguments="${model.errorstrArgs}"/></c:if>

    </div>
</c:if>

<c:if test="${ftpConnectError && ftpConfigured}">
    <div class="alert alert-danger" role="alert">

        <strong>Fehler beim Verbinden zum FTP-Server.</strong>


            <c:if test="${ftpLoginError}">Benutzer und Passwort wurden vom Server verweigert.</c:if>
            <mediadesk:login role="<%= User.ROLE_ADMIN %>">
            <a href="<c:url value="setimport#ftp"/>" class="alert-link">Hier k&ouml;nnen Sie die FTP-Zugangsdaten &uuml;berpr&uuml;fen</a>.
            </mediadesk:login>


    </div>
</c:if>

<c:if test="${ftpConfigured && !ftpConnectError}">

    <form method="post" action="<c:url value="/${lng}/uploadftp"/>">

                <div class="form-group">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="checkbox2" value="checkbox" id="cbxall" checked="true" onClick="javascript:selectAll();"> <spring:message code="ftp.all"/>
                      </label>
                    </div>
                </div>

<!-- TABELLE -->
    <table class="table table-striped">
      <thead>
        <tr>
          <th class="md-table-col-icon">&nbsp;</th>
          <th>Filename&nbsp;</th>
          <th>Size&nbsp;</th>
          <th class="text-right">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
     <% int i = 1; %>
     <c:forEach items="${fileList}" var="importFile">
     <% i++; %>
        <tr>
          <td><input type="checkbox" name="importFile" id="cbi<%= i-1 %>" value="<c:out value="${importFile.name}"/>" checked="true"></td>
		  <td><c:out value="${importFile.name}"/></td>
          <td class="text-left"><c:out value="${importFile.length}"/> Kb</td>
          <td></td>
        </tr>
     </c:forEach>
      </tbody>
    </table>
<!-- /TABELLE -->

    <button type="submit" name="delete" class="btn btn-default"<c:if test="${command.autoImportEnabled}"> disabled="true"</c:if>><spring:message code="ftp.delete"/></button>
    <button type="submit" name="submit" class="btn btn-default"<c:if test="${command.autoImportEnabled}"> disabled="true"</c:if>><spring:message code="ftp.import"/></button>

    </form>

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

    <mediadesk:login role="<%= User.ROLE_ADMIN %>">
        <a href="<c:url value="setimport#ftp"/>">Automatischer Import via FTP einschalten</a>
    </mediadesk:login>

<script type="text/javascript">

function selectAll() {

    var cbxall = document.getElementById("cbxall");

    for (var a=1;a<=${fn:length(fileList)};a++) {
        var cbx = document.getElementById("cbi"+a);
        cbx.checked = cbxall.checked;
    }

}

</script>

</c:if>

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- ######### /3er teilung upload optioen ######### -->


    </div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>