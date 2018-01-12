package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.stack.WebStack;
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
 * User: franzstumpner
 * Date: 25.04.2005
 * Time: 21:58:28
 * To change this template use File | Settings | File Templates.
 */
public class FolderBreakupController extends SimpleFormControllerMd {

    public FolderBreakupController() {

        this.setCommandClass(Folder.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WebStack webStack = new WebStack(httpServletRequest);
        webStack.push();

        FolderService userService = new FolderService();
        int userId = Integer.parseInt(httpServletRequest.getParameter("folderid"));
        Folder user = (Folder)userService.getFolderById(userId);

        return user;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        this.setContentTemplateFile("/message_yes_no.jsp",httpServletRequest);
        model.put("headline","folderbreakup.headline");
        model.put("subheadline","folderbreakup.subheadline");
        model.put("text","folderbreakup.text");

        httpServletRequest.setAttribute("headline","folderbreakup.headline");
        httpServletRequest.setAttribute("subheadline","folderbreakup.subheadline");
        httpServletRequest.setAttribute("info","folderbreakup.text");
        httpServletRequest.setAttribute("redirectTo","");

        return super.showForm(httpServletRequest,e,this.getFormView(),model);
        //return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        Folder folder = (Folder)o;
        if (httpServletRequest.getParameter("yes")!=null) {
            this.deleteFolder((Folder)o);
            httpServletResponse.sendRedirect(".");
        } else {
            httpServletResponse.sendRedirect("");
        }
        this.setContentTemplateFile("/message.jsp",httpServletRequest);


        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void deleteFolder(Folder folder) {

        FolderService folderService = new FolderService();
        try {
            folderService.deleteById(folder.getFolderId());
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
