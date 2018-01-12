<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><%= Config.webTitle %></title>
<script	type="text/javascript" src="/main.js" language="Javascript"></script>
<link rel="stylesheet" href="/auge.css" type="text/css">
<meta http-equiv="imagetoolbar" content="no">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"> <!--onContextMenu="return false;">-->
<table width="400" border="0" cellspacing="0" cellpadding="0">
<tr align="left" valign="top">
<td width="20"><img src="/images/bl.gif" width="20" height="10"></td>
<td width="157"><img src="/images/bl.gif" width="1" height="10"></td>
<td><img src="images/bl.gif" width="1" height="10"></td>
</tr>
<!--
<tr align="left" valign="top">
<td>&nbsp;</td>
<td>&nbsp;</td>
<td align="right" valign="middle" class="PopupGrau10"><a href="/index/image?id=<c:out value="${model.image.ivid}"/>&shoppingCart=add" class="TopSeiteLink">Add to Cart</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href="/index/image?id=<c:out value="${model.image.ivid}"/>&lightbox=add" class="TopSeiteLink">Add to Lightbox</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href="javascript:window.print();" class="TopSeiteLink">print
window</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href="javascript:window.close();" class="TopSeiteLink">close
window</a></td>
</tr>
-->
<tr align="left" valign="top">
<td><img src="/images/bl.gif" width="20" height="15"></td>
<td><img src="/images/bl.gif" width="1" height="15"></td>
<td><img src="/images/bl.gif" width="1" height="15"></td>
</tr>
</table>
<table width="310" border="0" cellspacing="0" cellpadding="0">
<tr align="left" valign="top">
<td width="20"><img src="/images/bl.gif" width="20" height="1"></td>
<td><table width="290" border="0" cellspacing="0" cellpadding="0">
<tr align="left" valign="top">
<c:forEach items="${metadataList}" var="metadata">
<td width="100" class="PopupGrauBold10"><c:out value="${metadata.metaKey}"/></td>
          <td class="PopupGrau10"><c:out value="${metadata.metaValue}"/></td>
</tr>
</c:forEach>
<tr align="left" valign="top">
<td><img src="/images/bl.gif" width="1" height="1"></td>
<td>&nbsp;</td>
</tr>
</table>
</body>
</html>
