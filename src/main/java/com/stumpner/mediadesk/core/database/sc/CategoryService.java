package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.CategoryLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.HolderClass;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.CronService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.category.*;
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
public class CategoryService extends MultiLanguageService implements IServiceClass {

    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {
        return getCategoryById(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Folder getCategoryById(int id) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FolderMultiLang folder = new FolderMultiLang();
        try {
            folder = (FolderMultiLang)smc.queryForObject("getCategoryById",new Integer(id));
            if (folder!=null) folder.setUsedLanguage(getUsedLanguage());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (folder==null) throw new ObjectNotFoundException("Kategorie mit der ID "+id+" nicht gefunden");
        return folder;

    }

    public List getParentCategoryList(int id) throws ObjectNotFoundException, IOServiceException {

        int recParentCat = id;
        LinkedList recCatList = new LinkedList();

        int whileLoopCounter = 0;
        //System.out.println("getParentCategoryList");

        //Rekursiv nach übergeordneten Kategorien suchen:
        while (recParentCat!=0) {
            FolderMultiLang recCat = (FolderMultiLang)this.getCategoryById(recParentCat);
            recCat.setUsedLanguage(getUsedLanguage());
            if (recCat==null) throw new ObjectNotFoundException();
            recCatList.addFirst(recCat);
            recParentCat=recCat.getParent();
            whileLoopCounter = whileLoopCounter+1;
            if (whileLoopCounter>0) {
                //System.out.println("SF-DEBUG: whileLoopCounter="+whileLoopCounter+" (getParentCategoryList) "+Config.instanceName);
            }
            if (whileLoopCounter>11) {
                System.err.println("getParentCategoryList: maybe endless loop detected > 11 "+Config.instanceName+" "+new Date());
                break;
            }
        }

        return recCatList;

    }

    public List getCategorySubTree(int parentId,int maxSubElements) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        List treeList = new ArrayList();
        List allCategoryList = new ArrayList();
        Map categoryMap = new HashMap();

        CategoryLoaderClass loaderClass = new CategoryLoaderClass();
        loaderClass.setSort(Config.categorySort);
        loaderClass.setId(parentId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        /*
        try {
            allCategoryList = smc.queryForList("getAllCategoryListIc",new Integer(parentId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Iterator categories = allCategoryList.iterator();
        while (categories.hasNext()) {
            Folder category = (Folder)categories.next();
            categoryMap.put(new Integer(category.getCategoryId()),category.getCatTitle());
        }
        */

        try {
            folderList = smc.queryForList("getCategorySubTree",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Iterator cats = folderList.iterator();
        while (cats.hasNext()) {

            Folder cat = (Folder)cats.next();
            FolderTreeElement cte = new FolderTreeElement(cat);
            if (cte.getParent()==parentId) {

                //cte.setCatTitle((String)categoryMap.get(new Integer(cte.getCategoryId())));
                Iterator subCats = folderList.iterator();
                int subElements = 0;
                while (subCats.hasNext()) {

                    Folder subCat = (Folder)subCats.next();
                    if (subCat.getParent()==cte.getCategoryId()) {
                        if (subElements<maxSubElements) {
                            cte.getSubCategoryList().add(new FolderTreeElement(subCat));
                        }
                        subElements++;
                    }
                }
                treeList.add(cte);
            }
        }

        return treeList;

    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return getCategoryByName(name);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Folder getCategoryByName(String name) throws ObjectNotFoundException, IOServiceException {
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FolderMultiLang folder = new FolderMultiLang();
        try {
            folder = (FolderMultiLang)smc.queryForObject("getCategoryByName",name);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (folder==null) throw new ObjectNotFoundException();
        folder.setUsedLanguage(getUsedLanguage());
        return folder;
    }

    public List getCategoryList(int parentCategoryId) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        CategoryLoaderClass loaderClass = new CategoryLoaderClass();
        loaderClass.setSort(Config.categorySort);
        loaderClass.setId(parentCategoryId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getCategoryList",loaderClass);
            //folderList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public List getAllCategoryList() {

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
    public List getAllCategoryListSuborder() {

        List categoryList = getAllCategoryList();
        List categoryListSuborder = new ArrayList();
        Map categoryMap = new HashMap();
        Iterator categories = categoryList.iterator();

        return getAllCategoryListSuborderRecursive(0,categoryList,categoryListSuborder,"-");
    }

    private List getAllCategoryListSuborderRecursive(int parentId, List categoryList, List categoryListSuborder, String submarker) {



        //System.out.println("Suche nach UK: parentid:"+parentId);
        Iterator categories = categoryList.iterator();
        while (categories.hasNext()) {
            Folder folder = (Folder)categories.next();
            if (folder.getParent()==parentId) {
                //System.out.println("Unterkategorie: "+folder.getCatName() + " ("+folder.getCategoryId()+")");
                //gehört als Unterkategorie zu dieser Kategorie
                folder.setCatName(submarker+" "+ folder.getCatName());
                categoryListSuborder.add(folder);
                categoryListSuborder =
                        getAllCategoryListSuborderRecursive(folder.getCategoryId(),categoryList,categoryListSuborder,submarker+submarker);
            }
        }

        return categoryListSuborder;
    }

    public List getCategoryListFromImageVersion(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List categoryList = new ArrayList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(ivid);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            categoryList = smc.queryForList("getCategoryListFromImageVersion", loaderClass);
            //categoryList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return categoryList;

    }
    /**
     * Returns a List of Folder where the image is in.

    public List getCategoryListFromImageVersion(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();

        try {
            folderList = smc.queryForList("getFolderListFromImageVersion", new Integer(ivid));
            //folderList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public List getCategoryList(int resultsPerPage) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = null;

        try {
            folderList = smc.queryForPaginatedList("getCategoryList",null,resultsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }


    public PaginatedList getVisibleFolderListPages(int resultsPerPage) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PaginatedList folderList = null;

        try {
            folderList = smc.queryForPaginatedList("getVisibleFolderList",null,resultsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

     **/

    public void save(Object object) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Folder filmReel = (Folder)object;
        //Wenn Fid = leer dann auf NULL setzen, wegen Unique-ID
        if (filmReel.getFid()!=null) {
            if (filmReel.getFid().trim().length()==0) { filmReel.setFid(null); }
        }
        try {
            smc.update("saveCategory",filmReel);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        DatabaseService.setTriggerStage1(true);
    }

    public void add(Object object) throws IOServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public synchronized void addCategory(Folder folder) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        List<FolderMultiLang> parentCategoryList = getCategoryList(folder.getParent());
        for (FolderMultiLang pCat : parentCategoryList) {
            if (pCat.getCatName().equalsIgnoreCase(folder.getCatName())) {
                //Kategorie mit diesem Namen existiert bereits
                throw new DublicateEntry("Duplicate FolderName: "+folder.getCatName());
            }
        }
        //Wenn Fid = leer dann auf NULL setzen, wegen Unique-ID
        if (folder.getFid()!=null) {
            if (folder.getFid().trim().length()==0) { folder.setFid(null); }
        }

        try {
            smc.insert("addCategory",folder);
            Integer maxFilmReelId = (Integer)smc.queryForObject("getMaxCategoryId",new Integer(0));
            folder.setCategoryId(maxFilmReelId.intValue());

            //Acl von Elternkategorie Übernehmen:
                    Acl acl = new Acl();
                    if (folder.getParent()!=0) {
                        //Unterkategorie einer bestehenden
                        Folder parentFolder = this.getCategoryById(folder.getParent());
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
            throw new IOServiceException("Fehler beim anlegen einer Kategorie: "+e.getMessage());
        } catch (ObjectNotFoundException e) {
            //Wenn beim ACL setzten die Parent-Kategorie nicht gefunden wurde
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        DatabaseService.setTriggerStage1(true);

    }

    public void addImageToCategory(int folderId, int ivid) throws DublicateEntry {

        if (this.isImageInCategory(folderId,ivid)) {
            throw new DublicateEntry("Folder["+folderId+"] has already this image: "+ivid);
        }

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setCategoryId(folderId);
        folderHolder.setIvid(ivid);
        //folderHolder.setImageTitleAlias("");

        try {
            // Kategorie mit ID=0 gibt es nicht: Root-Kategorie
            if (folderId!=0) {
                Folder folder = this.getCategoryById(folderId);
                folder.setChangedDate(new Date());
                this.save(folder);
            }
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
        CronService.triggerSitemapXmlPost = true;
        //old:this.calcImageCountBranch(folderId);
    }

    /**
     * Prüft ob das angegebene Image bereits in dieser Kategorie ist
     * @param categoryId
     * @param ivid
     * @return
     */
    private boolean isImageInCategory(int categoryId, int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = 0;
        try {
            count = (Integer)smc.queryForObject("isImageInCategory",new HolderClass(ivid,categoryId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (count>0) {
            return true;
        } else {
            return false;
        }
        
    }


    public void addImageToCategory(int categoryId, ImageVersion imageVersion) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setCategoryId(categoryId);
        folderHolder.setIvid(imageVersion.getIvid());

        try {
            Folder folder = this.getCategoryById(categoryId);
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
        //old:this.calcImageCountBranch(categoryId);

    }

    protected void deleteImageFromAllCategories(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteImageFromAllCategories", new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        DatabaseService.setTriggerStage1(true);
        //this.calcImageCountBranch(0);
    }

    public void deleteImageFromCategory(Folder folder, ImageVersion imageVersion) {

        this.deleteImageFromCategory(folder.getCategoryId(),imageVersion.getIvid());
    }

    public void deleteImageFromCategory(int categoryId, int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setCategoryId(categoryId);
        folderHolder.setIvid(ivid);

        try {
            //Kategorie 0 = Root-Folder: exitsiert nicht in der DB
            if (categoryId !=0 && categoryId !=-1) {
                Folder folder = this.getCategoryById(categoryId);
                folder.setChangedDate(new Date());
                this.save(folder);
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            smc.delete("deleteImageFromCategory", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        DatabaseService.setTriggerStage1(true);
        //this.calcImageCountBranch(categoryId);
    }

    public void deleteImagesFromCategory(Folder folder, List imageVersionList) {

        Iterator images = imageVersionList.iterator();
        while (images.hasNext()) {

            ImageVersion imageVersion = (ImageVersion)images.next();
            deleteImageFromCategory(folder.getCategoryId(),imageVersion.getIvid());
        }

    }

    public void deleteById(int id) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteAllImagesFromCategory",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            smc.delete("deleteCategoryById", new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        DatabaseService.setTriggerStage1(true);
        //this.calcImageCountBranch(id);

        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected int calcImageCount(int categoryId) {

        List allCategoryList = new LinkedList();
        Logger logger = Logger.getLogger(getClass());

        /* Alle Kategorien inklusive neu gerechneter Bilanzahl (in der kategorie selbst) laden */
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            //System.out.println("get All CategoryList Ic");
            allCategoryList = smc.queryForList("getAllCategoryListIc",new Integer(categoryId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //System.out.println("calcImageCount("+categoryId+")");
        if (allCategoryList==null) { logger.debug("Folder List is NULL!!"); }

        ImageCountCalc icc = new ImageCountCalc(allCategoryList);
        int imageCount = icc.getImageCount(categoryId);

        /* Geänderte Bildanzahl in die Kategorien schreiben (speichern) */

        List updateCategoryList = icc.getUpdateCategoryList();
        Iterator updateCategories = updateCategoryList.iterator();
        //System.out.println("geänderte Kategorien updaten");
        while (updateCategories.hasNext()) {
            Folder folder = (Folder)updateCategories.next();
            try {
                //System.out.println("Kategorie speichern: "+folder.getCategoryId());
                //System.out.println("Image-count: "+folder.getImageCount());
                //System.out.println("Images-count: "+folder.getImageCountS());
                this.save(folder);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return imageCount;

    }

    /**
     * Berechnet die Bildanzahl für den gesamten Zweig dieser (Sub)Kategorie,
     * es wird zuerst die oberste (Root)-Kategorie festgestellt und dann
     * die Bildanzahl aller darunterliegenden Kategorien berechnet
     *
     * @param categoryId
     * @return
     * @deprecated Use DatabaseService.setTriggerStage1(true)
     */
    private int calcImageCountBranch(int categoryId) {

        Logger logger = Logger.getLogger(getClass());
        int imageCount = 0;

        logger.debug("CategoryService: Image-Branch neu berechnen, categoryID="+categoryId);

        try {
            List parentCategoryList = this.getParentCategoryList(categoryId);
            logger.debug("Root Kategorie ist...");
            if (!parentCategoryList.isEmpty()) {
                logger.debug("RootKat von "+categoryId+" = "+((Folder)parentCategoryList.get(0)).getCategoryId());
                logger.debug("Image-Count berechnen...");
                imageCount = this.calcImageCount(((Folder)parentCategoryList.get(0)).getCategoryId());
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageCount;
    }


    public Folder getCategoryByPath(String categoryPath) throws ObjectNotFoundException {

        CategoryService categoryService = new CategoryService();
        String[] pathToken = categoryPath.split("/");
        int start = 0;
        int categoryId = 0;

        if (categoryPath.startsWith("/")) { start = 1; }

        for (int a=start;a<pathToken.length;a++) {
            List categoryList = categoryService.getCategoryList(categoryId);
            Iterator categories = categoryList.iterator();
            boolean found = false;
            while (categories.hasNext()) {
                Folder folder = (Folder)categories.next();
                if (folder.getCatName().equalsIgnoreCase(pathToken[a])) {
                    categoryId = folder.getCategoryId();
                    found = true;
                    break;
                }
            }

            if (found == false) {
                throw new ObjectNotFoundException();
            }

        }

        try {
            Folder folder = categoryService.getCategoryById(categoryId);
            return folder;
        } catch (IOServiceException e) {
            throw new ObjectNotFoundException();
        }
    }

    public int getCategoryIdByFid(String fid) throws ObjectNotFoundException, IOServiceException {
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer catid = null;
        try {
            catid = (Integer)smc.queryForObject("getCategoryIdByFid",fid);
            if (catid==null) {
                throw new ObjectNotFoundException("Folder mit der FID "+fid+" nicht gefunden.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOServiceException("SQL-Fehler: "+e.getMessage());
        }
        return catid;
    }

    /**
     * gibt true oder false zurück ob der angegebene User diese Kategorie verändern darf (homecategory)
     * @param user
     * @param categoryId
     * @return
     */
    public boolean isCanModifyCategory(User user, int categoryId) {

        try {
            int homeCategoryId = user.getHomeCategoryId();
            if (homeCategoryId!=0) {
                int id = categoryId;
                List categoryList = this.getParentCategoryList(id);
                Iterator categories = categoryList.iterator();
                while (categories.hasNext()) {
                    Folder folder = (Folder)categories.next();
                    if (folder.getCategoryId()==homeCategoryId) {
                        return true;
                    }
                }
            }
        } catch (ObjectNotFoundException e) {
            return false;
        } catch (IOServiceException e) {
            System.err.println("isCanModifyCategory: "+e.getMessage());
            return false;
        }

        return false;
    }

    public void deleteRecursiv(int categoryId) throws IOServiceException {

        List<Folder> list = getCategoryList(categoryId);
        for (Folder c : list) {
            deleteRecursiv(c.getCategoryId());
        }
        deleteById(categoryId);
    }
}
