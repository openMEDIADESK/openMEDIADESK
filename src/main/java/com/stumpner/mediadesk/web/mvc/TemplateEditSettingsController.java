package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import com.stumpner.mediadesk.web.template.TemplateService;
import com.stumpner.mediadesk.web.template.Template;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.TemplateEdit;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.File;
import java.net.URLEncoder;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
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
 * Date: 14.11.2010
 * Time: 19:30:24
 * To change this template use File | Settings | File Templates.
 */
public class TemplateEditSettingsController extends SimpleFormControllerMd {

    public TemplateEditSettingsController() {

        this.setCommandClass(TemplateEdit.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        if (request.getParameter("name")!=null) {
            String name = request.getParameter("name");
            TemplateService templateService = new TemplateService();
            List<Template> templateList = templateService.getCustomTemplateList();
            Template template = null;
            for (Template templateTmp : templateList) {
                if (templateTmp.getName().equalsIgnoreCase(name)) {
                    template = templateTmp;
                }
            }
            TemplateEdit templateEdit = new TemplateEdit();
            templateEdit.setTemplate(template);
            if (request.getParameter("file")!=null) {
                File file = null;
                for (File fileTmp : template.getFileList()) {
                    if (fileTmp.getName().equalsIgnoreCase(request.getParameter("file"))) {
                        file = fileTmp;
                    }
                }
                String content = "";
                if (file!=null) {
                    //Datei existiert
                    content = templateService.getTemplateFileContent(null,file);
                } else {
                    //Neue Datei
                    file = templateService.createTemplateFile(template, request.getParameter("file"));
                    content = templateService.getTemplateFileContent(null,file);
                }
                templateEdit.setFile(file);
                templateEdit.setContents(content);
            }
            //Nochmals alles einlesen um auch das eventuell neu  angelegte file auflisten zu k�nnen
            templateList = templateService.getCustomTemplateList();
            template = null;
            for (Template templateTmp : templateList) {
                if (templateTmp.getName().equalsIgnoreCase(name)) {
                    template = templateTmp;
                }
            }
            templateEdit.setTemplate(template);
            return templateEdit;
        } else {
            throw new Exception("Kein Template angegeben");
        }
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (httpServletRequest.getParameter("errorMessage")!=null) {
            System.out.println("setting Error Message Attribute");
            httpServletRequest.setAttribute("errorMessage",httpServletRequest.getParameter("errorMessage"));
        }

        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        TemplateEdit templateEdit = (TemplateEdit)o;
        TemplateService templateService = new TemplateService();
        templateService.setTemplateFileContent(null,templateEdit.getFile(),templateEdit.getContents());
        if (httpServletRequest.getParameter("delete")!=null) {
            templateService.deleteTemplateFile(null,templateEdit.getFile());
        }

        if (Config.customTemplate.equalsIgnoreCase(templateEdit.getTemplate().getName())) {
            //wenn es sich um das aktive template handelt, dann sofort verf�rbar machen
            try {
                templateService.setDevOp(true); //Damit der CSS Output nicht komprimiert wird
                templateService.setTemplate(Config.customTemplate);
                System.out.println("template sofort verfuegbar gemacht, nicht komprimiert");
            } catch (LessException lessException) {
                System.out.println("filename "+lessException.getFilename());
                System.out.println("message "+lessException.getMessage());
                System.out.println("column "+lessException.getColumn());
                System.out.println("line "+lessException.getLine());
                System.out.println("type "+lessException.getType());

                if (getSuccessView().isEmpty()) {
                    httpServletResponse.sendRedirect(
                            httpServletResponse.encodeRedirectURL("settemplateedit?name="+templateEdit.getTemplate().getName()+"&file="+templateEdit.getFile().getName()+"&errorMessage="+ URLEncoder.encode(lessException.getMessage()))
                    );
                    System.out.println("after redirecting error");
                    return null;
                } else {
                    httpServletRequest.setAttribute("errorMessage",lessException.getMessage());
                    return super.onSubmit(httpServletRequest,httpServletResponse,o,e);
                }

            }
        }

        System.out.println("redirecting after save");
        System.out.println("FormView = "+getFormView());
        System.out.println("SuccessView = "+getSuccessView());

        if (getSuccessView().isEmpty()) {
            System.out.println("redirect");
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("settemplateedit?name="+templateEdit.getTemplate().getName())
            );
            return null;
        } else {
            System.out.println("show successview");
            return super.onSubmit(httpServletRequest,httpServletResponse,o,e);
        }



    }
}
