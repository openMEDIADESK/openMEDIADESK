package com.stumpner.mediadesk.web.webdav;

import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import com.stumpner.mediadesk.web.webdav.auth.DigestAuthUtils;

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
 * Date: 05.05.2011
 * Time: 10:25:05
 * To change this template use File | Settings | File Templates.
 */
public class WebdavAuthFilter implements Filter {


    private static final int AUTH_BASIC = 1;
    private static final int AUTH_DIGEST = 2;
    private static final int authmode = AUTH_DIGEST;

    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = null;
        HttpServletRequest request = null;
        if (servletResponse instanceof HttpServletResponse) {
            response = ((HttpServletResponse)servletResponse);
        }
        if (servletRequest instanceof HttpServletRequest) {
            request = ((HttpServletRequest)servletRequest);
        }

        if (!Config.webdavEnabled) {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);

        } else {

            //Get Authorization header
            String auth = request.getHeader("Authorization");
            //System.out.println("WebdavRequest [IP:"+request.getRemoteAddr()+"],[Authorization = "+ auth+"]");

            //Do we allow that user?
            if (!checkAuthorization(auth, request)) {
                // Not allowed, so reports he's unauthorized
                //System.err.println("WebdavRequest: [IP:"+request.getRemoteAddr()+"] Auth: Not allowed");
                requestAuth(response);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

            } else {

                //Allowed, so show him the secret stuff
                //System.out.println("RemoteUser= "+request.getRemoteUser());
                //System.out.println("AuthType= "+request.getAuthType());
                //System.out.println("Remote-User= "+request.getRemoteUser());
                //System.out.println("Principal= "+request.getUserPrincipal());
                //System.out.println("digestUser = "+request.getAttribute("digestUser"));

                filterChain.doFilter(request,response);
            }

        }

    }

    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void requestAuth(HttpServletResponse response) {

        switch(authmode) {
            case AUTH_BASIC:
               //System.out.println("requestAuth=BASIC");
                requestBasicAuth(response);
                break;
            case AUTH_DIGEST:
               //System.out.println("requestAuth=DIGEST");
                requestDigestAuth(response);
                break;
        }
    }

    private boolean checkAuthorization(String auth, HttpServletRequest request) throws IOException {



        if (auth==null) return false; // no authmode

        switch(authmode) {
            case AUTH_BASIC:
                return checkBasicAuthorization(auth);
            case AUTH_DIGEST:
                return checkDigestAuthorization(auth, request);
            default:
                return false;
        }

    }

    private boolean checkDigestAuthorization(String auth, HttpServletRequest request) {

        if (!auth.toUpperCase().startsWith("DIGEST "))
          return false;  // we only do DIGEST

        Map<String,String> headerMap = DigestAuthUtils.parseDigestRequest(auth);
/*
        String section212response = auth.substring(7);

        String[] headerEntries = DigestAuthUtils.splitIgnoringQuotes(section212response, ',');
        Map<String,String> headerMap = DigestAuthUtils.splitEachArrayElementAndCreateMap(headerEntries, "=", "\"");
*/
        //System.out.println("DigestRequest: "+auth);

        String username = headerMap.get("username");
        String realm = headerMap.get("realm");
        String nonce = headerMap.get("nonce");
        String uri = headerMap.get("uri");
        String responseDigest = headerMap.get("response");
        String qop = headerMap.get("qop"); // RFC 2617 extension
        String nc = headerMap.get("nc"); // RFC 2617 extension
        String cnonce = headerMap.get("cnonce"); // RFC 2617 extension

        String password = "";
        Authenticator authenticator = new Authenticator();
        password = authenticator.getDigestPassword(username);
        if (password.length()==0) { return false; } //leere passw�rter sind nich zugelassen

        String serverDigestMd5 = DigestAuthUtils.generateDigest(true, username, realm, password,
                                request.getMethod(), uri, qop, nonce, nc, cnonce);

        // If digest is still incorrect, definitely reject authentication attempt
        //System.out.println("Response Digest: "+responseDigest);
        //System.out.println("Server Digest: "+serverDigestMd5);

        if (!serverDigestMd5.equals(responseDigest)) {
            //Passwort stimmt nicht �berein:
            return false;
        } else {
            //System.out.println("digestUser settet");
            request.setAttribute("digestUser",username);
            return true;
        }



        //return true;  //To change body of created methods use File | Settings | File Templates.
    }

    private boolean checkBasicAuthorization(String auth) throws IOException {

        if (!auth.toUpperCase().startsWith("BASIC "))
          return false;  // we only do BASIC

        // Get encoded user and password, comes after "BASIC "
        String userpassEncoded = auth.substring(6);

        // Decode it, using any base 64 decoder
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        String userpassDecoded = new String(dec.decodeBuffer(userpassEncoded));

        String[] decodedArray = userpassDecoded.split(":");
        String username = "";
        String password = "";
        if (decodedArray.length==2) {
            username = decodedArray[0];
            password = decodedArray[1];
        } else {
            return false;
        }

        com.stumpner.mediadesk.usermanagement.Authenticator authenticator = new Authenticator();
        return authenticator.checkPassword(username,password);

        /*
        System.out.println("Username: "+username+" Password: "+password);

        //todo with authenticator class
        // Check our user list to see if that user and password are "allowed"
        if ("allowed".equals(users.get(userpassDecoded)))
          return true;
        else
          return false;
        */


    }

    private void requestBasicAuth(HttpServletResponse response) {

        response.setHeader("WWW-Authenticate", "BASIC realm=\"users\"");
    }

    private void requestDigestAuth(HttpServletResponse response) {

        StringBuffer authString = new StringBuffer();

        

        authString.append("Digest ");
        authString.append("realm=\""+ UserService.REALM+"\",");
        //authString.append("domain=\"192.168.178.99\",");
        authString.append("nonce=\""+System.currentTimeMillis()+"\",");
        //authString.append("opaque=\"ce"+System.currentTimeMillis()+"\"\n");
        //authString.append("qop=\"auth,auth-int\","); mit diesen werten hat es bei ff + ie gefunkt bei windows nicht
        authString.append("qop=\"auth\",");
        //authString.append("stale=\"false\",\n");
        authString.append("algorithm=\"MD5\"");
        response.setHeader("WWW-Authenticate", authString.toString());

        //System.out.println("Header: WWW-Authenticat (to Client): "+authString.toString());
    }
}
