package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.PinService;
import com.stumpner.mediadesk.media.AutoMediaAssigner;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.pin.Pin;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.util.WebFileUploadBean;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.lic.LicenceChecker;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.QuotaExceededException;
import com.stumpner.mediadesk.media.image.util.ImageImport;
import com.stumpner.mediadesk.media.image.util.SizeExceedException;
import com.stumpner.mediadesk.media.image.util.MetadataReadException;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;
import com.stumpner.mediadesk.upload.FileRejectException;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import java.security.acl.AclNotFoundException;

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
 * User: franzstumpner
 * Date: 08.05.2005
 * Time: 21:20:56
 * To change this template use File | Settings | File Templates.
 */
public class UploadWebController extends ModelFormPageController {

    AutoMediaAssigner autoMediaAssigner = new AutoMediaAssigner();

    public UploadWebController() {
        this.setCommandClass(WebFileUploadBean.class);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_IMPORTER;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!isUserPermitted(request)) {
            //Nicht erlaubt
            if (getUser(request).getRole()<User.ROLE_HOME_EDITOR) {
                //Redirecten auf einen Auswahlseite mit Ordnern in die der Benutzer hochladen darf
                response.sendRedirect(response.encodeRedirectURL("uploadselector"));
                return null;
            } else {
                response.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
                return null;
            }
        }

        if (Config.webdavEnabled) {
            request.setAttribute("webdavEnabled",Config.webdavEnabled);
            request.setAttribute("serverName",request.getServerName());            
            request.setAttribute("httpScheme",request.getScheme());
        }

        return super.handleRequestInternal(request, response);
    }

    protected boolean isUserPermitted(HttpServletRequest request) {

        //Prüfen ob der Besucher erlaubt ist
        if (!super.isUserPermitted(request)) {
            //Der Benutzer laut Rolle nicht erlaubt
            //Prüfen ob der Besucher in einem Pin ist und uploaden darf
            if (WebHelper.isFromPinUploadContext(request)) {
                return true;
            }
            return false;
        } else {

            if (getUser(request).getRole()<User.ROLE_HOME_EDITOR) {
                if (WebHelper.isFromPinUploadContext(request)) {
                    return true;
                } else {
                    //Lieferant, Redakteur von den ACL Settings abhängig aber grundsätzlich erlaubt
                    // --> es wird dann eine auswahlliste an Ordnern angezeigt
                    return isUserPermittetForFolder(request);
                }

            } else {
                //Mandant, Chef Redakteur und Admin Upload immer erlaubt
                return true;
            }
        }
    }

    private boolean isUserPermittetForFolder(HttpServletRequest request) {

        try {
            Folder folder = getImportFolder(request);
            if (folder ==null) { return false; }
            AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
            return aclCtx.checkPermission(new AclPermission("write"), folder);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (AclNotFoundException e) {
            e.printStackTrace();
        } catch (IOServiceException e) {
            e.printStackTrace();
        }
        return false;

    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException e) throws Exception {

        autoMediaAssigner = new AutoMediaAssigner();
        if ("".equalsIgnoreCase(request.getParameter("catid")) && "".equalsIgnoreCase(request.getParameter("pinid"))) {
            autoMediaAssigner.clear(request);
        }
        if (request.getParameter("catid")!=null) {
            if (!request.getParameter("catid").equalsIgnoreCase("")) {
                autoMediaAssigner.clear(request);
                Folder folder = getImportFolder(request);
                request.setAttribute("folder", folder);
                if (folder !=null) { autoMediaAssigner.setDestination(request, folder); }
            }
        }
        if (request.getParameter("pinid")!=null) {
            if (!request.getParameter("pinid").equalsIgnoreCase("")) {
                //Dateien automatich in den Pin laden...
                autoMediaAssigner.clear(request);
                PinService pinService = new PinService();
                Pin pin = (Pin)pinService.getById(Integer.parseInt(request.getParameter("pinid")));
                autoMediaAssigner.setDestination(request,pin);
            }
        }

        if (!isUserPermitted(request)) {
            response.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        if (request.getParameter("error")!=null) {
            //Ein Fehler trat beim Flash-Upload auf und wurde auf diesen Controller umgeleitet:
            /*
              a) Dateigröße überschritten: HTTP 413: Request Entity Too Large /
              b) Kein freier Speicherplatz: HTTP 507 Insufficient Storage /
              c) Medientyp wird nicht unterstützt: HTTP 415: Unsupported Media Type
             */
            final int errorCode = Integer.parseInt(request.getParameter("error"));
            String errorMessage = "";
            String[] errorArgs = null;
            try {
                switch (errorCode) {
                    case 413: errorMessage = "web.upload.error.413"; errorArgs = new String[] {Integer.toString(Config.maxImageSize/1000)};  break;
                    case 415: errorMessage = "web.upload.error.415"; break;
                    case 407: errorMessage = "web.upload.error.407"; break;
                    case 507: errorMessage = "web.upload.error.507"; break;
                    case 500: errorMessage = "web.upload.error.500"; break;
                }
                MailWrapper.sendErrorReport(request, null, "Uploader: HTTP Fehlercode: "+errorMessage);
            } catch (NumberFormatException ne) {
                //Error-Code ist kein Zahlen wert = Es handelt sich um einen Flash IO Fehlercode
                errorMessage = "web.upload.error.flashio";
                errorArgs = new String[] {request.getParameter("error")};
                MailWrapper.sendErrorReport(request, null, "Uploader: Flash IO Fehler (Verbindung zurückgesetzt): "+request.getParameter("error"));
            }
            request.setAttribute("errorMessage",errorMessage);
            request.setAttribute("errorArgs",errorArgs);

        }
        LicenceChecker licenceChecker = new LicenceChecker();
        if (!licenceChecker.checkLicence()) {
            //lizenz ausgelaufen...
            throw new QuotaExceededException();
        } else {

            Object autoImportObject = autoMediaAssigner.getAutoImportObject(request);
            if (autoImportObject!=null) {
                request.setAttribute("redirectUrl", autoMediaAssigner.getRedirectOfAutoImport(autoImportObject));
            } else {
                request.setAttribute("redirectUrl","lightbox");
            }
        }

        return super.showForm(request, response, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        WebFileUploadBean upload = (WebFileUploadBean)o;
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        this.setContentTemplateFile("message_imageimport_web.jsp",httpServletRequest);
        boolean emptyUpload=true;

        Map map = httpServletRequest.getParameterMap();
        Iterator sets  = map.keySet().iterator();
        while (sets.hasNext()) {
            String set = (String)sets.next();
            //System.out.println("KEY: "+set);
            //System.out.println("VALUE: "+httpServletRequest.getParameter(set));
        }

        try {
                 /*
            try {
                if (upload.getUp().getOriginalFilename().length()!=0) {
                    doFileUpload(upload.getUp(),user,autoMediaAssigner.getAutoImportObject(httpServletRequest));
                    emptyUpload=false;
                }
            } catch (MetadataReadException ex) { httpServletRequest.setAttribute("warning","web.warning.metadata"); ex.printStackTrace(); }*/
            try {
                if (upload.getFile()!=null) {
                    if (upload.getFile().getOriginalFilename().length()!=0) {
                        doFileUpload(upload.getFile(),user, autoMediaAssigner.getAutoImportObject(httpServletRequest));
                        emptyUpload=false;
                    }
                }
            } catch (MetadataReadException ex) { httpServletRequest.setAttribute("warning","web.warning.metadata"); ex.printStackTrace(); }
            try {
                if (upload.getFile2()!=null) {
                    if (upload.getFile2().getOriginalFilename().length()!=0) {
                        doFileUpload(upload.getFile2(),user, autoMediaAssigner.getAutoImportObject(httpServletRequest));
                        emptyUpload=false;
                    }
                }
            } catch (MetadataReadException ex) { httpServletRequest.setAttribute("warning","web.warning.metadata"); ex.printStackTrace(); }
            try {
                if (upload.getFile3()!=null) {
                    if (upload.getFile3().getOriginalFilename().length()!=0) {
                        doFileUpload(upload.getFile3(),user, autoMediaAssigner.getAutoImportObject(httpServletRequest));
                        emptyUpload=false;
                    }
                }
            } catch (MetadataReadException ex) { httpServletRequest.setAttribute("warning","web.warning.metadata"); ex.printStackTrace(); }

        } catch (SizeExceedException err) {

            this.setContentTemplateFile("imageimport_web.jsp",httpServletRequest);
            e.reject("imageimport.sizeexceed","Maximale Bildgroesse ueberschritten");
            return super.onSubmit(httpServletRequest, httpServletResponse, o, e);
        } catch (MimeTypeNotSupportedException err) {

            File file = err.getFile();
            //Datei löschen:
            //System.out.println("Datei löschen: "+file.getAbsolutePath());
            System.out.println("Datei gelöscht: "+file.delete());

            this.setContentTemplateFile("imageimport_web.jsp",httpServletRequest);
            e.reject("imageimport.mimenotsupported",new String[] { err.getMimeType() },"Der Mimetype wird nicht unterstützt");

            return super.onSubmit(httpServletRequest, httpServletResponse, o, e);
        }

        if (emptyUpload) {
            e.reject("imageimport.emptyimport","Keine Bilder zum Upload angegeben");
            this.setContentTemplateFile("imageimport_web.jsp",httpServletRequest);
            return super.onSubmit(httpServletRequest, httpServletResponse, o, e);
        }

        Object autoImportObject = autoMediaAssigner.getAutoImportObject(httpServletRequest);
        if (autoImportObject!=null) {
            autoMediaAssigner.sendRedirectOfAutoImport(autoImportObject,httpServletResponse);
            autoMediaAssigner.clear(httpServletRequest);
            return null;
        } else {
            //in die Lightbox (bis 3.2 Inbox) redirecten
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("lightbox"));
            return null;
        }
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {

        if (WebHelper.isFromPinUploadContext(httpServletRequest)) {

//            Pin pin = WebHelper.getPinFromContext(httpServletRequest);

//            AutoMediaAssigner assigner = new AutoMediaAssigner();
//            assigner.clear(httpServletRequest);
//            assigner.setDestination(httpServletRequest, pin);
//            System.out.println("Assigner.setDestination is PIN ");

        }
        servletRequestDataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    private void doFileUpload(MultipartFile uploadFile, User user, Object autoImportObject) throws MetadataReadException, SizeExceedException, IOException, MimeTypeNotSupportedException, FileRejectException {

        byte[] uploadContent = uploadFile.getBytes();

        String olFileName = uploadFile.getOriginalFilename().replaceAll(" ","-");

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(Config.getTempPath()+File.separator+olFileName));
        bos.write(uploadContent);
        bos.flush();
        bos.close();

        //final int importFailure = ImageImport.processImage(Config.fileSystemImportPath+File.separator+olFileName,user.getUserId());
        //todo: handle Import error
        int importFailure = 0;
        int ivid = 0;
                ImportFactory importFactory = Config.importFactory;
                File importFile = new File(Config.getTempPath()+File.separator+olFileName);
                MediaImportHandler importHandler =
                        importFactory.createMediaImportHandler(
                                importFile);
        if (autoImportObject instanceof Pin) {
            Pin pin = (Pin)autoImportObject;
            ivid = importHandler.processImport(importFile, pin.getCreatorUserId());
        } else {
                ivid = importHandler.processImport(importFile,user.getUserId());
        }

                //todo Import-Fehler behandeln!


        //Checken ob das Bild automatisch in eine Kategorie/Ordner zugewiesen werden soll, ansonsten in lightbox verschieben
        autoMediaAssigner.assign(autoImportObject,ivid);

        File file = new File(Config.getTempPath()+File.separator+olFileName);
        file.delete();

        switch (importFailure) {
            case ImageImport.IMPORT_METADATA_ERROR:
                throw new MetadataReadException();
                //Probleme beim Importieren von Meta-Daten

           default: //;
        }
    }

    private Folder getImportFolder(HttpServletRequest request) throws ObjectNotFoundException, IOServiceException {

        if (request.getParameter("catid")!=null) {
            if (!request.getParameter("catid").equalsIgnoreCase("")) {
                //bilder automatisch in eine kategorie laden...
                FolderService folderService = new FolderService();
                Folder folder = new Folder();
                if (request.getParameter("catid").equalsIgnoreCase("0")) {
                    //Root-Kategory existiert nicht...
                    folder.setCategoryId(0);
                } else {
                    folder = folderService.getFolderById(Integer.parseInt(
                            request.getParameter("catid")
                    ));
                }
                return folder;
            }
        } else {
            Object autoImportObject = autoMediaAssigner.getAutoImportObject(request);
            //System.out.println("auto import object: "+autoImportObject.getClass().getName());
            return (Folder)autoImportObject;
        }

        return null;
    }

}
