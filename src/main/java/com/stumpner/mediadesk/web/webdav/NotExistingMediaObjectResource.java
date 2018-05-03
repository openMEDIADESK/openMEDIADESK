package com.stumpner.mediadesk.web.webdav;

import com.bradmcevoy.http.*;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.common.Path;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.io.*;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.media.image.util.SizeExceedException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import com.stumpner.mediadesk.upload.FileRejectException;

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
 * Date: 06.04.2011
 * Time: 20:57:32
 * To change this template use File | Settings | File Templates.
 */
public class NotExistingMediaObjectResource implements FileResource, PutableResource {

    Folder folder = null;
    Path path = null;
    String name = "";

    public NotExistingMediaObjectResource(Folder linkedFolder, Path path) {

        System.out.println("creating new object in cat: "+ linkedFolder.getCategoryId()+" path: "+path);

        this.folder = linkedFolder;
        this.path = path;
    }

    public Date getCreateDate() {
                System.out.println("createDate");
        return new Date();
    }

    public Resource createNew(String newName, InputStream inputStream, Long length, String contentType) throws IOException, ConflictException {

        int ivid = -1;
        name = newName;

        System.out.println("createNew");
        System.out.println("new Name: "+newName);
        System.out.println("length: "+length);
        System.out.println("content Type: "+length);

        InputStream is = new BufferedInputStream(inputStream);

        String olFileName = newName.replaceAll(" ","-");

        BufferedOutputStream os = new BufferedOutputStream(
                new FileOutputStream(Config.getTempPath()+File.separator+olFileName));

        byte[] buffer = new byte[1];
        try {
            while (is.read(buffer)!=-1) {
                os.write(buffer);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {

            ImportFactory importFactory = Config.importFactory;
            File importFile = new File(Config.getTempPath()+File.separator+olFileName);
            MediaImportHandler importHandler =
                    importFactory.createMediaImportHandler(
                            importFile);
            ivid = importHandler.processImport(importFile,0); //todo: user.getUserId()

            //todo Import-Fehler behandeln!
            //importFailure = (importFailure==0) ? importF : importFailure;


            File file = new File(Config.getTempPath()+File.separator+olFileName);
            file.delete();
            FolderService folderService = new FolderService();
            folderService.addMediaToFolder(folder.getCategoryId(),ivid);

        }  catch (MimeTypeNotSupportedException e) {
            System.out.println("Diese Datei wird nicht unterst�tzt");
        } catch (SizeExceedException e) {
            System.out.println("Maximale Bildanzahl wurde erreicht");
        } catch (DublicateEntry dublicateEntry) {
            System.out.println("Datei existiert bereits in dieser Kategorie");
        } catch (FileRejectException e) {
            System.out.println("FileRejectException in NotExistingMediaObjectResource.java");
        }

        MediaService imageService = new MediaService();
        MediaObjectMultiLang media = (MediaObjectMultiLang)imageService.getMediaObjectById(ivid);
        return new MediaObjectResource(folder,media);
    }

    public Resource child(String s) {
        System.out.println("child: s="+s);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<? extends Resource> getChildren() {
        System.out.println("getChildren");
        return new LinkedList<Resource>();
    }

    public String getUniqueId() {
        return String.valueOf(System.currentTimeMillis());  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getName() {
        return name;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object authenticate(String s, String s1) {
        return s;
    }

    public boolean authorise(Request request, Request.Method method, Auth auth) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getRealm() {
        return WebdavResourceFactory.REALM;
    }

    public Date getModifiedDate() {
        return new Date();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String checkRedirect(Request request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void copyTo(CollectionResource collectionResource, String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void delete() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendContent(OutputStream outputStream, Range range, Map<String, String> stringStringMap, String s) throws IOException, NotAuthorizedException, BadRequestException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getMaxAgeSeconds(Auth auth) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getContentType(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long getContentLength() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveTo(CollectionResource collectionResource, String s) throws ConflictException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String processForm(Map<String, String> stringStringMap, Map<String, FileItem> stringFileItemMap) throws BadRequestException, NotAuthorizedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}


