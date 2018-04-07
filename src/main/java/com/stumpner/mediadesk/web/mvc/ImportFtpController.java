package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.image.AutoMediaAssigner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.log4j.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.lic.LicenceChecker;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.exceptions.QuotaExceededException;
import com.stumpner.mediadesk.image.util.ImageImport;
import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.image.util.MetadataReadException;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.upload.util.FTPFileWrapper;
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
 * User: franzstumpner
 * Date: 26.04.2005
 * Time: 22:24:10
 * To change this template use File | Settings | File Templates.
 */
public class ImportFtpController extends ModelFormPageController {

    AutoMediaAssigner autoMediaAssigner = new AutoMediaAssigner();

    public ImportFtpController() {

        this.setCommandClass(ImportFtpCommand.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
        this.setValidateOnBinding(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_IMPORTER;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        LinkedList fileList = new LinkedList();
        FTPClient ftpClient = new FTPClient();

        ImportFtpCommand command = (ImportFtpCommand)e.getTarget();

        try {
            if (command.getFtpHost().trim().length()>0) {
                httpServletRequest.setAttribute("ftpConfigured",true);
                ftpClient.setAutodetectUTF8(true);
                ftpClient.connect(command.getFtpHost(),21);
                System.out.println("[mediaDESK-"+Config.instanceName+"] ftpClient.connect(host="+command.getFtpHost()+", user="+command.getFtpUser()+", "+"password=xxxxx)");
                boolean loginSuccess = ftpClient.login(command.getFtpUser(),command.getFtpPassword());
                if (loginSuccess) {
                    System.out.println("[mediaDESK-"+Config.instanceName+"] ftpClient.login, success = "+loginSuccess);
                    ftpClient.enterLocalPassiveMode();
                    for (FTPFile ftpFile : ftpClient.listFiles()) {
                        if (ftpFile.isFile()) {
                            if (!ftpFile.getName().startsWith(".")) {
                                System.out.println("Datei: "+ftpFile.getName()+ " size: "+ftpFile.getSize());
                                System.out.println("raw: "+ftpFile.getRawListing()+ " tostring: "+ftpFile.toString());
                                fileList.add(new FTPFileWrapper(ftpFile));
                            } else {
                                //System.out.println("- startet mit . - nicht verwenden: "+ftpFile.getName());
                            }
                        } else {
                            //System.out.println("- keine Datei: "+ftpFile.getName());
                        }
                    }
                    ftpClient.logout();
                    ftpClient.disconnect();
                } else {
                    //Login Fehler
                    httpServletRequest.setAttribute("ftpConnectError",true);
                    httpServletRequest.setAttribute("ftpLoginError",true);
                }
            } else {
                httpServletRequest.setAttribute("ftpConfigured",false);
            }
        } catch (Exception se) {
            System.out.println("Exception connect: "+se.getMessage());
            se.printStackTrace();
            httpServletRequest.setAttribute("ftpConnectError",true);
        }



        /*
        File importPath = new File(Config.fileSystemImportPath);
        File[] files = importPath.listFiles();
        LinkedList fileList = new LinkedList();
        for(int p=0;p<files.length;p++) {
            File file = files[p];
            if (file.isFile()) {
                if (!file.getName().startsWith(".") && !file.getName().equalsIgnoreCase("Thumbs.db")) {
                    fileList.add(new FTPFileWrapper(file));
                }
            }
            if (file.isDirectory()) {
                httpServletRequest.setAttribute("errorstr","imageimport.directoryexist");
            }
        } */

        httpServletRequest.setAttribute("fileList",fileList);

        LicenceChecker licenceChecker = new LicenceChecker();
        if (!licenceChecker.checkLicence()) {
            //lizenz ausgelaufen...
            throw new QuotaExceededException();
        } else {

        }

        //httpServletRequest.setAttribute("model",model);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBind(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        ImportFtpCommand command = (ImportFtpCommand)o;
        String[] importFiles = httpServletRequest.getParameterValues("importFile");
        command.setImportFiles(importFiles);
        if (httpServletRequest.getParameter("autoImportEnable")!=null) {
            command.setAutoImportEnabled(true);
        }
        if (httpServletRequest.getParameter("autoImportDisable")!=null) {
            command.setAutoImportEnabled(false);
        }
        super.onBind(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView processFormSubmission(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        User user = (User)httpServletRequest.getSession().getAttribute("user");
        ImportFtpCommand command = (ImportFtpCommand)o;

        if (httpServletRequest.getParameter("delete")!=null) {
            String[] importFiles = httpServletRequest.getParameterValues("importFile");
            if (importFiles!=null) {
                this.deleteFiles(importFiles,command);
                //httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("uploadfs"));
                e.reject("","Datei gelöscht");
                return super.processFormSubmission(httpServletRequest, httpServletResponse, o, e);
                //return null;
            } else {
                httpServletRequest.setAttribute("errorstr","imageimport.noimageselected");
                e.reject("imageimport.noimageselected","Keine Datei ausgewaehlt");
                this.setContentTemplateFile("imageimport_filesystem.jsp",httpServletRequest);
            }
            //this.setContentTemplateFile("imageimport_filesystem.jsp",httpServletRequest);
        } else if (httpServletRequest.getParameter("autoImportEnable")!=null ||
                httpServletRequest.getParameter("autoImportDisable")!=null) {
            //Autoimport setzen:
            Config.autoimportFtp = command.isAutoImportEnabled();
            Config.autoImportFtpCat = command.getAutoImportFtpCat();
            Config.saveConfiguration();
            e.reject("autoImport");
            this.setContentTemplateFile("imageimport_filesystem.jsp",httpServletRequest);
        } else {

            this.setContentTemplateFile("message_imageimport_filesystem.jsp",httpServletRequest);
            try {
                if (command.importFiles==null) {
                    //System.out.println("Keine Bilder angewählt");
                    httpServletRequest.setAttribute("errorstr","imageimport.noimageselected");
                    e.reject("imageimport.noimageselected","Kein Bild ausgeaehlt");
                } else {
                    logger.debug("Import images...");
                    this.importImages(command.importFiles,user.getUserId(), autoMediaAssigner.getAutoImportObject(httpServletRequest),command);
                }
            } catch (SizeExceedException err) {
                httpServletRequest.setAttribute("errorstr","imageimport.sizeexceed");
                e.reject("imageimport.sizeexceed","Maximale Bildgroesse ueberschritten");
                this.setContentTemplateFile("imageimport_filesystem.jsp",httpServletRequest);
            } catch (MetadataReadException err) {
                httpServletRequest.setAttribute("warning","ftp.warning.metadata");
            } catch (MimeTypeNotSupportedException err) {
                e.reject("imageimport.mimenotsupported",new String[] { err.getMimeType() },"Der Mimetype wird nicht unterstützt");
                httpServletRequest.setAttribute("errorstr","imageimport.mimenotsupported");
                httpServletRequest.setAttribute("errorstrArgs",err.getMimeType());
                this.setContentTemplateFile("imageimport_filesystem.jsp",httpServletRequest);
            }

        }
        httpServletRequest.setAttribute("model",model);

        if (e.hasErrors()) {
            return super.processFormSubmission(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
        }

        if (autoMediaAssigner.getAutoImportObject(httpServletRequest)!=null && !e.hasErrors()) {
            autoMediaAssigner.sendRedirectOfAutoImport(autoMediaAssigner.getAutoImportObject(httpServletRequest),httpServletResponse);
            autoMediaAssigner.clear(httpServletRequest);
            return null;
        } else {
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("lightbox"));
            return null;
        }
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }


    protected boolean isFormSubmission(HttpServletRequest httpServletRequest) {

        //model.remove("errorstr");
        //httpServletRequest.setAttribute("model",model);

        if (httpServletRequest.getParameter("delete")!=null || httpServletRequest.getParameter("submit")!=null || httpServletRequest.getParameter("autoImportEnable")!=null || httpServletRequest.getParameter("autoImportDisable")!=null) {
            return true;
        } else {
            return false;
        }



        //return super.isFormSubmission(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        //File importPath = new File(Config.fileSystemImportPath);
        //File[] files = importPath.listFiles();
        ImportFtpCommand fileList = new ImportFtpCommand();
        /*
        for(int p=0;p<files.length;p++) {

            File file = files[p];
            if (!file.getName().startsWith("."))
                fileList.getFileList().add(file);
        } */

        fileList.setAutoImportEnabled(Config.autoimportFtp);
        fileList.setAutoImportFtpCat(Config.autoImportFtpCat);

        FolderService folderService = new FolderService();
        fileList.setFolderList(folderService.getFolderList(0));

        //Hilfsweise das Objekt sofort in die Session speichern...
        HttpSession session = request.getSession();
        //session.setAttribute(getClass().getName()+".FORM."+this.getCommandName(),fileList);

        if (Config.ftpHost.length()>0) {
            //Ftp-Config von Config übernehmen
            fileList.setFtpHost(Config.ftpHost);
            fileList.setFtpUser(Config.ftpUser);
            fileList.setFtpPassword(Config.ftpPassword);

        } else {
            //Aus Request-Parameter
            fileList.setFtpHost(request.getParameter("ftpHost"));
            fileList.setFtpUser(request.getParameter("ftpUser"));
            fileList.setFtpPassword(request.getParameter("ftpPassword"));

            if (getUser(request).getRole()==User.ROLE_ADMIN) {
                if (request.getParameter("saveFtp")!=null) {
                    Config.ftpHost = fileList.getFtpHost();
                    Config.ftpUser = fileList.getFtpUser();
                    Config.ftpPassword = fileList.getFtpPassword();
                    Config.saveConfiguration();
                }
            }
        }

        return fileList;
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void importImages(String[] imageFiles,int userId,Object autoImportObject, ImportFtpCommand command) throws SizeExceedException, MetadataReadException, MimeTypeNotSupportedException, FileRejectException {

        Logger logger = Logger.getLogger(ImportFtpController.class);
        int importFailure = 0;
        int ivid = 0;

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(command.getFtpHost(),21);
            ftpClient.login(command.getFtpUser(),command.getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            /*
            for (int p=0;p<files.length;p++) {

                ftpClient.deleteFile(files[p]);
                System.out.println("delete file from ftp: "+files[p]);
            } */

            for (int p=0;p<imageFiles.length;p++) {

                FileOutputStream fos = null;

                try {
                    //dateinamen-umkonvertieren-auf-ohne-leerzeichen...
                    String olFileName = imageFiles[p].replaceAll(" ","-");
                    //logger.debug("BasicMediaObject-Import: Dateiname "+imageFiles[p]+" wird unbenannt in: "+olFileName+" (Operation: Leerzeichen entfernen)");
                    //File uFile = new File(Config.fileSystemImportPath+File.separator+imageFiles[p]);
                    File dFile = new File(Config.getTempPath()+File.separator+olFileName);
                    //FileUtil.copyFile(uFile,dFile);
                    fos = new FileOutputStream(dFile);
                    //System.out.println("[mediaDESK-"+Config.instanceName+"] dFile = "+dFile.getAbsolutePath());
                    boolean retr = ftpClient.retrieveFile(imageFiles[p],fos);
                    //System.out.println("[mediaDESK-"+Config.instanceName+"] ftpClient.retrieveFile = "+retr);
                    fos.close();
                    fos=null;

                    //int importF = ImageImport.processImage(Config.fileSystemImportPath+File.separator+olFileName,userId);
                    //ivid = ImageImport.getImportedIvid();

                        ImportFactory importFactory = Config.importFactory;
                        MediaImportHandler importHandler =
                                importFactory.createMediaImportHandler(
                                        dFile);
                        ivid = importHandler.processImport(dFile,userId);

                        //todo Import-Fehler behandeln!
                        //importFailure = (importFailure==0) ? importF : importFailure;


                    File file = new File(Config.getTempPath()+File.separator+olFileName);
                    file.delete();

                    //Automatischen import in eine Kategorie/Ordner prüfen, ansonsten in lightbox/favoriten schieben
                    autoMediaAssigner.assign(autoImportObject,ivid);

                    //File vom FTP-Server löschen
                    ftpClient.deleteFile(imageFiles[p]);

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    if (fos!=null) { fos.close(); }
                }

            }

            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException e) {
            importFailure = 99;
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        switch (importFailure) {
            case ImageImport.IMPORT_METADATA_ERROR:
                throw new MetadataReadException();
                //Probleme beim Importieren von Meta-Daten

           default: //;
        }

    }

    private void deleteFiles(String[] files, ImportFtpCommand command) {

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(command.getFtpHost(),21);
            ftpClient.login(command.getFtpUser(),command.getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            for (int p=0;p<files.length;p++) {

                ftpClient.deleteFile(files[p]);
                //System.out.println("delete file from ftp: "+files[p]);
            }
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
