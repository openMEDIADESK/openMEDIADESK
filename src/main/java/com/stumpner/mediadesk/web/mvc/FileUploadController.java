package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.util.WebFileUploadBean;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.sites.MessageSite;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Date: 24.01.2007
 * Time: 16:39:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class FileUploadController extends SimpleFormControllerMd {

    final public static int INPUT_TYPE_BOOL1 = 11;
    final public static int INPUT_TYPE_BOOL2 = 12;

    public FileUploadController() {

        this.setCommandClass(WebFileUploadBean.class);
        this.setSessionForm(true);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_EDITOR;

    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException, Map map) throws Exception {

        this.setContentTemplateFile("fileupload.jsp",httpServletRequest);
        if (map==null) map = new HashMap();
        map.put("actionUrl",getActionUrl(httpServletRequest));
        map.put("headline",getTextHeadline());
        map.put("subheadline",getTextSubheadline());
        map.put("info",getTextInfo());
        map.put("boolData1",defineInput(INPUT_TYPE_BOOL1));
        map.put("boolData2",defineInput(INPUT_TYPE_BOOL2));

        return super.showForm(httpServletRequest, httpServletResponse, bindException, map);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Diese Methode muss überschrieben werden wenn es Input types geben soll
     * @param type
     * @return message-resource die für dieses Settings ausgegeben werden soll
     */
    protected String defineInput(int type) {
        return null;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        Logger logger = Logger.getLogger(getClass());

        //todo: klasse messageSite überarbeiten
        MessageSite messageSite = new MessageSite(this,model,httpServletRequest);

        messageSite.setHeadline(getTextHeadline());
        messageSite.setSubHeadline(getTextSubheadline());
        messageSite.setText(getTextSuccess());
        messageSite.setNextUrl(getNextUrl());
        messageSite.setHtmlCode(getHtmlCode());

        httpServletRequest.setAttribute("headline",getTextHeadline());
        httpServletRequest.setAttribute("subheadline",getTextSubheadline());
        httpServletRequest.setAttribute("text",getTextSuccess());
        httpServletRequest.setAttribute("info",getTextSuccess());
        httpServletRequest.setAttribute("nextUrl",getNextUrl());
        httpServletRequest.setAttribute("redirectTo",getNextUrl());
        httpServletRequest.setAttribute("htmlCode",getHtmlCode());
        httpServletRequest.setAttribute("subheadlineArgs",new String[] {""});

        httpServletRequest.setAttribute("model",model);

        return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onUploadSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException, WebFileUploadBean upload, User user) {

        try {
            if (upload.getFile().getOriginalFilename().length()!=0) doFileUpload(upload,user);
        } catch (IOException err) {
            bindException.reject("imageimport.sizeexceed","Maximale Bildgroesse ueberschritten");
        }

    }


    /**
     * Gibt die URL zurück an die das Upload per Post Request durchgeführt wird
     * @param request
     * @return
     */
    protected String getActionUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }

    protected void doFileUpload(WebFileUploadBean upload, User user) throws IOException {

        String destinationPath = getDestinationPath(
                upload.getFile().getOriginalFilename(),upload);
        writeFile(upload.getFile(),destinationPath);

        File file = new File(destinationPath);
        doAfterFileUpload(file);
    }

    protected void writeFile(MultipartFile file, String filename) throws IOException {

        byte[] uploadContent = file.getBytes();
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(filename));
        bos.write(uploadContent);
        bos.flush();
        bos.close();

    }

    protected void doAfterFileUpload(File file) {
    }

    protected String getDestinationPath(String originalFilename,WebFileUploadBean upload) {
        String olFileName = originalFilename.replaceAll(" ","");
        return Config.fileSystemImportPath+ File.separator+olFileName;
    }

    abstract protected String getTextHeadline();
    abstract protected String getTextSubheadline();
    abstract protected String getTextInfo();
    abstract protected String getNextUrl();
    abstract protected String getTextSuccess();
    abstract protected String getHtmlCode();

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {
        servletRequestDataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {
        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.

        WebFileUploadBean upload = (WebFileUploadBean)o;

        this.setContentTemplateFile("message.jsp",httpServletRequest);

        User user = (User)httpServletRequest.getSession().getAttribute("user");
        if (user==null) logger.debug("User==null!!");

        if (upload.getFile()==null) { logger.debug("File is NULL"); }


        onUploadSubmit(httpServletRequest,null, o, e, upload, user);

    }
}
