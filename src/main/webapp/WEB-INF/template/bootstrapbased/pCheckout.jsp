<%@ page import="com.stumpner.mediadesk.usermanagement.User,
                 com.stumpner.mediadesk.core.Config"%>
<%@ page import="com.stumpner.mediadesk.media.image.util.CustomTextService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="#"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li><a href="shop"><i class="fa fa-shopping-cart fa-fw"></i> <spring:message code="shoppingcart.headline"/></a></li>
    <li class="active"><i class="fa fa-credit-card fa-fw"></i> <spring:message code="download.checkout"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="download.checkout"/> <!--<small>KREDITKARTENBEZAHLUNG</small>--></h3>
<h4><spring:message code="download.checkoutsub"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- hier war die leiste für optionen -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- WARENKORB CHECKOUT ################################################################################################################################## -->
<!-- warenkorb CHECKOUT -->
<div class="row"><!-- row -->

<!-- spalte 1 mit form -->
<!-- KREDITKARTENFORM -->
<div class="col-xs-12 col-md-4">
<!-- paymill integration -->
<script type="text/javascript">

  var PAYMILL_PUBLIC_KEY = '<c:out value="${paymillKeyPublic}"/>';

</script>

<script type="text/javascript" src="https://bridge.paymill.com/"></script>
<script type="text/javascript" src="https://static.paymill.com/assets/js/jquery/jquery-1.7.2.min.js"></script>

<!-- Paymill integration -->
<script type="text/javascript">

$(document).ready(function() {

  $("#payment-form").submit(function(event) {
    // Deactivate submit button to avoid further clicks

    $('.submit-button').attr("disabled", "disabled");


    paymill.createToken({
      number: $('.card-number').val(),  // required, ohne Leerzeichen und Bindestriche
      exp_month: $('.card-expiry-month').val(),   // required
      exp_year: $('.card-expiry-year').val(),     // required, vierstellig z.B. "2016"
      cvc: $('.card-cvc').val(),                  // required
      amount_int: $('.card-amount-int').val(),    // required, integer, z.B. "15" für 0,15 Euro
      currency: $('.card-currency').val(),    // required, ISO 4217 z.B. "EUR" od. "GBP"
      cardholder: $('.card-holdername').val() // optional
    }, PaymillResponseHandler);                   // Info dazu weiter unten

    return false;
  });
});

function PaymillResponseHandler(error, result) {
    //debug: alert('callback'+result);
  if (error) {
    // Shows the error above the form
      location.href="<c:out value="?payapierror="/>"+error.apierror;
  } else {
    var form = $("#payment-form");
    // Output token
    var token = result.token;
    // Insert token into form in order to submit to server
      //alert('token: '+token);
      location.href="?token="+token;
  }
}

</script>
<!--
    <h4>HEADLINE?</h4>
    <form id="payment-form" action="#" method="POST">
    <div class="form-group">
    <label for="Titel">form da her nach vorlage</label>
    <input type="email" class="form-control input-sm" id="Leute" placeholder="">
    </div>
    <button type="button" class="btn btn-success"><i class="fa fa-credit-card"></i>&nbsp;&nbsp;ZUR KASSA&nbsp;&nbsp;<i class="fa fa-credit-card"></i></button>
    <button type="button" class="btn btn-link">Doch ned ...</button>
    </form> -->

            <!-- CREDIT CARD FORM STARTS HERE -->
            <div class="panel panel-default credit-card-box">
                <div class="panel-heading display-table" >
                    <div class="row display-tr" >
                        <h3 class="panel-title display-td" >&nbsp;<!-- Zahlungsdetails --></h3>
                        <div class="display-td" >
                            <img class="img-responsive pull-right" src="/img/creditcards.png">
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <form id="payment-form" action="#" method="POST" role="form">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="form-group">
                                    <label for="card-number">Karten Nr.:</label>
                                    <div class="input-group">
                                        <input
                                            type="tel"
                                            class="form-control card-number"
                                            id="card-number"
                                            size="20"
                                            autocomplete="cc-number"
                                            required autofocus
                                        />
                                        <span class="input-group-addon"><i class="fa fa-credit-card"></i></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-7 col-md-7">

                                <div class="form-group">
                                    <label for="card-expiry-month"><span class="hidden-xs">Gültig bis:</span><span class="visible-xs-inline">Gültig:</span></label>

                                    <div class="row">
                                        <div class="col-xs-4">
                                            <input
                                                type="tel"
                                                class="form-control card-expiry-month"
                                                size="2"
                                                id="card-expiry-month"
                                                placeholder="MM"
                                                autocomplete="cc-exp"
                                                required
                                            />
                                        </div>
                                        <div class="col-xs-8">
                                            <input
                                                type="tel"
                                                class="form-control card-expiry-year"
                                                size="4"
                                                placeholder="YYYY"
                                                autocomplete="cc-exp"
                                                required
                                            />
                                        </div>
                                    </div>

                                </div>
                            </div>
                            <div class="col-xs-5 col-md-5 pull-right">
                                <div class="form-group">
                                    <label for="card-cvc">CVC*:</label>
                                    <input
                                        type="tel"
                                        class="form-control card-cvc"
                                        id="card-cvc"
                                        placeholder="CVC"
                                        autocomplete="cc-csc"
                                        required
                                    />
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="form-group">
                                    <label for="card-holdername">Karten Inhaber:</label>
                                    <input type="text" class="form-control card-holdername" size="14" id="card-holdername"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <button class="subscribe btn btn-success btn-lg btn-block submit-button" type="submit"><i class="fa fa-credit-card"></i>&nbsp;bezahlen&nbsp;<i class="fa fa-credit-card"></i></button>
                            </div>
                        </div>
                        <c:if test="${error}">
                        <div class="row">
                            <div class="col-xs-12">
                                <p class="payment-errors"><c:out value="${errormessage}"/></p>
                            </div>
                        </div>
                        </c:if>
                    </form>
                </div>
            </div>
            <!-- CREDIT CARD FORM ENDS HERE -->
</div>
<!-- /KREDITKARTENFORM -->


<!-- spalte 2 mit liste -->
<div class="col-xs-12 col-md-8">

<!-- receipt example -->
    <!-- Ausbaustufe 2
            <div class="row">
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <address>
                        <strong>xxx</strong>
                        <br>
                        xxx Sunset Blvd
                        <br>
                        Linz, CA 90026
                        <br>
                        <abbr title="Phone">P:</abbr> (xx) xxx
                    </address>
                </div>
                <div class="col-xs-6 col-sm-6 col-md-6 text-right">
                    <p>
                        <em>Date:</em>
                    </p>
                    <p>
                        <em>Receipt #: xxx234235</em>
                    </p>
                </div>
            </div>
            -->
            <div class="row">
                <div class="text-center">
                    <h1>Zahlungsbeleg</h1>
                </div>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Datei/Bild</th>
                            <th>&nbsp;</th>
                            <th class="text-center">&nbsp;</th>
                            <th class="text-center">Preis</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${mediaObjectList}" var="mediaObject" varStatus="moStatus">
                        <tr>
                            <td class="col-md-9"><em><c:out value="${mediaObject.versionName}"/></em></td>
                            <td class="col-md-1" style="text-align: center"> &nbsp; </td>
                            <td class="col-md-1 text-center">&nbsp;</td>
                            <td class="col-md-1 text-center"><fmt:formatNumber value="${mediaObject.price}" type="number" pattern="#.##"/> <c:out value="${currency}"/></td>
                        </tr>
                        </c:forEach>
                        <!--
                        <tr>
                            <td class="col-md-9"><em>Lebanese Cabbage Salad</em></td>
                            <td class="col-md-1" style="text-align: center"> 1 </td>
                            <td class="col-md-1 text-center">$8</td>
                            <td class="col-md-1 text-center">$8</td>
                        </tr>
                        -->
                        <c:if test="${deposit>0}">
                        <tr>
                            <td class="col-md-9"><em>Summe</em></td>
                            <td class="col-md-1" style="text-align: center"> &nbsp; </td>
                            <td class="col-md-1 text-center">&nbsp;</td>
                            <td class="col-md-1 text-center"><fmt:formatNumber value="${subtotalbeforedeposit}" type="number" pattern="#.##"/> <c:out value="${currency}"/></td>
                        </tr>
                        <tr>
                            <td class="col-md-9"><em>Guthaben</em></td>
                            <td class="col-md-1" style="text-align: center"> &nbsp; </td>
                            <td class="col-md-1 text-center">&nbsp;</td>
                            <td class="col-md-1 text-center">-<fmt:formatNumber value="${deposit}" type="number" pattern="#.##"/> <c:out value="${currency}"/></td>
                        </tr>
                        </c:if>
                        <tr>
                            <td>   </td>
                            <td>   </td>
                            <td class="text-right">
                            <p>
                                <strong>Zwischensumme: </strong>
                            </p>
                            <p>
                                <strong><spring:message code="register.vat"/> <c:out value="${vatPercent}"/>%: </strong>
                            </p></td>
                            <td class="text-center">
                            <p>
                                <strong><fmt:formatNumber value="${subtotal}" type="number" pattern="#.##"/> <c:out value="${currency}"/></strong>
                            </p>
                            <p>
                                <strong><fmt:formatNumber value="${vat}" type="number" pattern="#.##"/> <c:out value="${currency}"/></strong>
                            </p></td>
                        </tr>
                        <tr>
                            <td>   </td>
                            <td>   </td>
                            <td class="text-right"><h4><strong>Total: </strong></h4></td>
                            <td class="text-center text-danger"><h4><strong><fmt:formatNumber value="${total}" type="number" pattern="#.##"/> <c:out value="${currency}"/></strong></h4></td>
                        </tr>
                    </tbody>
                </table>
            </div>

<!-- ende receipt example -->

</div>
<!-- /spalte 2 mit liste -->

</div><!-- /row -->

<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->


<!-- /warenkorb CHECKOUT-->
<!-- /WARENKORB CHECKOUT ################################################################################################################################## -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->



</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->

<!-- ###################################################################################################################################################### -->
<!-- /WARENKORB ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>