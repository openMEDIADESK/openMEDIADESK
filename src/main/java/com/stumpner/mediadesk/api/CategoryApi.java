package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.folder.FolderMultiLang;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.folder.Folder;

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
 * Date: 19.03.2008
 * Time: 11:54:40
 * @deprecated use {@link com.stumpner.mediadesk.web.api.rest.FolderRestApi}
 */
public class CategoryApi extends ApiBase {

    private final int CATEGORYEXIST = 1;
    private final int CATEGORYCREATE = 2;
    private final int CATEGORYDELETE = 3;
    private final int CATEGORYCHANGEDATE = 4;
    private final int REMOVEOBJECTSFROMCATEGORY = 5;
    private final int ADDOBJECTTOCATEGORY = 6;
    private final int GETOBJECTSFROMCATEGORY = 7;

    private final int DELETEMEDIAOBJECTFROMCATEGORY = 8;
    private final int GETCATEGORYIDBYFID = 9;

    public CategoryApi() {

        registerMethod("categoryExist", CATEGORYEXIST);
        registerMethod("categoryCreate", CATEGORYCREATE);
        registerMethod("categoryDelete", CATEGORYDELETE);
        registerMethod("categoryChangeDate", CATEGORYCHANGEDATE);
        registerMethod("removeObjectsFromCategory", REMOVEOBJECTSFROMCATEGORY);
        registerMethod("addObjectToCategory", ADDOBJECTTOCATEGORY);
        registerMethod("getObjectsFromCategory", GETOBJECTSFROMCATEGORY);

        registerMethod("deleteMediaObjectFromCategory",  DELETEMEDIAOBJECTFROMCATEGORY);
        registerMethod("getCategoryIdByFid", GETCATEGORYIDBYFID);

    }

    public String call(User user, String method, String[] parameter) {

        switch (getMethodId(method)) {
            case CATEGORYEXIST:
                return existsCategory(parameter);
            case CATEGORYCREATE:
                return categoryCreate(parameter);
            case CATEGORYDELETE:
                return categoryDelete(parameter);
            case CATEGORYCHANGEDATE:
                return categoryChangeDate(parameter);
            case REMOVEOBJECTSFROMCATEGORY:
                return removeObjectsFromCategory(parameter);
            case ADDOBJECTTOCATEGORY:
                return addObjectToCategory(parameter);
            case GETOBJECTSFROMCATEGORY:
                return getObjectsFromCategory(parameter);
            case DELETEMEDIAOBJECTFROMCATEGORY:
                return deleteMediaObjectFromCategory(parameter);
            case GETCATEGORYIDBYFID:
                return getCategoryIdByFid(parameter);
        }

        return "no value.";
    }

    /**
     * Gibt die Folder-ID anhang der FID (Foreign-ID) zurück
     * @param parameter
     * @return
     */
    private String getCategoryIdByFid(String[] parameter) {
        String fid = parameter[0];
        FolderService catService = new FolderService();
        try {
            return String.valueOf(catService.getFolderIdByFid(fid));
        } catch (ObjectNotFoundException e) {
            return "-1;ObjectNotFound";
        } catch (IOServiceException e) {
            return "ERROR";
        }
    }

    private String deleteMediaObjectFromCategory(String[] parameter) {
        int categoryId = Integer.parseInt(parameter[0]);
        int ivid = Integer.parseInt(parameter[1]);
        
        FolderService folderService = new FolderService();
        folderService.deleteMediaFromFolder(categoryId,ivid);
        return "OK";
    }

    /**
     * F�gt ein Objekt (image) einer Kategorie zu
     * @param parameter
     * @return
     */
    private String addObjectToCategory(String[] parameter) {
        int ivid = Integer.parseInt(parameter[0]);
        int categoryId = Integer.parseInt(parameter[1]);

        FolderService folderService = new FolderService();
        try {
            folderService.addMediaToFolder(categoryId,ivid);
            return "OK";
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "ERROR";
        }


    }

    /**
     * Gibt eine List mit ivid's (Objekte) der angegebenen Kategorie zur�ck.
     * Die ausgabe erfolgt in einem String, mit ; getrennt
     * @param parameter
     * @return
     */
    private String getObjectsFromCategory(String[] parameter) {

        StringBuffer returnString = new StringBuffer();
        MediaService imageService = new MediaService();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(Integer.parseInt(parameter[0]));
        List imageList = imageService.getFolderMediaObjects(loaderClass);
        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject imageVersion = (MediaObject)images.next();
            returnString.append(imageVersion.getIvid()+";");
        }

        return returnString.toString();

    }

    /**
     * Entfernt alle Objekte aus der angegeben Kategorie
     * @param parameter
     * @return
     */
    private String removeObjectsFromCategory(String[] parameter) {

        try {
            FolderService folderService = new FolderService();
            MediaService imageService = new MediaService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            loaderClass.setId(Integer.parseInt(parameter[0]));
            List categoryImages = imageService.getFolderMediaObjects(loaderClass);
            Folder folder = folderService.getFolderById(loaderClass.getId());

            folderService.deleteMediaFromFolder(folder,categoryImages);

            return "OK";
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "FAILED;ObjectNotFoundException;";
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "FAILED;IOServiceException;";
        }

    }

    private String categoryChangeDate(String[] parameter) {


        try {
            Folder folder = getCategoryByPath(parameter[0]);
            return String.valueOf(folder.getChangedDate().getTime());

        } catch (ObjectNotFoundException e) {
            return "ERROR";
        }

    }

    /**
     * Der Kategoriename kann der Name einer Kategorie sein oder ein Pfad
     * category1/subcategory/
     * Achtung: fangt die Pfadangabe mit einem / an, wird dieses weggeschnitten!
     * @param parameter
     * @return
     */
    private String existsCategory(String[] parameter) {

        String categoryName = parameter[0];

        if (categoryName.indexOf("/")==-1) {
            return existsCategoryName(categoryName);
        } else {
            if (categoryName.startsWith("/")) {
                //Vorausgehendes / wegschneiden
                categoryName = categoryName.substring(1);
            }
            return existsCategoryPath(categoryName);
        }

    }

    private String existsCategoryName(String categoryName) {

        boolean exist = false;
        FolderService folderService = new FolderService();
        Folder folder = new Folder();
        try {
            folder = folderService.getFolderByName(categoryName);
            exist = true;
        } catch (ObjectNotFoundException e) {
            exist = false;
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (exist) {
            return "true;id="+ folder.getFolderId();
        } else {
            return String.valueOf(exist);
        }

    }

    private String existsCategoryPath(String categoryPath) {

        FolderService folderService = new FolderService();
        String[] pathToken = categoryPath.split("/");
        int categoryId = 0;

        for (int a=0;a<pathToken.length;a++) {

            List categoryList = folderService.getFolderList(categoryId);
            Iterator categories = categoryList.iterator();
            boolean found = false;
            while (categories.hasNext()) {
                Folder folder = (Folder)categories.next();
                if (folder.getName().equalsIgnoreCase(pathToken[a])) {
                    categoryId = folder.getFolderId();
                    found = true;
                    break;
                }
            }

            if (found == false) {
                return "false";
            }

        }

        return "true;id="+categoryId;

    }

    private Folder getCategoryByPath(String categoryPath) throws ObjectNotFoundException {

        FolderService folderService = new FolderService();
        return folderService.getFolderByPath(categoryPath);

    }

    private String categoryCreate(String[] parameter) {

        String categoryName = parameter[0];
        String categoryTitle = parameter[0];
        if (parameter.length==2) {
            categoryTitle = parameter[1];
        }
        FolderService folderService = new FolderService();
        boolean success = false;
        //Pr�fen ob eine Pfadangabe oder Folder-Path enthalten
        if (categoryName.indexOf("/")==-1) {
            //kein Pfad
            FolderMultiLang category = new FolderMultiLang();
            category.setName(categoryName);
            category.setTitle(categoryName);
            category.setTitleLng1(categoryTitle);
            category.setTitleLng2(categoryTitle);
            try {
                folderService.addFolder(category);
                success = true;
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                success = false;
            }
        } else {
            //Pfad

            String[] pathToken = categoryName.split("/");
            int categoryId = 0;

            for (int a=0;a<pathToken.length;a++) {

                List categoryList = folderService.getFolderList(categoryId);
                Iterator categories = categoryList.iterator();
                boolean found = false;
                while (categories.hasNext()) {
                    Folder folder = (Folder)categories.next();
                    if (folder.getName().equalsIgnoreCase(pathToken[a])) {
                        categoryId = folder.getFolderId();
                        found = true;
                        if (a==pathToken.length-1) {
                            success = true;
                        }
                        break;
                    }
                }

                if (found == false) {
                    //Folder anlegen:
                    FolderMultiLang category = new FolderMultiLang();
                    category.setName(pathToken[a]);
                    category.setTitle(pathToken[a]);
                    if (a==pathToken.length-1) {
                        //Wenn die "letzte" Kategorie in der Pfadangabe
                        category.setTitleLng1(categoryTitle);
                        category.setTitleLng2(categoryTitle);
                    } else {
                        category.setTitleLng1(pathToken[a]);
                        category.setTitleLng2(pathToken[a]);
                    }
                    category.setParent(categoryId);
                    a--; //Nochmals durchlaufen...
                    try {
                        folderService.addFolder(category);
                        success = true;
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        success = false;
                    }
                }

            }

        }

        if (success) {
            return "OK";
        } else {
            return "ERROR";
        }
    }

    private String categoryDelete(String[] parameter) {

        FolderService folderService = new FolderService();
        MediaService imageService = new MediaService();
        String categoryName = parameter[0];
        if (categoryName.indexOf("/")==-1) {
            //Kategorieangabe
            Folder folder = null;
            try {
                folder = folderService.getFolderByName(categoryName);
                deleteCategory(folder.getFolderId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            //Kategorie-Pfadangabe
            String response = existsCategoryPath(categoryName);
            if (!response.startsWith("ERROR")) {
                String[] responseToken = response.split(";");
                String[] idToken = responseToken[1].split("=");
                int id = Integer.parseInt(idToken[1]);
                deleteCategory(id);
            } else {
                return "ERROR";
            }
        }
        //todo
        return "not implemented yet";
    }

    private void deleteCategory(int categoryId) {

        FolderService folderService = new FolderService();
        MediaService imageService = new MediaService();
                try {
                    Folder folder = folderService.getFolderById(categoryId);
                    SimpleLoaderClass loaderClass = new SimpleLoaderClass();
                    loaderClass.setId(categoryId);
                    List imageList = imageService.getFolderMediaObjects(loaderClass);
                    Iterator images = imageList.iterator();
                    while (images.hasNext()) {
                        MediaObject image = (MediaObject)images.next();
                        List categoryList = folderService.getFolderListFromImageVersion(image.getIvid());
                        if (categoryList.size()==1) {
                            imageService.deleteMediaObject(image);
                        }
                        folderService.deleteMediaFromFolder(folder,image);
                    }
                    folderService.deleteById(folder.getFolderId());
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

    }


}
