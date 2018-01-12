<%@ page import="com.stumpner.mediadesk.core.database.sc.CategoryService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.stumpner.mediadesk.image.category.Category" %>
<%@ page import="com.stumpner.mediadesk.image.category.CategoryMultiLang" %>
<%@ page import="com.stumpner.mediadesk.usermanagement.User" %>
<%@ page import="net.stumpner.security.acl.AclControllerContext" %>
<%@ page import="com.stumpner.mediadesk.usermanagement.acl.AclContextFactory" %>
<%@ page import="net.stumpner.security.acl.AclPermission" %>
<%@ page import="com.stumpner.mediadesk.core.Config" %>
<%@ page import="com.stumpner.mediadesk.web.LngResolver" %>
<%@ page import="com.stumpner.mediadesk.core.database.sc.AclCategoryService" %>
<%@ page import="net.stumpner.security.acl.AclController" %>
<%@ page import="net.stumpner.security.acl.Acl" %>
<%@ page import="com.stumpner.mediadesk.core.database.sc.UserService" %>
<%@ page import="com.stumpner.mediadesk.usermanagement.SecurityGroup" %>
<%@ page import="com.stumpner.mediadesk.web.mvc.util.WebHelper" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<%

    /*********************************************************
     Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

     openMEDIADESK is licensed under Apache License Version 2.0

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.

     *********************************************************/

    long start = System.currentTimeMillis();

    if (request.getParameter("node")!=null) {
        int node = Integer.parseInt(request.getParameter("node"));
        AclCategoryService categoryService = new AclCategoryService(request);
        LngResolver lngResolver = new LngResolver();
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));

        //Zwischenspeichern wenn Gastbenutzer
        User user = WebHelper.getUser(request);
        boolean showCustomFolderIcons = false;
        if (user.getRole()>=User.ROLE_EDITOR) {
            showCustomFolderIcons = true;
        }
        List categoryList = null;
        if (user.getUserId()==-1) { //Gast - Public
            if (application.getAttribute("publicCategoryListTime"+node)==null) {
                categoryList = categoryService.getCategorySubTree(node,0);
                application.setAttribute("publicCategoryList"+node, categoryList);
                application.setAttribute("publicCategoryListTime"+node, System.currentTimeMillis());
                // System.out.println("jsonCategory.jsp: fill cache"+node);
            } else {
                //System.out.println("jsonCategory.jsp: using cache "+node);
                categoryList = (List)application.getAttribute("publicCategoryList"+node);
                long lastUpdate = (Long)application.getAttribute("publicCategoryListTime"+node);
                if (System.currentTimeMillis()-lastUpdate > 10000) { //Millisekunden zum cachen
                    application.removeAttribute("publicCategoryListTime"+node);
                }
            }
        } else {
            categoryList = categoryService.getCategorySubTree(node,0);
        }

        Iterator categories = categoryList.iterator();
    %>

    [<% while (categories.hasNext()) { %>{
    <%
        boolean checkedValue = false;
        Category category = (Category)categories.next();
        request.setAttribute("jsonCategory",category);
        if (request.getParameter("lng")!=null) { request.setAttribute("lng",request.getParameter("lng")); }
        else { request.setAttribute("lng","index"); }
        String categoryTitle = category.getCatTitle().replace('"',' ');
        if (request.getParameter("type")!=null) {
            if (request.getParameter("type").equalsIgnoreCase("ACL")) {
                Acl acl = AclController.getAcl(category);
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(Integer.parseInt(request.getParameter("id")));
                AclPermission permission = new AclPermission(AclPermission.READ);
                if (securityGroup.getId()==0) { permission = new AclPermission("view"); }
                if (acl.checkPermission(securityGroup, permission)) {
                    checkedValue = true;
                }
            }
        }
        request.setAttribute("checkedValue",checkedValue);
    %>
    <c:url value="/${lng}/c" var="catUrl">
        <c:param name="id" value="${jsonCategory.categoryId}"/>
    </c:url>
    <c:url value="/${lng}/categoryedit" var="editUrl">
        <c:param name="categoryid" value="${jsonCategory.categoryId}"/>
    </c:url>
            "id": "<%= category.getCategoryId() %>",
            "text": "<%=  categoryTitle %>",
<%          if (request.getParameter("checked")==null) { %>
            "uiProvider": 'col',
<%          } %>
<% /*
            qtipCfg:{
                text: "<a href='<c:out value="${editUrl}"/>'>[EDIT]</a>",
                width: 200
            }, */%><%
            if (request.getParameter("href")!=null) {
            %>"href": "<c:out value="${catUrl}"/>",<% }
            if (request.getParameter("checked")!=null) {
            %>"checked": <c:out value="${checkedValue}"/>,<% } %>
            "leaf": false
    <%
        if (category.isPublicAcl() && showCustomFolderIcons) {
    %>
            ,"iconCls": "treeIconPublic" 
    <%
        } else if (category.isProtectedAcl() && showCustomFolderIcons) {
    %>
            ,"iconCls": "treeIconProtected"
    <%
        }
    %>
    <% if (category.getIcon().length()>0) { %>
            ,"icon": "<%= category.getIcon() %>"
    <% } %>
        }<% if (categories.hasNext()) { %>,<% } } %>]
<%
    }

    //System.out.println("jsonCategory.jsp: "+(System.currentTimeMillis()-start)+" ms");
%>