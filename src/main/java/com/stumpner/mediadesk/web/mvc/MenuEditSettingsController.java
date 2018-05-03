package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.menu.Menu;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.MenuService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

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
 * Time: 21:52:40
 * To change this template use File | Settings | File Templates.
 */
public class MenuEditSettingsController extends AbstractAutoFillController {

    public MenuEditSettingsController() {

        this.setCommandClass(Menu.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        if (httpServletRequest.getParameter("id")!=null) {
            //objekt laden
            MenuService menuService = new MenuService();
            return menuService.getMenuById(Integer.parseInt(httpServletRequest.getParameter("id")));
        } else {
            if (httpServletRequest.getParameter("type")!=null) {
                //neues objekt erstellen
                Menu menu = new Menu();
                menu.setType(Integer.parseInt(httpServletRequest.getParameter("type")));
                return menu;
            }
        }

        return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.validation.BindException e) throws Exception {

        this.setContentTemplateFile("/settings_menu_edit.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        MenuService menuService = new MenuService();
        Menu menu = (Menu)object;
        //ergänzende Daten:
        if (menu.getMetaData().equalsIgnoreCase("0")) {
            menu.setMetaData("1;"+request.getParameter("metaDataUrl"));
        }

        if (menu.getId()==0) {
            menuService.addMenu(menu); //erstellen
        } else {
            menuService.saveMenu(menu); //speichern
        }
        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("../setmenu"));
        return null;
        //return super.onSubmit(request, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    void doNameAsTitle(Object o) {
        //do nothing
    }

    void doAutoFill(Object o) {
        Menu menu = (Menu)o;

        menu.setTitleLng1(
                doAutoFillField(menu.getTitleLng1(),menu.getTitle(),menu.getTitleLng2())
        );
        menu.setTitleLng2(
                doAutoFillField(menu.getTitleLng2(),menu.getTitle(),menu.getTitleLng1())
        );
    }
}
