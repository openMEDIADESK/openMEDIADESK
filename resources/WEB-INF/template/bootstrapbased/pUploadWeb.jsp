<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main" ng-controller="UploadViewCtrl"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <!--<li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>-->
    <li class="active"><i class="fa fa-cloud-upload fa-fw"></i> <spring:message code="imageimport.choose.web"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="web.headline"/></h3>
<h4><spring:message code="web.subheadline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->

	<!-- FORMS FÜR EDIT -->

        <div type="button" class="btn btn-default btn-lg btn-block"
             flow-init="{target: '/gateway/upload/folder/<c:out value="${folder.folderId}"/>/;jsessionid=<%= session.getId() %>', chunkSize:201073741824, simultaneousUploads:1}"
             flow-files-submitted="$flow.upload()"
             flow-file-success="uploadFileSuccess($file, $message, $flow)"
             flow-file-error="uploadFileError($file, $message, $flow)"
             flow-complete="uploadComplete($flow,'c?id=<c:out value="${folder.folderId}"/>')"
             flow-error="uploadError($file, $message, $flow)"
             flow-btn
             flow-drop><i class="fa fa-cloud-upload md-text-prim md-thumb-item-upload"></i>
            <div class="md-item-text" ng-show="$flow.files.length==0"><small><spring:message code="dropfileshere"/></small><br /><span class="lead"><strong>oder hier klicken um Dateien auszuwählen</strong></span></div>
            <!-- statuszeilen -->
            <div>
                <div class="md-item-text" ng-show="hasUploadError"><span class="text-danger"><small>{{errorMessage}} <i class="fa fa-exclamation-triangle"></i></small></span></div>
                <div class="md-item-text" ng-repeat="file in $flow.files">
                    <span class="text-primary" ng-show="file.progress(true)==0"><small>{{file.name}} {{file.msg}}</small></span>
                    <span class="text-info" ng-show="file.isUploading() && file.progress(true)!=0"><small>{{file.progress(true)*100 | number:0}}% {{$index+1}} {{file.name}} {{file.msg}} <i class="fa fa-spinner fa-spin"></i></small></span>
                    <span class="text-success" ng-show="file.isComplete() && !file.error"><small>100% {{file.name}} {{file.msg}}<i class="fa fa-thumbs-o-up"></i></small></span>
                    <span class="text-danger" ng-show="file.error"><small>{{file.name}} {{file.msg}}<i class="fa fa-exclamation-triangle"></i></small></span>
                </div>
                <div class="md-item-text" ng-show="$flow.isComplete()"><strong>complete</strong></div>
            </div>
            <!-- / statuszeilen -->
        </div>

    <!-- /FORMS FÜR EDIT -->

<!-- ENDE EDIT BILD -->
<!-- ###################################################################################################################################################### -->



<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- ######### 3er teilung upload optionen - direkt in die inhaltsspalte vor dem /div /col-sm-10 main SPALTE 2 FÜR INHALT ZU  ######### -->
<div class="row"> <!-- 3er row -->
	<!-- erklärung -->
    <div class="col-xs-12 text-center text-info">
    Oder wählen Sie weitere Möglichkeiten um Dateien hochzuladen

   	<!-- mediadesk abstand -->
	<div class="md-space">&nbsp;</div>
	<!-- /mediadesk abstand -->


    </div>
    <!-- /erklärung -->


	<!-- spalte 1 -->
    <div class="col-xs-4">

        <!-- panel -->
        <div class="panel panel-default">

            <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-exchange fa-fw" aria-hidden="true"></i> <spring:message code="imageimport.choose.ftp"/></h3>
            </div>

            <div class="panel-body">
            <!-- panel inhalt -->
                <p class="help-block">
                <!-- test wegen neuem fontawesome -->
                <!--<h4><i class="fa fa-american-sign-language-interpreting" aria-hidden="true"></i> test font awesome</h4><br>-->
                <!-- /test wegen neuem fontawesome -->
                <spring:message code="imageimport.choose.ftpinfo"/> <a href="<c:url value="/${lng}/uploadftp"/>">weiter...</a>
                </p>
            <!-- /panel inhalt -->
            </div>
        </div>
        <!-- /panel -->

    </div>
	<!-- /spalte 1 -->


	<!-- spalte 2 -->
    <div class="col-xs-4">

        <!-- panel -->
        <div class="panel panel-default">

            <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-terminal fa-fw" aria-hidden="true"></i> WEBDAV</h3>
            </div>

            <div class="panel-body">
            <!-- panel inhalt -->
                <p class="help-block">
                    Geben Sie in Ihrem Webdav-Client <strong><c:out value="${httpScheme}"/>://<c:out value="${serverName}"/>/webdav/</strong> ein um auf Ihre mediaDESK zuzugreifen.
                <br/>
                    Sie können unter Windows in der Eingabeaufforderung auch ein Windowslaufwerk verbinden mit:
            <code style="font-family:courier;display:block;background-color:black;border-style:dashed;color:white;">
                net use Z: <c:out value="${httpScheme}"/>://<c:out value="${serverName}"/>/webdav
            </code>
                Wenn Sie Hilfe benötigen finden Sie weitere Details im <a href="https://wiki.openmediadesk.net/en/Webdav">Wiki</a>.
                </p>
            <!-- /panel inhalt -->
            </div>
        </div>
        <!-- /panel -->

    </div>
	<!-- /spalte 2 -->


	<!-- spalte 3 -->
    <div class="col-xs-4">

        <!-- panel -->
        <div class="panel panel-default">

            <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-arrow-up fa-fw" aria-hidden="true"></i> mediaDESK UP</h3>
            </div>

            <div class="panel-body">
            <!-- panel inhalt -->
                <p class="help-block">
                    Mit unserem Desktop-Programm Dateien bequem zu mediaDESK hochladen. Download und Infos  <a href="https://wiki.openmediadesk.net/en/mediaDESK_UP" target="_blank">hier</a>.
                </p>
            <!-- /panel inhalt -->
            </div>
        </div>
        <!-- /panel -->

    </div>
	<!-- /spalte 3 -->





</div> <!-- /3er row -->




<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->

<!-- ######### /3er teilung upload optioen ######### -->


    </div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>