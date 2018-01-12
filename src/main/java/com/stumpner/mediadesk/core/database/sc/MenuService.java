package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.menu.MenuLoaderClass;
import com.stumpner.mediadesk.menu.Menu;
import com.stumpner.mediadesk.core.database.AppSqlMap;

import java.util.List;
import java.util.LinkedList;
import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

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
 * Time: 17:50:21
 * To change this template use File | Settings | File Templates.
 */
public class MenuService extends MultiLanguageService {

    /**
     * Gibt das angegebene Menü in form einer Liste (alle Menüeinträge gereiht nach Position)
     * zurück
     * @param menuType
     * @return
     */
    public List getMenu(int menuType) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List menuList = new LinkedList();
        MenuLoaderClass loaderClass = new MenuLoaderClass();
        loaderClass.setType(menuType);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            menuList = smc.queryForList("getMenu",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return menuList;
    }

    public Menu getMenuById(int id) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Menu menu = new Menu();

        try {
            menu = (Menu)smc.queryForObject("getMenuById",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return menu;
    }

    public void addMenu(Menu menu) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.insert("addMenu",menu);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void saveMenu(Menu menu) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.update("saveMenu",menu);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteMenu(int id) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.delete("deleteMenu",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
