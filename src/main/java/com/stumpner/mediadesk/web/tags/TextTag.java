package com.stumpner.mediadesk.web.tags;

import com.stumpner.mediadesk.media.image.util.CustomTextService;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
 * Textverwaltung / Custom-Text abfrage und ausgabe
 */
public class TextTag extends TagSupport {

    String code = "";
    boolean showBodyOnEmpty = true; //Body des Tags anzeigen wenn der Text leer ist (standard)
    boolean showBodyOnText = false; //Body des Tags anzeigen wenn der Text nicht leer ist

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isShowBodyOnEmpty() {
        return showBodyOnEmpty;
    }

    public void setShowBodyOnEmpty(boolean showBodyOnEmpty) {
        this.showBodyOnEmpty = showBodyOnEmpty;
    }

    public boolean isShowBodyOnText() {
        return showBodyOnText;
    }

    public void setShowBodyOnText(boolean showBodyOnText) {
        this.showBodyOnText = showBodyOnText;
    }

    public int doStartTag() throws JspException {

        if (CustomTextService.hasCustomText(code,(HttpServletRequest)pageContext.getRequest())) {

            //hat einen Custom Text
            try {
                //Das out Object beschaffen
                JspWriter out = pageContext.getOut();
                out.println(CustomTextService.getCustomText(code,(HttpServletRequest)pageContext.getRequest()));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return SKIP_BODY;
        } else {
            return EVAL_BODY_INCLUDE;
        }
    }

}
