package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.commandclass.settings.CustomFieldSettings;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.list.CustomListService;
import com.stumpner.mediadesk.list.CustomListEntry;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Date: 30.03.2010
 * Time: 19:45:15
 * To change this template use File | Settings | File Templates.
 */
public class CustomFieldSettingsController extends SimpleFormControllerMd {

    public CustomFieldSettingsController() {

        this.setCommandClass(CustomFieldSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        CustomFieldSettings settings = new CustomFieldSettings();
        settings.setCustomList1Lng1(Config.customList1Lng1);
        settings.setCustomList1Lng2(Config.customList1Lng2);
        settings.setCustomList2Lng1(Config.customList2Lng1);
        settings.setCustomList2Lng2(Config.customList2Lng2);
        settings.setCustomList3Lng1(Config.customList3Lng1);
        settings.setCustomList3Lng2(Config.customList3Lng2);

        settings.setCustomList1(getCustomListEntries(1));
        settings.setCustomList2(getCustomListEntries(2));
        settings.setCustomList3(getCustomListEntries(3));

        settings.setCustomStr1(Config.customStr1);
        settings.setCustomStr2(Config.customStr2);
        settings.setCustomStr3(Config.customStr3);
        settings.setCustomStr4(Config.customStr4);
        settings.setCustomStr5(Config.customStr5);
        settings.setCustomStr6(Config.customStr6);
        settings.setCustomStr7(Config.customStr7);
        settings.setCustomStr8(Config.customStr8);
        settings.setCustomStr9(Config.customStr9);
        settings.setCustomStr10(Config.customStr10);

        return settings;
    }

    private List getCustomListEntries(int i) {

        CustomListEntry customListEntry1 = new CustomListEntry();
        customListEntry1.setClid(i);
        CustomListEntry customListEntry2 = new CustomListEntry();
        customListEntry2.setClid(i);
        CustomListService service = new CustomListService();
        List list = service.getCustomListEntries(i);
        list.add(customListEntry1);
        list.add(customListEntry2);
        return list;

    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        this.setContentTemplateFile("settings_customfields.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        CustomFieldSettings settings = (CustomFieldSettings)o;
        Config.customList1Lng1 = settings.getCustomList1Lng1();
        Config.customList1Lng2 = settings.getCustomList1Lng2();
        Config.customList2Lng1 = settings.getCustomList2Lng1();
        Config.customList2Lng2 = settings.getCustomList2Lng2();
        Config.customList3Lng1 = settings.getCustomList3Lng1();
        Config.customList3Lng2 = settings.getCustomList3Lng2();

        Config.customStr1 = settings.getCustomStr1();
        Config.customStr2 = settings.getCustomStr2();
        Config.customStr3 = settings.getCustomStr3();
        Config.customStr4 = settings.getCustomStr4();
        Config.customStr5 = settings.getCustomStr5();
        Config.customStr6 = settings.getCustomStr6();
        Config.customStr7 = settings.getCustomStr7();
        Config.customStr8 = settings.getCustomStr8();
        Config.customStr9 = settings.getCustomStr9();
        Config.customStr10 = settings.getCustomStr10();

        Config.saveConfiguration();

        changeCustomList(settings.getCustomList1());
        changeCustomList(settings.getCustomList2());
        changeCustomList(settings.getCustomList3());

        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("setcustomfields")
        );

        return null;
    }

    private void changeCustomList(List<CustomListEntry> customList1) {

        CustomListService customListService = new CustomListService();
        for (CustomListEntry entry : customList1) {
            if (entry.getId()!=0) {
                //Eintrag Ändern:
                customListService.saveListEntry(entry);
            } else {

                if (entry.getTitleLng1().length()>0 || entry.getTitleLng2().length()>0) {
                    customListService.addListEntry(entry);
                }
            }
        }
    }
}
