<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.stumpner.mediadesk.image.category.Category" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<!-- MENUE -->

<div id="menue">
<%
    if (session.getAttribute("user")==null) {
        //nicht eingeloggte user
%>

    <div id="guestmenu">
        <ul>
        <c:forEach items="${topMenu}" var="menue">
            <c:url value="${menue.linkUrl}" var="linkUrl">
                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
            </c:url>
            <li><a href="<c:out value="${linkUrl}"/>"<c:if test="${menue.linkUrl=='/pin'}"> class="menuPinDownload"</c:if><c:if test="${menue.openAs==2 || menue.openAs==1}"> onClick="window.open('<c:out value="${linkUrl}"/>','PopTerm'<c:if test="${menue.openAs==2}">,'scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=600,height=500'</c:if>);return false;"</c:if>><c:out value="${menue.title}"/></a></li>
        </c:forEach>
        </ul>
    </div>
<%
    } else {
        //eingeloggte user
%>
    <mediadesk:login role="<%= User.ROLE_IMPORTER %>">
        <ul id="adminmenu">
            <c:if test="${showMenuUpload}">
            <c:url value="/${lng}/uploadweb" var="uploadUrl">
                <c:param name="catid" value="${category.categoryId}"/>
                <c:param name="pinid" value="${pin.pinpicId}"/>
            </c:url>
            <li><a href="<c:out value="${uploadUrl}"/>" class="menuImageimport"><spring:message code="menu.imageimport"/></a></li>
            </c:if>
            <c:if test="${showMenuPinmanager}">
                <li><a href="<c:url value="/${lng}/pinlist"/>" class="menuPinlist"><spring:message code="menu.pinlist"/></a></li>
            </c:if>
            <c:if test="${showMenuUsermanager}">
                <li><a href="<c:url value="/${lng}/usermanager"/>" class="menuUserlist"><spring:message code="menu.usermanager"/></a></li>
            </c:if>
            <mediadesk:login roleNot="<%= User.ROLE_ADMIN %>">
                <li><a href="<c:url value="/${lng}/password"/>" class="menuPassword"><spring:message code="menu.setpassword"/></a></li>
            </mediadesk:login>
            <mediadesk:login role="<%= User.ROLE_ADMIN %>">
                <li><a href="<c:url value="/${lng}/settings"/>" class="menuSettings"><spring:message code="menu.settings"/></a></li>
                <li><a href="<c:url value="/${lng}/stat"/>" class="menuStats"><spring:message code="menu.stats"/></a></li>
            </mediadesk:login>
            <li><a href="<c:url value="/${lng}/login"><c:param name="logout" value=""/></c:url>" class="menuLogout"><spring:message code="menu.logout"/></a></li>
        </ul>
    </mediadesk:login>
    <mediadesk:login roleNot="<%= User.ROLE_IMPORTER %>">
        <ul id="usermenu">
            <li><a href="<c:url value="/${lng}/login"><c:param name="logout" value=""/></c:url>"><spring:message code="menu.logout"/></a></li>
            <li><a href="<c:url value="/${lng}/password"/>"><spring:message code="menu.setpassword"/></a></li>
        </ul>
    </mediadesk:login>
<%
    }
%>
</div>

<!-- \MENUE -->
<div id="panel">


    <c:if test="${fn:length(sideMenu)>0}">
    <div id="sideMenu">
        <ul>
        <c:forEach items="${sideMenu}" var="menue">
            <c:url value="${menue.linkUrl}" var="linkUrl">
                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
            </c:url>
            <li><a href="<c:out value="${linkUrl}"/>"<c:if test="${menue.openAs==2 || menue.openAs==1}"> onClick="window.open('<c:out value="${linkUrl}"/>','PopTerm'<c:if test="${menue.openAs==2}">,'scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=600,height=500'</c:if>);return false;"</c:if>><c:out value="${menue.title}"/></a></li>
        </c:forEach>
        </ul>
    </div>
    </c:if>

    <mediadesk:login>
    <c:if test="${useShoppingCart || showLightbox}">
    <div id="box">
        <div id="cartBox">
            <c:if test="${useShoppingCart}">
            <div id="shoppingCart">
                <h2><spring:message code="left.shoppingcart"/></h2>
                <div id="shoppingCartCa">
                    <span><c:out value="${shoppingCartCount}"/> <spring:message code="left.itemsinthecart"/></span>
                    <c:if test="${shoppingCartCount>0}">
                        <a href="<c:url value="/${lng}/shop"/>">&raquo; <spring:message code="left.showcart"/></a>
                    </c:if>
                </div>
            </div>
            </c:if>
            <c:if test="${showLightbox}">
            <div id="lightBox">
                <h2><spring:message code="left.lightbox"/></h2>
                <div id="lightBoxCa">
                    <span><c:out value="${lightboxCount}"/> <spring:message code="left.itemsinthelightbox"/></span>
                    <c:if test="${lightboxCount>0}">
                        <a href="<c:url value="/${lng}/f"/>">&raquo; <spring:message code="left.showlightbox"/></a>
                    </c:if>
                </div>
            </div>
            </c:if>
        </div>
    </div>
    </c:if>
    </mediadesk:login>



<%
    if (Config.showCategoryTree) {
%>
<!--
 CSS Anweisungen für den Category-Tree liegen unter /css/presets/extjs-tree.css und werden in headercss.jsp geladen
-->

    <jsp:include page="categorytree.jsp" flush="true" />
<%
    }
%>
</div>
