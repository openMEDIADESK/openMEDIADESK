package com.stumpner.mediadesk.web.servlet;

import net.sf.webdav.WebdavServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Hashtable;

import com.stumpner.mediadesk.usermanagement.Authenticator;
import org.apache.log4j.Logger;

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
 * Date: 30.12.2008
 * Time: 18:19:01
 * To change this template use File | Settings | File Templates.
 */
public class SimpleWebdavServlet extends WebdavServlet {

    Logger logger = Logger.getLogger(SimpleWebdavServlet.class);

    private static final int AUTH_BASIC = 1;
    private static final int AUTH_DIGEST = 2;
    private static final int authmode = AUTH_DIGEST;


    Hashtable users = new Hashtable();

    public SimpleWebdavServlet() {

        users.put("franzi:franzi","allowed");
    }

    /*
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        //Get Authorization header
        String authmode = httpServletRequest.getHeader("Authorization");
        System.out.println("Authorization = "+ authmode);

        //Do we allow that user?
        if (!checkAuthorization(authmode)) {
            // Not allowed, so reports he's unauthorized
            System.out.println("Not allowed");
            httpServletResponse.setHeader("WWW-Authenticate", "BASIC realm=\"users\"");
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {

            //Allowed, so show him the secret stuff

            System.out.println("AuthType= "+httpServletRequest.getAuthType());
            System.out.println("Remote-User= "+httpServletRequest.getRemoteUser());
            System.out.println("Principal= "+httpServletRequest.getUserPrincipal());

            super.doGet(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.

        }
    }
    */

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        //Get Authorization header
        String auth = httpServletRequest.getHeader("Authorization");
        logger.debug("WebdavRequest [IP:"+httpServletRequest.getRemoteAddr()+"],[Authorization = "+ auth+"]");

        //Do we allow that user?
        if (!checkAuthorization(auth)) {
            // Not allowed, so reports he's unauthorized
            logger.debug("WebdavRequest: Auth: Not allowed");
            requestAuth(httpServletResponse);
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        } else {

            //Allowed, so show him the secret stuff
            logger.debug("AuthType= "+httpServletRequest.getAuthType());
            logger.debug("Remote-User= "+httpServletRequest.getRemoteUser());
            logger.debug("Principal= "+httpServletRequest.getUserPrincipal());

             super.service(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.

        }
    }

    private void requestAuth(HttpServletResponse response) {

        switch(authmode) {
            case AUTH_BASIC:
                logger.debug("requestAuth=BASIC");
                requestBasicAuth(response);
                break;
            case AUTH_DIGEST:
                logger.debug("requestAuth=DIGEST");
                requestDigestAuth(response);
                break;
        }


    }

    private void requestBasicAuth(HttpServletResponse response) {

        response.setHeader("WWW-Authenticate", "BASIC realm=\"users\"");
    }

    private void requestDigestAuth(HttpServletResponse response) {

        StringBuffer authString = new StringBuffer();


        authString.append("Digest ");
        authString.append("realm=\"mediaDESK\",");
        //authString.append("domain=\"192.168.178.99\",");
        authString.append("nonce=\""+System.currentTimeMillis()+"\",");
        //authString.append("opaque=\"ce"+System.currentTimeMillis()+"\"\n");
        authString.append("qop=\"auth,auth-int\",");
        //authString.append("stale=\"false\",\n");
        authString.append("algorithm=\"MD5\"");
        response.setHeader("WWW-Authenticate", authString.toString());

        logger.debug("Header: WWW-Authenticat (to Client): "+authString.toString());
    }

    private boolean checkAuthorization(String auth) throws IOException {

        

        if (auth==null) return false; // no authmode

        switch(authmode) {
            case AUTH_BASIC:
                return checkBasicAuthorization(auth);
            case AUTH_DIGEST:
                return checkDigestAuthorization(auth);
            default:
                return false;
        }

    }

    private boolean checkDigestAuthorization(String auth) {

        if (!auth.toUpperCase().startsWith("DIGEST "))
          return false;  // we only do DIGEST

        logger.debug("DigestRequest: "+auth);

        return true;  //To change body of created methods use File | Settings | File Templates.
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

        Authenticator authenticator = new Authenticator();
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
}
