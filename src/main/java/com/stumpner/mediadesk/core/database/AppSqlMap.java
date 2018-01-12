package com.stumpner.mediadesk.core.database;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.Reader;
import java.util.Properties;

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
 * Date: 11.04.2005
 * Time: 21:33:57
 * To change this template use File | Settings | File Templates.
 */
public class AppSqlMap {

    private static SqlMapClient sqlMap;

    public static final void initialize(Reader configFile, Properties properties) {

        Logger logger = Logger.getLogger(AppSqlMap.class);

        try {

            sqlMap = SqlMapClientBuilder.buildSqlMapClient(configFile, properties);

        } catch (Exception e) {

            e.printStackTrace();
            logger.fatal("Error initializing SqlMapClient");
            throw new RuntimeException("Error initializing SqlMapClient. Cause: " +e);

        }


        logger.info("Initializing SqlMapClient...done");

    }

    public static SqlMapClient getSqlMapInstance() {
        return sqlMap;
    }

}
