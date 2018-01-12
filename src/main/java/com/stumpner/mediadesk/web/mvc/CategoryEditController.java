package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.category.CategoryEditValidator;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.LngResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletOutputStream;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.security.acl.AclNotFoundException;

import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclPermission;

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
 * Date: 05.10.2005
 * Time: 23:25:52
 * To change this template use File | Settings | File Templates.
 */
public class CategoryEditController extends AbstractAutoFillController {

    public static int CATEGORY_NEW = 0;

    public CategoryEditController() {

        this.setCommandClass(CategoryMultiLang.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        this.setValidator(new CategoryEditValidator());
        this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_EDITOR;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        Category category = new CategoryMultiLang();

        if (!isFormSubmission(httpServletRequest)) {

            CategoryService folderService = new CategoryService();


            if (httpServletRequest.getParameter("categoryid")!=null) {
                //kategorie editieren
                int categoryId = Integer.parseInt(httpServletRequest.getParameter("categoryid"));
                category = folderService.getCategoryById(categoryId);
            } else {
                //kategorie erstellen
                if (httpServletRequest.getParameter("parentCat")!=null && !"".equalsIgnoreCase(httpServletRequest.getParameter("parentCat"))) {
                    category.setParent(Integer.parseInt(httpServletRequest.getParameter("parentCat")));
                } else {
                    User user = getUser(httpServletRequest);
                    if (user.getRole()==User.ROLE_HOME_EDITOR) {
                        category.setParent(user.getHomeCategoryId());
                    }
                    if (user.getRole()>=User.ROLE_MASTEREDITOR) {
                        category.setParent(0);
                    }
                }
                category.setCategoryId(CATEGORY_NEW);
            }

            //Hilfsweise das Objekt sofort in die Session speichern...
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute(getClass().getName()+".FORM."+this.getCommandName(),category);
            return category;

        } else {
            HttpSession session = httpServletRequest.getSession();
            category = (CategoryMultiLang)session.getAttribute(getClass().getName()+".FORM."+this.getCommandName());
            System.out.println("Form Submission - Returning category"+category);
            if (category==null) {
                category = new CategoryMultiLang();
                session.setAttribute(getClass().getName()+".FORM."+this.getCommandName(),category);
            }
            //return category;
            return category;
        }
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {

        servletRequestDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"),true));
        super.initBinder(httpServletRequest, servletRequestDataBinder);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBind(HttpServletRequest httpServletRequest, Object o) throws Exception {

        CategoryMultiLang c = (CategoryMultiLang)o;
        if (httpServletRequest.getParameter("inheritAcl")==null) {
            c.setInheritAcl(false);
        }

    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.validation.BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        httpServletRequest.setAttribute("langAutoFill",new Boolean(Config.langAutoFill));

        this.setContentTemplateFile("/admin_categoryedit.jsp",httpServletRequest);

        //todo: ACLs ins Command Object geben!
        /*
        UserService userService = new UserService();
        httpServletRequest.setAttribute("securityGroupList",userService.getSecurityGroupList());
        Category category = (Category)this.getCommand(httpServletRequest);
        Acl acl = AclController.getAcl(category);
        Map aclMap = new HashMap();
        Iterator acls = acl.iterator();
        while (acls.hasNext()) {
            AclEntry aclEntry = (AclEntry)acls.next();
            int serialId = aclEntry.getAclPrincipal().getAclObjectSerialId();
            aclMap.put(new Integer(serialId),aclEntry);
        }
        httpServletRequest.setAttribute("acl",aclMap);
        */

        Category category = (Category)this.getCommand(httpServletRequest);
        if (category==null) {
            //System.out.println("No Category Object!");
        }
        Acl acl = AclController.getAcl(category);
        httpServletRequest.setAttribute("aclInfo",acl);

        CategoryService categoryService = new CategoryService();
        List parentCategoryList = new ArrayList();
        try {
            if (category.getCategoryId()!=0) {
                parentCategoryList = categoryService.getParentCategoryList(category.getCategoryId());
            } else {
                parentCategoryList = categoryService.getParentCategoryList(category.getParent());
            }
            httpServletRequest.setAttribute("parentCategoryList",parentCategoryList);
        } catch (ObjectNotFoundException e2) {
            //irgendeine parent-kategory wurde nicht gefunden
            //todo: wenn eine Kategorie aufgelöst wird, müssen auch die unterkategorien aufgelöst werden
            e2.printStackTrace();
            System.err.println("getParentCategoryList liefert eine gelöschte Kategorie: "+e.getMessage());
            httpServletResponse.setStatus(404);
            return null;
        }

        //Security-Groups
        StringBuffer aclPermitText = new StringBuffer();
        HashMap securityMap = new HashMap(3);
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            securityMap.put(new Integer(sg.getId()),sg);
            if (acl.checkPermission(sg, new AclPermission("read"))) {
                if (aclPermitText.length()>0) { aclPermitText = aclPermitText.append(", "); }
                aclPermitText = aclPermitText.append(sg.getName());
            }
        }
        httpServletRequest.setAttribute("securityMap",securityMap);

        //User (acl)
        HashMap userMap = new HashMap();
        Iterator users = userService.getUserList().iterator();
        while (users.hasNext()) {
            User u = (User)users.next();
            userMap.put(new Integer(u.getUserId()),u);
            if (acl.checkPermission(u, new AclPermission("read"))) {
                if (aclPermitText.length()>0) { aclPermitText = aclPermitText.append(", "); }
                aclPermitText = aclPermitText.append(u.getUserName());
            }
        }
        httpServletRequest.setAttribute("userMap",userMap);
        httpServletRequest.setAttribute("aclPermitText", aclPermitText.toString());

        httpServletRequest.setAttribute("aclEnabled",AclController.isEnabled());

        ModelAndView mv =  super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.

        if (httpServletRequest.getParameter("jsondata")!=null) {
            //POST erwartet sich einen xmldata-response (für die Extjs-Form, Quelle: http://dev.sencha.com/deploy/ext-3.4.0/examples/form/xml-form.html)
            writeXmlDataResponse(httpServletResponse, e, "");
            return null;
        } else {
            return mv;
        }

    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, org.springframework.validation.BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        CategoryMultiLang category = (CategoryMultiLang)o;
        if (category.getCategoryId()==CATEGORY_NEW) {
            //kategorie creator setzen
            User user = (User)httpServletRequest.getSession().getAttribute("user");
        }

        IOServiceException ioServiceException = null;
        try {
            this.saveCategory((Category)o);
        } catch (IOServiceException ex) {
            ioServiceException = ex;
        }

        if (category.isInheritAcl()) {
            //Berechtigungen vom Übergeordneten übernehmen
            inheritAclFromParent(category);
        }

        if (category.isChildInheritAcl()) {
            //Berechtigungen in die Unterordner kopieren
            inheritAclToChilds(category);
        }

        //Sessionvariablen in der sortierung usw zwischengespeichert wird, leeren
        //AbstractImageLoaderController Zeile 82
        httpServletRequest.getSession().removeAttribute("sortBy"+CategoryIndexController.class.getName()+"."+category.getCategoryId());
        httpServletRequest.getSession().removeAttribute("orderBy"+CategoryIndexController.class.getName()+"."+category.getCategoryId());

        String redirectTo = null;

        //Prüfen auf ACL-Edit:
        if (httpServletRequest.getParameter("acl")!=null) {
            //Zur ACL-Seite redirectedn

            LngResolver lngResolver = new LngResolver();
            category.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
            httpServletRequest.getSession().setAttribute("accessObject",category);
            httpServletRequest.getSession().setAttribute("redirectTo","categoryedit?categoryid="+category.getCategoryId());

            redirectTo = httpServletResponse.encodeRedirectURL("acl");

        } else {
            //Zur Kategorie umleiten
            int redirectToCategoryId = category.getCategoryId();
            if (category.getCategoryId()==CATEGORY_NEW) {
                redirectToCategoryId = category.getParent();
            }

            redirectTo = httpServletResponse.encodeRedirectURL("c?id="+redirectToCategoryId);
        }

        if (httpServletRequest.getParameter("jsondata")!=null) {
            //POST erwartet sich einen xmldata-response (für die Extjs-Form, Quelle: http://dev.sencha.com/deploy/ext-3.4.0/examples/form/xml-form.html)
            if (ioServiceException!=null) {

                e.reject(ioServiceException.getMessage());
            }
            writeXmlDataResponse(httpServletResponse, e, redirectTo);

        } else {
            httpServletResponse.sendRedirect(redirectTo);
        }

        return null;
    }

    private void inheritAclToChilds(CategoryMultiLang category) {

        Acl acl = AclController.getAcl(category);
        inheritAclToChildsRekursive(category, acl);
        System.out.println("...done!");
    }

    private void inheritAclToChildsRekursive(CategoryMultiLang category, Acl acl) {

        CategoryService categoryService = new CategoryService();
        List<CategoryMultiLang> list = categoryService.getCategoryList(category.getCategoryId());
        for (CategoryMultiLang c : list) {
            System.out.println("working c "+c.getCategoryId());
            AclController.setAcl(c, acl);
            inheritAclToChildsRekursive(c, acl);
            try {
                AclEditController.renewCategoryPublicProtectedStatus(c);
            } catch (AclNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        
    }

    private void inheritAclFromParent(CategoryMultiLang category) throws ObjectNotFoundException, IOServiceException {

        CategoryService categoryService = new CategoryService();
        if (category.getParent()!=0) { //Von der Root Kategorie können keine ACls übernommen werden
            Category parentCategory = categoryService.getCategoryById(category.getParent());

            Acl acl = AclController.getAcl(parentCategory);
            AclController.setAcl(category, acl);
            try {
                AclEditController.renewCategoryPublicProtectedStatus(category);
            } catch (AclNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        //Todo: im Acl-Controller muss dann geprüft werden ob Childs auch die Acl erben/aktualisieren müssen
        //To change body of created methods use File | Settings | File Templates.
    }

    private void writeXmlDataResponse(HttpServletResponse response, BindException e, String redirectTo) {
        try {


            /*
            ServletOutputStream os = response.getOutputStream();
            os.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            os.println("<message success=\""+(e.hasErrors() ? "false" : "true")+"\">\n");

            if (e.hasErrors()) {
                os.println(" <errors>\n");
                os.println(" \n");
                Iterator fieldErrors = e.getFieldErrors().iterator();
                while (fieldErrors.hasNext()) {
                    FieldError fieldError = (FieldError)fieldErrors.next();
                    os.println("  <field>\n");
                    os.println("   <id>"+fieldError.getField()+"</id>\n");
                    os.println("   <msg><![CDATA[\n Invalid Field \n]]></msg>\n");
                    os.println("  </field>\n");                                        
                }
                os.println(" </errors>\n");
            }
            os.println("</message>\n");
            */


            //JSON
            ServletOutputStream os = response.getOutputStream();
            os.println("{\n");
            os.println("  success: "+(e.hasErrors() ? "false" : "true")+", \n");
            os.println("  redirectTo: \""+redirectTo+"\", \n");

            if (e.hasErrors()) {
                os.println(" errors: { \n");
                os.println(" \n");
                    os.println("   global : \""+e.getGlobalError().getCode()+"\"" + (e.getFieldErrors().size()>0 ? ", ": "") +"\n");
                Iterator fieldErrors = e.getFieldErrors().iterator();
                while (fieldErrors.hasNext()) {
                    FieldError fieldError = (FieldError)fieldErrors.next();
                    os.println("   "+fieldError.getField()+": \"Fehler im Feld\""+(fieldErrors.hasNext() ? ",":"")+"\n");            
                }
                os.println("  }\n");
            }
            os.println("}\n");

        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void saveCategory(Category category) throws IOServiceException {

        CategoryService categoryService = new CategoryService();

        if (category.getCatTitle().equalsIgnoreCase("")) {
            //Kategorietitel auf den Kategorienamen setzen, wenn kein titel eingegeben wurde!
            category.setCatTitle(category.getCatName());
        }

        if (category.getCategoryId()==CategoryEditController.CATEGORY_NEW) {
            categoryService.addCategory(category);
            //TODO: ACL von Elternkategorie kopieren:
        } else {
            categoryService.save(category);
        }

    }


    void doNameAsTitle(Object o) {
        CategoryMultiLang category = (CategoryMultiLang)o;

        //Wenn kein Name angegeben wurde, den Titel LNG1 als Name verwenden
        if (category.getCatName().length()==0 && category.getCatTitleLng1().length()>0) {
            category.setCatName(category.getCatTitleLng1());
        }

        category.setCatTitleLng1(
                doAutoFillField(
                        category.getCatTitleLng1(),
                        category.getCatName(),""
                )
        );
        category.setCatTitleLng2(
                doAutoFillField(
                        category.getCatTitleLng2(),
                        category.getCatName(),""
                )
        );
    }

    void doAutoFill(Object o) {
        CategoryMultiLang category = (CategoryMultiLang)o;
        category.setCatTitleLng1(
                doAutoFillField(category.getCatTitleLng1(),
                        category.getCatTitleLng2(),
                        category.getCatName())
        );
        category.setCatTitleLng2(
                doAutoFillField(category.getCatTitleLng2(),
                        category.getCatTitle(),
                        category.getCatName())
        );
        category.setDescriptionLng1(
                doAutoFillField(category.getDescriptionLng1(),
                        category.getDescriptionLng2(),"")
        );
        category.setDescriptionLng2(
                doAutoFillField(category.getDescriptionLng2(),
                        category.getDescriptionLng1(),"")
        );
    }

    protected Map referenceData(HttpServletRequest httpServletRequest) throws Exception {

        Map referenceData = new HashMap();

        CategoryService categoryService = new CategoryService();
        List<CategoryMultiLang> list = categoryService.getCategoryList(0);

        referenceData.put("parentList", list);

        return referenceData;
    }
}