package com.stumpner.mediadesk.web;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.*;
import java.io.*;

import com.stumpner.mediadesk.util.FileUtil;
import com.stumpner.mediadesk.web.template.TemplateService;
import com.stumpner.mediadesk.core.Config;

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
 * Diese statische Klasse ändert die Resource Bundles. Benötigt wird das  beim Umschalten
 * der Medienverwaltung zwischen Bilddatenbank / Mediendatenbank.
 * Da bei der Bilddatenbank andere (Bild-spezifische) Texte verwendet werden sind bei einer Umstellung
 * die Resource-Bundles umzukopieren was von dieser Klasse gemacht wird
 *
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 17.04.2009
 * Time: 04:45:18
 */
public class ResourceBundleChanger extends ResourceBundleMessageSource {

    private static String RB_IMAGE_ARCHIV = "/WEB-INF/messages/image";
    private static String RB_MEDIA_ARCHIV = "/WEB-INF/messages/media";
    private static String BASENAME = "/WEB-INF/classes"; //Pfad für die Resource-Bundles

    /**
     * Abhängig von Cofig.mediaHandler
     */
    public static void changeResourceBundles() throws Exception {

        if (Config.mediaHandling==Config.MEDIAHANDLING_IMAGEONLY) {
            copyBundlesFromTo(
                    Config.webroot.getAbsolutePath()+
                    RB_IMAGE_ARCHIV,
                    Config.webroot.getAbsolutePath()+
                    BASENAME);
        } else {
            copyBundlesFromTo(
                    Config.webroot.getAbsolutePath()+
                    RB_MEDIA_ARCHIV,
                    Config.webroot.getAbsolutePath()+
                    BASENAME);
        }

        System.out.println("custom Template: "+Config.customTemplate);

        if (!TemplateService.isStandardTemplate(Config.customTemplate)) {
            //prüfen ob messages_<LNG> im Template Pfad liegen, wenn ja mergen
            TemplateService templateService = new TemplateService();
            File customTemplateDir = templateService.getCustomTemplateDir(Config.customTemplate);

            File[] files = customTemplateDir.listFiles();
            for (File templateFile : files) {
                if (templateFile.getName().startsWith("messages") && templateFile.getName().endsWith(".properties")) {
                    //Messages File
                    String messageFileString = Config.webroot.getAbsolutePath()+BASENAME+File.separator+templateFile.getName();
                    File messageFile = new File(messageFileString);
                    System.out.println("ResourceBundles: merging template resource bundle file: "+templateFile.getName());

                    //Original öffnen
                    Properties propsOriginal = new Properties();
                    if (messageFile.exists()) {
                        InputStreamReader inOriginal = new InputStreamReader(
                                new FileInputStream(messageFileString)
                                , "UTF-8");
                        propsOriginal.load(inOriginal);
                        inOriginal.close();
                    }

                    //Template Message öffnen
                    Properties propsTemplate = new Properties();
                    InputStreamReader inTemplate = new InputStreamReader(
                            new FileInputStream(templateFile)
                            , "UTF-8");
                    propsTemplate.load(inTemplate);
                    inTemplate.close();

                    for (String propertyName : propsTemplate.stringPropertyNames()) {
                        propsOriginal.setProperty(propertyName,
                                propsTemplate.getProperty(propertyName));
                    }

                    OutputStreamWriter outOriginal = new OutputStreamWriter(
                            new FileOutputStream(messageFileString)
                            , "UTF-8"
                    );
                    propsOriginal.store(outOriginal, "template messages file");
                    outOriginal.close();
                }
            }
        }

    }

    private static void copyBundlesFromTo(String from, String to) throws Exception {

        File srcDirectory = new File(from);
        File[] srcFiles = srcDirectory.listFiles();
        //System.out.println("change resource bundles from "+from+" to "+to);
        for (File srcFile : srcFiles) {
            if (srcFile.isFile()) {
                //System.out.println("copy: "+srcFile.getAbsolutePath()+" -> "+to);
                FileUtil.copyFile(srcFile,new File(to+"/"+srcFile.getName()));
            }
        }

    }

}
