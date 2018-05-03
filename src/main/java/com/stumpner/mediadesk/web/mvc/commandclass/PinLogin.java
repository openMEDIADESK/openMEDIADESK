package com.stumpner.mediadesk.web.mvc.commandclass;

import com.stumpner.mediadesk.pin.Pin;

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
 * Created by stumpner on 21.08.2017.
 */
public class PinLogin extends Pin {

    String pin = "";
    String password = "";

    boolean usePassword = false;

    boolean useCaptcha = true;
    boolean captchaOk = false;

    @Override
    public String getPin() {
        return pin;
    }

    @Override
    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsePassword() {
        return usePassword;
    }

    public void setUsePassword(boolean usePassword) {
        this.usePassword = usePassword;
    }

    public boolean isUseCaptcha() {
        return useCaptcha;
    }

    public void setUseCaptcha(boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
    }

    public boolean isCaptchaOk() {
        return captchaOk;
    }

    public void setCaptchaOk(boolean captchaOk) {
        this.captchaOk = captchaOk;
    }
}
