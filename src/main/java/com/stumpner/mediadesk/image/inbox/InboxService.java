package com.stumpner.mediadesk.image.inbox;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.MultiLanguageService;

import java.util.List;
import java.util.LinkedList;
import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

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
 * Date: 27.04.2005
 * Time: 21:08:35
 * To change this template use File | Settings | File Templates.
 */
public class InboxService extends MultiLanguageService {

    public List getInbox() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List imageList = new LinkedList();

        try {
            imageList = smc.queryForList("getInbox",new Integer(this.getUsedLanguage()));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public void addImage(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.insert("addImageToInbox",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void removeImage(int ivid) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteImageFromInbox",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
