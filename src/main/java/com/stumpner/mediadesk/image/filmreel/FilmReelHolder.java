package com.stumpner.mediadesk.image.filmreel;

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
 * Time: 18:15:48
 * To change this template use File | Settings | File Templates.
 */
public class FilmReelHolder {

    int frhid = -1;
    int filmReelId = -1;
    int ivid = -1;
    int pos = -1;

    public int getFrhid() {
        return frhid;
    }

    public void setFrhid(int frhid) {
        this.frhid = frhid;
    }

    public int getFilmReelId() {
        return filmReelId;
    }

    public void setFilmReelId(int filmReelId) {
        this.filmReelId = filmReelId;
    }

    public int getIvid() {
        return ivid;
    }

    public void setIvid(int ivid) {
        this.ivid = ivid;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
