package com.stumpner.mediadesk.web.mvc.commandclass;

import java.util.List;
import java.util.LinkedList;

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
 * Date: 11.06.2012
 * Time: 20:38:14
 * To change this template use File | Settings | File Templates.
 */
public class FolderSelection {

    private String type = "";
    private int id = 0;

    private List<SelectableFolder> categoryList = new LinkedList<SelectableFolder>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SelectableFolder> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<SelectableFolder> categoryList) {
        this.categoryList = categoryList;
    }
}
