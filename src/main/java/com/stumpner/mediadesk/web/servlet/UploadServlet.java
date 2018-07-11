package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.media.AutoMediaAssigner;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.folder.FolderMultiLang;
import com.stumpner.mediadesk.pin.Pin;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.security.acl.AclNotFoundException;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.lic.LicenceChecker;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.media.image.util.SizeExceedException;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import com.stumpner.mediadesk.util.FileUtil;
import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;
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
 * Date: 15.11.2006
 * Time: 19:22:30
 * To change this template use File | Settings | File Templates.
 */
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletRequest.setCharacterEncoding("UTF-8");
        
        AutoMediaAssigner assigner = new AutoMediaAssigner();

        //Bild über SuSIDE Desk importieren
        int httpErrorCode = 400;

       // Check that we have a file upload request
        Logger logger = Logger.getLogger(getClass());
        boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);
        if (isMultipart) {

            // Create a factory for disk-based file items
            logger.debug("Before factory");
            FileItemFactory factory = new DiskFileItemFactory();
            logger.debug("Before upload");
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try {
                List /*File Item*/ items = upload.parseRequest(httpServletRequest);

                //Authentication Credits
                String authUser = "";
                String authPass = "";
                String folderName = "";
                int folderId = -1;
                int ivid = -1;

                String urlPathInfo = httpServletRequest.getPathInfo();
                if (urlPathInfo.startsWith("/folder/")) {
                    //Upload in einen Ordner
                    String idString = urlPathInfo.substring("/folder/".length());
                    //System.out.println("idString = "+idString);
                    if (idString.endsWith("/")) { idString = idString.replaceAll("/",""); }
                    //System.out.println("bereinigt idString = "+idString);
                    folderId = Integer.parseInt(idString);
                }


                //Process Field Items
                //System.out.println("Process Field Items");
                Iterator formItems = items.iterator();
                while(formItems.hasNext()) {
                    FileItem item = (FileItem)formItems.next();
                    if (item.isFormField()) {
                        //System.out.println("ProcessField: "+item.getFieldName());
                        //Form Field
                        if (item.getFieldName().equalsIgnoreCase("USERNAME")) {
                            authUser = item.getString();
                        }
                        if (item.getFieldName().equalsIgnoreCase("PASSWORD")) {
                            authPass = item.getString();
                        }
                        if (item.getFieldName().equalsIgnoreCase("FOLDERNAME")) {
                            folderName = item.getString();
                        }
                        if (item.getFieldName().equalsIgnoreCase("FOLDERID")) {
                            folderId = Integer.parseInt(item.getString());
                        }
                    }
                }
                //System.out.println("After Process Field Items");
                Object autoImportObject = null;
                autoImportObject = createAutoImportObject(folderName, folderId);

                //Zugriff checken
                Authenticator auth = new Authenticator();
                boolean hasAccess = false;
                logger.debug("Check Authentication: [USER]="+authUser+",[PASS]="+authPass);
                User user = WebHelper.getUser(httpServletRequest); //Standard: Auth from Session or create visitor user
                if (auth.checkPassword(authUser,authPass)) {
                    //Auth from User and Password Parameter
                    UserService userService = new UserService();
                    try {
                        user = (User)userService.getByName(authUser);
                        if (user.getRole()>User.ROLE_EDITOR) hasAccess = true;
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                } else {
                    //Auth from Sesion
                    user = WebHelper.getUser(httpServletRequest);
                }
                //System.out.println("Check Session: id="+httpServletRequest.getSession().getId());

                int pinpicId = -1;
                if (!hasAccess) {
                    //Prüfen ob der Zugriff aus einem Pin kommt:
                    //Prüfen ob der Besucher in einem Pin ist und uploaden darf
                    if (user.getRole()==User.ROLE_UNDEFINED) {
                        if (WebHelper.isFromPinUploadContext(httpServletRequest)) {
                            Pin pin = WebHelper.getPinFromContext(httpServletRequest);
                            assigner.clear(httpServletRequest);
                            assigner.setDestination(httpServletRequest, pin);
                            pinpicId = pin.getPinId();
                            autoImportObject = assigner.getAutoImportObject(httpServletRequest);
                            hasAccess = true;
                        }
                    }
                }

                if (!hasAccess) {
                    //Prüfen ob der Zugriff aus einer Session kommt:
                    User sessionUser = WebHelper.getUser(httpServletRequest);
                    if (sessionUser.getRole()>=User.ROLE_IMPORTER) {
                        System.out.println("Upload, authentication from Session! [User="+sessionUser.getName()+"]");
                        if (autoImportObject==null) {
                            autoImportObject = assigner.getAutoImportObject(httpServletRequest);
                        }
                        //Prüfen ob Berechtigt
                        if (user.getRole()==User.ROLE_IMPORTER || user.getRole()==User.ROLE_EDITOR) {
                            //Prüfung auf ACL
                            Folder folder = (Folder)autoImportObject;
                            AclControllerContext aclCtx = AclContextFactory.getAclContext(httpServletRequest);
                            try {
                                hasAccess=aclCtx.checkPermission(new AclPermission("write"), folder);
                            } catch (AclNotFoundException e) {
                                //Kein Zugriff
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        } else {
                            //Alle anderen: Chef-Redakteur & Admin
                            hasAccess=true;
                        }
                    }
                }

                if (hasAccess) {
                    logger.debug("AUTH: OK!");
                    //System.out.println("Auth-OK");
                    //Authentication erfolgreich

                    // Process the uploaded items
                    boolean ok = false;
                    String errMsg = "";
                    Iterator iter = items.iterator();
                    while (iter.hasNext()) {
                        FileItem item = (FileItem) iter.next();

                        if (item.isFormField()) {
                            //processFormField(item);
                        } else {
                            //System.out.println("File-Field: "+item.getFieldName());
                            //processUploadedFile(item);
                            String fieldName = item.getFieldName();
                            String fileName = item.getName();
                            String contentType = item.getContentType();
                            boolean isInMemory = item.isInMemory();

                            long sizeInBytes = item.getSize();

                            MediaService mediaService = new MediaService();
                            LicenceChecker licenceChecker = new LicenceChecker();
                            int imageCount = mediaService.getMediaCount(); //Anzahl der Bilder in der Datenbank überpr.

                            //System.out.println("Before import");
                                if (sizeInBytes/1000<Config.maxFileSize) {
                                    //System.out.println("image size OK");
                                    //Nur Importieren wenn die Größe der Bilder erlaubt ist

                                    if (licenceChecker.checkLicence()) {
                                        //System.out.println("lic ok");
                                        //Nur Importieren wenn die anzahl der Bildlizenzen nicht überschritten wird
                                        File uploadedFile = new File(Config.getTempPath()+File.separator+fileName);
                                        //httpServletResponse.getWriter().print("File: "+fileName+"<br>");
                                        boolean writeSucceed = false;
                                        try {
                                            //System.out.println("Begin write...");
                                            item.write(uploadedFile);
                                            //System.out.println("write done");
                                            writeSucceed = true;
                                        } catch(Exception e) {
                                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                            ok = false;
                                            errMsg = "Exception "+e.getMessage();
                                            httpErrorCode = 500;
                                        }
                                        if (writeSucceed) {
                                        try {

                                            //bild importieren: (in die inbox)
                                            String olFileName = fileName.replaceAll(" ","-");
                                            //Original-Dateiname == Normalisierter Dateiname??
                                            if (!(Config.getTempPath()+File.separator+fileName).equalsIgnoreCase(
                                                    Config.getTempPath()+File.separator+olFileName
                                            )) {
                                                logger.debug("BasicMediaObject-Import RenFile: "+fileName+" TO: "+olFileName);
                                                //System.out.println("BasicMediaObject-Import Normalize File: "+fileName+" TO: "+olFileName);
                                                //todo: Dateiname musste normalisiert werden:
                                                File uFile = new File(Config.getTempPath()+File.separator+fileName);
                                                File dFile = new File(Config.getTempPath()+File.separator+olFileName);
                                                FileUtil.copyFile(uFile,dFile);
                                                uFile.delete();
                                            } else {
                                                //System.out.println("File is normalized, no rename");
                                            }

                                            /*
                                            try {
                                                ImageImport.copyFile(uFile,new File(Config.fileSystemImportPath+File.separator+olFileName));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            */

                                            //ImageImport.processImage(Config.fileSystemImportPath+File.separator+olFileName,user.getUserId());
                                            //todo:HandleImportError

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

                                            assigner.assign(autoImportObject,ivid);

                                            ok = true;
                                        } catch (MimeTypeNotSupportedException e) {
                                            httpServletRequest.getSession().setAttribute("lasterror",e);
                                            e.printStackTrace();
                                            ok = false;
                                            logger.debug("Diese Datei wird nicht unterstützt");
                                            errMsg = "Diese Datei wird nicht unterstützt";
                                            httpErrorCode = 415; //Http 415: Unsupported Mediatype
                                        } catch (SizeExceedException e) {
                                            httpServletRequest.getSession().setAttribute("lasterror",e);
                                            logger.debug("Maximale Bildanzahl/Speicher der Bilddatenbank wurde erreicht");
                                            errMsg = "Maximale Bildanzahl/Speicher der Datenbank wurde erreicht";
                                            ok = false;
                                            httpErrorCode = 507; //Http 507 Insufficient Storage
                                        } catch (FileRejectException e) {
                                            httpServletRequest.getSession().setAttribute("lasterror",e);
                                            logger.debug("FileRejected in UploadServlet.java");
                                            errMsg = "Rejected: "+e.getMessage();
                                            ok = false;
                                            httpErrorCode = 420; //Http 420 Policy Not Fulfilled
                                            //httpErrorCode = 500; //Internal Server Error
                                        }
                                        }
                                    } else {
                                        httpServletRequest.getSession().setAttribute("lasterror",new SizeExceedException((int)sizeInBytes/1000,Config.licMaxMediaObjects));
                                        logger.debug("Kein freier Speicherplatz");
                                        errMsg = "Out Of Space - Kein freier Speicherplatz";
                                        ok = false;
                                        httpErrorCode = 507; //Http 507 Insufficient Storage
                                    }
                                } else {
                                    httpServletRequest.getSession().setAttribute("lasterror",new SizeExceedException((int)sizeInBytes/1000,Config.licMaxMediaObjects));
                                    logger.debug("Hochgeladene Datei zu gross");
                                    errMsg = "File oversized! Datei ist zu groß: "+(sizeInBytes/1000000)+"MB ("+(Config.maxFileSize /1000)+"MB allowed) ";
                                    ok = false;
                                    httpErrorCode = 413; //Http 413: Request Entity Too Large
                                }
                        }
                    }

                    if (ok) {
                        httpServletResponse.setContentType("text/html");
                        httpServletResponse.getWriter().print(ivid+";OK");

                        logger.debug("File Uploaded");
                    } else {
                        System.out.println("UploadServlet.java ERROR: "+httpErrorCode + " "+ errMsg);
                        //httpServletResponse.sendError(httpErrorCode,errMsg);
                        httpServletResponse.setStatus(httpErrorCode);
                        httpServletResponse.setContentType("text/html");
                        httpServletResponse.getWriter().print(errMsg);
                        httpServletResponse.flushBuffer();
                        return;
                    }

                } else {
                    //User Authentication ist fehlgeschlagen
                    logger.error("UploadServlet: Authentication failure: CREDIT FALSE");
                    httpServletResponse.sendError(403,"Anmeldung ist fehlgeschlagen");                    
                }
            } catch (FileUploadException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                httpServletResponse.getWriter().print("ERROR");
            }

            } else {

                httpServletResponse.sendError(400,"Es wurden nicht die notwendigen Daten übergeben");

            }
    }

    private Object createAutoImportObject(String folderName, int folderId) {

        Object autoImportObject = null;
        if (!folderName.equalsIgnoreCase("")) {
            //in einen Ordner importieren
            FolderService folderService = new FolderService();
            FolderMultiLang folder = null;

            try {
                folder = (FolderMultiLang) folderService.getByName(folderName);
            } catch (ObjectNotFoundException e) {
                /*Ordner existiert noch nicht --> neue erstellen*/
                folder = new FolderMultiLang();
                folder.setParent(0);
                folder.setName(folderName);
                folder.setTitle(folderName);
                folder.setTitleLng1(folderName);
                folder.setTitleLng2(folderName);
                folder.setDescription("Autoimport");
                try {
                    folderService.addFolder(folder);
                    try {
                        folder = (FolderMultiLang) folderService.getByName(folderName);
                    } catch (ObjectNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOServiceException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (IOServiceException e) {
                e.printStackTrace();
            }
            autoImportObject = folder;
        }

        if (folderId!=-1) {
            //in einen ordner importieren
            if (folderId!=0) {
                FolderService folderService = new FolderService();
                try {
                    Folder folder = (FolderMultiLang) folderService.getById(folderId);
                    autoImportObject = folder;
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else {
                // Root Folder
                Folder c = new FolderMultiLang();
                c.setFolderId(0);
                autoImportObject = c;
            }
        }

        return autoImportObject;  //To change body of created methods use File | Settings | File Templates.
    }


}
