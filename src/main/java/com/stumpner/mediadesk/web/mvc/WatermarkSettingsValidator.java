package com.stumpner.mediadesk.web.mvc;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.WatermarkSettings;

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
 * Date: 01.12.2008
 * Time: 20:50:53
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkSettingsValidator implements Validator {

    public boolean supports(Class aClass) {

        if (aClass.equals(WatermarkSettings.class)) {
            return true;
        } else {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public void validate(Object o, Errors errors) {

        WatermarkSettings settings = (WatermarkSettings)o;
        if (settings.getIntensity()<0 || settings.getIntensity()>100) {
            errors.reject("set.watermark.error.intensity","!!DM intensity");
            errors.rejectValue("intensity","",null,"value required");
        }

    }
}
