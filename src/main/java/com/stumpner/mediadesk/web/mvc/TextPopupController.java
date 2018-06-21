package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.media.image.util.CustomTextService;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

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
 * Date: 24.05.2007
 * Time: 09:53:16
 * To change this template use File | Settings | File Templates.
 */
public class TextPopupController extends AbstractPageController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        Map model = new HashMap();
        String[] uriParts = request.getRequestURI().split("/");
        if (uriParts.length<2) {
            httpServletResponse.sendError(404,"Seite nicht gefunden");
            return null;
        } else {
            String text = uriParts[uriParts.length-1];
            //Prüfen ob auf statische Seiten verlinkt werden soll:
            if (toRedirectUrl(text,request)) {
                httpServletResponse.sendRedirect(
                        getRedirectUrl(text,CustomTextService.getLocale(request))
                );
                return null;
            } else {
                request.setAttribute("text", CustomTextService.getCustomText(text,request));
            }
        }

        return super.handleRequestInternal(request, httpServletResponse);

        //return new ModelAndView("/WEB-INF/template/"+ Config.templatePath+"textpopup.jsp","model",model);
    }

    private boolean toRedirectUrl(String text, HttpServletRequest request) {

        if (text.startsWith("contact") && CustomTextService.getCustomBool(text,request)) {
            return true;
        }

        return false;
    }

    private String getRedirectUrl(String code, Locale locale) {

        if (code.equalsIgnoreCase("contact") && Config.useContactForm) {
            return "../contact";
        }

        return "";

    }
}
