package com.stumpner.mediadesk.web.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.Calendar;
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
 * Date: 19.10.2009
 * Time: 19:41:09
 * To change this template use File | Settings | File Templates.
 */
public class LogoServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(LogoServlet.class);

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        logger.debug("Logo Request: "+httpServletRequest.getRequestURI());

        String filename = httpServletRequest.getRequestURI().substring(1); //for example: logo.gif logo2.png

        Logger logger = Logger.getLogger(getClass());
        httpServletResponse.setContentType("image/gif");

        Calendar cal = GregorianCalendar.getInstance();
        int cacheHours = 12;
        cal.add(GregorianCalendar.SECOND,cacheHours*60*60);

        httpServletResponse.setDateHeader("Expires",cal.getTimeInMillis());
        httpServletResponse.setHeader("Cache-Control","max-age="+(cacheHours*60*60));

        String icoFilename = Config.imageStorePath+ File.separator + filename;
        File icoFile = new File(icoFilename);
        if (!icoFile.exists()) {

            icoFilename = getServletContext().getRealPath("/WEB-INF/"+filename);
            logger.debug("No custom logo exists, using mediaDESK-logo: "+icoFilename);
        }

        logger.debug("logo-request");


        FileInputStream is = null;
        OutputStream os = null;

            //Bild anzeigen
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


        //super.doGet(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
