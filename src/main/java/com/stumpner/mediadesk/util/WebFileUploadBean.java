package com.stumpner.mediadesk.util;

import org.springframework.web.multipart.MultipartFile;

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
 * Date: 08.05.2005
 * Time: 21:24:24
 * To change this template use File | Settings | File Templates.
 */
public class WebFileUploadBean {

    //private MultipartFile up = null;

    private MultipartFile file = null;
    private MultipartFile file2 = null;
    private MultipartFile file3 = null;

    private boolean boolData1 = false;
    private boolean boolData2 = false;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile2() {
        return file2;
    }

    public void setFile2(MultipartFile file2) {
        this.file2 = file2;
    }

    public MultipartFile getFile3() {
        return file3;
    }

    public void setFile3(MultipartFile file3) {
        this.file3 = file3;
    }

    public boolean isBoolData1() {
        return boolData1;
    }

    public void setBoolData1(boolean boolData1) {
        this.boolData1 = boolData1;
    }

    public boolean isBoolData2() {
        return boolData2;
    }

    public void setBoolData2(boolean boolData2) {
        this.boolData2 = boolData2;
    }
    /*
    public MultipartFile getUp() {
        return up;
    }

    public void setUp(MultipartFile up) {
        this.up = up;
    } */
}
