package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.folder.FolderMultiLang;
import net.sf.webdav.ITransaction;
import net.sf.webdav.StoredObject;
import net.sf.webdav.exceptions.WebdavException;
import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;

import java.io.*;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.security.Principal;

import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import org.apache.log4j.Logger;

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
 * Date: 02.01.2009
 * Time: 17:46:28
 * To change this template use File | Settings | File Templates.
 */
public class SimpleWebdavStore implements net.sf.webdav.IWebdavStore {

    Logger logger = Logger.getLogger(SimpleWebdavStore.class);

    public SimpleWebdavStore(File file) {

        logger.debug("WebDav-Store created");
    }

    /*
    public void checkAuthentication(ITransaction iTransaction) throws SecurityException {

        System.out.println("Principal: "+iTransaction.getPrincipal());

        super.checkAuthentication(iTransaction);    //To change body of overridden methods use File | Settings | File Templates.
    }
    */

    public String[] getChildrenNames(ITransaction iTransaction, String s) throws WebdavException {

        String[] childrenNames =  new String[] {};

        FolderService folderService = new FolderService();
        Folder folder = null;

        if (s.equalsIgnoreCase("/")) {
            //root folder
            folder = new Folder();
            folder.setFolderId(0);
        } else {
            // folder-name
            try {
                folder = folderService.getFolderByPath(s.substring(1));
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            } 
        }

        MediaService mediaService = new MediaService();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();

        loaderClass.setId(folder.getFolderId());
        List mediaList = mediaService.getFolderMediaObjects(loaderClass);
        List folderList = folderService.getFolderList(folder.getFolderId());

                childrenNames = new String[mediaList.size()+folderList.size()];
                Iterator mediaObjects = mediaList.iterator();
                int counter = 0;
                Iterator mos = folderList.iterator();
                while (mos.hasNext()) {
                    Folder f = (Folder)mos.next();
                    childrenNames[counter] = f.getName();
                    //System.out.println("childrenNames[counter] (dir) = "+childrenNames[counter]);
                    counter++;
                }
                while (mediaObjects.hasNext()) {
                    MediaObject mediaObject = (MediaObject)mediaObjects.next();
                    if (mediaObject.getVersionName().endsWith("."+mediaObject.getExtention())) {
                        childrenNames[counter] = mediaObject.getVersionName();
                    } else {
                        childrenNames[counter] = mediaObject.getVersionName()+"."+mediaObject.getExtention();
                    }
                    //System.out.println("childrenNames[counter] = "+childrenNames[counter]);
                    counter++;
                }


        for (int a=0;a<childrenNames.length;a++) {
            //System.out.println("cn: "+childrenNames[a]);
        }

        return childrenNames;
    }

    public long getResourceLength(ITransaction iTransaction, String s) {
        //System.out.println("getResourceLength");
        MediaObject imageVersion = getMediaObject(s);
        File file = getFile(imageVersion);
        return file.length();
        //return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeObject(ITransaction iTransaction, String s) {

        FolderService folderService = new FolderService();
        try {
            FolderMultiLang folder = (FolderMultiLang) folderService.getFolderByPath(s.substring(1));
            try {
                folderService.deleteById(folder.getFolderId());
            } catch (IOServiceException e) {
                e.printStackTrace();
            }
        } catch (ObjectNotFoundException e) {
            //Kein Folder, Mediendatei löschen:
            MediaObject mediaObject = getMediaObject(s);
            String pathName = s.substring(0,s.lastIndexOf("/"));
            if (mediaObject!=null) {
                MediaService mediaService = new MediaService();
                List folderList = folderService.getFolderListFromMediaObject(mediaObject.getIvid());
                if (folderList.size()==0) {
                    //Mediendatei löschen
                    try {
                        mediaService.deleteMediaObject(mediaObject);
                    } catch (IOServiceException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else {
                    //verknüfpung aus folder löschen
                    try {
                        FolderMultiLang folder = null;
                        folder = (FolderMultiLang) folderService.getFolderByPath(pathName.substring(1));
                        folderService.deleteMediaFromFolder(folder,mediaObject);
                        System.out.println("Datei: "+mediaObject.getVersionName()+" aus folder "+pathName+" gelöscht");
                    } catch (ObjectNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            } else {
                System.out.println("Kein Object zum löschen");
            }
        }

        //System.out.println("removeObject");

    }

    public InputStream getResourceContent(ITransaction iTransaction, String s) throws WebdavException {

        logger.debug("getResourceContent: s="+s);
        MediaObject mediaObject = getMediaObject(s);

        if (mediaObject!=null) {

            logger.debug("found MediaObject = "+mediaObject.getIvid());
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(getFile(mediaObject));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return inputStream;
        } else {
            logger.debug("Keine MediaObject gefunden unter String="+s);
            return null;
        }
    }

    public void createResource(ITransaction iTransaction, String s) throws WebdavException {

        System.out.println("createResource");
        System.out.println("string = "+s);
        //super.createResource(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ITransaction begin(Principal principal) {

        final Principal p = principal;
        return new ITransaction() {

            public Principal getPrincipal() {
                return p;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        //return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void checkAuthentication(ITransaction iTransaction) {
        //System.out.println("checkAuthentication");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void commit(ITransaction iTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
        //System.out.println("commit");
    }

    public void rollback(ITransaction iTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createFolder(ITransaction iTransaction, String s) throws WebdavException {

        FolderService folderService = new FolderService();
        int lastIndexOf = s.lastIndexOf("/");

        //Daten in den neuen Folder füllen:
        String folderName = s.substring(s.lastIndexOf("/")+1);
        FolderMultiLang f = new FolderMultiLang();
        f.setName(folderName);
        f.setTitle(folderName);
        f.setTitleLng1(folderName);
        f.setTitleLng2(folderName);

        if (lastIndexOf==0) {
            //in den root folder anlegen:
            f.setParent(0);

        } else {
            String pathName = s.substring(1,lastIndexOf);
            //System.out.println("Create Folder at Path: "+pathName);
            try {
                Folder parentFolder = folderService.getFolderByPath(pathName);
                //System.out.println(" new folder hast parentid="+parentFolder.getFolderId());
                f.setParent(parentFolder.getFolderId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        //Kategorie anlegen/speichern:
            try {
                folderService.addFolder(f);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        //super.createFolder(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long setResourceContent(ITransaction iTransaction, String s, InputStream inputStream, String s1, String s2) throws WebdavException {

        logger.debug("setResourceContent s="+s+", s1="+s1+", s2="+s2);

        long length = 0;

        if (getStoredObject(iTransaction,s)==null) {
            logger.debug("setResourceContent[iT="+iTransaction.hashCode()+"]: Objekt existiert noch nicht");
            //objekt existiert noch nicht, erstellen

            try {
                String fileName = s.substring(s.lastIndexOf("/")+1);
                File importFile = new File(Config.getTempPath()+File.separator+fileName);
                writeTo(inputStream,importFile);

                int importFailure = 0;
                int ivid = 0;
                ImportFactory importFactory = Config.importFactory;
                MediaImportHandler importHandler =
                        importFactory.createMediaImportHandler(
                                importFile);
                //todo: userid herausfinden!!! und mitschreiben in den import!
                ivid = importHandler.processImport(importFile,1);
                //importdatei löschen
                importFile.delete();
                logger.debug("setResourceContent: neue Datei hat ivid = "+ivid);
                MediaService mediaService = new MediaService();
                MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)mediaService.getMediaObjectById(ivid);
                mediaObject.setVersionName(fileName);
                mediaObject.setVersionTitle(fileName);
                mediaObject.setVersionTitleLng1(fileName);
                mediaObject.setVersionTitleLng2(fileName);
                mediaService.saveMediaObject(mediaObject);


                //Medienobjekt automatisch dem Folder zuweisen:
                String path = s.substring(1,s.lastIndexOf("/"));
                FolderService folderService = new FolderService();
                Folder folder = folderService.getFolderByPath(path);
                logger.debug("setResourceContent: Speichern in Folder: "+path+" id="+ folder.getFolderId());
                folderService.addMediaToFolder(folder.getFolderId(),ivid);

                length = getFile(mediaObject).length();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            StoredObject object = getStoredObject(iTransaction,s);
            logger.debug("setResourceContent[iT="+iTransaction.hashCode()+"]: Objekt existiert bereits: "+s+" contentLength="+object.getResourceLength());
            MediaObject mediaObject = getMediaObject(s);
            File file = getFile(mediaObject);
            try {
                writeTo(inputStream,file);
            } catch (IOException e) {
                logger.fatal("setResourceContent: failed to overwrite file");
                e.printStackTrace();
            }

        }

        return length;
    }

    private void writeTo(InputStream is, File file) throws IOException {

        int BUF_SIZE = 62024;
        
            OutputStream os = new BufferedOutputStream(new FileOutputStream(
                    file), BUF_SIZE);
            try {
                int read;
                byte[] copyBuffer = new byte[BUF_SIZE];

                while ((read = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
                    os.write(copyBuffer, 0, read);
                }
            } finally {
                try {
                    is.close();
                } finally {
                    os.close();
                }
            }

    }

    public StoredObject getStoredObject(ITransaction iTransaction, String s) {

        //System.out.println("getStoredObject2 "+s);
        MediaService imageService = new MediaService();
        FolderService folderService = new FolderService();
        Folder folder = null;
        if (s.equalsIgnoreCase("")) { s="/"; }
        StoredObject storedObject = new StoredObject();
        try {
            if (!s.equalsIgnoreCase("/")) {
                //System.out.println("getFolder");
                folder = folderService.getFolderByPath(s.substring(1));
                storedObject.setFolder(true);
                storedObject.setCreationDate(new Date());
                storedObject.setLastModified(new Date());
                logger.debug("getStoredObject [Sub-Kategorie] s="+s);
            } else {
                logger.debug("getStoredObject [ROOT-Kategorie] s="+s);
                storedObject.setFolder(true);
                storedObject.setCreationDate(new Date());
                storedObject.setLastModified(new Date());
                //storedObject.setNullResource(false);
                //storedObject = super.getStoredObject(iTransaction, s);
                //System.out.println("root-folder:");
                //System.out.println("isFolder: "+storedObject.isFolder());
                //System.out.println("isNullResource: "+storedObject.isNullResource());
                //System.out.println("isResource: "+storedObject.isResource());
                //System.out.println("getCreationDate: "+storedObject.getCreationDate());
                //System.out.println("getLastModified: "+storedObject.getLastModified());
                //System.out.println("getResourceLength: "+storedObject.getResourceLength());
            }
        } catch (ObjectNotFoundException e) {
            //Keine kategory, suchen nach datei:
            MediaObject foundImage = getMediaObject(s);
            if (foundImage!=null) {

                logger.debug("getStoredObject[iT="+iTransaction.hashCode()+"]: Datei gefunden mit namen: "+s);
                storedObject.setFolder(false);
                storedObject.setCreationDate(new Date());
                storedObject.setLastModified(new Date());

                File file = getFile(foundImage);
                storedObject.setResourceLength(file.length());

            } else {
                logger.error("getStoredObject[iT="+iTransaction.hashCode()+"]: Keine Datei gefunden mit namen: "+s);
                storedObject = null;
            }
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return storedObject;
    }



    private MediaObject getMediaObject(String s) {

        //StoredObject storedObject = new StoredObject();
        FolderService folderService = new FolderService();
        MediaService mediaService = new MediaService();
        MediaObject foundMediaObject = null;
        //System.out.println("No Folder found, searching for file ");
        int lastIndexOf = s.lastIndexOf("/");
        //System.out.println("lastIndexOf / "+lastIndexOf);
        String pathString = "";
        if (lastIndexOf!=0) {
            pathString = s.substring(1,lastIndexOf);
        }
        //System.out.println("Path: "+categoryPathString);
        String fileString = s.substring(lastIndexOf+1);
        //System.out.println("ObjektNotFound, searching for parent Folder: ");
        //System.out.println("FileString:   "+fileString);
        try {
            Folder folder2 = new Folder();
            if (!pathString.equalsIgnoreCase("")) {
                folder2 = folderService.getFolderByPath(pathString);
            } else {
                folder2 = new Folder();
                folder2.setFolderId(0);
            }
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            loaderClass.setId(folder2.getFolderId());
            List folderMediaObjectList = mediaService.getFolderMediaObjects(loaderClass);
            Iterator folderMediaObjects = folderMediaObjectList.iterator();
            while (folderMediaObjects.hasNext()) {
                MediaObject mediaObject = (MediaObject)folderMediaObjects.next();
                String extention = mediaObject.getExtention();
                String filename = "";
                if (mediaObject.getVersionName().endsWith("."+extention)) {
                    filename = mediaObject.getVersionName();
                } else {
                    filename = mediaObject.getVersionName()+"."+extention;
                }
                if (fileString.equalsIgnoreCase(filename)) {
                    foundMediaObject = mediaObject;
                }
            }

            if (foundMediaObject!=null) {

                //System.out.println("foundimage");
                //storedObject.setFolder(false);
                //storedObject.setCreationDate(new Date());
                //storedObject.setLastModified(new Date());

            } else {

                //System.out.println("setNullResource");
                //storedObject.setNullResource(true);
                //storedObject = null;

            }

        } catch (ObjectNotFoundException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        return foundMediaObject;
    }

    private File getFile(MediaObject imageVersion) {

        logger.debug("webdav: getFile("+imageVersion.getIvid()+");");
            String filename = Config.imageStorePath+"/"+imageVersion.getIvid()+"_0";
        logger.debug("file: "+filename);
            File file = new File(filename);

        return file;

    }
}
