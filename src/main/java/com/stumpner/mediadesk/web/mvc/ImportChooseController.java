package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.exceptions.QuotaExceededException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.lic.LicenceChecker;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.media.AutoMediaAssigner;

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
 * Date: 26.04.2005
 * Time: 22:03:25
 * To change this template use File | Settings | File Templates.
 */
public class ImportChooseController extends AbstractPageController {

    public ImportChooseController() {

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_IMPORTER;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        AutoMediaAssigner assigner = new AutoMediaAssigner();
        //assigner.clear(httpServletRequest);

        //ftp
        if (Config.ftpHost.length()>0) {
            httpServletRequest.setAttribute("ftpConfigured",true);
        } else {
            httpServletRequest.setAttribute("ftpConfigured",false);
            if (getUser(httpServletRequest).getRole()==User.ROLE_ADMIN) {
                httpServletRequest.setAttribute("canAdminFtp", true);
            }
        }
        //webdav
        if (Config.webdavEnabled) {
            httpServletRequest.setAttribute("webdavEnabled",true);
            httpServletRequest.setAttribute("serverName",httpServletRequest.getServerName());
        }

        if (httpServletRequest.getParameter("catid")!=null) {
            //bilder automatisch in eine kategorie laden...
            FolderService folderService = new FolderService();
            Folder folder = new Folder();
            if (!httpServletRequest.getParameter("catid").equalsIgnoreCase("")) {
                if (httpServletRequest.getParameter("catid").equalsIgnoreCase("0")) {
                    //Root-Kategory existiert nicht...
                    folder.setCategoryId(0);
                } else {
                    folder = folderService.getFolderById(Integer.parseInt(
                            httpServletRequest.getParameter("catid")
                    ));
                }
                assigner.setDestination(httpServletRequest, folder);
            }
        }

        LicenceChecker licenceChecker = new LicenceChecker();
        if (!licenceChecker.checkLicence()) {
            //lizenz ausgelaufen...
            throw new QuotaExceededException();
        } else {
            //lizenz bestätigt...

        }
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
