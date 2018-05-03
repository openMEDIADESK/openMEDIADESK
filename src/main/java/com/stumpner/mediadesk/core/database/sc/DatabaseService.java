package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.media.image.util.Pop3Import;
import org.apache.log4j.Logger;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.Config;

import java.sql.SQLException;
import java.util.Date;

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
 * User: franz.stumpner
 * Date: 28.12.2006
 * Time: 01:07:51
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseService {

    static Logger logger = Logger.getLogger(DatabaseService.class);
    public static boolean triggerStage1 = false;
    private static Boolean popRunning = false;

    /**
     * Wartungs-Script Stage-1 (Oft)
     */
    public static void maintenanceStage1() {

        if (triggerStage1) {

            logger.debug("DatabaseService: updateing Folder BasicMediaObject Count");
            FolderService folderService = new FolderService();
            folderService.calcMediaCount(0);

            //AclImage Neu aufbauen:
            logger.debug("DatabseService: updateing ImageACL");
            MediaService ivs = new MediaService();
            ivs.updateAcl();
            triggerStage1 = false;

        }

    }

    public static void checkEmailImport() {


            if (popRunning==false && Config.emailImportEnabled==true) {
                    //pop3 account abfragen
                    popRunning = true;
                    //System.out.println("Check mails...");
                    Pop3Import pop3 = new Pop3Import();
                    pop3.host = Config.emailImportHost;
                    pop3.username = Config.emailImportUsername;
                    pop3.password = Config.emailImportPassword;
                    pop3.checkMail();
                    if (pop3.error.trim().length()>0) {
                        Config.emailImportLastError = pop3.error + " ["+(new Date())+"]";
                    }
                    popRunning = false;
            }


    }

    /**
     * Deletes the Search-Temp Table...
     */
    public static void imageSearchReset() {

        Logger logger = Logger.getLogger(DatabaseService.class);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.update("resetSearch",null);
            logger.info("Search ReSettet");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("Failed to reset Search");
        }

    }

    public static synchronized void setTriggerStage1(boolean triggerStage1) {
        DatabaseService.triggerStage1 = triggerStage1;
    }

}
