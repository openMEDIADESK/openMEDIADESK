package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import java.io.File;

import com.stumpner.mediadesk.web.template.TemplateService;

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
 * Date: 12.07.2016
 * Time: 20:32:41
 * To change this template use File | Settings | File Templates.
 */
public class SplashPageEditController extends AbstractPageController {

    public SplashPageEditController() {

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        //Sucht im aktuellen Template im File pSplashpage.jsp nach Vorkommnisse von ${applicationScope.splashPageValueMap['feldname']}

        Set<String> fieldList = new LinkedHashSet<String>();

        TemplateService templateService = new TemplateService();
        String pSplashpageContent = templateService.getTemplateFileContent(null, new File(Config.webroot + "/WEB-INF/template/current/pSplashpage.jsp"));

        //System.out.println("pSplashpageContent: "+pSplashpageContent);

        //String txt="${applicationScope.splashPageValueMap['section1text']}";

        String txt = pSplashpageContent;

        String re1=".*?";	// Non-greedy match on filler
        String re2="(applicationScope\\.splashPageValueMap)";	// Fully Qualified Domain Name 1
        String re3=".*?";	// Non-greedy match on filler
        String re4="((?:[a-z][a-z0-9_]*))";	// Variable Name 1

        Pattern p = Pattern.compile(re1+re2+re3+re4,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(txt);
        System.out.println("m.find:");
        while (m.find())
        {
            String fqdn1=m.group(1);
            String var1=m.group(2);
            System.out.print("("+fqdn1.toString()+")"+"("+var1.toString()+")"+"\n");

            fieldList.add(var1.toString());
        }

        httpServletRequest.setAttribute("fieldList", fieldList);

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
