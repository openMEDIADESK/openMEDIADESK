<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<div id="breadcrumb">
    <p>
        <a href="<c:url value="${home}"/>">&raquo; HOME</a>

            &nbsp;/ <spring:message code="menu.imageimport"/>

    </p>
</div>

<div id="contentHead">
    <h1><spring:message code="imageimport.choose.headline"/></h1>
    <h2><spring:message code="imageimport.choose.subheadline"/></h2>
</div>

<div class="imageimportCoose">

    <div class="importoption ioWeb"  onclick="javascript:location.href='<c:url value="/${lng}/uploadweb"/>';">
        <h3><spring:message code="imageimport.choose.web"/></h3>
        <span><spring:message code="imageimport.choose.webinfo"/></span>
        <a href="<c:url value="/${lng}/uploadweb"/>"><spring:message code="imageimport.choose.go"/> &gt;&gt;</a>
    </div>

    <div class="importoption ioHtml5"  onclick="javascript:location.href='<c:url value="/${lng}/uploadweb?html5=true"/>';">
        <h3><spring:message code="imageimport.choose.web5"/></h3>
        <span><spring:message code="imageimport.choose.web5info"/></span>
        <a href="<c:url value="/${lng}/uploadweb?html5=true"/>"><spring:message code="imageimport.choose.go"/> &gt;&gt;</a>
    </div>

    <c:if test="${ftpConfigured}">
    <div class="importoption ioFtp" onclick="javascript:location.href='<c:url value="/${lng}/uploadfs"/>';">
        <h3><spring:message code="imageimport.choose.ftp"/></h3>
        <span><spring:message code="imageimport.choose.ftpinfo"/></span>
        <a href="<c:url value="/${lng}/uploadfs"/>"><spring:message code="imageimport.choose.go"/> &gt;&gt;</a>
    </div>
    </c:if>
    <c:if test="${!ftpConfigured}">
    <div class="importoption ioFtp">
        <h3><spring:message code="imageimport.choose.ftp"/></h3>
        <span><spring:message code="imageimport.choose.ftpinfo"/></span>
        <form action="<c:url value="/${lng}/uploadfs"/>" method="POST">
            <dl>
                <dt><label>FTP-Server: </label></dt>
                <dd><input type="text" name="ftpHost"/></dd>
                <dt><label>Benutzer: </label></dt>
                <dd><input type="text" name="ftpUser"/></dd>
                <dt><label>Passwort: </label></dt>
                <dd><input type="password" name="ftpPassword"/></dd>

                <c:if test="${canAdminFtp}">
                <dt>&nbsp;</dt>
                <dd><input type="checkbox" name="saveFtp" value="true"/> <label>als Standard konfigurieren</label></dd>
                </c:if>

                <dt><input type="submit" value="<spring:message code="imageimport.choose.go"/>"/></dt>
                <dd>&nbsp;</dd>
            </dl>
        </form>
    </div>
    </c:if>

    <c:if test="${webdavEnabled}">
    <div class="importoption ioWebdav">
        <h3>Webdav</h3> <a href="https://wiki.openmediadesk.net/en/Webdav" target="_blank">[?]</a>
        <span>Geben Sie in Ihrem Webdav-Client <strong>http://<c:out value="${serverName}"/>/webdav/</strong> ein um auf Ihre mediaDESK zuzugreifen.</span>
        <p>
            Sie können unter Windows in der Eingabeaufforderung auch ein Windowslaufwerk verbinden mit:
            <code style="font-family:courier;display:block;background-color:black;border-style:dashed;color:white;">
                net use Z: http://<c:out value="${serverName}"/>/webdav
            </code>
        </p>
        <p>Webdav Clients:</p>
        <ul>
            <li><a href="http://www.webdrive.com/download/index.html" target="_blank">http://www.webdrive.com</a></li>
            <li><a href="https://market.android.com/details?id=com.schimera.webdavnavlite" target="_blank">Android WebDav Navigator Lite</a></li>
            <li><a href="http://itunes.apple.com/us/app/webdav-navigator/id382551345?mt=8" target="_blank">iPhone/iPad WebDav Navigator Lite</a></li>
        </ul>
    </div>
    </c:if>

</div>

<jsp:include page="footer.jsp"/>