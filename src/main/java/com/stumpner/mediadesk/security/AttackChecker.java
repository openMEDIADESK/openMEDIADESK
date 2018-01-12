package com.stumpner.mediadesk.security;

import javax.servlet.http.HttpServletRequest;
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
 * Date: 07.03.2007
 * Time: 18:08:52
 * Prüft den Request ob eine Attacke gemacht wurde
 */
public class AttackChecker {

    static Map failedRequestMap = new HashMap();
    static Map attackList = new HashMap();



    public boolean checkAttack(HttpServletRequest request) {

        if (attackList.containsKey(request.getRemoteAddr())) {

            Calendar calendar = GregorianCalendar.getInstance();
            calendar.roll(Calendar.MINUTE,-5);
            Date attackDate = (Date)attackList.get(request.getRemoteAddr());
            if (attackDate.after(calendar.getTime())) {
                return true; //attacke abgelaufen (vorbei)
            } else {
                return false; //attacke!
            }
        } else { return false; }

    }

    public void setAttack(HttpServletRequest request) {


        if (failedRequestMap.containsKey(request.getRemoteAddr())) {
            RequestEntry requestEntry =
                    (RequestEntry)failedRequestMap.get(request.getRemoteAddr());

            if (requestEntry.getFailcount()>2) {
                attackList.put(request.getRemoteAddr(),new Date());
                failedRequestMap.remove(request.getRemoteAddr());
            } else {
                requestEntry.setFailcount(
                        requestEntry.getFailcount()+1
                );
                failedRequestMap.put(request.getRemoteAddr(),requestEntry);
            }
        }
    }

}
