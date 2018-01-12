package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.stats.DownloadLogger;
import com.stumpner.mediadesk.stats.XyMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang3.StringEscapeUtils;

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
 * Date: 22.03.2016
 * Time: 23:38:04
 * To change this template use File | Settings | File Templates.
 *
 * //vorschlag /api/rest/stat/{downloadtype:all|download|pin|podcast|stream|webdav}/{start:2016-01-01|YEAR2016|}/{ende:2016-05-01}
 * /api/rest/stat/interval/list/{interval:1|2|..}/{downloadtype:1|}
 *   1   2     3     4       5        6                 7
 *
 *  6 --> 0|1 = Jahr,
 *        2 = Monat
 *        3 = Woche
 *        4 = Tag
 *        5 = Stunde
 *
 *  7 --> 1 = Downloads,
 *        2 = PIN,
 *        3 = Podcasts
 *        4 = Stream start
 *        5 = Stream end
 *        6 = Webdav
 *
 * /api/rest/stat/interval/chart/{interval:1|2|..}/{downloadtype:1|}
 *
 * /api/rest/stat/<categoryid>/removeselected
 * /api/rest/stat/<categoryid>/deleteselected
 * /api/rest/stat/<categoryid>/insertselected
 */
public class StatisticRestApi extends RestBaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.

        int downloadType = 1; //Download
        List userList = new LinkedList();

        String slash4 = this.getUriSection(4, request);

        DownloadLoggerService dlls = new DownloadLoggerService();
        PrintWriter out = response.getWriter();

        if (slash4.equalsIgnoreCase("interval")) {

            String outputType = this.getUriSection(5, request);
            String intervalType = this.getUriSection(6, request);
            int interval = Integer.parseInt(intervalType);

                if (outputType.equalsIgnoreCase("list")) {

                    List<DownloadLogger> allLog = dlls.getLog(interval,downloadType,userList);
                    out.println("[");
                    for (DownloadLogger l : allLog) {

                        out.println(" {");
                        out.println("  \"ivid\" : "+l.getIvid()+",");
                        out.println("  \"downloaddate\" : "+l.getDownloadDate().getTime()+",");
                        out.println("  \"type\" : "+l.getDownloadtype()+",");
                        out.println("  \"formatx\" : "+l.getFormatx()+",");
                        out.println("  \"formaty\" : "+l.getFormaty()+",");
                        out.println("  \"medianumber\" : \""+ StringEscapeUtils.escapeJson(l.getImageNumber())+"\",");
                        out.println("  \"ip\" : \""+StringEscapeUtils.escapeJson(l.getIp())+"\",");
                        out.println("  \"medianame\" : \""+StringEscapeUtils.escapeJson(l.getMediaName())+"\",");
                        out.println("  \"name\" : \""+StringEscapeUtils.escapeJson(l.getName())+"\",");
                        out.println("  \"paytransactionid\" : \""+StringEscapeUtils.escapeJson(l.getPayTransactionId())+"\",");
                        out.println("  \"pin\" : \""+StringEscapeUtils.escapeJson(l.getPin())+"\",");
                        out.println("  \"pinid\" : \""+l.getPinid()+"\",");
                        out.println("  \"userid\" : \""+l.getUserId()+"\",");
                        out.println("  \"downloadlink\" : \"download?download=ivid&ivid="+l.getIvid()+"\",");
                        out.println("  \"username\" : \""+StringEscapeUtils.escapeJson(l.getUserName())+"\"");

                        if (allLog.indexOf(l)<allLog.size()-1) {
                            out.println(" },");
                        } else {
                            //Letztes Element
                            out.println(" }");
                        }

                    }
                    out.println("]");

                }

            if (outputType.equalsIgnoreCase("chart")) {

                List<XyMap> list = dlls.getAllDownloadedPics(interval,0,downloadType);

                //list = fillEmptyIntervalsAndLabels(list, interval);

                out.print("{\"data\" : ");

                out.print("[[");
                for (XyMap l : list) {

                    out.print(l.getY());

                    if (list.indexOf(l)<list.size()-1) {
                        out.print(",");
                    } else {
                        //Letztes Element
                        //out.println("");
                    }
                }

                /*
                out.print("],[");

                for (XyMap l : list) {

                    out.print(l.getY());

                    if (list.indexOf(l)<list.size()-1) {
                        out.print(",");
                    } else {
                        //Letztes Element
                        //out.println("");
                    }
                } */
                out.print("]]");

                out.print(",");
                out.print("\"labels\": [");

                for (XyMap l : list) {

                    out.print("\""+l.getX()+"\"");

                    if (list.indexOf(l)<list.size()-1) {
                        out.print(",");
                    } else {
                        //Letztes Element
                        //out.println("");
                    }
                }

                out.print("],");
                out.print("\"series\": []");

                out.print("}");

            }
            
        } else {



        }

    }
}
