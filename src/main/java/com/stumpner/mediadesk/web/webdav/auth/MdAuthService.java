package com.stumpner.mediadesk.web.webdav.auth;

import com.bradmcevoy.http.AuthenticationService;
import com.bradmcevoy.http.AuthenticationHandler;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.http11.auth.NonceProvider;
import com.bradmcevoy.http.http11.auth.DigestAuthenticationHandler;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

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
 * Date: 11.05.2011
 * Time: 20:15:00
 * To change this template use File | Settings | File Templates.
 */
public class MdAuthService extends AuthenticationService {

    public MdAuthService(List<AuthenticationHandler> authenticationHandlers) {
        super(authenticationHandlers);    //To change body of overridden methods use File | Settings | File Templates.
        System.out.println("MdAuthService1");
    }

    public MdAuthService(NonceProvider nonceProvider) {
        super(nonceProvider);    //To change body of overridden methods use File | Settings | File Templates.
        System.out.println("MdAuthService2");
    }

    public MdAuthService() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
        System.out.println("MdAuthService");
    }

    public void setDisableBasic(boolean b) {
        super.setDisableBasic(b);    //To change body of overridden methods use File | Settings | File Templates.
        System.out.println("setDisableBasic");
    }

    public boolean isDisableBasic() {
        System.out.println("isDisableBasic");
        return super.isDisableBasic();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setDisableDigest(boolean b) {
        System.out.println("setDisableDigest");
        super.setDisableDigest(b);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isDisableDigest() {
        System.out.println("isDisableDigest");
        return super.isDisableDigest();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AuthStatus authenticate(Resource resource, Request request) {
        System.out.println("AuthService authenticate");
        return super.authenticate(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List<String> getChallenges(Resource resource, Request request) {
        System.out.println("getChallenges");
        return super.getChallenges(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List<AuthenticationHandler> getAuthenticationHandlers() {

        List<AuthenticationHandler> ah = super.getAuthenticationHandlers();
        System.out.println("getAuthenticationHandlers");
        Iterator<AuthenticationHandler> it = ah.iterator();
        while (it.hasNext()) {
            AuthenticationHandler h = it.next();
            System.out.println(" + "+h.getClass().getName());
        }

        List<AuthenticationHandler> ahl = new LinkedList<AuthenticationHandler>();
        ahl.add(new DigestAuthenticationHandler() {

            public boolean supports(Resource resource, Request request) {
                System.out.println("AuthenticationHandler: supports");
                return super.supports(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
            }

            public Object authenticate(Resource resource, Request request) {
                System.out.println("AuthenticationHandler: authenticate");
                return super.authenticate(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
            }

            public String getChallenge(Resource resource, Request request) {
                System.out.println("AuthenticationHandler: getChallenge");
                return super.getChallenge(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
            }

            public boolean isCompatible(Resource resource) {
                System.out.println("AuthenticationHandler: isCompatible");
                return super.isCompatible(resource);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        return ahl;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List<AuthenticationHandler> getExtraHandlers() {
        System.out.println("getExtraHandlers");
        return super.getExtraHandlers();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setExtraHandlers(List<AuthenticationHandler> authenticationHandlers) {
        System.out.println("setExtraHandlers");
        super.setExtraHandlers(authenticationHandlers);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
