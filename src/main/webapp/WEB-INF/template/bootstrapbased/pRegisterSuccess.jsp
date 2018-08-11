<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

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
            <h3><spring:message code="register.succeed"/></h3>
            <p><spring:message code="register.sucessmsg"/></p>
        </div>
        <!-- /panel -->
    </div>
    <!-- /spalte 2 form -->
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
</div>
<!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->

<jsp:include page="footer.jsp"/>