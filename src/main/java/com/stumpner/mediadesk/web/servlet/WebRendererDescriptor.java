package com.stumpner.mediadesk.web.servlet;

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
 * Date: 22.03.2005
 * Time: 21:28:54
 * To change this template use File | Settings | File Templates.
 */
public class WebRendererDescriptor {

    private String head_html = "";
    private String left_html = "";
    private String content_html = "";

    public String getHead_html() {
        return head_html;
    }

    protected void setHead_html(String head_html) {
        this.head_html = head_html;
    }

    public String getLeft_html() {
        return left_html;
    }

    protected void setLeft_html(String left_html) {
        this.left_html = left_html;
    }

    public String getContent_html() {
        return content_html;
    }

    protected void setContent_html(String content_html) {
        this.content_html = content_html;
    }
}
