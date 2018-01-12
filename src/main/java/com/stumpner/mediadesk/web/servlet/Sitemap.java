package com.stumpner.mediadesk.web.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

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
 * Date: 26.09.2006
 * Time: 21:44:05
 * To change this template use File | Settings | File Templates.
 */
public class Sitemap extends HttpServlet {

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        Logger logger = Logger.getLogger(getClass());
        logger.debug("sitemap-servlet");

        if (Config.robotsAllow) {
            httpServletResponse.setContentType("text/xml;charset=UTF-8");
            httpServletRequest.getRequestDispatcher("/WEB-INF/sitemap.jsp").include(httpServletRequest,httpServletResponse);
        } else {
            httpServletResponse.setContentType("text/html;charset=UTF-8");
            httpServletResponse.getWriter().write("indexing not allowed by configuration");
        }

        //super.service(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static String getChangefreq(Date objectDate) {

        GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
        calendar.roll(GregorianCalendar.DAY_OF_MONTH,-1);

        if (objectDate.after(calendar.getTime())) {
            //gestern:
            return "always";
        }

        calendar.roll(GregorianCalendar.WEEK_OF_YEAR,-1);

        if (objectDate.after(calendar.getTime())) {
            //letzte woche
            return "weekly";
        }

        calendar.roll(GregorianCalendar.MONTH,-1);

        if (objectDate.after(calendar.getTime())) {
            //letztes monat
            return "monthly";
        }

        calendar.roll(GregorianCalendar.YEAR,-1);

        if (objectDate.after(calendar.getTime())) {
            //letztes monat
            return "yearly";
        }

        return "always";
    }

    public static String getPriority(Date date, int imageCount) {

        if (imageCount==0) {
            return "0.1";
        }
        if (imageCount<5) {
            return "0.3";
        }

        GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
        calendar.roll(GregorianCalendar.MONTH,-1);

        if (date.after(calendar.getTime())) {
            //gestern:
            return "1.0";
        }

        return "0.1";
    }
}
