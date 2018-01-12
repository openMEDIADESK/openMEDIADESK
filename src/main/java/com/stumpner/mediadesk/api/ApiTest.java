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
 * Time: 10:36:36
 */
public class ApiTest extends ApiBase {

    private final int TEST = 1;
    private final int ECHO = 2;
    private final int ECHOMULTI = 3;

    public ApiTest() {

        registerMethod("test",TEST);
        registerMethod("echo",ECHO);
        registerMethod("echomulti",ECHOMULTI);

    }

    public String call(User user, String method, String[] parameter) {

        switch (getMethodId(method)) {
            case TEST:
                return test(parameter);
            case ECHO:
                return echo(parameter);
            case ECHOMULTI:
                return echomulti(parameter);
        }

        return "no value";
    }

    private String test(String[] parameter) {
        return "test";
    }

    private String echo(String[] parameter) {
        return parameter[0]; 
    }

    private String echomulti(String[] parameter) {

        String echo = "";
        for (int i=0;i<parameter.length;i++) {
            echo = echo + " " + parameter[i];
        }

        return echo;
    }

}
