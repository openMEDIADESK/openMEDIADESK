package com.stumpner.mediadesk.lic;

import java.util.TimerTask;
import java.util.List;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.UserService;
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
 * Date: 27.05.2005
 * Time: 15:00:17
 * To change this template use File | Settings | File Templates.
 */
public class LicenceChecker extends TimerTask {

    public void run() {

        //checkig Licence
        //IMPORTANT: this licence checker must be run widhin 8 hours, because of the
        //not working ?reconnect=true mysql driver
        //this is a socalled workaround for this:

        UserService userService = new UserService();
        List userList = userService.getUserAtomList();

        Logger logger = Logger.getLogger(LicenceChecker.class);
        logger.info("Licence Check: usercount="+userList.size());
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Überprüft ob die Linzenzwerte überschritten wurden
     * Maximale Bildanzahl
     * Maximale MB-Speicherplatz
     * @return true wenn der Speicherplatz überschritten wurde
     */
    public boolean checkLicence() {

        int imageCount = getImageCount();
        int imageMb = getImageMb();
        if (Config.licMaxImages!=0) {
            //Anzahl der Bilder prüfen
            if (imageCount>Config.licMaxImages) {
                return false;
            }
        }
        if (Config.licMaxMb!=0) {
            //Anzahl des benötigten Speicherplatz prüfen
            if (imageMb>Config.licMaxMb) {
                return false;
            }
        }
        return true;
    }

    public int getImageCount() {

        MediaService imageService = new MediaService();
        return imageService.getMediaCount();
    }

    public int getImageMb() {

        MediaService imageService = new MediaService();
        return imageService.getUsedMb();
    }
}
