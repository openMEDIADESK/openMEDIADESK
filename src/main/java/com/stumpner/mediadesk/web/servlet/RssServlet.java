package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.category.Category;
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

            if (httpServletRequest.getPathInfo().equalsIgnoreCase("/folder.js")) {
                this.serviceJS(httpServletRequest,httpServletResponse);
            }
            if (httpServletRequest.getPathInfo().equalsIgnoreCase("/folder.xml")) {
                this.serviceXML(httpServletRequest,httpServletResponse);
            }
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

            ImageVersionService imageService = new ImageVersionService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            loaderClass.setId(folderId);
            loaderClass.setOrderBy(Config.orderByFolder);
            loaderClass.setSortBy(Config.sortByFolder);
            itemList = imageService.getFolderImages(loaderClass);

            FolderService folderService = new FolderService();
            folderService.setUsedLanguage(lngResolver.resolveLng(request));
            try {
                Folder folder = folderService.getFolderById(folderId);
                writePodcastHeader(writer, httpBase,
                        folder.getFolderTitle(),folder.getFolderSubTitle(),"","",true);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //writePodcastHeader(writer, true);
            //FolderService folderService = new FolderService();
            //itemList = folderService.getFolderList(10);
        }
        if (request.getPathInfo().startsWith("/podcast/category/")) {

            String tokens[] = request.getPathInfo().split("/");
            int folderId = Integer.parseInt(tokens[tokens.length-1]);

            ImageVersionService imageService = new ImageVersionService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            imageService.setUsedLanguage(lngResolver.resolveLng(request));
            loaderClass.setId(folderId);
            loaderClass.setOrderBy(Config.orderByFolder);
            loaderClass.setSortBy(Config.sortByFolder);
            itemList = imageService.getCategoryImages(loaderClass);

            CategoryService categoryService = new CategoryService();
            categoryService.setUsedLanguage(lngResolver.resolveLng(request));
            try {
                Category category = new Category();
                if (folderId!=0) {
                    category = categoryService.getCategoryById(folderId);
                }
                //todo: cheat
                //Cheat: Description ;http://logourl/logo.gif wird dann also logo im podcast angezeigt
                String logoUrl = Config.instanceLogo;
                String description = category.getDescription();
                String[] descriptionTokens = category.getDescription().split(";http://");
                if (descriptionTokens.length>1) {
                    description = descriptionTokens[0];
                    logoUrl = "http://"+descriptionTokens[1];
                }

                writePodcastHeader(writer, httpBase,
                        category.getCatTitle(),description,"",logoUrl,true);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        Iterator folders = itemList.iterator();
        while (folders.hasNext()) {
            ImageVersion mo = (ImageVersion)folders.next();
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

    private String getPodcastFilename(ImageVersion imageVersion) {

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
            writer.print("<itunes:category text=\"Technology\">");
            writer.print("  <itunes:category text=\"Podcasting\"/>");
            writer.print("</itunes:category>");
            //writer.print("<itunes:category text=\"TV &amp; Film\"/>");


        }

    }

    protected void serviceXML(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        String httpBase = "http://";
        httpBase = httpBase + httpServletRequest.getServerName();
        if (httpServletRequest.getServerPort()!=80) {
            httpBase = httpBase + ":" + Integer.toString(httpServletRequest.getServerPort());
        }
        httpBase = httpBase + "/";


        httpServletResponse.setHeader("Content-type","application/xhtml+xml");
        httpServletRequest.setCharacterEncoding("ISO-8859-1");

        PrintWriter writer = httpServletResponse.getWriter();
        writer.print("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        writer.print("<rss version=\"2.0\">\n"); /*         xmlns:media=\"http://search.yahoo.com/mrss/\"\n" +
                "\txmlns:dc=\"http://purl.org/dc/elements/1.1/\" >\n");*/
        writer.print("<channel>\n");

        writer.print("<title>"+Config.webTitle+" RSS-Feed</title>\n");
        writer.print("<link>"+httpBase+"</link>\n");
        writer.print("<description>Serves the last Folders created with mediaDESK</description>\n");

          //writer.print("<generator>http://www.flickr.com/</generator>\n");

        writer.print("<language>de-de</language>\n");
        writer.print("<copyright>"+Config.webTitle+"</copyright>\n");
        writer.print("<image><url>"+Config.instanceLogo+"</url><title>logo</title><link>"+httpBase+"</link></image>\n");

        /*
        writer.print("\n<item>\n");
        writer.print("<title>t2</title>\n");
        writer.print("<link>http://www.flickr.com/photos/8903873@N03/1378294166/</link>\n");
        //writer.print("<description>abc</description>\n");
        writer.print("<description>&lt;p&gt;&lt;a href=&quot;http://www.flickr.com/people/familievanaalst/&quot;&gt;Familie van Aalst&lt;/a&gt; hat ein Foto gepostet:&lt;/p&gt; &lt;p&gt;&lt;a href=&quot;http://www.flickr.com/photos/familievanaalst/1377485535/&quot; title=&quot;IMG_1820&quot;&gt;&lt;img src=&quot;http://farm2.static.flickr.com/1276/1377485535_3bc7aa165e_m.jpg&quot; width=&quot;240&quot; height=&quot;180&quot; alt=&quot;IMG_1820&quot; /&gt;&lt;/a&gt;&lt;/p&gt;</description>\n");
        writer.print("<pubDate>Fri, 14 Sep 2007 00:26:17 -0800</pubDate>\n");
        writer.print("<dc:date.Taken>2007-09-14T00:26:17-08:00</dc:date.Taken>\n");
        writer.print("<author>nobody@flickr.com (lady_in_the_water1991)</author>\n");
        writer.print("<guid isPermaLink=\"false\">tag:flickr.com,2004:/photo/1378294166</guid>\n");
        writer.print("<media:content url=\"http://farm2.static.flickr.com/1051/1378294166_c9f2fdc414_o.jpg\" \n");
        writer.print("\t\t\t\t       type=\"image/jpeg\"\n");
        writer.print("\t\t\t\t       height=\"640\"\n");
        writer.print("\t\t\t\t       width=\"480\"/>\n");
        writer.print("\t\t\t<media:title>t2</media:title>\n");
        writer.print("<media:text type=\"html\">&lt;p&gt;&lt;a href=&quot;http://www.flickr.com/people/familievanaalst/&quot;&gt;Familie van Aalst&lt;/a&gt; hat ein Foto gepostet:&lt;/p&gt; &lt;p&gt;&lt;a href=&quot;http://www.flickr.com/photos/familievanaalst/1377485535/&quot; title=&quot;IMG_1820&quot;&gt;&lt;img src=&quot;http://farm2.static.flickr.com/1276/1377485535_3bc7aa165e_m.jpg&quot; width=&quot;240&quot; height=&quot;180&quot; alt=&quot;IMG_1820&quot; /&gt;&lt;/a&gt;&lt;/p&gt;</media:text>\n");
        writer.print("<media:thumbnail url=\"http://farm2.static.flickr.com/1051/1378294166_f53c274c76_s.jpg\" height=\"75\" width=\"75\" />\n");
        writer.print("<media:credit role=\"photographer\">lady_in_the_water1991</media:credit>\n");
        writer.print("</item>\n");
        */
        //folders:

        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd MMM yyyy kk:mm", Locale.US);
        FolderService folderService = new FolderService();
        List folderList = folderService.getFolderList(10);
        Iterator folders = folderList.iterator();
        while (folders.hasNext()) {
            Folder folder = (Folder)folders.next();

            writer.print("\n<item>\n");
            writer.print("<guid isPermaLink=\"false\">"+httpBase+"index/folder?id="+folder.getFolderId()+"</guid>\n");
            writer.print("<title>"+folder.getFolderTitle().replace("&"," ")+"</title>\n");
            writer.print("<description>\n");
            writer.print("\t<![CDATA[\n");
            writer.print("<p>"+folder.getFolderSubTitle().replace("&"," ")+"</p>\n");
            if (folder.getPrimaryIvid()!=-1) {
                writer.print("<img src=\""+httpBase+"imageservlet/"+folder.getPrimaryIvid()+"/1/image.jpg\""+"/>");
            }
            //writer.print("&lt;img src=&quot;")
            writer.print(" ]]>\n");
            writer.print("</description>\n");
            if (folder.getPrimaryIvid()!=-1) {
                //writer.print("<enclosure url=\""+httpBase+"imageservlet/"+folder.getPrimaryIvid()+"/1/image.jpg\" type=\"image/jpeg\"/>\n");
            }
            writer.print("<link>"+httpBase+"index/folder?id="+folder.getFolderId()+"</link>\n");
            writer.print("<pubDate>"+sdf.format(folder.getFolderDate())+"</pubDate>\n");
            writer.print("</item>\n");
        }



        writer.print("</channel>\n");
        writer.print("</rss>\n");

        writer.flush();
        writer.close();

        //super.service(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void serviceJS(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {




        httpServletResponse.setHeader("Content-type","text/html");

        PrintWriter writer = httpServletResponse.getWriter();
        //writer.print(" alert('included');");
        //writer.print("function susideSyndPic(folderPos,target,cssClass) {\n");
        //writer.print(" document.write(\"<a href='"+Config.httpBase+"folder?id=\"+folderId[folderPos]+\"'><img src='"+Config.httpBase+"/imageservlet/\"+folderIvid[folderPos]+\"'>\"\n");
        //writer.print(" alert('hallo');");
        //writer.print("}\n");
        /*
        writer.print("<title>Mein RSS-Feed</title>");
        writer.print("<link>http://www.suside.net</link>");
        writer.print("<description>Meine tolle site</description>");
        writer.print("<language>de-de</language>");
        writer.print("<copyright>2005 by suside.net</copyright>");
        writer.print("<image><url>"+Config.instanceLogo+"</url><title>logo</title><link>http://www.suside.net</link></image>");
        */

        //folders:
        FolderService folderService = new FolderService();
        List allFolderList = folderService.getFolderList(3);
        //only take folder who are empty
        List folderList = new LinkedList();
        Iterator allFolders = allFolderList.iterator();
        while (allFolders.hasNext()) {
            Folder folder = (Folder)allFolders.next();
            if (folder.getImageCount()>0) {
                folderList.add(folder);
            }
        }
        Iterator folders = folderList.iterator();
        writer.print("var folderImage = new Array("+folderList.size()+");\n");
        writer.print("var folderLink = new Array("+folderList.size()+");\n");
        writer.print("var folderText = new Array("+folderList.size()+");\n");
        while (folders.hasNext()) {
            Folder folder = (Folder)folders.next();

           writer.print("folderImage["+folderList.indexOf(folder)+"] = \""+Config.httpBase+"imageservlet/"+folder.getPrimaryIvid()+"/1/image.jpg\";\n");
           writer.print("folderLink["+folderList.indexOf(folder)+"] = \""+Config.httpBase+"index/folder?id="+folder.getFolderId()+"\";\n");
           writer.print("folderText["+folderList.indexOf(folder)+"] = \""+(folder.getFolderTitle()).replaceAll("\"","&quot;")+"\";\n");

           //writer.print("folderSubTitle["+folderList.indexOf(folder)+"] = "+folder.getFolderSubTitle()+";\n");

        }


        writer.flush();
        writer.close();

        //super.service(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }



}
