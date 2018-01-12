package com.stumpner.mediadesk.util;

import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
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

    //private Thread etocThread = new Thread();
    private String etocState = "";
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

    /*
    public boolean etocActive() {
        return etocThread.getState()!=Thread.State.NEW && etocThread.getState()!=Thread.State.TERMINATED;
    } */

    public boolean isResetAclActive() {
        return resetAclActive;
    }

    /*
    public synchronized boolean etocStart(final int defaultUserId) {

        System.out.println("etocStart");
        if (!etocActive()) {
            System.out.println("creating Thread");
            etocThread = new Thread() {
                public void run() {
                    try {
                        etocState = "in Arbeit...";
                        migrateEventsToCategories(defaultUserId);
                        etocState = "Abgeschlossen um "+new Date();
                    } catch (IOServiceException e) {
                        e.printStackTrace();
                        etocState = e.getMessage();
                    }
                }
            };
            etocThread.start();
            return true;
        } else {
            return false;
        }
    } */

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

        CategoryService categoryService = new CategoryService();
        try {
            setResetAcl(0, categoryService, securityGroupList);
        } catch (AclNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        resetAclActive = false;

    }

    private void setResetAcl(int categoryId, CategoryService categoryService, List securityGroupList) throws IOServiceException, AclNotFoundException {
        List<CategoryMultiLang> categoryList = categoryService.getCategoryList(categoryId);
        for (CategoryMultiLang cat : categoryList) {
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
            setResetAcl(cat.getCategoryId(),categoryService, securityGroupList);
        }

    }

    public String getEtocState() {
        return etocState;
    }

    public String getResetAclState() {
        return resetAclState;
    }

    /**
     * Events werden in Kategorien konvertiert
     */
    private void migrateEventsToCategories(int defaultUserId) throws IOServiceException {

        System.out.println("migrateEventsToCategories wurde gestartet");
        FolderService folderService = new FolderService();
        List folderList = folderService.getFolderList(Integer.MAX_VALUE);
        Iterator folders = folderList.iterator();
        while (folders.hasNext()) {
            FolderMultiLang folder = (FolderMultiLang)folders.next();
            int dublicateCounter = 0;
            boolean retry = true;

            while (retry) {
                retry = false;
                this.etocState = "In Arbeit: "+folder.getFolderName()+" wird umgewandelt...";
                try {
                    migrateEventToCategory(folder, dublicateCounter, defaultUserId);
                } catch (DublicateEntry e) {
                    //Eine Kategorie mit diesem Namen gibt es bereits, z�hlen
                    dublicateCounter = dublicateCounter+1;
                    this.etocState = "In Arbeit: "+folder.getFolderName()+"[dublicateCounter="+dublicateCounter+"]";
                    System.out.println("Dublicate Name: "+folder.getFolderName()+" dublicateCounter = "+dublicateCounter);
                    if (dublicateCounter>=Integer.MAX_VALUE) { throw new IOServiceException("trying to migrate folder "+folder.getFolderId()+", giving up at "+dublicateCounter+" retry."); }
                    retry = true;
                }
            }
        }
    }

    private void migrateEventToCategory(FolderMultiLang folder, int dublicateCounter, int defaultUserId) throws IOServiceException {

        CategoryService categoryService = new CategoryService();
        FolderService folderService = new FolderService();

        CategoryMultiLang category = new CategoryMultiLang();
        category.setCatName(folder.getFolderName() + (dublicateCounter>0 ? dublicateCounter : ""));
        category.setCatTitle(folder.getFolderTitle());
        category.setCatTitleLng1(folder.getFolderTitleLng1());
        category.setCatTitleLng2(folder.getFolderTitleLng2());
        category.setDescription(folder.getFolderSubTitle());
        category.setDescriptionLng1(folder.getFolderSubTitleLng1());
        category.setDescriptionLng2(folder.getFolderSubTitleLng2());
        if (folder.getCreateUserId()!=-1) { category.setCreatorUserId(folder.getCreateUserId()); }
        else { category.setCreatorUserId(defaultUserId); }
        category.setCreateDate(folder.getCreateDate());
        category.setCategoryDate(folder.getFolderDate());
        category.setParent(0);
        category.setPrimaryIvid(folder.getPrimaryIvid());
        category.setDefaultview(Category.VIEW_THUMBNAILS);
        System.out.println("Prepare Folder "+folder.getFolderId());

        //Wenn die neue Kategorie nicht angelegt werden kann, gibt es eine IOServiceException
        categoryService.addCategory(category);

        System.out.println("Kategorie "+category.getCategoryId()+" erstellt");
        //inhalt �bernehmen
        ImageVersionService imageService = new ImageVersionService();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(folder.getFolderId());
        List imageList = imageService.getFolderImages(loaderClass);

        System.out.println("Medienobjekte des Events in die Kategorie "+category.getCategoryId()+" migrieren, [size="+imageList.size()+"]");
        
        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            ImageVersionMultiLang image = (ImageVersionMultiLang)images.next();
            categoryService.addImageToCategory(category.getCategoryId(), image);
            //System.out.println("Medienobjekt: "+category.getCategoryId()+" wird in die Kategorie aufgenommen.");
            //Image aus Folder entfernen
            folderService.deleteImageFromFolder(folder,image);
        }
        //acl �bernehmen
        Acl acl = AclController.getAcl(folder);
        AclController.setAcl(category,acl);
        //delete folder
        folderService.deleteById(folder.getFolderId());
    }

}
