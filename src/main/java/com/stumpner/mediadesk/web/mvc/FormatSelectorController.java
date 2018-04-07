package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.web.mvc.commandclass.FormatSelector;
import com.stumpner.mediadesk.web.mvc.exceptions.UndefinedWebStateException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.List;
import java.awt.*;

import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;
import com.stumpner.mediadesk.media.Format;
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
 * Date: 04.01.2008
 * Time: 10:03:59
 * To change this template use File | Settings | File Templates.
 */
public class FormatSelectorController extends SimpleFormControllerMd {


    public FormatSelectorController() {

        this.setCommandClass(FormatSelector.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;

    }


    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        FormatSelector formatSelector = (FormatSelector)httpServletRequest.getSession().getAttribute("formatSelector");
        if (formatSelector==null) throw new UndefinedWebStateException("No FormatSelector definied in Session-Scope.");

        //System.out.println("Denied List: "+formatSelector.getDeniedList().size());

        //Formate aus Config holen:
            //Nur berechtigte Formate
            List availableFormats = new ArrayList();
            Iterator formats = Config.downloadRes.iterator();
            while (formats.hasNext()) {
                Format format = (Format)formats.next();
                AclControllerContext aclCtx = AclContextFactory.getAclContext(httpServletRequest);
                if (aclCtx.checkPermission(new AclPermission("read"),format)) {
                    availableFormats.add(format);
                }
            }
            formatSelector.setAvailableFormatList(availableFormats);

            if (availableFormats.size()==0) {
                //Wenn der Benutzer f�r keine Download-Aufl�sungen berechtigt ist, dann zum Download verweigern
                formatSelector.getDeniedList().addAll(formatSelector.getDownloadList());
                formatSelector.setDownloadList(new LinkedList());
                formatSelector.setAllFormatsDenied(true);
            }

        
        return formatSelector;

        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        this.setContentTemplateFile("/formatselector.jsp",httpServletRequest);

        return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }


    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        FormatSelector formatSelector = (FormatSelector)object;
        Iterator images = formatSelector.getDownloadList().iterator();
        while (images.hasNext()) {
            MediaObject image = (MediaObject)images.next();
            Rectangle rectangle = formatSelector.getFormat(image);
        }

        httpServletResponse.sendRedirect(
            httpServletResponse.encodeRedirectURL("download?download=selectedMedia&format=AArr3345gdfsgojljoiixvbfdgts976356tdfgsbfxftz45"));
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
