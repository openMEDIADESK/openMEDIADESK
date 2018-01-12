package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.web.LocaleResolver;

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
 * Date: 28.09.2010
 * Time: 21:06:41
 * To change this template use File | Settings | File Templates.
 */
public class RedirectLngController extends AbstractController {

    private String redirectTo = "";

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String lngPrefix = "/index/";
        LocaleResolver localeResolver = new LocaleResolver();
        Locale locale = localeResolver.resolveLocale(httpServletRequest);

        if (locale.equals(Locale.GERMAN)) {
            lngPrefix = "/de/";
        }

        if (locale.equals(Locale.ENGLISH)) {
            lngPrefix = "/en/";
        }

        String qs = "";
        if (httpServletRequest.getQueryString()!=null) {
            qs = "?"+httpServletRequest.getQueryString();
        }

        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(lngPrefix+redirectTo+qs));

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
