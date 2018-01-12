package com.stumpner.mediadesk.core.database.sc;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.stats.*;
import com.stumpner.mediadesk.image.ImageVersion;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;
import java.sql.SQLException;
import java.awt.*;

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
 * Time: 22:02:26
 * To change this template use File | Settings | File Templates.
 */
public class DownloadLoggerService {

    public final int INTERVAL_ALL = 0;
    public final int INTERVAL_YEAR = 1;
    public final int INTERVAL_MONTH = 2;
    public final int INTERVAL_WEEK = 3;
    public final int INTERVAL_DAY = 4;
    public final int INTERVAL_HOUR = 5;

    /**
     *
     * @param userId userid
     * @param ivid ivid
     * @param rect rect
     * @param type SimpleDownloadLogger.Download_Type
     * @param request httpservletrequest
     * @param bytes bytes
     */
    public void log(int userId, int ivid, Rectangle rect, int type, HttpServletRequest request, int bytes) {

        log(userId, ivid, rect, type, request, bytes, 0);

    }

    public void log(SimpleDownloadLogger logger) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.insert("logDownload",logger);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public List getLog(int interval, int downloadType, List userList) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List<DownloadLogger> log = new LinkedList();
        AllPicFilter allPicFilter = getIntervalFilter(interval);
        allPicFilter.setUserList(userList);
        allPicFilter.setDownloadType(downloadType);

        try {
            log = smc.queryForList("getDownloadLog",allPicFilter);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return log;
    }

    /**
     * @deprecated use getLog
     * @return
     */
    public List getLogMonth() {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List log = new LinkedList();
        StatFilter statFilter = new StatFilter();
        statFilter.setToDate(new Date());

        try {
            log = smc.queryForList("getDownloadLogMonth",statFilter);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return log;

    }

    public List getAllDownloadedPics(int interval, int userId, int downloadType) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List<XyMap> log = new LinkedList<XyMap>();
        AllPicFilter statFilter = getIntervalFilter(interval);
        statFilter.setUserId(userId);
        statFilter.setDownloadType(downloadType);

        try {
            log = smc.queryForList("getAllDownloadedPics",statFilter);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List<XyMap> list = new LinkedList<XyMap>();

        int stepx=1;
        int stepmax = getMaxStepsFromIntervatl(interval);
        for (XyMap l : log) {

            int xint = Integer.parseInt(l.getX());
            //if (stepx==0) { stepx = xint; }

            while (stepx<xint) {
                //Intervalle mit leeren Daten hinzuf�gen
                XyMap xy = new XyMap();
                xy.setX(String.valueOf(stepx));
                xy.setY(0);
                list.add(xy);
                stepx = stepx+1;
            }

                            System.out.println(l.getDownloaddate());
            list.add(l);
        }

        /*
        //DateFormatter
        String pattern = "yyyy-MM-dd";
        switch (interval) {
            case INTERVAL_ALL:
            case INTERVAL_YEAR: pattern = "MMM"; break;
            case INTERVAL_MONTH: pattern = "dd"; break;
            case INTERVAL_WEEK: pattern = "EEE"; break;
            case INTERVAL_DAY: pattern = "HH:00"; break;
            case INTERVAL_HOUR: pattern = "HH:mm"; break;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        //X- Achse noch benennen
        for (XyMap l : list) {
            l.setX(df.format(l.getDownloaddate()));
        } */

        //return correctLog(log,interval);
        return list;
    }

    private int getMaxStepsFromIntervatl(int interval) {

        switch (interval) {
            case INTERVAL_ALL: return 12;
            case INTERVAL_YEAR: return 12;
            case INTERVAL_MONTH: return 31;
            case INTERVAL_WEEK: return 12;
            case INTERVAL_DAY: return 24;
            case INTERVAL_HOUR: return 60;
        }

        return 0;

    }

    private AllPicFilter getIntervalFilter(int interval) {

        AllPicFilter statFilter = new AllPicFilter();
        switch (interval) {
            case INTERVAL_ALL:
                statFilter.setGroup("MONTH");
                statFilter.setInterval("1 YEAR");
                break;
            case INTERVAL_YEAR:
                statFilter.setGroup("MONTH");
                statFilter.setInterval("1 YEAR");
                break;
            case INTERVAL_MONTH:
                statFilter.setGroup("DAYOFMONTH");
                statFilter.setInterval("1 MONTH");
                break;
            case INTERVAL_WEEK:
                statFilter.setGroup("DAYOFWEEK");
                statFilter.setInterval("7 DAY");
                break;
            case INTERVAL_DAY:
                statFilter.setGroup("HOUR");
                statFilter.setInterval("1 DAY");
                break;
            case INTERVAL_HOUR:
                statFilter.setGroup("MINUTE");
                statFilter.setInterval("1 HOUR");
                break;
        }

        return statFilter;
    }

    private List correctLog(List log, int interval) {

        List correctedList = new ArrayList();
        Iterator logIt = log.iterator();

        if (interval==INTERVAL_MONTH) {

            while (logIt.hasNext()) {
                XyMap xy = (XyMap)logIt.next();
                String newValue = Integer.toString(Integer.parseInt(xy.getX())+1);
                xy.setX(newValue);
                correctedList.add(xy);
            }

        } else {
            correctedList = log;
        }

        return correctedList;

    }

    public void log(int userId, int ivid, Rectangle rect, int type, HttpServletRequest request, int bytes, int pinid) {

        log(userId, ivid, rect, type, request, bytes, pinid, "");
    }

    public void log(int userId, int ivid, Rectangle rect, int type, HttpServletRequest request, int bytes, int pinid, String payTransactionId) {

        SimpleDownloadLogger sdl = new SimpleDownloadLogger();
        sdl.setDownloadDate(new Date());
        sdl.setUserId(userId);
        sdl.setIvid(ivid);
        if (rect!=null) { sdl.setFormat(rect); }
        sdl.setDownloadtype(type);
        sdl.setBytes(bytes);
        sdl.setIp(request.getRemoteAddr());
        sdl.setDns(request.getRemoteHost());
        sdl.setPinid(pinid);
        sdl.setPayTransactionId(payTransactionId);

        ImageVersionService is = new ImageVersionService();
        ImageVersion media = is.getImageVersionById(ivid);
        sdl.setName(media.getVersionName());

        log(sdl);

    }
}
