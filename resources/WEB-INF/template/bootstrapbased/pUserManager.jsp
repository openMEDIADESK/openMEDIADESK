<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="#"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <!--<li><a href="#"><i class="fa fa-folder-open-o fa-fw"></i> Benutzer</a></li>-->
    <li class="active"><i class="fa fa-users fa-fw"></i> Benutzer</li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3>Benutzer</h3>
<h4>Verwalten</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->



<!-- hier startet mit ROW eine neue zeile mit den div als thumbcontainer -->
<!-- verschachtelung -->
<div class="row"> <!-- thumb row -->





</div><!-- /thumb row ende und zu -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->

<a href="<c:url value="/${lng}/useredit"/>" class="btn btn-default">Neuen Benutzer anlegen</a>


<!-- TABELLE -->

    <table class="table table-striped">
      <thead>
        <tr>
          <th class="md-table-col-icon">&nbsp;</th>
          <!--<th>&nbsp;</th>-->
          <th>Benutzer&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung runter"><i class="fa fa-sort-desc fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung rauf"><i class="fa fa-sort-asc fa-fw"></i></a>--></th>
          <th>Vorname&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th>Nachname&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th>Berechtigungsgruppe&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th>Rolle&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
          <th>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
        </tr>
      </thead>
      <tbody>

        <c:forEach items="${userAtomList}" var="userAtom" varStatus="stat">
          <!--

            ['<c:out value="${userAtom.userId}"/>','<c:out value="${userAtom.userName}"/>',<c:out value="${userAtom.enabled}"/>,'<c:out value="${userAtom.firstName}"/>','<c:out value="${userAtom.lastName}"/>','<c:out value="${userAtom.securityGroup}"/>','<c:out value="${userAtom.role}"/>','<c:out value="${userAtom.email}"/>','<c:out value="${userAtom.phone}"/>','<c:out value="${userAtom.company}"/>',<c:out value="${userAtom.credits}"/>]//,//,0.0,0.0,0.0,'9/1 12:00am'],

          -->


        <tr>
          <td scope="row"><i class="fa <c:if test="${userAtom.enabled}">fa-user</c:if><c:if test="${!userAtom.enabled}">fa-user-times text-danger</c:if>"></i></td>
		  <!--<td>
        <label>
          <input type="checkbox">
        </label>
          </td>-->
          <td><c:out value="${userAtom.userName}"/></td>
          <td><c:out value="${userAtom.firstName}"/></td>
          <td><c:out value="${userAtom.lastName}"/></td>
          <td><c:out value="${securityGroupResolver.get(userAtom.securityGroup).name}"/></td>
          <td>
              <c:choose>
                  <c:when test="${userAtom.role==0}">
                      <spring:message code="useredit.role.user"/>
                  </c:when>
                  <c:when test="${userAtom.role==4}">
                      <spring:message code="useredit.role.importer"/>
                  </c:when>
                  <c:when test="${userAtom.role==5}">
                      <spring:message code="useredit.role.pinmakler"/>
                  </c:when>
                  <c:when test="${userAtom.role==6}">
                      <spring:message code="useredit.role.pineditor"/>
                  </c:when>
                  <c:when test="${userAtom.role==8}">
                      <spring:message code="useredit.role.editor"/>
                  </c:when>
                  <c:when test="${userAtom.role==9}">
                      <spring:message code="useredit.role.homeeditor"/>
                  </c:when>
                  <c:when test="${userAtom.role==10}">
                      <spring:message code="useredit.role.mastereditor"/>
                  </c:when>
                  <c:when test="${userAtom.role==99}">
                      <spring:message code="useredit.role.admin"/>
                  </c:when>
              </c:choose>
          </td>
          <td class="text-right"><c:if test="${canSetPassword}"><a href="<c:url value="/${lng}/password"/>?userid=<c:out value="${userAtom.userId}"/>" title="Passwort"><i class="fa fa-key"></i></a>&nbsp;</c:if>
              <a href="<c:url value="/${lng}/useredit"/>?userid=<c:out value="${userAtom.userId}"/>" title="bearbeiten"><i class="fa fa-pencil fa-fw"></i></a>&nbsp;
              <a href="<c:url value="/${lng}/userdelete"/>?userid=<c:out value="${userAtom.userId}"/>" title="löschen"><i class="fa fa-trash"></i></a>&nbsp;</td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
<!-- /TABELLE -->

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- ordnertitel und infos -->
<h3>Berechtigungsgruppen</h3>
<h4>Verwalten</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<form action="?" method="post" class="form-inline">
        <div class="form-group">
        <label for="textName"><spring:message code="userlist.addgroup"/></label>
        <input type="text" class="form-control input-sm" id="textName" name="addgroup" value="">
        </div>

        <button type="submit" class="btn btn-default">anlegen</button>

</form>


<!-- TABELLE -->

    <table class="table table-striped table-condensed">
      <thead>
        <tr>
          <th>&nbsp;</th>
          <!--<th>&nbsp;</th>-->
          <th>Berechtigungsgruppe&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung runter"><i class="fa fa-sort-desc fa-fw"></i></a>&nbsp;<a href="#" title="Sortierung rauf"><i class="fa fa-sort-asc fa-fw"></i></a>--></th>
          <th>&nbsp;</th>
          <th>&nbsp;<!--<a href="#" title="Sortierung"><i class="fa fa-sort fa-fw"></i></a>--></th>
        </tr>
      </thead>
      <tbody>

        <c:forEach items="${securitygroupList}" var="securitygroup" varStatus="stat">
          <!--

            ['<c:out value="${userAtom.userId}"/>','<c:out value="${userAtom.userName}"/>',<c:out value="${userAtom.enabled}"/>,'<c:out value="${userAtom.firstName}"/>','<c:out value="${userAtom.lastName}"/>','<c:out value="${userAtom.securityGroup}"/>','<c:out value="${userAtom.role}"/>','<c:out value="${userAtom.email}"/>','<c:out value="${userAtom.phone}"/>','<c:out value="${userAtom.company}"/>',<c:out value="${userAtom.credits}"/>]//,//,0.0,0.0,0.0,'9/1 12:00am'],

          -->


        <tr>
          <td class="md-table-col-icon" scope="row"><i class="fa fa fa-users"></i></td>
		  <!--<td>
            <label>
              <input type="checkbox">
            </label>
          </td> -->
          <td class="text-left"><c:out value="${securitygroup.name}"/>&nbsp;</td>
          <td>&nbsp;</td>
          <td class="text-right">              
              <a href="<c:url value="/${lng}/treeacl?type=acl&redirectTo=usermanager&id=${securitygroup.id}"/>" title="bearbeiten"><i class="fa fa-folder"></i></a>&nbsp;
              <c:if test="${securitygroup.id>0}">
              <a href="<c:url value="?delgroup=${securitygroup.id}"/>" title="löschen"><i class="fa fa-trash"></i></a>&nbsp;</td>
              </c:if>
        </tr>
        </c:forEach>
      </tbody>
    </table>
<!-- /TABELLE -->

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

    </div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>
