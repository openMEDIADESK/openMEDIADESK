package com.stumpner.mediadesk.image;

import com.stumpner.mediadesk.image.folder.NameValidator;

import java.util.Date;

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
 * Date: 10.04.2005
 * Time: 22:57:59
 * To change this template use File | Settings | File Templates.
 */
public class SimpleImage {

    protected int imageId = -1;
    private String imageName = "";
    private String imageTitle = "";
    private String imageSubTitle = "";
    private Date createDate;
    private String imageNumber = "";

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = NameValidator.normalize(imageName);
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getImageSubTitle() {
        return imageSubTitle;
    }

    public void setImageSubTitle(String imageSubTitle) {
        this.imageSubTitle = imageSubTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }
}
