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
        <c:forEach items="${breadCrumb}" var="navItem">
            <c:url var="thisCatLink" value="${navItem.url}">
                <c:param name="id" value="${navItem.id}"/>
            </c:url>
            &nbsp;/
            <c:if test="${navItem.showFolder}">
                <img alt="folder" src="/img/o_folder.gif" style="border:none;vertical-align:top;"/>
            </c:if>
            <a href="<c:out value="${thisCatLink}"/>"><c:out value="${navItem.title}"/></a>
        </c:forEach>
            &nbsp;/ Per Email versenden
    </p>
</div>

<div id="contentHead">
        <h1>
            Per Email versenden
        </h1>
        <h2><c:out value="${command.imageVersion.versionTitle}"/></h2>
</div>

<img src="/imageservlet/<c:out value="${command.imageVersion.ivid}"/>/1/file.JPG" alt=""/>

<p>
    Versenden Sie diese Datei per Email: als Attachment sodass der Empfänger die komplette Bilddatei erhält oder als
    Link damit der Empfänger sich das Bild in der Bilddatenbank ansehen kann.
</p>

<form action="send" method="POST" id="sendform">

            <dl>
                <dt>
                    Absender:
                </dt>
                <dd>
                    <%= Config.mailsender %>
                </dd>
            </dl>

        <spring:bind path="command.recipient">
            <dl>
                <dt>
                    <label for="textRecipient"<c:if test="${status.error}"> class="fieldError"</c:if>>Email Empfänger: </label>
                </dt>
                <dd>
                    <input name="recipient" class="textField<c:if test="${status.error}"> fieldError</c:if>" type="text" id="textRecipient" value="<c:out value="${status.value}"/>"/>
                </dd>
            </dl>
        </spring:bind>

        <spring:bind path="command.subject">
            <dl>
                <dt>
                    <label for="textSubject"<c:if test="${status.error}"> class="fieldError"</c:if>>Betreff: </label>
                </dt>
                <dd>
                    <input name="subject" class="textField<c:if test="${status.error}"> fieldError</c:if>" type="text" id="textSubject" value="<c:out value="${status.value}"/>"/>
                </dd>
            </dl>
        </spring:bind>

        <spring:bind path="command.mailtext">
            <dl>
                <dt>
                    <label for="textMailtext"<c:if test="${status.error}"> class="fieldError"</c:if>>Text: </label>
                </dt>
                <dd>
                    <textarea cols="15" rows="15" name="mailtext" class="textField<c:if test="${status.error}"> fieldError</c:if>" id="textMailtext"><c:out value="${status.value}"/></textarea>
                </dd>
            </dl>
        </spring:bind>

        <spring:bind path="command.asAttachment">
            <dl>
                <dt>&nbsp;</dt>
                <dd>
                <input name="asAttachment" class="textField<c:if test="${status.error}"> fieldError</c:if>" type="radio" id="cbxAsAttachment" value="true" <c:if test="${status.value}">checked="true"</c:if> <c:if test="${command.onlyAsLink}"> disabled="true" readonly="true" </c:if>>
                <label for="cbxAsAttachment"<c:if test="${status.error}"> class="fieldError"</c:if>>als Attachment versenden. </label>
                </dd>
            </dl>
            <dl>
                <dt>&nbsp;</dt>
                <dd>
                <input name="asAttachment" class="textField<c:if test="${status.error}"> fieldError</c:if>" type="radio" id="cbxAsLink" value="false" <c:if test="${!status.value}">checked="false"</c:if>/>
                <label for="cbxAsLink"<c:if test="${status.error}"> class="fieldError"</c:if>>als Link versenden. </label>
                </dd>
            </dl>
        </spring:bind>

    <c:if test="${useCaptcha}">
            <dl>
                <dt>&nbsp;</dt>
                <dd>
                    <fieldset id="captcha">
                        <p>
                        <img src="/captcha.jsp;jsessionid=<%= session.getId() %>" alt="Chaptcha"/>
                        </p>
                        <label><spring:message code="login.captcha"/>:</label>
                        <input type="text" name="captcharesponse" value="">
                    </fieldset>
                </dd>
            </dl>
     </c:if>

        <dl>
                <dt>&nbsp;</dt>
                <dd>
                    <input type="submit" name="cancel" value="abbrechen"/>
                    <input type="submit" value="absenden"/>
                </dd>
        </dl>

         <spring:hasBindErrors name="command">
         <spring:bind path="command">
            <div class="formErrorSummary">
                  <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br>
                  </c:forEach>
             </div>
         </spring:bind>
         </spring:hasBindErrors>


</form>

<jsp:include page="footer.jsp"/>