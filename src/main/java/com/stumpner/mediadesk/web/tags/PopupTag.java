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
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 24.05.2007
 * Time: 08:26:25
 * To change this template use File | Settings | File Templates.
 */
public class PopupTag extends TagSupport {

    /**
     * Code unter dem der Custom Text zu finden ist
     */
    String code = "";
    /**
     * Html-Datei ohne Static Url Prefix und ohne Language Code
     */
    String htmlFile = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int doStartTag() throws JspException {

        if (CustomTextService.hasCustomText(code,(HttpServletRequest)pageContext.getRequest())) {

            //hat einen Custom Text
            try {
                //Das out Object beschaffen
                JspWriter out = pageContext.getOut();
                out.println(
         "<a href=\"/index/popup/"+code+"\" onClick=\"window.open('/index/popup/"+code+"','PopTerm','scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,width=600,height=500');return false;\" >text</a>"
                );
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return SKIP_BODY;
        } else {
            return EVAL_BODY_INCLUDE;
        }
    }

}
