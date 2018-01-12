package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
 * Date: 17.10.2008
 * Time: 15:32:54
 * To change this template use File | Settings | File Templates.
 */
public class RobotsTxt extends HttpServlet {

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("User-agent: *\n");
        if (Config.robotsAllow) {
            response.getWriter().write("Disallow: /index/search\n");
            response.getWriter().write("Disallow: /index/send\n");
            response.getWriter().write("Disallow: /login\n");
            response.getWriter().write("Disallow: /register\n");
            response.getWriter().write("Disallow: /de/search\n");
            response.getWriter().write("Disallow: /de/send\n");
            response.getWriter().write("Disallow: /de/login\n");
            response.getWriter().write("Disallow: /de/register\n");
            response.getWriter().write("Disallow: /en/search\n");
            response.getWriter().write("Disallow: /en/send\n");
            response.getWriter().write("Disallow: /en/login\n");
            response.getWriter().write("Disallow: /en/register\n");
            String sitemapUrl = "";
            if (Config.httpBase.endsWith("/")) {
                sitemapUrl = Config.httpBase+"sitemap.xml";
            } else {
                sitemapUrl = Config.httpBase+"/sitemap.xml";
            }
            response.getWriter().write("Sitemap: "+sitemapUrl+"\n");
        } else {
            response.getWriter().write("Disallow: /\n");
        }
        //super.service(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
