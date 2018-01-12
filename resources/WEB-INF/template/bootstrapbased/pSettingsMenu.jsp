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
    <li class="active"><i class="fa fa-bars"></i> <spring:message code="set.menu.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.menu.headline"/></h3>
<h4>Navigation und Footer-Einträge bearbeiten</h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <h3>Navigationsmenü</h3>

    <table class="table table-bordered">

      <thead>
        <tr>
          <th>#</th>
          <th>Bezeichnung</th>
          <th>Link</th>
          <th>&nbsp;</th>
        </tr>
      <tbody>
     <% int i = 1; %>
     <c:forEach items="${navLinks}" var="menu">
     <% i++; %>
        <tr>
            <td><c:out value="${menu.id}"/></td>
            <td><c:out value="${menu.title}"/></td>
            <td>&nbsp;</td>
            <td class="text-right">
                <a href="<c:url value="setmenu/edit?id=${menu.id}"/>"><i class="fa fa-pencil" aria-hidden="true"></i></a>&nbsp;
                <a href="?func=delete&id=<c:out value="${menu.id}"/>"><i class="fa fa-trash" aria-hidden="true"></i></a>
            </td>
        </tr>
      </c:forEach>
          <form method="post" action="<c:url value="setmenu/edit"/>">
              <input type="hidden" name="type" value="3"/>
              <input type="hidden" name="metaData" value="0"/>
        <tr>
            <td><i class="fa fa-plus-circle" aria-hidden="true"></i></td>
            <td><input type="text" class="form-control" name="titleLng1" placeholder="Menüpunkt"></td>
            <td><input type="text" class="form-control" name="metaDataUrl" placeholder="http://"></td>
            <td><button type="submit" class="btn btn-default">OK</button></td>
        </tr>
          </form>
      </tbody>

    </table>

    <h3>Footer 2</h3>

    <table class="table table-bordered">

      <thead>
        <tr>
          <th>#</th>
          <th>Bezeichnung</th>
          <th>Link</th>
          <th>&nbsp;</th>
        </tr>
      <tbody>
     <% i = 1; %>
     <c:forEach items="${footer2Links}" var="menu">
     <% i++; %>
        <tr>
            <td><c:out value="${menu.id}"/></td>
            <td><c:out value="${menu.title}"/></td>
            <td>&nbsp;</td>
            <td class="text-right">
                <a href="<c:url value="setmenu/edit?id=${menu.id}"/>"><i class="fa fa-pencil" aria-hidden="true"></i></a>&nbsp;
                <a href="?func=delete&id=<c:out value="${menu.id}"/>"><i class="fa fa-trash" aria-hidden="true"></i></a>
            </td>
        </tr>
      </c:forEach>
          <form method="post" action="<c:url value="setmenu/edit"/>">
              <input type="hidden" name="type" value="4"/>
              <input type="hidden" name="metaData" value="0"/>
        <tr>
            <td><i class="fa fa-plus-circle" aria-hidden="true"></i></td>
            <td><input type="text" class="form-control" name="titleLng1" placeholder="Menüpunkt"></td>
            <td><input type="text" class="form-control" name="metaDataUrl" placeholder="http://"></td>
            <td><button type="submit" class="btn btn-default">OK</button></td>
        </tr>
          </form>
      </tbody>

    </table>

    <h3>Footer 3</h3>

    <table class="table table-bordered">

      <thead>
        <tr>
          <th>#</th>
          <th>Bezeichnung</th>
          <th>Link</th>
          <th>&nbsp;</th>
        </tr>
      <tbody>
     <% i = 1; %>
     <c:forEach items="${footer3Links}" var="menu">
     <% i++; %>
        <tr>
            <td><c:out value="${menu.id}"/></td>
            <td><c:out value="${menu.title}"/></td>
            <td>&nbsp;</td>
            <td class="text-right">
                <a href="<c:url value="setmenu/edit?id=${menu.id}"/>"><i class="fa fa-pencil" aria-hidden="true"></i></a>&nbsp;
                <a href="?func=delete&id=<c:out value="${menu.id}"/>"><i class="fa fa-trash" aria-hidden="true"></i></a>
            </td>
        </tr>
      </c:forEach>
          <form method="post" action="<c:url value="setmenu/edit"/>">
              <input type="hidden" name="type" value="5"/>
              <input type="hidden" name="metaData" value="0"/>
        <tr>
            <td><i class="fa fa-plus-circle" aria-hidden="true"></i></td>
            <td><input type="text" class="form-control" name="titleLng1" placeholder="Menüpunkt"></td>
            <td><input type="text" class="form-control" name="metaDataUrl" placeholder="http://"></td>
            <td><button type="submit" class="btn btn-default">OK</button></td>
        </tr>
          </form>
      </tbody>

    </table>

    <h3>Footer 4</h3>

    <table class="table table-bordered">

      <thead>
        <tr>
          <th>#</th>
          <th>Bezeichnung</th>
          <th>Link</th>
          <th>&nbsp;</th>
        </tr>
      <tbody>
     <% i = 1; %>
     <c:forEach items="${footer4Links}" var="menu">
     <% i++; %>
        <tr>
            <td><c:out value="${menu.id}"/></td>
            <td><c:out value="${menu.title}"/></td>
            <td>&nbsp;</td>
            <td class="text-right">
                <a href="<c:url value="setmenu/edit?id=${menu.id}"/>"><i class="fa fa-pencil" aria-hidden="true"></i></a>&nbsp;
                <a href="?func=delete&id=<c:out value="${menu.id}"/>"><i class="fa fa-trash" aria-hidden="true"></i></a>
            </td>
        </tr>
      </c:forEach>
          <form method="post" action="<c:url value="setmenu/edit"/>">
              <input type="hidden" name="type" value="6"/>
              <input type="hidden" name="metaData" value="0"/>
        <tr>
            <td><i class="fa fa-plus-circle" aria-hidden="true"></i></td>
            <td><input type="text" class="form-control" name="titleLng1" placeholder="Menüpunkt"></td>
            <td><input type="text" class="form-control" name="metaDataUrl" placeholder="http://"></td>
            <td><button type="submit" class="btn btn-default">OK</button></td>
        </tr>
          </form>
      </tbody>

    </table>

    <h3>Footer 5</h3>

    <table class="table table-bordered">

      <thead>
        <tr>
          <th>#</th>
          <th>Bezeichnung</th>
          <th>Link</th>
          <th>&nbsp;</th>
        </tr>
      <tbody>
     <% i = 1; %>
     <c:forEach items="${footer5Links}" var="menu">
     <% i++; %>
        <tr>
            <td><c:out value="${menu.id}"/></td>
            <td><c:out value="${menu.title}"/></td>
            <td>&nbsp;</td>
            <td class="text-right">
                <a href="<c:url value="setmenu/edit?id=${menu.id}"/>"><i class="fa fa-pencil" aria-hidden="true"></i></a>&nbsp;
                <a href="?func=delete&id=<c:out value="${menu.id}"/>"><i class="fa fa-trash" aria-hidden="true"></i></a>
            </td>
        </tr>
      </c:forEach>
          <form method="post" action="<c:url value="setmenu/edit"/>">
              <input type="hidden" name="type" value="7"/>
              <input type="hidden" name="metaData" value="0"/>
        <tr>
            <td><i class="fa fa-plus-circle" aria-hidden="true"></i></td>
            <td><input type="text" class="form-control" name="titleLng1" placeholder="Menüpunkt"></td>
            <td><input type="text" class="form-control" name="metaDataUrl" placeholder="http://"></td>
            <td><button type="submit" class="btn btn-default">OK</button></td>
        </tr>
          </form>
      </tbody>

    </table>

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