<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
    <c:forEach items="${model.navItems}" var="navItem">
    <li><a href="<c:out value="${navItem.url}"/>"><i class="fa fa-users fa-fw"></i> <c:out value="${navItem.name}"/></a></li>
    </c:forEach>
    <li class="active"><i class="fa fa-user fa-fw"></i> Abfrage</li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="${headline}"/></h3>
<!--<h4></h4>-->
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" id="message" action="<c:out value="${url}"/>">
        <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <div class="alert<c:if test="${empty attentionText}"> alert-info</c:if><c:if test="${not empty attentionText}"> alert-danger</c:if>" role="alert">

            <spring:message code="${info}" arguments="${infoArgument}"/>

            <c:if test="${not empty attentionText}">
            <strong><spring:message code="${attentionText}" arguments="${infoArgument}"/></strong>
            </c:if>

            <c:if test="${useCbx}">

            <div class="checkbox">
                <label>
                  <input type="checkbox" name="cbx" value="true"<c:if test="${cbxChecked}"> checked="true"</c:if>> <spring:message code="${cbxText}"/>
                </label>
            </div>

            </c:if>

        <spring:hasBindErrors name="command">
        <spring:bind path="command">

              <c:forEach items="${status.errorMessages}" var="error">
                <c:out value="${error}"/><br/>
              </c:forEach>

        </spring:bind>
        </spring:hasBindErrors>

        </div>

        <button class="btn btn-default" name="yes" type="submit"><spring:message code="message.yes"/></button>
        <button class="btn btn-default" name="no" type="submit"><spring:message code="message.no"/></button>

    </form>

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
