package com.stumpner.mediadesk.web.webdav;


import com.bradmcevoy.http.http11.auth.BasicAuthHandler;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;

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
 * Date: 19.04.2011
 * Time: 20:20:26
 * To change this template use File | Settings | File Templates.
 */
public class Authenticator extends BasicAuthHandler {

    public Authenticator() {

        //System.out.println("construction authenticator");

    }

    /*
    public Object authenticate(Resource resource, Request request) {
        System.out.println("Authenticate: ");
        return super.authenticate(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
    } */

    public boolean supports(Resource resource, Request request) {

        boolean supports = super.supports(resource, request);
        //System.out.println("Supports: "+supports);
        return true;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object authenticate(Resource resource, Request request) {
        //System.out.println("Authenticate->Authenticate");
        return super.authenticate(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getChallenge(Resource resource, Request request) {
        //System.out.println("GetChallenge");
        return super.getChallenge(resource, request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isCompatible(Resource resource) {
        return super.isCompatible(resource);    //To change body of overridden methods use File | Settings | File Templates.
    }

    

}
