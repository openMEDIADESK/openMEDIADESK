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
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
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
public class CategoryResource implements MakeCollectionableResource, PropFindableResource, CollectionResource, PutableResource, QuotaResource, MoveableResource, DeletableResource {

    User user = null;
    AclControllerContext aclCtx = null;

    private final WebdavResourceFactory resourceFactory;
    List categoryList = null;
    String categoryName = "empty";
    int id = 0;
    Category category = null;
    private String username;


    public CategoryResource(WebdavResourceFactory resourceFactory, Category category) {
        this.category = category;
        this.resourceFactory = resourceFactory;

        this.id = category.getCategoryId();
        this.categoryName = category.getCatName();

        CategoryService categoryService = new CategoryService();
        categoryList = categoryService.getCategoryList(category.getCategoryId());
    }

    public CategoryResource(WebdavResourceFactory resourceFactory, String categoryPath) {
        category = new CategoryMultiLang();
        category.setCatName("root");
        category.setCategoryId(0);
        this.resourceFactory = resourceFactory;
        this.id = 0;
        this.categoryName = "root";

        CategoryService categoryService = new CategoryService();
        // leerer CategoryPath = Root, ansonsten Category ausfindig machen:
        if (!categoryPath.equalsIgnoreCase("")) {
            try {
                Category category = categoryService.getCategoryByPath(categoryPath);
                this.id = category.getCategoryId();
                this.categoryName = category.getCatName();
            } catch (ObjectNotFoundException e) {
                System.err.println("Pfad: "+categoryPath+" nicht gefunden");
            }
        }

        categoryList = categoryService.getCategoryList(id);
    }

    public Date getCreateDate() {
        return category.getChangedDate();
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
        return WebdavResourceFactory.authroise(request,category,user,aclCtx);
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
        System.out.println("Webdav CategoryResource.child: [string="+s+"]  ,categoryid="+category.getCategoryId()+",name="+category.getCatName());
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<? extends Resource> getChildren() {

        System.out.println("Webdav CategoryResource.getChildren: ,categoryid="+category.getCategoryId()+",name="+category.getCatName());

        List<Resource> list = new LinkedList<Resource>();
        AclCategoryService categoryService = new AclCategoryService(aclCtx);

        if (user!=null) { System.out.println("getChildren: user="+user.getName()); }

        
        //Home bzw Mandanten-Kategorie pr�fen: OB NUR HOME_DIR angezeigt werden sol
        if (Config.homeCategoryId!=-1) {
            if (category.getCategoryId()==0) {
                //Aktuelle Kategorie = ROOT
                if (user.getHomeCategoryId()!=-1) {
                    //Benutzer hat eine Home-Kategorie
                    if (Config.homeCategoryAsRoot) {
                        //Soll als Root angezeigt werden, daher nur Config.homeCategoryId anzeigen
                        try {
                            CategoryMultiLang category = (CategoryMultiLang)categoryService.getCategoryById(Config.homeCategoryId);
                            list.add(new CategoryResource(resourceFactory, category));
                            return list;
                        } catch (ObjectNotFoundException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOServiceException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
            if (category.getCategoryId()==Config.homeCategoryId) {
                //Aktuelle Kategorie = Home-Categorie Container, nur die Home-Kategorie des aktuellen Users anzeigen
                if (user.getHomeCategoryId()!=-1) {
                    if (Config.homeCategoryAsRoot) {
                        //Soll als Root angezeigt werden, daher nur Config.homeCategoryId anzeigen
                        try {
                            List<Category> categoryList = categoryService.getCategorySubTree(category.getCategoryId(),0);
                            for (Category catListElem : categoryList) {
                                list.add(new CategoryResource(resourceFactory, catListElem));
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


        //categoryList = categoryService.getCategoryList(category.getCategoryId());
        try {
            categoryList = categoryService.getCategorySubTree(category.getCategoryId(),0);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (Object aCategory : categoryList) {
            Category category = (Category) aCategory;
            list.add(new CategoryResource(resourceFactory,category));
        }

        ImageVersionService imageService = new ImageVersionService();
        SimpleLoaderClass loader = new SimpleLoaderClass();
        loader.setId(id);
        List categoryMediaList = imageService.getCategoryImages(loader);

        for (Object aCategoryMedia : categoryMediaList) {
            ImageVersionMultiLang media = (ImageVersionMultiLang)aCategoryMedia;
            //System.out.println(" [+] mediaObjectResource: "+media.getVersionName());
            list.add(new MediaObjectResource(this.category,media));
        }

        return list;
    }

    public Resource createNew(String newName, InputStream inputStream, Long length, String contentType) throws IOException, ConflictException {

        //if (user.getRole()<User.ROLE_HOME_EDITOR) {
            //Erst ab Rolle Editor darf jemand neue Objekte anlegen
        //    System.out.println("Webdav CategoryResource.createNew: nicht erlaubt: "+user.getUserName()+" [role="+user.getRole()+"]");
        //    return null;
        //}

        System.out.println("Webdav CategoryResource.createNew: ["+newName+",length="+length+",contentType="+contentType+"] ,catid="+category.getCategoryId()+",catname="+category.getCatName());

        int ivid = -1;
        int overwrittenIvId = -1;
        MediaObjectResource alreadyExistingResource = null;
        CategoryService categoryService = new CategoryService();

        //// suchen ob es dieses objekt mittlerweile gibt?
        Iterator<? extends Resource> it = getChildren().iterator();
        while (it.hasNext()) {
            Resource resource = it.next();
            if (resource instanceof MediaObjectResource) {
                MediaObjectResource res = (MediaObjectResource)resource;
                if (res.getName().equalsIgnoreCase(newName)) {
                    System.out.println("  [+] Konflikt: existiert bereits "+res.getName()+", �berschreiben");
                    ImageVersionService mediaService = new ImageVersionService();
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
                        categoryService.deleteImageFromCategory(category.getCategoryId(),res.getMediaObject().getIvid());
                    }

                    //return null;
                    //throw new ConflictException(res);
                    //??throw new ConflictException(res);
                }
            }

            if (resource instanceof CategoryResource) {
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
            ImageVersionMultiLang media = createEmptyMediaObject(newName);
            try {
                categoryService.addImageToCategory(category.getCategoryId(),media.getIvid());
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return new MediaObjectResource(category,media);
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

                categoryService.addImageToCategory(category.getCategoryId(),ivid);

            }  catch (MimeTypeNotSupportedException e) {
                System.out.println("Diese Datei wird nicht unterst�tzt");
            } catch (SizeExceedException e) {
                System.out.println("Maximale Bildanzahl wurde erreicht");
            } catch (DublicateEntry dublicateEntry) {
                System.out.println("Datei existiert bereits in dieser Kategorie");
            } catch (FileRejectException e) {
                System.out.println("FileRejectException in CategoryResource.java");
            }

            ImageVersionService imageService = new ImageVersionService();
            ImageVersionMultiLang media = (ImageVersionMultiLang)imageService.getImageVersionById(ivid);
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
            return new MediaObjectResource(category,media);

        } else {
            return alreadyExistingResource;
        }

    }

    //Entfernt overwrittenIvid aus ALLEN kategorien und setzt media anstelle dessen
    private void renewLinkedCategories(ImageVersionMultiLang media, int overwrittenIvId) {

        CategoryService categoryService = new CategoryService();
        List<Category> linkedCategoryList = categoryService.getCategoryListFromImageVersion(overwrittenIvId);
        for (Category linkedCat : linkedCategoryList) {
            categoryService.deleteImageFromCategory(linkedCat.getCategoryId(), overwrittenIvId);
            try {
                categoryService.addImageToCategory(linkedCat.getCategoryId(), media.getIvid());
            } catch (DublicateEntry dublicateEntry) {
                dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    private ImageVersionMultiLang createEmptyMediaObject(String newName) {

        Logger logger = Logger.getLogger(CategoryResource.class);
        ImageVersionMultiLang imageVersion = new ImageVersionMultiLang();

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
        ImageVersionService imageService = new ImageVersionService();
        try {
            logger.debug("Datei in der Datenbank anlegen...");
            imageVersion = (ImageVersionMultiLang)imageService.addImage(imageVersion);
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

        ImageVersionService mediaService = new ImageVersionService();
        return mediaService.getQuotaUsed();
    }

    public Long getQuotaAvailable() {

        ImageVersionService mediaService = new ImageVersionService();
        return mediaService.getQuotaAvailable();
    }

    public CollectionResource createCollection(String s) throws NotAuthorizedException, ConflictException, BadRequestException {

        //todo: Logik von CategoryIndexController Zeile 243 implementieren
        CategoryService categoryService = new CategoryService();
        boolean isCreateAllowed = true;

        //if (user.getRole()==User.ROLE_HOME_EDITOR) { isCreateAllowed = categoryService.isCanModifyCategory(user,category.getCategoryId()); }
        //if (user.getRole()>=User.ROLE_MASTEREDITOR) { isCreateAllowed = true; } //Erst ab Rolle Editor darf jemand neue Objekte anlegen

        if (isCreateAllowed) {

            CategoryMultiLang newCategory = new CategoryMultiLang();
            newCategory.setParent(category.getCategoryId());
            newCategory.setCatName(s);
            newCategory.setCatTitle(s);
            newCategory.setCatTitleLng1(s);
            newCategory.setCatTitleLng2(s);

            try {
                categoryService.addCategory(newCategory);
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return new CategoryResource(resourceFactory, newCategory);
        } else {
            throw new NotAuthorizedException(this);
        }

    }

    public void moveTo(CollectionResource collectionResource, String s) throws ConflictException, NotAuthorizedException, BadRequestException {

        CategoryService categoryService = new CategoryService();
        boolean isMoveAllowed = true;

        //if (user.getRole()==User.ROLE_HOME_EDITOR) { isMoveAllowed = categoryService.isCanModifyCategory(user,category.getCategoryId()); }
        //if (user.getRole()>=User.ROLE_MASTEREDITOR) { isMoveAllowed = true; } //Erst ab Rolle Editor darf jemand neue Objekte anlegen

        if (isMoveAllowed) {
            System.out.println("Webdav moveTo: cat="+this.getUniqueId()+"...res="+collectionResource.getUniqueId()+" s="+s);
            if (String.valueOf(category.getParent()).equalsIgnoreCase(collectionResource.getUniqueId())) {

                try {
                    //Umbenennen
                    CategoryMultiLang categoryML = (CategoryMultiLang)categoryService.getCategoryById(category.getCategoryId());
                    if (categoryML.getCatTitle().equalsIgnoreCase(category.getCatName())) {
                        categoryML.setCatTitle(s);
                    }
                    if (categoryML.getCatTitleLng1().equalsIgnoreCase(category.getCatName())) {
                        categoryML.setCatTitleLng1(s);
                    }
                    if (categoryML.getCatTitleLng2().equalsIgnoreCase(category.getCatName())) {
                        categoryML.setCatTitleLng2(s);
                    }
                    categoryML.setCatName(s);

                    categoryService.save(categoryML);
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            } else {
                //Verschieben
                category.setParent(Integer.parseInt(collectionResource.getUniqueId()));
                try {
                    categoryService.save(category);
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
            CategoryService categoryService = new CategoryService();
            try {
                categoryService.deleteById(category.getCategoryId());
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        //}
    }
}
