package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.usermanagement.User;

import java.util.Map;
import java.util.HashMap;

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
 * Time: 11:30:18
 * @deprecated use {@link com.stumpner.mediadesk.web.api.rest.FolderRestApi}
 */
public class ApiBase implements ApiClass {

    private Map methodMap = new HashMap();

    public String call(User user, String method, String[] parameter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean supportsMethod(String method) {
        Object methodId = methodMap.get(method);
        if (methodId!=null) {
            return true;
        } else {
            return false;
        }
    }

    protected void registerMethod(String methodName, int methodId) {
        methodMap.put(methodName,new Integer(methodId));
    }

    protected int getMethodId(String methodName) {
        Integer methodId = (Integer)methodMap.get(methodName);
        return methodId.intValue();
    }
}
