package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.util.WebFileUploadBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

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
 * Date: 24.01.2007
 * Time: 17:01:32
 * To change this template use File | Settings | File Templates.
 */
public class FaviconUploadController extends FileUploadController {

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException, Map map) throws Exception {
        return super.showForm(httpServletRequest, httpServletResponse, bindException, map);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected String getDestinationPath(String originalFilename, WebFileUploadBean upload) {

        return Config.imageStorePath+ File.separator+"favicon.ico";

    }

    protected String getTextHeadline() {
        return "set.web.favicon.headline";
    }

    protected String getTextSubheadline() {
        return "set.web.favicon.subheadline";
    }

    protected String getTextInfo() {
        return "set.web.favicon.info";
    }

    protected String getNextUrl() {
        return "setweb";
    }

    protected String getTextSuccess() {
        return "set.web.favicon.success";
    }

    protected String getHtmlCode() {
        return "<p><img src=\"/favicon.ico\"/></p>";
    }

}
