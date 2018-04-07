package com.stumpner.mediadesk.web.webdav;

import com.bradmcevoy.http.*;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.exceptions.BadRequestException;

import java.util.*;
import java.io.*;

import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.image.inbox.InboxService;
import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.importing.AbstractImportFactory;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import com.stumpner.mediadesk.upload.FileRejectException;
import net.stumpner.security.acl.AclControllerContext;
import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

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
 * Date: 16.03.2011
 * Time: 18:53:14
 * To change this template use File | Settings | File Templates.
 */
public class FolderResource implements MakeCollectionableResource, PropFindableResource, CollectionResource, PutableResource, QuotaResource, MoveableResource, DeletableResource {

    User user = null;
    AclControllerContext aclCtx = null;

    private final WebdavResourceFactory resourceFactory;
    List categoryList = null;
    String categoryName = "empty";
    int id = 0;
    Folder folder = null;
    private String username;


    public FolderResource(WebdavResourceFactory resourceFactory, Folder folder) {
        this.folder = folder;
        this.resourceFactory = resourceFactory;

        this.id = folder.getCategoryId();
        this.categoryName = folder.getCatName();

        FolderService folderService = new FolderService();
        categoryList = folderService.getFolderList(folder.getCategoryId());
    }

    public FolderResource(WebdavResourceFactory resourceFactory, String categoryPath) {
        folder = new FolderMultiLang();
        folder.setCatName("root");
        folder.setCategoryId(0);
        this.resourceFactory = resourceFactory;
        this.id = 0;
        this.categoryName = "root";

        FolderService folderService = new FolderService();
        // leerer CategoryPath = Root, ansonsten Folder ausfindig machen:
        if (!categoryPath.equalsIgnoreCase("")) {
            try {
                Folder folder = folderService.getFolderByPath(categoryPath);
                this.id = folder.getCategoryId();
                this.categoryName = folder.getCatName();
            } catch (ObjectNotFoundException e) {
                System.err.println("Pfad: "+categoryPath+" nicht gefunden");
            }
        }

        categoryList = folderService.getFolderList(id);
    }

    public Date getCreateDate() {
        return folder.getChangedDate();
    }

    public String getUniqueId() {
        return String.valueOf(id);
    }

    public String getName() {
        return categoryName;
    }

    public Object authenticate(String user, String pwd) {
        //System.out.println("user:"+user);
        //System.out.println("password: "+pwd);
        this.username = pwd;
        return pwd;

    }

    public boolean authorise(Request request, Request.Method method, Auth auth) {

        user = WebdavResourceFactory.getUsernameFromAuthHeader(request,method,auth);
        aclCtx = WebdavResourceFactory.createAclContext(user);
        return WebdavResourceFactory.authroise(request, folder,user,aclCtx);
    }

    public String getRealm() {
        return WebdavResourceFactory.REALM;
    }

    public Date getModifiedDate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String checkRedirect(Request request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Resource child(String s) {
        System.out.println("Webdav FolderResource.child: [string="+s+"]  ,categoryid="+ folder.getCategoryId()+",name="+ folder.getCatName());
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<? extends Resource> getChildren() {

        System.out.println("Webdav FolderResource.getChildren: ,categoryid="+ folder.getCategoryId()+",name="+ folder.getCatName());

        List<Resource> list = new LinkedList<Resource>();
        AclFolderService categoryService = new AclFolderService(aclCtx);

        if (user!=null) { System.out.println("getChildren: user="+user.getName()); }

        
        //Home bzw Mandanten-Kategorie pr�fen: OB NUR HOME_DIR angezeigt werden sol
        if (Config.homeCategoryId!=-1) {
            if (folder.getCategoryId()==0) {
                //Aktuelle Kategorie = ROOT
                if (user.getHomeCategoryId()!=-1) {
                    //Benutzer hat eine Home-Kategorie
                    if (Config.homeCategoryAsRoot) {
                        //Soll als Root angezeigt werden, daher nur Config.homeCategoryId anzeigen
                        try {
                            FolderMultiLang category = (FolderMultiLang)categoryService.getFolderById(Config.homeCategoryId);
                            list.add(new FolderResource(resourceFactory, category));
                            return list;
                        } catch (ObjectNotFoundException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOServiceException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
            if (folder.getCategoryId()==Config.homeCategoryId) {
                //Aktuelle Kategorie = Home-Categorie Container, nur die Home-Kategorie des aktuellen Users anzeigen
                if (user.getHomeCategoryId()!=-1) {
                    if (Config.homeCategoryAsRoot) {
                        //Soll als Root angezeigt werden, daher nur Config.homeCategoryId anzeigen
                        try {
                            List<Folder> folderList = categoryService.getFolderSubTree(folder.getCategoryId(),0);
                            for (Folder catListElem : folderList) {
                                list.add(new FolderResource(resourceFactory, catListElem));
                            }
                            return list;
                        } catch (ObjectNotFoundException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOServiceException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
        }


        //categoryList = categoryService.getFolderList(folder.getCategoryId());
        try {
            categoryList = categoryService.getFolderSubTree(folder.getCategoryId(),0);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (Object aCategory : categoryList) {
            Folder folder = (Folder) aCategory;
            list.add(new FolderResource(resourceFactory, folder));
        }

        MediaService imageService = new MediaService();
        SimpleLoaderClass loader = new SimpleLoaderClass();
        loader.setId(id);
        List categoryMediaList = imageService.getCategoryImages(loader);

        for (Object aCategoryMedia : categoryMediaList) {
            MediaObjectMultiLang media = (MediaObjectMultiLang)aCategoryMedia;
            //System.out.println(" [+] mediaObjectResource: "+media.getVersionName());
            list.add(new MediaObjectResource(this.folder,media));
        }

        return list;
    }

    public Resource createNew(String newName, InputStream inputStream, Long length, String contentType) throws IOException, ConflictException {

        //if (user.getRole()<User.ROLE_HOME_EDITOR) {
            //Erst ab Rolle Editor darf jemand neue Objekte anlegen
        //    System.out.println("Webdav FolderResource.createNew: nicht erlaubt: "+user.getUserName()+" [role="+user.getRole()+"]");
        //    return null;
        //}

        System.out.println("Webdav FolderResource.createNew: ["+newName+",length="+length+",contentType="+contentType+"] ,catid="+ folder.getCategoryId()+",catname="+ folder.getCatName());

        int ivid = -1;
        int overwrittenIvId = -1;
        MediaObjectResource alreadyExistingResource = null;
        FolderService folderService = new FolderService();

        //// suchen ob es dieses objekt mittlerweile gibt?
        Iterator<? extends Resource> it = getChildren().iterator();
        while (it.hasNext()) {
            Resource resource = it.next();
            if (resource instanceof MediaObjectResource) {
                MediaObjectResource res = (MediaObjectResource)resource;
                if (res.getName().equalsIgnoreCase(newName)) {
                    System.out.println("  [+] Konflikt: existiert bereits "+res.getName()+", �berschreiben");
                    MediaService mediaService = new MediaService();
                    if (res.getContentLength()==0) {
                        //Medien-Objekt mit Gr��e 0-Bytes (Platzhalter) wird �berschrieben, daher l�schen
                    //todo: �berall l�schen oder nur hier l�schen?
                        try {
                            mediaService.deleteImageVersion(res.getMediaObject());
                        } catch (IOServiceException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    } else {
                        overwrittenIvId = res.getMediaObject().getIvid();
                        folderService.deleteMediaFromFolder(folder.getCategoryId(),res.getMediaObject().getIvid());
                    }

                    //return null;
                    //throw new ConflictException(res);
                    //??throw new ConflictException(res);
                }
            }

            if (resource instanceof FolderResource) {
                if (resource.getName().equalsIgnoreCase(newName)) {
                    System.out.println("  [+] Konflikt: existiert bereits als Kategorie?!");
                    //alreadyExistingResource = res;
                    //??throw new ConflictException(res);
                }
            }
        }

        if (length==0) {
            //Webdav Client versucht ein Neues 0-Byte objekt anzulegen und dieses zu �berschreieben, daher:
            //EmptyMediaObject anlegen dass dann �berschrieben werden kann
            MediaObjectMultiLang media = createEmptyMediaObject(newName);
            try {
                folderService.addMediaToFolder(folder.getCategoryId(),media.getIvid());
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return new MediaObjectResource(folder,media);
        }

        if (alreadyExistingResource==null) {

            System.out.println("  [+] OK, resource wird neu angelegt... Creator= Userid:"+user.getUserId());
            //Resource existiert noch nicht
            InputStream is = inputStream;

            String olFileName = newName.replaceAll(" ","-");

            OutputStream os = new FileOutputStream(Config.getTempPath()+File.separator+olFileName);
            
            try {
                IOUtils.copy( is, os );
            } finally {
                IOUtils.closeQuietly( os );
            }

            try {

                ImportFactory importFactory = Config.importFactory;
                File importFile = new File(Config.getTempPath()+File.separator+olFileName);
                MediaImportHandler importHandler =
                        importFactory.createMediaImportHandler(
                                importFile);
                ivid = importHandler.processImport(importFile,user.getUserId());

                //todo Import-Fehler behandeln!
                //importFailure = (importFailure==0) ? importF : importFailure;


                File file = new File(Config.getTempPath()+File.separator+olFileName);
                file.delete();

                folderService.addMediaToFolder(folder.getCategoryId(),ivid);

            }  catch (MimeTypeNotSupportedException e) {
                System.out.println("Diese Datei wird nicht unterst�tzt");
            } catch (SizeExceedException e) {
                System.out.println("Maximale Bildanzahl wurde erreicht");
            } catch (DublicateEntry dublicateEntry) {
                System.out.println("Datei existiert bereits in dieser Kategorie");
            } catch (FileRejectException e) {
                System.out.println("FileRejectException in FolderResource.java");
            }

            MediaService imageService = new MediaService();
            MediaObjectMultiLang media = (MediaObjectMultiLang)imageService.getImageVersionById(ivid);
            media.setVersionName(newName);
            media.setVersionTitle(newName);
            media.setVersionTitleLng1(newName);
            media.setVersionTitleLng2(newName);
            try {
                imageService.saveImageVersion(media);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            //Wenn ein File �berschrieben wurde, auch die Verkn�pfungen aktualisieren
            renewLinkedCategories(media, overwrittenIvId);

            InboxService inboxService = new InboxService();
            inboxService.removeImage(media.getIvid());

            System.out.println("  [+] fertig...");
            return new MediaObjectResource(folder,media);

        } else {
            return alreadyExistingResource;
        }

    }

    //Entfernt overwrittenIvid aus ALLEN kategorien und setzt media anstelle dessen
    private void renewLinkedCategories(MediaObjectMultiLang media, int overwrittenIvId) {

        FolderService folderService = new FolderService();
        List<Folder> linkedFolderList = folderService.getFolderListFromImageVersion(overwrittenIvId);
        for (Folder linkedCat : linkedFolderList) {
            folderService.deleteMediaFromFolder(linkedCat.getCategoryId(), overwrittenIvId);
            try {
                folderService.addMediaToFolder(linkedCat.getCategoryId(), media.getIvid());
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    private MediaObjectMultiLang createEmptyMediaObject(String newName) {

        Logger logger = Logger.getLogger(FolderResource.class);
        MediaObjectMultiLang imageVersion = new MediaObjectMultiLang();

        //Daten der Datei setzen
        imageVersion.setHeight(0);
        imageVersion.setWidth(0);
        imageVersion.setDpi(0);
        imageVersion.setCreateDate(new Date());
        imageVersion.setCreatorUserId(user.getUserId()); //todo: userid
        imageVersion.setKb(0);

        //MimeType + FileType:
        //todo: auslagern in den ImportHandler (eventuell in eine Superklasse!?)
        imageVersion.setMimeType(AbstractImportFactory.getMimeTypeFromExt(newName));
        imageVersion.setExtention(AbstractImportFactory.getFileExtention(newName));

        //Dateiname als Titel:
        imageVersion.setVersionName(newName);
        imageVersion.setVersionTitle(newName);

        //File-Objekt in der Datenbank erstellen
        MediaService imageService = new MediaService();
        try {
            logger.debug("Datei in der Datenbank anlegen...");
            imageVersion = (MediaObjectMultiLang)imageService.addImage(imageVersion);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            imageService.saveImageVersion(imageVersion);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageVersion;

    }

    public Long getQuotaUsed() {

        MediaService mediaService = new MediaService();
        return mediaService.getQuotaUsed();
    }

    public Long getQuotaAvailable() {

        MediaService mediaService = new MediaService();
        return mediaService.getQuotaAvailable();
    }

    public CollectionResource createCollection(String s) throws NotAuthorizedException, ConflictException, BadRequestException {

        //todo: Logik von FolderIndexController Zeile 243 implementieren
        FolderService folderService = new FolderService();
        boolean isCreateAllowed = true;

        //if (user.getRole()==User.ROLE_HOME_EDITOR) { isCreateAllowed = folderService.isCanModifyFolder(user,folder.getCategoryId()); }
        //if (user.getRole()>=User.ROLE_MASTEREDITOR) { isCreateAllowed = true; } //Erst ab Rolle Editor darf jemand neue Objekte anlegen

        if (isCreateAllowed) {

            FolderMultiLang newCategory = new FolderMultiLang();
            newCategory.setParent(folder.getCategoryId());
            newCategory.setCatName(s);
            newCategory.setCatTitle(s);
            newCategory.setCatTitleLng1(s);
            newCategory.setCatTitleLng2(s);

            try {
                folderService.addFolder(newCategory);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return new FolderResource(resourceFactory, newCategory);
        } else {
            throw new NotAuthorizedException(this);
        }

    }

    public void moveTo(CollectionResource collectionResource, String s) throws ConflictException, NotAuthorizedException, BadRequestException {

        FolderService folderService = new FolderService();
        boolean isMoveAllowed = true;

        //if (user.getRole()==User.ROLE_HOME_EDITOR) { isMoveAllowed = folderService.isCanModifyFolder(user,folder.getCategoryId()); }
        //if (user.getRole()>=User.ROLE_MASTEREDITOR) { isMoveAllowed = true; } //Erst ab Rolle Editor darf jemand neue Objekte anlegen

        if (isMoveAllowed) {
            System.out.println("Webdav moveTo: cat="+this.getUniqueId()+"...res="+collectionResource.getUniqueId()+" s="+s);
            if (String.valueOf(folder.getParent()).equalsIgnoreCase(collectionResource.getUniqueId())) {

                try {
                    //Umbenennen
                    FolderMultiLang categoryML = (FolderMultiLang) folderService.getFolderById(folder.getCategoryId());
                    if (categoryML.getCatTitle().equalsIgnoreCase(folder.getCatName())) {
                        categoryML.setCatTitle(s);
                    }
                    if (categoryML.getCatTitleLng1().equalsIgnoreCase(folder.getCatName())) {
                        categoryML.setCatTitleLng1(s);
                    }
                    if (categoryML.getCatTitleLng2().equalsIgnoreCase(folder.getCatName())) {
                        categoryML.setCatTitleLng2(s);
                    }
                    categoryML.setCatName(s);

                    folderService.save(categoryML);
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            } else {
                //Verschieben
                folder.setParent(Integer.parseInt(collectionResource.getUniqueId()));
                try {
                    folderService.save(folder);
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } else {
            throw new NotAuthorizedException(this); 
        }
    }

    public void delete() throws NotAuthorizedException, ConflictException, BadRequestException {
        //if (user.getRole()>=User.ROLE_MASTEREDITOR) { //Erst ab Rolle Editor darf jemand neue Objekte anlegen
            FolderService folderService = new FolderService();
            try {
                folderService.deleteById(folder.getCategoryId());
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        //}
    }
}
