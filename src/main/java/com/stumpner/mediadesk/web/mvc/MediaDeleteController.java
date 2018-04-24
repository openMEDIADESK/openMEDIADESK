package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Time: 21:23:24
 * To change this template use File | Settings | File Templates.
 */
public class MediaDeleteController extends SimpleFormControllerMd {

    public MediaDeleteController() {
        this.setCommandClass(String.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_EDITOR;
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        if (httpServletRequest.getParameter("redirectTo")!=null) {
            //Nach der Eingabe auf eine Seite redirecten
            httpServletRequest.getSession().setAttribute("redirectTo",httpServletRequest.getParameter("redirectTo"));
            //System.out.println("Redirect: "+httpServletRequest.getParameter("redirectTo"));
        }

        return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse httpServletResponse, BindException bindException, Map map) throws Exception {

        HttpSession session = request.getSession();
        List mediaList = new ArrayList();
        if (session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
            mediaList = (List)session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
            logger.debug("mediaDeleteController: mediaList to delete: "+mediaList+", size:"+mediaList.size());
        }
        request.setAttribute("deleteList",mediaList);
        request.setAttribute("deleteCount",new Integer(mediaList.size()));
        return super.showForm(request, httpServletResponse, bindException, map);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        if (httpServletRequest.getParameter("submit")!=null) {
            //löschen
                MediaService imageService = new MediaService();
                if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                    List mediaList = (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                    imageService.deleteMediaObjects(
                            (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)
                    );
                    httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                    logger.debug("imageDeleteController: save new empty list "+new LinkedList()+" to SessionVar: "+Resources.SESSIONVAR_SELECTED_IMAGES);
                    httpServletRequest.getSession().setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,new LinkedList());
                }
        } else {
            //nicht löschen...
        }

        String redirectUrl = (String)httpServletRequest.getSession().getAttribute("redirectTo");
        httpServletRequest.getSession().removeAttribute("redirectTo");
        String url = redirectUrl.replaceAll("mark","nofunc").replaceAll("unmark","nofunc");
        httpServletResponse.sendRedirect(url);
        return null;
    }
}
