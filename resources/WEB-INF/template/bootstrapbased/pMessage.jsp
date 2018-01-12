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
<h4><spring:message code="${subheadline}" arguments="${subheadlineArgs}"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" id="message" action="<c:out value="${nextUrl}"/>">
        <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <div class="alert alert-success" role="alert">
            
            <strong><spring:message code="${text}"/></strong>

            <c:out value="${htmlCode}" escapeXml="false"/>

        <spring:hasBindErrors name="command">
        <spring:bind path="command">

              <c:forEach items="${status.errorMessages}" var="error">
                <c:out value="${error}"/><br/>
              </c:forEach>

        </spring:bind>
        </spring:hasBindErrors>

        </div>

        <button class="btn btn-default" type="submit"><spring:message code="message.ok"/></button>

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
