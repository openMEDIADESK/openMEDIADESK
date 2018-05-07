package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.web.LngResolver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.text.SimpleDateFormat;

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
 * Date: 01.08.2005
 * Time: 21:09:05
 * To change this template use File | Settings | File Templates.
 */
public class RssServlet extends HttpServlet {

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {


        //checken ob rss enabled ist

        if (Config.rss) {

            if (httpServletRequest.getPathInfo().startsWith("/podcast/")) {
                if (Config.podcastEnabled) {
                    this.servicePodcast(httpServletRequest,httpServletResponse);
                } else {
                //printout help:
                PrintWriter writer = httpServletResponse.getWriter();
                writer.print("Podcasts not enabled!");
                writer.flush();
                writer.close();
                }
            }

            if (httpServletRequest.getPathInfo().equalsIgnoreCase("/")) {

                //printout help:
                PrintWriter writer = httpServletResponse.getWriter();
                writer.print("use /rss/folder.js for javascript syndication or /rss/folder.xml for rss0.92 syndication");
                writer.flush();
                writer.close();

            }

        } else {
            //printout rss disabled:
            PrintWriter writer = httpServletResponse.getWriter();
            writer.print("rss0.92 syndication ist nicht aktiviert / rss0.92 syndication is not enabled");
            writer.flush();
            writer.close();
        }


    }

    private void servicePodcast(HttpServletRequest request, HttpServletResponse response) throws IOException {

        LngResolver lngResolver = new LngResolver();
        boolean iTunesCompatible = true;
        String httpBase = "http://";
        httpBase = httpBase + request.getServerName();
        if (request.getServerPort()!=80) {
            httpBase = httpBase + ":" + Integer.toString(request.getServerPort());
        }
        httpBase = httpBase + "/";


        response.setHeader("Content-type","application/xhtml+xml");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();

        //SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd MMM yyyy HH:mm Z", Locale.US);

        List itemList = new ArrayList();

        if (request.getPathInfo().startsWith("/podcast/folder/")) {

            String tokens[] = request.getPathInfo().split("/");
            int folderId = Integer.parseInt(tokens[tokens.length-1]);

            MediaService imageService = new MediaService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            imageService.setUsedLanguage(lngResolver.resolveLng(request));
            loaderClass.setId(folderId);
            loaderClass.setOrderBy(Config.orderByFolder);
            loaderClass.setSortBy(Config.sortByFolder);
            itemList = imageService.getFolderMediaObjects(loaderClass);

            FolderService folderService = new FolderService();
            folderService.setUsedLanguage(lngResolver.resolveLng(request));
            try {
                Folder folder = new Folder();
                if (folderId!=0) {
                    folder = folderService.getFolderById(folderId);
                }
                //todo: cheat
                //Cheat: Description ;http://logourl/logo.gif wird dann also logo im podcast angezeigt
                String logoUrl = Config.instanceLogo;
                String description = folder.getDescription();
                String[] descriptionTokens = folder.getDescription().split(";http://");
                if (descriptionTokens.length>1) {
                    description = descriptionTokens[0];
                    logoUrl = "http://"+descriptionTokens[1];
                }

                writePodcastHeader(writer, httpBase,
                        folder.getFolderTitle(),description,"",logoUrl,true);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        Iterator folders = itemList.iterator();
        while (folders.hasNext()) {
            MediaObject mo = (MediaObject)folders.next();
                                          /* http://localhost:8080/index/image?id=9764 */
            writer.print("\n<item>\n");
            writer.print("<guid isPermaLink=\"false\">"+httpBase+"index/ppreview?id="+ mo.getIvid()+"</guid>\n");
            writer.print("<title>"+ mo.getVersionTitle().replace("&"," ")+"</title>\n");
            if (iTunesCompatible) {
                writer.print("<itunes:author>"+Config.webTitle+"</itunes:author>");
                writer.print("<itunes:subtitle>no subtitle</itunes:subtitle>");
                writer.print("<itunes:summary>summary</itunes:summary>");
                //Duration Datum-Format
                SimpleDateFormat ddf = new SimpleDateFormat("mm:ss");
                System.out.println("Duration long="+mo.getDuration());
                System.out.println("Duration Date="+mo.getDurationDate());
                writer.print("<itunes:duration>"+ddf.format(mo.getDurationDate())+"</itunes:duration>");
                writer.print("<itunes:keywords>no keywords</itunes:keywords>");
            }
            String description = mo.getNote().replace("&"," ");
            if (mo.getExtention().equalsIgnoreCase("JPG") || mo.getExtention().equalsIgnoreCase("JPEG")) {
                description = "<img src=\"/imageservlet/"+ mo.getIvid()+"/1/image.jpg\">";
            }
            writer.print("<description>\n");
            writer.print("\t<![CDATA[\n");
            writer.print("<p>"+description+"</p>\n");
            //writer.print("&lt;img src=&quot;")
            writer.print(" ]]>\n");
            writer.print("</description>\n");
            //if (mo.getPrimaryIvid()!=-1) {
                writer.print("<enclosure url=\""+httpBase+"podcast/object/"+ mo.getIvid()+"/"+getPodcastFilename(mo)+"\" length=\""+(mo.getKb()*1000)+"\" type=\""+ mo.getPrimaryMimeType()+"\"/>\n");
            //}
            writer.print("<link>"+httpBase+"index/ppreview?id="+ mo.getIvid()+"</link>\n");
            writer.print("<pubDate>"+sdf.format(mo.getCreateDate())+"</pubDate>\n");
            writer.print("</item>\n");
        }



        writer.print("</channel>\n");
        writer.print("</rss>\n");

        writer.flush();
        writer.close();
        
    }

    private String getPodcastFilename(MediaObject imageVersion) {

        if (imageVersion.getVersionName().length()>0) {
            String ext = "";
            if (!imageVersion.getVersionName().toUpperCase().endsWith("."+imageVersion.getExtention().toUpperCase())) { ext = "."+imageVersion.getExtention(); }
            return imageVersion.getVersionName()+ext;
        }
        if (imageVersion.getVersionTitle().length()>0) {
            String ext = "";
            if (!imageVersion.getVersionTitle().toUpperCase().endsWith("."+imageVersion.getExtention().toUpperCase())) { ext = "."+imageVersion.getExtention(); }
            return imageVersion.getVersionTitle()+ext; 
        }
        return "file";
    }

    private void writePodcastHeader(PrintWriter writer, String httpBase, String title, String description, String copyright, String logoUrl, boolean iTunesCompatible) {

        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        if (!iTunesCompatible) {
            writer.print("<rss version=\"2.0\">\n"); /*         xmlns:media=\"http://search.yahoo.com/mrss/\"\n" +
                "\txmlns:dc=\"http://purl.org/dc/elements/1.1/\" >\n");*/
        } else {
            writer.print("<rss version=\"2.0\" xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\">");
        }
        writer.print("<channel>\n");

        writer.print("<title>"+title+"</title>\n");
        writer.print("<link>"+httpBase+"</link>\n");
        writer.print("<description>"+description+"</description>\n");

          //writer.print("<generator>http://www.flickr.com/</generator>\n");

        writer.print("<language>de-de</language>\n");
        writer.print("<copyright>"+copyright+"</copyright>\n");
        writer.print("<image><url>"+logoUrl+"</url><title>logo</title><link>"+httpBase+"</link></image>\n");

        if (iTunesCompatible) {
            writer.print("<itunes:subtitle></itunes:subtitle>");
            writer.print("<itunes:author></itunes:author>");
            writer.print("<itunes:summary></itunes:summary>");

            writer.print("<itunes:owner>");
            writer.print("  <itunes:name>"+copyright+"</itunes:name>");
            writer.print("  <itunes:email>"+Config.mailsender+"</itunes:email>");
            writer.print("</itunes:owner>");
            writer.print("<itunes:image href=\""+logoUrl+"\" />");
            writer.print("<itunes:folder text=\"Technology\">");
            writer.print("  <itunes:folder text=\"Podcasting\"/>");
            writer.print("</itunes:folder>");
            //writer.print("<itunes:folder text=\"TV &amp; Film\"/>");


        }

    }




}
