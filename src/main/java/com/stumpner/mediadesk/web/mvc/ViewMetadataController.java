package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.stumpner.mediadesk.core.database.sc.ImageMetadataService;

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
 * Date: 26.05.2005
 * Time: 12:10:48
 * To change this template use File | Settings | File Templates.
 */
public class ViewMetadataController extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        int ivid = Integer.parseInt(httpServletRequest.getParameter("ivid"));
        //metadaten laden:
        ImageMetadataService ims = new ImageMetadataService();
        List metadataList = ims.getMetadata(ivid);
        //return null;  //To change body of implemented methods use File | Settings | File Templates.
        //Map model = new HashMap();
        httpServletRequest.setAttribute("metadataList",metadataList);

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);//new ModelAndView("/WEB-INF/template/"+Config.templatePath+"viewmetadata.jsp","model",model);
    }
}
