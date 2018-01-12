/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 12.09.2007
 * Time: 18:02:09
 * To change this template use File | Settings | File Templates.
 */
package com.stumpner.mediadesk.search;

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

public class SearchLogger {
    private static SearchLogger ourInstance = new SearchLogger();

    public static SearchLogger getInstance() {
        return ourInstance;
    }

    private SearchLogger() {
    }

    private LinkedList searchList = new LinkedList();

    public void log(SearchEntity searchEntity) {
        searchList.add(searchEntity);
    }

    public List getLog() {
        return (List)searchList.clone();
    }

    public synchronized List clearLog() {
        List returnList = (List)searchList.clone();
        searchList.clear();
        return returnList;
    }
}
