package com.stumpner.mediadesk.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
 * Date: 16.06.2010
 * Time: 21:32:02
 * To change this template use File | Settings | File Templates.
 */
public class MessageViewController extends AbstractPageController {

    protected Map getModel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
                request.setAttribute("headline",session.getAttribute("headline"));
                request.setAttribute("nextUrl",session.getAttribute("nextUrl"));
                request.setAttribute("text",session.getAttribute("text"));
                request.setAttribute("subheadline",session.getAttribute("subheadline"));
                request.setAttribute("subheadlineArgs", session.getAttribute("subheadlineArgs"));

                request.removeAttribute("headline");
                request.removeAttribute("nextUrl");
                request.removeAttribute("text");
                request.removeAttribute("subheadline");
                request.removeAttribute("subheadlineArgs");

        return super.getModel(request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
