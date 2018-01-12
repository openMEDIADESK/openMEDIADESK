package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;
import com.stumpner.mediadesk.stats.DownloadLogger;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
 * Date: 09.08.2016
 * Time: 13:04:57
 * To change this template use File | Settings | File Templates.
 */
public class StatisticExportServlet extends HttpServlet {


    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        User user = WebHelper.getUser(httpServletRequest);
        if (user.getRole()<User.ROLE_ADMIN) {
            httpServletResponse.sendError(403,"Keine Berechtigung zum Download der Statistik");
        } else {

            httpServletResponse.setHeader("Content-Description","File Transfer");
            httpServletResponse.setHeader("Cache-Control","no-store");     //HTTP 1.1
            httpServletResponse.setHeader("Pragma","no-cache"); //HTTP 1.0
            httpServletResponse.setDateHeader ("Expires", 0); //prevents caching at the proxy server
            //httpServletResponse.addHeader("Content-Length", Long.toString(file.length()));
            httpServletResponse.setContentType("text/csv");
            httpServletResponse.setHeader("Content-Disposition","attachment; filename=\"Downloadstat.csv\"");

            Writer w = httpServletResponse.getWriter();
            w.write("Zeit;Typ;Username;Userid;MediaNo;Medianame;Dateiname;Ivid;PIN;FormatX;FormatY;DNS;IP;Payment-TransactionId;Download-Timestamp;\n");
            


            export(SimpleDownloadLogger.DTYPE_DOWNLOAD, w);
            export(SimpleDownloadLogger.DTYPE_PIN, w);
            export(SimpleDownloadLogger.DTYPE_PODCAST, w);
            export(SimpleDownloadLogger.DTYPE_VIEW, w);
            export(SimpleDownloadLogger.DTYPE_STREAM_START, w);
            export(SimpleDownloadLogger.DTYPE_WEBDAV, w);


            w.close();



        }

    }

    private void export(int dtypeDownload, Writer w) throws IOException {

        DownloadLoggerService dlls = new DownloadLoggerService();
        List<DownloadLogger> downloadList = dlls.getLog(dlls.INTERVAL_ALL, dtypeDownload, new ArrayList());

        for (DownloadLogger log : downloadList) {

            write(w,log);
        }

    }

    private void write(Writer w, DownloadLogger log) throws IOException {

        DateFormat formatter = new SimpleDateFormat();

            w.write(formatter.format(log.getDownloadDate())+";");
            w.write(getType(log.getDownloadtype())+";");
            w.write(String.valueOf(log.getUserName())+";");
            w.write(String.valueOf(log.getUserId())+";");
            w.write(String.valueOf(log.getImageNumber())+";");
            w.write(String.valueOf(log.getMediaName())+";");
            w.write(String.valueOf(log.getName())+";");
            w.write(String.valueOf(log.getIvid())+";");
            w.write(String.valueOf(log.getPin())+";");
            w.write(String.valueOf(log.getFormatx())+";");
            w.write(String.valueOf(log.getFormaty())+";");
            w.write(String.valueOf(log.getDns())+";");
            w.write(String.valueOf(log.getIp())+";");
            w.write(String.valueOf(log.getPayTransactionId())+";");
            w.write(String.valueOf(log.getDownloadDate().getTime())+";");
            w.write("\n");

    }

    private String getType(int downloadtype) {

        switch (downloadtype) {

            case SimpleDownloadLogger.DTYPE_DOWNLOAD: return "Download";
            case SimpleDownloadLogger.DTYPE_PIN: return "PIN-Download";
            case SimpleDownloadLogger.DTYPE_PODCAST: return "Podcast";
            case SimpleDownloadLogger.DTYPE_STREAM_START: return "Stream";
            case SimpleDownloadLogger.DTYPE_VIEW: return "Vorschau";
            case SimpleDownloadLogger.DTYPE_WEBDAV: return "Webdav";


        }

        return "nn";
    }
}
