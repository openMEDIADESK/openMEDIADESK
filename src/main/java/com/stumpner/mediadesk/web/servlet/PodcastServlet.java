package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;

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
 * Date: 28.01.2008
 * Time: 21:24:31
 * To change this template use File | Settings | File Templates.
 */
public class PodcastServlet extends AbstractStreamServlet {

    protected String getServletMapping() {
        return "/podcast";
    }

    protected int getDownloadType() {
        return SimpleDownloadLogger.DTYPE_PODCAST;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        if (Config.podcastEnabled) {
            super.doGet(request, httpServletResponse);
        } else {
            //Podcasts sind nicht aktiviert:
            httpServletResponse.sendError(412,"Podcasts not enabled");
        }
    }
}
