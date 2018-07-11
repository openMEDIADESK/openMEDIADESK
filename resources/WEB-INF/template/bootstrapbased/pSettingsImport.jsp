<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>
    <li class="active"><i class="fa fa-upload"></i> <spring:message code="set.import"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.import"/></h3>
<h4><spring:message code="set.import.headline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" action="<c:url value="setimport"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <div>

          <!-- Nav tabs -->
          <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Allgemein</a></li>
            <li role="presentation"><a href="#iptc" aria-controls="iptc" role="tab" data-toggle="tab">IPTC</a></li>
            <li role="presentation"><a href="#ftp" aria-controls="messages" role="tab" data-toggle="tab">FTP</a></li>
            <li role="presentation"><a href="#email" aria-controls="settings" role="tab" data-toggle="tab">E-Mail</a></li>
            <li role="presentation"><a href="#thumbnail" aria-controls="thumbnails" role="tab" data-toggle="tab">Thumbnails</a></li>
          </ul>

          <!-- Tab panes -->
          <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="home">
                <!-- Allgemein Tab content -->
                <spring:bind path="command.importImageNumberSerially">
                <div class="form-group">
                    <label>&nbsp; <!--<spring:message code="set.import.imagenumberserially"/>--></label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="importImageNumberSerially" id="cbxImageNumberSerially" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="set.import.imagenumberserially"/>
                      </label>
                    </div>
                </div>
                </spring:bind>

                <spring:bind path="command.fileEncoding">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.fileEncoding"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                              <option value=""<c:if test="${status.value==''}"> selected</c:if>><spring:message code="set.import.fileEncoding.default"/></option>
                              <option value="Cp1252"<c:if test="${status.value=='Cp1252'}"> selected</c:if>>Cp1252</option>
                              <option value="UTF-8"<c:if test="${status.value=='UTF-8'}"> selected</c:if>>UTF-8</option>
                              <option value="MacRoman"<c:if test="${status.value=='MacRoman'}"> selected</c:if>>MacRoman</option>
                        </select>
                    </div>
                </spring:bind>
            </div>

            <div role="tabpanel" class="tab-pane" id="iptc">
                <!-- IPTC Tab content -->
                <spring:bind path="command.importName">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.name"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importTitle">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.title"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importSubtitle">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="mediaedit.subtitle"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importInfo">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="mediaedit.info"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importNumber">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.number"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importSite">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.site"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importPhotographerAlias">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.photographer"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importByline">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.byline"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importKeywords">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="mediaedit.keywords"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importPeople">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.import.people"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importNote">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="mediaedit.note"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importRestrictions">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="mediaedit.restrictions"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.importDate">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="mediaedit.photographdate"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${metadataHeaders}" var="metadata">
                                    <option value="<c:out value="${metadata}"/>"<c:if test="${status.value==metadata}"> selected</c:if>><c:out value="${metadata}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <div class="form-group">            
                    <p class="help-block"><spring:message code="set.import.help"/></p>
                </div>

            </div>

            <div role="tabpanel" class="tab-pane" id="ftp">
                <!-- FTP Tab -->
                <spring:bind path="command.autoImportFtp">
                <div class="form-group">
                    <label>&nbsp;<!--Automatischer Import via FTP einschalten--></label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="autoImportFtp" id="cbxAutoImportFtp" value="true"<c:if test="${status.value==true}"> checked</c:if>> Import via FTP einschalten
                      </label>
                    </div>
                </div>
                </spring:bind>

                <spring:bind path="command.ftpHost">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">FTP-Server</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.ftpUser">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">FTP-Benutzer</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.ftpPassword">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Passwort</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.autoImportFtpCat">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Dateien am FTP automatisch importieren in</label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="0"<c:if test="${status.value==0}"> selected="true"</c:if>>---</option>
                            <c:forEach items="${folderList}" var="folder" varStatus="stat">
                            <option value="<c:out value="${folder.folderId}"/>"<c:if test="${status.value==folder.folderId}"> selected="true"</c:if>><c:out value="${folder.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="email">
                <!-- E-Mail Tab -->

                <spring:bind path="command.emailImportEnabled">
                <div class="form-group">
                    <label>&nbsp;<!--Import via Email einschalten--></label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="emailImportEnabled" id="cbxEmailImportEnabled" value="true"<c:if test="${status.value==true}"> checked</c:if>> Import via Email einschalten
                      </label>
                    </div>
                </div>
                </spring:bind>

                <spring:bind path="command.emailImportHost">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">POP3-Server</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.emailImportUsername">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Benutzername</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.emailImportPassword">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Passwort</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.emailImportPasswordAgain">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Passwort</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.autoImportEmailFolder">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Dateien im Postfach automatisch importieren in Ordner</label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="0"<c:if test="${status.value==0}"> selected="true"</c:if>>---</option>
                            <c:forEach items="${folderList}" var="folder" varStatus="stat">
                            <option value="<c:out value="${folder.folderId}"/>"<c:if test="${status.value==folder.folderId}"> selected="true"</c:if>><c:out value="${folder.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="thumbnail">

                <!-- Thumbnail und Vorschaugröße -->

                <c:if test="${!fn:contains(licFunc, '-thumbsize')}">
                    <span class="help-block">Diese Funktion steht nur in der Enterprise Edition zur Verfügung!</span>
                    <a href="<c:url value="https://openmediadesk.net/signup/premium.do?id=${licId}&url=${url}"/>" target="_blank" class="btn btn-warning">Upgrade!</a>
                </c:if>

                <spring:bind path="command.imagesizeThumbnail">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Thumbnailgröße px</label>
                    <input type="number" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if><c:if test="${!fn:contains(licFunc, '-thumbsize')}"> readonly="true"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.imagesizePreview">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Vorschaubild px</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if><c:if test="${!fn:contains(licFunc, '-thumbsize')}"> readonly="true"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

            </div>

          </div>

        </div>

    <button type="submit" class="btn btn-default"><spring:message code="mediaedit.submit"/></button>

    </form>

    <spring:hasBindErrors name="command">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <spring:bind path="command">
                <div class="formErrorSummary">
                <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br>
                </c:forEach>
                </div>
            </spring:bind>
    </div>
    </spring:hasBindErrors>

    <!-- /FORMS FÜR EDIT -->
	</div>
</div>      <!-- /row -->
<!-- ENDE EDIT BILD -->
<!-- ###################################################################################################################################################### -->



<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>