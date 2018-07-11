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
public class FolderApi extends ApiBase {

    private final int FOLDEREXIST = 1;
    private final int FOLDERCREATE = 2;
    private final int FOLDERDELETE = 3;
    private final int FOLDERCHANGEDATE = 4;
    private final int REMOVEOBJECTSFROMFOLDER = 5;
    private final int ADDOBJECTTOFOLDER = 6;
    private final int GETOBJECTSFROMFOLDER = 7;

    private final int DELETEMEDIAOBJECTFROMFOLDER = 8;
    private final int GETFOLDERIDBYFID = 9;

    public FolderApi() {

        registerMethod("categoryExist", FOLDEREXIST);
        registerMethod("categoryCreate", FOLDERCREATE);
        registerMethod("categoryDelete", FOLDERDELETE);
        registerMethod("categoryChangeDate", FOLDERCHANGEDATE);
        registerMethod("removeObjectsFromCategory", REMOVEOBJECTSFROMFOLDER);
        registerMethod("addObjectToCategory", ADDOBJECTTOFOLDER);
        registerMethod("getObjectsFromCategory", GETOBJECTSFROMFOLDER);
        registerMethod("deleteMediaObjectFromCategory", DELETEMEDIAOBJECTFROMFOLDER);
        registerMethod("getCategoryIdByFid", GETFOLDERIDBYFID);

    }

    public String call(User user, String method, String[] parameter) {

        switch (getMethodId(method)) {
            case FOLDEREXIST:
                return existsFolder(parameter);
            case FOLDERCREATE:
                return folderCreate(parameter);
            case FOLDERDELETE:
                return folderDelete(parameter);
            case FOLDERCHANGEDATE:
                return folderChangeDate(parameter);
            case REMOVEOBJECTSFROMFOLDER:
                return removeObjectsFromFolder(parameter);
            case ADDOBJECTTOFOLDER:
                return addObjectToFolder(parameter);
            case GETOBJECTSFROMFOLDER:
                return getObjectsFromFolder(parameter);
            case DELETEMEDIAOBJECTFROMFOLDER:
                return deleteMediaObjectFromFolder(parameter);
            case GETFOLDERIDBYFID:
                return getFolderIdByFid(parameter);
        }

        return "no value.";
    }

    /**
     * Gibt die Folder-ID anhang der FID (Foreign-ID) zurück
     * @param parameter
     * @return
     */
    private String getFolderIdByFid(String[] parameter) {
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

    private String deleteMediaObjectFromFolder(String[] parameter) {
        int folderId = Integer.parseInt(parameter[0]);
        int ivid = Integer.parseInt(parameter[1]);
        
        FolderService folderService = new FolderService();
        folderService.deleteMediaFromFolder(folderId,ivid);
        return "OK";
    }

    /**
     * F�gt ein Objekt (image) einer Kategorie zu
     * @param parameter
     * @return
     */
    private String addObjectToFolder(String[] parameter) {
        int ivid = Integer.parseInt(parameter[0]);
        int folderId = Integer.parseInt(parameter[1]);

        FolderService folderService = new FolderService();
        try {
            folderService.addMediaToFolder(folderId,ivid);
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
    private String getObjectsFromFolder(String[] parameter) {

        StringBuffer returnString = new StringBuffer();
        MediaService mediaService = new MediaService();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(Integer.parseInt(parameter[0]));
        List mediaList = mediaService.getFolderMediaObjects(loaderClass);
        Iterator mos = mediaList.iterator();
        while (mos.hasNext()) {
            MediaObject mediaObject = (MediaObject)mos.next();
            returnString.append(mediaObject.getIvid()+";");
        }

        return returnString.toString();

    }

    /**
     * Entfernt alle Objekte aus der angegeben Kategorie
     * @param parameter
     * @return
     */
    private String removeObjectsFromFolder(String[] parameter) {

        try {
            FolderService folderService = new FolderService();
            MediaService mediaService = new MediaService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            loaderClass.setId(Integer.parseInt(parameter[0]));
            List folderMediaObjects = mediaService.getFolderMediaObjects(loaderClass);
            Folder folder = folderService.getFolderById(loaderClass.getId());

            folderService.deleteMediaFromFolder(folder,folderMediaObjects);

            return "OK";
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "FAILED;ObjectNotFoundException;";
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "FAILED;IOServiceException;";
        }

    }

    private String folderChangeDate(String[] parameter) {


        try {
            Folder folder = getFolderByPath(parameter[0]);
            return String.valueOf(folder.getChangedDate().getTime());

        } catch (ObjectNotFoundException e) {
            return "ERROR";
        }

    }

    /**
     * Der Ordnername kann der Name eines Ordners sein oder ein Pfad
     * folder1/subfolder/
     * Achtung: fangt die Pfadangabe mit einem / an, wird dieses weggeschnitten!
     * @param parameter
     * @return
     */
    private String existsFolder(String[] parameter) {

        String path = parameter[0];

        if (path.indexOf("/")==-1) {
            return existsFolderName(path);
        } else {
            if (path.startsWith("/")) {
                //Vorausgehendes / wegschneiden
                path = path.substring(1);
            }
            return existsFolderPath(path);
        }

    }

    private String existsFolderName(String name) {

        boolean exist = false;
        FolderService folderService = new FolderService();
        Folder folder = new Folder();
        try {
            folder = folderService.getFolderByName(name);
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

    private String existsFolderPath(String path) {

        FolderService folderService = new FolderService();
        String[] pathToken = path.split("/");
        int folderId = 0;

        for (int a=0;a<pathToken.length;a++) {

            List folderList = folderService.getFolderList(folderId);
            Iterator folders = folderList.iterator();
            boolean found = false;
            while (folders.hasNext()) {
                Folder folder = (Folder)folders.next();
                if (folder.getName().equalsIgnoreCase(pathToken[a])) {
                    folderId = folder.getFolderId();
                    found = true;
                    break;
                }
            }

            if (found == false) {
                return "false";
            }

        }

        return "true;id="+folderId;

    }

    private Folder getFolderByPath(String path) throws ObjectNotFoundException {

        FolderService folderService = new FolderService();
        return folderService.getFolderByPath(path);

    }

    private String folderCreate(String[] parameter) {

        String folderName = parameter[0];
        String folderTitle = parameter[0];
        if (parameter.length==2) {
            folderTitle = parameter[1];
        }
        FolderService folderService = new FolderService();
        boolean success = false;
        //Prüfen ob eine Pfadangabe oder Folder-Path enthalten
        if (folderName.indexOf("/")==-1) {
            //kein Pfad
            FolderMultiLang folder = new FolderMultiLang();
            folder.setName(folderName);
            folder.setTitle(folderName);
            folder.setTitleLng1(folderTitle);
            folder.setTitleLng2(folderTitle);
            try {
                folderService.addFolder(folder);
                success = true;
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                success = false;
            }
        } else {
            //Pfad

            String[] pathToken = folderName.split("/");
            int folderId = 0;

            for (int a=0;a<pathToken.length;a++) {

                List folderList = folderService.getFolderList(folderId);
                Iterator folders = folderList.iterator();
                boolean found = false;
                while (folders.hasNext()) {
                    Folder folder = (Folder)folders.next();
                    if (folder.getName().equalsIgnoreCase(pathToken[a])) {
                        folderId = folder.getFolderId();
                        found = true;
                        if (a==pathToken.length-1) {
                            success = true;
                        }
                        break;
                    }
                }

                if (found == false) {
                    //Folder anlegen:
                    FolderMultiLang folder = new FolderMultiLang();
                    folder.setName(pathToken[a]);
                    folder.setTitle(pathToken[a]);
                    if (a==pathToken.length-1) {
                        //Wenn die "letzte" Kategorie in der Pfadangabe
                        folder.setTitleLng1(folderTitle);
                        folder.setTitleLng2(folderTitle);
                    } else {
                        folder.setTitleLng1(pathToken[a]);
                        folder.setTitleLng2(pathToken[a]);
                    }
                    folder.setParent(folderId);
                    a--; //Nochmals durchlaufen...
                    try {
                        folderService.addFolder(folder);
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

    private String folderDelete(String[] parameter) {

        FolderService folderService = new FolderService();
        String folderName = parameter[0];
        if (folderName.indexOf("/")==-1) {
            //Kategorieangabe
            Folder folder = null;
            try {
                folder = folderService.getFolderByName(folderName);
                deleteFolder(folder.getFolderId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            //Kategorie-Pfadangabe
            String response = existsFolderPath(folderName);
            if (!response.startsWith("ERROR")) {
                String[] responseToken = response.split(";");
                String[] idToken = responseToken[1].split("=");
                int id = Integer.parseInt(idToken[1]);
                deleteFolder(id);
            } else {
                return "ERROR";
            }
        }
        //todo
        return "not implemented yet";
    }

    private void deleteFolder(int id) {

        FolderService folderService = new FolderService();
        MediaService mediaService = new MediaService();
                try {
                    Folder folder = folderService.getFolderById(id);
                    SimpleLoaderClass loaderClass = new SimpleLoaderClass();
                    loaderClass.setId(id);
                    List mediaObjectList = mediaService.getFolderMediaObjects(loaderClass);
                    Iterator mediaObjects = mediaObjectList.iterator();
                    while (mediaObjects.hasNext()) {
                        MediaObject mo = (MediaObject)mediaObjects.next();
                        List folderList = folderService.getFolderListFromMediaObject(mo.getIvid());
                        if (folderList.size()==1) {
                            mediaService.deleteMediaObject(mo);
                        }
                        folderService.deleteMediaFromFolder(folder,mo);
                    }
                    folderService.deleteById(folder.getFolderId());
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

    }


}
