<%@ page import="com.stumpner.mediadesk.core.Config"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@page contentType="text/html;charset=utf-8"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<div class="row"><!-- login/register row -->
<!-- LOGIN IM 10breiten INHALT -->
<!-- spalte 1 leer -->
<div class="col-xs-2">
<!-- leer -->
</div>
<!-- /spalte 1 leer -->
<!-- spalte 2 form -->
<div class="col-xs-6">
<!-- hier inhalt 6er spalte mittig -->
<div class="panel panel-default"> <!-- panel -->
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-envelope fa-fw"></i> Kontaktieren Sie uns</h3>
                    </div>
	<div class="panel-body">

        <!-- panel inhalt -->

        <p><c:out value="${text}" escapeXml="no"/></p>

        <div class="alert alert-success" role="alert">
            <spring:message code="contact.success"/>
        </div>

    <!-- /panel inhalt -->
	</div>
	</div>
</div> <!-- /panel -->
</div><!-- /spalte 2 form -->
<!-- spalte 3 leer -->
<div class="col-xs-2">
<!-- leer -->
</div>
<!-- /spalte 3 leer -->
<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->
</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->

<jsp:include page="footer.jsp"/>