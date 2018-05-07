package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.media.FavoriteMediaDescriptor;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;

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

    public List getMediaObjectList(int userId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List mediaList = new LinkedList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(userId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            mediaList = smc.queryForList("getFavMediaList",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return mediaList;
    }

    public void addMediaToFav(int ivid, int userId) {

        FavoriteMediaDescriptor lid = new FavoriteMediaDescriptor();
        //damit nicht 2x das selbe image drin ist, vorher löschen (kommt nur zur wirkung wenn es drin is)
        this.removeMediaFromFav(ivid,userId);
        lid.setIvid(ivid);
        lid.setUserId(userId);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.insert("addMediaToFav",lid);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void removeMediaFromFav(int ivid, int userId) {

        FavoriteMediaDescriptor lid = new FavoriteMediaDescriptor();
        lid.setIvid(ivid);
        lid.setUserId(userId);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("removeMediaFromFav",lid);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteMeiaObjectFromAllFavs(int ivid) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("removeMediaFromAllFavs",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void removeMediaListFromFav(List imageList, int userId) {

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject imageVersion = (MediaObject)images.next();
            this.removeMediaFromFav(imageVersion.getIvid(),userId);
        }
    }

    public int getFavUserCount(int userId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(-1);

        try {
            count = (Integer)smc.queryForObject("getFavUserCount",new Integer(userId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return count.intValue();
    }


    public boolean isInFav(int userId, int ivid) {

        List<MediaObjectMultiLang> imageList = getMediaObjectList(userId);

        for (MediaObjectMultiLang i : imageList) {
            if (i.getIvid()==ivid) return true;
        }

        return false;

    }
}
