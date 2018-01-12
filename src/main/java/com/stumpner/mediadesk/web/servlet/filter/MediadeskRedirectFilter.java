package com.stumpner.mediadesk.web.servlet.filter;

import com.stumpner.filter.sendredirect.HostSendRedirectFilter;

import javax.servlet.*;
import java.io.IOException;

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
 * Date: 28.09.2016
 * Time: 19:40:00
 * To change this template use File | Settings | File Templates.
 */
public class MediadeskRedirectFilter extends HostSendRedirectFilter {

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);    //To change body of overridden methods use File | Settings | File Templates.

        //System.out.println("MediadeskRedirectFilter init");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (Config.configParam.contains("-REDI")) {
            super.doFilter(request, response, chain);    //To change body of overridden methods use File | Settings | File Templates.
        } else {
            //System.out.println("Filter: Normal Operation");
            chain.doFilter(request, response);
        }


    }
}
