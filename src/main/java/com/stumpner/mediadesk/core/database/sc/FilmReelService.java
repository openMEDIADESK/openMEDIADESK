package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.image.filmreel.FilmReel;
import com.stumpner.mediadesk.image.filmreel.FilmReelImageList;
import com.stumpner.mediadesk.image.filmreel.FilmReelHolder;
import com.stumpner.mediadesk.image.ImageVersion;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.SQLException;

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
 * Time: 19:01:24
 * To change this template use File | Settings | File Templates.
 */
public class FilmReelService implements IServiceClass {

    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FilmReel getFilmReelById(int id) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FilmReel filmReel = new FilmReel();
        try {
            filmReel = (FilmReel)smc.queryForObject("getFilmReelById",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return filmReel;

    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void save(Object object) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        FilmReel filmReel = (FilmReel)object;
        try {
            smc.update("saveFilmReel",filmReel);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void add(Object object) throws IOServiceException {

    }

    public synchronized void addFilmReel(FilmReel object) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        FilmReel filmReel = (FilmReel)object;

        try {
            smc.insert("addFilmReel",filmReel);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Integer maxFilmReelId = (Integer)smc.queryForObject("getMaxFilmReelId",new Integer(0));
            filmReel.setFilmReelId(maxFilmReelId.intValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addImageToFilmReel(FilmReelImageList filmReelImageList, ImageVersion imageVersion) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FilmReelHolder filmReelHolder = new FilmReelHolder();
        filmReelHolder.setFilmReelId(filmReelImageList.getFilmReelId());
        filmReelHolder.setIvid(imageVersion.getIvid());
        filmReelHolder.setPos(0);

        try {
            smc.insert("addImageToFilmReel", filmReelHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void deleteImageFromFilmReel(FilmReelImageList filmReelImageList, ImageVersion imageVersion) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        FilmReelHolder filmReelHolder = new FilmReelHolder();
        filmReelHolder.setFilmReelId(filmReelImageList.getFilmReelId());
        filmReelHolder.setIvid(imageVersion.getIvid());
        filmReelHolder.setPos(0);

        try {
            smc.insert("deleteImageFromFilmReel", filmReelHolder);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void deleteById(int id) throws IOServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
