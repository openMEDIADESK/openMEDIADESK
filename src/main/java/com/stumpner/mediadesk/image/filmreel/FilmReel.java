package com.stumpner.mediadesk.image.filmreel;

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
 * Date: 13.04.2005
 * Time: 18:12:22
 * To change this template use File | Settings | File Templates.
 *
 * Businessobject FilmReel (Filmrolle)
 */
public class FilmReel {

    int filmReelId = -1;
    Date deploymentDate = new Date();
    int deploymentUserId = -1;
    String filmReelTitle = "";
    String filmReelName = "";

    public int getFilmReelId() {
        return filmReelId;
    }

    public void setFilmReelId(int filmReelId) {
        this.filmReelId = filmReelId;
    }

    public Date getDeploymentDate() {
        return deploymentDate;
    }

    public void setDeploymentDate(Date deploymentDate) {
        this.deploymentDate = deploymentDate;
    }

    public int getDeploymentUserId() {
        return deploymentUserId;
    }

    public void setDeploymentUserId(int deploymentUserId) {
        this.deploymentUserId = deploymentUserId;
    }

    public String getFilmReelTitle() {
        return filmReelTitle;
    }

    public void setFilmReelTitle(String filmReelTitle) {
        this.filmReelTitle = filmReelTitle;
    }

    public String getFilmReelName() {
        return filmReelName;
    }

    public void setFilmReelName(String filmReelName) {
        this.filmReelName = filmReelName;
    }

}
