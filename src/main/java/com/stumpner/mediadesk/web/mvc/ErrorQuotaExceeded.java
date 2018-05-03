package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import com.stumpner.mediadesk.core.lic.LicenceChecker;
import com.stumpner.mediadesk.core.Config;

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
 * Date: 21.10.2010
 * Time: 19:03:55
 * To change this template use File | Settings | File Templates.
 */
public class ErrorQuotaExceeded extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Map model = new HashMap();

        System.out.println("ERROR: Quota Full (Licence) "+httpServletRequest.getRequestURI());

        LicenceChecker licenceChecker = new LicenceChecker();
        httpServletRequest.setAttribute("redirectTo","http://www.mediaDESK.net");
        httpServletRequest.setAttribute("headline","imageimport.nolicense");
        httpServletRequest.setAttribute("info","imageimport.nolicensetextmb");
        httpServletRequest.setAttribute("args",Config.licMaxMb+" ("+ licenceChecker.getImageMb()+")");

        return super.handleRequestInternal(httpServletRequest,httpServletResponse);
    }

}
