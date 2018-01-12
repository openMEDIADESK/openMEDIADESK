package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.core.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.beans.XMLEncoder;

import org.json.JSONObject;

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
 * Date: 10.07.2016
 * Time: 13:40:10
 * To change this template use File | Settings | File Templates.
 */
public class SplashpageRestApi extends RestBaseServlet {

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html");

        System.out.println("doGet [SplashpageRestApi]");

        Map<String,String> splashPageValueMap = new HashMap<String,String>(); //Hier werden die Werte gespeichert!!
        if (httpServletRequest.getServletContext().getAttribute("splashPageValueMap")!=null) {
            splashPageValueMap = (Map<String,String>)httpServletRequest.getServletContext().getAttribute("splashPageValueMap");
        }

        JSONObject responseObj = new JSONObject();

        for (String key : splashPageValueMap.keySet()) {
            responseObj.put(key,splashPageValueMap.get(key));
        }

            //System.out.println("out = "+responseObj.toString());


            PrintWriter writer = httpServletResponse.getWriter();
            responseObj.write(writer);



        /*
        String type = this.getUriSection(5, request);
        if (type!=null) {
            if (type.equalsIgnoreCase("medialist")) {
                //Liste der Medienobjekte
                System.out.println("doGet [CartRestApi->medialist]");
                jsonCartMedialist(request, response);
            }

            if (type.equalsIgnoreCase("removeselected")) {
                removeSelected(request, response);
            }
        } else {
            //immer medialist zur�ckgeben wenn get request und sonst nichts angegeben
            System.out.println("doGet [CartRestApi->medialist]");
            jsonCartMedialist(request, response);
        } */
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPut [SplashpageRestApi]");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        int ivid = this.getUriSectionInt(4, request);


    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        Map<String,String> splashPageValueMap = new HashMap<String,String>(); //Hier werden die Werte gespeichert!!
        if (httpServletRequest.getServletContext().getAttribute("splashPageValueMap")!=null) {
            splashPageValueMap = (Map<String,String>)httpServletRequest.getServletContext().getAttribute("splashPageValueMap");
        }

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html");

        System.out.println("doPost [SplashpageRestApi]");

        httpServletRequest.getParameterNames();

        if (httpServletRequest.getParameterNames().hasMoreElements()) {
            String jsonString = httpServletRequest.getParameterNames().nextElement();

            //System.out.println("value= "+jsonString);

            JSONObject jsonObject = new JSONObject(jsonString);

            String welcometext = jsonObject.optString("welcometext","");
            String section1headline = jsonObject.optString("section1headline","");
            String section1text = jsonObject.optString("section1text","");

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                splashPageValueMap.put(key,value);
                //System.out.println("KEY:VALUE = "+key+" : "+value);
            }

            /*
            name = jsonObject.getString("name");
            username = jsonObject.getString("username");
            email = jsonObject.getString("email");
            */
            /*
            System.out.println("name="+welcometext);
            System.out.println("username="+section1headline);
            System.out.println("email="+section1text);
              */
            Map<String,String> errorMap = new HashMap<String,String>();
            if (welcometext.isEmpty()) {
                errorMap.put("welcometext","Name is required");
            }

            JSONObject responseObj = new JSONObject();
            if (errorMap.size()>0) {
                responseObj.put("errors", errorMap);
            }
            responseObj.put("message","Form data is going well");
            //responseObj.w

            //System.out.println("out = "+responseObj.toString());

            httpServletRequest.getServletContext().setAttribute("splashPageValueMap", splashPageValueMap);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            XMLEncoder xmlEncoder = new XMLEncoder(bos);
            xmlEncoder.writeObject(splashPageValueMap);
            xmlEncoder.flush();

            String serializedMap = bos.toString();

            Config.splashPageData = serializedMap;

            Config.saveConfiguration();

            System.out.println("serializedMap: "+serializedMap);

            PrintWriter writer = httpServletResponse.getWriter();
            responseObj.write(writer);
        }

    }
}
