package com.stumpner.mediadesk.upload;

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
 * Date: 13.11.2015
 * Time: 09:22:56
 */
public class PluginResult {

    public enum Status { OK,WARNING,ERROR }

    public PluginResult() {
        this.status = Status.OK;
        this.message = "";
        this.value = null;
    }

    public PluginResult(Status status, String message, Object value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    private Status status;

    private String message;

    private Object value;

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getValue() {
        return value;
    }
}
