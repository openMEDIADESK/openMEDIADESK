package com.stumpner.mediadesk.util;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.web.mvc.AclEditController;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.security.acl.AclNotFoundException;

import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.AclPermission;
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
 * User: franz.stumpner
 * Date: 16.10.2012
 * Time: 21:30:41
 * To change this template use File | Settings | File Templates.
 */
public class MaintenanceService {

    private String resetAclState = "";
    private boolean resetAclActive = false;
    private Thread resetAclThread = new Thread();

    private static MaintenanceService instance = null;

    /**
     * Default-Konstruktor, der nicht au�erhalb dieser Klasse
     * aufgerufen werden kann
     */
    private MaintenanceService() {}

    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zur�ck
     */
    public static MaintenanceService getInstance() {
        if (instance == null) {
            instance = new MaintenanceService();
        }
        return instance;
    }

    public boolean isResetAclActive() {
        return resetAclActive;
    }

    public synchronized boolean resetAclStart() {

        System.out.println("resetAclStart: "+ Config.instanceName);
        if (!isResetAclActive()) {
            System.out.println("creating Thread");
            resetAclThread = new Thread() {
                public void run() {
                    try {
                        resetAclState = "in Arbeit...";
                        resetAcl();
                        resetAclState = "Abgeschlossen um "+new Date();
                    } catch (IOServiceException e) {
                        e.printStackTrace();
                        resetAclState = e.getMessage();
                    }
                }
            };
            resetAclThread.start();
            return true;
        } else {
            return false;
        }
    }


    private void resetAcl() throws IOServiceException {

        System.out.println("resetacl");

        resetAclActive = true;

        UserService userService = new UserService();
        List securityGroupList = userService.getSecurityGroupList();

        FolderService folderService = new FolderService();
        try {
            setResetAcl(0, folderService, securityGroupList);
        } catch (AclNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        resetAclActive = false;

    }

    private void setResetAcl(int categoryId, FolderService folderService, List securityGroupList) throws IOServiceException, AclNotFoundException {
        List<FolderMultiLang> categoryList = folderService.getCategoryList(categoryId);
        for (FolderMultiLang cat : categoryList) {
            Acl acl = AclController.getAcl(cat);

            Iterator securityGroups = securityGroupList.iterator();
            while (securityGroups.hasNext()) {
                SecurityGroup sg = (SecurityGroup)securityGroups.next();
                acl.removePermission(sg,new AclPermission(AclPermission.READ));
                acl.removePermission(sg,new AclPermission("view"));
                acl.removePermission(sg,new AclPermission("write"));

                try {
                    acl.addPermission(sg,new AclPermission("view"));
                    if (sg.getId()>0) { //Bei allen nicht �ffentlich lesen + schreiben (zeigen + download)
                        acl.addPermission(sg,new AclPermission(AclPermission.READ));
                        acl.addPermission(sg,new AclPermission("write"));
                    }
                } catch (PermissionAlreadyExistException e) {
                    //no error handling or logging
                }
            }

            this.resetAclState = "In Arbeit: Ordner ACL "+cat.getCategoryId()+" wird zur�ckgesetzt...";

            System.out.println("In Arbeit: Ordner ACL "+cat.getCategoryId()+" wird zur�ckgesetzt...");

            AclController.setAcl(cat, acl);
            AclEditController.renewCategoryPublicProtectedStatus(cat);
            setResetAcl(cat.getCategoryId(), folderService, securityGroupList);
        }

    }

    public String getResetAclState() {
        return resetAclState;
    }

}
