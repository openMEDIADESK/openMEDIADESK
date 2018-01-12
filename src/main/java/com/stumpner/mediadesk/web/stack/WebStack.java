package com.stumpner.mediadesk.web.stack;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Stack;
import java.util.Iterator;

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
 * User: franzstumpner
 * Date: 29.09.2005
 * Time: 21:58:16
 * To change this template use File | Settings | File Templates.
 */
public class WebStack {

    Stack stack = new Stack();
    HttpServletRequest request = null;

    public WebStack(HttpServletRequest request) {

        this.request = request;

        //stack aus session laden:
        if (request.getSession().getAttribute("stack")!=null) {
            stack = (Stack)request.getSession().getAttribute("stack");
        }

        if (stack.empty()) {
            //Wenn Stack leer: aktuelle Seite als erstes Element
            stack.push(request.getRequestURL());
        }

    }

    public void register() {

        Logger logger = Logger.getLogger(WebStack.class);
        //aktuelle seite in den stack laden:
        stack.set(stack.size()-1,(String)(request.getRequestURL()+"?"+request.getQueryString()));

        request.getSession().setAttribute("stack",stack);
        logger.info("WebStack: REGISTER: "+request.getRequestURL()+"?"+request.getQueryString());
    }

    public void push() {

        Logger logger = Logger.getLogger(WebStack.class);
        //stack erweitern (neuer abschnitt)
        stack.push((String)(request.getRequestURL()+"?"+request.getQueryString()));
        logger.info("WebStack: PUSH: "+request.getRequestURL()+"?"+request.getQueryString());
        request.getSession().setAttribute("stack",stack);

    }

    public String pop() {

        Logger logger = Logger.getLogger(WebStack.class);
        String stackContent = (String)stack.pop();
        request.getSession().setAttribute("stack",stack);
        logger.info("WebStack: POP: "+stackContent);
        return stackContent.toString();

    }

    public String getStacklistString() {

        String stacklistString = "";
        Iterator stackIt = stack.iterator();
        while (stackIt.hasNext()) {
            if (stacklistString.length()>0) { stacklistString+=" -> "; }
            stacklistString = stacklistString + stackIt.next();
        }
        return stacklistString;
    }



}
