<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<div class="image" id="imgIvId387">
          <div class="imageCa">

          <ul class="imageActionMenu">
            <li>&nbsp;<!--<a href="/de/download?download=ivid&amp;ivid=387"><img class="buttonDownload iconStd" alt="lightbox" src="/img/bl.gif" title="herunterladen"/></a>--></li>
            <li>&nbsp;<!--<a href="/de/send?ivid=387&amp;redirect=%2fde%2fcat%3fnull"><img class="buttonSend iconStd" alt="send" src="/img/bl.gif" title="per Email versenden"/></a>--></li>
          </ul>


        <div class="imageDisplay">

                        <a href="<c:url value="/${lng}/c?id=${subCategory.categoryId}"/>">
                            <c:if test="${subCategory.primaryIvid==0}">
                                <img src="/img/folderthumbnail.gif" alt="<c:out value="${subCategory.catTitle}"/>">
                            </c:if>
                            <c:if test="${subCategory.primaryIvid!=0}">
                                <img alt="<c:out value="${subCategory.catTitle}"/>" src="/imageservlet/<c:out value="${subCategory.primaryIvid}"/>/1/image.jpg">
                            </c:if>
                        </a>

        </div>                    
          <!-- DATA -->
          <!--<p class="imgTitle"><c:out value="${subCategory.catTitle}"/>&nbsp;</p>-->
          <p class="imgTitle"><a href="<c:url value="/${lng}/c?id=${subCategory.categoryId}"/>"><c:out value="${subCategory.catTitle}"/></a>&nbsp;</p>
          <c:if test="${subCategory.primaryIvid!=0}">
          <p class="folderDate"><a href="<c:url value="/${lng}/c?id=${subCategory.categoryId}"/>" class="folderDate"><dt:format pattern="dd MMMM yyyy" default=""><c:out value="${subCategory.categoryDate.time}"/></dt:format></a>&nbsp;</p>
          </c:if>
              <!--
          <p class="imgPeople">&nbsp;</p>
          <p class="imgSite">xx&nbsp;</p>
          <p class="imgDate">xxx&nbsp;</p>
          <p class="imgByline"> &nbsp;</p>
          -->

          </div>
      </div>