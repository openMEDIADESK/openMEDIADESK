package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.FolderMultiLang;
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
        List<FolderMultiLang> categoryTree = folderService.getAllFolderList();

        List<TreeAclCommand.TreeAclCommandEntity> selectableCategoryList = getSelectableCategoryList(request.getParameter("type"), categoryTree, request);
        TreeAclCommand categorySelection = new TreeAclCommand();
        categorySelection.setFolderList(selectableCategoryList);

        categorySelection.setType(request.getParameter("type"));
        categorySelection.setId(Integer.parseInt(request.getParameter("id")));

        return categorySelection;
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

        TreeAclCommand categorySelection = (TreeAclCommand)o;

        if (categorySelection.getType().equalsIgnoreCase("ACL")) {
            UserService us = new UserService();
            SecurityGroup group = us.getSecurityGroupById(categorySelection.getId());
            String right = "Download";
            if (group.getId()==0) { right = "Zeige"; }
            request.setAttribute("targetname",group.getName());
            request.setAttribute("headline","categoryselector.aclheadline");
            request.setAttribute("subheadline","categoryselector.aclsubheadline");
        }

        return super.referenceData(request, o, errors);
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException e) throws Exception {



        return super.showForm(request, response, e);
    }

    /**
     * Diese Methode muss je nach typ die Selectable CategoryList zur�ckgeben
     * @param typeParameter
     * @return
     */
    private List<TreeAclCommand.TreeAclCommandEntity> getSelectableCategoryList(String typeParameter, List<FolderMultiLang> categoryTree, HttpServletRequest request) {

        List<TreeAclCommand.TreeAclCommandEntity> selectableCategoryList = new ArrayList<TreeAclCommand.TreeAclCommandEntity>();

        Stack<Integer> parentStack = new Stack<Integer>();
        parentStack.push(0);

        int lastParent = 0;
        int lastCategory = 0;
        int stufe = 0;

        if (typeParameter.equalsIgnoreCase("ACL")) {
            for (FolderMultiLang category : categoryTree) {

                if (lastParent!=category.getParent()) {

                    if (lastCategory==category.getParent()) {
                        stufe = stufe+1;
                    } else {
                        stufe = stufe-1;
                    }
                    lastParent = category.getParent();
                }

                lastCategory = category.getCategoryId();

                TreeAclCommand.TreeAclCommandEntity selCat = new TreeAclCommand.TreeAclCommandEntity();
                selCat.setFolder(category);
                selCat.setStufe(stufe);

                Acl acl = AclController.getAcl(category);
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(Integer.parseInt(request.getParameter("id")));

                //AclPermission permission = new AclPermission(AclPermission.READ);
                //if (securityGroup.getId()==0) { permission = new AclPermission("view"); }

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

                selCat.setPermissionString(permissionString);

                selectableCategoryList.add(selCat);
            }
        }

        return selectableCategoryList;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        FolderService folderService = new FolderService();
        ImageVersionService mediaService = new ImageVersionService();
        TreeAclCommand categorySelection = (TreeAclCommand)o;
        for (TreeAclCommand.TreeAclCommandEntity category : categorySelection.getFolderList()) {
            /**
             * ACL bearbeiten
             */

            if (categorySelection.getType().equalsIgnoreCase("ACL")) {
                Acl acl = AclController.getAcl(category.getFolder());
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(categorySelection.getId());

                if (category.getPermissionString().isEmpty()) {
                    //Jede Berechtigung wegnehmen:
                    acl.removePermission(securityGroup, new AclPermission("view"));
                    acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                    acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));
                } else {
                    //Berechtigung pr�fen/setzen

                    if (category.getPermissionString().equalsIgnoreCase("view")) {
                        //Nur View
                        acl.removePermission(securityGroup, new AclPermission("view"));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));

                        acl.addPermission(securityGroup, new AclPermission("view"));
                    }
                    if (category.getPermissionString().equalsIgnoreCase("read")) {
                        //View + Read
                        acl.removePermission(securityGroup, new AclPermission("view"));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));

                        acl.addPermission(securityGroup, new AclPermission("view"));
                        acl.addPermission(securityGroup, new AclPermission(AclPermission.READ));
                    }
                    if (category.getPermissionString().equalsIgnoreCase("write")) {
                        //View + Read + Write
                        acl.removePermission(securityGroup, new AclPermission("view"));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.removePermission(securityGroup, new AclPermission(AclPermission.WRITE));

                        acl.addPermission(securityGroup, new AclPermission("view"));
                        acl.addPermission(securityGroup, new AclPermission(AclPermission.READ));
                        acl.addPermission(securityGroup, new AclPermission(AclPermission.WRITE));
                    }
                }

                AclController.setAcl(category.getFolder(),acl);
            }
        }

        //Redirecten
        if (categorySelection.getType().equalsIgnoreCase("ACL")) {
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("usermanager?tab=group"));
        }
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
