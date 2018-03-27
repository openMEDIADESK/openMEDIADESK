package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.web.stack.WebStack;
import com.stumpner.mediadesk.web.mvc.exceptions.UndefinedWebStateException;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import net.stumpner.security.acl.*;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.security.acl.AclNotFoundException;

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
 * Date: 27.11.2007
 * Time: 19:21:53
 * To change this template use File | Settings | File Templates.
 */
public class AclEditController extends SimpleFormControllerMd {

    public AclEditController() {

        this.setCommandClass(Acl.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_EDITOR;
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WebStack webStack = new WebStack(httpServletRequest);
        webStack.push();

        if (httpServletRequest.getSession().getAttribute("accessObject")==null)
            throw new UndefinedWebStateException("No ACL-Object set.");
        AccessObject accessObject = (AccessObject)httpServletRequest.getSession().getAttribute("accessObject");
        //Folder folder = new Folder();
        //folder.getCategoryId();
        Acl acl = AclController.getAcl(accessObject);
        return acl;

        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        //SecurityGroups in das Request Objekt stellen:
        HashMap securityMap = new HashMap(3);
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            securityMap.put(new Integer(sg.getId()),sg);
        }
        httpServletRequest.setAttribute("securityMap",securityMap);
        httpServletRequest.setAttribute("aclEnabled",AclController.isEnabled());

        this.setContentTemplateFile("/admin_acledit.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }


    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        Acl acl = (Acl)object;

        //Alle ACLs loeschen:
        HashMap securityMap = new HashMap(3);
        UserService userService = new UserService();
        Iterator securityGroups = userService.getSecurityGroupList().iterator();
        System.out.println("acl revoke");
        while (securityGroups.hasNext()) {
            SecurityGroup sg = (SecurityGroup)securityGroups.next();
            acl.removePermission(sg,new AclPermission(AclPermission.READ));
            acl.removePermission(sg,new AclPermission("view"));
            acl.removePermission(sg,new AclPermission("write"));
            System.out.println("removed= "+sg.getId());
            securityMap.put(new Integer(sg.getId()),sg);
        }

        System.out.println("acl save");
        String acls[] = httpServletRequest.getParameterValues("acl");
        if (acls!=null) {
            for (int p=0;p<acls.length;p++) {
                if (acls[p].length()>0) {
                    System.out.println("ACLS[p]= "+acls[p]);
                    String[] aclInfo = acls[p].split("\\.");
                    Integer sgId = new Integer(aclInfo[0]);
                    //SecurityGroup sg = (SecurityGroup)securityMap.get(new Integer(acls[p]));
                    SecurityGroup sg = (SecurityGroup)securityMap.get(sgId);
                    acl.addPermission(sg,new AclPermission(aclInfo[1].toLowerCase()));
                    //System.out.println("permission: "+aclInfo[1].toLowerCase()+" sg="+sg);
                    if (aclInfo[1].equalsIgnoreCase("READ")) { //Read beinhaltet auch das view recht
                        acl.addPermission(sg,new AclPermission("view"));
                    }
                    if (aclInfo[1].equalsIgnoreCase("WRITE")) { //Write beinhaltet auch das view und read recht
                        acl.addPermission(sg,new AclPermission("view"));
                        acl.addPermission(sg,new AclPermission("read"));
                    }
                }
            }
        }

        AccessObject accessObject = (AccessObject)httpServletRequest.getSession().getAttribute("accessObject");
        AclController.setAcl(accessObject,acl);
        String redirectTo = (String)httpServletRequest.getSession().getAttribute("redirectTo");
        httpServletRequest.getSession().removeAttribute("accessObject");
        httpServletRequest.getSession().removeAttribute("redirectTo");

        if (accessObject instanceof FolderMultiLang) {
            FolderMultiLang c = (FolderMultiLang)accessObject;
            //Public true/false setzen
            renewFolderPublicProtectedStatus(c);
            //Auf Vererbende ACLs pr�fen
            inheritAclToChildsRekursive(acl, c);
        }

        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(redirectTo));
        return null;

        //return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void inheritAclToChildsRekursive(Acl acl, FolderMultiLang c) {

            FolderService folderService = new FolderService();
            List<FolderMultiLang> list = folderService.getFolderList(c.getCategoryId());
            for (FolderMultiLang ic : list) {
                if (ic.isInheritAcl()) {
                    System.out.println("inherit acl to cat: "+ic.getCategoryId()+" "+ic.getCatName());
                    AclController.setAcl(ic, acl);
                    try {
                        renewFolderPublicProtectedStatus(ic);
                    } catch (AclNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    inheritAclToChildsRekursive(acl, ic);
                }
            }

    }

    public static void renewFolderPublicProtectedStatus(FolderMultiLang c) throws AclNotFoundException, IOServiceException {

        UserService userService = new UserService();
        //Public true/false setzen
        boolean publicAcl = AclController.checkPermission(userService.getSecurityGroupVisitors(),new AclPermission(AclPermission.READ), c);
        if (c.isPublicAcl() && publicAcl) {
            //nichts tun
        } else {
            //�ndern
            c.setPublicAcl(publicAcl);
            FolderService folderService = new FolderService();
            folderService.save(c);
        }

        boolean protecedAcl = !AclController.checkPermission(userService.getSecurityGroupVisitors(),new AclPermission("view"), c);
        if (c.isProtectedAcl() && protecedAcl) {
            //nichts tun
        } else {
            //�ndern
            c.setProtectedAcl(protecedAcl);
            FolderService folderService = new FolderService();
            folderService.save(c);
        }

    }
}
