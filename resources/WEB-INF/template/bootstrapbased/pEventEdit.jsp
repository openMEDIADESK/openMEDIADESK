<%@ page import="com.stumpner.mediadesk.usermanagement.User" %>
<%@ page import="com.stumpner.mediadesk.core.Config" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<div id="breadcrumb">
    <p>
        <a href="<c:url value="${home}"/>">&raquo; HOME</a>

            &nbsp;/ <a href=""><spring:message code="menu.foldermanager"/></a>

        &nbsp;/ <spring:bind path="command.folderName"><c:out value="${status.value}"/></spring:bind>
    </p>
</div>

<div id="contentHead">
    <h1><spring:message code="folderedit.headline"/></h1>
    <h2><spring:message code="folderedit.subheadline"/></h2>
</div>

<div id="foldereditpage">

    <p>
        <spring:message code="folderedit.info"/>
    </p>

    <form method="post" id="folderedit" action="folderedit">

        <dl>
        <spring:bind path="command.folderNumber">
            <dt><label for="textFoldernumber"><spring:message code="folderedit.number"/></label></dt>
            <dd><input type="text" id="textFoldernumber" value="<c:out value="${status.value}"/>" readonly="true"></dd>
        </spring:bind>

        <spring:bind path="command.folderName">
            <dt><label for="textName"><spring:message code="folderedit.name"/></label></dt>
            <dd><input name="folderName" type="text" id="textName" value="<c:out value="${status.value}"/>"></dd>
        </spring:bind>

        <spring:bind path="command.folderTitleLng1">
            <dt><label for="textTitleLng1" class="labLangDe"><spring:message code="folderedit.title"/> [DE]</label></dt>
            <dd><input name="folderTitleLng1" type="text" id="textTitleLng1" value="<c:out value="${status.value}"/>"></dd>
        </spring:bind>

        <spring:bind path="command.folderTitleLng2">
            <dt><label for="textTitleLng2" class="labLangEn"><spring:message code="folderedit.title"/> [EN]</label></dt>
            <dd><input name="folderTitleLng2" type="text" id="textTitleLng2" value="<c:out value="${status.value}"/>"></dd>
        </spring:bind>

        <spring:bind path="command.folderSubTitleLng1">
            <dt><label for="textSubtitleLng1" class="labLangDe"><spring:message code="folderedit.subtitle"/> [DE]</label></dt>
            <dd><input name="folderSubTitleLng1" type="text" id="textSubtitleLng1" value="<c:out value="${status.value}"/>"></dd>
        </spring:bind>

        <spring:bind path="command.folderSubTitleLng2">
            <dt><label for="textSubtitleLng2" class="labLangEn"><spring:message code="folderedit.subtitle"/> [EN]</label></dt>
            <dd><input name="folderSubTitleLng2" type="text" id="textSubtitleLng2" value="<c:out value="${status.value}"/>"></dd>
        </spring:bind>

        <spring:bind path="command.folderDate">
            <dt><label for="textFolderdate"><spring:message code="folderedit.folderdate"/></label></dt>

            <dd>
                <input name="folderDate" id="textFolderdate" type="text" value="<c:out value="${status.value}"/>">
                <img alt="calendar" src="/img/calendar.jpg" id="trigger" type="button"/>
                <script type="text/javascript">
                    Calendar.setup(
                            {
                                    inputField : "textFolderdate",
                                    ifFormat : "%d.%m.%Y",
                                    button : "trigger"
                            }
                            );
                </script>
            </dd>
        </spring:bind>

        <spring:bind path="command.createDate">
            <dt><label for="textCreationdate"><spring:message code="folderedit.creationdate"/></label></dt>
            <dd><input type="text" id="textCreationdate" value="<dt:format pattern="dd MMMM yyyy" default=""><c:out value="${command.createDate.time}"/></dt:format>" readonly="true"/></dd>
        </spring:bind>

            <dt><label for="selSortBy"><spring:message code="set.application.sort.folder"/></label></dt>
            <dd>
                <select id="selSortBy" name="sortBy">
                <spring:bind path="command.sortBy">
                    <option value="0"<c:if test="${status.value==0}"> selected</c:if>>Standardsortierung verwenden</option>
                    <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.sort.latestDate"/></option>
                    <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="set.application.sort.latestImport"/></option>
                    <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.sort.latestTitle"/></option>
                    <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.sort.latestPeople"/></option>
                    <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.application.sort.latestSite"/></option>
                    <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="set.application.sort.latestByline"/></option>
                </spring:bind>
                </select>

                <select id="selOrderByFolder" name="orderBy">
                <spring:bind path="command.orderBy">
                    <option value="0"<c:if test="${status.value==0}"> selected</c:if>>-</option>
                    <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.sort.asc"/></option>
                    <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.sort.desc"/></option>
                </spring:bind>
                </select>
            </dd>
        </dl>

        <input name="Submit2" type="reset" class="actionButton resetButton" value="<spring:message code="folderedit.reset"/>">
        <input <c:if test="${command.folderNumber==0}">disabled="true" readonly="true"</c:if> name="acl" type="submit" class="actionButton aclButton" value="<spring:message code="categoryedit.acl"/>">
        <input name="Submit" type="submit" class="actionButton resetButton" value="<spring:message code="folderedit.submit"/>">


    </form>



</div>

<jsp:include page="footer.jsp"/>