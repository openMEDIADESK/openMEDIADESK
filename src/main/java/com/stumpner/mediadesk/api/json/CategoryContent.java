package com.stumpner.mediadesk.api.json;

import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.image.ImageVersionMultiLang;
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.security.acl.AclNotFoundException;

import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;

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
 * Abgreifen der Files in einer Kategorie via JSON:
 * http://localhost:8080/api/json/categorycontent/<categoryId>
 * oder mit jsessionid
 * http://localhost:8080/api/json/categorycontent/<categoryId>;jsessionid=<sessionid>
 * Ber�cksichtigt wird, ob der eingeloggte Benutzer auch Zugriff auf diese Files hat.
 * Mit dem Paramter ?callback=function wird das JSON-Array mit einer Callback-Funktion aufgerufen
 *   callbackFunction(...json...)
 *
 * [{"id":"3391","title":"Sicherheits- und Montagehinweise SPRECON-E Englisch","mime":"application/pdf","downloadUrl":"/de/download?download=ivid&amp;ivid=3391"}, ... ]
 *
 * User: stumpner
 * Date: 28.09.2011
 * Time: 10:00:53
 */
public class CategoryContent extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        //System.out.println("jsonRequest. CategoryContent, jsessionid="+request.getSession().getId());
        //System.out.println("jsonRequest. CategoryContent, uri="+request.getRequestURI());

        String URI = request.getRequestURI();
        if (URI.toUpperCase().indexOf(";JSESSIONID")>0) {
            //Wenn SessionID in der URL �bergeben, dann herausfiltern:
            URI = URI.substring(0,URI.toUpperCase().indexOf(";JSESSIONID"));
        }
        String[] uriArray = URI.split("/");
        int categoryId = 0;
        try {
            categoryId = Integer.parseInt(uriArray[uriArray.length - 1]);
            String json = getJsonOutput(categoryId, request);
            String responseString = getJsonCallback(json,request);
            response.getWriter().write(responseString);

        } catch (ObjectNotFoundException e) {
            response.sendError(404,"Category not found: " + categoryId + " - " + e.getMessage());
        } catch (IOServiceException e) {
            throw new ServletException("IOServiceException e: " + e.getMessage());
        } catch (AclNotFoundException e) {
            throw new ServletException("AclNotFoundException e: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(400,"Category-ID is not a number: " + categoryId + " - " + e.getMessage());
        }

    }

    private String getJsonCallback(String json, HttpServletRequest request) {

        if (request.getParameter("callback")!=null) {
            return request.getParameter("callback")+"("+json+")";
        } else {
            return json;
        }

    }

    private String getJsonOutput(int categoryId, HttpServletRequest request) throws ObjectNotFoundException, IOServiceException, AclNotFoundException {

        StringBuffer jsonOutput = new StringBuffer("[");

        CategoryService categoryService = new CategoryService();
            Category category = categoryService.getCategoryById(categoryId);

            AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
            if (aclCtx.checkPermission(new AclPermission("read"), category)) {

                LngResolver lngResolver = new LngResolver();
                ImageVersionService mediaService = new ImageVersionService();
                mediaService.setUsedLanguage(lngResolver.resolveLng(request));
                SimpleLoaderClass loader = new SimpleLoaderClass();
                loader.setId(categoryId);
                List<ImageVersionMultiLang> mediaObjectList = mediaService.getCategoryImages(loader);

                Iterator<ImageVersionMultiLang> mediaIt = mediaObjectList.iterator();
                while (mediaIt.hasNext()) {

                    ImageVersionMultiLang mediaObject = mediaIt.next();
                    //Content
                    jsonOutput.append("\n{");

                    jsonOutput.append("\"id\":\"" + mediaObject.getImageId() + "\",");
                    jsonOutput.append("\"title\":\"" + mediaObject.getVersionTitle() + "\",");
                    jsonOutput.append("\"mime\":\"" + mediaObject.getMimeType() + "\",");
//                    jsonOutput.append("\"downloadUrl\":\"/de/download?download=ivid&amp;ivid=" + mediaObject.getImageId() + "\",");
                    jsonOutput.append("\"downloadUrl\":\"/de/download;jsessionid="+request.getSession().getId()+"?download=ivid&amp;ivid=" + mediaObject.getImageId() + "\"");

                    jsonOutput.append("}");

                    if (mediaIt.hasNext()) {
                        jsonOutput.append(",");
                    }

                }
            }

        //Ende
        jsonOutput.append("]");

        return jsonOutput.toString();

    }

}

