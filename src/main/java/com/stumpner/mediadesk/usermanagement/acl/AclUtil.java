package com.stumpner.mediadesk.usermanagement.acl;

import com.stumpner.mediadesk.image.category.Folder;
import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AclController;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.security.acl.AclNotFoundException;

import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;

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
 * Date: 28.11.2007
 * Time: 21:05:46
 * To change this template use File | Settings | File Templates.
 */
public class AclUtil {

    /**
     * Gibt eine Liste zurück mit den Bildern auf die der Benutzer im aclContext zugriff hat.
     *
     * Algoritmus:
     *
     * Das bild muss mindestens in einer Kategorie vorkommen, in der auch der Benutzer
     * eine download berechtigung hat.
     *
     * @param aclContext
     * @param downloadList
     * @return
     */
    public static List getPermittedImages(AclControllerContext aclContext,List downloadList) {
        return getPermittedImages(aclContext, downloadList, AclPermission.READ);
    }

    public static List getPermittedImages(AclControllerContext aclContext,List downloadList, String permission) {

        if (AclController.isEnabled()) {
            List permittedImages = new LinkedList();
            CategoryService categoryService = new CategoryService();
            Iterator downloadListImages = downloadList.iterator();
            while (downloadListImages.hasNext()) {
                boolean permitted = false;
                ImageVersionMultiLang imageVersion = (ImageVersionMultiLang)downloadListImages.next();

                //Ordner prüfen
                List categoryList = categoryService.getCategoryListFromImageVersion(imageVersion.getIvid());
                Iterator categories = categoryList.iterator();
                //todo: Zugriffverhalten wenn in keinem Ordner!? (z.b. neueste Bilder)
                while (categories.hasNext()) {
                    Folder folder = (Folder)categories.next();
                    try {
                        if (aclContext.checkPermission(new AclPermission(permission), folder)) {
                            permitted = true;
                        }
                    } catch (AclNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

                if (permitted) {
                    //Zugriff auf dieses Bild durch eine Kategorie oder Folder gegeben
                    permittedImages.add(imageVersion);
                }
            }

            return permittedImages;
        } else {
            return downloadList;
        }
    }

}
