package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.validation.BindException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stumpner.mediadesk.image.*;
import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.image.inbox.InboxService;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.list.CustomListService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.ApplicationSettings;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.LngResolver;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

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
 * User: franzstumpner
 * Date: 08.05.2005
 * Time: 16:59:10
 * To change this template use File | Settings | File Templates.
 */
public class ImageEditController extends AbstractAutoFillController {

    public ImageEditController() {
        this.setCommandClass(ImageVersionMetadata.class);
        this.setValidator(new ImageEditValidator());
        this.setValidateOnBinding(true);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_EDITOR;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        ImageVersionMetadata imageVersionMetadata = (ImageVersionMetadata)e.getTarget();

        User user = this.getUser(httpServletRequest);
        if (user==null) { httpServletResponse.sendError(401,"nicht angemeldet: Bild bearbeiten nicht moeglich"); return null; }
        if (UserService.getSecurityGroupVisitors().getId()==user.getSecurityGroup()) { httpServletResponse.sendRedirect("/login"); return null; }
        if (user.getRole()<User.ROLE_EDITOR) { httpServletResponse.sendError(403,"Berechtigungen fehlen um ein Bild zu bearbeiten"); return null; }
        if (httpServletRequest.getAttribute("noid")!=null) {
            httpServletResponse.sendError(404,"Keine ID angegeben"); return null;
            //zum login redirecten
            //httpServletResponse.sendRedirect("/login");
            //return null;
        }

        /**
         * Bilder in Ordner und Kategorien...
         */
        LngResolver lngResolver = new LngResolver();
        FolderService folderService = new FolderService();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        int ivid = ((ImageVersionMetadata)e.getTarget()).getImageVersion().getIvid();
        model.put("categoryList", folderService.getFolderListFromImageVersion(ivid));

        /**
         * Markierte Bilder testen bzw. zählen...
         */
        int selectedImageCount=0;
        if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
            List imageList = (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
            Iterator images = imageList.iterator();
            while (images.hasNext()) {
                ImageVersion image = (ImageVersion)images.next();
                selectedImageCount++;
            }
        }
        model.put("selectedList",new Integer(selectedImageCount));

        /**
         * Bilder in der Inbox
         */
        boolean inInbox=false;
        int inboxImage = 0;
        InboxService inboxService = new InboxService();
        Iterator inboxs = inboxService.getInbox().iterator();
        while (inboxs.hasNext()) {
            ImageVersion image = (ImageVersion)inboxs.next();
            if (image.getIvid()==ivid) { inInbox=true; }
            inboxImage++;
        }
        model.put("inboxImage",new Integer((inInbox) ? inboxImage:0));

        /**
         * Custom-Lists
         */
        CustomListService customListService = new CustomListService();
        customListService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        httpServletRequest.setAttribute("customLists",customListService.getCustomLists());
        httpServletRequest.setAttribute("customList", new List[] {
                customListService.getCustomListEntries(1),
                customListService.getCustomListEntries(2),
                customListService.getCustomListEntries(3)
        });

        httpServletRequest.setAttribute("langAutoFill",new Boolean(Config.langAutoFill));
        //httpServletRequest.setAttribute("config", new Config());

        if (imageVersionMetadata.getImageVersion().getMayorMime().equalsIgnoreCase("video") ||
            imageVersionMetadata.getImageVersion().getMayorMime().equalsIgnoreCase("audio")) {
            String videoStreamUrl = "/stream/object/"+imageVersionMetadata.getImageVersion().getIvid()+"."+imageVersionMetadata.getImageVersion().getExtention();
            httpServletRequest.setAttribute("streamUrl",videoStreamUrl);
            if (imageVersionMetadata.getImageVersion().getMayorMime().equalsIgnoreCase("video")) {
                httpServletRequest.setAttribute("hasVideo",true);
            }
            if (imageVersionMetadata.getImageVersion().getMayorMime().equalsIgnoreCase("audio")) {
                httpServletRequest.setAttribute("hasAudio",true);
            }
        }

        httpServletRequest.setAttribute("config.currency", Config.currency);

        ImageMetadataService ims = new ImageMetadataService();
        List metadataList = ims.getMetadata(ivid);
        //return null;  //To change body of implemented methods use File | Settings | File Templates.
        //Map model = new HashMap();
        httpServletRequest.setAttribute("metadataList",metadataList);

        this.setContentTemplateFile("imageedit.jsp",httpServletRequest);
        return super.showForm(httpServletRequest,e, this.getFormView(), model);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        super.initBinder(httpServletRequest,servletRequestDataBinder);
        servletRequestDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"),true));
        servletRequestDataBinder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, new DecimalFormat("0.00"),false) {

            public void setAsText(String s) throws IllegalArgumentException {
                super.setAsText(s);    //To change body of overridden methods use File | Settings | File Templates.
            }

            public void setValue(Object o) {
                super.setValue(o);    //To change body of overridden methods use File | Settings | File Templates.
            }

            public String getAsText() {
                return super.getAsText();    //To change body of overridden methods use File | Settings | File Templates.
            }
        });
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        //WebStack webStack = new WebStack(httpServletRequest);
        //webStack.push();

        if (httpServletRequest.getParameter("redirect")!=null) {
            //Nach der Eingabe auf eine Seite redirecten
            httpServletRequest.getSession().setAttribute("redirectTo",httpServletRequest.getParameter("redirect"));
            //System.out.println("Redirect: "+httpServletRequest.getParameter("redirect"));
        }

        try {
            int ivid = -1;
            if (httpServletRequest.getParameter("id").equalsIgnoreCase("n")) {
                //neue datei erstellen
                int categoryId = Integer.parseInt(httpServletRequest.getParameter("c"));
                ivid = createNewFile(WebHelper.getUser(httpServletRequest));
                FolderService folderService = new FolderService();
                folderService.addMediaToFolder(categoryId,ivid);
            } else {
                ivid = Integer.parseInt(httpServletRequest.getParameter("id"));
            }
            ImageMetadataService imageMetadataService = new ImageMetadataService();

        ImageVersionMetadata ivmd = imageMetadataService.getImageVersionMetadata(ivid);
        ApplicationSettings settings = ivmd.getApplicationSettings();

        settings.setEditCopyTitle(Config.editCopyTitle);
        settings.setEditCopyTitleLng1(Config.editCopyTitleLng1);
        settings.setEditCopyTitleLng2(Config.editCopyTitleLng2);

        settings.setEditCopySubTitle(Config.editCopySubTitle);
        settings.setEditCopySubTitleLng1(Config.editCopySubTitleLng1);
        settings.setEditCopySubTitleLng2(Config.editCopySubTitleLng2);

        settings.setEditCopyInfo(Config.editCopyInfo);
        settings.setEditCopyInfoLng1(Config.editCopyInfoLng1);
        settings.setEditCopyInfoLng2(Config.editCopyInfoLng2);

        settings.setEditCopySite(Config.editCopySite);
        settings.setEditCopyPhotographDate(Config.editCopyPhotographDate);
        settings.setEditCopyPhotographer(Config.editCopyPhotographer);
        settings.setEditCopyByline(Config.editCopyByline);
        settings.setEditCopyKeywords(Config.editCopyKeywords);

        settings.setEditCopyPeople(Config.editCopyPeople);
        settings.setEditCopyOrientation(Config.editCopyOrientation);
        settings.setEditCopyPerspective(Config.editCopyPerspective);
        settings.setEditCopyMotive(Config.editCopyMotive);
        settings.setEditCopyGesture(Config.editCopyGesture);
        settings.setEditCopyNote(Config.editCopyNote);
        settings.setEditCopyRestrictions(Config.editCopyRestrictions);
        settings.setEditCopyFlag(Config.editCopyFlag);

        settings.setEditCopySiteLng1(Config.editCopySiteLng1);
        settings.setEditCopySiteLng2(Config.editCopySiteLng2);
        settings.setEditCopyNoteLng1(Config.editCopyNoteLng1);
        settings.setEditCopyNoteLng2(Config.editCopyNoteLng2);
        settings.setEditCopyRestrictionsLng1(Config.editCopyRestrictionsLng1);
        settings.setEditCopyRestrictionsLng2(Config.editCopyRestrictionsLng2);

        settings.setEditCopyPrice(Config.editCopyPrice);
        settings.setEditCopyLicValid(Config.editCopyLicValid);

        return ivmd;

        } catch (NumberFormatException e) {
            //keine id angegeben
            httpServletRequest.setAttribute("noid",true);
            return new ImageVersionMetadata();
        }
    }

    //Erstellt eine neue Textdate
    private int createNewFile(User user) throws IOException {

        String tmpFilename = Config.getTempPath()+ File.separator+System.currentTimeMillis()+".txt";
        File newFile = new File(tmpFilename);
        newFile.createNewFile();

        int ivid = -1;

        try {
            ImportFactory importFactory = Config.importFactory;
            MediaImportHandler importHandler = null;
            importHandler = importFactory.createMediaImportHandler(newFile);
            ivid = importHandler.processImport(newFile,user.getUserId());

        } catch (MimeTypeNotSupportedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SizeExceedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileRejectException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return ivid;
    }

    void doNameAsTitle(Object o) {
        ImageVersionMetadata imageVersionMetadata = (ImageVersionMetadata)o;
        ImageVersionMultiLang imageVersion = (ImageVersionMultiLang)imageVersionMetadata.getImageVersion();
        imageVersion.setVersionTitleLng1(
                doAutoFillField(imageVersion.getVersionTitleLng1(),
                        imageVersion.getVersionName(),"")
        );
        imageVersion.setVersionTitleLng2(
                doAutoFillField(imageVersion.getVersionTitleLng2(),
                        imageVersion.getVersionName(),"")
        );
    }

    protected void doAutoFill(Object o) {

        ImageVersionMetadata ivmd = (ImageVersionMetadata)o;
        ImageVersionMultiLang ivml = (ImageVersionMultiLang)ivmd.getImageVersion();

        ivml.setVersionTitleLng1(
                doAutoFillField(
                ivml.getVersionTitleLng1(),
                ivml.getVersionTitleLng2(),
                ivml.getVersionName())
        );
        ivml.setVersionTitleLng2(
                doAutoFillField(
                ivml.getVersionTitleLng2(),
                ivml.getVersionTitleLng1(),
                ivml.getVersionName())
        );
        ivml.setVersionSubTitleLng1(
                doAutoFillField(
                ivml.getVersionSubTitleLng1(),
                ivml.getVersionSubTitleLng2(),"")
        );
        ivml.setVersionSubTitleLng2(
                doAutoFillField(
                ivml.getVersionSubTitleLng2(),
                ivml.getVersionSubTitleLng1(),"")
        );
        ivml.setInfoLng1(
                doAutoFillField(
                ivml.getInfoLng1(),
                ivml.getInfoLng2(),"")
        );
        ivml.setInfoLng2(
                doAutoFillField(
                ivml.getInfoLng2(),
                ivml.getInfoLng1(),"")
        );
        ivml.setSiteLng1(
                doAutoFillField(
                        ivml.getSiteLng1(),
                        ivml.getSiteLng2(),""
                )
        );
        ivml.setSiteLng2(
                doAutoFillField(
                        ivml.getSiteLng2(),
                        ivml.getSiteLng1(),""
                )
        );
        ivml.setNoteLng1(
                doAutoFillField(
                        ivml.getNoteLng1(),
                        ivml.getNoteLng2(),""
                )
        );
        ivml.setNoteLng2(
                doAutoFillField(
                        ivml.getNoteLng2(),
                        ivml.getNoteLng1(),""
                )
        );
        ivml.setRestrictionsLng1(
                doAutoFillField(
                        ivml.getRestrictionsLng1(),
                        ivml.getRestrictionsLng2(),""
                )
        );
        ivml.setRestrictionsLng2(
                doAutoFillField(
                        ivml.getRestrictionsLng2(),
                        ivml.getRestrictionsLng1(),""
                )
        );


    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (!isUserPermitted(httpServletRequest)) {
            httpServletResponse.sendError(403,"Nicht erlaubt oder nicht eingeloggt");
            return null;
        }

        ImageVersionMetadata imageVersionMetadata = (ImageVersionMetadata) o;
        ImageVersionMultiLang imageVersion = (ImageVersionMultiLang)imageVersionMetadata.getImageVersion();

        if (httpServletRequest.getParameter("replaceFolder")!=null) {
            //folderimages ersetzen
            String[] folderIdStrings = httpServletRequest.getParameterValues("replaceFolder");
            for (int p=0;p<folderIdStrings.length;p++) {
                logger.debug("ImageEditController: copy imagedata to folder: "+folderIdStrings[p]);
                this.copyImageDataOfFolder(imageVersionMetadata,Integer.parseInt(folderIdStrings[p]));
            }
        }

        if (httpServletRequest.getParameter("replaceCategory")!=null) {
            //folderimages ersetzen
            String[] categoryIdStrings = httpServletRequest.getParameterValues("replaceCategory");
            for (int p=0;p<categoryIdStrings.length;p++) {
                logger.debug("ImageEditController: copy imagedata to cat: "+categoryIdStrings[p]);
                this.copyImageDataOfCategory(imageVersionMetadata,Integer.parseInt(categoryIdStrings[p]));
            }
        }

        if (httpServletRequest.getParameter("replaceSelected")!=null) {
            //selectedimages ersetzen
            List imageList = (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
            logger.debug("ImageEditController: Copy selected images");
            this.copyImageDataOfImages(imageVersionMetadata,imageList);
        }

        if (httpServletRequest.getParameter("replaceInbox")!=null) {
            //selectedimages ersetzen
            InboxService inboxService = new InboxService();
            List inboxList = inboxService.getInbox();
            this.copyImageDataOfImages(imageVersionMetadata,inboxList);
        }

        this.saveImage((ImageVersionMultiLang)imageVersionMetadata.getImageVersion());

        //Bei Text-Files Content Laden
        if (imageVersion.getMayorMime().equalsIgnoreCase("text")) {
            saveFileContent(imageVersionMetadata);
        }

        //WebStack webStack = new WebStack(httpServletRequest);
        String redirectUrl = (String)httpServletRequest.getSession().getAttribute("redirectTo");
        httpServletRequest.getSession().removeAttribute("redirectTo");
        String url = redirectUrl.replaceAll("mark","nofunc").replaceAll("unmark","nofunc");
        httpServletResponse.sendRedirect(url);
        return null;
    }

    private void saveFileContent(ImageVersionMetadata imageVersionMetadata) {

        //Bei Text-Files Content Laden
        ImageVersion imageVersion = imageVersionMetadata.getImageVersion();
        if (imageVersion.getMayorMime().equalsIgnoreCase("text")) {

            String filename = Config.imageStorePath+ File.separator+imageVersion.getIvid()+"_0";
            try {
                FileUtils.writeStringToFile(new File(filename), imageVersionMetadata.getContent());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void copyImageDataOfImages(ImageVersionMetadata imageVersionMetadata, List imageList) {

        ImageVersion imageVersion = imageVersionMetadata.getImageVersion();
        if (imageList==null) { logger.warn("copyImageDataOfImages: imageList == null"); }
        Iterator images = imageList.iterator();

        while(images.hasNext()) {
            ImageVersion imageTo = (ImageVersion)images.next();
            logger.debug("copy image-data to "+imageTo.getIvid());
            this.copyIvid((ImageVersionMultiLang)imageVersion,(ImageVersionMultiLang)imageTo,
                    imageVersionMetadata.getCopyfield());
            this.saveImage((ImageVersionMultiLang)imageTo);
        }
    }

    private void copyImageDataOfFolder(ImageVersionMetadata imageVersionMetadata, int folderId) {

        ImageVersionService imageService = new ImageVersionService();
        List imageList = imageService.getFolderImages(new SimpleLoaderClass(folderId));
        //Daten kopieren:
        copyImageDataOfImages(imageVersionMetadata,imageList);
    }

    private void copyImageDataOfCategory(ImageVersionMetadata imageVersionMetadata, int categoryId) {

        ImageVersionService imageService = new ImageVersionService();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(categoryId);
        List imageList = imageService.getCategoryImages(loaderClass);
        //Daten kopieren:
        copyImageDataOfImages(imageVersionMetadata,imageList);
    }

    /**
     * ivid wird immer ersetzt!
     * @param metadataFrom
     * @param metadataTo
     * @deprecated hat keine funktion mehr, da die Language-Daten nichtmehr in den Meta-Daten gespeichert werden
     */
    private void copyImageMetadata(List metadataFrom, List metadataTo, int ivid, String[] fields) {

    }

    private void saveImage(ImageVersionMultiLang image) {

        ImageVersionService imageService = new ImageVersionService();
        logger.debug("save image [Date]"+image.getPhotographDate());
        try {
            int language = image.getUsedLanguage();
            image.setUsedLanguage(0);
            imageService.saveImageVersion(image);
            image.setUsedLanguage(language);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Methode um die Daten eines Bildes auf ein anderes Bild zu kopieren
     * @param ividFrom Das zu kopierende Bild
     * @param ividTo Das bild auf den die Daten kopiert werden sollen
     * @param fields Array mit Feldnamen die kopiert werden sollen.
     */
    private void copyIvid(ImageVersionMultiLang ividFrom, ImageVersionMultiLang ividTo, String[] fields) {

        if (fields!=null) {

            int oldFromLanguage = ividFrom.getUsedLanguage();
            int oldToLanguage = ividTo.getUsedLanguage();
            ividFrom.setUsedLanguage(0);
            ividTo.setUsedLanguage(0);

            for (int p=0;p<fields.length;p++) {

                if (fields[p].equalsIgnoreCase("versionTitle")) {
                    ividTo.setImageTitle(ividFrom.getImageTitle()); }
                if (fields[p].equalsIgnoreCase("versionSubTitle")) {
                    ividTo.setImageSubTitle(ividFrom.getImageSubTitle()); }
                if (fields[p].equalsIgnoreCase("versionTitle")) {
                    ividTo.setVersionTitle(ividFrom.getVersionTitle()); }
                if (fields[p].equalsIgnoreCase("versionTitleLng1")) {
                    ividTo.setVersionTitleLng1(ividFrom.getVersionTitleLng1()); }
                if (fields[p].equalsIgnoreCase("versionTitleLng2")) {
                    ividTo.setVersionTitleLng2(ividFrom.getVersionTitleLng2()); }
                if (fields[p].equalsIgnoreCase("versionSubTitle")) {
                    ividTo.setVersionSubTitle(ividFrom.getVersionSubTitle()); }
                if (fields[p].equalsIgnoreCase("versionSubTitleLng1")) {
                    ividTo.setVersionSubTitleLng1(ividFrom.getVersionSubTitleLng1()); }
                if (fields[p].equalsIgnoreCase("versionSubTitleLng2")) {
                    ividTo.setVersionSubTitleLng2(ividFrom.getVersionSubTitleLng2()); }
                if (fields[p].equalsIgnoreCase("info")) {
                    ividTo.setInfo(ividFrom.getInfo()); }
                if (fields[p].equalsIgnoreCase("infoLng1")) {
                    ividTo.setInfoLng1(ividFrom.getInfoLng1()); }
                if (fields[p].equalsIgnoreCase("infoLng2")) {
                    ividTo.setInfoLng2(ividFrom.getInfoLng2()); }
                if (fields[p].equalsIgnoreCase("site")) {
                    ividTo.setSite(ividFrom.getSite()); }
                if (fields[p].equalsIgnoreCase("siteLng1")) {
                    ividTo.setSiteLng1(ividFrom.getSiteLng1()); }
                if (fields[p].equalsIgnoreCase("siteLng2")) {
                    ividTo.setSiteLng2(ividFrom.getSiteLng2()); }
                if (fields[p].equalsIgnoreCase("photographDate")) {
                    ividTo.setPhotographDate(ividFrom.getPhotographDate()); }
                if (fields[p].equalsIgnoreCase("photographerAlias")) {
                    ividTo.setPhotographerAlias(ividFrom.getPhotographerAlias()); }
                if (fields[p].equalsIgnoreCase("byline")) {
                    ividTo.setByline(ividFrom.getByline()); }
                if (fields[p].equalsIgnoreCase("keywords")) {
                    ividTo.setKeywords(ividFrom.getKeywords()); }
                if (fields[p].equalsIgnoreCase("people")) {
                    ividTo.setPeople(ividFrom.getPeople()); }
                if (fields[p].equalsIgnoreCase("note")) {
                    ividTo.setNote(ividFrom.getNote()); }
                if (fields[p].equalsIgnoreCase("noteLng1")) {
                    ividTo.setNoteLng1(ividFrom.getNoteLng1()); }
                if (fields[p].equalsIgnoreCase("noteLng2")) {
                    ividTo.setNoteLng2(ividFrom.getNoteLng2()); }
                if (fields[p].equalsIgnoreCase("restrictions")) {
                    ividTo.setRestrictions(ividFrom.getRestrictions()); }
                if (fields[p].equalsIgnoreCase("restrictionsLng1")) {
                    ividTo.setRestrictionsLng1(ividFrom.getRestrictionsLng1()); }
                if (fields[p].equalsIgnoreCase("restrictionsLng2")) {
                    ividTo.setRestrictionsLng2(ividFrom.getRestrictionsLng2()); }

                if (fields[p].equalsIgnoreCase("orientation")) {
                    ividTo.setOrientation(ividFrom.getOrientation());
                }
                if (fields[p].equalsIgnoreCase("perspective")) {
                    ividTo.setPerspective(ividFrom.getPerspective());
                }
                if (fields[p].equalsIgnoreCase("motive")) {
                    ividTo.setMotive(ividFrom.getMotive());
                }
                if (fields[p].equalsIgnoreCase("gesture")) {
                    ividTo.setGesture(ividFrom.getGesture());
                }
                if (fields[p].equalsIgnoreCase("flag")) {
                    ividTo.setFlag(ividFrom.getFlag());
                }
                if (fields[p].equalsIgnoreCase("artist")) {
                    ividTo.setArtist(ividFrom.getArtist());
                }
                if (fields[p].equalsIgnoreCase("album")) {
                    ividTo.setAlbum(ividFrom.getAlbum());
                }
                if (fields[p].equalsIgnoreCase("genre")) {
                    ividTo.setGenre(ividFrom.getGenre());
                }
                if (fields[p].equalsIgnoreCase("price")) {
                    ividTo.setPrice(ividFrom.getPrice());
                }
                if (fields[p].equalsIgnoreCase("licValid")) {
                    ividTo.setLicValid(ividFrom.getLicValid());
                }
                if (fields[p].equalsIgnoreCase("masterdataId")) {
                    ividTo.setMasterdataId(ividFrom.getMasterdataId());
                }
            }
            ividFrom.setUsedLanguage(oldFromLanguage);
            ividTo.setUsedLanguage(oldToLanguage);

        }
    }

    /**
     * Methode um Ivid-Objekte anhand von properties zu kopieren
     * in properties müssen die Felder aufgelistet werden, die kopiert werden sollen
     * @param ividFrom
     * @param ividTo
     * @param fields
     * @deprecated copyIvid mit ImageVersionMultiLang
     */
    private void copyIvid(ImageVersion ividFrom, ImageVersion ividTo, String[] fields) {

        //todo: methode entfernen...
        if (ividFrom instanceof ImageVersionMultiLang && ividTo instanceof ImageVersionMultiLang) {
            copyIvid((ImageVersionMultiLang)ividFrom,(ImageVersionMultiLang)ividTo,fields);
        } else {
            logger.warn("copyIvid is deprecated: potentially not all fields are copied!");
            for (int p=0;p<fields.length;p++) {

                //System.out.println("Copy-Field: "+fields[p]);

                if (fields[p].equalsIgnoreCase("versionTitle")) {
                //    System.out.println("-> ImageTitle");
                    ividTo.setImageTitle(       ividFrom.getImageTitle());
                }
                if (fields[p].equalsIgnoreCase("versionSubTitle")) {
                //    System.out.println("-> ImageSubTitle");
                    ividTo.setImageSubTitle(    ividFrom.getImageSubTitle());
                }
                if (fields[p].equalsIgnoreCase("versionTitle")) {
                //    System.out.println("-> VersionTitle");
                    ividTo.setVersionTitle(     ividFrom.getVersionTitle());
                }
                if (fields[p].equalsIgnoreCase("versionSubTitle")) {
                //    System.out.println("-> VersionSubTitle");
                    ividTo.setVersionSubTitle(  ividFrom.getVersionSubTitle());
                }
                if (fields[p].equalsIgnoreCase("info")) {
                    ividTo.setInfo(             ividFrom.getInfo());
                }
                if (fields[p].equalsIgnoreCase("site")) {
                    ividTo.setSite(             ividFrom.getSite());
                }
                if (fields[p].equalsIgnoreCase("photographDate")) {
                    ividTo.setPhotographDate(   ividFrom.getPhotographDate());
                }

                if (fields[p].equalsIgnoreCase("photographerAlias")) {
                //    System.out.println("-> PhotographerAlias");
                    ividTo.setPhotographerAlias(ividFrom.getPhotographerAlias());
                }

                if (fields[p].equalsIgnoreCase("byline")) {
                    ividTo.setByline(           ividFrom.getByline());
                }
                if (fields[p].equalsIgnoreCase("keywords")) {
                    ividTo.setKeywords(         ividFrom.getKeywords());
                }
                if (fields[p].equalsIgnoreCase("people")) {
                    ividTo.setPeople(           ividFrom.getPeople());
                }
                if (fields[p].equalsIgnoreCase("note")) {
                    ividTo.setNote(             ividFrom.getNote());
                }
                if (fields[p].equalsIgnoreCase("restrictions")) {
                    ividTo.setRestrictions(     ividFrom.getRestrictions());
                }
            }
        }
    }

    /**
     *
     * @param metadataList
     * @deprecated muss nicht gespeichert werden, da keine lang-daten beinhaltet!
     */
    private void saveMetadata(List metadataList) {

        ImageMetadataService ims = new ImageMetadataService();
        Iterator metadataIt = metadataList.iterator();
        while (metadataIt.hasNext()) {
            Metadata metadata = (Metadata)metadataIt.next();
            //nur lang-daten ändern!
            if (metadata.getLang().length()>0) {
                if (metadata.getImdid()==-1) {
                    //add
                    logger.debug("add Metadata to image, ivid: "+metadata.getIvid()+", imageid: "+metadata.getImageId()+" data: "+metadata.getMetaValue());
                    ims.addMetadata(metadata);
                } else {
                    //update
                    logger.debug("update Metadata to image, ivid: "+metadata.getIvid()+", imageid: "+metadata.getImageId()+" data: "+metadata.getMetaValue());
                    ims.saveMetadata(metadata);
                }
            }
        }
    }

    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
        if (e.hasErrors()) {
            e.getAllErrors();
        }
        super.onBindAndValidate(request, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}