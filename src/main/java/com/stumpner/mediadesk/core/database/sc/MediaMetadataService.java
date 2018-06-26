package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.media.MediaDetailEditCommand;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.Metadata;
import com.stumpner.mediadesk.usermanagement.User;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.util.List;
import java.util.LinkedList;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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
 * Date: 08.05.2005
 * Time: 16:40:42
 * To change this template use File | Settings | File Templates.
 */
public class MediaMetadataService {

    public List getMetadata(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List metadataList = new LinkedList();

        try {
            metadataList = smc.queryForList("getMediaMetadata",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return metadataList;

    }

    public MediaDetailEditCommand getMediaObjectMetadata(int ivid) {

        MediaService mediaService = new MediaService();
        MediaDetailEditCommand mediaDetailEditCommand = new MediaDetailEditCommand();
        MediaMetadataService mediaMetadataService = new MediaMetadataService();

        MediaObject mediaObject = mediaService.getMediaObjectById(ivid);
        if (mediaObject!=null) {
            mediaDetailEditCommand.setMediaObject(mediaObject);
            mediaDetailEditCommand.setMetadata(mediaMetadataService.getMetadata(ivid));

            //Ersteller-User laden:
            if (mediaDetailEditCommand.getMediaObject().getCreatorUserId()!=-1) {
                UserService userService = new UserService();
                try {
                    mediaDetailEditCommand.setCreator(
                        (User)userService.getById(mediaDetailEditCommand.getMediaObject().getCreatorUserId())
                    );
                } catch(ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            //Bei Text-Files Content Laden
            if (mediaObject.getMayorMime().equalsIgnoreCase("text")) {

                String filename = Config.imageStorePath+ File.separator+ivid+"_0";
                try {
                    String content = FileUtils.readFileToString(new File(filename));
                    mediaDetailEditCommand.setContent(content);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } else {
            return null;
        }

        return mediaDetailEditCommand;
    }

    public void addMetadata(Metadata metadata) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.insert("addMediaMetadata",metadata);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void saveMetadata(Metadata metadata) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.update("saveMediaMetadata",metadata);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void deleteAllMetadataFromIvid(int ivid) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteAllMetadataFromIvid",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
