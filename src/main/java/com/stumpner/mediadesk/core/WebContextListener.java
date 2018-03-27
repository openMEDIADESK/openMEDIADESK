package com.stumpner.mediadesk.core;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.lic.LicenceChecker;
import com.stumpner.mediadesk.util.IniFile;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.Authenticator;
import com.stumpner.mediadesk.media.MimeCssMap;
import com.stumpner.mediadesk.web.mvc.AclEditController;
import com.stumpner.mediadesk.image.util.ImageImport;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.*;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.security.acl.AclNotFoundException;
import java.beans.XMLDecoder;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import net.stumpner.util.mailproxy.MailProxy;
import com.stumpner.sql.installer.SqlInstallerBase;
import com.stumpner.mediadesk.web.template.TemplateService;
import com.asual.lesscss.LessException;
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
 * Time: 22:48:00
 * To change this template use File | Settings | File Templates.
 */
public class WebContextListener implements ServletContextListener {

    /**
     * Config-File wird entweder in /WEB-INF Ordner oder im Parent-Ordner gesucht.
     * Der Vorteil von Parent Ordner ist, dass das War-File entpackt werden kann und außerhalb, eine ebene höher das Config File bestehen bleibt
     */
    public static String configFilename = "mediaDESK.conf";
    public static String configFile = "";

    Timer licenceChecker = new Timer();
    Timer cronServiceTimer = new Timer();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static ScheduledFuture nightly = null;
    public static ScheduledFuture daily = null;

    public void contextInitialized(ServletContextEvent event) {

        Config.webroot = new File(event.getServletContext().getRealPath(""));
        System.out.println("Starting mediaDESK Version: "+Config.versionNumbner+" ["+Config.versionDate+"], "+Config.instanceName);
        //Todo: Manifest Version in die Config.version schreiben
        System.out.println("Manifest Version: "+WebContextListener.class.getPackage().getImplementationVersion());
        System.out.println("Webroot: "+Config.webroot);
        Logger logger = Logger.getLogger(WebContextListener.class);
        logger.info("Starting mediaDESK Version: "+Config.versionNumbner+" ["+Config.versionDate+"], "+Config.instanceName);

        ServletContext sc = event.getServletContext();
        ServletRegistration sr = sc.addServlet("IndexDispatcher","org.springframework.web.servlet.DispatcherServlet");
        sr.addMapping("/index/*");

        //returns array of all locales
        Locale locales[] = SimpleDateFormat.getAvailableLocales();

        //iterate through each locale and print
        // locale code, display name and country
        for (int i = 0; i < locales.length; i++) {

            //sr.setInitParameter()
            if (locales[i].toString().length()>0) {
                sr.addMapping("/"+locales[i].toString()+"/*");
                //System.out.printf("Adding ServletMapping (Locale): %10s - %s, %s \n" , locales[i].toString(),
                //    locales[i].getDisplayName(), locales[i].getDisplayCountry());
            }

        }

        //Property Retriever:
        Properties prop = System.getProperties();
        Set keySet = prop.keySet();
        Iterator keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            //System.out.println("KEY: "+key+" --> "+System.getProperty(key));
        }

        System.setProperty("java.awt.headless","true");

        //Prüfen ob es ein configFile gibt...
        System.out.println("Checking Config File: "+event.getServletContext().getRealPath("/WEB-INF/"+configFilename));
        File fileConfig = new File(event.getServletContext().getRealPath("/WEB-INF/"+configFilename));
        configFile = fileConfig.getAbsolutePath();
        if (!fileConfig.exists()) {

            //Wenn es die Datei nicht gibt im parent suchen
            //Check if there's a Config File in the Parent Directory (ausserhalb der Webapp für besseres Deployment/Update)
            String realPath = event.getServletContext().getRealPath("");
            File webroot = new File(realPath);
            File webrootParent = webroot.getParentFile();
            File confInWebrootParent = new File(webrootParent, configFilename);
            fileConfig = confInWebrootParent;

            configFile = confInWebrootParent.getAbsolutePath();

            System.out.println("Checking Config File: "+confInWebrootParent.getAbsolutePath());

        }
        if (fileConfig.exists()) {

            //Konfiguration Laden...
            doAndReloadConfiguration(event.getServletContext());
            
        } else {
            //Initiale Konfiguration erledigen...
            //todo:
            System.out.println("Do initial mediaDESK-Configuration....");

        }

        //Styles laden:
        //logger.info("Loading Styles...["+styleConfigFile+"]");
        //IniFile styleFile = new IniFile();
        //styleFile.open(event.getServletContext().getRealPath(styleConfigFile));
        //StyleConfig.initConfiguration(styleFile);

        //ApplicationContext
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        BeanFactory factory = (BeanFactory)appContext;
        
        //Object messageSource = factory.getBean("messageSource");

        //Prüfen ob ein upgrade (der Datenbank durchgeführt werden muss
        File updateSqlFile = new File(event.getServletContext().getRealPath("/WEB-INF/update.sql"));
        if (updateSqlFile.exists() && Config.isConfigured) {
            upgradeSuSIDE(event.getServletContext());
            //Konfiguration Laden...
            doAndReloadConfiguration(event.getServletContext());
        }

        if (Config.isConfigured) {
            //Verweiste Datensätze aus Pinpic-Holder löschen:
            PinpicService pinPicService = new PinpicService();
            pinPicService.deleteOrphanedHoler();
            //Sonderzeichen aus Namen in Medienobjekten und Kategorien entfernen
            ImageVersionService mediaService = new ImageVersionService();
            mediaService.normalizeNames();
            //Alte TMP-Dateien im Repository-Temp Verzeichnis löschen
            File tmpPath = new File(Config.getTempPath());
            for (File file : tmpPath.listFiles()) {
                if (file.isFile()) {file.delete();}
            }
        }

        event.getServletContext().setAttribute("home",Config.redirectStartPage);
        event.getServletContext().setAttribute("redirectStartPage",Config.redirectStartPage);
        //MimeCSSMap in den ApplicationContext Stellen
        event.getServletContext().setAttribute("mimeCssMap",new MimeCssMap());

        //Wenn eine Externe Startseite auf Home geschalten wird
        if (Config.upstreamingStartpageUrl.length()>0) {
            event.getServletContext().setAttribute("home",Config.upstreamingStartpageUrl);
        }

        MailProxy.getInstance().setPostmaster("franz@stumpner.com","mailgate.stumpner.net");

        //Logging:
        PropertyConfigurator.configure(event.getServletContext().getRealPath("/")+"WEB-INF/classes/log4j.properties");
        Logger log = Logger.getLogger(WebContextListener.class.toString());

        log.info("LOGGING HAS BEEN CONFIGURED by franzi");
        //System.out.println("SpringVersion="+ SpringVersion.getVersion());

        //Config.customTemplate = "bootstrap"; //!!!!! - immer nach hochfahren bootstrap template verwenden

        //Verwendetes Template hochfahren (in current kopieren)
            System.out.println("Current Template: "+Config.customTemplate);
            TemplateService templateService = new TemplateService();
            try {
                templateService.setTemplate(Config.customTemplate);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                System.out.println("ERROR LOADING TEMPLATE: Fallback to default Template");
            } catch (LessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                System.out.println("Less Exception in TEMPLATE: Fallback to default Template");
            }

        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void doAndReloadConfiguration(ServletContext servletContext) {

        //Konfiguration laden:
        Logger logger = Logger.getLogger(WebContextListener.class);
        logger.info("Loading Configuration...["+configFile+"]");
        IniFile iniFile = new IniFile();
        iniFile.open(configFile);
        Config.iniFilename = configFile;
        Config.initConfiguration(iniFile);

        //SqlMap
        InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/SqlMapConfig.xml"));
        AppSqlMap.initialize(isr,Config.getDatabaseProperties());

        //Configuration aus DB Laden (und mit aktueller überschreiben)
        System.out.println("Loading Configuration from DB");
        Config.loadConfigurationFromDB();

        //checking for root user
        //if it does not exist, create one with password root
        UserService userService = new UserService();
        try {
            userService.getByName("admin");
        } catch (ObjectNotFoundException e) {
            //user nicht gefunden, erstellen...
            this.createInitialUser();
            logger.info("Create Initial ADMIN-User: "+Config.versionNumbner+" ["+Config.versionDate+"], "+Config.instanceName);

        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        licenceChecker.scheduleAtFixedRate(new LicenceChecker(),10000,3600000);
        cronServiceTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                CronService.minutely();
            }
        },10000,60000);

        scheduler.shutdown();
        scheduler = Executors.newScheduledThreadPool(1);

        //Timer für Tägliche Tasks und Benachrichtigungen:
        Calendar nightlyTimerStart = GregorianCalendar.getInstance();
        nightlyTimerStart.set(Calendar.HOUR_OF_DAY, 2);  //Täglich um 2 Uhr
        nightlyTimerStart.set(Calendar.MINUTE, 0);
        nightly = scheduler.scheduleAtFixedRate(new Runnable() {

                public void run() {
                    System.out.println("Starting Nightly Cron "+(new Date()));
                    CronService.nightly();
                }

        },getDiffToTime(nightlyTimerStart), TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);

        //Timer für Tägliche Tasks und Benachrichtigungen:
        Calendar dailyTimerStart = GregorianCalendar.getInstance();
        dailyTimerStart.set(Calendar.HOUR_OF_DAY, 9);  //Täglich um 9 Uhr
        dailyTimerStart.set(Calendar.MINUTE, 0);
        daily = scheduler.scheduleAtFixedRate(new Runnable() {

                public void run() {
                    System.out.println("Starting Daily Cron "+(new Date()));
                    if (Config.configParam.contains("-MAIL")) {
                        System.out.println("Sending Ping-Mail");
                        CronService.pingMail();
                    }
                }

        },getDiffToTime(dailyTimerStart), TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);


        DatabaseService.setTriggerStage1(true);

        Config.initPlugins();

        //XML für Splash Page Data laden:
        if (!Config.splashPageData.isEmpty()) {
            //System.out.println("Config.splashPageData: "+Config.splashPageData);
            XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(Config.splashPageData.getBytes()));
            Map<String, String> splashPageValueMap = (Map<String, String>) xmlDecoder.readObject();
            servletContext.setAttribute("splashPageValueMap", splashPageValueMap);
        }

        Config.isConfigured = true;

    }

    private long getDiffToTime(Calendar timeStart) {

        long diff = timeStart.getTime().getTime() - (new Date()).getTime();
        if (diff<0) {
            timeStart.add(Calendar.DAY_OF_YEAR, 1);
            diff = timeStart.getTime().getTime() - (new Date()).getTime();
        }

        return diff;

    }

    /**
     * Installiert die SuSIDE Instanz und legt die Configuration an
     * @param conf
     */
    public void setupMediaDESK(Properties conf, ServletContext servletContext) throws IOException {

        // Konfiguration schreiben:
        File cFile = new File(WebContextListener.configFile);
        FileWriter confWriter = new FileWriter(cFile);

        //Default Watermark in Datastore kopieren
        Config.watermarkHorizontal = conf.get("imageStorePath")+"wmh.gif";
        Config.watermarkVertical = conf.get("imageStorePath")+"wmv.gif";
        File wmhSrc = new File(servletContext.getRealPath("/WEB-INF/wmh.gif"));
        File wmvSrc = new File(servletContext.getRealPath("/WEB-INF/wmv.gif"));
        File wmhDest = new File(Config.watermarkHorizontal);
        if (wmhDest.getParentFile().mkdirs()) { System.out.println("Directory for Watermark created"); }
        File wmvDest = new File(Config.watermarkVertical);
        try {
            ImageImport.copyFile(wmhSrc,wmhDest);
            ImageImport.copyFile(wmvSrc,wmvDest);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new IOException("Failed to copy watermark-file to imagestore-path");
        }
        conf.put("watermarkHorizontal",Config.watermarkHorizontal);
        conf.put("watermarkVertical",Config.watermarkVertical);

        conf.store(confWriter,"mediaDESK Config-File");
        confWriter.close();
        //Update.sql entfernen
        File updateSqlFile = new File(servletContext.getRealPath("/WEB-INF/update.sql"));
        if (updateSqlFile.exists()) {
            updateSqlFile.delete();
        }
        IniFile iniFile = new IniFile();
        iniFile.open(WebContextListener.configFile);
        Config.initConfiguration(iniFile);

        try {
            InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/SqlMapConfig.xml"));
            AppSqlMap.initialize(isr,Config.getDatabaseProperties());
            //AppSqlMap.getSqlMapInstance().getDataSource().getConnection()
            Connection connection = AppSqlMap.getSqlMapInstance().getDataSource().getConnection();
            SqlInstallerBase sqlInstaller =
                    new SqlInstallerBase(
                            new File(servletContext.getRealPath("/WEB-INF/create.sql")),
                            connection);

            sqlInstaller.run();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        doAndReloadConfiguration(servletContext);

    }

    /**
     * Aktualisiert das SQL bzw. die Datenbankstruktur für die neue Version (aus /WEB-INF/update.sql) und
     * löscht anschließend diese Datei
     */
    private void upgradeSuSIDE(ServletContext servletContext) {

        System.out.println("["+Config.instanceName+"]: Found /WEB-INF/update.sql");
        System.out.println("["+Config.instanceName+"]: Upgrading Instance "+Config.instanceName);

        File updateSqlFile = new File(servletContext.getRealPath("/WEB-INF/update.sql"));

        try {
            InputStreamReader isr = new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/SqlMapConfig.xml"));
            AppSqlMap.initialize(isr,Config.getDatabaseProperties());
            //AppSqlMap.getSqlMapInstance().getDataSource().getConnection()
            Connection connection = AppSqlMap.getSqlMapInstance().getDataSource().getConnection();
            SqlInstallerBase sqlInstaller =
                    new SqlInstallerBase(
                            new File(servletContext.getRealPath("/WEB-INF/update.sql")),
                            connection);

            sqlInstaller.run();

            if (updateSqlFile.exists()) { updateSqlFile.delete(); }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //renewCategoryPublicProtectedStatusRekursiv(0);
        //System.out.println("renewFolderPublicProtectedStatus done");
    }

    public static void renewCategoryPublicProtectedStatusRekursiv(int categoryId) {

        System.out.println("["+Config.instanceName+"]: renewFolderPublicProtectedStatus "+categoryId);

        FolderService cService = new FolderService();
        List<FolderMultiLang> l = cService.getFolderList(categoryId);
        for (FolderMultiLang c : l) {
            try {
                AclEditController.renewFolderPublicProtectedStatus(c);
                renewCategoryPublicProtectedStatusRekursiv(c.getCategoryId());
            } catch (AclNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    private void createInitialUser() {

        Logger logger = Logger.getLogger(WebContextListener.class);
        logger.debug("Create Initial User [admin/admin]");

        UserService userService = new UserService();
            User user = new User();
            user.setUserName("admin");
            user.setEmail("office@openmediadesk.org");
            user.setEnabled(true);
            user.setRole(User.ROLE_ADMIN);
            try {
                userService.add(user);
                Authenticator auth = new Authenticator();
                auth.setPassword("admin","admin");
            } catch (IOServiceException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }  catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


    }

    public void contextDestroyed(ServletContextEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.

        System.out.println("mediaDESK Instance "+Config.instanceName+" goes down...");
        licenceChecker.cancel();
        cronServiceTimer.cancel();
        scheduler.shutdownNow();
        System.out.println("...ok!");
    }
}
