package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.image.category.Category;

import java.util.List;
import java.util.Iterator;

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
 * User: stumpner
 * Date: 17.01.2011
 * Time: 15:37:19
 */
public class MediaObjectApi extends ApiBase {

    private final int GETMEDIAOBJECTINFO = 1;
    private final int SETMEDIAOBJECTINFO = 2;
    private final int DELETEMEDIAOBJECT  = 3;
    private final int GETMEDIAOBJECTCATEGORIES  = 4;

    private final int GETMEDIAOBJECTIDBYFID = 5;

    public MediaObjectApi() {

        registerMethod("getMediaObjectInfo", GETMEDIAOBJECTINFO);
        registerMethod("setMediaObjectInfo", SETMEDIAOBJECTINFO);
        registerMethod("deleteMediaObject",  DELETEMEDIAOBJECT);
        registerMethod("getMediaObjectCategories",  GETMEDIAOBJECTCATEGORIES);

        registerMethod("getMediaObjectIdByFid",  GETMEDIAOBJECTIDBYFID);

        //--
        //registerMethod("categoryCreate", CATEGORYCREATE);
        //registerMethod("categoryDelete", CATEGORYDELETE);
        //registerMethod("categoryChangeDate", CATEGORYCHANGEDATE);
        //registerMethod("removeObjectsFromCategory", REMOVEOBJECTSFROMCATEGORY);
        //registerMethod("addObjectToCategory", ADDOBJECTTOCATEGORY);
        //registerMethod("getObjectsFromCategory", GETOBJECTSFROMCATEGORY);

    }

    public String call(User user, String method, String[] parameter) {

        switch (getMethodId(method)) {
            case GETMEDIAOBJECTINFO:
                return getMediaObjectInfo(parameter);
            case SETMEDIAOBJECTINFO:
                return setMediaObjectInfo(parameter);
            case DELETEMEDIAOBJECT:
                return deleteMediaObject(parameter);
            case GETMEDIAOBJECTCATEGORIES:
                return getMediaObjectCategories(parameter);
            case GETMEDIAOBJECTIDBYFID:
                return getMediaObjectIdByFid(parameter);
        }

        return "no value.";
    }

    /**
     * Gibt die MedieObject-ID anhang der FID (Foreign-ID) zur�ck
     * @param parameter
     * @return
     */
    private String getMediaObjectIdByFid(String[] parameter) {
        String fid = parameter[0];
        ImageVersionService imageService = new ImageVersionService();
        try {
            return String.valueOf(imageService.getMediaObjectIdByFid(fid));
        } catch (ObjectNotFoundException e) {
            return "-1;ObjectNotFound";
        } catch (IOServiceException e) {
            return "ERROR";
        }
    }

    private String getMediaObjectCategories(String[] parameter) {

        StringBuffer sb = new StringBuffer();
        int ivid = Integer.parseInt(parameter[0]);
        ImageVersionService imageService = new ImageVersionService();
        CategoryService categoryService = new CategoryService();
        List categoryList = categoryService.getCategoryListFromImageVersion(ivid);
        Iterator categories = categoryList.iterator();
        while (categories.hasNext()) {
            Category category = (Category)categories.next();
            sb.append(category.getCategoryId()+";");
        }

        return sb.toString();

    }

    private String getMediaObjectInfo(String[] parameter) {

        StringBuffer sb = new StringBuffer();
        int ivid = Integer.parseInt(parameter[0]);
        ImageVersionService imageService = new ImageVersionService();
        ImageVersionMultiLang imageVersion = (ImageVersionMultiLang)imageService.getImageVersionById(ivid);
        sb.append("createdate="+imageVersion.getCreateDate().getTime()+";");
        sb.append("versionname="+imageVersion.getVersionName()+";");
        sb.append("versiontitlelng1="+imageVersion.getVersionTitleLng1()+";");
        sb.append("versiontitlelng2="+imageVersion.getVersionTitleLng2()+";");
        sb.append("notelng1="+imageVersion.getNoteLng1()+";");
        sb.append("notelng2="+imageVersion.getNoteLng2()+";");
        return sb.toString();
    }

    private String setMediaObjectInfo(String[] parameter) {

        int ivid = Integer.parseInt(parameter[0]);
        String key = parameter[1];
        String value = parameter[2];
        ImageVersionService imageService = new ImageVersionService();
        ImageVersionMultiLang imageVersion = (ImageVersionMultiLang)imageService.getImageVersionById(ivid);

        if (key.equalsIgnoreCase("versionname")) {
            imageVersion.setVersionName(value);
        }
        if (key.equalsIgnoreCase("versiontitlelng1")) {
            imageVersion.setVersionTitleLng1(value);
        }
        if (key.equalsIgnoreCase("versiontitlelng2")) {
            imageVersion.setVersionTitleLng2(value);
        }
        if (key.equalsIgnoreCase("notelng1")) {
            imageVersion.setNoteLng1(value);
        }
        if (key.equalsIgnoreCase("notelng2")) {
            imageVersion.setNoteLng2(value);
        }
        if (key.equalsIgnoreCase("fid")) {
            imageVersion.setFid(value);
        }

        try {
            imageService.saveImageVersion(imageVersion);
            return "OK";
        } catch (IOServiceException e) {
            return "ERROR";
        }
    }

    private String deleteMediaObject(String[] parameter) {

        int ivid = Integer.parseInt(parameter[0]);
        ImageVersionService imageService = new ImageVersionService();
        try {
            imageService.deleteImage(ivid);
            return "OK";
        } catch (IOServiceException e) {
            return "ERROR";
        }

    }

}

