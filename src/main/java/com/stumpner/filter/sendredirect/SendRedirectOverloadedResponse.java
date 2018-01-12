package com.stumpner.filter.sendredirect;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
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
 * Date: 28.09.2016
 * Time: 19:28:32
 * To change this template use File | Settings | File Templates.
 */
public class SendRedirectOverloadedResponse extends HttpServletResponseWrapper {

    private HttpServletRequest request = null;

    public SendRedirectOverloadedResponse(HttpServletRequest request, HttpServletResponse response) {

        super(response);
        //System.out.println("SendRedirectOverloadedResponse constructor ");
        this.request = request;
    }

    public void sendRedirect(String s) throws IOException {
        System.out.println("sendRedirect: "+s);

        if (!isUrlAbsolute(s)) {
            //Bei nicht absoluten URLs auf den richtigen Hostnamen redirecten
            String newUrl = getServerNameUrl(request)+s;
            if (!s.startsWith("/")) {
                //Relative Pfadangabe: z.B. c?id=13
                System.out.println("rewrite relative path: "+request.getRequestURI());
                String relativePath = getRelativePath(request.getRequestURI());
                newUrl = getServerNameUrl(request)+relativePath+s;
            }

            System.out.println("rewrite redirect to "+newUrl+" from old "+s);

            super.sendRedirect(newUrl);
        } else {
            //Bei absoluten URLs normal redirecten
            System.out.println("normal Redirect "+s);
            super.sendRedirect(s);
        }


    }

    /**
     * Wenn request URI /de/categoryedit ist, dann gibt diese Funktion /de/ zur�ck
     * @param requestURI
     * @return
     */
    private String getRelativePath(String requestURI) {

        if (requestURI.isEmpty()) {
            return "";
        } else {

            int indexOfSlash = requestURI.lastIndexOf("/");
            if (indexOfSlash>=0) {
                return requestURI.substring(0,indexOfSlash+1);
            } else {
                return ""; //Es gibt keinen Slash (/)
            }
        }

    }

    public boolean isUrlAbsolute(String url) {
      String lowercaseurl = url.toLowerCase();
      if (lowercaseurl.startsWith("http") == true)
      {
         return true;
      }
      else
      {
         return false;
      }
   }

    public String getServerNameUrl(HttpServletRequest request) {

        String portString = "";
        if (request.getScheme().equalsIgnoreCase("http") && request.getServerPort()!=80) {
            portString = ":"+request.getServerPort();
        } else if (request.getScheme().equalsIgnoreCase("https") && request.getServerPort()!=443) {
            portString = ":"+request.getServerPort();
        }

        String serverName = request.getServerName();
        if (request.getHeader("X-MEDIADESK-HOST")!=null) {
            serverName = request.getHeader("X-MEDIADESK-HOST");
        }

        String url = request.getScheme()+"://"+serverName+portString;

        return url;
    }

    /*

    public String getServerNameUrlPath(HttpServletRequest request) {

        String url = getServerNameUrl(request)+request.getRequestURI();

        return url;
    }

    public  String getServerNameUrlPathWithQueryString(HttpServletRequest request) {

        String url = getServerNameUrlPath(request);
        if (request.getQueryString()!=null) {
            url = url+"?"+request.getQueryString();
        }

        return url;
    } */
}
