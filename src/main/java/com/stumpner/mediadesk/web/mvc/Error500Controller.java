package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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
 * User: franzstumpner
 * Date: 05.07.2005
 * Time: 22:51:39
 * To change this template use File | Settings | File Templates.
 */
public class Error500Controller extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        String errorinfo = "";
        errorinfo = errorinfo + "URI:"+request.getAttribute("javax.servlet.error.request_uri");
        errorinfo = errorinfo + " Exception: "+request.getAttribute("javax.servlet.error.exception");
        errorinfo = errorinfo + " Type:"+request.getAttribute("javax.servlet.error.exception_type");
        errorinfo = errorinfo + " Message:"+request.getAttribute("javax.servlet.error.message");
        errorinfo = errorinfo + " Servlet:"+request.getAttribute("javax.servlet.error.servlet_name");
        errorinfo = errorinfo + " Qs:"+request.getQueryString();
        errorinfo = errorinfo + " IP:"+request.getRemoteAddr();
        errorinfo = errorinfo + " User-Agent:"+request.getHeader("User-Agent");
        errorinfo = errorinfo + " From:"+request.getHeader("From");
        Config.lastError500 = errorinfo+" "+(new Date());

        return super.handleRequestInternal(request,httpServletResponse);
    }

}
