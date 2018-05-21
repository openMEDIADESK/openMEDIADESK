package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.usermanagement.User;

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
 * User: stumpner
 * Date: 19.03.2008
 * Time: 10:32:37
 * @deprecated use {@link com.stumpner.mediadesk.web.api.rest.FolderRestApi}
 */
public interface ApiClass {

    /**
     * F�rt die angegebene Methode mit den parametern und als angegebener User aus
     * @param user
     * @param method
     * @param parameter
     * @return
     */
    public String call(User user, String method, String[] parameter);

    /**
     * �berpr�ft ob die klasse die angegebene Methode unterst�tzt.
     * @param method
     * @return
     */
    public boolean supportsMethod(String method);

}
