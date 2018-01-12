package com.stumpner.mediadesk.web.mvc.commandclass;

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
 * Date: 21.02.2007
 * Time: 21:20:54
 * To change this template use File | Settings | File Templates.
 */
public class BulkModification {

    boolean reimportMetadata = false;
    boolean redrawWatermark = false;

    int imageCount = 0;
    int imageProcessed = 0;

    boolean inProgress = false;
    boolean halted = false;

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getImageProcessed() {
        return imageProcessed;
    }

    public void setImageProcessed(int imageProcessed) {
        this.imageProcessed = imageProcessed;
    }

    public boolean isReimportMetadata() {
        return reimportMetadata;
    }

    public void setReimportMetadata(boolean reimportMetadata) {
        this.reimportMetadata = reimportMetadata;
    }

    public boolean isRedrawWatermark() {
        return redrawWatermark;
    }

    public void setRedrawWatermark(boolean redrawWatermark) {
        this.redrawWatermark = redrawWatermark;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isHalted() {
        return halted;
    }

    public void setHalted(boolean halted) {
        this.halted = halted;
    }

}
