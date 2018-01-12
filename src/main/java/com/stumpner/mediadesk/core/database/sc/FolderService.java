package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.HolderClass;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.CronService;
import com.stumpner.mediadesk.image.folder.*;
import com.stumpner.mediadesk.image.ImageVersion;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.common.util.PaginatedList;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

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
 * Date: 13.04.2005
 * Time: 21:36:09
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
        if (folder==null) throw new ObjectNotFoundException();
        return folder;

    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return getFolderByName(name);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Folder getFolderByName(String name) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FolderMultiLang folder = new FolderMultiLang();
        try {
            folder = (FolderMultiLang)smc.queryForObject("getFolderByName",name);
            folder.setUsedLanguage(getUsedLanguage());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (folder==null) throw new ObjectNotFoundException();
        return folder;

    }

    public List getFolderList(int numberOfResults) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getFolderList",loaderClass);
            //folderList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    /**
     * Returns a List of Folder where the image is in.
     * @param ivid
     * @return
     */
    public List getFolderListFromImageVersion(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = new ArrayList();
        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());
        loaderClass.setId(ivid);

        try {
            folderList = smc.queryForList("getFolderListFromImageVersion", loaderClass);
            //folderList = smc.queryForPaginatedList("getFolderList",new Integer(numberOfResults),12);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public PaginatedList getFolderListPages(int resultsPerPage) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PaginatedList folderList = null;
        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForPaginatedList("getFolderList",loaderClass,resultsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public List getVisibleFolderList() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List folderList = null;
        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForList("getVisibleFolderList",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public PaginatedList getVisibleFolderListPages(int resultsPerPage) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PaginatedList folderList = null;
        FolderLoaderClass loaderClass = new FolderLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            folderList = smc.queryForPaginatedList("getVisibleFolderList", loaderClass ,resultsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return folderList;

    }

    public void save(Object object) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Folder filmReel = (Folder)object;
        try {
            smc.update("saveFolder",filmReel);
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

        try {
            smc.insert("addFolder",folder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Integer maxFilmReelId = (Integer)smc.queryForObject("getMaxFolderId",new Integer(0));
            folder.setFolderId(maxFilmReelId.intValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseService.setTriggerStage1(true);

    }

    public void addImageToFolder(int folderId, int ivid) throws DublicateEntry {

        if (isImageInFolder(folderId,ivid)) {
            throw new DublicateEntry("Folder["+folderId+"] has already this image: "+ivid);
        }

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setFolderId(folderId);
        folderHolder.setIvid(ivid);
        folderHolder.setImageTitleAlias("");

        try {
            smc.insert("addImageToFolder", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Folder folder = (Folder)getById(folderId);
            if (folder.getPrimaryIvid()==-1) {
                //Primary Image des folders setzen
                folder.setPrimaryIvid(ivid);
                save(folder);
            }
            CronService.triggerSitemapXmlPost = true;
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /*
    public void addImageToFolder(FolderImageList folderImageList, ImageVersion imageVersion) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setFolderId(folderImageList.getFolderId());
        folderHolder.setIvid(imageVersion.getIvid());
        folderHolder.setImageTitleAlias("");

        try {
            smc.insert("addImageToFolder", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    */

    private boolean isImageInFolder(int folderId, int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = 0;
        try {
            count = (Integer)smc.queryForObject("isImageInFolder",new HolderClass(ivid,folderId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (count>0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteImageFromFolder(Folder folder, ImageVersion imageVersion) {

        this.deleteImageFromFolder(folder.getFolderId(),imageVersion.getIvid());
    }

    public void deleteImageFromFolder(int folderId, int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FolderHolder folderHolder = new FolderHolder();
        folderHolder.setFolderId(folderId);
        folderHolder.setIvid(ivid);

        try {
            smc.delete("deleteImageFromFolder", folderHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //checken ob das primary image erneuert werden muss
        try {
            Folder folder = (Folder)getById(folderId);
            if (folder.getPrimaryIvid()==ivid) {
                ImageVersionService imageService = new ImageVersionService();
                SimpleLoaderClass loader = new SimpleLoaderClass();
                loader.setId(folder.getFolderId());
                List imageList = imageService.getFolderImages(loader);
                Iterator images = imageList.iterator();
                while (images.hasNext() && folder.getPrimaryIvid()==ivid) {
                    ImageVersion newPrimaryImage = (ImageVersion)images.next();
                    folder.setPrimaryIvid(newPrimaryImage.getIvid());
                }
                save(folder);
            }
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected void deleteImageFromAllFolders(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteImageFromAllFolders", new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void deleteImagesFromFolder(Folder folder, List imageVersionList) {

        Iterator images = imageVersionList.iterator();
        boolean primaryFolderImageIsDeleted = false;
        while (images.hasNext()) {

            ImageVersion imageVersion = (ImageVersion)images.next();
            if (folder.getPrimaryIvid()==imageVersion.getIvid()) {
                primaryFolderImageIsDeleted=true;
            }
            deleteImageFromFolder(folder.getFolderId(),imageVersion.getIvid());
        }

        if (primaryFolderImageIsDeleted) {
            //Wenn das Primary-Bild gelöscht wurde, neu vergeben:
            ImageVersionService imageVersionService = new ImageVersionService();
            List imageList = imageVersionService.getFolderImages(new SimpleLoaderClass(folder.getFolderId()));
            ImageVersion imageVersion = (ImageVersion)imageList.get(imageList.size()-1);
            folder.setPrimaryIvid(imageVersion.getIvid());
            try {
                this.save(folder);
            } catch (IOServiceException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteById(int id) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteAllImagesFromFolder",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            smc.delete("deleteFolderById", new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
