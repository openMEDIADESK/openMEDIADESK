package com.stumpner.mediadesk.web.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
 * Date: 06.12.2010
 * Time: 20:20:56
 * To change this template use File | Settings | File Templates.
 */
public class CssServlet extends HttpServlet {

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        Logger logger = Logger.getLogger(getClass());
        httpServletResponse.setContentType("text/css");

        Calendar cal = GregorianCalendar.getInstance();
        int cacheHours = 12;
        cal.add(GregorianCalendar.SECOND,cacheHours*60*60);

        httpServletResponse.setDateHeader("Expires",cal.getTimeInMillis());
        httpServletResponse.setHeader("Cache-Control","max-age="+(cacheHours*60*60));

        String icoFilename = getServletContext().getRealPath("/WEB-INF/template/current/style.css");
        File icoFile = new File(icoFilename);
        if (!icoFile.exists()) {

            logger.debug("No custom style.css exists, using none");
            httpServletResponse.sendError(404);
        } else {

            logger.debug("style.css-request");


            FileInputStream is = null;
            OutputStream os = null;

                //CSS vom templatepfad ausgeben
                try {
                    is = new FileInputStream(icoFilename);

                    os = httpServletResponse.getOutputStream();

                    int nbytes = -1;
                    byte b[] = new byte[10000];
                    while((nbytes = is.read(b,0,10000)) != -1) {
                      os.write(b,0,nbytes);
                    }

                    //clientstream close
                    os.close();
                    //filestream close
                    is.close();
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    httpServletResponse.sendError(404);
                } finally {
                    if (is!=null) is.close();
                }

            }
        }


        //super.doGet(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }