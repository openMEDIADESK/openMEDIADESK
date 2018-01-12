package com.stumpner.mediadesk.web.mvc.commandclass;

import com.stumpner.mediadesk.image.ImageVersion;

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
 * Date: 23.07.2008
 * Time: 20:13:31
 * To change this template use File | Settings | File Templates.
 */
public class SendCommand {

    private String recipient = "";
    private String subject = "";
    private String mailtext = "";
    private boolean asAttachment = false;
    private boolean onlyAsLink = false;
    private ImageVersion imageVersion = null;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMailtext() {
        return mailtext;
    }

    public void setMailtext(String mailtext) {
        this.mailtext = mailtext;
    }

    public boolean isAsAttachment() {
        return asAttachment;
    }

    public void setAsAttachment(boolean asAttachment) {
        this.asAttachment = asAttachment;
    }

    public ImageVersion getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(ImageVersion imageVersion) {
        this.imageVersion = imageVersion;
    }

    public boolean isOnlyAsLink() {
        return onlyAsLink;
    }

    public void setOnlyAsLink(boolean onlyAsLink) {
        this.onlyAsLink = onlyAsLink;
    }
}
