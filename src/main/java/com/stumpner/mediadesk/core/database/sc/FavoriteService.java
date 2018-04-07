package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.image.FavoriteMediaDescriptor;
import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.common.util.PaginatedList;

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
 * Date: 16.05.2005
 * Time: 22:29:34
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteService extends MultiLanguageService {

    public List getLightboxImageList(int userId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List imageList = new LinkedList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(userId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForList("getLightboxImageList",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;
    }

    /**
     * Lädt die Bilder der Lightbox des angegebenen Benutzers in eine Seitenweise-List (PaginatedList)
     * mit der angegeben Bilderanzahl pro Seite.
     * Diese Methode verwendet Mehrsprachige Bilder-Daten.
     * @param loaderClass
     * @param imagesPerPage
     * @return PaginatedList mit den Bilder der Lightbox
     */
    public PaginatedList getLightboxImagesPaginatedList(SimpleLoaderClass loaderClass, int imagesPerPage) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        PaginatedList imageList = null;
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForPaginatedList("getLightboxImageList",loaderClass,imagesPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    /**
     * Lädt die Bilder der Lightbox des angegebenen Benutzers in eine Seitenweise-List (PaginatedList)
     * mit der angegeben Bilderanzahl pro Seite
     * @param userId
     * @param imagesPerPage
     * @return PaginatedList mit den Bilder der Lightbox
     * @deprecated stattdessen ist die neue Multi-Language-Methode zu verwenden {@link FavoriteService#getLightboxImagesPaginatedList(com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass, int)}
     */
    public PaginatedList getLightboxImagesPaginatedList(int userId, int imagesPerPage) {

        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(userId);
        return getLightboxImagesPaginatedList(loaderClass,imagesPerPage);

    }

    public void addImageToLightbox(int ivid, int userId) {

        FavoriteMediaDescriptor lid = new FavoriteMediaDescriptor();
        //damit nicht 2x das selbe image drin ist, vorher löschen (kommt nur zur wirkung wenn es drin is)
        this.removeImageToLightbox(ivid,userId);
        lid.setIvid(ivid);
        lid.setUserId(userId);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.insert("addImageToLightbox",lid);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void removeImageToLightbox(int ivid, int userId) {

        FavoriteMediaDescriptor lid = new FavoriteMediaDescriptor();
        lid.setIvid(ivid);
        lid.setUserId(userId);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("removeImageFromLightbox",lid);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteImageFromAllLightbox(int ivid) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("removeImageFromAllLightbox",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void removeImagesToLightbox(List imageList, int userId) {

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject imageVersion = (MediaObject)images.next();
            this.removeImageToLightbox(imageVersion.getIvid(),userId);
        }
    }

    public int getLightboxUserCount(int userId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(-1);

        try {
            count = (Integer)smc.queryForObject("getLightboxUserCount",new Integer(userId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return count.intValue();
    }


    public boolean isInFav(int userId, int ivid) {

        List<MediaObjectMultiLang> imageList = getLightboxImageList(userId);

        for (MediaObjectMultiLang i : imageList) {
            if (i.getIvid()==ivid) return true;
        }

        return false;

    }
}
