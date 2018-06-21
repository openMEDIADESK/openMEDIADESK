package com.stumpner.mediadesk.web.mvc.commandclass.settings;

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
 * Date: 09.12.2005
 * Time: 13:50:01
 * To change this template use File | Settings | File Templates.
 */
public class WebSettings {

    String title = "";
    String copyright = "";
    String corporateSiteName = "";
    String corporateSiteLink = "";
    String instanceLogoURL = "";
    boolean showLogoUrl = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCorporateSiteName() {
        return corporateSiteName;
    }

    public void setCorporateSiteName(String corporateSiteName) {
        this.corporateSiteName = corporateSiteName;
    }

    public String getCorporateSiteLink() {
        return corporateSiteLink;
    }

    public void setCorporateSiteLink(String corporateSiteLink) {
        this.corporateSiteLink = corporateSiteLink;
    }

    public String getInstanceLogoURL() {
        return instanceLogoURL;
    }

    public void setInstanceLogoURL(String instanceLogoURL) {
        this.instanceLogoURL = instanceLogoURL;
    }

    public boolean isShowLogoUrl() {
        return showLogoUrl;
    }

    public void setShowLogoUrl(boolean showLogoUrl) {
        this.showLogoUrl = showLogoUrl;
    }
}
