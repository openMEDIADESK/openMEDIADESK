<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.image.MediaObject" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<!-- ALT: jsp:include page="${listView}" flush="true" /> -->
<!-- hier startet mit ROW eine neue zeile mit den div als thumbcontainer -->
<!-- verschachtelung -->
<!-- div class="row"> <!-- thumb row -->

<!-- #################################################################### -->
<!-- ###################### THUMB ELEMENTE AB HIER ######################-->

<!-- inhaltThumbs -->
<div id="md-thumbs" ng-show="mosView=='thumbnails'">

    <jsp:include page="listThumbnails.jsp"/>

</div>
<!-- inhaltFiles/Tabelle -->
<jsp:include page="listFiles.jsp"/>

<!-- Daten werden geladen -->
<div id="md-moview-loading" ng-show="loadInProgress!=0">
    <jsp:include page="iconSpinner.jsp?size=5"/>
    <!--
    <i class="fa fa-spinner fa-5x fa-spin"></i>
    -->
</div>
<!-- /Daten werden geladen -->

<!-- ende gesucht -->
<!-- /div><!-- /thumb row ende und zu -->
<!-- Hier wird per include der HTML Code fuer Modal Windows eingefuegt -->
<jsp:include page="wPreview.jsp"/>
<jsp:include page="wDeleteMedia.jsp"/>
