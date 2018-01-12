package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.MenuService;
import com.stumpner.mediadesk.menu.MenuType;
import com.stumpner.mediadesk.web.LngResolver;
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
 * Date: 11.09.2007
 * Time: 21:27:14
 * To change this template use File | Settings | File Templates.
 */
public class MenuSettingsController extends AbstractPageController {

    public MenuSettingsController() {
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        MenuService menuService = new MenuService();
        LngResolver lngResolver = new LngResolver();
        menuService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));

        if (httpServletRequest.getParameter("func")!=null) {
            //derzeit sowieso nur löschen möglich
            int id = Integer.parseInt(httpServletRequest.getParameter("id"));
            menuService.deleteMenu(id);
        }

        httpServletRequest.setAttribute("setTopMenu",menuService.getMenu(MenuType.TOP));
        httpServletRequest.setAttribute("setSideMenu",menuService.getMenu(MenuType.SIDE));

        httpServletRequest.setAttribute("navLinks",menuService.getMenu(MenuType.NAV));
        httpServletRequest.setAttribute("footer2Links",menuService.getMenu(MenuType.FOOTER2));
        httpServletRequest.setAttribute("footer3Links",menuService.getMenu(MenuType.FOOTER3));
        httpServletRequest.setAttribute("footer4Links",menuService.getMenu(MenuType.FOOTER4));
        httpServletRequest.setAttribute("footer5Links",menuService.getMenu(MenuType.FOOTER5));
        this.setContentTemplateFile("settings_menu.jsp",httpServletRequest);
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
