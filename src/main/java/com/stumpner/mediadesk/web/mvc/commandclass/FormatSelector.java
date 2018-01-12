package com.stumpner.mediadesk.web.mvc.commandclass;

import com.stumpner.mediadesk.image.ImageVersion;

import java.util.List;
import java.util.LinkedList;
import java.awt.*;

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
 * Date: 04.01.2008
 * Time: 10:04:32
 * To change this template use File | Settings | File Templates.
 */
public class FormatSelector {

/*
                model.put("downloadList",downloadList);
                model.put("downloadCount",new Integer(downloadList.size()));
                model.put("user",this.getUser(httpServletRequest));
                model.put("deniedList",deniedList);
 */

    List downloadList = new LinkedList();
    List deniedList = new LinkedList();
    List availableFormatList = new LinkedList(); //Verfügbare Formate <Rectangle>
    List selectedFormat = new LinkedList(); //Ausgewähltes Format(index) für die download List (index muss zusammenstimmen)
    
    int downloadCount = 0;

    boolean allFormatsDenied = false;

    public Rectangle getFormat(ImageVersion imageVersion) {

        int imageIndex = downloadList.indexOf(imageVersion);
        int formatIndex = Integer.parseInt((String)selectedFormat.get(imageIndex));
        if (formatIndex==-1) {
            //Original-Auflösung
            return new Rectangle(imageVersion.getWidth(),imageVersion.getHeight());
        } else {
            return (Rectangle)availableFormatList.get(formatIndex);
        }
    }

    /**
     * Gibt zurück ob die ImageVersion im Original-Format angefordert (ausgewählt) wurde
     * @param imageVersion
     * @return
     */
    public boolean isOriginalFormat(ImageVersion imageVersion) {

        Rectangle rect = getFormat(imageVersion);
        if (rect.getWidth()==imageVersion.getWidth() &&
                rect.getHeight()==imageVersion.getHeight()) {
            return true;
        } else {
            return false;
        }

    }

    public List getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(List downloadList) {
        this.downloadList = downloadList;
    }

    public List getDeniedList() {
        return deniedList;
    }

    public void setDeniedList(List deniedList) {
        this.deniedList = deniedList;
    }

    public List getAvailableFormatList() {
        return availableFormatList;
    }

    public void setAvailableFormatList(List availableFormatList) {
        this.availableFormatList = availableFormatList;
    }

    public List getSelectedFormat() {
        return selectedFormat;
    }

    public void setSelectedFormat(List selectedFormat) {
        this.selectedFormat = selectedFormat;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public boolean isAllFormatsDenied() {
        return allFormatsDenied;
    }

    public void setAllFormatsDenied(boolean allFormatsDenied) {
        this.allFormatsDenied = allFormatsDenied;
    }
}
