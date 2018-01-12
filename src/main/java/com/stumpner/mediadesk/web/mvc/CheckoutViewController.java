package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paymill.context.PaymillContext;
import com.paymill.services.PaymentService;
import com.paymill.services.TransactionService;
import com.paymill.services.ClientService;
import com.paymill.models.Payment;
import com.paymill.models.Transaction;
import com.paymill.models.Client;
import com.paymill.models.PaymillList;
import com.paymill.exceptions.PaymillException;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;

import java.util.Iterator;
import java.util.Date;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 07.10.2014
 * Time: 21:17:38
 * To change this template use File | Settings | File Templates.
 */
public class CheckoutViewController extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        LngResolver lngResolver = new LngResolver();
        ShoppingCartService shoppingCartService = new ShoppingCartService();
        shoppingCartService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));

        ShoppingCartService.Checkout checkout = shoppingCartService.createCheckoutInfo(httpServletRequest);

        httpServletRequest.setAttribute("mediaObjectList", checkout.mediaObjectList);
        httpServletRequest.setAttribute("subtotalbeforedeposit", checkout.subtotalbeforedeposit);
        httpServletRequest.setAttribute("deposit", checkout.deposit);
        httpServletRequest.setAttribute("subtotal", checkout.subtotal);
        httpServletRequest.setAttribute("vatPercent", checkout.vatPercent);
        httpServletRequest.setAttribute("vat", checkout.vat);
        httpServletRequest.setAttribute("total", checkout.total);
        httpServletRequest.setAttribute("paymillKeyPublic", Config.paymillKeyPublic);
        httpServletRequest.setAttribute("currency", Config.currency);

        //Checkout / Paymill Service
        if (httpServletRequest.getParameter("token")!=null) {
            //Zahlung starten....
            String paymentTokenString = httpServletRequest.getParameter("token");

            //PaymillContext paymillContext = new PaymillContext("8597921b50ff200286e109c836972c65");
            PaymillContext paymillContext = new PaymillContext(Config.paymillKeyPrivate);
            //ClientService clientService = paymillContext.getClientService();
            PaymentService paymentService = paymillContext.getPaymentService();
            ClientService clientService = paymillContext.getClientService();
            String clientString = checkout.user.getUserName();
            Client c = null;
            try {
                PaymillList<Client> clientList = clientService.list();
                Iterator<Client> clients = clientList.getData().iterator();
                while (clients.hasNext()) {
                    Client client = clients.next();
                    if (client.getDescription()!=null) {
                        System.out.println("client: "+client.getDescription());
                        if (client.getDescription().equalsIgnoreCase(clientString)) {
                            c = client;
                            System.out.println("CLIENT gefunden: "+c.getId());
                            break;
                        }
                    }
                }
            } catch (PaymillException e) {
                System.out.println("PaymillException e: "+e.getMessage());
            }
            if (c==null) {
                //Client anlegen
                System.out.println("client nicht gefunden: "+clientString+" ");
                c = clientService.createWithEmailAndDescription(checkout.user.getEmail(),clientString);
                //if (Config.userEmailAsUsername) {
                    //c.setDescription(user.getLastName()+", "+user.getLastName()+" ("+user.getCompany()+")");
                //} else {
                    //c.setDescription(user.getUserName());
                //}
                //clientService.update(c);
            } else {
                System.out.println("client gefunden: "+c.getId()+" ");
            }
            System.out.println("token: "+paymentTokenString+" ");
            //try {
            Payment payment = paymentService.createWithTokenAndClient(paymentTokenString,c);

            TransactionService transactionService = paymillContext.getTransactionService();
            Transaction transaction = transactionService.createWithPayment(payment, checkout.totalInCents, Config.currency, checkout.transactionDescription);

            System.out.println("transaction: "+transaction.getId());

            httpServletRequest.getSession().setAttribute("checkout",transaction.getId());

            shoppingCartService.setPayTransactionId(checkout.user.getUserId(), transaction.getId());

            sendCheckoutBeleg(transaction.getId(), checkout);

            httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("download?ref=shop&download=checkout&nosdl=nosdl")
            );
            //} catch (PaymillException e) {

            //}

        }

        //Fehlerbehandlung aus Formular
        if (httpServletRequest.getParameter("payapierror")!=null) {

            String payapierror = httpServletRequest.getParameter("payapierror");
            String errormessage = payapierror;
            if (payapierror.equalsIgnoreCase("field_invalid_card_number")) {
                errormessage = "Kreditkartennummer ung�ltig";
            }
            if (payapierror.equalsIgnoreCase("field_invalid_card_exp")) {
                errormessage = "G�ltig bis falsch";
            }
            if (payapierror.equalsIgnoreCase("field_invalid_card_cvc")) {
                errormessage = "CVC Code falsch";
            }

            httpServletRequest.setAttribute("errormessage", errormessage);
            httpServletRequest.setAttribute("error", true);
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Sendet einen Checkout-Beleg an den Admin als Information dass Bilder bezahlt wurden
     * @param id
     * @param checkout
     */
    private void sendCheckoutBeleg(String id, ShoppingCartService.Checkout checkout) {


        UserService userService = new UserService();
        /*
                MessageFormat mf = new MessageFormat(Config.mailDownloadInfoMailBody);
                Object[] parameters = { Config.webTitle , user.getUserName() , i , Config.httpBase , imageListString , new Date() };
                String mailbody = mf.format(parameters);
                */
        String subject = "Checkout Beleg "+id;
        String mailbody = "Dieser Beleg informiert sie �ber eine Kreditkarten-Transaktion:\n\n";
        mailbody = mailbody + "-Transaktions-ID: "+id+"\n";
        mailbody = mailbody + "-Benutzer       : "+checkout.user.getName()+" "+checkout.user.getEmail()+"\n";
        mailbody = mailbody + "-Zeitpunkt      : "+new Date()+"\n";
        mailbody = mailbody + "\n\nDatei                      Preis\n";

        mailbody = mailbody + "--------------------------------\n";

        Iterator mediaObjects = checkout.mediaObjectList.iterator();
        while (mediaObjects.hasNext()) {
            ImageVersionMultiLang mo = (ImageVersionMultiLang)mediaObjects.next();
            mailbody = mailbody + mo.getVersionName()+"              "+mo.getPrice()+"\n";
        }
        mailbody = mailbody +"Zwischensumme              "+checkout.subtotal+" EUR\n";
        mailbody = mailbody +"MwSt ("+checkout.vatPercent+"%)   "+checkout.vat+" EUR\n";
        mailbody = mailbody +"Gesamt                     "+checkout.total+" EUR\n";
        mailbody = mailbody + "\n\n\nAchtung: Gilt nicht als Rechnung, diese muss separat gestellt und an den Kunden geschickt werden.\n";
        mailbody = mailbody + "-Transaktions-ID: "+id+"\n";

                    try {
                        User admin = (User) userService.getByName("admin");
                        MailWrapper.sendAsync(Config.mailserver,Config.mailsender,admin.getEmail(),subject,mailbody);

                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } 

    }

}
