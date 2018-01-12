package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.web.LngResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.exceptions.PermissionAlreadyExistException;

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
 * Time: 20:57:34
 * To change this template use File | Settings | File Templates.
 */
public class FolderEditController extends AbstractAutoFillController {

    public static int FOLDER_NEW = 0;

    public FolderEditController() {

        this.setCommandClass(FolderMultiLang.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        this.setValidator(new FolderEditValidator());
        this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {


        FolderService folderService = new FolderService();
        FolderMultiLang folder = new FolderMultiLang();

        if (httpServletRequest.getParameter("folderid")!=null) {
            //folder editieren
            int folderId = Integer.parseInt(httpServletRequest.getParameter("folderid"));
            folder = (FolderMultiLang)folderService.getFolderById(folderId);
        } else {
            //folder erstellen
            folder.setFolderId(FOLDER_NEW);
        }

        return folder;
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {

        servletRequestDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"),true));
        httpServletRequest.setCharacterEncoding("UTF-8");
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.validation.BindException e) throws Exception {

        httpServletRequest.setAttribute("langAutoFill",new Boolean(Config.langAutoFill));

        this.setContentTemplateFile("/admin_folderedit.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.

    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, org.springframework.validation.BindException e) throws Exception {

        Folder folder = (Folder)o;
        if (((Folder)o).getFolderId()==FOLDER_NEW) {
            //folder creator setzen
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            ((Folder)o).setCreateUserId(user.getUserId());
        } else {
            //bei geänderter sortierung: session-sortierung entfernen
            FolderService folderService = new FolderService();
            Folder folderOldData = folderService.getFolderById(folder.getFolderId());
            if (folderOldData.getSortBy()!=folder.getSortBy() ||
                    folderOldData.getOrderBy()!=folder.getOrderBy()) {
                resetSessionSort(httpServletRequest, folder);
            }
        }
        this.saveFolder((Folder)o);

        //Prüfen auf ACL-Edit:
        if (httpServletRequest.getParameter("acl")!=null) {
            //Zur ACL-Seite redirectedn
            //FolderMultiLang folder = (FolderMultiLang)o;
            LngResolver lngResolver = new LngResolver();
            //folder.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
            httpServletRequest.getSession().setAttribute("accessObject",folder);
            httpServletRequest.getSession().setAttribute("redirectTo","folderedit?folderid="+folder.getFolderId());
            httpServletResponse.sendRedirect("acl");
        } else {
            httpServletResponse.sendRedirect("folder?id="+folder.getFolderId());
        }

        /*WebStack webStack = new WebStack(httpServletRequest);
        String redirectTo = webStack.pop();
        httpServletResponse.sendRedirect(redirectTo);*/
        /*
        if (redirectTo.endsWith("/index/folder?id="+folder.getFolderId())) {
            //Nicht auf diese seite redirecten, da es sie nichtmehr gibt,
            // sondern auf die parent-Category!
            //httpServletResponse.sendRedirect("/index/");
            httpServletResponse.sendRedirect(redirectTo);
        } else {
            httpServletResponse.sendRedirect(redirectTo);
        }
        /*
        this.setContentTemplateFile("/message_folderedit.jsp",httpServletRequest);
        return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
        */

        return null;
    }

    private void resetSessionSort(HttpServletRequest request, Folder folder) {
        //sortBysnapdox.web.mvc.FolderViewController.2
        //sortBysnapdox.web.mvc.FolderViewController.2
        String sortkey = "sortBy"+FolderViewController.class.getName()+"."+folder.getFolderId();
        request.getSession().removeAttribute(sortkey);
        String orderby = "orderBy"+FolderViewController.class.getName()+"."+folder.getFolderId();
        request.getSession().removeAttribute(orderby);
    }

    private void saveFolder(Folder folder) {

        FolderService folderService = new FolderService();

        if (folder.getFolderTitle().equalsIgnoreCase("")) {
            //Wenn kein Folder-Titel angegeben wurde, den Folder-Namen als Titel abspeichern
            folder.setFolderTitle(folder.getFolderName());
        }

        if (folder.getFolderId()==FolderEditController.FOLDER_NEW) {
            try {
                folderService.addFolder(folder);

                //Standard-ACL (alle Berechtigungen) setzen:
                    Acl acl = new Acl();
                    //Default ACL "zusammenbauen":
                    UserService userService = new UserService();
                    Iterator securityGroups = userService.getSecurityGroupList().iterator();
                    while (securityGroups.hasNext()) {
                        SecurityGroup securityGroup = (SecurityGroup)securityGroups.next();
                        try {
                            acl.addPermission(securityGroup,new AclPermission("read"));
                        } catch (PermissionAlreadyExistException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    AclController.setAcl(folder,acl);

            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            try {
                folderService.save(folder);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    void doNameAsTitle(Object o) {
        FolderMultiLang folder = (FolderMultiLang)o;
        folder.setFolderTitleLng1(
                doAutoFillField(folder.getFolderTitleLng1(),
                        folder.getFolderName(),"")
        );
        folder.setFolderTitleLng2(
                doAutoFillField(folder.getFolderTitleLng2(),
                        folder.getFolderName(),"")
        );
    }

    void doAutoFill(Object o) {
        FolderMultiLang folder = (FolderMultiLang)o;

        folder.setFolderTitleLng1(
                doAutoFillField(folder.getFolderTitleLng1(),
                        folder.getFolderTitleLng2(),folder.getFolderName()
                        )
        );
        folder.setFolderTitleLng2(
                doAutoFillField(folder.getFolderTitleLng2(),
                        folder.getFolderTitleLng1(),
                        folder.getFolderName()
                        )
        );

        folder.setFolderSubTitleLng1(
                doAutoFillField(folder.getFolderSubTitleLng1(),
                        folder.getFolderSubTitleLng2(),""
                        )
        );
        folder.setFolderSubTitleLng2(
                doAutoFillField(folder.getFolderSubTitleLng2(),
                        folder.getFolderSubTitleLng1(),""
                        )
        );

    }
}
