package com.stumpner.mediadesk.core;

import com.stumpner.mediadesk.util.IniFile;
import com.stumpner.mediadesk.util.MailWrapper;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.lic.License;
import com.stumpner.mediadesk.web.mvc.FormatListController;
import com.stumpner.mediadesk.web.mvc.ThumbnailModuleController;
import com.stumpner.mediadesk.web.ResourceBundleChanger;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.media.importing.*;
import com.stumpner.mediadesk.media.Format;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.store.SqlAclStore;
import com.stumpner.mediadesk.upload.ImportPluginHandlerChain;
import com.stumpner.mediadesk.upload.ImportPluginHandler;

import java.util.*;
import java.util.List;
import java.sql.SQLException;
import java.io.File;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

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
 * Date: 11.01.2005
 * Time: 22:49:41
 * To change this template use File | Settings | File Templates.
 */
public class Config {

    //Hauptsettings (vorm Compilieren)

    public static boolean isConfigured = false;
    public static boolean reset = false;

    public static final int MEDIAHANDLING_IMAGEONLY = 1;
    public static final int MEDIAHANDLING_ALLMEDIA = 2;
    /**
     * Als was die DMS-Datenbank verwendet wird
     * 1 = Bilddatenbank
     * 2 = Datei-Exchange (Datei-Datenbank)
     */
    public static int mediaHandling = MEDIAHANDLING_ALLMEDIA;

    public static String iniFilename = "";

    // Instance Settings

    public static String instanceName = "mediaDESK";
    public static String httpBase = "http://localhost:8080/";

    public static String fileSystemImportPath = "/var/import";

    public static String mailserver = "mail.stumpner.net";
    public static String mailsender = "robot@openMEDIADESK.org";

    public static String mailNewPasswordMailSubject = "";
    public static String mailNewPasswordMailBody = "";

    public static String mailDownloadInfoMailSubject = "";
    public static String mailDownloadInfoMailBody = "";

    public static String mailUploadInfoMailSubject = "Upload Benachrichtigung";
    public static String mailUploadInfoMailBody = "";
    public static boolean mailUploadInfoEnabled = false;

    public static boolean creditSystemEnabled = false;

    public final static int PINCODEKEYGEN_4NUM = 1;
    public final static int PINCODEKEYGEN_8NUM = 2;
    public final static int PINCODEKEYGEN_4NUMLETTERS = 3;
    public final static int PINCODEKEYGEN_8NUMLETTERS = 4;
    public static int pinCodeKeyGen = 1;
    public static boolean useCaptchaPin = false;
    public static boolean useCaptchaRegister = true;
    public static boolean useCaptchaSend = true;

    // Programm Settings
                                                   //sr6
    public static String versionNumbner = "2018n20180122";//- a1...n,b1...n,rc1...n,rtm,sr1...n";
    public static String versionDate = "2018-01-22";
    public static String SERIAL_UID = "2018012200";

    // --- Programm: Folder

    public static int folderOrder = 0;
    public static final int FOLDERORDER_CREATEDATE = 0;
    public static final int FOLDERORDER_FOLDERDATE = 1;

    //

    public static boolean activateNewUsers = false;
    public static boolean informOfNewUsers = true;
    public static boolean passmailUser = true;
    public static boolean passmailCopyAdmin = false;
    public static boolean informDownloadAdmin = true;
    public static int defaultCredits = 0; //Credits die ein neuer User automatisch bekommt
    public static int defaultSecurityGroup = 1; //Security Group in die ein neuer User kommt
    public static int defaultRole = User.ROLE_USER; //Rolle eines neu registrierten Benutzers

    // DB Configurations

    private static Properties dbProperties = new Properties();

    // Image Configurations           

    public static int imagesizeThumbnail = 170;
    public static int imagesizePreview = 590;

    /**
     * @deprecated
     */
    public static String fileWatermark = "/var/watermark.gif";
    public static String watermarkVertical = "/var/watermark.gif";
    public static String watermarkHorizontal = "/var/watermark.gif";
    public static int watermarkIntensity = 25;

    public static String imageStorePath = "/var/is"; //todo: make configurable

    // HTML configurations

    /**
     * @deprecated Wurde abgelöst von {@link Config#customTemplate}
     */
    public static String templatePath = "/current/";

    public static String redirectStartPage = "/index/cat"; //redirect to startpage...

    public static String webTitle = "openMEDIADESK";
    public static String webKeywords = "mediaDESK Mediendatenbank";
    public static String webDescription = "";
    public static String footerCopyright = "";
    public static String footerCorpSite = "www.openMEDIADESK.org";
    public static String footerCorpLink = "http://www.openmediadesk.org";

    public static String staticContentUrlPrefix = "";
    public static String instanceLogo = "/logo.gif";

    public static boolean showLinkCategory = false; //Link zum Kategorie Index anzeigen
    public static boolean showLinkFolder = false; //Link zum Folder Index anzeigen
    public static boolean showCategoryTree = true;

    public static String css = ""; //CSS Style Sheet (klassen-anweisungen): steht hier nichts drin, wird style.conf verwendet
    public static String cssTheme = "maddox.css"; //Vorgegebene Css Styles
    public static String cssAdd = ""; //CSS Code der zusätzlich eingefügt werden soll

    // License Information

    public static Date licExpireDate = new Date(106,11,31);
    public static String licTo = "";
    public static int licMaxImages = Integer.MAX_VALUE;
    public static int licMaxMb = 0; //Anzahl der MB welche die Datenbank verbrauchen darf
    public static int licMaxUsers = 15;
    public static String licFunc = "";
    public static String licId = "";

    //feature-lic information

    public static boolean multiLang = true;
    public static boolean rss = true;
    public static int maxImageSize = 5000;
    public static boolean pinPicEnabled = true;

    // Statcounter & Google

    public static String statCounterCode = "";
    public static String googleWebmasters = "";
    public static String googleAnalytics = "";

    // Import-Settings

    public static String importName = "Job Name";
    public static String importTitle = "Job Name";
    public static String importNumber = "Object Name";
    public static String importByline = "Copyright Notice";
    public static String importPhotographerAlias = "By-line";
    public static String importSite = "By-line Title";
    public static String importPeople = "Writer/Editor";
    public static String importInfo = "Caption/Abstract";

    public static String importSubtitle = "------------";
    public static String importKeywords = "------------";
    public static String importNote = "------------";
    public static String importRestrictions = "------------";

    public static String importDate = "Date/Time";
    public static boolean importImageNumberSerially = true;

    // Language Settings

    public static String[] langFieldsImageVersion = { "versionTitle","versionSubTitle", "info" };
    public static String[] langCodesAvailable = { "de", "en" };

    // Menu Config (Image Menu)

    public static boolean copyImages = true;
    public static boolean showImageMenuMarkAll = true;
    public static boolean showImageMenuUnmarkAll = true;

    public static boolean categoryLatestOnRoot = false;

    public final static int SELECT_MODE_IMAGE = 1;
    public final static int SELECT_MODE_JAVASCRIPT = 2;
    public final static int SELECT_MODE_AJAX = 3;
    public static int imageSelectMode = SELECT_MODE_AJAX;

    // Userdefined File Encoding

    public static String fileEncoding = "UTF-8";

    // welchen Dateinamen das Bild haben soll, das im Zip-file zum download angeboten wird
    // standardmässig ist es die Bildnummer

    public static String downloadImageFilename = "versionName";

    // Anzeige Einstellung Vorschaubilder/Image Popup

    public static boolean popupIvidShowVersionTitle = true;
    public static boolean popupIvidShowVersionSubTitle = true;
    public static boolean popupIvidShowInfo = true;
    public static boolean popupIvidShowImagenumber = true;
    public static boolean popupIvidShowPhotographdate = true;
    public static boolean popupIvidShowSite = true;
    public static boolean popupIvidShowPhotographerAlias = true;
    public static boolean popupIvidShowByline = true;
    public static boolean popupIvidShowKbPixelDpi = true;
    public static boolean popupIvidShowPeople = true;
    public static boolean popupIvidShowNote = true;
    public static boolean popupIvidShowRestrictions = true;
    public static boolean popupIvidKeywords = false;

    public static boolean popupIvidPageNavTop = false; //Seiten/Blättern-Navigation über dem Bild
    public static boolean popupIvidPageNavBottom = true; //S-Navigation unter dem Bild

    //Settings

    public static boolean quickDownload = true;
    public static boolean showSendImage = true;
    public static String upstreamingStartpageUrl = "";
    public static boolean inlinePreview = false; // Vorschaubilder in der Seite (true) oder als Popup (false)

    //Search - Settings

    public static boolean searchAnd = false; //wenn true: die Such-Keywords werden UND verknüpft +

    public static int sortByLatest = SimpleLoaderClass.SORT_CREATEDATE; // Importdatum
    public static int sortByFolder = SimpleLoaderClass.SORT_CREATEDATE;
    public static int orderByFolder = SimpleLoaderClass.ORDER_DESC;
    public static int sortByCategory = SimpleLoaderClass.SORT_CREATEDATE;
    public static int orderByCategory = SimpleLoaderClass.ORDER_DESC;

    /**
     * Gravity:
     * 1 - NorthWest
     * 2 - North
     * 3 - NorthEast
     * 4 - West
     * 5 - Center
     * 6 - East
     * 7 - SouthWest
     * 8 - South
     * 9 - SouthEast
     */
    public static int gravity = 5;

    /**
     * Texte für Seiten:
     */
    public static String textCatLng1 = ""; //de
    public static String textCatLng2 = ""; //en
    public static String textFolderLng1 = ""; //de
    public static String textFolderLng2 = ""; //en
    public static String textLastLng1 = ""; //de
    public static String textLastLng2 = ""; //en

    /**
     * Texte für Popup
     */
    public static String textHelpLng1 = ""; //de
    public static String textHelpLng2 = ""; //en
    public static String textHelpSearchLng1 = ""; //de
    public static String textHelpSearchLng2 = ""; //en
    public static String textAgbLng1 = ""; //de
    public static String textAgbLng2 = ""; //en
    public static String textPrivacyLng1 = ""; //de
    public static String textPrivacyLng2 = ""; //en
    public static String textContactLng1 = ""; //de
    public static String textContactLng2 = ""; //en
    public static String textImprintLng1 = ""; //de
    public static String textImprintLng2 = ""; //en
    public static String textFaqLng1 = ""; //de
    public static String textFaqLng2 = ""; //de

    // Language

    public static boolean langUseDefault = false;
    public static boolean langAutoFill = true;

    //Edit-Copy Fields

    public static boolean editCopyTitle = false;
    public static boolean editCopyTitleLng1 = false;
    public static boolean editCopyTitleLng2 = false;

    public static boolean editCopySubTitle = false;
    public static boolean editCopySubTitleLng1 = false;
    public static boolean editCopySubTitleLng2 = false;

    public static boolean editCopyInfo = false;
    public static boolean editCopyInfoLng1 = false;
    public static boolean editCopyInfoLng2 = false;

    public static boolean editCopySite = true;
    public static boolean editCopyPhotographDate = true;
    public static boolean editCopyPhotographer = true;
    public static boolean editCopyByline = true;
    public static boolean editCopyKeywords = false;

    public static boolean editCopyPeople = false;
    public static boolean editCopyOrientation = false;
    public static boolean editCopyPerspective = false;
    public static boolean editCopyMotive = false;
    public static boolean editCopyGesture = false;
    public static boolean editCopyNote = false;
    public static boolean editCopyRestrictions = false;
    public static boolean editCopyFlag = false;

    public static boolean editCopySiteLng1 = true;
    public static boolean editCopySiteLng2 = true;
    public static boolean editCopyNoteLng1 = false;
    public static boolean editCopyNoteLng2 = false;
    public static boolean editCopyRestrictionsLng1 = false;
    public static boolean editCopyRestrictionsLng2 = false;

    public static boolean editCopyPrice = false;
    public static boolean editCopyLicValid = false;
    //Kontakt-Popup mit Mailformular

    public static boolean useContactForm = true;

    public static int searchPerEmail = 2; //0 - nein, 1 - ja, sofort, 2 - stündlich, 3 - täglich
    public static boolean showFormOnEmptySearch = true;

    //download - setting

    public static boolean useShoppingCart = mediaHandling == MEDIAHANDLING_IMAGEONLY ? true : false;
    public static boolean useLightbox = mediaHandling == MEDIAHANDLING_IMAGEONLY ? true : false;

    public static ImportFactory importFactory = null;
    public static String importFactoryClass = "";

    public static boolean useDownloadResolutions = false; //Verschiedene Download auflösungen verwenden
    public static boolean downloadResOrig = true;
    public static List downloadRes = new LinkedList(); //Auflösungen (gelöst über rectangle-Klassen)

    public static boolean showDownloadToVisitors = true; //Download-Link auch bei Visitors (nicht eingeloggte Benutzer einblenden)

    public static boolean podcastEnabled = false; //Podcasts können über /rss/podcast/... und /podcast/.. heruntergeladen werden (funktion ist nur bei DMS aktiv)

    public static boolean complexPasswords = true; //Kompexe Passwörter verwenden
    public static boolean robotsAllow = true; //Robots.txt Suchroboter erlauben
    //@deprecated: categorySort
    public static boolean categorySortByName = false; //@deprecated: Folder Sort By Name
    public static int categorySort = 1; //Folder Sort: 1 = Name, 2 = Titel, 3 = createDate, 4 = categoryDate
    public static File webroot = null;

    public static int homeCategoryId = -1; //CategoryId in denen sich die "Home-Verzeichnisse/Kategorien" der User befinden, bei -1 wird die homecategory funktion nicht verwendet
    public static boolean homeCategoryAsRoot = false; //Das Home-Vereichnis wird als Wurzelverzeichnis angezeigt, andere Verzeichnisse werden nicht angezeigt
    public static boolean homeCategoryAutocreate = false; //Ob das Home-Verzeichnis/Benutzerkategorie automatisch (beim erstellen eines Benutzers) erzeugt wird

    public static boolean autoimportFtp = false; //Ob die Dateien am FTP-Server automatisch imortiert werden sollen
    public static int autoImportFtpCat = 0;

    public static boolean emailImportEnabled = false;
    public static String emailImportHost = "";
    public static String emailImportUsername = "";
    public static String emailImportPassword = "";
    public static String emailImportLastError = "";
    public static int autoImportEmailCat = 0;

    public static int itemCountPerPage = 45;

    // Licence
    //siehe Class License

    //Auflistungs-Felder
    public static String customList1Lng1 = "Eigene Auflistung1";
    public static String customList1Lng2 = "Own List1";
    public static String customList2Lng1 = "Eigene Auflistung2";
    public static String customList2Lng2 = "Own List2";
    public static String customList3Lng1 = "Eigene Auflistung3";
    public static String customList3Lng2 = "Own List3";

    public static String customStr1 = ""; //Indiv String Felder
    public static String customStr2 = "";
    public static String customStr3 = "";
    public static String customStr4 = "";
    public static String customStr5 = "";
    public static String customStr6 = "";
    public static String customStr7 = "";
    public static String customStr8 = "";
    public static String customStr9 = "";
    public static String customStr10 = "";

    public static String customTemplate = "bootstrap";
    public static String forbiddenDomains = "";

    public static int categoryDefaultViewOnRoot = 0;

    public static boolean usersCanSendAttachments = false;

    public static String ftpHost = "";
    public static String ftpUser = "";
    public static String ftpPassword = "";

    public static boolean wsUsersyncEnabled = false;
    public static String wsUsersyncUrl = "";
    public static String wsUsersyncUsername = "";
    public static String wsUsersyncPassword = "";
    public static String wsUsersyncGroupnameFilter = "";
    public static boolean wsUsersyncTrustAllCerts = false;

    //Stream
    public static boolean streamEnabled = true;
    public static boolean streamToVisitors = true;
    public static int streamConvertToKbitSek = 0; //Auf wieviel kbit/sek das Video für das Streaming runtergerechnet werden soll

    //Webdav
    public static boolean webdavEnabled = true;

    //Anzeige Vorschaufenster:
    public static boolean blankWhenFieldEmpty = true;

    public static boolean userEmailAsUsername = true;
    public static boolean showUserCompanyFields = false;
    public static boolean showUserAddressFields = false;
    public static boolean showUserTelFaxFields = false;

    public static boolean allowRegister = true;

    public static boolean advancedSearchEnabled = true;

    public static boolean resetSecurityGroupWhenUserIsDisabled = false;

    public static boolean useAutoLogin = false;
    public static boolean onlyLoggedinUsers = false; //Nur eingeloggte Benutzer dürfen mediaDESK verwenden
    public static boolean showSubCategoryInListView = true; //todo:
    public static boolean showSubCategoryInListViewOnlyWhenEmpty = true; //Unterkategorien im Listview nur anzeigen wenn keine anderen Mediendateien existieren//todo:

    public static boolean dataView = false; //Neue dataView Ansicht (2014)

    public static String configParam = "";

    public static String paymillKeyPublic = "";
    public static String paymillKeyPrivate = "";
    public static int vatPercent = 0;

    public static String currency = ""; //Bei currency="" wird das Creditsystem verwendet: Jedes Bild hat dann einen credit

    public static Date uptime = new Date();
    public static String lastError500 = "";
    public static String lastError400 = "";
    public static Date lastLogin = null;

    //XML SplashPageData
    public static String splashPageData = new String();

    //ImportPlugin
    public static String importPluginClass[] = null;

    public static boolean smtpUseTls = false;
    public static String smtpUsername = "";
    public static String smtpPassword = "";
    public static String mailReceiverAdminEmail = "";

    public static String cssColorPrimaryHex = ""; /* Hex Code der Primären Farbe */
    public static String cssColorAHref = ""; /* Hex Code der Link Farbe */

    public static void reloadMaxMb(ServletContext servletContext) {

        IniFile iniFile = new IniFile();
        iniFile.open(servletContext.getRealPath(WebContextListener.configFile));
        
        Config.licMaxMb = Integer.parseInt(iniFile.getProperty("licMaxMb","0"));
        Config.maxImageSize = Integer.parseInt(iniFile.getProperty("maxImageSize","5000"));
        Config.licFunc = getConvertedLicFunc(iniFile.getProperty("licFunc","-none"));
        Config.licId = iniFile.getProperty("licId","");
        Config.reset = iniFile.getProperty("reset","false").equalsIgnoreCase("true") ? true:false;

    }

    private static String getConvertedLicFunc(String property) {
        if (property.equalsIgnoreCase("-all")) {
            property = "-template -thumbsize -mailserver -noads";
        }

        return property;
    }

    public static void initConfiguration(IniFile iniFile) {

        //feature-lic config (kann nicht verändert werden)
        reset = iniFile.getProperty("reset","false").equalsIgnoreCase("true") ? true:false;
        multiLang = iniFile.getProperty("multiLang","true").equalsIgnoreCase("true") ? true:false;
        licFunc = getConvertedLicFunc(iniFile.getProperty("licFunc","-none"));
        licId = iniFile.getProperty("licId","");
        rss = iniFile.getProperty("rss","true").equalsIgnoreCase("true") ? true:false;
        maxImageSize = Integer.parseInt(iniFile.getProperty("maxImageSize","5000"));
        pinPicEnabled = iniFile.getProperty("pinPicEnabled","true").equalsIgnoreCase("true") ? true:false;

        instanceName = iniFile.getProperty("instanceName","mediaDESK");
        httpBase = iniFile.getProperty("httpBase","http://nourlconfigured");
        fileSystemImportPath = iniFile.getProperty("fileSystemImportPath","/notconfigured");

        watermarkVertical = iniFile.getProperty("watermarkVertical","/var/watermark.gif");
        watermarkHorizontal = iniFile.getProperty("watermarkHorizontal","/var/watermark.gif");
        watermarkIntensity = Integer.parseInt(iniFile.getProperty("watermarkIntensity","30"));

        webTitle = iniFile.getProperty("webTitle","mediaDESK (R)");
        webKeywords = iniFile.getProperty("webKeywords","mediaDESK Medien Datenbank");
        webDescription = iniFile.getProperty("webDescription","");
        footerCopyright = iniFile.getProperty("footerCopyright","copyright goes here");
        footerCorpSite = iniFile.getProperty("footerCorpSite","corporate site");
        footerCorpLink = iniFile.getProperty("footerCorpLink","http://linktocorporatesite");
        instanceLogo = iniFile.getProperty("instanceLogo","/img/LOGO.GIF");
        watermarkIntensity = Integer.parseInt(iniFile.getProperty("watermarkIntensity","25"));

        mailserver = iniFile.getProperty("mailserver","mail.stumpner.net");
        mailsender = iniFile.getProperty("mailsender","robot@mediaDESK.net");

        //staticContentUrlPrefix = iniFile.getProperty("staticContentUrlPrefix","http://www.suside.net/help/");
        licMaxImages = Integer.parseInt(iniFile.getProperty("licMaxImages",String.valueOf(Integer.MAX_VALUE)));
        licMaxMb = Integer.parseInt(iniFile.getProperty("licMaxMb","0"));

        imageStorePath = iniFile.getProperty("imageStorePath","/var/is");

        /** Deprecated: since mediaDESK 3.3, daher standardwerte **/
        showLinkCategory = false; //iniFile.getProperty("showLinkCategory",String.valueOf(showLinkCategory)).equalsIgnoreCase("true") ? true : false;
        showLinkFolder = false; //iniFile.getProperty("showLinkFolder",String.valueOf(showLinkFolder)).equalsIgnoreCase("true") ? true : false;

        creditSystemEnabled = iniFile.getProperty("creditSystemEnabled","false").equalsIgnoreCase("true") ? true : false;

        dbProperties.setProperty("JDBC.ConnectionURL",iniFile.getProperty("db.connectionUrl"));
        dbProperties.setProperty("JDBC.Username",iniFile.getProperty("db.username"));
        dbProperties.setProperty("JDBC.Password",iniFile.getProperty("db.password"));
        //new-driver!!
        dbProperties.setProperty("JDBC.Driver",iniFile.getProperty("db.jdbcDriver"));

        //Download-Resolutions
        downloadRes.add(new Format(800,600));
        downloadRes.add(new Format(1024,768));
        downloadRes.add(new Format(1280,1024));

        //Temp-Path im Image-Store-Path erstellen:
        File tmpPath = new File(getTempPath());
        if (tmpPath.exists() && tmpPath.isDirectory()) {
            //tmpPath existiert, leeren... //todo: tmp path leeren
            System.out.println("Cleanup tmp-path: "+getTempPath());
        } else {
            //tmpPath erstellen
            System.out.println("Create tmp-path: "+getTempPath());
            tmpPath.mkdirs();
        }

        File templateArchivePath = new File(getTemplateArchivePath());
        if (templateArchivePath.exists() && templateArchivePath.isDirectory()) {
            //path exists + ok
        } else {
            System.out.println("Create Template-Archive Path");
            templateArchivePath.mkdirs();
        }

    /*public static int downloadResX[] = { 800, 1024, 1280 };
    public static int downloadResY[] = { 600, 768,  1024 };*/

        /*
        //Import-FactoryClass (wie importiert werden soll)
        importFactoryClass = iniFile.getProperty("importFactoryClass","");


        //Dynamisch instanzieren: (noch nicht)
        try {
            Class cls = Class.forName(importFactoryClass);
            importFactory = (ImportFactory)cls.newInstance();
            Logger logger  = Logger.getLogger(Config.class);
            logger.info("ImportFactoryClass = "+importFactory.getClass().getName());
        } catch (ClassNotFoundException e) {
            System.err.println("ImportFactoryClass ["+importFactoryClass+"] not found, using default: DmsImportFactory");
            importFactory = new DmsImportFactory();
        } catch (IllegalAccessException e) {
            System.err.println("ImportFactoryClass ["+importFactoryClass+"] not accessable, using default: DmsImportFactory");
            importFactory = new DmsImportFactory();
        } catch (InstantiationException e) {
            System.err.println("ImportFactoryClass ["+importFactoryClass+"] not instantiable, using default: DmsImportFactory");
            importFactory = new DmsImportFactory();
        }
        */

        try {
            ResourceBundleChanger.changeResourceBundles();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            importFactory = new MediaImportFactory();
        //} else {
        //    importFactory = new ImageImportFactory();
        //}
        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            //Filelist verwenden
            ThumbnailModuleController.setDefaultViewMode(ThumbnailModuleController.USE_FILELIST);
        } else {
            ThumbnailModuleController.setDefaultViewMode(ThumbnailModuleController.USE_THUMBNAIL);
        }

        License.initLic(iniFile);

    }

    public static void saveConfiguration() {

        alterConf();
    }

    public static void loadConfigurationFromDB() {

        //Wichtigste Einstellungen zuerst

        //Access Controller konfigurieren (es gibt keine Einstellung mehr dass keine Berechtigungen verwendet werden)
        try {
            AclController.setStore(new SqlAclStore(AppSqlMap.getSqlMapInstance().getDataSource()));
            AclController.setEnabled(true);
        } catch (SQLException e) {
            System.err.println("Fehler beim Initialisieren des ACL-Sql-Store, probleme mit der Datenbank");
            throw new RuntimeException("Fehler beim Initialisieren des ACL-Sql-Store, probleme mit der Datenbank");
        }

        //Download Formate/Auflösung
        String strDownloadRes = null;
        boolean createAcl = false;
        try {
            strDownloadRes = (
                loadPropertyFromDB("downloadRes")
                );
            //"800x600;1024x768;1280x1024"
        } catch (Exception e) {
            strDownloadRes = "800x600;1024x768;1280x1024";
            alterProperty("","downloadRes","800x600;1024x768;1280x1024");
            createAcl = true;
            System.out.println("No Acl for Download-Resolution exist, creating...");
        }

        //Umkonvertieren in Format-Klassen
        Format formatt = new Format(0,0);
        Config.downloadRes.clear();
        Config.downloadRes.add(formatt); // 0,0 = Original-Format
        if (createAcl) {
            System.out.println("Create Acl-Data For Group with objectSerialId="+formatt.getAclObjectSerialId());
            FormatListController.addAccessToAllGroups(formatt);
        }
        if (strDownloadRes.trim().length()>0) {
            //System.out.println("Download Formate erstellen");
            //Download-Formate nur erstellen, wenn welche angelegt wurden
            String[] formats = strDownloadRes.split(";");
            for (int i=0;i<formats.length;i++) {
                String[] format = formats[i].split("x");
                int width = (int)Double.parseDouble(format[0]);
                int height = (int)Double.parseDouble(format[1]);
                Format formatObj = new Format(width,height);
                Config.downloadRes.add(formatObj);
                if (createAcl) {
                    System.out.println("Create Acl-Data For Group with objectSerialId="+formatObj.getAclObjectSerialId());
                    FormatListController.addAccessToAllGroups(formatObj);
                }
            }
        }

        Config.mailsender = loadPropertyFromDB("mailsender",Config.mailsender);
        Config.mailserver = loadPropertyFromDB("mailserver",Config.mailserver);
        Config.watermarkIntensity = Integer.parseInt(
                loadPropertyFromDB("watermarkIntensity",new Integer(Config.watermarkIntensity).toString())
        );
        Config.webTitle = loadPropertyFromDB("webTitle",Config.webTitle);
        Config.footerCopyright = loadPropertyFromDB("footerCopyright",Config.footerCopyright );
        Config.footerCorpLink = loadPropertyFromDB("footerCorpLink",Config.footerCorpLink);
        Config.footerCorpSite = loadPropertyFromDB("footerCorpSite",Config.footerCorpSite);
        Config.instanceLogo = loadPropertyFromDB("instanceLogo",Config.instanceLogo);
        //Config.staticContentUrlPrefix = loadPropertyFromDB("staticContentUrlPrefix",Config.staticContentUrlPrefix);
        Config.webKeywords = loadPropertyFromDB("webKeywords",Config.webKeywords);
        Config.webDescription = loadPropertyFromDB("webDescription",Config.webDescription);
        Config.redirectStartPage = loadPropertyFromDB("redirectStartPage",Config.redirectStartPage);

        /** Deprecated: since mediaDESK 3.3
        Config.showLinkCategory = (loadPropertyFromDB("showLinkCategory",new Boolean(Config.showLinkCategory).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.showLinkFolder = (
                loadPropertyFromDB("showLinkFolder",new Boolean(Config.showLinkFolder).toString()).equalsIgnoreCase("true")) ? true:false;
         */
        Config.creditSystemEnabled = (
                loadPropertyFromDB("creditSystemEnabled", new Boolean(Config.creditSystemEnabled).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.css = loadPropertyFromDB("css",Config.css);
        Config.cssTheme = loadPropertyFromDB("cssTheme",Config.cssTheme);
        //Config.templatePath = loadPropertyFromDB("templatePath",Config.templatePath);

        Config.folderOrder = Integer.parseInt(loadPropertyFromDB("folderOrder",new Integer(Config.folderOrder).toString()));

        Config.activateNewUsers = (
                loadPropertyFromDB("activateNewUsers", new Boolean(Config.activateNewUsers).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.informOfNewUsers = (
                loadPropertyFromDB("informOfNewUsers", new Boolean(Config.informOfNewUsers).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.passmailUser = (
                loadPropertyFromDB("passmailUser", new Boolean(Config.passmailUser).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.passmailCopyAdmin = (
                loadPropertyFromDB("passmailCopyAdmin", new Boolean(Config.passmailCopyAdmin).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.informDownloadAdmin = (
                loadPropertyFromDB("informDownloadAdmin", new Boolean(Config.informDownloadAdmin).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.defaultCredits = Integer.parseInt(loadPropertyFromDB("defaultCredits",new Integer(Config.defaultCredits).toString()));

        Config.statCounterCode = loadPropertyFromDB("statCounterCode",Config.statCounterCode);
        Config.googleWebmasters = loadPropertyFromDB("googleWebmasters",Config.googleWebmasters);
        Config.googleAnalytics = loadPropertyFromDB("googleAnalytics",Config.googleAnalytics);

        Config.cssAdd = loadPropertyFromDB("cssAdd",Config.cssAdd);

        Config.importName = loadPropertyFromDB("importName",Config.importName);
        Config.importTitle = loadPropertyFromDB("importTitle",Config.importTitle);
        Config.importNumber = loadPropertyFromDB("importNumber",Config.importNumber);
        Config.importByline = loadPropertyFromDB("importByline",Config.importByline);
        Config.importPhotographerAlias = loadPropertyFromDB("importPhotographerAlias",Config.importPhotographerAlias);
        Config.importSite = loadPropertyFromDB("importSite",Config.importSite);
        Config.importPeople = loadPropertyFromDB("importPeople",Config.importPeople);
        Config.importInfo = loadPropertyFromDB("importInfo",Config.importInfo);

        Config.importSubtitle = loadPropertyFromDB("importSubtitle",Config.importSubtitle);
        Config.importKeywords = loadPropertyFromDB("importKeywords",Config.importKeywords);
        Config.importNote = loadPropertyFromDB("importNote",Config.importNote);
        Config.importRestrictions = loadPropertyFromDB("importRestrictions",Config.importRestrictions);

        Config.copyImages = (
        loadPropertyFromDB("copyImages", new Boolean(Config.copyImages).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.showImageMenuMarkAll = (
        loadPropertyFromDB("showImageMenuMarkAll", new Boolean(Config.showImageMenuMarkAll).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.showImageMenuUnmarkAll = (
        loadPropertyFromDB("showImageMenuUnmarkAll", new Boolean(Config.showImageMenuUnmarkAll).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.categoryLatestOnRoot = (
        loadPropertyFromDB("categoryLatestOnRoot", new Boolean(Config.categoryLatestOnRoot).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.imageSelectMode = Integer.parseInt(loadPropertyFromDB("imageSelectMode",Integer.toString(Config.imageSelectMode)));

        Config.importDate = loadPropertyFromDB("importDate",Config.importDate);
        Config.importImageNumberSerially = (
        loadPropertyFromDB("importImageNumberSerially", new Boolean(Config.importImageNumberSerially).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.fileEncoding = loadPropertyFromDB("fileEncoding",Config.fileEncoding);

        Config.downloadImageFilename = loadPropertyFromDB("downloadImageFilename",Config.downloadImageFilename);

        Config.inlinePreview = (loadPropertyFromDB("inlinePreview", new Boolean(Config.inlinePreview).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.popupIvidShowVersionTitle = (
        loadPropertyFromDB("popupIvidShowVersionTitle", new Boolean(Config.popupIvidShowVersionTitle).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowVersionSubTitle = (
        loadPropertyFromDB("popupIvidShowVersionSubTitle", new Boolean(Config.popupIvidShowVersionSubTitle).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowInfo = (
        loadPropertyFromDB("popupIvidShowInfo", new Boolean(Config.popupIvidShowInfo).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowImagenumber = (
        loadPropertyFromDB("popupIvidShowImagenumber", new Boolean(Config.popupIvidShowImagenumber).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowPhotographdate = (
        loadPropertyFromDB("popupIvidShowPhotographdate", new Boolean(Config.popupIvidShowPhotographdate).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowSite = (
        loadPropertyFromDB("popupIvidShowSite", new Boolean(Config.popupIvidShowSite).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowPhotographerAlias = (
        loadPropertyFromDB("popupIvidShowPhotographerAlias", new Boolean(Config.popupIvidShowPhotographerAlias).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowByline = (
        loadPropertyFromDB("popupIvidShowByline", new Boolean(Config.popupIvidShowPhotographerAlias).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowKbPixelDpi = (
        loadPropertyFromDB("popupIvidShowKbPixelDpi", new Boolean(Config.popupIvidShowKbPixelDpi).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowPeople = (
        loadPropertyFromDB("popupIvidShowPeople", new Boolean(Config.popupIvidShowPeople).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowNote = (
        loadPropertyFromDB("popupIvidShowNote", new Boolean(Config.popupIvidShowNote).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidShowRestrictions = (
        loadPropertyFromDB("popupIvidShowRestrictions", new Boolean(Config.popupIvidShowRestrictions).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidKeywords = (
        loadPropertyFromDB("popupIvidKeywords", new Boolean(Config.popupIvidKeywords).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidPageNavTop = (
        loadPropertyFromDB("popupIvidPageNavTop", new Boolean(Config.popupIvidPageNavTop).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.popupIvidPageNavBottom = (
        loadPropertyFromDB("popupIvidPageNavBottom", new Boolean(Config.popupIvidPageNavBottom).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.upstreamingStartpageUrl = (
                loadPropertyFromDB("upstreamingStartpageUrl",Config.upstreamingStartpageUrl)
                );

        Config.quickDownload = (
            loadPropertyFromDB("quickDownload", new Boolean(Config.quickDownload).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.searchAnd = (
            loadPropertyFromDB("searchAnd", new Boolean(Config.searchAnd).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.sortByLatest = Integer.parseInt(loadPropertyFromDB("sortByLatest",Integer.toString(Config.sortByLatest)));

        Config.sortByFolder = Integer.parseInt(loadPropertyFromDB("sortByFolder",Integer.toString(Config.sortByFolder)));
        Config.orderByFolder = Integer.parseInt(loadPropertyFromDB("orderByFolder",Integer.toString(Config.orderByFolder)));
        Config.sortByCategory = Integer.parseInt(loadPropertyFromDB("sortByCategory",Integer.toString(Config.sortByCategory)));
        Config.orderByCategory = Integer.parseInt(loadPropertyFromDB("orderByCategory",Integer.toString(Config.orderByCategory)));

        Config.gravity = Integer.parseInt(loadPropertyFromDB("gravity",Integer.toString(Config.gravity)));

        Config.textCatLng1 = loadPropertyFromDB("textCatLng1",Config.textCatLng1);
        Config.textCatLng2 = loadPropertyFromDB("textCatLng2",Config.textCatLng1);
        Config.textFolderLng1 = loadPropertyFromDB("textFolderLng1",Config.textCatLng1);
        Config.textFolderLng2 = loadPropertyFromDB("textFolderLng2",Config.textCatLng1);
        Config.textLastLng1 = loadPropertyFromDB("textLastLng1",Config.textCatLng1);
        Config.textLastLng2 = loadPropertyFromDB("textLastLng2",Config.textCatLng1);

        Config.textHelpLng1 = loadPropertyFromDB("textHelpLng1",Config.textHelpLng1);
        Config.textHelpLng2 = loadPropertyFromDB("textHelpLng2",Config.textHelpLng2);
        Config.textHelpSearchLng1 = loadPropertyFromDB("textHelpSearchLng1",Config.textHelpSearchLng1);
        Config.textHelpSearchLng2 = loadPropertyFromDB("textHelpSearchLng2",Config.textHelpSearchLng2);
        Config.textAgbLng1 = loadPropertyFromDB("textAgbLng1",Config.textAgbLng1);
        Config.textAgbLng2 = loadPropertyFromDB("textAgbLng2",Config.textAgbLng2);
        Config.textPrivacyLng1 = loadPropertyFromDB("textPrivacyLng1",Config.textPrivacyLng1);
        Config.textPrivacyLng2 = loadPropertyFromDB("textPrivacyLng2",Config.textPrivacyLng2);
        Config.textContactLng1 = loadPropertyFromDB("textContactLng1",Config.textContactLng1);
        Config.textContactLng2 = loadPropertyFromDB("textContactLng2",Config.textContactLng2);
        Config.textImprintLng1 = loadPropertyFromDB("textImprintLng1",Config.textImprintLng1);
        Config.textImprintLng2 = loadPropertyFromDB("textImprintLng2",Config.textImprintLng2);
        Config.textFaqLng1 = loadPropertyFromDB("textFaqLng1",Config.textFaqLng1);
        Config.textFaqLng2 = loadPropertyFromDB("textFaqLng1",Config.textFaqLng2);

        Config.langAutoFill = loadPropertyFromDB("langAutoFill",new Boolean(Config.langAutoFill).toString()).equalsIgnoreCase("true") ? true:false;

        Config.editCopyTitle = (
            loadPropertyFromDB("editCopyTitle", new Boolean(Config.editCopyTitle).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyTitleLng1 = (
            loadPropertyFromDB("editCopyTitleLng1", new Boolean(Config.editCopyTitleLng1).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyTitleLng2 = (
            loadPropertyFromDB("editCopyTitleLng2", new Boolean(Config.editCopyTitleLng2).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.editCopySubTitle = (
            loadPropertyFromDB("editCopySubTitle", new Boolean(Config.editCopySubTitle).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopySubTitleLng1 = (
            loadPropertyFromDB("editCopySubTitleLng1", new Boolean(Config.editCopySubTitleLng1).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopySubTitleLng2 = (
            loadPropertyFromDB("editCopySubTitleLng2", new Boolean(Config.editCopySubTitleLng2).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.editCopyInfo = (
            loadPropertyFromDB("editCopyInfo", new Boolean(Config.editCopyInfo).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyInfoLng1 = (
            loadPropertyFromDB("editCopyInfoLng1", new Boolean(Config.editCopyInfoLng2).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyInfoLng2 = (
            loadPropertyFromDB("editCopyInfoLng2", new Boolean(Config.editCopyInfoLng2).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.editCopySite = (
            loadPropertyFromDB("editCopySite", new Boolean(Config.editCopySite).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyPhotographDate = (
            loadPropertyFromDB("editCopyPhotographDate", new Boolean(Config.editCopyPhotographDate).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyPhotographer = (
            loadPropertyFromDB("editCopyPhotographer", new Boolean(Config.editCopyPhotographer).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyByline = (
            loadPropertyFromDB("editCopyByline", new Boolean(Config.editCopyByline).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyKeywords = (
            loadPropertyFromDB("editCopyKeywords", new Boolean(Config.editCopyKeywords).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.editCopyPeople = (
            loadPropertyFromDB("editCopyPeople", new Boolean(Config.editCopyPeople).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyOrientation = (
            loadPropertyFromDB("editCopyOrientation", new Boolean(Config.editCopyOrientation).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyPerspective = (
            loadPropertyFromDB("editCopyPerspective", new Boolean(Config.editCopyPerspective).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyMotive = (
            loadPropertyFromDB("editCopyMotive", new Boolean(Config.editCopyMotive).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyGesture = (
            loadPropertyFromDB("editCopyGesture", new Boolean(Config.editCopyGesture).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyNote = (
            loadPropertyFromDB("editCopyNote", new Boolean(Config.editCopyNote).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyRestrictions = (
            loadPropertyFromDB("editCopyRestrictions", new Boolean(Config.editCopyRestrictions).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyFlag = (
            loadPropertyFromDB("editCopyFlag", new Boolean(Config.editCopyFlag).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.editCopySiteLng1 = (
            loadPropertyFromDB("editCopySiteLng1", new Boolean(Config.editCopySiteLng1).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopySiteLng2 = (
            loadPropertyFromDB("editCopySiteLng2", new Boolean(Config.editCopySiteLng2).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyNoteLng1 = (
            loadPropertyFromDB("editCopyNoteLng1", new Boolean(Config.editCopyNoteLng1).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyNoteLng2 = (
            loadPropertyFromDB("editCopyNoteLng2", new Boolean(Config.editCopyNoteLng2).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyRestrictionsLng1 = (
            loadPropertyFromDB("editCopyRestrictionsLng1", new Boolean(Config.editCopyRestrictionsLng1).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.editCopyRestrictionsLng2 = (
            loadPropertyFromDB("editCopyRestrictionsLng2", new Boolean(Config.editCopyRestrictionsLng2).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.useContactForm = (
            loadPropertyFromDB("useContactForm", new Boolean(Config.useContactForm).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.searchPerEmail = Integer.parseInt(loadPropertyFromDB("searchPerEmail",Integer.toString(Config.searchPerEmail)));
        Config.showFormOnEmptySearch = (
            loadPropertyFromDB("showFormOnEmptySearch", new Boolean(Config.showFormOnEmptySearch).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.useShoppingCart = (
            loadPropertyFromDB("useShoppingCart", new Boolean(Config.useShoppingCart).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.useLightbox = (
            loadPropertyFromDB("useLightbox", new Boolean(Config.useLightbox).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.mailNewPasswordMailSubject = loadPropertyFromDB("mailNewPasswordMailSubject","[Password] - "+Config.webTitle);
        Config.mailNewPasswordMailBody = loadPropertyFromDB("mailNewPasswordMailBody","Ihre Zugangsdaten fuer: {0}\n\nBenutzername: {1}\nPasswort: {2}\nSie koennen sich nun unter {3} einloggen.\n\n--------------------------------------\nYour Account for: {0}\n\nUsername: {1}\nPassword: {2}\n\nLogin on our Website {3}");


        Config.mailDownloadInfoMailSubject = loadPropertyFromDB("mailDownloadInfoMailSubject","[Download] - "+Config.webTitle);
        Config.mailDownloadInfoMailBody = loadPropertyFromDB("mailDownloadInfoMailBody","Benutzer {1} hat {2} Dateien aus mediaDESK "+Config.instanceName+" heruntergeladen: \n\n{4}\n\nDownload-Zeit:{5}");

        Config.mailUploadInfoMailSubject = loadPropertyFromDB("mailUploadInfoMailSubject","[Upload-Info] - "+Config.webTitle);
        Config.mailUploadInfoMailBody = loadPropertyFromDB("mailUploadInfoMailBody","Es wurden {1} neue Dateien "+Config.webTitle+" hochgeladen: \n\n{4}\n\nUpload-Zeit:{5}");;

        Config.mailUploadInfoEnabled = (
                loadPropertyFromDB("mailUploadInfoEnabled", new Boolean(Config.mailUploadInfoEnabled).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.useDownloadResolutions = (
                loadPropertyFromDB("useDownloadResolutions", new Boolean(Config.useDownloadResolutions).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.downloadResOrig = (
                loadPropertyFromDB("downloadResOrig", new Boolean(Config.downloadResOrig).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.showDownloadToVisitors = (
                loadPropertyFromDB("showDownloadToVisitors", new Boolean(Config.showDownloadToVisitors).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.podcastEnabled = (
                loadPropertyFromDB("podcastEnabled", new Boolean(Config.podcastEnabled).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.complexPasswords = (
                loadPropertyFromDB("complexPasswords", new Boolean(Config.complexPasswords).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.showCategoryTree = (
                loadPropertyFromDB("showCategoryTree", new Boolean(Config.showCategoryTree).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.defaultSecurityGroup = Integer.parseInt(
                loadPropertyFromDB("defaultSecurityGroup",new Integer(Config.defaultSecurityGroup).toString())
        );
        Config.defaultRole = Integer.parseInt(
                loadPropertyFromDB("defaultRole",new Integer(Config.defaultRole).toString())
        );

        Config.useCaptchaPin = (
                loadPropertyFromDB("useCaptchaPin", new Boolean(Config.useCaptchaPin).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.useCaptchaRegister = (
                loadPropertyFromDB("useCaptchaRegister", new Boolean(Config.useCaptchaRegister).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.showSendImage = (
                loadPropertyFromDB("showSendImage", new Boolean(Config.showSendImage).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.robotsAllow = (
                loadPropertyFromDB("robotsAllow", new Boolean(Config.robotsAllow).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.categorySortByName = (
                loadPropertyFromDB("categorySortByName", new Boolean(Config.categorySortByName).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.categorySort = Integer.parseInt(
                loadPropertyFromDB("categorySort", Config.categorySortByName ? "1" : "2")); 

        Config.mediaHandling = Integer.parseInt(loadPropertyFromDB("mediaHandling",Integer.toString(Config.mediaHandling)));
        try {
            ResourceBundleChanger.changeResourceBundles();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /*
        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            importFactory = new MediaImportFactory();
        } else {
            importFactory = new ImageImportFactory();
        }
        */
        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            //Filelist verwenden
            ThumbnailModuleController.setDefaultViewMode(ThumbnailModuleController.USE_FILELIST);
        } else {
            ThumbnailModuleController.setDefaultViewMode(ThumbnailModuleController.USE_THUMBNAIL);
        }

        Config.homeCategoryId = Integer.parseInt(
                loadPropertyFromDB("homeCategoryId",new Integer(Config.homeCategoryId).toString()));
        Config.homeCategoryAsRoot = (
                loadPropertyFromDB("homeCategoryAsRoot", new Boolean(Config.homeCategoryAsRoot).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.homeCategoryAutocreate = (
                loadPropertyFromDB("homeCategoryAutocreate", new Boolean(Config.homeCategoryAutocreate).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.autoimportFtp = (
                loadPropertyFromDB("autoimportFtp", new Boolean(Config.autoimportFtp).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.autoImportFtpCat = Integer.parseInt(
                loadPropertyFromDB("autoImportFtpCat",new Integer(Config.autoImportFtpCat).toString()));

        Config.emailImportEnabled = (
                loadPropertyFromDB("emailImportEnabled", new Boolean(Config.emailImportEnabled).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.emailImportHost = loadPropertyFromDB("emailImportHost",Config.emailImportHost);
        Config.emailImportUsername = loadPropertyFromDB("emailImportUsername",Config.emailImportUsername);
        Config.emailImportPassword = loadPropertyFromDB("emailImportPassword",Config.emailImportPassword);
        Config.autoImportEmailCat = Integer.parseInt(
                loadPropertyFromDB("autoImportEmailCat",new Integer(Config.autoImportEmailCat).toString()));

        Config.itemCountPerPage = Integer.parseInt(
                loadPropertyFromDB("itemCountPerPage",new Integer(Config.itemCountPerPage).toString()));

        Config.customList1Lng1 = loadPropertyFromDB("customList1Lng1", Config.customList1Lng1);
        Config.customList1Lng2 = loadPropertyFromDB("customList1Lng2", Config.customList1Lng2);
        Config.customList2Lng1 = loadPropertyFromDB("customList2Lng1", Config.customList2Lng1);
        Config.customList2Lng2 = loadPropertyFromDB("customList2Lng2", Config.customList2Lng2);
        Config.customList3Lng1 = loadPropertyFromDB("customList3Lng1", Config.customList3Lng1);
        Config.customList3Lng2 = loadPropertyFromDB("customList3Lng2", Config.customList3Lng2);

        Config.customStr1 = loadPropertyFromDB("customStr1", Config.customStr1);
        Config.customStr2 = loadPropertyFromDB("customStr2", Config.customStr2);
        Config.customStr3 = loadPropertyFromDB("customStr3", Config.customStr3);
        Config.customStr4 = loadPropertyFromDB("customStr4", Config.customStr4);
        Config.customStr5 = loadPropertyFromDB("customStr5", Config.customStr5);
        Config.customStr6 = loadPropertyFromDB("customStr6", Config.customStr6);
        Config.customStr7 = loadPropertyFromDB("customStr7", Config.customStr7);
        Config.customStr8 = loadPropertyFromDB("customStr8", Config.customStr8);
        Config.customStr9 = loadPropertyFromDB("customStr9", Config.customStr9);
        Config.customStr10 = loadPropertyFromDB("customStr10", Config.customStr10);

        Config.customTemplate = loadPropertyFromDB("customTemplate", Config.customTemplate);

        Config.useCaptchaSend = (loadPropertyFromDB("useCaptchaSend", Boolean.toString(Config.useCaptchaSend)).toString()).equalsIgnoreCase("true") ? true:false;
        Config.pinCodeKeyGen = Integer.parseInt(
                loadPropertyFromDB("pinCodeKeyGen",Integer.toString(Config.pinCodeKeyGen)));

        //forbiddenDomains
        Config.forbiddenDomains = loadPropertyFromDB("forbiddenDomains","");

        Config.categoryDefaultViewOnRoot = Integer.parseInt(
                loadPropertyFromDB("categoryDefaultViewOnRoot", new Integer(Config.categoryDefaultViewOnRoot).toString())
        );

        Config.usersCanSendAttachments = (
                loadPropertyFromDB("usersCanSendAttachments", new Boolean(Config.usersCanSendAttachments).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.ftpHost = loadPropertyFromDB("ftpHost", Config.ftpHost);
        Config.ftpUser = loadPropertyFromDB("ftpUser", Config.ftpUser);
        Config.ftpPassword = loadPropertyFromDB("ftpPassword", Config.ftpPassword);

        Config.wsUsersyncEnabled = (
                loadPropertyFromDB("wsUsersyncEnabled", new Boolean(Config.wsUsersyncEnabled).toString()).equalsIgnoreCase("true")) ? true:false;
        Config.wsUsersyncUrl = loadPropertyFromDB("wsUsersyncUrl", Config.wsUsersyncUrl);
        Config.wsUsersyncUsername = loadPropertyFromDB("wsUsersyncUsername", Config.wsUsersyncUsername);
        Config.wsUsersyncPassword = loadPropertyFromDB("wsUsersyncPassword", Config.wsUsersyncPassword);
        Config.wsUsersyncGroupnameFilter = loadPropertyFromDB("wsUsersyncGroupnameFilter", Config.wsUsersyncGroupnameFilter);

        Config.wsUsersyncTrustAllCerts = (
            loadPropertyFromDB("wsUsersyncTrustAllCerts", new Boolean(Config.wsUsersyncTrustAllCerts).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.streamEnabled = (
                loadPropertyFromDB("streamEnabled", new Boolean(Config.streamEnabled).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.streamToVisitors = (
                loadPropertyFromDB("streamToVisitors", new Boolean(Config.streamToVisitors).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.streamConvertToKbitSek = Integer.parseInt(
                loadPropertyFromDB("streamConvertToKbitSek", new Integer(Config.streamConvertToKbitSek).toString())
        );

        Config.webdavEnabled = (
                loadPropertyFromDB("webdavEnabled", new Boolean(Config.webdavEnabled).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.blankWhenFieldEmpty = (
                loadPropertyFromDB("blankWhenFieldEmpty", new Boolean(Config.blankWhenFieldEmpty ).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.userEmailAsUsername = (
                loadPropertyFromDB("userEmailAsUsername", new Boolean(Config.userEmailAsUsername).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.showUserCompanyFields = (
                loadPropertyFromDB("showUserCompanyFields", new Boolean(Config.showUserCompanyFields).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.showUserAddressFields = (
                loadPropertyFromDB("showUserAddressFields", new Boolean(Config.showUserAddressFields).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.showUserTelFaxFields = (
                loadPropertyFromDB("showUserTelFaxFields", new Boolean(Config.showUserTelFaxFields).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.allowRegister = (
                loadPropertyFromDB("allowRegister", new Boolean(Config.allowRegister).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.multiLang = (
                loadPropertyFromDB("multiLang", new Boolean(Config.multiLang).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.advancedSearchEnabled = (
                loadPropertyFromDB("advancedSearchEnabled", new Boolean(Config.advancedSearchEnabled).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.resetSecurityGroupWhenUserIsDisabled = (
                loadPropertyFromDB("resetSecurityGroupWhenUserIsDisabled", new Boolean(Config.resetSecurityGroupWhenUserIsDisabled).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.onlyLoggedinUsers = (
                loadPropertyFromDB("onlyLoggedinUsers", new Boolean(Config.onlyLoggedinUsers).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.showSubCategoryInListView = (
                loadPropertyFromDB("showSubCategoryInListView", new Boolean(Config.showSubCategoryInListView).toString()).equalsIgnoreCase("true")) ? true:false;

        Config.dataView = (
                loadPropertyFromDB("dataView", new Boolean(Config.dataView).toString()).equalsIgnoreCase("true")) ? true: false;

        Config.configParam = loadPropertyFromDB("configParam","");

        Config.paymillKeyPublic = loadPropertyFromDB("paymillKeyPublic","");
        Config.paymillKeyPrivate = loadPropertyFromDB("paymillKeyPrivate","");
        Config.vatPercent = Integer.parseInt(
                loadPropertyFromDB("vatPercent", new Integer(Config.vatPercent).toString())
        );

        Config.currency = loadPropertyFromDB("currency","");

        Config.editCopyPrice = (
                loadPropertyFromDB("editCopyPrice", new Boolean(Config.editCopyPrice).toString()).equalsIgnoreCase("true")) ? true: false; 
        Config.editCopyLicValid = (
                loadPropertyFromDB("editCopyLicValid", new Boolean(Config.editCopyLicValid).toString()).equalsIgnoreCase("true")) ? true: false;

        //Import Plugins
        String importPluginClassConfigString = loadPropertyFromDB("importPluginClass", null);
        if (importPluginClassConfigString!=null) {
            Config.importPluginClass = importPluginClassConfigString.split("\n");
        }

        Config.splashPageData = loadPropertyFromDB("splashPageData","");

        Config.smtpUseTls = (
                loadPropertyFromDB("smtpUseTls", new Boolean(Config.smtpUseTls).toString()).equalsIgnoreCase("true")) ? true: false;
        Config.smtpUsername = loadPropertyFromDB("smtpUsername","");
        Config.smtpPassword = loadPropertyFromDB("smtpPassword","");
        Config.mailReceiverAdminEmail = loadPropertyFromDB("mailReceiverAdminEmail","");

        if (Config.mailReceiverAdminEmail.isEmpty()) {
            UserService userService = new UserService();
            try {
                User admin = (User) userService.getByName("admin");
                System.out.println("Using admin Email as Receiver Admin Email");
                Config.mailReceiverAdminEmail = admin.getEmail();
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        MailWrapper.setConfig(Config.smtpUsername, Config.smtpPassword, Config.smtpUseTls);

        Config.cssColorPrimaryHex = loadPropertyFromDB("cssColorPrimaryHex","");
        Config.cssColorAHref = loadPropertyFromDB("cssColorAHref","");

        Config.imagesizeThumbnail = Integer.parseInt(
                loadPropertyFromDB("imagesizeThumbnail", new Integer(Config.imagesizeThumbnail).toString())
        );
        Config.imagesizePreview = Integer.parseInt(
                loadPropertyFromDB("imagesizePreview", new Integer(Config.imagesizePreview).toString())
        );

        saveConfiguration();
    }

    private static String loadPropertyFromDB(String key, String defaultValue) {

        String property = "";

        try {
            SqlMapClient smc =AppSqlMap.getSqlMapInstance();
            property = (String)smc.queryForObject("getConfig",key);
            if (property == null) {
                property =  defaultValue;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return property;
    }

    /**
     * Wirft eine Exception wenn dieser Wert in der DB als Key nicht gefunden wurde
     * @param key
     * @return
     * @throws Exception
     */
    private static String loadPropertyFromDB(String key) throws Exception {

        String property = "";

        try {
            SqlMapClient smc =AppSqlMap.getSqlMapInstance();
            property = (String)smc.queryForObject("getConfig",key);
            if (property == null) {
                throw new Exception();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return property;
    }

    private static String alterConf() {

        String conf = "";
        conf = alterProperty(conf,"mailsender",Config.mailsender);
        conf = alterProperty(conf,"mailserver",Config.mailserver);

        conf = alterProperty(conf,"watermarkIntensity",new Integer(Config.watermarkIntensity).toString());
        conf = alterProperty(conf,"webTitle",Config.webTitle);
        conf = alterProperty(conf,"footerCopyright",Config.footerCopyright);
        conf = alterProperty(conf,"footerCorpLink",Config.footerCorpLink);
        conf = alterProperty(conf,"footerCorpSite",Config.footerCorpSite);
        conf = alterProperty(conf,"instanceLogo",Config.instanceLogo);
        //conf = alterProperty(conf,"staticContentUrlPrefix",Config.staticContentUrlPrefix);

        conf = alterProperty(conf,"webKeywords",Config.webKeywords);
        conf = alterProperty(conf,"webDescription",Config.webDescription);

        conf = alterProperty(conf,"redirectStartPage",Config.redirectStartPage);

        /** Deprecated: since 3.3
        conf = alterProperty(conf,"showLinkCategory",new Boolean(Config.showLinkCategory).toString());
        conf = alterProperty(conf,"showLinkFolder",new Boolean(Config.showLinkFolder).toString());
         */

        conf = alterProperty(conf,"creditSystemEnabled",new Boolean(Config.creditSystemEnabled).toString());

        conf = alterProperty(conf,"css",Config.css);
        //conf = alterProperty(conf,"templatePath",Config.templatePath);

        conf = alterProperty(conf,"cssTheme",Config.cssTheme);

        conf = alterProperty(conf,"folderOrder",new Integer(Config.folderOrder).toString());

        conf = alterProperty(conf,"activateNewUsers",new Boolean(Config.activateNewUsers).toString());
        conf = alterProperty(conf,"informOfNewUsers",new Boolean(Config.informOfNewUsers).toString());
        conf = alterProperty(conf,"passmailUser",new Boolean(Config.passmailUser).toString());
        conf = alterProperty(conf,"passmailCopyAdmin",new Boolean(Config.passmailCopyAdmin).toString());
        conf = alterProperty(conf,"informDownloadAdmin",new Boolean(Config.informDownloadAdmin).toString());
        conf = alterProperty(conf,"defaultCredits", new Integer(defaultCredits).toString());

        conf = alterProperty(conf,"statCounterCode",Config.statCounterCode);
        conf = alterProperty(conf,"googleWebmasters",Config.googleWebmasters);
        conf = alterProperty(conf,"googleAnalytics",Config.googleAnalytics);
        conf = alterProperty(conf,"cssAdd",Config.cssAdd);

        conf = alterProperty(conf,"importName",Config.importName);
        conf = alterProperty(conf,"importTitle",Config.importTitle);
        conf = alterProperty(conf,"importNumber",Config.importNumber);
        conf = alterProperty(conf,"importByline",Config.importByline);
        conf = alterProperty(conf,"importPhotographerAlias",Config.importPhotographerAlias);
        conf = alterProperty(conf,"importSite",Config.importSite);
        conf = alterProperty(conf,"importPeople",Config.importPeople);
        conf = alterProperty(conf,"importInfo",Config.importInfo);
        conf = alterProperty(conf,"importSubtitle",Config.importSubtitle);
        conf = alterProperty(conf,"importKeywords",Config.importKeywords);
        conf = alterProperty(conf,"importNote",Config.importNote);
        conf = alterProperty(conf,"importRestrictions",Config.importRestrictions);


        Config.importSubtitle = loadPropertyFromDB("importSubtitle",Config.importSubtitle);
        Config.importKeywords = loadPropertyFromDB("importKeywords",Config.importKeywords);
        Config.importNote = loadPropertyFromDB("importNote",Config.importNote);
        Config.importRestrictions = loadPropertyFromDB("importRestrictions",Config.importRestrictions);

        conf = alterProperty(conf,"copyImages",new Boolean(Config.copyImages).toString());
        conf = alterProperty(conf,"showImageMenuMarkAll",new Boolean(Config.showImageMenuMarkAll).toString());
        conf = alterProperty(conf,"showImageMenuUnmarkAll",new Boolean(Config.showImageMenuUnmarkAll).toString());
        conf = alterProperty(conf,"categoryLatestOnRoot",new Boolean(Config.categoryLatestOnRoot).toString());
        conf = alterProperty(conf,"imageSelectMode",new Integer(Config.imageSelectMode).toString());

        conf = alterProperty(conf,"importDate",Config.importDate);
        conf = alterProperty(conf,"importImageNumberSerially",new Boolean(Config.importImageNumberSerially).toString());

        conf = alterProperty(conf,"fileEncoding",Config.fileEncoding);
        conf = alterProperty(conf,"downloadImageFilename",Config.downloadImageFilename);
        //System.out.println(conf);

        // popup ivid anzeigeeinstellung

        conf = alterProperty(conf,"inlinePreview",new Boolean(Config.inlinePreview).toString());
        conf = alterProperty(conf,"popupIvidShowVersionTitle",new Boolean(Config.popupIvidShowVersionTitle).toString());
        conf = alterProperty(conf,"popupIvidShowVersionSubTitle",new Boolean(Config.popupIvidShowVersionSubTitle).toString());
        conf = alterProperty(conf,"popupIvidShowInfo",new Boolean(Config.popupIvidShowInfo).toString());
        conf = alterProperty(conf,"popupIvidShowImagenumber",new Boolean(Config.popupIvidShowImagenumber).toString());
        conf = alterProperty(conf,"popupIvidShowPhotographdate",new Boolean(Config.popupIvidShowPhotographdate).toString());
        conf = alterProperty(conf,"popupIvidShowSite",new Boolean(Config.popupIvidShowSite).toString());
        conf = alterProperty(conf,"popupIvidShowPhotographerAlias",new Boolean(Config.popupIvidShowPhotographerAlias).toString());
        conf = alterProperty(conf,"popupIvidShowByline",new Boolean(Config.popupIvidShowByline).toString());
        conf = alterProperty(conf,"popupIvidShowKbPixelDpi",new Boolean(Config.popupIvidShowKbPixelDpi).toString());
        conf = alterProperty(conf,"popupIvidShowPeople", new Boolean(Config.popupIvidShowPeople).toString());
        conf = alterProperty(conf,"popupIvidShowNote",new Boolean(Config.popupIvidShowNote).toString());
        conf = alterProperty(conf,"popupIvidShowRestrictions",new Boolean(Config.popupIvidShowRestrictions).toString());
        conf = alterProperty(conf,"popupIvidKeywords",new Boolean(Config.popupIvidKeywords).toString());
        conf = alterProperty(conf,"popupIvidPageNavTop",new Boolean(Config.popupIvidPageNavTop).toString());
        conf = alterProperty(conf,"popupIvidPageNavBottom",new Boolean(Config.popupIvidPageNavBottom).toString());

        conf = alterProperty(conf,"upstreamingStartpageUrl",Config.upstreamingStartpageUrl);
        conf = alterProperty(conf,"quickDownload",new Boolean(Config.quickDownload).toString());

        conf = alterProperty(conf,"searchAnd",new Boolean(Config.searchAnd).toString());
        conf = alterProperty(conf,"sortByLatest",Integer.toString(Config.sortByLatest));

        conf = alterProperty(conf,"sortByFolder",Integer.toString(Config.sortByFolder));
        conf = alterProperty(conf,"orderByFolder",Integer.toString(Config.orderByFolder));
        conf = alterProperty(conf,"sortByCategory",Integer.toString(Config.sortByCategory));
        conf = alterProperty(conf,"orderByCategory",Integer.toString(Config.orderByCategory));

        conf = alterProperty(conf,"gravity",Integer.toString(Config.gravity));

        conf = alterProperty(conf,"textCatLng1",Config.textCatLng1);
        conf = alterProperty(conf,"textCatLng2",Config.textCatLng2);
        conf = alterProperty(conf,"textFolderLng1",Config.textFolderLng1);
        conf = alterProperty(conf,"textFolderLng2",Config.textFolderLng2);
        conf = alterProperty(conf,"textLastLng1",Config.textLastLng1);
        conf = alterProperty(conf,"textLastLng2",Config.textLastLng2);

        conf = alterProperty(conf,"textHelpLng1",Config.textHelpLng1);
        conf = alterProperty(conf,"textHelpLng2",Config.textHelpLng2);
        conf = alterProperty(conf,"textHelpSearchLng1",Config.textHelpSearchLng1);
        conf = alterProperty(conf,"textHelpSearchLng2",Config.textHelpSearchLng2);
        conf = alterProperty(conf,"textAgbLng1",Config.textAgbLng1);
        conf = alterProperty(conf,"textAgbLng2",Config.textAgbLng2);
        conf = alterProperty(conf,"textPrivacyLng1",Config.textPrivacyLng1);
        conf = alterProperty(conf,"textPrivacyLng2",Config.textPrivacyLng2);
        conf = alterProperty(conf,"textContactLng1",Config.textContactLng1);
        conf = alterProperty(conf,"textContactLng2",Config.textContactLng2);
        conf = alterProperty(conf,"textImprintLng1",Config.textImprintLng1);
        conf = alterProperty(conf,"textImprintLng2",Config.textImprintLng2);
        conf = alterProperty(conf,"textFaqLng1",Config.textFaqLng1);
        conf = alterProperty(conf,"textFaqLng2",Config.textFaqLng2);

        conf = alterProperty(conf,"langAutoFill",new Boolean(Config.langAutoFill).toString());

        conf = alterProperty(conf,"editCopyTitle",new Boolean(Config.editCopyTitle).toString());
        conf = alterProperty(conf,"editCopyTitleLng1",new Boolean(Config.editCopyTitleLng1).toString());
        conf = alterProperty(conf,"editCopyTitleLng2",new Boolean(Config.editCopyTitleLng2).toString());
        conf = alterProperty(conf,"editCopySubTitle",new Boolean(Config.editCopySubTitle).toString());
        conf = alterProperty(conf,"editCopySubTitleLng1",new Boolean(Config.editCopySubTitleLng1).toString());
        conf = alterProperty(conf,"editCopySubTitleLng2",new Boolean(Config.editCopySubTitleLng2).toString());
        conf = alterProperty(conf,"editCopyInfo",new Boolean(Config.editCopyInfo).toString());
        conf = alterProperty(conf,"editCopyInfoLng1",new Boolean(Config.editCopyInfoLng1).toString());
        conf = alterProperty(conf,"editCopyInfoLng2",new Boolean(Config.editCopyInfoLng2).toString());
        conf = alterProperty(conf,"editCopySite",new Boolean(Config.editCopySite).toString());
        conf = alterProperty(conf,"editCopyPhotographDate",new Boolean(Config.editCopyPhotographDate).toString());
        conf = alterProperty(conf,"editCopyPhotographer",new Boolean(Config.editCopyPhotographer).toString());
        conf = alterProperty(conf,"editCopyByline",new Boolean(Config.editCopyByline).toString());
        conf = alterProperty(conf,"editCopyKeywords",new Boolean(Config.editCopyKeywords).toString());
        conf = alterProperty(conf,"editCopyPeople",new Boolean(Config.editCopyPeople).toString());
        conf = alterProperty(conf,"editCopyOrientation",new Boolean(Config.editCopyOrientation).toString());
        conf = alterProperty(conf,"editCopyPerspective",new Boolean(Config.editCopyPerspective).toString());
        conf = alterProperty(conf,"editCopyMotive",new Boolean(Config.editCopyMotive).toString());
        conf = alterProperty(conf,"editCopyGesture",new Boolean(Config.editCopyGesture).toString());
        conf = alterProperty(conf,"editCopyNote",new Boolean(Config.editCopyNote).toString());
        conf = alterProperty(conf,"editCopyRestrictions",new Boolean(Config.editCopyRestrictions).toString());
        conf = alterProperty(conf,"editCopyFlag",new Boolean(Config.editCopyFlag).toString());

        conf = alterProperty(conf,"editCopySiteLng1",new Boolean(Config.editCopySiteLng1).toString());
        conf = alterProperty(conf,"editCopySiteLng2",new Boolean(Config.editCopySiteLng2).toString());
        conf = alterProperty(conf,"editCopyNoteLng1",new Boolean(Config.editCopyNoteLng1).toString());
        conf = alterProperty(conf,"editCopyNoteLng2",new Boolean(Config.editCopyNoteLng2).toString());
        conf = alterProperty(conf,"editCopyRestrictionsLng1",new Boolean(Config.editCopyRestrictionsLng1).toString());
        conf = alterProperty(conf,"editCopyRestrictionsLng2",new Boolean(Config.editCopyRestrictionsLng2).toString());

        conf = alterProperty(conf,"useContactForm",new Boolean(Config.useContactForm).toString());

        conf = alterProperty(conf,"searchPerEmail",Integer.toString(Config.searchPerEmail));
        conf = alterProperty(conf,"showFormOnEmptySearch",new Boolean(Config.showFormOnEmptySearch).toString());

        conf = alterProperty(conf,"useShoppingCart",new Boolean(Config.useShoppingCart).toString());
        conf = alterProperty(conf,"useLightbox",new Boolean(Config.useLightbox).toString());

        conf = alterProperty(conf,"mailNewPasswordMailSubject",Config.mailNewPasswordMailSubject);
        conf = alterProperty(conf,"mailNewPasswordMailBody",Config.mailNewPasswordMailBody);

        //AclController neu setzen
        AclController.setEnabled(true);

        conf = alterProperty(conf,"mailDownloadInfoMailSubject",Config.mailDownloadInfoMailSubject);
        conf = alterProperty(conf,"mailDownloadInfoMailBody",Config.mailDownloadInfoMailBody);

        conf = alterProperty(conf,"mailUploadInfoMailSubject",Config.mailUploadInfoMailSubject);
        conf = alterProperty(conf,"mailUploadInfoMailBody",Config.mailUploadInfoMailBody);
        conf = alterProperty(conf,"mailUploadInfoEnabled",new Boolean(Config.mailUploadInfoEnabled).toString());

        conf = alterProperty(conf,"useDownloadResolutions",new Boolean(Config.useDownloadResolutions).toString());
        conf = alterProperty(conf,"downloadResOrig",new Boolean(Config.downloadResOrig).toString());

        conf = alterProperty(conf,"showDownloadToVisitors",new Boolean(Config.showDownloadToVisitors).toString());
        conf = alterProperty(conf,"podcastEnabled", new Boolean(Config.podcastEnabled).toString());
        conf = alterProperty(conf,"complexPasswords", new Boolean(Config.complexPasswords).toString());

        conf = alterProperty(conf,"showCategoryTree", new Boolean(Config.showCategoryTree).toString());
        conf = alterProperty(conf,"defaultSecurityGroup", String.valueOf(Config.defaultSecurityGroup));
        conf = alterProperty(conf,"defaultRole", String.valueOf(Config.defaultRole));

        StringBuffer downloadRes = new StringBuffer();
        Iterator formats = Config.downloadRes.iterator();
        while (formats.hasNext()) {
            Format format = (Format)formats.next();
            if (format.width>0 && format.height>0) {
                //width=0 height=0 kennzeichen für original = nicht speichern
                downloadRes.append(format.getWidth()+"x"+format.getHeight());
                if (formats.hasNext()) { downloadRes.append(";"); }
            }
        }
        conf = alterProperty(conf,"downloadRes",downloadRes.toString());

        conf = alterProperty(conf,"showSendImage", new Boolean(Config.showSendImage).toString());
        conf = alterProperty(conf,"useCaptchaRegister", new Boolean(Config.useCaptchaRegister).toString());
        conf = alterProperty(conf,"useCaptchaPin", new Boolean(Config.useCaptchaPin).toString());
        conf = alterProperty(conf,"robotsAllow", new Boolean(Config.robotsAllow).toString());
        conf = alterProperty(conf,"categorySortByName", new Boolean(Config.categorySortByName).toString());
        conf = alterProperty(conf,"categorySort", new Integer(Config.categorySort).toString());
        conf = alterProperty(conf,"mediaHandling", new Integer(Config.mediaHandling).toString());

        try {
            ResourceBundleChanger.changeResourceBundles();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /*
        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            Config.importFactory = new MediaImportFactory();
        } else {
            Config.importFactory = new ImageImportFactory();
        }
        */
        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            //Filelist verwenden
            ThumbnailModuleController.setDefaultViewMode(ThumbnailModuleController.USE_FILELIST);
        } else {
            ThumbnailModuleController.setDefaultViewMode(ThumbnailModuleController.USE_THUMBNAIL);
        }

        conf = alterProperty(conf,"homeCategoryId", String.valueOf(Config.homeCategoryId));
        conf = alterProperty(conf,"homeCategoryAsRoot", new Boolean(Config.homeCategoryAsRoot).toString());

        conf = alterProperty(conf,"homeCategoryAutocreate", new Boolean(Config.homeCategoryAutocreate).toString());
        conf = alterProperty(conf,"autoimportFtp", new Boolean(Config.autoimportFtp).toString());

        conf = alterProperty(conf,"emailImportEnabled", new Boolean(Config.emailImportEnabled).toString());
        conf = alterProperty(conf,"emailImportHost", Config.emailImportHost);
        conf = alterProperty(conf,"emailImportUsername", Config.emailImportUsername);
        conf = alterProperty(conf,"emailImportPassword", Config.emailImportPassword);

        conf = alterProperty(conf,"itemCountPerPage", new Integer(Config.itemCountPerPage).toString());

        conf = alterProperty(conf,"customList1Lng1", Config.customList1Lng1);
        conf = alterProperty(conf,"customList1Lng2", Config.customList1Lng2);
        conf = alterProperty(conf,"customList2Lng1", Config.customList2Lng1);
        conf = alterProperty(conf,"customList2Lng2", Config.customList2Lng2);
        conf = alterProperty(conf,"customList3Lng1", Config.customList3Lng1);
        conf = alterProperty(conf,"customList3Lng2", Config.customList3Lng2);

        conf = alterProperty(conf,"customStr1", Config.customStr1);
        conf = alterProperty(conf,"customStr2", Config.customStr2);
        conf = alterProperty(conf,"customStr3", Config.customStr3);
        conf = alterProperty(conf,"customStr4", Config.customStr4);
        conf = alterProperty(conf,"customStr5", Config.customStr5);
        conf = alterProperty(conf,"customStr6", Config.customStr6);
        conf = alterProperty(conf,"customStr7", Config.customStr7);
        conf = alterProperty(conf,"customStr8", Config.customStr8);
        conf = alterProperty(conf,"customStr9", Config.customStr9);
        conf = alterProperty(conf,"customStr10", Config.customStr10);

        conf = alterProperty(conf,"customTemplate", Config.customTemplate);

        conf = alterProperty(conf,"useCaptchaSend", new Boolean(Config.useCaptchaSend).toString());
        conf = alterProperty(conf,"pinCodeKeyGen", new Integer(Config.pinCodeKeyGen).toString());

        conf = alterProperty(conf,"forbiddenDomains", Config.forbiddenDomains);

        conf = alterProperty(conf,"categoryDefaultViewOnRoot", new Integer(Config.categoryDefaultViewOnRoot).toString());

        conf = alterProperty(conf,"usersCanSendAttachments", new Boolean(Config.usersCanSendAttachments).toString());

        conf = alterProperty(conf,"ftpHost", Config.ftpHost);
        conf = alterProperty(conf,"ftpUser", Config.ftpUser);
        conf = alterProperty(conf,"ftpPassword", Config.ftpPassword);

        conf = alterProperty(conf,"wsUsersyncEnabled", new Boolean(Config.wsUsersyncEnabled).toString());
        conf = alterProperty(conf,"wsUsersyncUrl", Config.wsUsersyncUrl);
        conf = alterProperty(conf,"wsUsersyncUsername", Config.wsUsersyncUsername);
        conf = alterProperty(conf,"wsUsersyncPassword", Config.wsUsersyncPassword);
        conf = alterProperty(conf,"wsUsersyncGroupnameFilter", Config.wsUsersyncGroupnameFilter);
        conf = alterProperty(conf,"wsUsersyncTrustAllCerts", new Boolean(Config.wsUsersyncTrustAllCerts).toString());

        conf = alterProperty(conf,"streamEnabled", new Boolean(Config.streamEnabled).toString());
        conf = alterProperty(conf,"streamToVisitors", new Boolean(Config.streamToVisitors).toString());
        conf = alterProperty(conf,"streamConvertToKbitSek", new Integer(Config.streamConvertToKbitSek).toString());

        conf = alterProperty(conf,"webdavEnabled", new Boolean(Config.webdavEnabled).toString());
        conf = alterProperty(conf,"blankWhenFieldEmpty", new Boolean(Config.blankWhenFieldEmpty).toString());

        conf = alterProperty(conf,"userEmailAsUsername", new Boolean(Config.userEmailAsUsername).toString());
        conf = alterProperty(conf,"showUserCompanyFields", new Boolean(Config.showUserCompanyFields).toString());
        conf = alterProperty(conf,"showUserAddressFields", new Boolean(Config.showUserAddressFields).toString());
        conf = alterProperty(conf,"showUserTelFaxFields", new Boolean(Config.showUserTelFaxFields).toString());

        conf = alterProperty(conf,"allowRegister", new Boolean(Config.allowRegister).toString());

        conf = alterProperty(conf,"multiLang", new Boolean(Config.multiLang).toString());

        conf = alterProperty(conf,"advancedSearchEnabled", new Boolean(Config.advancedSearchEnabled).toString());
        conf = alterProperty(conf,"resetSecurityGroupWhenUserIsDisabled", new Boolean(Config.resetSecurityGroupWhenUserIsDisabled).toString());

        conf = alterProperty(conf,"onlyLoggedinUsers", new Boolean(Config.onlyLoggedinUsers).toString());
        conf = alterProperty(conf,"showSubCategoryInListView", new Boolean(Config.showSubCategoryInListView).toString());

        conf = alterProperty(conf,"autoImportEmailCat", new Integer(Config.autoImportEmailCat).toString());
        conf = alterProperty(conf,"autoImportFtpCat", new Integer(Config.autoImportFtpCat).toString());

        conf = alterProperty(conf,"dataView", new Boolean(Config.dataView).toString());

        conf = alterProperty(conf,"configParam", Config.configParam);

        conf = alterProperty(conf,"paymillKeyPublic", Config.paymillKeyPublic);
        conf = alterProperty(conf,"paymillKeyPrivate", Config.paymillKeyPrivate);
        conf = alterProperty(conf,"vatPercent", new Integer(Config.vatPercent).toString());
        conf = alterProperty(conf,"currency", Config.currency);

        conf = alterProperty(conf,"editCopyPrice", new Boolean(Config.editCopyPrice).toString());
        conf = alterProperty(conf,"editCopyLicValid", new Boolean(Config.editCopyLicValid).toString());

        conf = alterProperty(conf,"importPluginClass", StringUtils.join(Config.importPluginClass, "\n"));

        conf = alterProperty(conf,"splashPageData", Config.splashPageData);

        conf = alterProperty(conf,"smtpUseTls", new Boolean(Config.smtpUseTls).toString());
        conf = alterProperty(conf,"smtpUsername", Config.smtpUsername);
        conf = alterProperty(conf,"smtpPassword", Config.smtpPassword);
        conf = alterProperty(conf,"mailReceiverAdminEmail", Config.mailReceiverAdminEmail);
        conf = alterProperty(conf,"cssColorPrimaryHex", Config.cssColorPrimaryHex);
        conf = alterProperty(conf,"cssColorAHref", Config.cssColorAHref);

        //Vorschaubild Größe
        conf = alterProperty(conf,"imagesizePreview", new Integer(Config.imagesizePreview).toString());
        conf = alterProperty(conf,"imagesizeThumbnail", new Integer(Config.imagesizeThumbnail).toString());

        return conf;
    }

    private static String alterProperty(String conf, String key, String property) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        ConfigProperty configProperty = new ConfigProperty();
        try {

            String sProperty = (String)smc.queryForObject("getConfig",key);

            configProperty.setKey(key);
            configProperty.setProperty(property);
            if (sProperty==null) {
                if (property!=null) { //Nur wenn der Einstellungswert nicht null ist wird dieser Key angelegt
                    smc.insert("addConfig",configProperty);
                }
            } else {
                smc.update("saveConfig",configProperty);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conf;
    }

    private static void deleteConfig(String conf, String key) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        ConfigProperty configProperty = new ConfigProperty();
        try {

            configProperty.setKey(key);
            smc.delete("deleteConfig",configProperty);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt nur die Properties für die Datenbank zurück:
     * @return
     */
    public static Properties getDatabaseProperties() {

        Logger logger = Logger.getLogger(Config.class);
        logger.info("Database Settings: "+dbProperties.toString());
        return dbProperties;
    }

    public static void putDmsConfigToRequest(HttpServletRequest request) {

        if (Config.mediaHandling == Config.MEDIAHANDLING_ALLMEDIA) {
            request.setAttribute("isFiledb",true);
            request.setAttribute("isImagedb",false);
        } else {
            request.setAttribute("isImagedb",true);
            request.setAttribute("isFiledb",false);
        }

    }

    public static String getTempPath() {
        String path = Config.imageStorePath;
        if (!path.endsWith("/")) { path = path + "/"; }
        path = path + "tmp";
        return path;
    }

    public static String getTemplateArchivePath() {

        String path = Config.imageStorePath;
        if (!path.endsWith("/")) { path = path + "/"; }
        path = path + "template";
        return path;

    }

    /**
     * Plugins initialisieren
     */
    public static void initPlugins() {

        //PluginHandlerChain (ImportPlugin) initialisieren
        //Test: ImportPluginHandlerChain.getInstance().add(new EmptyImportPlugin());
        if (Config.importPluginClass != null) {
            System.out.println("Initialisiere ImportPluginHandler Instanz");
            ImportPluginHandlerChain.getInstance().init();
            for (String importPluginClass : Config.importPluginClass) {
                if (importPluginClass.length()>0 && !importPluginClass.startsWith("#")) {
                    try {
                        System.out.println("Registering ImportPluginHandler: "+importPluginClass);
                        String[] classAndParam = importPluginClass.split(" ");
                        String classname = classAndParam[0];
                        ImportPluginHandler plugin = (ImportPluginHandler)Class.forName(classname).newInstance();
                        if (classAndParam.length>1) { plugin.setParameter(classAndParam[1]); }
                        ImportPluginHandlerChain.getInstance().add(plugin);
                    } catch (InstantiationException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else {
                    if (importPluginClass.length()>0) {
                        System.out.println("ImportPluginHandler: wird ausgelassen: "+importPluginClass);
                    }
                }
            }
        }

    }
}
