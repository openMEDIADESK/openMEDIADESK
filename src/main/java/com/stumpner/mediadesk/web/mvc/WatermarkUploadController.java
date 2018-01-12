package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.util.WebFileUploadBean;
import com.stumpner.mediadesk.usermanagement.User;

import java.io.IOException;

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
 * Date: 25.05.2007
 * Time: 15:17:59
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkUploadController extends FileUploadController {

    /**
     * Speichert das upgeloadete File ab, je nachdem ob horizontal oder vertical angegeben wurde
     * @param upload
     * @param user
     * @throws IOException
     */
    protected void doFileUpload(WebFileUploadBean upload, User user) throws IOException {

        if (upload.isBoolData1()) {
            //Horizontal
            this.writeFile(upload.getFile(),Config.watermarkHorizontal);
        }
        if (upload.isBoolData2()) {
            //Vertical
            this.writeFile(upload.getFile(),Config.watermarkVertical);
        }
    }

    protected String defineInput(int type) {

        switch(type) {
            case INPUT_TYPE_BOOL1:
                //hor
                return "set.watermark.file.wmh";
            case INPUT_TYPE_BOOL2:
                //vert:
                return "set.watermark.file.wmv";
        }

        return "set.watermark.file.wmh";
    }

    protected String getTextHeadline() {
        return "set.watermark.file.headline";
    }

    protected String getTextSubheadline() {
        return "set.watermark.file.subheadline";
    }

    protected String getTextInfo() {
        return "set.watermark.file.info";
    }

    protected String getNextUrl() {
        return "../setwatermark";
    }

    protected String getTextSuccess() {
        return "set.watermark.file.success";
    }

    protected String getHtmlCode() {
        return "";
    }
}
