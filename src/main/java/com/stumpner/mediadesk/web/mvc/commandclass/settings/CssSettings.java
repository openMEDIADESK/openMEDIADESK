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
 * User: franzstumpner
 * Date: 27.12.2005
 * Time: 11:50:12
 * To change this template use File | Settings | File Templates.
 */
public class CssSettings {

    String cssAdd = "";
    String cssColorPrimaryHex = "";
    String cssColorAHref = "";

    public String getCssAdd() {
        return cssAdd;
    }

    public void setCssAdd(String cssAdd) {
        this.cssAdd = cssAdd;
    }

    public String getCssColorPrimaryHex() {
        return cssColorPrimaryHex;
    }

    public void setCssColorPrimaryHex(String cssColorPrimaryHex) {
        this.cssColorPrimaryHex = cssColorPrimaryHex;
    }

    public String getCssColorAHref() {
        return cssColorAHref;
    }

    public void setCssColorAHref(String cssColorAHref) {
        this.cssColorAHref = cssColorAHref;
    }
}
