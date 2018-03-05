package com.stumpner.mediadesk.image.folder;

import java.util.*;

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
 * Date: 27.12.2006
 * Time: 22:14:37
 * To change this template use File | Settings | File Templates.
 */
public class ImageCountCalc {

    List categoryList = new ArrayList();
    List updateCategoryList = new LinkedList();

    public ImageCountCalc(List categoryList) {
        this.categoryList = categoryList;
    }

    public Folder getCategory(int categoryId) {

        Iterator it = categoryList.iterator();
        while (it.hasNext()) {
            Folder cat = (Folder)it.next();
            if (cat.getCategoryId()==categoryId) {
                return cat;
            }
        }

        return null;
    }

    public List getChildList(int parentId) {

        List childList = new LinkedList();

        Iterator it = categoryList.iterator();
        while (it.hasNext()) {
            Folder cat = (Folder)it.next();
            if (cat.getParent()==parentId) {
                childList.add(cat);
            }
        }

        return childList;
    }

    public int getImageCount(int categoryId) {

        return getImageCountRec(categoryId);
    }

    private int getImageCountRec(int categoryId) {

        int count = 0;
        List childList = this.getChildList(categoryId);
        Iterator it = childList.iterator();

        while (it.hasNext()) {
            Folder cat = (Folder)it.next();
            int catCount = getImageCountRec(cat.getCategoryId());
            count += catCount;
        }

        if (categoryId!=0) {
            Folder folder = getCategory(categoryId);

            /* Prüfen ob die Cumulative Bildanzahl (Unterkategorien) stimmt, ansonsten in die Update-Liste geben */

            /* Derzeit wird IMMER upgedatet, weil es sonst zu fehlern/updateproblemen kommt */
            //if (folder.getImageCountS()!=folder.getImageCount()+count) {
                folder.setImageCountS(folder.getImageCount()+count);
                updateCategoryList.add(folder);
            //}

            return folder.getImageCount()+count;
        } else {
            return count;
        }
    }

    public List getUpdateCategoryList() {
        return updateCategoryList;
    }

}
