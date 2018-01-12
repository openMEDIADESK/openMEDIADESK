package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclPermission;
import com.stumpner.mediadesk.image.pinpics.Pinpic;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.web.mvc.commandclass.CategorySelection;
import com.stumpner.mediadesk.web.mvc.commandclass.SelectableCategory;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.*;

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
 * Date: 11.06.2012
 * Time: 20:36:20
 * To change this template use File | Settings | File Templates.
 */
public class CategorySelectorController extends SimpleFormControllerMd {

    public CategorySelectorController() {
        this.setCommandClass(CategorySelection.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        //this.setValidator(new FolderEditValidator());
        //this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole = User.ROLE_PINMAKLER;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        CategoryService categoryService = new AclCategoryService(request);
        LngResolver lngResolver = new LngResolver();
        categoryService.setUsedLanguage(lngResolver.resolveLng(request));
        List<CategoryMultiLang> categoryTree = categoryService.getAllCategoryList();

        List<SelectableCategory> selectableCategoryList = getSelectableCategoryList(request.getParameter("type"), categoryTree, request);
        CategorySelection categorySelection = new CategorySelection();
        categorySelection.setCategoryList(selectableCategoryList);

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

        CategorySelection categorySelection = (CategorySelection)o;

        if (categorySelection.getType().equalsIgnoreCase("PIN")) {
            PinpicService ps = new PinpicService();
            Pinpic pin = (Pinpic)ps.getById(categorySelection.getId());
            request.setAttribute("targetname",pin.getPin());
            request.setAttribute("headline","categoryselector.headline");
            request.setAttribute("subheadline","categoryselector.subheadline");
        }

        if (categorySelection.getType().equalsIgnoreCase("ACL")) {
            UserService us = new UserService();
            SecurityGroup group = us.getSecurityGroupById(categorySelection.getId());
            String right = "Download";
            if (group.getId()==0) { right = "Zeige"; }
            request.setAttribute("targetname",group.getName()+" ("+right+"-Berechtigung)");
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
    private List<SelectableCategory> getSelectableCategoryList(String typeParameter, List<CategoryMultiLang> categoryTree, HttpServletRequest request) {

        List<SelectableCategory> selectableCategoryList = new ArrayList<SelectableCategory>();

        if (typeParameter.equalsIgnoreCase("PIN")) {

            for (CategoryMultiLang category : categoryTree) {
                SelectableCategory selCat = new SelectableCategory();
                selCat.setCategory(category);
                selCat.setSelected(false);
                selectableCategoryList.add(selCat);
            }
        }

        if (typeParameter.equalsIgnoreCase("ACL")) {
            for (CategoryMultiLang category : categoryTree) {
                SelectableCategory selCat = new SelectableCategory();
                selCat.setCategory(category);

                Acl acl = AclController.getAcl(category);
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(Integer.parseInt(request.getParameter("id")));

                AclPermission permission = new AclPermission(AclPermission.READ);
                if (securityGroup.getId()==0) { permission = new AclPermission("view"); }

                if (acl.checkPermission(securityGroup, permission)) {
                  selCat.setSelected(true);
                } else {
                    selCat.setSelected(false);
                }

                selectableCategoryList.add(selCat);
            }
        }

        return selectableCategoryList;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        CategoryService categoryService = new CategoryService();
        ImageVersionService mediaService = new ImageVersionService();
        CategorySelection categorySelection = (CategorySelection)o;
        for (SelectableCategory category : categorySelection.getCategoryList()) {
            if (category.isSelected()) {
                //System.out.println("Selected: "+category.getCategory().getCategoryId());

                /**
                 * Kategorie-Inhalte einem PIN zuweisen
                 */

                if (categorySelection.getType().equalsIgnoreCase("PIN")) {
                    PinpicService pinService = new PinpicService();
                    int pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
                    SimpleLoaderClass slc = new SimpleLoaderClass(category.getCategory().getCategoryId());
                    List mediaList = mediaService.getCategoryImages(slc);
                    Iterator mediaObjects = mediaList.iterator();
                    while (mediaObjects.hasNext()) {
                        ImageVersion mediaObject = (ImageVersion)mediaObjects.next();
                        pinService.addImageToPinpic(mediaObject.getIvid(),pinId);
                    }
                }
            }
            /**
             * ACL bearbeiten
             */

            if (categorySelection.getType().equalsIgnoreCase("ACL")) {
                Acl acl = AclController.getAcl(category.getCategory());
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(categorySelection.getId());
                AclPermission permission = new AclPermission(AclPermission.READ);
                if (securityGroup.getId()==0) { permission = new AclPermission("view"); }
                //Zugriff pr�fen
                if (acl.checkPermission(securityGroup, permission)) {
                    //Hat Zugriff
                    if (category.isSelected()) {
                        //nichts �ndern
                        System.out.println("nichts tun (hatte bereits zugriff)"+category.getCategory().getCategoryId());
                    } else {
                        //zugriff entfernen
                        System.out.println("Zugriff entfernen: "+category.getCategory().getCategoryId());
                        acl.removePermission(securityGroup, permission);
                        if (permission.getAction().equalsIgnoreCase("read")) {
                            acl.removePermission(securityGroup, new AclPermission("view"));
                            acl.removePermission(securityGroup, new AclPermission("write"));
                        }
                        AclController.setAcl(category.getCategory(),acl);
                    }
                } else {
                    //Hat nicht Zugriff
                    if (category.isSelected()) {
                        //zugriff geben
                        System.out.println("Zugriff geben (hatte nicht)"+category.getCategory().getCategoryId());
                        //acl.removePermission(securityGroup,  new AclPermission("view")); //eventuell existierende view berechtigungen entfernen
                        acl.addPermission(securityGroup, permission);
                        if (permission.getAction().equalsIgnoreCase("read")) {
                            if (!acl.checkPermission(securityGroup, new AclPermission("view"))) {
                                acl.addPermission(securityGroup, new AclPermission("view"));
                            }
                        }
                        AclController.setAcl(category.getCategory(),acl);
                    } else {
                        //nichts �ndern
                        System.out.println("Hatte keinen Zugriff, braucht auch keinen: "+category.getCategory().getCategoryId());
                    }
                }
            }
        }

        //Redirecten
        if (categorySelection.getType().equalsIgnoreCase("PIN")) {
            int pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("pinview?pinid="+pinId));
        }
        if (categorySelection.getType().equalsIgnoreCase("ACL")) {
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("usermanager?tab=group"));
        }
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
