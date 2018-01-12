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
 * User: franz.stumpner
 * Date: 23.05.2007
 * Time: 14:14:52
 * To change this template use File | Settings | File Templates.
 */
public class TextEdit {

    String html = "";
    String messageHeadline = "";
    String messageInfo = "";

    boolean useExtraBoolValue = false;
    boolean booleanValue = false;
    String messageBoolean = "";

    public String getHtml() {
        return html;
    }

    public String getHtmlEscaped() {
        return html.replaceAll("\'","&#39;");
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getMessageHeadline() {
        return messageHeadline;
    }

    public void setMessageHeadline(String messageHeadline) {
        this.messageHeadline = messageHeadline;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public boolean isUseExtraBoolValue() {
        return useExtraBoolValue;
    }

    public void setUseExtraBoolValue(boolean useExtraBoolValue) {
        this.useExtraBoolValue = useExtraBoolValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getMessageBoolean() {
        return messageBoolean;
    }

    public void setMessageBoolean(String messageBoolean) {
        this.messageBoolean = messageBoolean;
    }


}
