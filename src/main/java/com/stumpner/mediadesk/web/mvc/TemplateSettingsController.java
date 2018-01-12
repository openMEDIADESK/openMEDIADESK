package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.TemplateSettings;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import com.stumpner.mediadesk.web.template.TemplateService;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
 * Date: 27.12.2005
 * Time: 11:36:54
 * To change this template use File | Settings | File Templates.
 */
public class TemplateSettingsController extends SimpleFormControllerMd {

    public TemplateSettingsController() {

        this.setCommandClass(TemplateSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        TemplateSettings ts = new TemplateSettings();
        ts.setTemplate(Config.customTemplate);

        return ts;
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        TemplateService templateService = new TemplateService();
        httpServletRequest.setAttribute("customTemplateList",templateService.getCustomTemplateList());

        boolean canChangeToNewDesign = false;

        if (Config.customTemplate.equalsIgnoreCase("default") && Config.homeCategoryId==-1) {
            canChangeToNewDesign = true;
        }

        httpServletRequest.setAttribute("newDesignCheckTemplate", Config.customTemplate.equalsIgnoreCase("default"));
        httpServletRequest.setAttribute("newDesignCheckMandant", Config.homeCategoryId==-1);

        httpServletRequest.setAttribute("canChangeToNewDesign",canChangeToNewDesign);

        this.setContentTemplateFile("settings_templates.jsp", httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        TemplateSettings ts = (TemplateSettings)o;

        if (httpServletRequest.getParameter("new")!=null && !"".equalsIgnoreCase(httpServletRequest.getParameter("name"))) {

            String name = httpServletRequest.getParameter("name");
            name = name.replaceAll(" ","");
            //Neues Template erstellen
            File templatePath = new File(Config.getTemplateArchivePath() + File.separator + name);
            if (!templatePath.exists()) {
                templatePath.mkdirs();
                if (httpServletRequest.getParameter("baseTemplate")!=null) {
                    Properties prop = new Properties();
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(Config.getTemplateArchivePath() + File.separator + name + File.separator + "template.config");
                        prop.setProperty("baseTemplate", httpServletRequest.getParameter("baseTemplate"));
                        prop.store(output,null);
                    } catch (IOException io) {
		                io.printStackTrace();
	                } finally {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }

            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("settemplates")
            );

            return null;

        } else {

            if (httpServletRequest.getParameter("edit")!=null) {

                if (httpServletRequest.getParameter("edit").equalsIgnoreCase("default")) {
                    //default template kann nicht bearbeitet werden
                    httpServletResponse.sendRedirect(
                            httpServletResponse.encodeRedirectURL("settemplates")
                    );
                    return null;
                }

                //Template bearbeiten
                httpServletResponse.sendRedirect(
                        httpServletResponse.encodeRedirectURL("settemplateedit?name="+ts.getTemplate())
                );
                return null;

            } else {

                //Template auswählen

                TemplateService templateService = new TemplateService();

                try {
                    templateService.setTemplate(ts.getTemplate());
                    Config.customTemplate = ts.getTemplate();
                    Config.saveConfiguration();

                    httpServletResponse.sendRedirect(
                            httpServletResponse.encodeRedirectURL("settings")
                    );
                    return null;

                } catch (LessException lessException) {
                    System.out.println("filename "+lessException.getFilename());
                    System.out.println("message "+lessException.getMessage());
                    System.out.println("column "+lessException.getColumn());
                    System.out.println("line "+lessException.getLine());
                    System.out.println("type "+lessException.getType());
                    String errorMessage = "";
                    /*
                    for (String extractLine : lessException.getExtract()) {
                        System.out.println("line "+extractLine);
                        errorMessage = errorMessage + extractLine + "\n";
                    } */

                    httpServletRequest.setAttribute("errorMessage",lessException.getMessage());

                    return super.onSubmit(httpServletRequest, httpServletResponse, o, e);

                }
            }
        }

        //return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }


}
