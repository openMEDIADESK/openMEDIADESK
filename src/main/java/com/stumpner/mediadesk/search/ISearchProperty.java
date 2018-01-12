package com.stumpner.mediadesk.search;

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
 * Date: 01.09.2005
 * Time: 19:49:12
 * To change this template use File | Settings | File Templates.
 */
public interface ISearchProperty {

    public void setUsedLanguage(int usedLanguage);

    public int getUsedLanguage();

    public String getKeywords();

    public void setKeywords(String keywords);

    public int getStartItem();

    public void setStartItem(int startItem);

    public int getItemCount();

    public void setItemCount(int itemCount);

    public int getSuid();

    public void setSuid(int suid);

    public void setRequery(int requery);

    public int getRequery();

}
