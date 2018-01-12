package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.util.WebFileUploadBean;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;

import java.io.File;
import java.awt.image.BufferedImage;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.imageio.ImageIO;

import com.stumpner.mediadesk.util.FileUtil;
import com.stumpner.mediadesk.web.mvc.common.GlobalRequestDataProvider;

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
 * Date: 19.10.2009
 * Time: 19:21:44
 * To change this template use File | Settings | File Templates.
 */
public class LogoUploadController extends FileUploadController {

    String filename = "";

    protected String getDestinationPath(String originalFilename, WebFileUploadBean upload) {

        if (filename.equalsIgnoreCase("logo2.png")) {
            return Config.imageStorePath+ File.separator+"TMP"+filename;
        } else {
            return Config.imageStorePath+ File.separator+filename;
        }

    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        Config.instanceLogo = "/logo.gif";
        Config.saveConfiguration();
        
        //cachefix
        GlobalRequestDataProvider.doCacheFix();

        ModelAndView mov = super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.

        return mov;

    }

    protected void onUploadSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException, WebFileUploadBean upload, User user) {

        super.onUploadSubmit(httpServletRequest, httpServletResponse, object, bindException, upload, user);    //To change body of overridden methods use File | Settings | File Templates.

        if (filename.equalsIgnoreCase("logo2.png")) {
            //Pr�fen wie gro� die Datei ist
            BufferedImage readImage = null;
            try {
                readImage = ImageIO.read(new File(Config.imageStorePath+ File.separator+"TMP"+filename));
                if (readImage==null) {
                    bindException.reject("set.web.logo2.formaterror","Falsches Dateiformat: M�glich sind PNG, GIF, JPG");
                } else {

                    int h = readImage.getHeight();
                    int w = readImage.getWidth();
                    System.out.println("Logo2 Upload h="+h+" w="+w);

                    boolean validateHeight = Config.configParam.indexOf("-LOGOL")>=0 ? false : true; //Logo-H�he nicht validieren, wenn -LOGOL als Parameter eingesetzt wurde
                    boolean heightValid = true;

                    if (validateHeight) {
                        if (h>22) {
                            bindException.reject("set.web.logo2.heighterror","Maximale Logo H�he �berschritten");
                            heightValid = false;
                        }
                    }

                    if (heightValid) {
                        FileUtil.copyFile(new File(Config.imageStorePath+ File.separator+"TMP"+filename),
                                new File(Config.imageStorePath+ File.separator+filename));

                        File tmpFile = new File(Config.imageStorePath+ File.separator+"TMP"+filename);
                        tmpFile.delete();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected String getTextHeadline() {
        return "set.web.logo.headline";
    }

    protected String getTextSubheadline() {
        return "set.web.logo.subheadline";
    }

    protected String getTextInfo() {
        if (filename.equalsIgnoreCase("logo2.png")) {
            return "set.web.logo2.info";
        } else {
            return "set.web.logo.info";
        }
    }

    protected String getNextUrl() {
        return "setweb";
    }

    protected String getTextSuccess() {
        return "set.web.logo.success";
    }

    protected String getHtmlCode() {
        return "<p><img src=\"/logo.gif\"/></p>";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
