package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.folder.Folder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Date: 21.07.2008
 * Time: 15:07:35
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewController extends AbstractImagePreviewController {


    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {


        httpServletRequest.setAttribute("pageNavTop",true);
        httpServletRequest.setAttribute("pageNavBottom",true);

        Object thumbnailListObject = httpServletRequest.getSession().getAttribute("thumbnailListObject");
        if (thumbnailListObject instanceof Folder) {
            //System.out.println("Kategorie-Image");
        }

        httpServletRequest.setAttribute("popup",false);
        //parentCategoryList = categoryService.getParentFolderList(id);

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
