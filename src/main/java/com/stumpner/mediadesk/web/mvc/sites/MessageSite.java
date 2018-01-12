package com.stumpner.mediadesk.web.mvc.sites;

import com.stumpner.mediadesk.web.mvc.SuSIDEBaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
 * Date: 22.01.2007
 * Time: 21:00:01
 * To change this template use File | Settings | File Templates.
 *
 * Eine Klasse die eine Message/Nachrichten-Box (Meldung) auf der Seite ausgibt
 *
 */
public class MessageSite {

    SuSIDEBaseController controller;
    Map model;

    public MessageSite(SuSIDEBaseController controller, Map model, HttpServletRequest request) {

        this.controller = controller;
        this.model = model;

        controller.setContentTemplateFile("message.jsp",request);
    }

    /*
        model.put("headline","loginforgot.headline");
        model.put("subheadline","loginforgot.subheadline");
        model.put("text","loginforgot.success");
        model.put("nextUrl","/login");
    */

    public void setHeadline(String string) {
        model.put("headline",string);
    }

    public void setSubHeadline(String string) {
        model.put("subheadline",string);
    }

    public void setText(String string) {
        model.put("text",string);
    }

    public void setNextUrl(String string) {
        model.put("nextUrl",string);
    }

    public void setHtmlCode(String html) {
        model.put("htmlCode",html);
    }

}
