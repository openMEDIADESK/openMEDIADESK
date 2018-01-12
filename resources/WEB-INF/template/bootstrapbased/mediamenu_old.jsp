<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<jsp:useBean id="counter" class="com.stumpner.mediadesk.util.CounterBean" scope="request"/>
<jsp:setProperty name="counter" property="increment" value="1"/>
<c:if test="${counter.value==1}">
    <mediadesk:login notLoggedIn="true">
        <c:if test="${pinmenu}">
<script type="text/javascript">

var selectedCounter = new Ext.Toolbar.Button({
    text: '<spring:message code="imagemenu.selected"/>: <c:out value="${selectedImages}"/>'
});

function mediaMenu() {

    var tb = new Ext.Toolbar();
    tb.render('toolbar');
    tb.add(selectedCounter);
    tb.addFill();

        //select menu
            <c:url var="unmarkAllUrl" value="${servletMapping}">
              <c:param name="unmark" value="all"/>
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:url var="markAllUrl" value="${servletMapping}">
              <c:param name="mark" value="all"/>
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:url var="markSiteUrl" value="${servletMapping}">
              <c:param name="mark" value="site"/>
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:if test="${mediaMenu.selection}">

    var selectMenu = new Ext.menu.Menu();
                <c:if test="${mediaMenu.selectionMarkAll}">
    selectMenu.add({
        text:'<spring:message code="categoryindex.markall"/>',
        href:'<c:out escapeXml="false" value="${markAllUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionMarkAll}">
    selectMenu.add({
        text:'<spring:message code="categoryindex.marksite"/>',
        href:'<c:out escapeXml="false" value="${markSiteUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionUnmarkAll}">
    selectMenu.add({
        text:'<spring:message code="categoryindex.unmarkall"/>',
        href:'<c:out escapeXml="false" value="${unmarkAllUrl}"/>'
    });
                </c:if>
            </c:if>

    tb.add({
        text: '<spring:message code="imagemenu.selection"/>',
        menu: selectMenu
    });

    tb.add(
        {
        <c:url var="downloadUrl" value="/download">
          <c:param name="pinpic" value="DTHCVBNCFG75GHDXC34XFGS346554345462345234523452GDFVGRTZUERETZE34232345SDFSGZTJ766456537FWERFASDFYX234"/>
        </c:url>
            text:'<spring:message code="shoppingcart.download"/>',
            iconCls: 'buttonDownload',  // <-- icon
            handler: function() {
                window.location.href = "<c:out escapeXml="false" value="${downloadUrl}"/>";
            }
            //menu: selectMenu  // assign menu by instance
        }
    );

}

</script>

    <div id="toolbar">
    </div>

    <span style="display:none;" id="selectedImages<c:out value="${counter.value}"/>"><c:out value="${selectedImages}"/></span>
        </c:if>
    </mediadesk:login>
    <mediadesk:login>
<script type="text/javascript">

var selectedCounter = new Ext.Toolbar.Button({
    text: '<spring:message code="imagemenu.selected"/>: <c:out value="${selectedImages}"/>'
});

function mediaMenu() {

    var tb = new Ext.Toolbar();
    tb.render('toolbar');
    tb.add(selectedCounter);
    tb.addFill();

    <mediadesk:login role="<%= User.ROLE_ADMIN %>">
        <c:if test="${mediaMenu.bulkModification}">
            <c:url var="bulkUrl" value="bulkmodification">
            </c:url>
    tb.add({
        text: '<spring:message code="searchresult.bulkmod"/>',
        handler: function() { window.location.href = "<c:out value="${bulkUrl}"/>"; }
    });
        </c:if>
    </mediadesk:login>
    <mediadesk:login>

        <c:if test="${servletMapping!='/index/lightbox' && servletMapping!='/index/shoppingcart'}">
            <c:url var="deleteUrl" value="${servletMapping}">
              <c:param name="delete" value="selectedMedia"/>
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
              <c:param name="redirectTo" value="${url}"/>
            </c:url>
            <c:url var="removeUrl" value="${servletMapping}">
              <c:param name="remove" value="selectedMedia"/>
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:if test="${mediaMenu.delete}">

    var deleteMenu = new Ext.menu.Menu();
                <c:if test="${mediaMenu.deleteFromDB}">
    deleteMenu.add({
        text: '<spring:message code="imagemenu.deletedb"/>',
        href: '<c:out escapeXml="false" value="${deleteUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.deleteFromCategory}">
    deleteMenu.add({
        text: '<spring:message code="imagemenu.deletecat"/>',
        href: '<c:out escapeXml="false" value="${removeUrl}"/>',
        iconCls: 'buttonDeleteFrom'
    });
                </c:if>
                <c:if test="${mediaMenu.deleteFromFolder}">
    deleteMenu.add({
        text: '<spring:message code="imagemenu.deletefolder"/>',
        href: '<c:out escapeXml="false" value="${removeUrl}"/>',
        iconCls: 'buttonDeleteFrom'
    });
                </c:if>
                <c:if test="${mediaMenu.deleteFromPin}">
    deleteMenu.add({
        text: '<spring:message code="pinpic.imageremove"/>',
        href: '<c:out escapeXml="false" value="${removeUrl}"/>'
    });
                </c:if>
    tb.add({
        text: '<spring:message code="imagemenu.delete"/>',
        iconCls: 'buttonDelete',
        menu: deleteMenu
    });
            </c:if>

        </c:if>
    </mediadesk:login>

    <!-- auswahl -->
    <mediadesk:login>

    //select menu
        <c:url var="unmarkAllUrl" value="${servletMapping}">
          <c:param name="unmark" value="all"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
        </c:url>
        <c:url var="markAllUrl" value="${servletMapping}">
          <c:param name="mark" value="all"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
        </c:url>
        <c:url var="markSiteUrl" value="${servletMapping}">
          <c:param name="mark" value="site"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
        </c:url>
        <c:url var="copyUrl" value="${servletMapping}">
          <c:param name="insert" value="selectedMedia"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
          <c:param name="func" value="copy"/>
        </c:url>
        <c:url var="copyCatUrl" value="${servletMapping}">
          <c:param name="insert" value="cat"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
          <c:param name="func" value="copy"/>
          <c:param name="type" value="pin"/>
        </c:url>
        <c:url var="moveUrl" value="${servletMapping}">
          <c:param name="insert" value="selectedMedia"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
          <c:param name="func" value="move"/>
        </c:url>
        <c:url var="asFolderImageUrl" value="${servletMapping}">
          <c:param name="setIvid" value="selectedMedia"/>
          <c:param name="id" value="${containerId}"/>
          <c:param name="page" value="${pageIndex}"/>
        </c:url>
        <c:url var="toPinUrl" value="pinwizard">
        </c:url>

            <c:if test="${mediaMenu.selection}">

    var selectMenu = new Ext.menu.Menu();
                <c:if test="${mediaMenu.selectionMarkAll}">
    selectMenu.add({
        text:'<spring:message code="categoryindex.markall"/>',
        href:'<c:out escapeXml="false" value="${markAllUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionMarkAll}">
    selectMenu.add({
        text:'<spring:message code="categoryindex.marksite"/>',
        href:'<c:out escapeXml="false" value="${markSiteUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionUnmarkAll}">
    selectMenu.add({
        text:'<spring:message code="categoryindex.unmarkall"/>',
        href:'<c:out escapeXml="false" value="${unmarkAllUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionAsFolderImage}">
    selectMenu.add({
        text:'<spring:message code="folderview.asfolderimage"/>',
        href:'<c:out escapeXml="false" value="${asFolderImageUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionCopy}">
    selectMenu.add({
        text:'<spring:message code="imagemenu.copy"/>',
        href:'<c:out escapeXml="false" value="${copyUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionFromCategory}">
    selectMenu.add({
        text:'<spring:message code="imagemenu.copycat"/>',
        href:'<c:out escapeXml="false" value="${copyCatUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionMove}">
    selectMenu.add({
        text:'<spring:message code="imagemenu.move"/>',
        href:'<c:out escapeXml="false" value="${moveUrl}"/>'
    });
                </c:if>
                <c:if test="${mediaMenu.selectionToPin}">
    selectMenu.add({
        text:'<spring:message code="imagemenu.aspin"/>',
        iconCls: 'buttonPin',
        href:'<c:out escapeXml="false" value="${toPinUrl}"/>'
    });
                </c:if>

    <!-- shopping -cart -->
    <c:url var="addToCartUrl" value="${servletMapping}">
      <c:param name="addToCart" value="selectedMedia"/>
      <c:param name="page" value="${pageIndex}"/>
    </c:url>
    <c:url var="addAllToCartUrl" value="${servletMapping}">
      <c:param name="addToCart" value="all"/>
      <c:param name="page" value="${pageIndex}"/>
    </c:url>

    <c:if test="${mediaMenu.selectionToShoppingcart}">
    selectMenu.add({
        text:'<spring:message code="lightbox.add"/>',
        href:'<c:out escapeXml="false" value="${addToCartUrl}"/>'
    });
    </c:if>
    <c:if test="${mediaMenu.allToShoppingcart}">
    selectMenu.add({
        text:'<spring:message code="lightbox.addall"/>',
        href:'<c:out escapeXml="false" value="${addAllToCartUrl}"/>'
    });
    </c:if>
    <!-- shopping -cart ende -->

    tb.add({
        text: '<spring:message code="imagemenu.selection"/>',
        menu: selectMenu
    });

            </c:if>
    </mediadesk:login>

    <!-- end auswahl -->

    <c:if test="${servletMapping=='/index/lightbox' || servletMapping=='/index/shoppingcart'}">
        <c:if test="${imageCount>0}">
            <c:url var="removeUrl" value="${servletMapping}">
              <c:param name="remove" value="selectedMedia"/>
              <c:param name="id" value="${containerId}"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:url var="removeAllUrl" value="${servletMapping}">
              <c:param name="remove" value="all"/>
              <c:param name="page" value="${pageIndex}"/>
            </c:url>
            <c:if test="${mediaMenu.delete}">

    var deleteMenu = new Ext.menu.Menu();
            <c:if test="${mediaMenu.deleteFromLightbox}">
    deleteMenu.add({
        text:'<spring:message code="shoppingcart.delteselection"/>',
        href:'<c:out escapeXml="false" value="${removeUrl}"/>'
    });
            </c:if>
            <c:if test="${mediaMenu.deleteFromShoppingcart}">
    deleteMenu.add({
        text:'<spring:message code="shoppingcart.delteselection"/>',
        href:'<c:out escapeXml="false" value="${removeUrl}"/>'
    });
            </c:if>
            <c:if test="${mediaMenu.deleteAll}">
    deleteMenu.add({
        text:'<spring:message code="shoppingcart.deleteall"/>',
        href:'<c:out escapeXml="false" value="${removeAllUrl}"/>'
    });
            </c:if>

    tb.add({
        text: '<spring:message code="imagemenu.delete"/>',
        menu: deleteMenu
    });

            </c:if>
        </c:if>

    </c:if>

    <c:if test="${!pinmenu}">
        <c:url var="downloadUrl" value="/index/shoppingcart">
            <c:param name="download" value="selectedMedia"/>
        </c:url>
    </c:if>
    <c:if test="${pinmenu}">
        <c:url var="downloadUrl" value="/download">
          <c:param name="pinpic" value="DTHCVBNCFG75GHDXC34XFGS346554345462345234523452GDFVGRTZUERETZE34232345SDFSGZTJ766456537FWERFASDFYX234"/>
        </c:url>
    </c:if>
    <% if (Config.quickDownload) { %>
    <c:if test="${mediaMenu.download}">
            tb.add(
                {
                    text:'<spring:message code="shoppingcart.download"/>',
                    iconCls: 'buttonDownload',  // <-- icon
                    handler: function() {
                        window.location.href = "<c:out escapeXml="false" value="${downloadUrl}"/>";
                    }
                    //menu: selectMenu  // assign menu by instance
                }
            );
    </c:if>
    <% } %>

    if (document.getElementById('selectedImages1')!=null) {
        var selectedCount = parseInt(document.getElementById('selectedImages1').innerHTML);
        selectedCounter.setText('<spring:message code="imagemenu.selected"/> : '+selectedCount);
    }

}

</script>

    <div id="toolbar">
    </div>

    <span style="display:none;" id="selectedImages<c:out value="${counter.value}"/>"><c:out value="${selectedImages}"/></span>

    </mediadesk:login>

    <c:if test="${!empty showInfoMessage}"><spring:message code="${showInfoMessage}"/></c:if>
</c:if>