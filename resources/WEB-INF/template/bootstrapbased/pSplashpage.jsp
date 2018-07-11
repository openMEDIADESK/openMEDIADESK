<%@ page import="java.util.Enumeration,com.stumpner.mediadesk.usermanagement.User,com.stumpner.mediadesk.core.Config,org.apache.log4j.Logger"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %><%@ taglib uri="/mediadesk" prefix="mediadesk" %><%@page contentType="text/html;charset=utf-8"%><%  response.setHeader("Pragma", "no-cache"); %><%  response.setHeader("Cache-Control", "no-cache"); %><%  response.setHeader("Cache-Control","no-store" ); %><%  response.setDateHeader("Expires", 0); %><!DOCTYPE html>
<html lang="de" ng-app="ui.mediadesk">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Die 3 Meta-Tags oben *müssen* zuerst im head stehen; jeglicher sonstiger head-Inhalt muss *nach* diesen Tags kommen -->
    <meta name="keywords" content="<%= Config.webKeywords %>">
    <meta name="description" content="<%= Config.webDescription %>">
    <link rel="icon" href="/favicon.ico"><!-- TODO -->
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <!-- bei buttons die nur icons enthalten noch: <span class="sr-only">Text</span> einfügen als hilfe -->
    <title><c:if test="${webSiteTitle==''}"><c:out value="${config.webTitle}"/></c:if><c:out value="${webSiteTitle}"/></title>
    <!-- Bootstrap-CSS -->
    <!-- !!ACHTUNG: verwende hier .min.css da das andere file mit padding im body arbeitet! -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <!-- auskommentiert weil es sonst auf der splash page oben einen abstand gibt!? <link href="/css/normalize.css" rel="stylesheet"> -->
    <!-- ngDialog CSS http://likeastore.github.io/ngDialog/ -->
    <link rel="stylesheet" href="/css/ngDialog.css">
    <link rel="stylesheet" href="/css/ngDialog-theme-plain.css">
    <!-- Chart -->
    <link rel="stylesheet" href="/app/lib/angular/angular-chart.css">
    <!-- angular JS Toaster https://github.com/jirikavi/AngularJS-Toaster -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/angularjs-toaster/1.1.0/toaster.min.css" rel="stylesheet" />
    <!-- ng-wig WYSIWYG Editor https://github.com/stevermeister/ngWig -->
    <link href="/app/lib/ng-wig/css/ng-wig.min.css" rel="stylesheet"/>
    <!-- warum? nur splashpage?? -->
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
<style type="text/css">
<%= Config.cssAdd %>
</style>
<%= Config.googleWebmasters %>
</head>

<body ng-controller="MainCtrl" ng-init="properties={lng:'<c:out value="${lng}"/>',loggedin:<mediadesk:login>true</mediadesk:login><mediadesk:login notLoggedIn="true">false</mediadesk:login>,username:'<c:out value="${loggedInUser.userName}"/>',role:<c:out value="${loggedInUser.role}"/>,cartCount:<c:out value="${shoppingCartCount}"/>,favCount:<c:out value="${lightboxCount}"/>}" ng-cloak>

<!-- //bis hier teile aus header.jsp nehmen! -->

<style type="text/css">

body,
html {
    width: 100%;
    height: 100%;
}

body,
h1,
h2,
h3,
h4,
h5,
h6 {
    font-family: "Lato","Helvetica Neue",Helvetica,Arial,sans-serif;
    font-weight: 700;
}

.topnav {
    font-size: 14px;
}

.lead {
    font-size: 18px;
    font-weight: 400;
}

.intro-header {
    padding-top: 50px; /* 50px of padding to make sure the navbar doesn't overlap content! */
    padding-bottom: 50px;
    text-align: center;
    color: #f8f8f8;
    //background: url(/img/intro-bg.jpg) no-repeat center center;
    background: url(/splashpageimageservlet/<c:out value="${applicationScope.splashPageValueMap['introivid']}"/>/0/image.jsp) no-repeat center center;
    background-size: cover;
}

.intro-message {
    position: relative;
    padding-top: 20%;
    padding-bottom: 20%;
}

.intro-message > h1 {
    margin: 0;
    text-shadow: 2px 2px 3px rgba(0,0,0,0.6);
    font-size: 5em;
}

.intro-divider {
    width: 400px;
    border-top: 1px solid #f8f8f8;
    border-bottom: 1px solid rgba(0,0,0,0.2);
}

.intro-message > h3 {
    text-shadow: 2px 2px 3px rgba(0,0,0,0.6);
}

@media(max-width:767px) {
    .intro-message {
        padding-bottom: 15%;
    }

    .intro-message > h1 {
        font-size: 3em;
    }

    ul.intro-social-buttons > li {
        display: block;
        margin-bottom: 20px;
        padding: 0;
    }

    ul.intro-social-buttons > li:last-child {
        margin-bottom: 0;
    }

    .intro-divider {
        width: 100%;
    }
}

.network-name {
    text-transform: uppercase;
    font-size: 14px;
    font-weight: 400;
    letter-spacing: 2px;
}

.content-section-a {
    padding: 50px 0;
    background-color: #f8f8f8;
}

.content-section-b {
    padding: 50px 0;
    border-top: 1px solid #e7e7e7;
    border-bottom: 1px solid #e7e7e7;
}

.section-heading {
    margin-bottom: 30px;
}

.section-heading-spacer {
    float: left;
    width: 200px;
    border-top: 3px solid #e7e7e7;
}

.banner {
    padding: 100px 0;
    color: #f8f8f8;
    //background: url(/img/banner-bg.jpg) no-repeat center center;
    background: url(/splashpageimageservlet/<c:out value="${applicationScope.splashPageValueMap['bannerivid']}"/>/0/image.jsp) no-repeat center center;
    background-size: cover;
}

.banner h2 {
    margin: 0;
    text-shadow: 2px 2px 3px rgba(0,0,0,0.6);
    font-size: 3em;
}

.banner ul {
    margin-bottom: 0;
}

.banner-social-buttons {
    float: right;
    margin-top: 0;
}

@media(max-width:1199px) {
    ul.banner-social-buttons {
        float: left;
        margin-top: 15px;
    }
}

@media(max-width:767px) {
    .banner h2 {
        margin: 0;
        text-shadow: 2px 2px 3px rgba(0,0,0,0.6);
        font-size: 3em;
    }

    ul.banner-social-buttons > li {
        display: block;
        margin-bottom: 20px;
        padding: 0;
    }

    ul.banner-social-buttons > li:last-child {
        margin-bottom: 0;
    }
}

footer {
    padding: 50px 0;
    background-color: #f8f8f8;
}

p.copyright {
    margin: 15px 0 0;
}

</style>


    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-fixed-top topnav" role="navigation">
        <div class="container topnav">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand topnav" href="#"><img src="/logo2.png" alt="MEDIADESK" title="MEDIADESK" border="0"/></a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                <mediadesk:login role="<%= User.ROLE_ADMIN %>">
                    <li><a href="#" ng-click="openOnePagerEditPopup('/<c:out value="${lng}"/>/splashpageedit')"><i class="fa fa-cog fa-lg fa-fw"></i></a></li>
                </mediadesk:login>
                <!-- mediadesk:login notLoggedIn="true"> -->
                <c:forEach items="${navLinks}" var="menue">
                    <c:url value="${menue.linkUrl}" var="linkUrl">
                        <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
                    </c:url>
                    <c:if test="${menue.linkUrl!='/pin' && menue.openAs!=2}">
                    <li><a href="<c:out value="${linkUrl}"/>"><c:out value="${menue.title}"/></a></li>
                    </c:if>
                </c:forEach>
                <mediadesk:login notLoggedIn="true">
                    <li><a href="<c:url value="/${lng}/login"/>">Login</a></li>
                    <% if (Config.allowRegister) { %>
                    <li><a href="<c:url value="/${lng}/login"/>">Registrieren</a></li>
                    <% } %>
                </mediadesk:login>
                <!-- /mediadesk:login -->
                    <li>
                        <a href="#punkt1">Punkt 1</a>
                    </li>
                    <li>
                        <a href="#punkt2">Punkt 2</a>
                    </li>
                    <li>
                        <a href="#punkt3">Puntk 3 / Contact</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>


    <!-- Header -->
    <div class="intro-header">
        <div class="container">

            <div class="row">
                <div class="col-lg-12">
                    <div class="intro-message">
<!-- SUCHFORM -->
    <div class="row">
        <div class="col-lg-3">&nbsp;</div>
        <div class="col-lg-6">
            <form role="search" method="post" action="<c:url value="/${lng}/search"/>">
                <div class="input-group">
                	<!-- evtl. 1. span entfallen lassen, erweiterte suche benötigt man nicht unbedingt hier -->
                    <span class="input-group-btn">
                    <button class="btn btn-default input-lg" type="button"><i class="fa fa-cogs fa-fw"></i></button>
                    </span>
                        <input type="text" class="form-control input-lg" name="q" id="sTextKeyword" placeholder="<spring:message code="mediasearch.imagesearch"/>">
                    <span class="input-group-btn">
                    <button class="btn btn-default input-lg" type="submit"><i class="fa fa-search fa-fw"></i></button>
                    </span>
                </div>
            </form>
        </div>
        <div class="col-lg-3">&nbsp;</div>
    </div><!-- /row -->
<!-- /SUCHFORM -->
		<!-- <h1>HALLO WELT</h1> -->
    	<h3><c:out value="${applicationScope.splashPageValueMap['welcometext']}"/></h3>
                        <hr class="intro-divider">
                        <ul class="list-inline intro-social-buttons">
                            <li>
                                <a href="<c:out value="${applicationScope.splashPageValueMap['instagramUrl']}"/>" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-instagram fa-fw"></i> <span class="network-name">Instagram</span></a>
                            </li>
                            <li>
                                <a href="<c:out value="${applicationScope.splashPageValueMap['twitterUrl']}"/>" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-twitter fa-fw"></i> <span class="network-name">Twitter</span></a>
                            </li>
                            <li>
                                <a href="<c:out value="${applicationScope.splashPageValueMap['facebookUrl']}"/>" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-facebook fa-fw"></i> <span class="network-name">Facebook</span></a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

        </div>
        <!-- /.container -->

    </div>
    <!-- /.intro-header -->

    <!-- Page Content -->

	<a  name="punkt2"></a>
    <div class="content-section-a">

        <div class="container">
            <div class="row">
                <div class="col-lg-5 col-sm-6">
                    <hr class="section-heading-spacer">
                    <div class="clearfix"></div>
                    <h2 class="section-heading"><c:out value="${applicationScope.splashPageValueMap['section1headline']}"/></h2>
                    <p class="lead"><c:out value="${applicationScope.splashPageValueMap['section1text']}"/></p>
                </div>
                <!-- Vorlage für Kreis: Bild muss ein Quadrat sein -->
                <div class="col-lg-5 col-lg-offset-2 col-sm-6">
                    <img class="img-responsive img-circle" src="/splashpageimageservlet/<c:out value="${applicationScope.splashPageValueMap['sercion1ivid']}"/>/0/image.jsp" alt="">
                </div>
                <!-- Vorlage für dynamisches Bild via ivid
                <div class="col-lg-5 col-lg-offset-2 col-sm-6">
                    <img class="img-responsive img-circle" src="/splashpageimageservlet/<c:out value="${applicationScope.splashPageValueMap['sercion1ivid']}"/>/0/image.jsp" alt="">
                </div>
                -->
            </div>

        </div>
        <!-- /.container -->

    </div>
    <!-- /.content-section-a -->

    <div class="content-section-b">

        <div class="container">

            <div class="row">
                <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
                    <hr class="section-heading-spacer">
                    <div class="clearfix"></div>
                    <h2 class="section-heading"><c:out value="${applicationScope.splashPageValueMap['section2headline']}"/></h2>
                    <p class="lead"><c:out value="${applicationScope.splashPageValueMap['section2text']}"/></p>
                </div>
                <div class="col-lg-5 col-sm-pull-6  col-sm-6">
                    <img class="img-responsive" src="/splashpageimageservlet/<c:out value="${applicationScope.splashPageValueMap['section2ivid']}"/>/0/image.jsp" alt="">
                </div>
            </div>

        </div>
        <!-- /.container -->

    </div>
    <!-- /.content-section-b -->

    <div class="content-section-a">

        <div class="container">

            <div class="row">
                <div class="col-lg-5 col-sm-6">
                    <hr class="section-heading-spacer">
                    <div class="clearfix"></div>
                    <h2 class="section-heading"><c:out value="${applicationScope.splashPageValueMap['section3headline']}"/></h2>
                    <p class="lead"><c:out value="${applicationScope.splashPageValueMap['section3text']}"/></p>
                </div>
                <div class="col-lg-5 col-lg-offset-2 col-sm-6">
                    <img class="img-responsive img-circle" src="/splashpageimageservlet/<c:out value="${applicationScope.splashPageValueMap['sercion3ivid']}"/>/0/image.jsp" alt="">
                </div>
            </div>

        </div>
        <!-- /.container -->

    </div>
    <!-- /.content-section-a -->

	<a  name="punkt3"></a>
    <div class="banner">

        <div class="container">

            <div class="row">
                <div class="col-lg-6">
                    <h2>Punkt 3 / Contact / Social</h2>
                </div>
                <div class="col-lg-6">
                    <ul class="list-inline banner-social-buttons">

                            <li>
                                <a href="<c:out value="${applicationScope.splashPageValueMap['instagramUrl']}"/>" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-instagram fa-fw"></i> <span class="network-name">Instagram</span></a>
                            </li>
                            <li>
                                <a href="<c:out value="${applicationScope.splashPageValueMap['twitterUrl']}"/>" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-twitter fa-fw"></i> <span class="network-name">Twitter</span></a>
                            </li>
                            <li>
                                <a href="<c:out value="${applicationScope.splashPageValueMap['facebookUrl']}"/>" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-facebook fa-fw"></i> <span class="network-name">Facebook</span></a>
                            </li>
                    </ul>
                </div>
            </div>

        </div>
        <!-- /.container -->

    </div>
    <!-- /.banner -->

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <ul class="list-inline">

                        <c:forEach items="${navLinks}" var="menue">
                            <c:url value="${menue.linkUrl}" var="linkUrl">
                                <c:param name="${menue.linkParam}" value="${menue.linkValue}"/>
                            </c:url>
                            <c:if test="${menue.linkUrl!='/pin' && menue.openAs!=2}">
                            <li><a href="<c:out value="${linkUrl}"/>"><c:out value="${menue.title}"/></a></li>
                            </c:if>
                        </c:forEach>
                        <mediadesk:login notLoggedIn="true">
                            <li><a href="<c:url value="/${lng}/login"/>">Login</a></li>
                            <% if (Config.allowRegister) { %>
                            <li><a href="<c:url value="/${lng}/login"/>">Registrieren</a></li>
                            <% } %>
                        </mediadesk:login>
                        <li>
                            <a href="#">Home</a>
                        </li>
                        <li class="footer-menu-divider">&sdot;</li>
                        <li>
                            <a href="#punkt1">Punkt 1</a>
                        </li>
                        <li class="footer-menu-divider">&sdot;</li>
                        <li>
                            <a href="#punkt2">Punkt 2</a>
                        </li>
                        <li class="footer-menu-divider">&sdot;</li>
                        <li>
                            <a href="#punkt3">Punkt 3 / Contact</a>
                        </li>
                    </ul>
                    <p class="copyright text-muted small">Copyright &copy; mediadesk 2015. All Rights Reserved</p>
                </div>
            </div>
        </div>
    </footer>

<!-- ab hier eventuell aus footer.jsp nehmen!!! -->
<!-- Bootstrap-JavaScript -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- salvattore-JavaScript -->
<script src="/js/salvattore.js"></script>
<!-- AngularJS 1.4.7  vormals 1.4.x Minified (IT/Sf) -->
<!-- <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.min.js"></script> -->
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-animate.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-sanitize.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.3/angular-touch.js"></script>
<!-- Bootstrap components written in pure AngularJS by the AngularUI Team https://angular-ui.github.io/bootstrap/ -->
<script src="//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-1.3.2.js"></script>
<!-- AngularJS Modul ng-Dialog -->
<script type="text/javascript" src="/app/lib/angular/ngDialog.js"></script>
<!-- flow.js + ng-flow libraries https://github.com/flowjs/ng-flow -->
<script type="text/javascript" src="/app/lib/angular/ng-flow-standalone.min.js"></script>
<!-- http://www.chartjs.org/docs/ -->
<script src="/app/lib/angular/Chart.js"></script>
<!-- http://jtblin.github.io/angular-chart.js/ -->
<script src="/app/lib/angular/angular-chart.js"></script>
<!-- videogular http://www.videogular.com/tutorials/how-to-start/ -->
<script src="/app/lib/videogular/videogular.js"></script>
<script src="/app/lib/videogular-controls/vg-controls.js"></script>
<script src="/app/lib/videogular-overlay-play/vg-overlay-play.js"></script>
<script src="/app/lib/videogular-poster/vg-poster.js"></script>
<script src="/app/lib/videogular-buffering/vg-buffering.js"></script>
<!-- angular JS Toaster https://github.com/jirikavi/AngularJS-Toaster -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/angularjs-toaster/1.1.0/toaster.min.js"></script>
<!-- ng-timago http://ngmodules.org/modules/ngtimeago -->
<script src="/app/lib/ng-timeago/ngtimeago.js"></script>
<!-- mediaDESK AngularJS App -->
<script type="text/javascript" src="/app/scripts/mediadeskangular.js"></script>
<!-- ng-wig WYSIWYG Editor https://github.com/stevermeister/ngWig -->
<script type="text/javascript" src="/app/lib/ng-wig/ng-wig.min.js"></script>
<!-- drag&Drop http://marceljuenemann.github.io/angular-drag-and-drop-lists -->
<!--
<script type="text/javascript" src="/app/lib/angular-drag-and-drop-lists/angular-drag-and-drop-lists.min.js"></script>
-->
<!-- drag&Drop http://angular-dragdrop.github.io/angular-dragdrop/ -->
<script type="text/javascript" src="/app/lib/angular-dragdrop/draganddropfork.js"></script>
<!-- Masonry -->
<!--
<script src="https://cdnjs.cloudflare.com/ajax/libs/masonry/3.3.2/masonry.pkgd.min.js"></script>
-->
<!-- Angular-masonry-directive -->
<!--
<script src="/js/angular-masonry-directive.js"></script>
-->

<script type="text/javascript">



</script>

<!--
mediaDESK Version: <%= Config.versionNumbner %>
mediaDESK VerDate: <%= Config.versionDate %>
-->

<%= Config.statCounterCode %>
<%= Config.googleAnalytics %>

</body>
</html>
