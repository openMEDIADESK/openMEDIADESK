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
    <li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>
    <li class="active"><i class="fa fa-paragraph fa-fw"></i> <spring:message code="set.text.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.text.headline"/></h3>
<!--<h4></h4>-->
<!-- /ordnertitel und infos -->

<div class="list-group">
    <a href="<c:url value="settext/agb-de"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.text.agb"/> Deutsch</h4>
        <p class="list-group-item-text"><spring:message code="set.text.agb.info"/></p>
    </a>

    <a href="<c:url value="settext/agb-en"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.text.agb"/> Englisch</h4>
        <p class="list-group-item-text"><spring:message code="set.text.agb.info"/></p>
    </a>

    <a href="<c:url value="settext/contact-de"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.text.contact"/> Deutsch</h4>
        <p class="list-group-item-text"><spring:message code="set.text.contact.info"/></p>
    </a>

    <a href="<c:url value="settext/contact-en"/>" class="list-group-item">
        <h4 class="list-group-item-heading"><spring:message code="set.text.contact"/> Englisch</h4>
        <p class="list-group-item-text"><spring:message code="set.text.contact.info"/></p>
    </a>
</div>

<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>