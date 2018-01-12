package com.stumpner.mediadesk.web.template;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.ResourceBundleChanger;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Properties;
import java.io.*;

import com.stumpner.mediadesk.util.FileUtil;
import com.stumpner.mediadesk.web.mvc.common.GlobalRequestDataProvider;
import com.asual.lesscss.LessEngine;
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
 * User: franz.stumpner
 * Date: 14.04.2010
 * Time: 20:51:49
 * To change this template use File | Settings | File Templates.
 */
public class TemplateService {

    private boolean devOp = false; //DevOp Option ( wenn true = keine Komprimierung bei den css Files)

    public static List<String> customCssFiles = new ArrayList<String>();

    /**
     * Gibt eine Liste von Custom-Templates zurück die verfügbar sind
     * @return
     */
    public List<Template> getCustomTemplateList() {

        List<Template> list = new LinkedList<Template>();

        File templateArchivePath = new File(Config.getTemplateArchivePath());
        File[] templateDirectories = templateArchivePath.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (File dir : templateDirectories) {
            Template template = new Template();
            template.setName(dir.getName());
            //Dateien des Templates
            File[] templateFiles = dir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.isFile();
                }
            });

            List<File> fileList = new LinkedList<File>();
            for (File templateFile : templateFiles) {
                fileList.add(templateFile);
            }
            template.setFileList(fileList);

            list.add(template);
        }

        return list;
    }

    public File getCustomDir() {

        File customDir = new File(Config.webroot + "/WEB-INF/template/current");

        return customDir;
    }

    /**
     * Legt das gewünschte Custom-Template fest und kopiert die Template-Dateien in /template/custom/
     * @param templateName
     */
    public void setTemplate(String templateName) throws IOException, LessException {

        //EXTJS Basis Template
        File baseTemplateDir = getBaseTemplateDir(templateName);
        System.out.println("BaseTemplate: "+baseTemplateDir.getName());

        File customDir = getCustomDir();
        if (!customDir.exists()) { customDir.mkdirs(); }
        //Current Path leeren
        clearCurrentPath(customDir);
        //Standard-Template Dateien kopieren
        copyDir(baseTemplateDir,customDir);

        if (!isStandardTemplate(templateName)) {
            //Custom-Template Files kopieren
            System.out.println("Using Custom-Template (copy): "+templateName);
            File customTemplateDir = getCustomTemplateDir(templateName);
            copyDir(customTemplateDir, customDir);
            //Resource-Bundels mitkopieren/mergen (wenn es welche im Template-Pfad gibt z.b. messages_en.properties)
            try {
                ResourceBundleChanger.changeResourceBundles();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //CSS-Dateien unter /css/template/<templatename> verfügbar machen
            copyCssFiles(templateName);
            //compileLessFiles(templateName);
        } else {
            //Ein Standard-Template wurde benutzt
            System.out.println("Using Default-Template (copy)");
        }

        // Datei /css/mediadesk.css erstellen:
        compileDefaultLessFile(templateName);

        //cachefix
        GlobalRequestDataProvider.doCacheFix();
    }

    /**
     * Gibt das Basis Template (extjs oder bootrstrap) für das angegebene Template zurück
     * @param templateName
     * @return
     */
    private File getBaseTemplateDir(String templateName) {

        String extjsBaseTemplateDir = Config.webroot + "/WEB-INF/template/extjsbased";
        String bootstrapBaseTemplateDir = Config.webroot + "/WEB-INF/template/bootstrapbased";

        //Als Standard bzw. Fallback wird das extjs Base Template verwendet
        File baseTemplateDir = new File(extjsBaseTemplateDir);

        if (isStandardTemplate(templateName)) {
            if (templateName.equalsIgnoreCase("bootstrap")) {
                //BOOTSTRAP Basis Template
                baseTemplateDir = new File(bootstrapBaseTemplateDir);
            }
        } else {
            //template.config aus dem Custom Template Directory holen
            InputStream input = null;
            try {
                input = new FileInputStream(getCustomTemplateDir(templateName)+File.separator+"template.config");
                Properties prop = new Properties();
                prop.load(input);
                String baseTemplate = prop.getProperty("baseTemplate");
                if (baseTemplate!=null) {
                    if (baseTemplate.equalsIgnoreCase("bootstrap")) {
                        //Bootstrap als Basis Template verwenden
                        baseTemplateDir = new File(bootstrapBaseTemplateDir);
                    }
                }
            } catch (FileNotFoundException e) {
                //template.config nicht gefunden --> Standardmässig wird extjs (default Template) verwendet
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input!=null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return baseTemplateDir;
    }

    /**
     * Gibt true zurück wenn es sich um ein STandard-Template (Buildin) handelt
     * @param templateName
     * @return
     */
    public static boolean isStandardTemplate(String templateName) {


        if (templateName.equalsIgnoreCase("default") || templateName.equalsIgnoreCase("bootstrap") || templateName.equalsIgnoreCase("bootstrapbased")) {
            return true;
        } else {
            return false;
        }
    }

    private void copyCssFiles(String templateName) {
        File customTemplateDir = getCustomTemplateDir(templateName);
        File customCssPath = new File(Config.webroot + File.separator + "css" + File.separator + "template" + File.separator + templateName);
        if (!customCssPath.mkdirs()) {
            System.out.println("Fehler beim erstellen des custom CSS Verzeichnis: "+customCssPath.getAbsolutePath());
        }

        File[] cssSrcFiles = customTemplateDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.toUpperCase().endsWith("CSS")) { return true; }
                else { return false; }
            }
        });

        customCssFiles = new ArrayList<String>();
        for (File cssSrcFile : cssSrcFiles) {
            File cssDstFile = new File(customCssPath, cssSrcFile.getName());
            try {
                System.out.println("copy css file: "+cssSrcFile.getName());
                customCssFiles.add(cssSrcFile.getName());
                FileUtil.copyFile(cssSrcFile, cssDstFile);
            } catch (IOException e) {
                System.out.println("Fehler beim kopieren der Template CSS-Datei: "+cssDstFile);
            }
        }
    }

    private void compileLessFiles(String templateName) throws LessException {

        File customTemplateDir = getCustomTemplateDir(templateName);
        File customCssPath = new File(Config.webroot + File.separator + "css" + File.separator + "template" + File.separator + templateName);
        if (!customCssPath.mkdirs()) {
            System.out.println("Fehler beim erstellen des custom CSS Verzeichnis: "+customCssPath.getAbsolutePath());
        }

        File[] lessSrcFiles = customTemplateDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.toUpperCase().endsWith("LESS")) { return true; }
                else { return false; }
            }
        });

        List customLessFiles = new ArrayList<String>();
        for (File lessSrcFile : lessSrcFiles) {
            File lessDstFile = new File(customCssPath, lessSrcFile.getName());
            try {
                System.out.println("copy less file: "+lessSrcFile.getName());
                customLessFiles.add(lessSrcFile.getName());
                FileUtil.copyFile(lessSrcFile, lessDstFile);
                //compile
                LessEngine engine = new LessEngine();
                engine.compile(lessSrcFile,
                        new File(customCssPath, lessSrcFile.getName().replaceAll("\\.less", ".css")));
            } catch (IOException e) {
                System.out.println("Fehler beim kopieren der Template LESS-Datei: "+lessDstFile);
            }
        }
    }

    /**
     * Setzt folgende Less Datei zusammen und kompiliert sie nach /css/mediadesk.css
     * @param templateName
     */
    private void compileDefaultLessFile(String templateName) throws IOException, LessException {

        File customDir = getCustomDir();

        LessEngine engine = new LessEngine();
        File stdVarLess = new File(Config.webroot + "/css/less/var.less");
        File tplVarLess = new File(customDir, "var.less");
        File stdCssLess = new File(customDir, "mediadesk.less");
        //File stdCssLess = new File(Config.webroot + "/css/less/mediadesk.less");

        String stdVarLessStr = getTextFileContent(stdVarLess);
        String tplVarLessStr = getTextFileContent(tplVarLess);
        String stdCssLessStr = getTextFileContent(stdCssLess);

        String cssColor = "";
        if (!Config.cssColorPrimaryHex.isEmpty()) {
            cssColor = "\n/* cssColorPrimaryHex from CSS config */\n@md-colorprim: "+Config.cssColorPrimaryHex+";\n@md-colorprim-hover: "+Config.cssColorPrimaryHex+";\n";
            //System.out.println("cssColorPrimaryHexLessStr: "+cssColor);
        }

        if (!Config.cssColorAHref.isEmpty()) {
            cssColor = cssColor + "\n/* cssColorAHref from CSS config */\n@md-acolor: "+Config.cssColorAHref+";\n";
            //System.out.println("cssColorAHref: "+cssColor);
        }

        String all = "\n/* /css/less/var.less */\n"+stdVarLessStr+"\n/* Template var.less */\n"+tplVarLessStr+"\n"+cssColor+"\n/* Template mediadesk.less */\n"+stdCssLessStr;

        File outFile = new File(Config.webroot + "/css/mediadesk.css");
        File allLessFile = new File(Config.webroot + "/css/less/genmediadesk.less");
        setTextFileContent(allLessFile, all);
        //try {
            String outStr = engine.compile(all, null, !devOp); //letzten Parameter auf true stellen damit der Output komprimiert wird

            setTextFileContent(outFile, outStr);


            System.out.println(outFile.getAbsolutePath()+" generiert");
        //} catch (LessException e) {
        //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        //}
    }


    public File getCustomTemplateDir(String templateName) {
        return new File(Config.getTemplateArchivePath()+File.separator+templateName);
    }

    private void clearCurrentPath(File customDir) {
        File[] files = customDir.listFiles();
        for (File file : files) {

            file.delete();

        }
    }

    public File createTemplateFile(Template template, String filename) throws IOException {

        File file = new File(Config.getTemplateArchivePath() + File.separator + template.getName() + File.separator + filename);
        if (!file.exists()) {
            File defaultFile = new File(getBaseTemplateDir(template.getName()).getAbsolutePath() + File.separator + filename);
            //System.out.println("defaultFile: "+defaultFile.getAbsolutePath());
            //File defaultFile = new File(Config.webroot + "/WEB-INF/template/default/"+filename);
            if (defaultFile.exists()) {
                //Original-Content kopieren
                //System.out.println("Original-File kopieren...");
                FileUtil.copyFile(defaultFile, file);
            } else {
                //System.out.println("File erstellen...");
                setTemplateFileContent(null, file, "");
            }
        }
        return file;
    }

    public String getTemplateFileContent(Template template, File file) throws IOException {

        /*
        String line = null;
        StringBuffer content = new StringBuffer();
        BufferedReader in = new BufferedReader(new FileReader(file));
        while ((line = in.readLine()) != null) {
            content.append(line+"\n");
        }
        in.close();

        return content.toString();
        */
          final StringBuilder text = new StringBuilder();
          final char[] buffer = new char[0xFFFF];

          File f = file;
          InputStreamReader reader = new InputStreamReader(new FileInputStream(f), "UTF-8");
          try {
            int len = 0;
            while ((len = reader.read(buffer)) != -1) {
              text.append(buffer, 0, len);
            }
          } finally {
            reader.close();
          }
          return text.toString().trim();

    }

    private String getTextFileContent(File file) throws IOException {

          final StringBuilder text = new StringBuilder();
          final char[] buffer = new char[0xFFFF];

          File f = file;
          InputStreamReader reader = new InputStreamReader(new FileInputStream(f), "UTF-8");
          try {
            int len = 0;
            while ((len = reader.read(buffer)) != -1) {
              text.append(buffer, 0, len);
            }
          } finally {
            reader.close();
          }
          return text.toString().trim();

    }

    private void setTextFileContent(File file, String content) throws IOException {

          File f = file;
          f.createNewFile();
          OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
          try {
            writer.write(content);
            writer.flush();
          } finally {
            writer.close();
          }

    }

    public void deleteTemplateFile(Template template, File file) throws IOException {

        file.delete();
    }

    public void setTemplateFileContent(Template template, File file, String content) throws IOException {

        /*
        Writer writer = new FileWriter(file);
        writer.write(content);
        writer.close();
        */
          File f = file;
          f.createNewFile();
          OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
          try {
            writer.write(content);
            writer.flush();
          } finally {
            writer.close();
          }
    }

    private void copyDir(File from, File to) throws IOException {

                System.out.println("copyDir: "+from+" --> "+to);

        File[] files = from.listFiles();
        for (File file : files) {
            FileUtil.copyFile(file, new File(to , file.getName()));
        }

    }

    public void setDevOp(boolean devOp) {
        this.devOp = devOp;
    }
}
