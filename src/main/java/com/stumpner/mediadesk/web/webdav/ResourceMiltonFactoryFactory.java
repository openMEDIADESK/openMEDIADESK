package com.stumpner.mediadesk.web.webdav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bradmcevoy.http.AuthenticationService;
import com.bradmcevoy.http.ResourceFactory;
import com.bradmcevoy.http.ResourceFactoryFactory;
import com.bradmcevoy.http.webdav.WebDavResponseHandler;
import com.bradmcevoy.http.webdav.DefaultWebDavResponseHandler;

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
 * Date: 16.03.2011
 * Time: 18:47:34
 * To change this template use File | Settings | File Templates.
 */
public class ResourceMiltonFactoryFactory implements ResourceFactoryFactory {

	private Logger log = LoggerFactory.getLogger(ResourceMiltonFactoryFactory.class);

	private static AuthenticationService authenticationService;
	private static WebdavResourceFactory resourceFactory;

	@Override
	public ResourceFactory createResourceFactory() {
        return resourceFactory;
	}

	@Override
	public WebDavResponseHandler createResponseHandler() {
        System.out.println("create responseHandler");
        return new DefaultWebDavResponseHandler(authenticationService);
	}

	@Override
	public void init() {
		log.debug("init");

        if( authenticationService == null ) {
			authenticationService = new AuthenticationService();
            //authenticationService = new MdAuthService();
            authenticationService.setDisableDigest(false);
            authenticationService.setDisableBasic(true);
            System.out.println("authenticationhandlers.size="+authenticationService.getAuthenticationHandlers().size());
            resourceFactory = new WebdavResourceFactory();
			checkInitialData();
		}
	}

	private void checkInitialData() {
	}

}
