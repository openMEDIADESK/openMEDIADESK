package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.folder.FolderMultiLang;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.AclPermission;
import com.stumpner.mediadesk.web.mvc.commandclass.TreeAclCommand;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.core.database.sc.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.springframework.validation.Errors;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

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
 * Date: 19.07.2016
 * Time: 21:37:25
 * To change this template use File | Settings | File Templates.
 */
public class TreeAclController extends SimpleFormControllerMd {

    public TreeAclController() {

        this.setCommandClass(TreeAclCommand.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole = User.ROLE_PINMAKLER;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        FolderService folderService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(request));
        List<FolderMultiLang> folderTree = folderService.getAllFolderList();

        List<TreeAclCommand.TreeAclCommandEntity> selectableFolderList = getSelectableFolderList(request.getParameter("type"), folderTree, request);
        TreeAclCommand folderSelection = new TreeAclCommand();
        folderSelection.setFolderList(selectableFolderList);

        folderSelection.setType(request.getParameter("type"));
        folderSelection.setId(Integer.parseInt(request.getParameter("id")));

        return folderSelection;
    }

    protected boolean isUserPermitted(HttpServletRequest request) {

        if (request.getParameter("type").equalsIgnoreCase("ACL")) {
            if (getUser(request).getRole()>=User.ROLE_MASTEREDITOR) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.isUserPermitted(request);
        }
    }

    protected Map referenceData(HttpServletRequest request, Object o, Errors errors) throws Exception {

        TreeAclCommand folderSelection = (TreeAclCommand)o;

        if (folderSelection.getType().equalsIgnoreCase("ACL")) {
            UserService us = new UserService();
            SecurityGroup group = us.getSecurityGroupById(folderSelection.getId());
            String right = "Download";
            if (group.getId()==0) { right = "Zeige"; }
            request.setAttribute("targetname",group.getName());
            request.setAttribute("headline","folderselector.aclheadline");
            request.setAttribute("subheadline","folderselector.aclsubheadline");
        }

        return super.referenceData(request, o, errors);
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException e) throws Exception {



        return super.showForm(request, response, e);
    }

    /**
     * Diese Methode muss je nach typ die Selectable FolderList zur�ckgeben
     * @param typeParameter
     * @return
     */
    private List<TreeAclCommand.TreeAclCommandEntity> getSelectableFolderList(String typeParameter, List<FolderMultiLang> folderTree, HttpServletRequest request) {

        List<TreeAclCommand.TreeAclCommandEntity> selectableFolderList = new ArrayList<TreeAclCommand.TreeAclCommandEntity>();

        Stack<Integer> parentStack = new Stack<Integer>();
        parentStack.push(0);

        int lastParent = 0;
        int lastFolder = 0;
        int stufe = 0;

        if (typeParameter.equalsIgnoreCase("ACL")) {
            for (FolderMultiLang folder : folderTree) {

                if (lastParent!=folder.getParent()) {

                    if (lastFolder==folder.getParent()) {
                        stufe = stufe+1;
                    } else {
                        stufe = stufe-1;
                    }
                    lastParent = folder.getParent();
                }

                lastFolder = folder.getFolderId();

                TreeAclCommand.TreeAclCommandEntity selFolder = new TreeAclCommand.TreeAclCommandEntity();
                selFolder.setFolder(folder);
                selFolder.setStufe(stufe);

                Acl acl = AclController.getAcl(folder);
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(Integer.parseInt(request.getParameter("id")));

                String permissionString = ""; //nichts

                if (acl.checkPermission(securityGroup, new AclPermission("view"))) {
                    permissionString = "view";
                }
                if (acl.checkPermission(securityGroup, new AclPermission(AclPermission.READ))) {
                    permissionString = "read";
                }
                if (acl.checkPermission(securityGroup, new AclPermission(AclPermission.WRITE))) {
                    permissionString = "write";
                }

                selFolder.setPermissionString(permissionString);

                selectableFolderList.add(selFolder);
            }
        }

        return selectableFolderList;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        FolderService folderService = new FolderService();
        MediaService mediaService = new MediaService();
        TreeAclCommand folderSelection = (TreeAclCommand)o;
        for (TreeAclCommand.TreeAclCommandEntity folder : folderSelection.getFolderList()) {
            /**
             * ACL bearbeiten
             */

            if (folderSelection.getType().equalsIgnoreCase("ACL")) {
                Acl acl = AclController.getAcl(folder.getFolder());
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(folderSelection.getId());

                if (folder.getPermissionString().isEmpty()) {
                    //Jede Berechtigung wegnehmen:
                    acl.removePermission(securityGroup, new AclPermission("view"));
                    acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                    acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));
                } else {
                    //Berechtigung pr�fen/setzen

                    if (folder.getPermissionString().equalsIgnoreCase("view")) {
                        //Nur View
                        acl.removePermission(securityGroup, new AclPermission("view"));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));

                        acl.addPermission(securityGroup, new AclPermission("view"));
                    }
                    if (folder.getPermissionString().equalsIgnoreCase("read")) {
                        //View + Read
                        acl.removePermission(securityGroup, new AclPermission("view"));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));

                        acl.addPermission(securityGroup, new AclPermission("view"));
                        acl.addPermission(securityGroup, new AclPermission(AclPermission.READ));
                    }
                    if (folder.getPermissionString().equalsIgnoreCase("write")) {
                        //View + Read + Write
                        acl.removePermission(securityGroup, new AclPermission("view"));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));

                        acl.addPermission(securityGroup, new AclPermission("view"));
                        acl.addPermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.addPermission(securityGroup, new AclPermission(AclPermission.WRITE));
                    }
                }

                AclController.setAcl(folder.getFolder(),acl);
            }
        }

        //Redirecten
        if (folderSelection.getType().equalsIgnoreCase("ACL")) {
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("usermanager?tab=group"));
        }
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
