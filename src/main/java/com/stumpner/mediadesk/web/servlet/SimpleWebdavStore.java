package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.image.category.Folder;
import com.stumpner.mediadesk.image.category.FolderMultiLang;
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

import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.image.inbox.InboxService;
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
        String[] childrenNames =  new String[] {};    //To change body of overridden methods use File | Settings | File Templates.

            CategoryService categoryService = new CategoryService();
        Folder folder = null;

        //System.out.println("getChildrenNames (s = "+s);
        if (s.equalsIgnoreCase("/")) {
            //root folder
            folder = new Folder();
            folder.setCategoryId(0);
        } else {
            // folder-name
            try {
                folder = categoryService.getCategoryByPath(s.substring(1));
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } 
        }

            ImageVersionService imageService = new ImageVersionService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();

                loaderClass.setId(folder.getCategoryId());
                List imageList = imageService.getCategoryImages(loaderClass);
        List categoryList = categoryService.getCategoryList(folder.getCategoryId());

                childrenNames = new String[imageList.size()+categoryList.size()];
                Iterator images = imageList.iterator();
                int counter = 0;
                Iterator categories = categoryList.iterator();
                while (categories.hasNext()) {
                    Folder cat = (Folder)categories.next();
                    childrenNames[counter] = cat.getCatName();
                    //System.out.println("childrenNames[counter] (dir) = "+childrenNames[counter]);
                    counter++;
                }
                while (images.hasNext()) {
                    ImageVersion image = (ImageVersion)images.next();
                    if (image.getVersionName().endsWith("."+image.getExtention())) {
                        childrenNames[counter] = image.getVersionName();
                    } else {
                        childrenNames[counter] = image.getVersionName()+"."+image.getExtention();
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
        ImageVersion imageVersion = getImage(s);
        File file = getFile(imageVersion);
        return file.length();
        //return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeObject(ITransaction iTransaction, String s) {
        //To change body of implemented methods use File | Settings | File Templates.

        CategoryService categoryService = new CategoryService();
        try {
            FolderMultiLang category = (FolderMultiLang)categoryService.getCategoryByPath(s.substring(1));
            try {
                categoryService.deleteById(category.getCategoryId());
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (ObjectNotFoundException e) {
            //Keine Kategorie, Bilddatei löschen:
            ImageVersion imageVersion = getImage(s);
            String pathName = s.substring(0,s.lastIndexOf("/"));
            if (imageVersion!=null) {
                ImageVersionService imageService = new ImageVersionService();
                List categoryList = categoryService.getCategoryListFromImageVersion(imageVersion.getIvid());
                if (categoryList.size()==0) {
                    //Bilddatei löschen
                    try {
                        imageService.deleteImageVersion(imageVersion);
                    } catch (IOServiceException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else {
                    //verknüfpung aus category löschen
                    try {
                        FolderMultiLang category = null;
                        category = (FolderMultiLang)categoryService.getCategoryByPath(pathName.substring(1));
                        categoryService.deleteImageFromCategory(category,imageVersion);
                        System.out.println("Datei: "+imageVersion.getVersionName()+" aus kategorie "+pathName+" gelöscht");
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
        ImageVersion imageVersion = getImage(s);

        //System.out.println("getResourceContent");
        //System.out.println("string = "+s);
        //System.out.println(iTransaction.getPrincipal());

        if (imageVersion!=null) {

            logger.debug("found ImageVersion = "+imageVersion.getIvid());
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(getFile(imageVersion));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return inputStream;
        } else {
            logger.debug("Keine ImageVersion gefunden unter String="+s);
            return null;
        }

        //return super.getResourceContent(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
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
        //System.out.println("rollback");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createFolder(ITransaction iTransaction, String s) throws WebdavException {
        //System.out.println("Create Folder "+s);

        CategoryService categoryService = new CategoryService();
        int lastIndexOf = s.lastIndexOf("/");

        //Daten in die neue Kategorie füllen:
            String categoryName = s.substring(s.lastIndexOf("/")+1);
            FolderMultiLang category = new FolderMultiLang();
            category.setCatName(categoryName);
            category.setCatTitle(categoryName);
            category.setCatTitleLng1(categoryName);
            category.setCatTitleLng2(categoryName);

        if (lastIndexOf==0) {
            //in der root kategorie anlegen:
            category.setParent(0);

        } else {
            String pathName = s.substring(1,lastIndexOf);
            //System.out.println("Create Folder at Path: "+pathName);
            try {
                Folder parentFolder = categoryService.getCategoryByPath(pathName);
                //System.out.println(" new category hast parentid="+parentFolder.getCategoryId());
                category.setParent(parentFolder.getCategoryId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        //Kategorie anlegen/speichern:
            try {
                categoryService.addCategory(category);
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

                /*
                    BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(importFile));

                byte[] buf = new byte[1024];
                int i = 0;
                while((i=inputStream.read(buf))!=-1) {
                    bos.write(buf, 0, i);
                }
                inputStream.close();
                bos.close();*/

                //bild importieren

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
                ImageVersionService imageService = new ImageVersionService();
                ImageVersionMultiLang imageVersion = (ImageVersionMultiLang)imageService.getImageVersionById(ivid);
                imageVersion.setVersionName(fileName);
                imageVersion.setVersionTitle(fileName);
                imageVersion.setVersionTitleLng1(fileName);
                imageVersion.setVersionTitleLng2(fileName);
                imageService.saveImageVersion(imageVersion);


                //Bild automatisch der Kategorie zuweisen:
                String categoryPath = s.substring(1,s.lastIndexOf("/"));
                CategoryService categoryService = new CategoryService();
                Folder folder = categoryService.getCategoryByPath(categoryPath);
                logger.debug("setResourceContent: Speichern in Kategorie: "+categoryPath+" id="+ folder.getCategoryId());
                categoryService.addImageToCategory(folder.getCategoryId(),ivid);

                InboxService inboxService = new InboxService();
                inboxService.removeImage(ivid);
                length = getFile(imageVersion).length();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            StoredObject object = getStoredObject(iTransaction,s);
            logger.debug("setResourceContent[iT="+iTransaction.hashCode()+"]: Objekt existiert bereits: "+s+" contentLength="+object.getResourceLength());
            ImageVersion imageVersion = getImage(s);
            File file = getFile(imageVersion);
            try {
                writeTo(inputStream,file);
            } catch (IOException e) {
                logger.fatal("setResourceContent: failed to overwrite file");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        return length;
        //return super.setResourceContent(iTransaction, s, inputStream, s1, s2);    //To change body of overridden methods use File | Settings | File Templates.
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
        ImageVersionService imageService = new ImageVersionService();
        CategoryService categoryService = new CategoryService();
        Folder folder = null;
        if (s.equalsIgnoreCase("")) { s="/"; }
        StoredObject storedObject = new StoredObject();
        try {
            if (!s.equalsIgnoreCase("/")) {
                //System.out.println("getFolder");
                folder = categoryService.getCategoryByPath(s.substring(1));
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
            ImageVersion foundImage = getImage(s);
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



    private ImageVersion getImage(String s) {

        //StoredObject storedObject = new StoredObject();
        CategoryService categoryService = new CategoryService();
        ImageVersionService imageService = new ImageVersionService();
        ImageVersion foundImage = null;
        //System.out.println("No Folder found, searching for file ");
        int lastIndexOf = s.lastIndexOf("/");
        //System.out.println("lastIndexOf / "+lastIndexOf);
        String categoryPathString = "";
        if (lastIndexOf!=0) {
            categoryPathString = s.substring(1,lastIndexOf);
        }
        //System.out.println("CategoryPath: "+categoryPathString);
        String fileString = s.substring(lastIndexOf+1);
        //System.out.println("ObjektNotFound, searching for parent Folder: ");
        //System.out.println("FileString:   "+fileString);
        try {
            Folder folder2 = new Folder();
            if (!categoryPathString.equalsIgnoreCase("")) {
                folder2 = categoryService.getCategoryByPath(categoryPathString);
            } else {
                folder2 = new Folder();
                folder2.setCategoryId(0);
            }
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            loaderClass.setId(folder2.getCategoryId());
            List categoryImageList = imageService.getCategoryImages(loaderClass);
            Iterator categoryImages = categoryImageList.iterator();
            while (categoryImages.hasNext()) {
                ImageVersion imageVersion = (ImageVersion)categoryImages.next();
                String extention = imageVersion.getExtention();
                String filename = "";
                if (imageVersion.getVersionName().endsWith("."+extention)) {
                    filename = imageVersion.getVersionName();
                } else {
                    filename = imageVersion.getVersionName()+"."+extention;
                }
                if (fileString.equalsIgnoreCase(filename)) {
                    foundImage = imageVersion;
                }
            }

            if (foundImage!=null) {

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



        return foundImage;
    }

    private File getFile(ImageVersion imageVersion) {

        logger.debug("webdav: getFile("+imageVersion.getIvid()+");");
            String filename = Config.imageStorePath+"/"+imageVersion.getIvid()+"_0";
        logger.debug("file: "+filename);
            File file = new File(filename);

        return file;

    }
}
