<%@ page import="com.stumpner.mediadesk.usermanagement.User"%>
<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

      <div class="cartObject" id="imgIvId<c:out value="${image.ivid}"/>">
          <div class="imageCa">

              <!--
          <c:if test="${showImageActionMenu}">
          <ul class="imageActionMenu">
            <c:if test="${showDownload}"><li><a href="<c:out value="${downloadUrl}"/>"><img class="buttonDownload iconStd" alt="lightbox" src="/img/bl.gif" title="<spring:message code="tm.download"/>"/></a></li></c:if>
            <c:if test="${showSendImage}"><li><a href="<c:out value="${sendUrl}"/>"><img class="buttonSend iconStd" alt="send" src="/img/bl.gif" title="<spring:message code="tm.send"/>"/></a></li></c:if>
            <mediadesk:login role="<%= User.ROLE_EDITOR %>"><li><a href="<c:out value="${editUrl}"/>"><img class="buttonEdit iconStd" alt="edit" src="/img/bl.gif" title="<spring:message code="tm.edit"/>"/></a></li></mediadesk:login>
            <mediadesk:login><c:if test="${useShoppingCart}"><li><a href="<c:out value="${shoppingCartUrl}"/>"><img class="buttonCart iconStd" alt="cart" src="/img/bl.gif" title="<spring:message code="tm.tocart"/>"/></a></li></c:if></mediadesk:login>
            <mediadesk:login><c:if test="${useLightbox}"><li><a href="<c:out value="${lightboxUrl}"/>"><img class="buttonLightbox iconStd" alt="lightbox" src="/img/bl.gif" title="<spring:message code="tm.tolightbox"/>"/></a></li></c:if></mediadesk:login>
          </ul>
          </c:if>  -->

        <div class="imageCart">

              <c:if test="${image.mayorMime=='image'||image.mayorMime=='video'||image.mayorMime=='audio'||image.mimeType=='application/pdf'}">
                  <c:if test="${image.mayorMime=='image'||image.mimeType=='application/pdf'}">
                      <c:if test="${inlinePreview}">
                       <c:if test="${true}">
                        <a href="<c:out value="${imageUrl}"/>"><img src="/imageservlet/<c:out value="${image.ivid}"/>/1/<c:out value="${image.webFilename}"/>" alt="<c:out value="${image.versionTitle}"/>"></a>
                       </c:if>
                       <c:if test="${false}">
                           <!-- lightbox: obige abfrage auf true stellen, dann wird die Lightbox angezeigt -->
                           <a href="/imageservlet/<c:out value="${image.ivid}"/>/2/<c:out value="${image.webFilename}"/>" class="lb-flower"><img src="/imageservlet/<c:out value="${image.ivid}"/>/1/<c:out value="${image.webFilename}"/>" alt="<c:out value="${image.versionTitle}"/>"></a>
                       </c:if>
                      </c:if>
                      <c:if test="${!inlinePreview}">
                        <a href="<c:out value="${imageUrl}"/>" onClick="var win = window.open('<c:out value="${imageUrl}"/>','001','scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=650,height=540');win.focus();return false;"><img src="/imageservlet/<c:out value="${image.ivid}"/>/1/<c:out value="${image.webFilename}"/>" alt="<c:out value="${image.versionTitle}"/>"></a>
                      </c:if>
                  </c:if>
                  <c:if test="${image.mayorMime=='video'}">

                        <!-- Audio/VIDEO -->
                    <c:if test="${false}">
                    <div id="mediaplayer<c:out value="${image.ivid}"/>">Audio/Videoplayer</div>

                    <script type="text/javascript" src="/jwplayer/jwplayer.js"></script>
                    <script type="text/javascript">
                        jwplayer("mediaplayer<c:out value="${image.ivid}"/>").setup({
                            flashplayer: "/jwplayer/player.swf",
                            width: '100%',
                            height: '100%',
                            controlbar: 'none',
                            stretching: 'uniform',
                            file: "<c:url value="/podcast/object/${image.ivid}.${image.extention}"/>",
                            image: "/imageservlet/<c:out value="${image.ivid}"/>/1/<c:out value="${image.webFilename}"/>"
                        });
                    </script>
                    </c:if>
                      <!-- SnapshotThumbnail von Video anzeigen -->
                      <c:if test="${inlinePreview}">
                        <a href="<c:out value="${imageUrl}"/>"><img src="/imageservlet/<c:out value="${image.ivid}"/>/1/<c:out value="${image.webFilename}"/>" alt="<c:out value="${image.versionTitle}"/>"></a>
                      </c:if>
                      <c:if test="${!inlinePreview}">
                        <a href="<c:out value="${imageUrl}"/>" onClick="var win = window.open('<c:out value="${imageUrl}"/>','001','scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=650,height=540');win.focus();return false;"><img src="/imageservlet/<c:out value="${image.ivid}"/>/1/<c:out value="${image.webFilename}"/>" alt="<c:out value="${image.versionTitle}"/>"></a>
                      </c:if>
                        <!-- ende Audio/Video -->

                  </c:if>
              </c:if>
              <c:if test="${image.mayorMime!='image'&&image.mayorMime!='video'&&image.mayorMime!='audio'&&image.mimeType!='application/pdf'}">
                <img src="/img/bl.gif" width="64" height="64" alt="<c:out value="${inboxImage.mimeType}"/>" name="<c:out value="${image.mimeType}"/>" class="<c:out value="${mimeCssMap[image.mimeType]}"/>64 mimeIcon64">
              </c:if>

        </div>

        <div class="imageCartDetail">
          <!-- DATA -->
          <!-- jsp:include page="listThumbnailDetailInfo.jsp" flush="true"/> -->

              <p class="imgTitle"><c:out value="${image.versionTitle}"/>&nbsp;</p>
              <c:if test="${showPrice}"><p class="imgPrice"><c:out value="${image.price}"/>&nbsp;<c:out value="${config.currency}"/></p></c:if>

        </div>

        <div class="imageCartSelector">

            <a href="<c:url value="?remove=${image.ivid}"/>" class="buttonDelete"><spring:message code="shoppingcart.delete"/></a>
            <!--
        <c:if test="${showSelect==true}">
            <mediadesk:imgselector ifNotSelected="${image.ivid}">
                <c:url var="selectUrl" value="${model.servletMapping}">
                  <c:param name="select" value="${image.ivid}"/>
                  <c:param name="id" value="${model.containerId}"/>
                  <c:param name="page" value="${model.pageIndex}"/>
                </c:url>
                <form class="selectImage" action="<c:out value="${selectUrl}"/>#imgIvId<c:out value="${image.ivid}"/>" method="GET">
                    <input id="cbxImage<c:out value="${image.ivid}"/>" type="checkbox"
                           onclick="javascript:requestUrl('<c:out value="${servletMapping}"/>','<c:out value="${image.ivid}"/>','<c:out value="${containerId}"/>','<c:out value="${pageIndex}"/>',this);"/>
                    <label for="cbxImage<c:out value="${image.ivid}"/>"><spring:message code="tm.labSelect"/></label>
                </form>
            </mediadesk:imgselector>
            <mediadesk:imgselector ifSelected="${image.ivid}">
                <c:url var="deselectUrl" value="${model.servletMapping}">
                  <c:param name="deselect" value="${image.ivid}"/>
                  <c:param name="id" value="${model.containerId}"/>
                  <c:param name="page" value="${model.pageIndex}"/>
                </c:url>
                <form class="selectImage" action="<c:out value="${deselectUrl}"/>#imgIvId<c:out value="${image.ivid}"/>" method="GET">
                    <input id="cbxImage<c:out value="${image.ivid}"/>" type="checkbox" checked="true"
                           onclick="javascript:requestUrl('<c:out value="${servletMapping}"/>','<c:out value="${image.ivid}"/>','<c:out value="${containerId}"/>','<c:out value="${pageIndex}"/>',this);"/>
                    <label for="cbxImage<c:out value="${image.ivid}"/>"><spring:message code="tm.labSelect"/></label>
                </form>
            </mediadesk:imgselector>
        </c:if>
        -->
        </div>

        </div>
      </div>