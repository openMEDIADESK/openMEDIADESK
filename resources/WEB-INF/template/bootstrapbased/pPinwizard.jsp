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
    <li class="active"><i class="fa fa-ticket fa-fw"></i> <spring:message code="pinwizard.headline"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="pinwizard.headline"/></h3>
<h4><spring:message code="pinwizard.subheadline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" id="message" action="pinwizard">
        <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <c:if test="${command.pinType==1}">

            <div class="radio">
              <label>
                <input type="radio" name="pinType" value="1" checked="true">
                <spring:message code="pinwizard.new"/>
              </label>
            </div>

            <div class="radio">
              <label>
                <input type="radio" name="pinType" value="2">
                <spring:message code="pinwizard.existing"/>
              </label>
            </div>

        </c:if>

        <c:if test="${command.pinType==2}">

            <p><spring:message code="pinwizard.pin"/></p>

            <div class="form-group">
                <label for="selectedPinId"><spring:message code="pinedit.enabled"/></label>

                <select class="form-control" id="selectedPinId" name="selectedPinId">
                    <c:forEach var="pin" items="${command.pinList}">
                        <option value="<c:out value="${pin.pinId}"/>"><c:out value="${pin.pin}"/> <c:out value="${pin.pinpicTitle}"/></option>
                    </c:forEach>
                </select>
            </div>
            
        </c:if>

        <c:if test="${empty command.imageList}">
            <div class="alert alert-danger" role="alert">
                <spring:message code="pinwizard.noimages"/>
            </div>
        </c:if>

        <c:if test="${not empty command.imageList}">
            <button class="btn btn-default" type="submit"><spring:message code="message.ok"/></button>
        </c:if>
        <c:if test="${empty command.imageList}">
            <button class="btn btn-default" type="button" onClick="javascript:window.history.back();"><spring:message code="download.back"/></button>
        </c:if>

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
