package com.stumpner.mediadesk.search;

import com.ibatis.sqlmap.client.SqlMapClient;

import java.util.TimerTask;
import java.sql.SQLException;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import org.apache.log4j.Logger;

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
 * Date: 26.09.2005
 * Time: 22:47:46
 * To change this template use File | Settings | File Templates.
 */
public class ImageSearchResetter extends TimerTask {

    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.

        Logger logger = Logger.getLogger(ImageSearchResetter.class);
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        try {
            smc.update("resetSearch",null);
            logger.info("Search ReSettet");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("Failed to reset Search");
        }


    }
}
