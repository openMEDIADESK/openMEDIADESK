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
 * Date: 22.05.2012
 * Time: 21:59:04
 * To change this template use File | Settings | File Templates.
 */
public class MaintenanceSettings {

    String etoc = "";
    boolean resetAclActive = false;
    String resetAclStatus = "";

    String catviewauto = "";
    String acl = "";

    int eventCount = 0;

    public boolean isResetAclActive() {
        return resetAclActive;
    }

    public void setResetAclActive(boolean resetAclActive) {
        this.resetAclActive = resetAclActive;
    }

    public String getResetAclStatus() {
        return resetAclStatus;
    }

    public void setResetAclStatus(String resetAclStatus) {
        this.resetAclStatus = resetAclStatus;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public String getEtoc() {
        return etoc;
    }

    public void setEtoc(String etoc) {
        this.etoc = etoc;
    }

    public String getCatviewauto() {
        return catviewauto;
    }

    public void setCatviewauto(String catviewauto) {
        this.catviewauto = catviewauto;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }
}
