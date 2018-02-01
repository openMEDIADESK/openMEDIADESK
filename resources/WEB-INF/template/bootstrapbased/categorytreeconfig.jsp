<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.stumpner.mediadesk.web.mvc.util.WebHelper" %>
<%@ page import="com.stumpner.mediadesk.image.category.Folder" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<script type="text/javascript">

    <%
    User user = WebHelper.getUser(request);
    %>

    var categoryTreeCfgSelectedPath = "/0<%
        if (request.getAttribute("parentCategoryList")!=null) {

            List parentCategoryList = (List)request.getAttribute("parentCategoryList");
            Iterator parentCategories = parentCategoryList.iterator();
            while (parentCategories.hasNext()) {
                Folder folder = (Folder)parentCategories.next();
                %>/<%= folder.getCategoryId() %><%
            }
        }
        %>";
    <c:url value="/${lng}/categoryedit" var="newSubCatUrl">
        <c:param name="parentCat" value="${folder.categoryId}"/>
    </c:url>
    <c:url value="/${lng}/categorybreakup" var="deleteCatUrl">
        <c:param name="categoryid" value="${folder.categoryId}"/>
    </c:url>
    var categoryTreeCfgNewUrl = "<c:url value="/${lng}/categoryedit"/>";
    var categoryTreeCfgNewCaption = "<spring:message code="sm.cat.createnew"/>";
    var categoryTreeCfgNewSubUrl = "<c:out escapeXml="false" value="${newSubCatUrl}"/>";
    var categoryTreeCfgNewSubCaption = "<spring:message code="folder.createsubin" arguments="${folder.catTitle}"/>";
    var categoryTreeCfgDeleteUrl = "<c:out escapeXml="false" value="${deleteCatUrl}"/>";

    var categoryTreeCfgJsonLoaderUrl = "/jsonCategory.jsp;jsessionid=<%= session.getId() %>?lng=<c:out value="${lng}"/>&href=true"

    var categoryTreeCfgDragAndDropEnabled = <%= user.getRole()>=User.ROLE_ADMIN ? "true" : "false" %>;

    var categoryTreeCfgWidth = 200;
    var categoryTreeCfgWidthEditButton = <%= user.getRole()>=User.ROLE_MASTEREDITOR ? "25" : "0" %>;

    var categoryTreeCfgShowToolbar = false;
    <c:if test="${mediaMenu.actionNewCat || createRootCat}">
    categoryTreeCfgShowToolbar = true;
    </c:if>
    var categoryTreeCfgShowToolbarNewRootAndSub = false;
    <c:if test="${createRootCat && folder.categoryId>0}">
    categoryTreeCfgShowToolbarNewRootAndSub = true;
    </c:if>
    var categoryTreeCfgShowToolbarDelete = false;
    <c:if test="${mediaMenu.actionDeleteCat && folder.categoryId>0}">
    categoryTreeCfgShowToolbarDelete = true;
    </c:if>

    var paramLng = "<c:out value="${lng}"/>";

    function categoryTreeBuilder() {
        createCategoryTree();
    }
</script>