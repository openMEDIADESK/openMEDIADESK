package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.exceptions.LoadThumbnailException;
import com.stumpner.mediadesk.web.mvc.common.MediaMenu;
import com.stumpner.mediadesk.web.LngResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;
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
 * User: franz.stumpner
 * Date: 11.01.2007
 * Time: 18:01:12
 * To change this template use File | Settings | File Templates.
 */
public class LastImageViewController extends AbstractThumbnailAjaxController {

    protected MediaMenu mediaMenuBaker(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MediaMenu mediaMenu = getMediaMenu(request);
        //mediaMenu.setDelete(true);
        //mediaMenu.setDeleteFromDB(true);
        //mediaMenu.setDeleteFromCategory(false);
        mediaMenu.setSelection(true);
        mediaMenu.setSelectionMarkAll(true);
        mediaMenu.setSelectionMarkSite(true);
        mediaMenu.setSelectionUnmarkAll(true);
        mediaMenu.setSelectionCopy(false);
        mediaMenu.setSelectionMove(false);
        if (Config.quickDownload) { mediaMenu.setDownloadSelected(true); }

        return mediaMenu;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        this.setContentTemplateFile("lastimageview.jsp",httpServletRequest);

        UserService userService = new UserService();
        if (userService.processAutologin(httpServletRequest)) {
            System.out.println("autologin processed");
        }

        MediaMenu mediaMenu = getMediaMenu(httpServletRequest);
        //mediaMenu.setDelete(true);
        //mediaMenu.setDeleteFromDB(true);
        //mediaMenu.setDeleteFromCategory(false);
        mediaMenu.setSelection(true);
        mediaMenu.setSelectionMarkAll(true);
        mediaMenu.setSelectionMarkSite(true);
        mediaMenu.setSelectionUnmarkAll(true);
        mediaMenu.setSelectionCopy(false);
        mediaMenu.setSelectionMove(false);
        if (Config.quickDownload) { mediaMenu.setDownloadSelected(true); }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "last";
    }

    protected int getImageCount(HttpServletRequest request) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void insert(ImageVersion image, HttpServletRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void remove(ImageVersion image, HttpServletRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected List loadThumbnailImageList(int sortBy, int orderBy, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        ImageVersionService ivs = new ImageVersionService();
        LngResolver lngResolver = new LngResolver();
        ivs.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        PaginatedList imageList = null;
        imageList = ivs.getLastImagesPagesAcl(48,this.getUser(httpServletRequest));

        imageList.gotoPage(this.getPageIndex(httpServletRequest)-1);
        return imageList;
    }

    protected List loadAllImageList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws LoadThumbnailException {

        ImageVersionService ivs = new ImageVersionService();
        LngResolver lngResolver = new LngResolver();
        ivs.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        List imageList = null;
        imageList = ivs.getLastImagesAcl(48,this.getUser(httpServletRequest));
        return imageList;
    }

    protected boolean showSelect(HttpServletRequest request) {
        if (Config.quickDownload) {
            return this.getUser(request).getRole() >= User.ROLE_USER;
        } else {
            return this.getUser(request).getRole() > User.ROLE_EDITOR;
        }
    }
}
