package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.StringTokenizer;

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
 * Date: 28.04.2005
 * Time: 21:35:49
 * To change this template use File | Settings | File Templates.
 */
public class ImageServlet extends HttpServlet {


    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletResponse.setContentType("image/jpeg");
        FileInputStream is = null;
        OutputStream os = null;

        /*
        An BasicMediaObject Url looks like this:
        /imageservlet/12345/3/image.jpg

        where:
        - 12345 is the ivid
        - 3 is the imagesize
        */

        StringTokenizer token = new StringTokenizer(httpServletRequest.getRequestURI(),"/");
        int ivid = -1;
        int imageSize = -1;

        int i=0;
        try {
            while (token.hasMoreTokens()) {
                i++;
                String tok = token.nextToken();
                switch (i) {
                    case 1: break;
                    case 2: ivid = Integer.parseInt(tok);
                    case 3: imageSize = Integer.parseInt(tok);
                }
            }

            //Zugriff auf Originalbild verweigern
            if (!Config.configParam.contains("-SLIDE")) { //With slideshow enabled, allow access to the original image
                if (imageSize == 0) {
                    imageSize = 1;
                    httpServletResponse.sendError(403, "Forbidden: Zugriff auf Originalbild verweigert");
                }
            }
            String imageFile = Config.imageStorePath+"/"+ivid+"_"+imageSize;

            //Bild anzeigen
            try {
                is = new FileInputStream(imageFile);

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

        } catch (NumberFormatException e) {
            e.printStackTrace();
            httpServletResponse.sendError(409,"Conflict: Ungueltige oder keine Bild-ID (IVID) oder Bildsize angegeben");
        }

    }

    private int getIvid(String requestUri) {

        StringTokenizer token = new StringTokenizer(requestUri,"/");
        return 1;

    }

    protected long getLastModified(HttpServletRequest httpServletRequest) {
        return super.getLastModified(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }



}
