package com.stumpner.mediadesk.image.util;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.usermanagement.User;

import java.io.*;

import com.stumpner.mediadesk.media.importing.ImportFactory;
import com.stumpner.mediadesk.media.importing.MediaImportHandler;
import com.stumpner.mediadesk.media.MimeTypeNotSupportedException;
import com.stumpner.mediadesk.upload.FileRejectException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;

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
 * Date: 11.08.2009
 * Time: 20:23:32
 *
 * Diese Klasse überprüft ein Verzeichnis ob neue Dateien hinzugekommen sind und importiert diese dann in die
 * Bildatenbank
 */
public class AutoImporter {

    private static AutoImporter autoImporter = null;
    private String path = "";
    private File[] fileList = null;

    public static AutoImporter getInstance(String path) {
        if (autoImporter==null) {
            autoImporter = new AutoImporter(path);
        }
        return autoImporter;
    }

    private AutoImporter(String path) {
        this.path = path;
    }

    /**
     * Check and import new Files
     */
    public void check() {

        if (Config.autoimportFtp) {
            if (isFtpHostValid()) {
                /*
                File file = new File(path);
                fileList = file.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        if (name.startsWith(".")) {
                            System.out.println("AutoImport: name = "+name);
                            System.out.println("AutoImport: dir.getName() = "+dir.getName());
                            System.out.println("startswidth . = true");
                            return false;  //To change body of implemented methods use File | Settings | File Templates.
                        } else {
                            System.out.println("AutoImport: name = "+name);
                            System.out.println("AutoImport: dir.getName() = "+dir.getName());
                            System.out.println("startswidth . = false");
                            return true;
                        }
                    }
                });*/
                //Alle Dateien in der Liste durchgehen und prüfen ob sich die Datei importiert werden kann...
                FTPClient ftpClient = new FTPClient();
                try {
                    ftpClient.setAutodetectUTF8(true);
                    ftpClient.connect(Config.ftpHost,21);
                    ftpClient.login(Config.ftpUser,Config.ftpPassword);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                    for (FTPFile file : ftpClient.listFiles()) {
                    //for (int a=0;a<fileList.length;a++) {
                        //if (isFileReadyToImport(fileList[a])) {
                        if (file.isFile() && !file.getName().startsWith(".")) {
                            if (file.getSize()>0) {
                                System.out.println("FTP-AutoImport: "+file.getName());
                                int ivid = importImage(file, ftpClient);
                                //in kategorie oder folder importieren
                                FolderService folderService = new FolderService();
                                try {
                                    folderService.addMediaToFolder(Config.autoImportFtpCat,ivid);
                                } catch (DublicateEntry dublicateEntry) {
                                    dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                            }


                        }
                        //}
                    }

                    ftpClient.logout();
                    ftpClient.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

    }

    private int importImage(FTPFile file, FTPClient ftpClient) {

        FileOutputStream fos = null;

        int ivid = -1;
            //dateinamen-umkonvertieren-auf-ohne-leerzeichen...
            String olFileName = file.getName().replaceAll(" ","-");
            //logger.debug("Image-Import: Dateiname "+imageFiles[p]+" wird unbenannt in: "+olFileName+" (Operation: Leerzeichen entfernen)");
            //File uFile = new File(Config.fileSystemImportPath+File.separator+imageFiles[p]);

            //int importF = ImageImport.processImage(Config.fileSystemImportPath+File.separator+olFileName,userId);
            //ivid = ImageImport.getImportedIvid();

            try {

                ImportFactory importFactory = Config.importFactory;
                File importFile = new File(Config.getTempPath()+File.separator+olFileName);

                fos = new FileOutputStream(importFile);
                ftpClient.retrieveFile(file.getName(),fos);
                fos.close();
                fos=null;

                MediaImportHandler importHandler =
                        importFactory.createMediaImportHandler(
                                importFile);
                ivid = importHandler.processImport(importFile,getAutoImportUserId());

                importFile.delete();
                ftpClient.deleteFile(file.getName());


            } catch (MimeTypeNotSupportedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SizeExceedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (FileRejectException e) {
                e.printStackTrace();
                System.out.println("FileRejectException in AutoImporter.java");
            }

        return ivid;
            //todo Import-Fehler behandeln!
                //importFailure = (importFailure==0) ? importF : importFailure;


            //File file = new File(Config.fileSystemImportPath+File.separator+olFileName);


    }

    private int getAutoImportUserId() {

        UserService userService= new UserService();
        try {
            User user = (User)userService.getByName("admin");
            return user.getUserId();
        } catch (ObjectNotFoundException e) {
            return 0;
        } catch (IOServiceException e) {
            return 0;
        }

    }

    /**
     * Überprüft ob die angegebene Datei abgespeichert ist (fertig übertragen) und importiert werden kann
     * @param file
     * @return
     */
    private boolean isFileReadyToImport(File file) {

        String fileName = file.getAbsolutePath();
        if (!fileName.endsWith(".ready")) {
            String renFileName = fileName + ".ready";
            File renFile = new File(renFileName);
            if (file.renameTo(renFile)) {
                if (renFile.renameTo(new File(fileName))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //Datei konnte nicht umbenannt werden -> noch mit einem Schreibpointer belegt? -> nicht importieren
                return false;
            }
        } else {
            //Wenn die Datei mit .ready aufhört, dann immer false zurückgeben (sicherheitsmechanismus)
            return false;
        }
    }

    public boolean isFtpHostValid() {

        if (Config.ftpHost.trim().length()==0) return false;

        if (Config.ftpHost.startsWith("http://")) return false;

        if (Config.ftpHost.startsWith("https://")) return false;

        if (Config.ftpHost.startsWith("ftp://")) return false;

        return true;
    }
}
