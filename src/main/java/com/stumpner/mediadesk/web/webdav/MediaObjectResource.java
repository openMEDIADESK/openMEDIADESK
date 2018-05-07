package com.stumpner.mediadesk.web.webdav;

import com.bradmcevoy.http.*;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;

import java.util.Date;
import java.util.Map;
import java.util.Iterator;
import java.io.*;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;
import net.stumpner.security.acl.AclControllerContext;

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
 * Time: 18:56:10
 * To change this template use File | Settings | File Templates.
 */
public class MediaObjectResource implements FileResource {

    User user = null;
    AclControllerContext aclCtx = null;
    Folder linkedFolder = null;
    MediaObjectMultiLang media = null;
    String ip = "";
    String dns = "";

    public MediaObjectResource(Folder linkedFolder, MediaObjectMultiLang media) {
        this.linkedFolder = linkedFolder;
        this.media = media;
    }

    public void copyTo(CollectionResource collectionResource, String s) {
        System.out.println("Webdav copyTo Request: ["+media.getVersionName()+"] linkedFolder="+ linkedFolder.getFolderId()+" - NOT IMPLEMENTED");
    }

    public String getUniqueId() {
        return String.valueOf("media"+media.getIvid());
    }

    public int getIvid() {
        return media.getIvid();
    }

    public String getName() {
        if (!media.getVersionName().equalsIgnoreCase("")) {
            String filename = media.getVersionName();
            if (!filename.toUpperCase().endsWith("."+media.getExtention().toUpperCase())) {
                filename = filename +"."+media.getExtention();
            }
            return filename;
        } else {
            return "media"+media.getIvid()+"."+media.getExtention();
        }
    }

    public Object authenticate(String s, String s1) {

        return s;
    }

    public boolean authorise(Request request, Request.Method method, Auth auth) {

        user = WebdavResourceFactory.getUsernameFromAuthHeader(request,method,auth);
        aclCtx = WebdavResourceFactory.createAclContext(user);
        ip = request.getRemoteAddr();
        dns = request.getRemoteAddr();
        return WebdavResourceFactory.authroise(request, linkedFolder,user,aclCtx);
    }

    public String getRealm() {
        return WebdavResourceFactory.REALM;
    }

    public Date getModifiedDate() {
        return media.getCreateDate();
    }

    public String checkRedirect(Request request) {
        System.out.println("Webdav checkRedirect Request: ["+media.getVersionName()+"] linkedFolder="+ linkedFolder.getFolderId()+"");
        return null;
    }

    public void delete() {

        if (user.getRole()>=User.ROLE_EDITOR) { //Erst ab Rolle Editor darf jemand neue Objekte anlegen
            System.out.println("Webdav Delete Request: ["+media.getVersionName()+"] linkedFolder="+ linkedFolder.getFolderId());
            FolderService folderService = new FolderService();
            folderService.deleteMediaFromFolder(linkedFolder.getFolderId(),media.getIvid());
        }
    }

    public void sendContent(OutputStream outputStream, Range range, Map<String, String> stringStringMap, String s) throws IOException, NotAuthorizedException, BadRequestException {

        System.out.println("Webdav sendContent Request: ["+media.getVersionName()+"] linkedFolder="+ linkedFolder.getFolderId()+"");
        String filename = Config.imageStorePath+"/"+media.getIvid()+"_0";

        InputStream is = new BufferedInputStream(new FileInputStream(new File(filename)));
        OutputStream os = new BufferedOutputStream(outputStream);
        int bytesSent = 0;
        byte[] buffer = new byte[1];
        try {
            while (is.read(buffer)!=-1) {
                os.write(buffer);
                bytesSent++;
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //todo: track download

        DownloadLoggerService dlls2 = new DownloadLoggerService();
        SimpleDownloadLogger log = new SimpleDownloadLogger();
        log.setDownloadtype(SimpleDownloadLogger.DTYPE_WEBDAV);
        log.setUserId(user.getUserId());
        log.setIvid(media.getIvid());
        log.setName(media.getVersionName());
        log.setIp(ip);
        log.setDns(dns);
        log.setBytes(bytesSent);
        log.setDownloadDate(new Date());
        dlls2.log(log);

    }

    public Long getMaxAgeSeconds(Auth auth) {
        return Long.valueOf(100000);
    }

    public String getContentType(String s) {
        return media.getMayorMime();

    }

    public Long getContentLength() {
        String filename = Config.imageStorePath+"/"+media.getIvid()+"_0";
        File file = new File(filename);
        return file.length();
    }

    public void moveTo(CollectionResource collectionResource, String s) throws ConflictException {

        if (user.getRole()>=User.ROLE_EDITOR) { //Erst ab Rolle Editor darf jemand Objekte umbenennen/verschieben

            int moveToCategoryId = Integer.parseInt(collectionResource.getUniqueId());
            MediaService mediaService = new MediaService();
            FolderService folderService = new FolderService();
            if (linkedFolder.getFolderId()==moveToCategoryId) {
                //Umbenennen
                if (media.getVersionTitle().equalsIgnoreCase(media.getVersionName())) {
                    media.setVersionTitle(s);
                }
                if (media.getVersionTitleLng1().equalsIgnoreCase(media.getVersionName())) {
                    media.setVersionTitleLng1(s);
                }
                if (media.getVersionTitleLng2().equalsIgnoreCase(media.getVersionName())) {
                    media.setVersionTitleLng2(s);
                }
                media.setVersionName(s);
                try {
                    mediaService.saveMediaObject(media);
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            } else {
                //Verschieben
                try {
                    folderService.addMediaToFolder(moveToCategoryId,media.getIvid());
                    folderService.deleteMediaFromFolder(linkedFolder,media);
                } catch (DublicateEntry dublicateEntry) {
                    dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }

        System.out.println("Webdav moveTo Request: ["+media.getVersionName()+"] linkedFolder="+ linkedFolder.getFolderId()+" - NOT IMPLEMENTED");
    }

    public String processForm(Map<String, String> stringStringMap, Map<String, FileItem> stringFileItemMap) throws BadRequestException, NotAuthorizedException {

        System.out.println("processForm");
        Iterator<Map.Entry<String, FileItem>> itE = stringFileItemMap.entrySet().iterator();
        while (itE.hasNext()) {

            Map.Entry<String, FileItem> entry = itE.next();
            FileItem fileItem = entry.getValue();
            System.out.println("itE.next: "+fileItem.getName());

        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Date getCreateDate() {
        return media.getCreateDate();
    }

    public MediaObject getMediaObject() {
        return media;
    }
}
