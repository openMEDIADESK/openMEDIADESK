package com.stumpner.mediadesk.web.servlet;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.usermanagement.acl.AclUtil;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.folder.Folder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.security.acl.AclNotFoundException;
import java.text.SimpleDateFormat;

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
 *
 * Unterst�tzung von Rage Requests (aber keine Multipart Range Requests)
 * Quelle/Information:
 * <ul>
 *  <li>http://benramsey.com/blog/2008/05/206-partial-content-and-range-requests/</li>
 *  <li>http://balusc.blogspot.co.at/2009/02/fileservlet-supporting-resume-and.html</li>
 * </ul>
 *
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 29.12.2011
 * Time: 10:25:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStreamServlet extends HttpServlet {

    private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.

    /**
     * Gibt das Servlet-Mapping (z.b. /servlet) zur�ck
     * @return
     */
    abstract protected String getServletMapping();

    /**
     * Download Type von
     *       DownloadLoggerService dlls2 = new DownloadLoggerService();
     *       dlls2.log(0,0, null,
     *               SimpleDownloadLogger.DTYPE_PODCAST, request,0);
     * @return
     */
    abstract protected int getDownloadType();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MediaObject imageVersion = resolveMediaobjectInRequest(request, response);
        if (imageVersion!=null) {
            download(imageVersion,request,response);
        } else {
            if (!response.isCommitted()) {
                response.sendError(404,"Could not resolve Mediaobject in Request");
            }
        }

    }


    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MediaObject imageVersion = resolveMediaobjectInRequest(request, response);
        if (imageVersion!=null) {
            sendResponseHeader(response,imageVersion,true,null);
        } else {
            if (!response.isCommitted()) {
                response.sendError(404,"Could not resolve Mediaobject in Request");
            }
        }

    }

    /**
     * Ermittelt das gew�nschet Medienobjekt im Request
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    private MediaObject resolveMediaobjectInRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

            //Berechtigungen f�r Podcast
            AclControllerContext aclContext = AclContextFactory.getAclContext(httpServletRequest);

            String[] urlTokens = httpServletRequest.getRequestURI().split("/");
            //System.out.println(getServletMapping()+"-Request: "+httpServletRequest.getRequestURI());
            if (urlTokens.length<4) {
                //Falsche Benutzung, URL muss aus 3 Tokens bestehen, wahlweise auch aus 4 (Dateiname):
                // /podcast/folder/1
                //1:2      :3       :4

                // /podcast/folder/1/dateiname.zip
                //1:2      :3       :4:5
                //PrintWriter writer = httpServletResponse.getWriter();
                //writer.print("Syntax Error: use: /podcast/folder/1 oder /podcast/folder/1/name.zip");
                httpServletResponse.sendError(400,"Syntax Error: use: "+getServletMapping()+"/folder/1 oder "+getServletMapping()+"/folder/1/name.zip");
                return null;
            } else {

                String type = urlTokens[2];
                String idString = urlTokens[3];
                String ext = "";

                if (idString.contains(".")) {
                    //Wenn eine Extention angegeben ist, dann diese f�r die ID wegschneiden
                    String idStringTokens[] = idString.split("\\.");
                    idString = idStringTokens[0];
                }

                try {
                    int id = Integer.parseInt(idString);

                    /*
                    writer.print("Token1: "+urlTokens[1]);
                    writer.print("Token2: "+urlTokens[2]);
                    writer.print("Token3: "+urlTokens[3]);
                    */
                    MediaService imageService = new MediaService();
                    LngResolver lngResolver = new LngResolver();
                    imageService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));

                    if (type.equalsIgnoreCase("folder")) {

                        FolderService folderService = new FolderService();
                        Folder folder = folderService.getFolderById(id);
                        aclContext.setDebug(true);

                        //Berechtigung pr�fen:
                        if (aclContext.checkPermission(new AclPermission("view"), folder) ||
                            aclContext.checkPermission(new AclPermission("read"), folder)) {

                            SimpleLoaderClass loader = new SimpleLoaderClass();
                            loader.setId(id);
                            loader.setOrderBy(Config.orderByFolder);
                            loader.setSortBy(Config.sortByFolder);
                            List imageList = imageService.getFolderMediaObjects(loader);
                            if (imageList.size()>0) {
                                //Pr�fen ob explizit ein Name in der Kategorie angegeben wurde, oder
                                //das aktuellste File geladen werden soll (kein name angegeben)
                                if (urlTokens.length==5) {
                                    MediaObject image = searchForName(imageList,urlTokens[4]);
                                    if (image!=null) {
                                        //download(image,httpServletRequest,httpServletResponse);
                                        return image;
                                    } else {
                                        httpServletResponse.sendError(404);
                                        return null;
                                    }
                                } else {
                                    //download((MediaObject)imageList.get(0),httpServletRequest,httpServletResponse);
                                    return (MediaObject)imageList.get(0);
                                }
                            } else {
                                //System.err.println("Folder "+id+" does not contain any mediafiles");
                                httpServletResponse.sendError(404,"Folder "+id+" does not contain any mediafiles");
                                return null;
                            }
                        } else {
                            //keine Berechtigung
                            httpServletResponse.sendError(403,"No Visitor Access (ACL) for FolderId="+id);
                            return null;
                        }
                    } else if (type.equalsIgnoreCase("object")) {
                        //Die URL kann entweder /podcast/object/#id#
                        //oder /podcast/object/#id#.#extention# sein

                        MediaObject imageVersion = imageService.getMediaObjectById(id);

                        if (imageVersion!=null) {
                            //check Permission
                            List downloadImages = new LinkedList();
                            downloadImages.add(imageVersion);
                            List permittedImages = AclUtil.getPermittedMediaObjects(aclContext, downloadImages, "view");
                            if (permittedImages.size()>0) {
                                //download(imageVersion,httpServletRequest,httpServletResponse);
                                return imageVersion;
                            } else {
                                httpServletResponse.sendError(403,"No Visitor Access (ACL) for objectId="+id);
                                return null;
                            }

                        } else {
                            //Wenn das mediaFile nicht existiert:
                            httpServletResponse.sendError(404,"mediaId="+id+" not found");
                            return null;

                        }
                    }
                } catch (NumberFormatException e) {
                    httpServletResponse.sendError(400,"ID as Number-Value expected /podcast/$type/$id");
                    return null;
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (AclNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        return null;
    }

    private MediaObject searchForName(List imageList, String name) {

        Iterator images = imageList.iterator();

        while (images.hasNext()) {
            MediaObject image = (MediaObject)images.next();
            if (image.getVersionName().equalsIgnoreCase(name)) {
                return image;
            }
        }

        return null;
    }

    private void download(MediaObject imageVersion, HttpServletRequest request, HttpServletResponse response) {

        String filename = getStreamSourceFilename(imageVersion);

        List<DownloadRange> downloadRange = getDownloadRange(request, imageVersion);
        sendResponseHeader(response, imageVersion, true, downloadRange);
        int sentBytes = 0;

        OutputStream outputStream = null;
        RandomAccessFile inputStream = null;

        try {
            outputStream = response.getOutputStream();
            //System.out.println("outputStream");
            inputStream = new RandomAccessFile(filename, "r");
            //System.out.println("inputStream ge�ffnet: "+filename);

            trackStreamStart(request, imageVersion, getDownloadType());

            long startBytes = 0;
            long lengthBytes = getSourceFile(imageVersion).length();

            //Download-Range: "nach vor spulen"
            if (downloadRange!=null) {
                if (downloadRange.size()==1) {
                    DownloadRange range = downloadRange.get(0);
                    startBytes = range.start;
                    lengthBytes = range.length;
                } else {
                    //todo: multi byte request
                }
            }

            sentBytes = writeStream(imageVersion, inputStream, outputStream, startBytes, lengthBytes);

        } catch (IOException e) {
            System.err.println("AbstractStreamServlet IOException: "+e.getMessage());
        } finally {
           if (inputStream!=null) {
                try {
                    inputStream.close();
                    //System.out.println("inputStream geschlossen:" +filename);
                } catch (IOException e) {
                    System.err.println("AbstractStreamServlet inputStream kann nicht geschlossen werden: "+e.getMessage());
                }
            }
            /* ist nicht n�tig
            if (outputStream!=null) {
                try {
                    outputStream.close();
                    System.out.println("outputStream geschlossen");
                } catch (IOException e) {
                    System.err.println("AbstractStreamServlet outputStream kann nicht geschlossen werden: "+e.getMessage());
                }
            }
            */
        }

        trackStreamEnd(request, imageVersion, getDownloadType(),sentBytes);

    }

    /**
     * Schreibt den Input Stream mit den angegebenen Parametern in den Output Stream
     * @param input
     * @param output
     * @param startBytes
     * @param lengthBytes
     */
    private int writeStream(MediaObject imageVersion, RandomAccessFile input, OutputStream output, long startBytes, long lengthBytes) throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        int sentBytes = 0;

        if (input.length() == lengthBytes) {
            // Write full range.
            while ((read = input.read(buffer)) > 0) {
                sentBytes = sentBytes+read;
                output.write(buffer, 0, read);
            }
        } else {
            // Write partial range.
            input.seek(startBytes);
            long toRead = lengthBytes;

            while ((read = input.read(buffer)) > 0) {
                if ((toRead -= read) > 0) {
                    sentBytes = sentBytes+read;
                    output.write(buffer, 0, read);
                } else {
                    sentBytes = sentBytes+read;
                    output.write(buffer, 0, (int) toRead + read);
                    break;
                }
            }
        }

        /*
        int sleep = getSleepInMsAfter1024bytes(imageVersion);
        int sentBytes = 0;
        byte b[] = new byte[51024];
        int len=0;
        while((len=inputStream.read(b)) != -1) {

            if (sleep>0 && sentBytes>50000) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            sentBytes = sentBytes+len;
            System.out.println("sent: "+sentBytes+" Bytes");
            outputStream.write(b,0,len);
        } */

        return sentBytes;
    }

    /**
     * Lest vom Request-Header die Range aus (Partial Content)
     * @param request
     * @param mediaObject
     * @return Download Range, wenn kein Range Request stattfindet wird null zur�ckgegeben
     */
    private List<DownloadRange> getDownloadRange(HttpServletRequest request, MediaObject mediaObject) {

        String range = request.getHeader("Range");
        if (range==null) { return null; } //Null zur�ckliefern, wenn es kein byterange-request ist

        List<DownloadRange> downloadRangeList = new ArrayList<DownloadRange>();
        long length = (int)getSourceFile(mediaObject).length();


        // Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
        if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
            //response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
            //response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            //return;
            return null; //noch nicht implementiert
        }

        for (String part : range.substring(6).split(",")) {
            // Assuming a file with length of 100, the following examples returns bytes at:
            // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
            long start = sublong(part, 0, part.indexOf("-"));
            long end = sublong(part, part.indexOf("-") + 1, part.length());

            if (start == -1) {
                start = length - end;
                end = length - 1;
            } else if (end == -1 || end > length - 1) {
                end = length - 1;
            }

            // Check if Range is syntactically valid. If not, then return 416.
            if (start > end) {
                //response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
                //response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                //return;
                return null;
            }

            downloadRangeList.add(new DownloadRange(start,end,length));
        }

        return downloadRangeList;
    }

    /**
     * Gibt das File-Object vom medienObjekt zur�ck. Das File Objekt repr�sentiert die Originaldatei im Filesystm des Servers
     * @param mediaObject
     * @return
     */
    private File getSourceFile(MediaObject mediaObject) {

        String filename = getStreamSourceFilename(mediaObject);
        File file = new File(filename);
        return file;
    }

    private String getDownloadFilename(MediaObject mediaObject) {

        String downloadFilename = mediaObject.getVersionTitle();
        if (!downloadFilename.endsWith(mediaObject.getExtention())) {
            downloadFilename = downloadFilename + "." + mediaObject.getExtention();
        }

        return downloadFilename;
    }

    /**
     * Schickt die HTTP Header an den Client
     * @param response
     * @param mediaObject
     * @param fileAsAttachment Ob das File als Attachment geschickt werden soll "Content-Disposition: attachment
     */
    private void sendResponseHeader(HttpServletResponse response, MediaObject mediaObject, boolean fileAsAttachment, List<DownloadRange> downloadRange) {

        String downloadFilename = getDownloadFilename(mediaObject);

        /*
                httpServletResponse.setHeader("Content-Description","File Transfer");
                httpServletResponse.setHeader("Cache-Control","no-store");     //HTTP 1.1
                httpServletResponse.setHeader("Pragma","no-cache"); //HTTP 1.0
                httpServletResponse.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                httpServletResponse.addHeader("Content-Length", Long.toString(zipTmpFile.length()));
                httpServletResponse.setContentType("application/zip");
                httpServletResponse.setHeader("Content-Disposition","attachment; filename=\"download.zip\"");
         */

        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Content-Description","File Transfer");
        response.setHeader("Cache-Control","no-store");     //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
        response.setContentType(mediaObject.getMimeType());
        if (downloadRange==null) {
            //Standardvorgang: ohne Byte Range Request
            //alt (2GB Bug) response.setContentLength((int)getSourceFile(mediaObject).length());
            response.addHeader("Content-Length", Long.toString(getSourceFile(mediaObject).length()));
        } else {

            if (downloadRange.size() == 1) {
                DownloadRange range = downloadRange.get(0);
                response.setHeader("Content-Range", "bytes "+ range.start + "-" +range.end + "/" +range.total);
                // (2GB Bug) response.setContentLength((int)range.length);
                response.addHeader("Content-Length", Long.toString(range.length));
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            } else {
                //todo: multipart byteranges
            }
        }
        System.out.println("Serving Stream with Mime Type: "+mediaObject.getMimeType()+", Filename: "+downloadFilename);
        response.setContentType(mediaObject.getMimeType());
        response.setHeader("Content-Disposition","attachment; filename=\""+downloadFilename+"\"");
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        response.setHeader("Last-Modified",df.format(mediaObject.getCreateDate())); //todo: nicht optimal, da die datei �ber webdav ge�ndert werden kann

    }

    protected String getStreamSourceFilename(MediaObject imageVersion) {

        return Config.imageStorePath+"/"+imageVersion.getIvid()+"_0";

    }

    protected void trackStreamStart(HttpServletRequest request, MediaObject imageVersion, int downloadType) {

            //Tracking
            trackStream(request, imageVersion, downloadType, 0);

    }

    protected void trackStreamEnd(HttpServletRequest request, MediaObject imageVersion, int downloadType, int bytes) {

        //Stream End is by default not tracked
    }

    protected void trackStream(HttpServletRequest request, MediaObject imageVersion, int downloadType, int bytes) {

            //Tracking
            DownloadLoggerService dlls2 = new DownloadLoggerService();
            dlls2.log(WebHelper.getUser(request).getUserId(),imageVersion.getIvid(), null,
                    downloadType, request, bytes);

    }

    protected int getSleepInMsAfter1024bytes(MediaObject imageVersion) {
        return 0;
    }

    /**
     * Returns a substring of the given string value from the given begin index to the given end
     * index as a long. If the substring is empty, then -1 will be returned
     * @param value The string value to return a substring as long for.
     * @param beginIndex The begin index of the substring to be returned as long.
     * @param endIndex The end index of the substring to be returned as long.
     * @return A substring of the given string value as long or -1 if substring is empty.
     */
    private static long sublong(String value, int beginIndex, int endIndex) {
        String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }

    /**
     * This class represents a byte range.
     */
    protected class DownloadRange {
        long start;
        long end;
        long length;
        long total;

        /**
         * Construct a byte range.
         * @param start Start of the byte range.
         * @param end End of the byte range.
         * @param total Total length of the byte source.
         */
        public DownloadRange(long start, long end, long total) {
            this.start = start;
            this.end = end;
            this.length = end - start + 1;
            this.total = total;
        }

    }

}
