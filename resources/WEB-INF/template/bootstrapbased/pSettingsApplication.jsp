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
    <li class="active"><i class="fa fa-desktop"></i> <spring:message code="set.application"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.application"/></h3>
<h4><spring:message code="set.headline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->

    <form method="post" action="<c:url value="setapplication"/>" ng-controller="SettingsCtrl">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

        <div>

          <!-- Nav tabs -->
          <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Allgemein</a></li>
            <li role="presentation"><a href="#folder" aria-controls="iptc" role="tab" data-toggle="tab">Ordner</a></li>
            <li role="presentation"><a href="#download" aria-controls="messages" role="tab" data-toggle="tab">Download</a></li>
            <li role="presentation"><a href="#search" aria-controls="messages" role="tab" data-toggle="tab">Suche</a></li>
            <li role="presentation"><a href="#edit" aria-controls="messages" role="tab" data-toggle="tab">Beschlagwortung</a></li>
            <li role="presentation"><a href="#user" aria-controls="messages" role="tab" data-toggle="tab">Benutzer</a></li>
            <li role="presentation"><a href="#webservice" aria-controls="settings" role="tab" data-toggle="tab">Webservice</a></li>
          </ul>

          <!-- Tab panes -->
          <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="home">
                <!-- Allgemein Tab content -->

                <spring:bind path="command.wording">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.mediahandling"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.mediahandling.images"/></option>
                            <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.mediahandling.all"/></option>
                        </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.startPageInt">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.startpage"/> <jsp:include page="wikiLink.jsp?url=http:///en/Programm-Einstellungen#Startseite"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="set.application.splashpage"/></option>
                            <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.catview"/></option>
                            <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.searchform"/></option>
                            <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.loginpage"/></option>
                            <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.application.last"/> (nicht mehr verwenden!)</option>
                            <option value="5"<c:if test="${status.value==5}"> selected</c:if>>PIN-Freigaben</option>
                        </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.upstreamingStartpageUrl">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.application.upstreamStartpage"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.pinCodeKeyGen">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.pincodekeygen"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.pincodekeygen.4numbers"/></option>
                            <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.pincodekeygen.8numbers"/></option>
                            <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.pincodekeygen.4numletters"/></option>
                            <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.application.pincodekeygen.8numletters"/></option>
                        </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.robotsAllow">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.robotsallow"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                        </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.useCaptchaPin">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.usecaptchapin"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                            <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                            <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                        </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.configParam">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Konfigurationsparameter <jsp:include page="wikiLink.jsp?url=https://wiki.openmediadesk.net/de/Konfigurationsparameter"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">Werte für die Konfigurationsparameter sind im <a href="https://wiki.openmediadesk.net/de/Konfigurationsparameter" target="_blank">Wiki</a> zu finden.</p>
                </div>

            </div>

            <div role="tabpanel" class="tab-pane" id="folder">
                <!-- Ordner Tab content -->
                
                <spring:bind path="command.folderSort">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.category.sortby"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.category.sortbyname"/></option>
                                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.category.sortbytitle"/></option>
                                <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.folderOrder.createDate"/></option>
                                <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.application.folderOrder.folderDate"/></option>
                            </select>
                    </div>
                </spring:bind>

                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                        <spring:bind path="command.sortByFolder">
                    <div class="col-sm-6">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.sort.category"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.sort.latestDate"/></option>
                                <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="set.application.sort.latestImport"/></option>
                                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.sort.latestTitle"/></option>
                                <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.sort.latestPeople"/></option>
                                <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.application.sort.latestSite"/></option>
                                <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="set.application.sort.latestByline"/></option>
                            </select>
                    </div>
                        </spring:bind>
                        <spring:bind path="command.orderByFolder">
                    <div class="col-sm-6">
                    <label for="input<c:out value="${status.expression}"/>">&nbsp;</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.sort.asc"/></option>
                                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.sort.desc"/></option>
                            </select>
                    </div>
                        </spring:bind>
                    </div>

                <spring:bind path="command.folderLatestOnRoot">
                <div class="form-group">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="set.application.category.latestonroot"/>
                      </label>
                    </div>
                </div>
                </spring:bind>

                <spring:bind path="command.folderDefaultViewOnRoot">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.category.defaultviewroot"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="0"<c:if test="${status.value==0}"> selected</c:if>>auto</option>
                                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="categoryedit.thumbview"/></option>
                                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="categoryedit.listview"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.homeFolderId">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Mandanten/Benutzer ROOT-Kategorie</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="-1"<c:if test="${status.value==-1}"> selected</c:if>>nicht verwenden</option>
                                <c:forEach items="${rootFolderList}" var="cat">
                                    <option value="<c:out value="${cat.folderId}"/>"<c:if test="${status.value==cat.folderId}"> selected</c:if>><c:out value="${cat.title}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.homeFolderAutocreate">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Mandant/Benutzerkategorie automatisch erstellen</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.homeFolderAsRoot">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Nur Mandant/Benutzerkategorie anzeigen</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="download">
                <!-- Download Tab -->

                <spring:bind path="command.informDownloadAdmin">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.informdownloadadmin"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.quickDownload">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.qickdownload"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.showDownloadToVisitors">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.showlinktovisitors"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.downloadImageFilenameInt">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.imagefilename"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.import.name"/></option>
                                <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="set.import.number"/></option>
                                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.import.title"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.useLightbox">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.uselightbox"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.useDownloadResolutions">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.usedownloadres"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control"<c:if test="${!fn:contains(licFunc, '-thumbsize')}"> disabled="disabled"</c:if>>
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>

                <c:if test="${!fn:contains(licFunc, '-thumbsize')}">
                    <span class="help-block">Diese Funktion steht nur in der Enterprise Edition zur Verfügung!</span>
                    <a href="<c:url value="https://openmediadesk.net/signup/premium.do?id=${licId}&url=${url}"/>" target="_blank" class="btn btn-warning">Upgrade!</a>
                </c:if>

                    </div>
                </spring:bind>

                <spring:bind path="command.podcastEnabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.podcastenabled"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.streamEnabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.streamenabled"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.streamToVisitors">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.streamtovisitors"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.webdavEnabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Webdav Funktion aktivieren</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

            <h4>Warenkorb-Einstellungen</h4>

                <div class="form-group">
                    <p class="help-block">Mehr Informationen zu den Warenkorb-Einstellung und zum Kreditkarten-Bezahlmodul gibt es im Wiki unter <a href="https://wiki.openmediadesk.net/en/Programm-Einstellungen#Warenkorb-Einstellungen" target="_blank">http:///en/Programm-Einstellungen#Warenkorb-Einstellungen</a>. </p>
                </div>

                <spring:bind path="command.useShoppingCart">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.download.useshoppingcart"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="selUseShoppingCart" onchange="javascript:toggleCartEnable(this);" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.creditSystemEnabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.creditSystemEnabled"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.currency">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.currency"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="selCurrency" onchange="javascript:toggleCurrencyEnable(this);" class="form-control">
                                <option value=""<c:if test="${status.value==''}"> selected</c:if>><spring:message code="set.application.currency.credits"/></option>
                                <option value="EUR"<c:if test="${status.value=='EUR'}"> selected</c:if>>EUR</option>
                                <option value="USD"<c:if test="${status.value=='USD'}"> selected</c:if>>USD</option>
                                <option value="CHF"<c:if test="${status.value=='CHF'}"> selected</c:if>>CHF</option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.paymillKeyPublic">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Paymill API Public Key</label>
                    <input type="text" class="form-control input-sm" id="selPaymillKeyPublic" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.paymillKeyPrivate">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Paymill API Private Key</label>
                    <input type="text" class="form-control input-sm" id="selPaymillKeyPrivate" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">Weitere Details und Registrierung für das Bezahlmodul beim Anbieter <a href="http://www.paymill.com" target="_blank">http://www.paymill.com</a>. </p>
                </div>

                <spring:bind path="command.vatPercent">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">MwSt in %</label>
                    <input type="text" class="form-control input-sm" id="selVatPercent" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

            <script type="text/javascript">
                function toggleCurrencyEnable(obj) {

                    /*alert(obj.value);*/
                    if (obj.value=="") { //Credits = alles andere deaktivieren
                        document.getElementById("selPaymillKeyPublic").setAttribute("disabled","disabled");
                        document.getElementById("selPaymillKeyPrivate").setAttribute("disabled","disabled");
                        document.getElementById("selVatPercent").setAttribute("disabled","disabled");
                    } else {
                        document.getElementById("selPaymillKeyPublic").removeAttribute("disabled");
                        document.getElementById("selPaymillKeyPrivate").removeAttribute("disabled");
                        document.getElementById("selVatPercent").removeAttribute("disabled");
                    }

                };

                function toggleCartEnable(obj) {

                    if (obj.selectedIndex==1) { //Kein Warenkorb = alles deaktivieren, Waehrung auf credits
                        document.getElementById("selCurrency").selectedIndex = 0;
                        document.getElementById("selCurrency").setAttribute("disabled","disabled");
                    } else { //Warenkorb aktiv, alles aktivieren
                        document.getElementById("selCurrency").removeAttribute("disabled");
                    }
                    toggleCurrencyEnable(document.getElementById("selCurrency"));

                };

                toggleCartEnable(document.getElementById("selUseShoppingCart"));
            </script>

            </div>

            <div role="tabpanel" class="tab-pane" id="search">
                <!-- Suchen Tab -->

                <spring:bind path="command.searchAnd">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.searchAndSettings"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.application.searchAnd"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.application.searchOr"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.searchPerEmail">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.searchPerEmail"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="set.application.searchPerEmail.0"/></option>
                                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.application.searchPerEmail.1"/></option>
                                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.application.searchPerEmail.2"/></option>
                                <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.application.searchPerEmail.3"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.showFormOnEmptySearch">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.showFormOnEmptySearch"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="edit">
                <!-- Beschlagwortungs Tab -->

                <div class="form-group">
                    <p class="help-block"><spring:message code="set.application.editcopy.info"/></p>
                </div>

                <div class="form-group">
                    <spring:bind path="command.editCopyTitleLng1">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.title"/> [DE]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyTitleLng2">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.title"/> [EN]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopySubTitleLng1">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.subtitle"/> [DE]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopySubTitleLng2">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.subtitle"/> [EN]
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyInfoLng1">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.info"/> [DE]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyInfoLng2">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.info"/> [EN]
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                   <spring:bind path="command.editCopySiteLng1">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.site"/> [DE]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopySiteLng2">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.site"/> [EN]
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyPhotographDate">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.photographdate"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyPhotographer">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.photographer"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyByline">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.byline"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyKeywords">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.keywords"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyPeople">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.people"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyOrientation">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.orientation"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyPerspective">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.perspective"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyMotive">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.motive"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyGesture">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.gesture"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyNoteLng1">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.note"/> [DE]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyNoteLng2">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.note"/> [EN]
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyRestrictionsLng1">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.restrictions"/> [DE]
                      </label>
                    </div>
                    </div>
                    </spring:bind>
                    <spring:bind path="command.editCopyRestrictionsLng2">
                    <div class="col-sm-6">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.restrictions"/> [EN]
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyFlag">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.flag"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyPrice">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.price"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                    <spring:bind path="command.editCopyLicValid">
                    <div class="col-sm-12">
                    <label>&nbsp;</label>
                    <div class="checkbox">
                      <label>
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"<c:if test="${status.value==true}"> checked</c:if>> <spring:message code="imageedit.licvalid"/>
                      </label>
                    </div>
                    </div>
                    </spring:bind>

                </div>

            </div>

            <div role="tabpanel" class="tab-pane" id="user">
                <!-- Benutzer Tab -->

                <spring:bind path="command.onlyLoggedinUsers">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Nur angemeldete Benutzer d&uuml;rfen mediaDESK verwenden (privat)</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.allowRegister">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.allowregister"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.forbiddenDomains">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.application.user.forbiddendomains"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">Die Domains können mit ; getrennt werden. Z.B.: gmail;yahoo.com;gmx.net</p>
                </div>

                <spring:bind path="command.activateNewUsers">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.activate"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.informOfNewUsers">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.informnew"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.defaultCredits">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.application.user.defaultcredits"/></label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.defaultRole">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.defaultrole"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                              <option value="0"<c:if test="${status.value==0}"> selected</c:if>><spring:message code="useredit.role.user"/></option>
                              <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="useredit.role.importer"/></option>
                              <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="useredit.role.pinmakler"/></option>
                              <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="useredit.role.pineditor"/></option>
                              <option value="8"<c:if test="${status.value==8}"> selected</c:if>><spring:message code="useredit.role.editor"/></option>
                              <option value="9"<c:if test="${status.value==9}"> selected</c:if>><spring:message code="useredit.role.homeeditor"/></option>
                              <option value="10"<c:if test="${status.value==10}"> selected</c:if>><spring:message code="useredit.role.mastereditor"/></option>
                              <option value="99"<c:if test="${status.value==99}"> selected</c:if>><spring:message code="useredit.role.admin"/></option>
                            </select>
                    </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block"><jsp:include page="wikiLink.jsp?url=https://wiki.openmediadesk.net/de/Rollen"/> Eine detailierte Beschreibung der Rollen ist im Wiki zu finden <a href="https://wiki.openmediadesk.net/de/Rollen" target="_blank">https://wiki.openmediadesk.net/en/Rollen</a>.</p>
                </div>

                <spring:bind path="command.defaultSecurityGroup">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.defaultsecuritygroup"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <c:forEach items="${securityGroupList}" var="securityGroup">
                                <option value="<c:out value="${securityGroup.id}"/>"<c:if test="${status.value==securityGroup.id}"> selected</c:if>><c:out value="${securityGroup.name}"/></option>
                                </c:forEach>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.resetSecurityGroupWhenUserIsDisabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Berechtigungsgruppe zur&uuml;cksetzten, wenn Benutzer deaktiviert wird</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.passmailUser">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.passmailuser"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.passmailCopyAdmin">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.passmailadmin"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <c:if test="${!command.userEmailAsUsername}">
                <spring:bind path="command.userEmailAsUsername">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.emailasusername"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <div class="form-group">
                    <p class="help-block">Wichtige Informationen zum Ändern dieser Einstellung sind im Wiki zu finden <a href="https://wiki.openmediadesk.net/en/FAQ_Email_als_Benutzernamen_verwenden" target="_blank">https://wiki.openmediadesk.net/en/FAQ_Email_als_Benutzernamen_verwenden</a>.</p>
                </div>
                </c:if>

                <spring:bind path="command.showUserCompanyFields">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.showusercompanyfields"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.showUserAddressFields">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.showuseraddressfields"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.showUserTelFaxFields">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.application.user.showusertelfaxfields"/></label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

            </div>

            <div role="tabpanel" class="tab-pane" id="webservice">
                <!-- Webserivce Tab -->

                <div class="form-group">
                    <p class="help-block">Wenn Benutzer über ein Webservice authentifiziert werden sollen, geben Sie hier die Daten ein</p>
                </div>

                <spring:bind path="command.wsUsersyncEnabled">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Benutzer &uuml;ber Webservice Authentifizieren</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.wsUsersyncUrl">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Webservice URL:</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" placeholder="https://" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.wsUsersyncTrustAllCerts">
                    <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="input<c:out value="${status.expression}"/>">Alle (auch unsignierte) SSL-Zertifikate vertrauen</label>
                            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                                <option value="true"<c:if test="${status.value==true}"> selected</c:if>><spring:message code="set.yes"/></option>
                                <option value="false"<c:if test="${status.value==false}"> selected</c:if>><spring:message code="set.no"/></option>
                            </select>
                    </div>
                </spring:bind>

                <spring:bind path="command.wsUsersyncUsername">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Webservice Benutzer:</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.wsUsersyncPassword">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Webservice Passwort:</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

                <spring:bind path="command.wsUsersyncGroupnameFilter">
                <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
                    <label for="text<c:out value="${status.expression}"/>">Gruppenname Filter:</label>
                    <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" placeholder="http://" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
                    <c:if test="${status.error}">
                      <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                      <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
                    </c:if>
                </div>
                </spring:bind>

            </div>

          </div>

        </div>

    <button type="submit" class="btn btn-default"><spring:message code="imageedit.submit"/></button>

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