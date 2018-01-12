package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.list.CustomListService;
import com.stumpner.mediadesk.core.database.sc.MultiLanguageService;
import com.stumpner.mediadesk.web.LngResolver;

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
 * Date: 07.09.2005
 * Time: 21:29:26
 * To change this template use File | Settings | File Templates.
 */
public class AdvancedSearchController extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (!Config.advancedSearchEnabled) {
            httpServletResponse.sendError(404,"Advanced Search not enabled");
            return null;
        }

        if (httpServletRequest.getSession().getAttribute("searchexpired")!=null) {
            httpServletRequest.getSession().removeAttribute("searchexpired");
            httpServletRequest.setAttribute("errorMessage","advancedsearch.error.expired");
        }

        LngResolver lngResolver = new LngResolver();
        int usedLanguage = lngResolver.resolveLng(httpServletRequest);
        CustomListService customListService = new CustomListService();
        customListService.setUsedLanguage(usedLanguage);

        httpServletRequest.setAttribute("customList1Caption", usedLanguage == MultiLanguageService.LNG1 ? Config.customList1Lng1 : Config.customList1Lng2);
        httpServletRequest.setAttribute("customList2Caption", usedLanguage == MultiLanguageService.LNG1 ? Config.customList2Lng1 : Config.customList2Lng2);
        httpServletRequest.setAttribute("customList3Caption", usedLanguage == MultiLanguageService.LNG1 ? Config.customList3Lng1 : Config.customList3Lng2);

        httpServletRequest.setAttribute("customList1",customListService.getCustomListEntries(1));
        httpServletRequest.setAttribute("customList2",customListService.getCustomListEntries(2));
        httpServletRequest.setAttribute("customList3",customListService.getCustomListEntries(3));

        this.setContentTemplateFile("advancedsearch.jsp",httpServletRequest);
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}

