package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.LightboxService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.MenuService;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.LngResolver;

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
 * Date: 24.03.2005
 * Time: 23:42:24
 * To change this template use File | Settings | File Templates.
 * @deprecated Wird nichtmehr verwendet {@link com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd}
 */
public class LeftModuleController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Map model = new HashMap();
        if (httpServletRequest.getSession().getAttribute("user")!=null) {
            LightboxService lightboxService = new LightboxService();
            ShoppingCartService shoppingCartService = new ShoppingCartService();
            User user = (User) httpServletRequest.getSession().getAttribute("user");

            model.put("lightboxCount",Integer.toString(lightboxService.getLightboxUserCount(user.getUserId())));
            model.put("shoppingCartCount",Integer.toString(shoppingCartService.getShoppingCartUserCount(user.getUserId())));
        }

        LngResolver lngResolver = new LngResolver();
        MenuService menuService = new MenuService();
        menuService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        //model.put("sideMenu",menuService.getMenu(MenuType.SIDE));
        //model.put("topMenu",menuService.getMenu(MenuType.TOP));

        Config.putDmsConfigToRequest(httpServletRequest);

        /*
        if (httpServletRequest.getRequestURI().startsWith("/index/cat")) {
            if (httpServletRequest.getParameter("id")!=null) {
                int id = Integer.parseInt(httpServletRequest.getParameter("id"));
                CategoryService categoryService = new CategoryService();
                System.out.println("Save parentCategoryList: ");
                httpServletRequest.setAttribute("parentCategoryList",categoryService.getParentCategoryList(id));
            }
        }
        */


        return new ModelAndView("/WEB-INF/template/"+Config.templatePath+"left.jsp","model",model);
    }
}
