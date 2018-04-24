package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.image.pinpics.Pin;
import com.stumpner.mediadesk.util.Zip;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.image.CartObject;
import com.stumpner.mediadesk.image.util.ImageMagickUtil;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclUtil;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.LocaleResolver;
import com.stumpner.mediadesk.web.mvc.commandclass.FormatSelector;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.mvc.LoginController;
import com.stumpner.mediadesk.stats.SimpleDownloadLogger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.util.List;
import java.text.MessageFormat;
import java.awt.*;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
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
 * User: franzstumpner
 * Date: 17.05.2005
 * Time: 23:16:13
 * To change this template use File | Settings | File Templates.
 */
public class DownloadServlet extends HttpServlet {

    private final static int DOWNLOAD_TYPE_ERROR = 0;
    private final static int DOWNLOAD_TYPE_IMAGES = 1;
    private final static int DOWNLOAD_TYPE_PINPIC = 2;
    private static final int DOWNLOAD_SINGLE = 3;

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        if (getDownloadType(httpServletRequest)==DOWNLOAD_TYPE_PINPIC) {
        //- PIN PIC DOWNLOAD
            List imageList = this.getImageList(httpServletRequest);
            if (imageList.size()==1) {
                singleDownload(httpServletRequest,httpServletResponse);
            } else {
                zipDownload(httpServletRequest,httpServletResponse);
            }

        } else {
        //- NORMALER DOWNLOAD
            if (getDownloadType(httpServletRequest)==DOWNLOAD_SINGLE) {
                singleDownload(httpServletRequest,httpServletResponse);
            } else {
                zipDownload(httpServletRequest,httpServletResponse);
            }
        }


        //super.doGet(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void singleDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        final long uniqueId = System.currentTimeMillis();
        httpServletRequest.setAttribute("uniqueId",uniqueId);
        boolean ividIsInImageList = true;
        //int ivid = Integer.parseInt(httpServletRequest.getParameter("ivid"));
        MediaService imageService = new MediaService();
        //MediaObject image = imageService.getMediaObjectById(ivid);

        List downloadImageList = getImageList(httpServletRequest);
        if (downloadImageList!=null) {
            //Es ist ein Medienobjekt ausgewählt
            if (downloadImageList.size()==0) {
                //Wenn sich keine Objekte in der Liste befinden, fehlt die Berechtigung
                httpServletResponse.sendError(403,"Access Denied for Download");
                return;
            }
            MediaObjectMultiLang image = (MediaObjectMultiLang)downloadImageList.get(0);

            List permittedDownloadImageList = null;
            if (getDownloadType(httpServletRequest)==DOWNLOAD_TYPE_PINPIC) {

                permittedDownloadImageList = downloadImageList;

            } else {

                AclControllerContext aclContext = AclContextFactory.getAclContext(httpServletRequest);
                permittedDownloadImageList = getPermittedImages(httpServletRequest, aclContext, downloadImageList);

            }

            if (canDownload(httpServletRequest,permittedDownloadImageList) && permittedDownloadImageList.size()==1) {

                String filename = Config.imageStorePath+"/"+image.getIvid()+"_0";
                //Prüfen ob die Bilddatenbank mit Format-Selector (benutzerdefinierte Auflösungen) arbeitet:
                if (isSpecialDownloadResolution(httpServletRequest)) {
                    filename = handleSpecialDownloadResolution(image,httpServletRequest);
                }

                File file = new File(filename);
                String downloadFilename = "";
                if (Config.downloadImageFilename.equalsIgnoreCase("imageNumber")) {
                    downloadFilename = image.getImageNumber();
                }
                if (Config.downloadImageFilename.equalsIgnoreCase("versionTitle")) {
                    downloadFilename = image.getVersionTitle().replaceAll(" ","_");
                }
                if (Config.downloadImageFilename.equalsIgnoreCase("versionName")) {
                    downloadFilename = image.getVersionName();
                }
                if (!downloadFilename.toUpperCase().endsWith("."+image.getExtention().toUpperCase())) {
                    downloadFilename = downloadFilename + "." + image.getExtention();
                }

                httpServletResponse.setHeader("Content-Description","File Transfer");
                httpServletResponse.setHeader("Cache-Control","no-store");     //HTTP 1.1
                httpServletResponse.setHeader("Pragma","no-cache"); //HTTP 1.0
                httpServletResponse.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                httpServletResponse.addHeader("Content-Length", Long.toString(file.length()));
                httpServletResponse.setContentType(image.getMimeType());
                httpServletResponse.setHeader("Content-Disposition","attachment; filename=\""+downloadFilename+"\"");


                OutputStream outputStream = httpServletResponse.getOutputStream();
                InputStream inputStream = new FileInputStream(filename);

                byte b[] = new byte[512];
                int len=0;
                while((len=inputStream.read(b)) != -1) {
                    outputStream.write(b,0,len);
                }
                outputStream.close();
                inputStream.close();

                if (Config.informDownloadAdmin) {
                    this.informDownload(httpServletRequest,downloadImageList);
                }

                this.trackDownload(httpServletRequest,downloadImageList);

                //Wenn eine Spezial-Auflösung zum Download angeboten wurde, müssen die Temp-Files gelöscht werden
                if (isSpecialDownloadResolution(httpServletRequest)) {

                        final List downloadImageListFinal = downloadImageList;

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {

                            public void run() {
                                cleanUpTempFiles(downloadImageListFinal,uniqueId);
                            }
                        }, 60*1000);
                }

                this.cleanUpDownload(httpServletRequest,downloadImageList);

                if (Config.useShoppingCart) {
                    //Wenn der ShoppingCart benutzt wurde das Objekt entfernen
                    User user = WebHelper.getUser(httpServletRequest);
                    if (user.getRole()>= User.ROLE_USER) {
                        ShoppingCartService scs = new ShoppingCartService();
                        scs.removeImageToShoppingCart(image.getIvid(), user.getUserId());
                    }
                }

            } else {

                httpServletResponse.sendError(403,"No Permission/No Credits");
            }
        } else {
            //Es ist kein Medienobjekt ausgewählt
            httpServletResponse.sendError(404);
        }

    }

    private void zipDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        long uniqueId = System.currentTimeMillis();
        httpServletRequest.setAttribute("uniqueId",uniqueId);

        GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
        String filePrefix = calendar.get(Calendar.DAY_OF_YEAR) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE);
        //httpServletResponse.setHeader("Content-Description:","File Transfer");

        String fileName = "download";
        if (getDownloadType(httpServletRequest)==DOWNLOAD_TYPE_IMAGES) {
            fileName = "download"+filePrefix;
        }
        if (getDownloadType(httpServletRequest)==DOWNLOAD_TYPE_PINPIC) {

                int pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
                PinpicService pinpicService = new PinpicService();
                Pin pinPic = new Pin();
            try {
                pinPic = (Pin)pinpicService.getById(pinId);

                String fileNameWithoutSpace = pinPic.getPinpicTitle().replace(' ','_');
                if (fileNameWithoutSpace.length()>0) {
                    fileName = fileNameWithoutSpace;
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


        //User user = (User)httpServletRequest.getSession().getAttribute("user");

        List downloadImageList = this.getImageList(httpServletRequest);
        //System.out.println("Download-Images: "+downloadImageList.size());
        if (downloadImageList.size()==0 && getDownloadType(httpServletRequest)==DownloadServlet.DOWNLOAD_TYPE_PINPIC) {
            LocaleResolver localeResolver = new LocaleResolver();
            Locale locale = localeResolver.resolveLocale(httpServletRequest);
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("/"+locale.getLanguage()+"/pinview?error=noimages"));
            return;
        }

        if (downloadImageList.size()==0) {
            System.out.println("Fehler in DownloadServlet:242 Download-BasicMediaObject-Size=0");
            //TODO: Eingebaut um den Fehler tracken zu können

            if (WebHelper.isLoggedIn(httpServletRequest)==false) {

                //Nicht eingeloggt ---> zur loginpage
                httpServletRequest.getSession().setAttribute(LoginController.ATTRIBUTE_REDIRECT_AFTER_LOGIN,
                        httpServletRequest.getRequestURI()+"?nosdl&"+httpServletRequest.getQueryString());

                httpServletResponse.sendRedirect("/login");

                return;
            } else {

                MailWrapper.sendErrorReport(httpServletRequest,new Exception("Empty Download Error (Stumpner indiv, logged in)"),"");
            }
        }

        String downloadFiles[] = new String[downloadImageList.size()];
        String zipNames[] = new String[downloadImageList.size()];

        HashMap doubleChecker = new HashMap();

        Logger logger = Logger.getLogger(DownloadServlet.class);

        if (this.canDownload(httpServletRequest,downloadImageList)) {

            Iterator downloadImages = downloadImageList.iterator();
            int i = 0;
            while (downloadImages.hasNext()) {
                String filenameInZip = "";
                MediaObject imageVersion = (MediaObject)downloadImages.next();

                //Unterscheidung zw. Original oder Special Resolution
                String imageFile = Config.imageStorePath+"/"+imageVersion.getIvid()+"_0";

                //Prüfen ob die Bilddatenbank mit Format-Selector (benutzerdefinierte Auflösungen) arbeitet:
                if (isSpecialDownloadResolution(httpServletRequest)) {

                    imageFile = handleSpecialDownloadResolution(imageVersion,httpServletRequest);
                }

                filenameInZip = imageVersion.getImageNumber(); // <- ist "standard"
                if (Config.downloadImageFilename.equalsIgnoreCase("imageNumber")) {
                    filenameInZip = imageVersion.getImageNumber();
                }
                if (Config.downloadImageFilename.equalsIgnoreCase("versionTitle")) {
                    filenameInZip = imageVersion.getVersionTitle();
                }
                if (Config.downloadImageFilename.equalsIgnoreCase("versionName")) {
                    filenameInZip = imageVersion.getVersionName();
                }
                // auf leeren Filenamen prÃ¼fen
                if (filenameInZip.length()==0) {
                    filenameInZip = "empty";
                }
                Integer count = new Integer(0);

                logger.debug("Add File: "+filenameInZip+" to Zip.");

                if (doubleChecker.containsKey(filenameInZip)) {
                    count = (Integer)doubleChecker.get(filenameInZip);
                    logger.debug("- Name exists in this File: "+count+" x");
                }
                count = new Integer(count.intValue()+1);
                if (count.intValue()>0) {
                    doubleChecker.remove(filenameInZip);
                }
                doubleChecker.put(filenameInZip,count);
                downloadFiles[i] = imageFile;
                zipNames[i] = (count.intValue()>1) ? filenameInZip+(count.intValue()-1) : filenameInZip;

                //File-Extention in den Dateinamen dazunehmen:
                if (imageVersion.getExtention().length()>0) {
                    if (!zipNames[i].endsWith(imageVersion.getExtention())) {
                        zipNames[i] = zipNames[i]+"."+imageVersion.getExtention();
                    }
                } else {
                    zipNames[i] = zipNames[i]+".JPG";
                }

                logger.debug("Done adding "+filenameInZip+" as ZIP-Name: "+zipNames[i]);
                i++;
            }

            if (Config.informDownloadAdmin) {
                this.informDownload(httpServletRequest,downloadImageList);
            }

            this.trackDownload(httpServletRequest,downloadImageList);

            System.out.println("Zip-Download");
            //Zip-Datei generieren
            String zipTmpFilename = Config.getTempPath()+"/"+httpServletRequest.getRemoteAddr()+System.currentTimeMillis()+".zip";
            System.out.println("Generate Tmp-File: "+zipTmpFilename);
            File zipTmpFile = new File(zipTmpFilename);
            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(zipTmpFile);

                Zip zip = new Zip();
                System.out.println("writing zip...");
                zip.main(downloadFiles,zipNames,fos);

                fos.flush();
                fos.close();
                System.out.println("closing zip file");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Datei zum Client schicken
            System.out.println("sending zip file to client: filename="+fileName);
                httpServletResponse.setHeader("Content-Description","File Transfer");
                httpServletResponse.setHeader("Cache-Control","no-store");     //HTTP 1.1
                httpServletResponse.setHeader("Pragma","no-cache"); //HTTP 1.0
                httpServletResponse.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                httpServletResponse.addHeader("Content-Length", Long.toString(zipTmpFile.length()));
                httpServletResponse.setContentType("application/zip");
                httpServletResponse.setHeader("Content-Disposition","attachment; filename=\"download.zip\"");
            /*
                //httpServletResponse.setHeader("Content-Description","File Transfer");
        httpServletResponse.setHeader("Content-Disposition:","attachment; filename=\""+fileName+".zip\"");
                httpServletResponse.setHeader("Cache-Control","no-store");     //HTTP 1.1
                httpServletResponse.setHeader("Pragma","no-cache"); //HTTP 1.0
                httpServletResponse.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                httpServletResponse.addHeader("Content-Length", Long.toString(zipTmpFile.length()));
                //httpServletResponse.setContentType(image.getMimeType());
                    httpServletResponse.setContentType("application/zip");
                //httpServletResponse.setHeader("Content-Disposition","attachment; filename=\""+downloadFilename+"\"");
              */

                OutputStream outputStream = httpServletResponse.getOutputStream();
                InputStream inputStream = new FileInputStream(zipTmpFile);

                byte b[] = new byte[512];
                int len=0;
                while((len=inputStream.read(b)) != -1) {
                    outputStream.write(b,0,len);
                }
                outputStream.close();
                inputStream.close();
            System.out.println("zip file closed");

            System.out.println("delete zip temp file (übersprungen)");
            //zipTmpFile.delete();


            final List downloadImageListFinal = downloadImageList;
            final long uniqueIdFinal = uniqueId;

            //Wenn eine Spezial-Auflösung zum Download angeboten wurde, müssen die Temp-Files gelöscht werden
            if (isSpecialDownloadResolution(httpServletRequest)) {

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        public void run() {
                            cleanUpTempFiles(downloadImageListFinal,uniqueIdFinal);
                        }
                    }, 60*1000);
            }

            this.cleanUpDownload(httpServletRequest,downloadImageList);

            if (Config.useShoppingCart) {
                //Wenn der ShoppingCart benutzt wurde das Objekt entfernen
                User user = WebHelper.getUser(httpServletRequest);
                if (user.getRole()>= User.ROLE_USER) {
                    ShoppingCartService scs = new ShoppingCartService();
                    scs.removeImagesToShoppingCart(downloadImageList, user.getUserId());
                }
            }
        }

        httpServletRequest.getSession().setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,new ArrayList());


    }

    private String handleSpecialDownloadResolution(MediaObject imageVersion, HttpServletRequest httpServletRequest) {

                long uniqueId = (Long)httpServletRequest.getAttribute("uniqueId");

                //Unterscheidung zw. Original oder Special Resolution
                String imageFile = Config.imageStorePath+"/"+imageVersion.getIvid()+"_0";
                String origImageFile = imageFile;

                    FormatSelector formatSelector = getFormatSelector(httpServletRequest);
                    if (!formatSelector.isOriginalFormat(imageVersion)) {
                        //Bildanfrage ist nicht gleich Orginalgröße, daher das neue Format berechenen und das
                        //Bild erzeugen
                        //Bild-Auflösung berechnen und imageFile-Name zurückgeben in imageFile
                        imageFile = Config.imageStorePath+"/"+imageVersion.getIvid()+"_res"+uniqueId;
                        ImageMagickUtil imageUtil = new ImageMagickUtil(false);
                        //System.out.println("Bild spezialauflösung berechnen: "+imageFile);

                        //orientation:
                        // 0 - square
                        // 1 - vertical
                        // 2 - horizontal
                        int orientation = 0;
                        if (imageVersion.getHeight()>imageVersion.getWidth()) {
                            //vertical
                            orientation = 1;
                        }
                        if (imageVersion.getWidth()>imageVersion.getHeight()) {
                            orientation = 2;
                        }

                        //images verkleinern vertical
                        if (orientation == 1 || orientation == 0) {
                            imageUtil.resizeImageVertical(origImageFile,imageFile,
                                    (int)getFormat(httpServletRequest,imageVersion).getHeight());
                        }
                        //images verkleinern horizontal
                        if (orientation == 2) {
                            imageUtil.resizeImageHorizontal(origImageFile,imageFile,
                                    (int)getFormat(httpServletRequest,imageVersion).getWidth());
                        }
                    }

        return imageFile;

    }

    private boolean isSpecialDownloadResolution(HttpServletRequest request) {

        if (getDownloadType(request)==DownloadServlet.DOWNLOAD_TYPE_PINPIC) {
            return false;
        }

        if (request.getSession().getAttribute("formatSelector")!=null) {
            return true;
        } else {
            return false;
        }
    }

    private FormatSelector getFormatSelector(HttpServletRequest request) {

        FormatSelector formatSelector = (FormatSelector)request.getSession().getAttribute("formatSelector");
        return formatSelector;

    }

    /**
     * @deprecated Bitte {@link DownloadServlet#getFormatSelector(javax.servlet.http.HttpServletRequest)}  und die Funktion {@link com.stumpner.mediadesk.web.mvc.commandclass.FormatSelector#getFormat(MediaObject)} benutzen
     * @param request
     * @param imageVersion
     * @return
     */
    private Rectangle getFormat(HttpServletRequest request, MediaObject imageVersion) {

        FormatSelector formatSelector = (FormatSelector)request.getSession().getAttribute("formatSelector");
        return formatSelector.getFormat(imageVersion);
    }

    private void informDownload(HttpServletRequest request,List imageList) {

        int i = imageList.size();
        String mailbody = "";

        String imageListString = "";
        DownloadLoggerService dlls = new DownloadLoggerService();
        MediaService ivs = new MediaService();
        LngResolver lngResolver = new LngResolver();
        ivs.setUsedLanguage(lngResolver.resolveLng(request));
        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject imageVersion = (MediaObject)images.next();
            //todo: "verschÃ¶nern"
            //Hier muss jedes Bild nochmal neu aus der Datenbank geladen werden:
            //MediaObject originalImage = ivs.getMediaObjectById(imageVersion.getIvid());
            imageListString = imageListString + imageVersion.getImageNumber();

            //System.out.println("ImageTitle: "+imageVersion.getVersionTitle());
            if (imageVersion.getVersionTitle().length()>0) {
                //System.out.println("ImageTitle: "+imageVersion.getVersionTitle());
                imageListString = imageListString +" - Titel: "+imageVersion.getVersionTitle();
            }

            if (isSpecialDownloadResolution(request)) {
                Rectangle rect = getFormat(request,imageVersion);
                imageListString = imageListString + " Format: "+((int)rect.getWidth())+"x"+((int)rect.getHeight());
            }
        }

                    UserService userService = new UserService();
                    User admin = null;
        MessageFormat mf = new MessageFormat("");


        switch (this.getDownloadType(request)) {
            case DownloadServlet.DOWNLOAD_SINGLE:
            case DownloadServlet.DOWNLOAD_TYPE_IMAGES:
                User user = WebHelper.getUser(request);
                mf = new MessageFormat(Config.mailDownloadInfoMailBody);
                String username = user.getUserName();
                if (Config.userEmailAsUsername) {
                    username = user.getEmail();
                }
                Object[] parameters = { Config.webTitle , username , i , Config.httpBase , imageListString , new Date() };
                mailbody = mf.format(parameters);
                MailWrapper.sendAsync(Config.mailserver,Config.mailsender,Config.mailReceiverAdminEmail,Config.mailDownloadInfoMailSubject,mailbody);
                break;
            case DownloadServlet.DOWNLOAD_TYPE_PINPIC:
                int pinId = ((Integer)request.getSession().getAttribute("pinid")).intValue();
                String pinPicString = String.valueOf(pinId);

                if (!pinPicString.equalsIgnoreCase(
                        (String)request.getSession().getAttribute("alreadyInformed")
                )) {
                    request.getSession().setAttribute("alreadyInformed",pinPicString);
                    mf = new MessageFormat("PIN {0} [{1}] wurde heruntergeladen.\n\n{2}\nDownload-Zeit: {3}");
                    PinpicService pinService = new PinpicService();
                    //todo: PinId
                    try {
                        Pin pin = (Pin)pinService.getById(pinId);
                        Object[] param = { pin.getPin() , pin.getPinpicName() , imageListString, new Date() };
                        mailbody = mf.format(param);

                        if (pin.getEmailnotification().length()>0) {
                            MailWrapper.sendAsync(Config.mailserver,Config.mailsender,pin.getEmailnotification(),Config.mailDownloadInfoMailSubject,mailbody);
                        }
                        MailWrapper.sendAsync(Config.mailserver,Config.mailsender,Config.mailReceiverAdminEmail,Config.mailDownloadInfoMailSubject,mailbody);

                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
        }

    }

    private List getImageList(HttpServletRequest request) {

        AclControllerContext aclContext = AclContextFactory.getAclContext(request);
        List downloadImageList = new ArrayList();

        switch (this.getDownloadType(request)) {

            case DownloadServlet.DOWNLOAD_SINGLE:
                List singleImageList = new LinkedList();
                int ivid = Integer.parseInt(request.getParameter("ivid"));
                List sselectedToDownloadList = (List)request.getSession().getAttribute(Resources.SESSIONVAR_DOWNLOAD_IMAGES);
                if (sselectedToDownloadList==null) { //Wenn nicht über den Download-View Controller gegangen wird, ist in der Session keine liste
                    MediaService mediaService = new MediaService();
                    MediaObject i = mediaService.getMediaObjectById(ivid);
                    sselectedToDownloadList = new LinkedList();
                    sselectedToDownloadList.add(i);
                }
                downloadImageList = getPermittedImages(request, aclContext, sselectedToDownloadList);
                //single image suchen:
                Iterator images = downloadImageList.iterator();
                while (images.hasNext()) {
                    MediaObject image = (MediaObject)images.next();
                    if (image.getIvid()==ivid) {
                        singleImageList.add(image);
                    }
                }
                return singleImageList;

            case DownloadServlet.DOWNLOAD_TYPE_IMAGES:

                List selectedToDownloadList = (List)request.getSession().getAttribute(Resources.SESSIONVAR_DOWNLOAD_IMAGES);
                if (selectedToDownloadList==null) { selectedToDownloadList = new LinkedList(); } //Wenn keine Liste in der Session, dann leere Liste zurückgeben
                downloadImageList = getPermittedImages(request, aclContext, selectedToDownloadList);
                break;

            case DownloadServlet.DOWNLOAD_TYPE_PINPIC:

                int pinId = ((Integer)request.getSession().getAttribute("pinid")).intValue();
                PinpicService pinpicService = new PinpicService();
                LngResolver lngResolver = new LngResolver();
                pinpicService.setUsedLanguage(lngResolver.resolveLng(request));
                //PrÃ¼fen ob alle Bilder des Pins oder nur ausgewÃ¤hlte angezeigt werden sollen:
                if (request.getParameter("ivid")!=null) {
                    //Einzelnes Bild soll heruntergeladen werden
                    List pinImageList = pinpicService.getPinpicImages(pinId);
                    Iterator pinImages = pinImageList.iterator();
                    while (pinImages.hasNext()) {
                        MediaObject imageVersion = (MediaObject)pinImages.next();
                        if (imageVersion.getIvid()==Integer.parseInt(request.getParameter("ivid"))) {
                            downloadImageList.add(imageVersion);
                        }
                    }

                } else if (request.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                    List pinImageList = pinpicService.getPinpicImages(pinId);
                    List selectedImageList = (List)request.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                    pinImageList.retainAll(selectedImageList);
                    downloadImageList = selectedImageList;
                    if (request.getParameter("pinpic").equalsIgnoreCase("all")) {
                        downloadImageList = pinpicService.getPinpicImages(pinId);
                    }
                } else {
                    if (request.getParameter("pinpic").equalsIgnoreCase("all")) {
                        //Alle Bilder im Pin herunterladen...
                        downloadImageList = pinpicService.getPinpicImages(pinId);
                    }
                }
                break;

        }

        return downloadImageList;
    }

    /**
     * Ob der angegebene request berechtigt ist, bilder downzuloaden
      * @param request
     * @param imageList
     * @return
     */
    private boolean canDownload(HttpServletRequest request, List imageList) {

        boolean canDownload = false;

        switch (this.getDownloadType(request)) {

            case DownloadServlet.DOWNLOAD_SINGLE:
            case DownloadServlet.DOWNLOAD_TYPE_IMAGES:

                User user = (User)request.getSession().getAttribute("user");
                //check if credits are enough
                canDownload = true;
                /*
                if (Config.creditSystemEnabled) {
                        if (imageList.size()>user.getCredits() && !(user.getCredits()==-1)) {
                            canDownload = false;
                        }
                } */
                //check Warenkorb/Shoppingcart
                if (Config.useShoppingCart) {
                    ShoppingCartService scs = new ShoppingCartService();
                    Iterator images = imageList.iterator();
                    while (images.hasNext()) {
                        MediaObjectMultiLang i = (MediaObjectMultiLang)images.next();
                        BigDecimal price = Config.currency.isEmpty() ? BigDecimal.valueOf(1) : i.getPrice();
                        if (price.compareTo(BigDecimal.valueOf(0))!=0) {
                            //Objekte die eine Preis haben prüfen ob sie bezahlt wurden (shoppingCart
                            if (user==null) {
                                //Wenn der Benutzer nicht eingeloggt ist, dann redirecten zum login
                            }
                            CartObject co = scs.getCartObject(user.getUserId(), i.getIvid());
                            if (co==null) {
                                canDownload = false; //nicht im warenkorb
                            } else {
                                if (co.getPayTransactionId().length()==0) {
                                    canDownload = false;
                                }
                            }
                        }
                    }
                }
                break;

           case DownloadServlet.DOWNLOAD_TYPE_PINPIC:

                int pinId = ((Integer)request.getSession().getAttribute("pinid")).intValue();
                PinpicService pinpicService = new PinpicService();
                Pin pinPic = new Pin();
                try {
                    pinPic = (Pin)pinpicService.getById(pinId);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (IOServiceException e) {
                    e.printStackTrace();
                }

                if (pinPic.isEnabled()) {
                    if (pinPic.getUsed()<=pinPic.getMaxUse()) {

                        //Umrechnung weil im Enddate immer das Datum+Zeitpunkt Mitternacht (also 14.Dezember 00:00 Uhr steht)
                        //aber der PIN bis 14.Dezember 23:59 Uhr gültig ist
                        Calendar endDateMidnight = GregorianCalendar.getInstance();
                        endDateMidnight.setTime(pinPic.getEndDate());
                        endDateMidnight.set(Calendar.HOUR_OF_DAY,23);
                        endDateMidnight.set(Calendar.MINUTE,59);

                        if (pinPic.getStartDate().before(new Date()) && (endDateMidnight.getTime().after(new Date())) ) {
                            canDownload=true;                           
                        }
                    }
                }

                break;
        }

        return canDownload;
    }

    /**
     * BasicMediaObject Download mitloggen bzw in die statistik aufnehmen
     * @param request
     * @param imageList
     */
    private void trackDownload(HttpServletRequest request, List imageList) {


        switch (this.getDownloadType(request)) {

            case DownloadServlet.DOWNLOAD_SINGLE:

                User suser = WebHelper.getUser(request);
                DownloadLoggerService sdlls = new DownloadLoggerService();
                Iterator simages = imageList.iterator();
                BigDecimal preis = BigDecimal.valueOf(0);
                while (simages.hasNext()) {
                    MediaObjectMultiLang imageVersion = (MediaObjectMultiLang)simages.next();
                    preis = Config.currency.isEmpty() ? BigDecimal.valueOf(1) : imageVersion.getPrice();
                    Rectangle rect = new Rectangle(imageVersion.getWidth(),imageVersion.getHeight());
                    if (isSpecialDownloadResolution(request)) {
                        rect = getFormat(request,imageVersion);
                    }

                    String payTransactionId = "";
                    if (Config.useShoppingCart) {
                        ShoppingCartService scs = new ShoppingCartService();
                        CartObject ivco = scs.getCartObject(suser.getUserId(), imageVersion.getIvid());

                        if (ivco!=null) {
                            payTransactionId = ivco.getPayTransactionId();
                        }
                    }

                    sdlls.log(suser.getUserId(),imageVersion.getIvid(), rect,
                            SimpleDownloadLogger.DTYPE_DOWNLOAD, request,0,0,payTransactionId);
                }

                if (Config.creditSystemEnabled) {
                    if (suser.getCredits().compareTo(BigDecimal.valueOf(0))>0) {
                        suser.setCredits(suser.getCredits().subtract(preis));
                        if (suser.getCredits().compareTo(BigDecimal.valueOf(0))<0) {
                            suser.setCredits(BigDecimal.valueOf(0));
                        }
                        UserService userService = new UserService();
                        try {
                            userService.save(suser);
                        } catch (IOServiceException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }

                break;

            case DownloadServlet.DOWNLOAD_TYPE_IMAGES:

                User user = WebHelper.getUser(request);
                int i = imageList.size();

                DownloadLoggerService dlls = new DownloadLoggerService();
                Iterator images = imageList.iterator();
                BigDecimal preis2 = BigDecimal.valueOf(0);

                while (images.hasNext()) {
                    MediaObjectMultiLang imageVersion = (MediaObjectMultiLang)images.next();
                    preis2 = preis2.add(Config.currency.isEmpty() ? BigDecimal.valueOf(1) : imageVersion.getPrice());
                    Rectangle rect = new Rectangle(imageVersion.getWidth(),imageVersion.getHeight());
                    if (isSpecialDownloadResolution(request)) {
                        rect = getFormat(request,imageVersion);
                    }

                    /**
                    dlls.log(user.getUserId(),imageVersion.getIvid(), rect,
                            SimpleDownloadLogger.DTYPE_DOWNLOAD, request,0);**/
                    ShoppingCartService scs = new ShoppingCartService();
                    CartObject ivco = scs.getCartObject(user.getUserId(), imageVersion.getIvid());
                    String payTransactionId = "";
                    if (ivco!=null) {
                        payTransactionId = ivco.getPayTransactionId();
                    }
                    dlls.log(user.getUserId(),imageVersion.getIvid(), rect,
                            SimpleDownloadLogger.DTYPE_DOWNLOAD, request,0,0,payTransactionId);
                }

                if (Config.creditSystemEnabled) {
                    if (user.getCredits().compareTo(BigDecimal.valueOf(0))>0) {
                        user.setCredits(user.getCredits().subtract(preis2));
                        if (user.getCredits().compareTo(BigDecimal.valueOf(0))<0) {
                            user.setCredits(BigDecimal.valueOf(0));
                        }
                        UserService userService = new UserService();
                        try {
                            userService.save(user);
                        } catch (IOServiceException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
                break;

            case DownloadServlet.DOWNLOAD_TYPE_PINPIC:

                int pinId = ((Integer)request.getSession().getAttribute("pinid")).intValue();
                try {
                    PinpicService pinpicService = new PinpicService();
                    Pin pinPic = new Pin();
                    pinPic = (Pin)pinpicService.getById(pinId);
                    String pinPicString = String.valueOf(pinPic.getPinpicId());
                    if (!pinPicString.equalsIgnoreCase(
                            (String)request.getSession().getAttribute("alreadyTracked")
                    )) {
                        request.getSession().setAttribute("alreadyTracked",pinPicString);
                        pinPic.setUsed(pinPic.getUsed()+1);
                        pinpicService.save(pinPic);
                    }

                    DownloadLoggerService dlls2 = new DownloadLoggerService();
                    Iterator images2 = imageList.iterator();
                    while (images2.hasNext()) {
                        MediaObject mediaObject = (MediaObject)images2.next();
                        dlls2.log(0,mediaObject.getIvid(), null,
                            SimpleDownloadLogger.DTYPE_PIN, request,0,pinPic.getPinpicId());
                    }

                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (IOServiceException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    private void cleanUpDownload(HttpServletRequest request, List downloadImageList) {

        long uniqueId = (Long)request.getAttribute("uniqueId");

        switch (this.getDownloadType(request)) {

            case DownloadServlet.DOWNLOAD_TYPE_IMAGES:

                User user = (User)request.getSession().getAttribute("user");
                ShoppingCartService shoppingCartService = new ShoppingCartService();
                shoppingCartService.removeImagesToShoppingCart(downloadImageList,user.getUserId());
                request.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);

                break;

            case DownloadServlet.DOWNLOAD_TYPE_PINPIC:
                break;

        }

        request.getSession().removeAttribute("formatSelector");
    }

    private void cleanUpTempFiles(List downloadImageList, long uniqueId) {

                //Verkleinerte Files wieder entfernen:
        //todo: muss umgebaut werden dass beim erstellen des Res-Files bereits eine liste erstellt wird mit dateien die gelöscht werden müssen
                Iterator images = downloadImageList.iterator();
                while (images.hasNext()) {
                    MediaObject imageVersion = (MediaObject)images.next();
                    String imageFile = Config.imageStorePath+File.separator+imageVersion.getIvid()+"_res"+uniqueId;
                    //System.out.println("Temporäre Datei löschen: "+imageFile);
                    File file = new File(imageFile);
                    if (file.delete()) {
                        //System.out.println("ok");
                    } else {
                        System.out.println("[DownloadServlet] Datei konnte nicht gelöscht werden: "+imageFile);
                    }
                }
                //System.out.println("cleanup fertig");

    }

    /**
     * Mit dieser funktion kann herausgefunden werden ob es sich um eienn image-download oder
     * einen pinpic-download handelt
     * @param request
     * @return
     */
    private int getDownloadType(HttpServletRequest request) {

        if (request.getParameter("sdl")!=null) {
            //Single Download
            return DownloadServlet.DOWNLOAD_SINGLE;
        }
        if (request.getParameter("pin")!=null) {
            return DownloadServlet.DOWNLOAD_TYPE_PINPIC;
        } else {
            return DownloadServlet.DOWNLOAD_TYPE_IMAGES;
        }
    }

    public static List getPermittedImages(HttpServletRequest request, AclControllerContext aclContext, List selectedToDownloadList) {

        List permittedImages = AclUtil.getPermittedImages(aclContext, selectedToDownloadList);
        //PrÃ¼fen ob PIN
        if (request.getSession().getAttribute("pinid")!=null) {
            int pinId = ((Integer)request.getSession().getAttribute("pinid"));
            PinpicService pinService = new PinpicService();
            List pinpicImageList = pinService.getPinpicImages(pinId);
            Iterator pinpicImages = pinpicImageList.iterator();
            while (pinpicImages.hasNext()) {
                //prÃ¼fen ob PIN Medien-Objekt in selected to download
                MediaObject mediaObject = (MediaObject)pinpicImages.next();
                if (isMediaObjectInList(mediaObject, selectedToDownloadList)) {
                    //prÃ¼fen ob bereits in permittetImages
                    if (isMediaObjectInList(mediaObject, permittedImages)) {
                        //bereits erlaubt, nichts unternehmen
                    } else {
                        //noch nicht in permitted Images -> dann erlauben/hinzufÃ¼gen
                        permittedImages.add(getMediaObjectInList(mediaObject, selectedToDownloadList));
                    }
                }
            }
        }
        return permittedImages;

    }

    public static boolean isMediaObjectInList(MediaObject mediaObject, List mediaList) {
        return getMediaObjectInList(mediaObject,mediaList) == null ? false: true;
    }

    public static MediaObject getMediaObjectInList(MediaObject mediaObject, List mediaList) {

        Iterator mediaObjects = mediaList.iterator();
        while (mediaObjects.hasNext()) {
            MediaObject mediaObjektA = (MediaObject)mediaObjects.next();
            //System.out.println("mediaObjektA: "+mediaObjektA.getClass().getName());
            //System.out.println("mediaObject: "+mediaObject.getClass().getName());
            if (mediaObjektA.getIvid() == mediaObject.getIvid()) { return mediaObjektA; }
        }

        return null;
    }
}