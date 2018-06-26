package com.stumpner.mediadesk.api.json;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
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
 * Abgreifen der Files in einem Ordner via JSON:
 * http://localhost:8080/api/json/categorycontent/<folderId>
 * oder mit jsessionid
 * http://localhost:8080/api/json/categorycontent/<folderId>;jsessionid=<sessionid>
 * Berücksichtigt wird, ob der eingeloggte Benutzer auch Zugriff auf diese Files hat.
 * Mit dem Paramter ?callback=function wird das JSON-Array mit einer Callback-Funktion aufgerufen
 *   callbackFunction(...json...)
 *
 * [{"id":"3391","title":"Meine PDF-Datei","mime":"application/pdf","downloadUrl":"/de/download?download=ivid&amp;ivid=3391"}, ... ]
 *
 * User: stumpner
 * Date: 28.09.2011
 * Time: 10:00:53
 * @deprecated use {@link com.stumpner.mediadesk.web.api.rest.FolderRestApi}
 */
public class FolderContent extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        String URI = request.getRequestURI();
        if (URI.toUpperCase().indexOf(";JSESSIONID")>0) {
            //Wenn SessionID in der URL übergeben, dann herausfiltern:
            URI = URI.substring(0,URI.toUpperCase().indexOf(";JSESSIONID"));
        }
        String[] uriArray = URI.split("/");
        int folderId = 0;
        try {
            folderId = Integer.parseInt(uriArray[uriArray.length - 1]);
            String json = getJsonOutput(folderId, request);
            String responseString = getJsonCallback(json,request);
            response.getWriter().write(responseString);

        } catch (ObjectNotFoundException e) {
            response.sendError(404,"Folder not found: " + folderId + " - " + e.getMessage());
        } catch (IOServiceException e) {
            throw new ServletException("IOServiceException e: " + e.getMessage());
        } catch (AclNotFoundException e) {
            throw new ServletException("AclNotFoundException e: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(400,"Folder-ID is not a number: " + folderId + " - " + e.getMessage());
        }

    }

    private String getJsonCallback(String json, HttpServletRequest request) {

        if (request.getParameter("callback")!=null) {
            return request.getParameter("callback")+"("+json+")";
        } else {
            return json;
        }

    }

    private String getJsonOutput(int folderId, HttpServletRequest request) throws ObjectNotFoundException, IOServiceException, AclNotFoundException {

        StringBuffer jsonOutput = new StringBuffer("[");

        FolderService folderService = new FolderService();
            Folder folder = folderService.getFolderById(folderId);

            AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
            if (aclCtx.checkPermission(new AclPermission("read"), folder)) {

                LngResolver lngResolver = new LngResolver();
                MediaService mediaService = new MediaService();
                mediaService.setUsedLanguage(lngResolver.resolveLng(request));
                SimpleLoaderClass loader = new SimpleLoaderClass();
                loader.setId(folderId);
                List<MediaObjectMultiLang> mediaObjectList = mediaService.getFolderMediaObjects(loader);

                Iterator<MediaObjectMultiLang> mediaIt = mediaObjectList.iterator();
                while (mediaIt.hasNext()) {

                    MediaObjectMultiLang mediaObject = mediaIt.next();
                    //Content
                    jsonOutput.append("\n{");

                    jsonOutput.append("\"id\":\"" + mediaObject.getIvid() + "\",");
                    jsonOutput.append("\"title\":\"" + mediaObject.getVersionTitle() + "\",");
                    jsonOutput.append("\"mime\":\"" + mediaObject.getMimeType() + "\",");
                    jsonOutput.append("\"downloadUrl\":\"/de/download;jsessionid="+request.getSession().getId()+"?download=ivid&amp;ivid=" + mediaObject.getIvid() + "\"");

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

