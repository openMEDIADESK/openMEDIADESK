package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

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
 * Date: 13.03.2013
 * Time: 18:22:16
 * To change this template use File | Settings | File Templates.
 */
public class RestBaseServlet extends HttpServlet {

    public int getUriSectionCount(HttpServletRequest request) {
        String[] sections = request.getRequestURI().split("/");
        return sections.length;
    }

    public String getUriSection(int section, HttpServletRequest request) {

        String[] sections = request.getRequestURI().split("/");
        if (sections.length>section) {
            if (sections[section].indexOf(";")>=0) {
                //; herausl�sen um ;jessionid zu erm�glichen
                return sections[section].substring(0, sections[section].indexOf(";"));
            } else{
                return sections[section];
            }
        } else {
            return null;
        }
    }

    public Boolean getUriSectionBoolean(int section, HttpServletRequest request) {
        String sectionString = getUriSection(section,request);
        if (sectionString!=null) {
            return Boolean.valueOf(sectionString);
        } else {
            return null;
        }
    }

    public Integer getUriSectionInt(int section, HttpServletRequest request) {
        String sectionString = getUriSection(section,request);
        if (sectionString!=null) {
            try {
                return Integer.valueOf(sectionString);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public User getUser(HttpServletRequest httpServletRequest) {

        return WebHelper.getUser(httpServletRequest);
    }

    public static boolean isLoggedIn(HttpServletRequest httpServletRequest) {

        return WebHelper.isLoggedIn(httpServletRequest);
    }

    public static boolean hasMinimumRole(HttpServletRequest httpServletRequest,int minimumRole) {

        return WebHelper.hasMinimumRole(httpServletRequest, minimumRole);
    }

    public String getCaption(ImageVersionMultiLang mo) {

                if (Config.downloadImageFilename.equalsIgnoreCase("imageNumber")) {
                    return mo.getImageNumber();
                }
                if (Config.downloadImageFilename.equalsIgnoreCase("versionTitle")) {
                    return mo.getVersionTitle();
                }
                if (Config.downloadImageFilename.equalsIgnoreCase("versionName")) {
                    return mo.getVersionName();
                }

        return "";

    }

}
