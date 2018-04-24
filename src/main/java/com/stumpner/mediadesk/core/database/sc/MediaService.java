package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.image.BasicMediaObject;
import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.image.folder.NameValidator;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.LastImagesLoaderClass;
import com.stumpner.mediadesk.core.database.sc.loader.DateLoaderClass;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.common.util.PaginatedList;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import javax.sql.DataSource;

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
 * Date: 12.04.2005
 * Time: 23:07:27
 * To change this template use File | Settings | File Templates.
 */
public class MediaService extends MultiLanguageService implements IServiceClass {

    static Logger logger = Logger.getLogger(MediaService.class);

    /**
     * Gives Back the imageversion identified by its ivid
     * @param ivid
     * @return
     */
    public MediaObject getImageVersionById(int ivid) {

        return this.getImageVersionById(ivid, new MediaObjectMultiLang());

    }

    /**
     * Loads the data into the given Object (must be a MediaObject inherited Object)
     * @param ivid
     * @param image
     * @return
     */
    protected MediaObject getImageVersionById(int ivid, MediaObjectMultiLang image) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        DataSource ds = smc.getDataSource();
        

        try {
            image = (MediaObjectMultiLang)smc.queryForObject("getImageVersionById",new Integer(ivid));
            if (image!=null) image.setUsedLanguage(getUsedLanguage());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return image;
    }

    public List getCategoryImages(SimpleLoaderClass loaderClass) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List imageList = new ArrayList();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForList("getFolderMediaObjects",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public List getByCreateDate(Date start, Date end) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List mediaList = new ArrayList();
        DateLoaderClass lc = new DateLoaderClass(start,end);
        lc.setUsedLanguage(getUsedLanguage());

        try {
            mediaList = smc.queryForList("getImagesByCratedate",lc);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return mediaList;

    }

    public List getLastImages(int count) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List imageList = null;

        LastImagesLoaderClass loaderClass = new LastImagesLoaderClass();
        loaderClass.setSortBy(Config.sortByLatest);
        loaderClass.setCount(count);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForList("getLastImages",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public List getLastImagesAcl(int count, User user) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List imageList = null;

        LastImagesLoaderClass loaderClass = new LastImagesLoaderClass();
        loaderClass.setSortBy(Config.sortByLatest);
        loaderClass.setCount(count);
        loaderClass.setUsedLanguage(getUsedLanguage());
        loaderClass.setGroupPrincipal(user.getSecurityGroup());
        loaderClass.setUserPrincipal(user.getUserId());

        try {
            imageList = smc.queryForList("getLastImages",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public PaginatedList getLastImagesPages(int count) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PaginatedList imageList = null;

        LastImagesLoaderClass loaderClass = new LastImagesLoaderClass();
        loaderClass.setSortBy(Config.sortByLatest);
        loaderClass.setCount(count);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForPaginatedList("getLastImages",loaderClass,Config.itemCountPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public PaginatedList getLastImagesPagesAcl(int count, User user) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        PaginatedList imageList = null;

        LastImagesLoaderClass loaderClass = new LastImagesLoaderClass();
        loaderClass.setSortBy(Config.sortByLatest);
        loaderClass.setCount(count);
        loaderClass.setUsedLanguage(getUsedLanguage());
        loaderClass.setGroupPrincipal(user.getSecurityGroup());
        loaderClass.setUserPrincipal(user.getUserId());

        try {
            imageList = smc.queryForPaginatedList("getLastImages",loaderClass,Config.itemCountPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public void saveImageVersion(BasicMediaObject imageVersion) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)imageVersion;
        //Wenn Fid = leer dann auf NULL setzen, wegen Unique-ID
        if (mediaObject.getFid()!=null) {
            if (mediaObject.getFid().trim().length()==0) { mediaObject.setFid(null); }
        }

            if (mediaObject.getMayorMime().equalsIgnoreCase("audio")) {

                //Bei Audio den Titel DE und EN als <Artist> - <Title> speichern
                StringBuffer sb = new StringBuffer("");
                sb.append(mediaObject.getArtist());
                if (sb.toString().length()>0) {
                    sb.append(" - ");
                }
                sb.append(mediaObject.getVersionTitle());
                mediaObject.setVersionTitleLng1(sb.toString());
                mediaObject.setVersionTitleLng2(sb.toString());
            }

        try {
            smc.update("saveImageVersion",mediaObject);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public MediaObject addImage(MediaObject image) throws IOServiceException {

        //super.addImage(image); wird nichtmehr benötigt, da image und imageversion - tabellen
        //auf eine tabelle zusammengeführt wurden --> imageversion
        this.addImageVersion(image);

        return image;
    }

    public void addImageVersion(MediaObject imageversion) throws IOServiceException {

        /* aus addImage */

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        imageversion.setCreateDate(new Date());
        try {

            //If BasicMediaObject has no number, take systemtime as number:
            if (imageversion.getImageNumber().length()==0) {
                String numberString = Long.toString(System.currentTimeMillis());
                if (numberString.length()>100) {
                    numberString = numberString.substring(1,99);
                }
                imageversion.setImageNumber(numberString);
            }
            this.getImageByNumber(imageversion.getImageNumber());
            //sorry imagenumber exists, throw DublicateEntry Exception
            throw new DublicateEntry("MediaService.addMedia(): DublicateEntry");

        } catch (ObjectNotFoundException e) {
            //okay - user does not exist, go on...
        }

        imageversion.setCreateDate(new Date());
        //Wenn Fid = leer dann auf NULL setzen, wegen Unique-ID
        if (imageversion.getFid()!=null) {
            if (imageversion.getFid().trim().length()==0) { imageversion.setFid(null); }
        }

        try {
            Integer obj = (Integer)smc.insert("addImageVersion",imageversion);
            //System.out.println("Inserted Primary Key: "+obj.getClass().getName());
            //System.out.println("Inserted Primary val: "+obj.toString());
            imageversion.setIvid(obj.intValue());
            imageversion.setImageId(obj.intValue());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        this.save(imageversion);

    }

    public void updateImageAcl() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            smc.startTransaction();
            smc.delete("aclimageDelete");
            smc.update("aclimageUpdate");
            smc.commitTransaction();
            smc.endTransaction();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteImage(int ivid) throws IOServiceException {

        MediaObject imageVersion = this.getImageVersionById(ivid);
        this.deleteImageVersion(imageVersion);

    }

    public void deleteImageVersion(MediaObject imageVersion) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Logger logger = Logger.getLogger(MediaService.class);
        logger.info("MediaService: deleteMedia, ivid="+imageVersion.getIvid());

        try {
            FolderService folderService = new FolderService();
            FavoriteService favoriteService = new FavoriteService();
            ShoppingCartService shoppingCartService = new ShoppingCartService();

            /* Kategorieverknüpfungen lösen: bild(er) aus ordner und kategorien löschen */
            logger.debug("deleteImageVersion: delete image from all categories, folders, lightbox, shoppingcart, inbox");
            folderService.deleteMediaFromAllFolder(imageVersion.getIvid());
            favoriteService.deleteImageFromAllLightbox(imageVersion.getIvid());
            shoppingCartService.deleteImageFromAllShoppingCart(imageVersion.getIvid());

            //Medien-Objekt aus MediaObject-Tabelle löschen inkl. Metadaten:
            logger.debug("deleteImageVersion: delete object from imageversion-db-table");
            smc.delete("deleteAllImageMetadataFromIvid", new Integer(imageVersion.getIvid()));
            logger.debug("deleteImageVersion: delete metadata for ivid");
            smc.delete("deleteImageVersion",new Integer(imageVersion.getIvid()));
            //datei löschen:
            new File(Config.imageStorePath+"/"+imageVersion.getIvid()+"_0").delete();
            new File(Config.imageStorePath+"/"+imageVersion.getIvid()+"_1").delete();
            new File(Config.imageStorePath+"/"+imageVersion.getIvid()+"_2").delete();
            File file4 = new File(Config.imageStorePath+"/"+imageVersion.getIvid()+"_4");
            if (file4.exists()) { file4.delete(); }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void deleteImageVersions(List imageVersionList) throws IOServiceException {

        Iterator imageVersions = imageVersionList.iterator();

        while(imageVersions.hasNext()) {
            MediaObject imageVersion = (MediaObject)imageVersions.next();
            deleteImageVersion(imageVersion);
        }
    }

    public int getImageCount() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(0);
        try {
            count = (Integer)smc.queryForObject("getImageCount",null);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return count.intValue();
    }

    /**
     * Gibt den benötigten Speicherplatz in Mb zurück
     * @return
     */
    public int getImageMb() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(0);
        try {
            count = (Integer)smc.queryForObject("getImageKb",null);
            if (count==null) {
                //Wenn noch keine Bilder in der Datenbank sind wird null
                //zurückgegeben, deshalb das null abfangen und 0 speichern
                count = new Integer(0);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return count.intValue()/1000;
    }

    public Object getImageByNumber(String number) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        BasicMediaObject image = new BasicMediaObject();

        try {
            image = (BasicMediaObject)smc.queryForObject("getImageByNumber",number);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (image==null) {
            //image not found
            throw new ObjectNotFoundException();
        }

        return image;
    }

    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {
        return this.getImageVersionById(id);
    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return null;
    }

    public void save(Object object) throws IOServiceException {
        this.saveImageVersion((MediaObject)object);
    }

    public void add(Object object) throws IOServiceException {
        this.addImageVersion((MediaObject)object);
    }

    public void deleteById(int id) throws IOServiceException {
        MediaObject imageVersion = this.getImageVersionById(id);
        this.deleteImageVersion(imageVersion);
    }

    public int getMediaObjectIdByFid(String fid) throws ObjectNotFoundException, IOServiceException {
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer ivid = null;
        try {
            ivid = (Integer)smc.queryForObject("getMediaObjectIdByFid",fid);
            if (ivid==null) {
                throw new ObjectNotFoundException("BasicMediaObject mit der FID "+fid+" nicht gefunden.");
            }
        } catch (SQLException e) {           
            e.printStackTrace();
            throw new IOServiceException("SQL-Fehler: "+e.getMessage());
        }
        return ivid;
    }

    // Quota Info
    /**
     * @deprecated getQuotaTotalMb
     * @return
     */
    public long getQuotaTotal() {
        return (long)Config.licMaxMb*1000000;
    }

    public BigDecimal getQuotaTotalMb() {
        return BigDecimal.valueOf(Config.licMaxMb);
    }

    /**
     * @deprecated getQuotaUsedMb
     * @return
     */
    public long getQuotaUsed() {
        return (long)getImageMb()*1000000;
    }

    public BigDecimal getQuotaUsedMb() {
        return BigDecimal.valueOf(getImageMb());
    }

    /**
     * @deprecated getQuotaAvailableMb
     * @return
     */
    public long getQuotaAvailable() {
        return getQuotaTotal()-getQuotaUsed();
    }

    public BigDecimal getQuotaAvailableMb() {
        return getQuotaTotalMb().subtract(getQuotaUsedMb());
    }

    /**
     * Normalisiert die Namen in Medienobjekten und Kategorien
     */
    public void normalizeNames() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            Connection connection = smc.getDataSource().getConnection();

            Statement stmt = connection.createStatement();
            String notAllowedChars = NameValidator.getNotAllowedChars();
            System.out.println("Normalize BasicMediaObject Names: "+notAllowedChars);
            stmt.addBatch("UPDATE mediaobject set versionname = REPLACE(versionname,'\\\\','_') WHERE versionname LIKE '%\\\\\\\\%'");
            for (int a=1;a<notAllowedChars.length();a++) {
                stmt.addBatch("UPDATE mediaobject set versionname = REPLACE(versionname,'"+notAllowedChars.charAt(a)+"','_') WHERE versionname LIKE '%"+notAllowedChars.charAt(a)+"%'");
                stmt.addBatch("UPDATE folder SET catname = REPLACE(catname,'"+notAllowedChars.charAt(a)+"','_') WHERE catname LIKE '%"+notAllowedChars.charAt(a)+"%'");
            }
            stmt.executeBatch();
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public List getLicValidDue(Date date) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List imageList = new ArrayList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForList("getLicValidDue",date);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }
}
