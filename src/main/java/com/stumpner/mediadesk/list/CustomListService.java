package com.stumpner.mediadesk.list;

import com.ibatis.sqlmap.client.SqlMapClient;

import java.util.List;
import java.util.LinkedList;
import java.sql.SQLException;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.Config;

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
 * Date: 22.03.2010
 * Time: 21:29:14
 * To change this template use File | Settings | File Templates.
 */
public class CustomListService {

    private int usedLanguage = 0;

    public List getCustomListEntries(int clid) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List list = new LinkedList();

        try {
            list = smc.queryForList("getCustomListEntries",LoaderClass.build(usedLanguage,clid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return list;
    }

    public List<CustomList> getCustomLists() {

        List<CustomList> list = new LinkedList<CustomList>();

        CustomList customList1 = new CustomList();
        customList1.setClid(1);
        customList1.setUsedLanguage(usedLanguage);
        customList1.setName(Config.customList1Lng1);
        customList1.setTitleLng1(Config.customList1Lng1);
        customList1.setTitleLng2(Config.customList1Lng2);
        list.add(customList1);

        CustomList customList2 = new CustomList();
        customList2.setClid(2);
        customList2.setUsedLanguage(usedLanguage);
        customList2.setName(Config.customList2Lng1);
        customList2.setTitleLng1(Config.customList2Lng1);
        customList2.setTitleLng2(Config.customList2Lng2);
        list.add(customList2);

        CustomList customList3 = new CustomList();
        customList3.setClid(3);
        customList3.setUsedLanguage(usedLanguage);
        customList3.setName(Config.customList3Lng1);
        customList3.setTitleLng1(Config.customList3Lng1);
        customList3.setTitleLng2(Config.customList3Lng2);
        list.add(customList3);

        return list;

    }

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    public void addListEntry(CustomListEntry entry) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.insert("addCustomListEntry",entry);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void saveListEntry(CustomListEntry entry) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.update("saveCustomListEntry",entry);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
