package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.category.Folder;
import com.stumpner.mediadesk.image.category.Folder;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.stack.WebStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import com.stumpner.mediadesk.core.service.MediaObjectService;

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
 * Date: 13.10.2005
 * Time: 20:21:36
 * To change this template use File | Settings | File Templates.
 */
public class FolderBreakupController extends SimpleFormControllerMd {

    public FolderBreakupController() {

        this.setCommandClass(Folder.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_EDITOR;
    }

    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {

        Folder c = (Folder)e.getTarget();

        if (getChilds(c)>0) {
            if (request.getParameter("cbx")==null) {
                e.reject("message.cbxreject","Bitte aktivieren sie die Checkbox");
            }
            if (request.getParameter("cbx")!=null) {
                if (!request.getParameter("cbx").equalsIgnoreCase("true")) {
                    e.reject("message.cbxreject","Bitte aktivieren sie die Checkbox");
                }
            }
        }
        super.onBindAndValidate(request, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WebStack webStack = new WebStack(httpServletRequest);
        webStack.push();

        CategoryService userService = new CategoryService();
        //Prüfen ob ein Parameter übergeben wurde
        if (httpServletRequest.getParameter("categoryid")==null) {
            httpServletRequest.setAttribute("categoryNotExists",true);
            return null;
            
        } else {

            try {
                int userId = Integer.parseInt(httpServletRequest.getParameter("categoryid"));
                Folder folder = null;
                try {
                    folder = (Folder)userService.getCategoryById(userId);
                } catch (ObjectNotFoundException e) {
                    folder = new Folder();
                    httpServletRequest.setAttribute("categoryNotExists",true);
                }

                return folder;

            } catch (NumberFormatException e) {
                //Wenn keine Nummer übergeben wurde
                httpServletRequest.setAttribute("categoryNotExists",true);
                return null;
            }
        }
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (httpServletRequest.getAttribute("categoryNotExists")!=null) {
            httpServletResponse.sendError(404);
        }

        Folder folder = (Folder)e.getTarget();


        //Prüfen ob diese Kategorie eine Benutzerkategorie ist:
        boolean isHomeCategory = false;
        UserService userService = new UserService();
        List userList = userService.getUserList();
        Iterator users = userList.iterator();
        while (users.hasNext()) {
            User user = (User)users.next();
            if (user.getHomeCategoryId()== folder.getCategoryId()) {
                isHomeCategory = true;
            }
        }
        if (Config.homeCategoryId== folder.getCategoryId()) {
            isHomeCategory=true;
        }
        int selectedImageListSize = MediaObjectService.getSelectedImageList(httpServletRequest.getSession()).size();
        if (isHomeCategory) {
            /*
            this.setContentTemplateFile("/message.jsp",httpServletRequest);
            model.put("headline","categorybreakup.headline");
            model.put("subheadline","categorybreakup.subheadline");
            model.put("text","categorybreakup.homecat");
            model.put("nextUrl","cat?id="+folder.getCategoryId());*/

            httpServletRequest.setAttribute("headline","categorybreakup.headline");
            httpServletRequest.setAttribute("subheadline","categorybreakup.subheadline");
            httpServletRequest.setAttribute("info","categorybreakup.homecat");
            httpServletRequest.setAttribute("infoArgument", folder.getCatTitle());
            if (selectedImageListSize>0) { httpServletRequest.setAttribute("attentionText","categorybreakup.attention"); }
            httpServletRequest.setAttribute("redirectTo","cat?id="+ folder.getCategoryId());

            return super.showForm(httpServletRequest,e,this.getFormView(),new HashMap());
        } else {
            /*
            this.setContentTemplateFile("/message_yes_no.jsp",httpServletRequest);
            model.put("headline","categorybreakup.headline");
            model.put("subheadline","categorybreakup.subheadline");
            model.put("text","categorybreakup.text");*/

            httpServletRequest.setAttribute("headline","categorybreakup.headline");
            httpServletRequest.setAttribute("subheadline","categorybreakup.subheadline");
            httpServletRequest.setAttribute("info","categorybreakup.text");
            httpServletRequest.setAttribute("infoArgument", folder.getCatTitle());
            if (selectedImageListSize>0) { System.out.println("selectedImageSize>0"); httpServletRequest.setAttribute("attentionText","categorybreakup.attention"); }
            if (getChilds(folder)>0) {
                httpServletRequest.setAttribute("useCbx",true);
                httpServletRequest.setAttribute("cbxText","categorybreakup.attentionsub");
            }
            httpServletRequest.setAttribute("redirectTo","");
            return super.showForm(httpServletRequest,e,this.getFormView(),new HashMap());
        }
        //return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        Folder folder = (Folder)o;
        if (httpServletRequest.getParameter("yes")!=null) {
            this.deleteFolder((Folder)o);
            httpServletResponse.sendRedirect("cat?id="+ folder.getParent());
        } else {
            httpServletResponse.sendRedirect("cat?id="+ folder.getCategoryId());
        }

        this.setContentTemplateFile("/message.jsp",httpServletRequest);


        /*
        WebStack webStack = new WebStack(httpServletRequest);
        String redirectTo = webStack.pop();
        if (redirectTo.contains("/cat") && redirectTo.contains("id="+folder.getCategoryId())) {
            //Nicht auf diese seite redirecten, da es sie nichtmehr gibt,
            // sondern auf die parent-Folder!
            httpServletResponse.sendRedirect("/index/cat?id="+folder.getParent());
        } else {
            httpServletResponse.sendRedirect(redirectTo);
        } */

        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void deleteFolder(Folder folder) {

        CategoryService folderService = new CategoryService();
        try {
            if (getChilds(folder)>0) {
                folderService.deleteRecursiv(folder.getCategoryId());
            } else {
                folderService.deleteById(folder.getCategoryId());
            }
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private int getChilds(Folder folder) {

        CategoryService categoryService = new CategoryService();
        int childs = categoryService.getCategoryList(folder.getCategoryId()).size();
        return childs;
    }

}