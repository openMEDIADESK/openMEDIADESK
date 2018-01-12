package com.stumpner.mediadesk.web.mvc;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import com.stumpner.mediadesk.web.stack.WebStack;

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
 * Date: 11.01.2007
 * Time: 22:03:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractThumbnailAjaxController extends AbstractThumbnailViewController {

    protected String getMasterTemplate(HttpServletRequest request) {
        Logger logger = Logger.getLogger(AbstractThumbnailAjaxController.class);
        if (request.getParameter("ajax")!=null) {
            logger.debug("Ajaxrequest detected");
            return "ajaxrequest.jsp";
        } else {
            return super.getMasterTemplate(request);
        }

    }

    public void stackCall(WebStack webStack, HttpServletRequest request) {

        if (request.getParameter("ajax")!=null) {
            //ajaxrequest - keine stack registrieren...
            logger.debug("AbstractThumbnailAjaxController: AjaxRequest ["+webStack.getStacklistString()+"]");
        } else {
            //normaler request - normale abfolge...
            super.stackCall(webStack, request);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}
