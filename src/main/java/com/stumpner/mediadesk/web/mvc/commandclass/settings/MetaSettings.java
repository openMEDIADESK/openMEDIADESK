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
 * Date: 12.12.2005
 * Time: 21:44:49
 * To change this template use File | Settings | File Templates.
 */
public class MetaSettings {

    String keywords = "";
    String description = "";

    String statCounterCode = "";
    String googleWebmasters = "";
    String googleAnalytics = "";

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatCounterCode() {
        return statCounterCode;
    }

    public void setStatCounterCode(String statCounterCode) {
        this.statCounterCode = statCounterCode;
    }

    public String getGoogleWebmasters() {
        return googleWebmasters;
    }

    public void setGoogleWebmasters(String googleWebmasters) {
        this.googleWebmasters = googleWebmasters;
    }

    public String getGoogleAnalytics() {
        return googleAnalytics;
    }

    public void setGoogleAnalytics(String googleAnalytics) {
        this.googleAnalytics = googleAnalytics;
    }


}
