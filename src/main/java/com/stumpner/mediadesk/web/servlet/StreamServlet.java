package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.File;
import java.math.BigDecimal;

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
 * Date: 29.12.2011
 * Time: 10:23:59
 * To change this template use File | Settings | File Templates.
 */
public class StreamServlet extends AbstractStreamServlet {
    protected String getServletMapping() {
        return "/stream";
    }

    protected int getDownloadType() {
        return SimpleDownloadLogger.DTYPE_STREAM_START;
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        if (Config.streamEnabled) {
            boolean streamAccessAllowed = false;
            User user = WebHelper.getUser(httpServletRequest);
            if (user.getRole()==User.ROLE_UNDEFINED && Config.streamToVisitors) {
                streamAccessAllowed = true;
            }
            if (user.getRole()>=User.ROLE_USER) {
                streamAccessAllowed = true;
            }

            if (streamAccessAllowed) {
                super.doGet(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.sendError(403,"Stream Access To Visitor is not allowed");
            }
        } else {
            //Podcasts sind nicht aktiviert:
            httpServletResponse.sendError(412,"Streams not enabled");
        }
    }

    protected int getSleepInMsAfter1024bytes(MediaObject mediaObject) {

        boolean enabled = false;
        if (enabled) {
            if (mediaObject.getBitrate()>0) {
                BigDecimal bitrate = new BigDecimal(mediaObject.getBitrate());
                BigDecimal byterate = bitrate.divide(BigDecimal.valueOf(8), BigDecimal.ROUND_UP);
                BigDecimal sleepInMs = BigDecimal.valueOf(1000).divide(byterate, BigDecimal.ROUND_DOWN);
                BigDecimal sleepInMsSafety = sleepInMs.divide(BigDecimal.valueOf(2), BigDecimal.ROUND_DOWN); //Das ganze halbieren damit bei der h�lfte das video/audio schon fertig geladen ist

                //System.out.println("bitrate: "+bitrate);
                //System.out.println("byterate: "+byterate);
                //System.out.println("sleep: "+sleepInMs);
                //System.out.println("Stream Servlet: sleepInMsSafety: "+sleepInMsSafety);

                return sleepInMsSafety.intValueExact();
            } else {
                return 0;
            }
        } else { return 0; }
    }

    protected void trackStreamEnd(HttpServletRequest request, MediaObject mediaObject, int downloadType, int bytes) {
        trackStream(request, mediaObject, SimpleDownloadLogger.DTYPE_STREAM_END, bytes);
    }

    protected String getStreamSourceFilename(MediaObject mediaObject) {

        if (mediaObject.getMayorMime().toUpperCase().equalsIgnoreCase("VIDEO")) {
            //System.out.println("videostream");

            File origFile = new File(Config.imageStorePath+"/"+ mediaObject.getIvid()+"_0");

            File mpeg4File = new File(Config.imageStorePath+"/"+ mediaObject.getIvid()+"_4");
            //File mpeg2File = new File(Config.imageStorePath+"/"+imageVersion.getIvid()+"_5");

            if (mpeg4File.exists()) { System.out.println("Streaming Small-File mp4"); return mpeg4File.toString(); }
            //if (mpeg2File.exists()) { System.out.println("mp2"); return mpeg2File.toString(); }
            if (origFile.exists()) { System.out.println("Streaming Original-File"); return origFile.toString(); }
            return null;

        } else {

            return super.getStreamSourceFilename(mediaObject);

        }
    }
}
