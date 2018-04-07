package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.stats.DownloadLogger;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
 * Date: 18.05.2005
 * Time: 22:35:12
 * To change this template use File | Settings | File Templates.
 */
public class StatViewController extends AbstractPageController {

    public StatViewController() {
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        UserService userService = new UserService();
        if (userService.processAutologin(httpServletRequest)) {
            System.out.println("autologin processed");
        }

        DownloadLoggerService dlls = new DownloadLoggerService();
        MediaService imageService = new MediaService();
        List userList = new ArrayList();
        int downloadType = 1;
        String chartTitle = "Heruntergeladene Dateien";

        int interval = 2;
        if (httpServletRequest.getParameter("interval")!=null) {
            interval = Integer.parseInt(httpServletRequest.getParameter("interval"));
        }
        if (httpServletRequest.getParameterValues("user")!=null) {
            String[] userIds = httpServletRequest.getParameterValues("user");
            for(int p=0;p<userIds.length;p++) {
                userList.add(userIds[p]);
            }
        }
        if (httpServletRequest.getParameter("downloadType")!=null) {
            downloadType = Integer.parseInt(httpServletRequest.getParameter("downloadType"));
        } else {
            downloadType = SimpleDownloadLogger.DTYPE_DOWNLOAD;
        }

        switch (downloadType) {
            case SimpleDownloadLogger.DTYPE_DOWNLOAD:
                chartTitle = "Heruntergeladene Dateien";
                break;
            case SimpleDownloadLogger.DTYPE_PIN:
                chartTitle = "PIN Downloads";
                break;
            case SimpleDownloadLogger.DTYPE_PODCAST:
                chartTitle = "Podcast Downloads";
                break;
            case SimpleDownloadLogger.DTYPE_STREAM_START:
                chartTitle = "Audio/Videostreams gestartet";
                break;
            case SimpleDownloadLogger.DTYPE_STREAM_END:
                chartTitle = "Audio/Videostreams fertig abgerufen";
                break;
            case SimpleDownloadLogger.DTYPE_WEBDAV:
                chartTitle = "Downloads via Webdav";
                break;
        }

        List<DownloadLogger> allLog = dlls.getLog(interval,downloadType,userList);

        httpServletRequest.setAttribute("userList",userService.getUserAtomList());
        httpServletRequest.setAttribute("interval",Integer.toString(interval));
        httpServletRequest.setAttribute("downloadType",Integer.toString(downloadType));

        httpServletRequest.setAttribute("logList",allLog);
        httpServletRequest.setAttribute("instanceName",Config.instanceName);
        httpServletRequest.setAttribute("versionNumber",Config.versionNumbner);
        httpServletRequest.setAttribute("versionDate",Config.versionDate);
        httpServletRequest.setAttribute("licExpires",Config.licExpireDate);
        httpServletRequest.setAttribute("licMaxImages",new Integer(Config.licMaxImages));
        httpServletRequest.setAttribute("licMaxMb",new Integer(Config.licMaxMb));
        httpServletRequest.setAttribute("licMaxUsers",new Integer(Config.licMaxUsers));
        httpServletRequest.setAttribute("licTo",Config.licTo);
        httpServletRequest.setAttribute("imageCount",new Integer(imageService.getImageCount()));
        httpServletRequest.setAttribute("imageMb",new Integer(imageService.getImageMb()));

        BigDecimal quotaTotal = imageService.getQuotaTotalMb();
        BigDecimal quotaAvailable = imageService.getQuotaAvailableMb();
        BigDecimal quotaUsed = quotaTotal.subtract(quotaAvailable);

            BigDecimal quotaTotalGb = quotaTotal.divide(BigDecimal.valueOf(1000));
            BigDecimal quotaAvailGb = quotaAvailable.divide(BigDecimal.valueOf(1000));
            BigDecimal quotaUsedGb = quotaTotalGb.subtract(quotaAvailGb);

        BigDecimal quotaUsedPercent = quotaUsed.divide(quotaTotal,100, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));

        System.out.println("Quota Percent: "+quotaUsedPercent);

        httpServletRequest.setAttribute("quotaUsedPercent",quotaUsedPercent);
        httpServletRequest.setAttribute("quotaTotalGb",quotaTotalGb);
        httpServletRequest.setAttribute("quotaAvailGb",quotaAvailGb);
        httpServletRequest.setAttribute("quotaUsedGb",quotaUsedGb);

        httpServletRequest.setAttribute("freemem",new Long(Runtime.getRuntime().freeMemory()));

        httpServletRequest.setAttribute("interval",new Integer(interval));

        httpServletRequest.setAttribute("chartTitle", chartTitle);


        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
