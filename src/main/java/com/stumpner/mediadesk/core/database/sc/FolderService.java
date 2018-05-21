package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.FolderLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.HolderClass;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.CronService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.folder.*;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.usermanagement.User;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;
import net.stumpner.security.acl.exceptions.PermissionAlreadyExistException;
import net.stumpner.security.acl.*;

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
 * Time: 22:43:33
 * To change this template use File | Settings | File Templates.
 */
public class FolderService extends MultiLanguageService implements IServiceClass {

    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {
        return getFolderById(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Folder getFolderById(int id) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FolderMultiLang folder = new FolderMultiLang();
        try {
            folder = (FolderMultiLang)smc.queryForObject("getFolderById",new Integer(id));
            if (folder!=null) folder.setUsedLanguage(getUsedLanguage());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (folder==null) throw new ObjectNotFoundException("Ordner mit der ID "+id+" nicht gefunden");
        return folder;

    }

    public List getParentFolderList(int id) throws ObjectNotFoundException, IOServiceException {

        int recParentFolder = id;
        LinkedList recFolderList = new LinkedList();

        int whileLoopCounter = 0;
        //System.out.println("getParentFolderList");

        //Rekursiv nach übergeordneten Ordnern suchen:
        while (recParentFolder!=0) {
            FolderMultiLang recFolder = (FolderMultiLang)this.getFolderById(recParentFolder);
            recFolder.setUsedLanguage(getUsedLanguage());
            if (recFolder==null) throw new ObjectNotFoundException();
            recFolderList.addFirst(recFolder);
            recParentFolder=recFolder.getParent();
            whileLoopCounter = whileLoopCounter+1;
            if (whileLoopCounter>0) {
                //System.out.println("SF-DEBUG: whileLoopCounter="+whileLoopCounter+" (getParentFolderList) "+Config.instanceName);
            }
            if (whileLoopCounter>11) {
                System.err.println("getParentFolderList: maybe endless loop detected > 11 "+Config.instanceName+" "+new Date());
                break;
            }
        }

        return recFolderList;

    }

    public List getFolderSubTree(int parentId, int maxSubElements) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        List treeList = new ArrayList();

        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setSort(Config.categorySort);
        loaderClass.setId(parentId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getFolderSubTree",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Iterator folders = folderList.iterator();
        while (folders.hasNext()) {

            Folder folder = (Folder)folders.next();
            FolderTreeElement folderTreeElement = new FolderTreeElement(folder);
            if (folderTreeElement.getParent()==parentId) {


                Iterator subCats = folderList.iterator();
                int subElements = 0;
                while (subCats.hasNext()) {

                    Folder subCat = (Folder)subCats.next();
                    if (subCat.getParent()==folderTreeElement.getFolderId()) {
                        if (subElements<maxSubElements) {
                            folderTreeElement.getSubFolderList().add(new FolderTreeElement(subCat));
                        }
                        subElements++;
                    }
                }
                treeList.add(folderTreeElement);
            }
        }

        return treeList;

    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return getFolderByName(name);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Folder getFolderByName(String name) throws ObjectNotFoundException, IOServiceException {
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FolderMultiLang folder = new FolderMultiLang();
        try {
            folder = (FolderMultiLang)smc.queryForObject("getFolderByName",name);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (folder==null) throw new ObjectNotFoundException();
        folder.setUsedLanguage(getUsedLanguage());
        return folder;
    }

    public List getFolderList(int parentFolderId) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setSort(Config.categorySort);
        loaderClass.setId(parentFolderId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getFolderList",loaderClass);
            //folderList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public List getAllFolderList() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getAllCategoryList",loaderClass);
            //folderList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    /**
     * Returns the full CategoryList in Sub-Folder-Order
     * @return
     */
    public List getAllFolderListSuborder() {

        List folderList = getAllFolderList();
        List folderListSuborder = new ArrayList();

        return getAllFolderListSuborderRecursive(0,folderList,folderListSuborder,"-");
    }

    private List getAllFolderListSuborderRecursive(int parentId, List folderList, List folderListSuborder, String submarker) {

        //System.out.println("Suche nach UK: parentid:"+parentId);
        Iterator categories = folderList.iterator();
        while (categories.hasNext()) {
            Folder folder = (Folder)categories.next();
            if (folder.getParent()==parentId) {
                //System.out.println("Unterkategorie: "+folder.getName() + " ("+folder.getFolderId()+")");
                //gehört als Unterkategorie zu dieser Kategorie
                folder.setName(submarker+" "+ folder.getName());
                folderListSuborder.add(folder);
                folderListSuborder =
                        getAllFolderListSuborderRecursive(folder.getFolderId(),folderList,folderListSuborder,submarker+submarker);
            }
        }

        return folderListSuborder;
    }

    public List getFolderListFromImageVersion(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(ivid);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getFolderListFromMediaObject", loaderClass);
            //categoryList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public void save(Object object) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Folder folder = (Folder)object;
        //Wenn Fid = leer dann auf NULL setzen, wegen Unique-ID
        if (folder.getFid()!=null) {
            if (folder.getFid().trim().length()==0) { folder.setFid(null); }
        }
        try {
            smc.update("saveFolder",folder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        DatabaseService.setTriggerStage1(true);
    }

    public void add(Object object) throws IOServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public synchronized void addFolder(Folder folder) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        List<FolderMultiLang> parentFolderList = getFolderList(folder.getParent());
        for (FolderMultiLang parentFolder : parentFolderList) {
            if (parentFolder.getName().equalsIgnoreCase(folder.getName())) {
                //Kategorie mit diesem Namen existiert bereits
                throw new DublicateEntry("Duplicate FolderName: "+folder.getName());
            }
        }
        //Wenn Fid = leer dann auf NULL setzen, wegen Unique-ID
        if (folder.getFid()!=null) {
            if (folder.getFid().trim().length()==0) { folder.setFid(null); }
        }

        try {
            smc.insert("addFolder",folder);
            Integer maxFolderId = (Integer)smc.queryForObject("getMaxFolderId",new Integer(0));
            folder.setFolderId(maxFolderId.intValue());

            //Acl von Elternkategorie Übernehmen:
                    Acl acl = new Acl();
                    if (folder.getParent()!=0) {
                        //Unterkategorie einer bestehenden
                        Folder parentFolder = this.getFolderById(folder.getParent());
                        acl = AclController.getAcl(parentFolder);
                    } else {
                        //Neue Root-Kategorie
                        UserService userService = new UserService();
                        Iterator securityGroups = userService.getSecurityGroupList().iterator();
                        while (securityGroups.hasNext()) {
                            SecurityGroup securityGroup = (SecurityGroup)securityGroups.next();
                            try {
                                acl.addPermission(securityGroup,new AclPermission("view"));
                                if (securityGroup.getId()!=UserService.getSecurityGroupVisitors().getId()) {
                                    acl.addPermission(securityGroup,new AclPermission("read"));
                                }
                            } catch (PermissionAlreadyExistException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                    AclController.setAcl(folder,acl);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOServiceException("Fehler beim anlegen eines Ordners: "+e.getMessage());
        } catch (ObjectNotFoundException e) {
            //Wenn beim ACL setzten die Parent-Kategorie nicht gefunden wurde
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        DatabaseService.setTriggerStage1(true);

    }

    public void addMediaToFolder(int folderId, int ivid) throws DublicateEntry {

        if (this.isMediaInFolder(folderId,ivid)) {
            throw new DublicateEntry("Folder["+folderId+"] has already this mediaobject: "+ivid);
        }

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setFolderId(folderId);
        folderHolder.setIvid(ivid);

        try {
            // Folder mit ID=0 gibt es nicht: Root-Folder
            if (folderId!=0) {
                Folder folder = this.getFolderById(folderId);
                folder.setChangedDate(new Date());
                this.save(folder);
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (IOServiceException e) {
            e.printStackTrace();
        }

        try {
            smc.insert("addMediaToFolder", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseService.setTriggerStage1(true);
        CronService.triggerSitemapXmlPost = true;
    }

    /**
     * Prüft ob das angegebene BasicMediaObject bereits in dieser Kategorie ist
     * @param folderId
     * @param ivid
     * @return
     */
    private boolean isMediaInFolder(int folderId, int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = 0;
        try {
            count = (Integer)smc.queryForObject("isMediaInFolder",new HolderClass(ivid,folderId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (count>0) {
            return true;
        } else {
            return false;
        }
        
    }


    public void addMediaToFolder(int folderId, MediaObject imageVersion) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setFolderId(folderId);
        folderHolder.setIvid(imageVersion.getIvid());

        try {
            Folder folder = this.getFolderById(folderId);
            folder.setChangedDate(new Date());
            this.save(folder);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            smc.insert("addImageToCategory", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        DatabaseService.setTriggerStage1(true);

    }

    protected void deleteMediaFromAllFolder(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteMediaFromAllFolders", new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        DatabaseService.setTriggerStage1(true);
    }

    public void deleteMediaFromFolder(Folder folder, MediaObject imageVersion) {

        this.deleteMediaFromFolder(folder.getFolderId(),imageVersion.getIvid());
    }

    public void deleteMediaFromFolder(int folderId, int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setFolderId(folderId);
        folderHolder.setIvid(ivid);

        try {
            //Folder 0 = Root-Folder: exitsiert nicht in der DB
            if (folderId !=0 && folderId !=-1) {
                Folder folder = this.getFolderById(folderId);
                folder.setChangedDate(new Date());
                this.save(folder);
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (IOServiceException e) {
            e.printStackTrace();
        }

        try {
            smc.delete("deleteMediaFromFolder", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseService.setTriggerStage1(true);
    }

    public void deleteMediaFromFolder(Folder folder, List moList) {

        Iterator images = moList.iterator();
        while (images.hasNext()) {

            MediaObject mo = (MediaObject)images.next();
            deleteMediaFromFolder(folder.getFolderId(),mo.getIvid());
        }

    }

    public void deleteById(int id) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteAllMediaFromFolder",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            smc.delete("deleteFolderById", new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseService.setTriggerStage1(true);
    }

    protected int calcMediaCount(int folderId) {

        List allFolderList = new LinkedList();
        Logger logger = Logger.getLogger(getClass());

        /* Alle Ordner inklusive neu gerechneter Medienobjekt-anzahl (in dem ordner selbst) laden */
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            allFolderList = smc.queryForList("getAllFolderListIc",new Integer(folderId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (allFolderList==null) { logger.debug("Folder List is NULL!!"); }

        MediaCountCalculator icc = new MediaCountCalculator(allFolderList);
        int mediaCount = icc.getMediaCount(folderId);

        /* Geänderte Medienanzahl in den Ordner schreiben (speichern) */

        List updateFolderList = icc.getUpdateList();
        Iterator updateFolders = updateFolderList.iterator();
        while (updateFolders.hasNext()) {
            Folder folder = (Folder)updateFolders.next();
            try {
                //System.out.println("Kategorie speichern: "+folder.getFolderId());
                //System.out.println("BasicMediaObject-count: "+folder.getMediaCount());
                //System.out.println("Images-count: "+folder.getMediaCountS());
                this.save(folder);
            } catch (IOServiceException e) {
                e.printStackTrace();
            }
        }

        return mediaCount;

    }

    /**
     * Berechnet die Bildanzahl für den gesamten Zweig dieser (Sub)Ordner,
     * es wird zuerst die oberste (Root)-Kategorie festgestellt und dann
     * die Bildanzahl aller darunterliegenden Kategorien berechnet
     *
     * @param folderId
     * @return
     * @deprecated Use DatabaseService.setTriggerStage1(true)
     */
    private int calcMediaCountBranch(int folderId) {

        Logger logger = Logger.getLogger(getClass());
        int mediaCount = 0;

        logger.debug("FolderService: MediaFolder-Branch neu berechnen, folderID="+folderId);

        try {
            List parentFolderList = this.getParentFolderList(folderId);
            logger.debug("Root Ordner ist...");
            if (!parentFolderList.isEmpty()) {
                logger.debug("RootOrdner von "+folderId+" = "+((Folder)parentFolderList.get(0)).getFolderId());
                logger.debug("Media-Count berechnen...");
                mediaCount = this.calcMediaCount(((Folder)parentFolderList.get(0)).getFolderId());
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return mediaCount;
    }


    public Folder getFolderByPath(String folderPath) throws ObjectNotFoundException {

        FolderService folderService = new FolderService();
        String[] pathToken = folderPath.split("/");
        int start = 0;
        int folderId = 0;

        if (folderPath.startsWith("/")) { start = 1; }

        for (int a=start;a<pathToken.length;a++) {
            List folderList = folderService.getFolderList(folderId);
            Iterator folders = folderList.iterator();
            boolean found = false;
            while (folders.hasNext()) {
                Folder folder = (Folder)folders.next();
                if (folder.getName().equalsIgnoreCase(pathToken[a])) {
                    folderId = folder.getFolderId();
                    found = true;
                    break;
                }
            }

            if (found == false) {
                throw new ObjectNotFoundException();
            }

        }

        try {
            Folder folder = folderService.getFolderById(folderId);
            return folder;
        } catch (IOServiceException e) {
            throw new ObjectNotFoundException();
        }
    }

    public int getFolderIdByFid(String fid) throws ObjectNotFoundException, IOServiceException {
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer folderId = null;
        try {
            folderId = (Integer)smc.queryForObject("getFolderIdByFid",fid);
            if (folderId==null) {
                throw new ObjectNotFoundException("Folder mit der FID "+fid+" nicht gefunden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOServiceException("SQL-Fehler: "+e.getMessage());
        }
        return folderId;
    }

    /**
     * gibt true oder false zurück ob der angegebene User diese Kategorie verändern darf (homecategory)
     * @param user
     * @param folderId
     * @return
     */
    public boolean isCanModifyFolder(User user, int folderId) {

        try {
            int homeCategoryId = user.getHomeCategoryId();
            if (homeCategoryId!=0) {
                int id = folderId;
                List folderList = this.getParentFolderList(id);
                Iterator folders = folderList.iterator();
                while (folders.hasNext()) {
                    Folder folder = (Folder)folders.next();
                    if (folder.getFolderId()==homeCategoryId) {
                        return true;
                    }
                }
            }
        } catch (ObjectNotFoundException e) {
            return false;
        } catch (IOServiceException e) {
            System.err.println("isCanModifyFolder: "+e.getMessage());
            return false;
        }

        return false;
    }

    public void deleteRecursiv(int folderId) throws IOServiceException {

        List<Folder> list = getFolderList(folderId);
        for (Folder f : list) {
            deleteRecursiv(f.getFolderId());
        }
        deleteById(folderId);
    }
}
