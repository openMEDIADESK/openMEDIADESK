package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.core.database.sc.MediaMetadataService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.media.image.util.SizeExceedException;
import com.stumpner.mediadesk.media.image.util.ImageMagickUtil;
import com.stumpner.mediadesk.media.image.util.IImageUtil;
import com.stumpner.mediadesk.media.Metadata;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import com.stumpner.mediadesk.util.UploadNotificationService;
import com.stumpner.mediadesk.upload.FileRejectException;

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
 * Date: 03.12.2011
 * Time: 18:10:43
 * To change this template use File | Settings | File Templates.
 */
public class VideoImportHandler extends AbstractAudioVideoImportHandler {

    int frame = 10; // Frame des Videos der als Bild exportiert werden soll

    public int processImport(File srcFile, int userId) throws SizeExceedException, FileRejectException {

        //todo: File/Video Metadaten auslesen via: http://www.longtailvideo.com/support/forums/jw-player/setup-issues-and-embedding/9448/how-to-get-video-duration-with-ffmpeg-and-php
        //und mit ffmpeg -i <mp3file> -f ffmetadata metadata.txt

        //System.out.println("Import video File:" +srcFile.getAbsolutePath());

        //todo: Video in mp4 konvertieren
        /*
        unterst�tzte formate von ffmpeg mit: ffmpeg -codecs
        convertieren mit:
        + ffmpeg -vcodec mpeg2video -i MOV06948.MPG testing.mp4
        + ffmpeg -vcodec mpeg4 -i MOV06948.MPG testing.mp4
        + ffmpeg -i MOV06948.MPG -f mp4 testing.mp4
        + ffmpeg -i MOV06948.MPG -f mpeg2video testing.mp4 (funktioniert auch auf linux)
        + ffmpeg -i MOV06948.MPG -f mov testing.mp4 (funktioniert auch auf linux)
        * Was funktioniert: zuerst auf mpeg2video und dann auf mp4 konvertieren
         */

        /*
        Video Formate im JW player:
        http://www.longtailvideo.com/support/jw-player/jw-player-for-flash-v5/12539/supported-video-and-audio-formats
         */

        String fileVideoFrameExtract = srcFile.getPath() + System.currentTimeMillis()+ "_frameextract.jpg";
        String fileVideoThumbnail_1 = srcFile.getPath() + System.currentTimeMillis()+ "_videothumb_1.jpg";
        String fileVideoThumbnail_2 = srcFile.getPath() + System.currentTimeMillis() + "_videothumb_2.jpg";

        FileInfo fileInfo = readFileInfo(srcFile);
        long videoDuration = getDuration(fileInfo);
        int bitrate = getBitrate(fileInfo);
        int sampleRate = getSampleRate(fileInfo);
        int channels = getChannels(fileInfo);
        List<Metadata> metadataList = getMetadataList(fileInfo);
        String videocodec = getVideoCodec(fileInfo);

        /// Video -> VideoFrameExtract
        Logger logger = Logger.getLogger(VideoImportHandler.class);
        try
        {
            Runtime rt = Runtime.getRuntime();
            String command = executeCommand+" convert -quiet "+srcFile+"["+frame+"] -resize "+ Config.imagesizeThumbnail+" "+fileVideoThumbnail_1+" 2>magicklog";
            if (true) {
                //mit ffmpeg: http://flowplayer.org/tutorials/generating-thumbs.html
                //old at n20111230: command = executeCommand+" ffmpeg -ss "+frame+" -vf scale="+Config.imagesizeThumbnail+":-1 -i "+srcFile+" -f image2 -vframes 1 "+fileVideoThumbnail_1+" 2>&1";
                command = executeCommand+" ffmpeg -ss "+frame+" -i "+srcFile+" -f image2 -vframes 1 "+fileVideoFrameExtract+this.trick;
                //command = executeCommand+" ffmpeg -ss 100 -i cher.mp4 -f image2 -vframes 1 thumb3.jpg"
            }
            //System.out.println(command);
            Process proc = rt.exec(command);
            //System.out.println("getInputStream");
            InputStream is = proc.getInputStream();
            //while(is.read())
            String string = new String();
            StringBuffer stdOutSb = new StringBuffer("");
               byte[] buf = new byte[32];
               int len;
               while ((len = is.read(buf)) > 0) {
                   string = new String(buf);
                   //System.out.println(string);
                   stdOutSb = stdOutSb.append(string);
                 //out.write(buf, 0, len);
               }
            //System.out.println(stdOutSb.toString());
            is.close();

            InputStream es = proc.getErrorStream();
            logger.error("ERROR:");
            //while(is.read())
               byte[] esbuf = new byte[4096];
               int eslen;
               while ((eslen = es.read(esbuf)) > 0) {
                   string = new String(esbuf);
                   logger.error("ERRORBUF: "+string);
                   //System.out.println(string);
                 //out.write(buf, 0, len);
               }
            is.close();

            proc.waitFor();
            int exitVal = proc.exitValue();
            //System.out.println("BasicMediaObject Magick Video-Process exitValue: " + exitVal);
// copy to
//            Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1"

        } catch (Exception e)
      {
        e.printStackTrace();
      }

        //System.out.println("validateFile Import...");
        int ivid = super.processImport(srcFile, userId);
        //System.out.println("new ivid="+ivid);

        //Bild-Thumbnails erstellen
        //Horizontal
        IImageUtil imageUtil = new ImageMagickUtil(true);

        int videoHeight = imageUtil.getImageHeight(fileVideoFrameExtract);
        int videoWidth = imageUtil.getImageWidth(fileVideoFrameExtract);

        //images verkleinern horizontal
        imageUtil.resizeImageHorizontal(
                fileVideoFrameExtract,Config.imageStorePath+File.separator+ivid+"_1",Config.imagesizeThumbnail);
        imageUtil.resizeImageHorizontal(
                fileVideoFrameExtract,Config.imageStorePath+File.separator+ivid+"_2",Config.imagesizePreview);
        imageUtil.overlayWatermark(Config.imageStorePath+File.separator+ivid+"_2",Config.watermarkHorizontal);

        //Video mit VLC in verschiedenen Codecs konvertieren:
        /*
        _4 = mp4
        _5 = mpeg2
         */
        //Runterreichnen
        if (Config.streamConvertToKbitSek>0) {
            //convertVideoTo(5,Config.imageStorePath+File.separator+ivid+"_0",Config.imageStorePath+File.separator+ivid+"_5");
            convertVideoTo(4,Config.imageStorePath+File.separator+ivid+"_0",Config.imageStorePath+File.separator+ivid+"_4");
        }

        File videoFrameExtract = new File(fileVideoFrameExtract);

        //System.out.println("delte video-extracted file");
        videoFrameExtract.delete();
        //videoThumbnailTmp1.delete();
        //videoThumbnailTmp2.delete();

        //Metadaten schreiben:
        MediaMetadataService mediaMetadataService = new MediaMetadataService();
        MediaService mediaService = new MediaService();
        MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)mediaService.getMediaObjectById(ivid);

        for (Metadata metadata : metadataList) {

            metadata.setIvid(mediaObject.getIvid());
            metadata.setLang("");
            metadata.setVersionId(mediaObject.getVersion());
            mediaMetadataService.addMetadata(metadata);

            if (metadata.getMetaKey().equalsIgnoreCase("artist")) {
                mediaObject.setArtist(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase("title")) {
                mediaObject.setVersionTitle(metadata.getMetaValue());
                mediaObject.setVersionTitleLng1(metadata.getMetaValue());
                mediaObject.setVersionTitleLng2(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase("genre")) {
                mediaObject.setGenre(metadata.getMetaValue());
            }
            if (metadata.getMetaKey().equalsIgnoreCase("album")) {
                mediaObject.setAlbum(metadata.getMetaValue());
            }
        }

        mediaObject.setVideocodec(videocodec);
        mediaObject.setDuration(videoDuration);
        mediaObject.setBitrate(bitrate);
        mediaObject.setSamplerate(sampleRate);
        mediaObject.setChannels(channels);
        mediaObject.setHeight(videoHeight);
        mediaObject.setWidth(videoWidth);
        try {
            mediaService.saveMediaObject(mediaObject);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        UploadNotificationService uns = new UploadNotificationService();
        uns.triggerUpload(ivid);

        return ivid;
    }

    //Video in verschiedenen Codecs konvertieren:
    /*
    _4 = mpeg4
    _5 = mpeg2
     */
    private void convertVideoTo(int type, String in, String out) {

        /*
        + ffmpeg -i MOV06948.MPG -f mpeg2video testing.mp4 (funktioniert auch auf linux)
        * Was funktioniert: zuerst auf mpeg2video und dann auf mp4 konvertieren
         */

        final int CONVERTER_VLC = 1;
        final int CONVERTER_FFMPEG = 2;

        String codec = "";
        switch (type) {
            case 4: codec = "mp4"; break;
            case 5: codec = "mpeg2video"; break;
        }

        try
        {
            Runtime rt = Runtime.getRuntime();
            int userConverter = CONVERTER_VLC;
            String command = "";
            if (userConverter == CONVERTER_FFMPEG) {
                command = executeCommand+" ffmpeg -i "+in+" -f "+codec+" "+out+""+this.trick;
            }
            if (userConverter == CONVERTER_VLC) {

                if (true) {
                    //nur video codieren, audio kopieren
                    command = executeCommand+" vlc "+in+" :sout='#transcode{vcodec=h264,vb="+Config.streamConvertToKbitSek+",scale=0}:std{dst=\""+out+"\"}' vlc:quit";
                } else {
                    //audio + video codieren
                    command = executeCommand+" vlc "+in+" :sout='#transcode{vcodec=h264,vb=0,scale=0,acodec=mp4a,ab=128,channels=2,samplerate=44100}:std{dst=\""+out+"\"}' vlc:quit";
                }

            }

            //System.out.println(command);
            Process proc = rt.exec(command);

            //System.out.println("encode video");
             InputStream is = proc.getInputStream();
             //while(is.read())
             String string = new String();
             StringBuffer stdOutSb = new StringBuffer("");
                byte[] buf = new byte[32];
                int len;
                while ((len = is.read(buf)) > 0) {
                    string = new String(buf);
                    //System.out.println(string);
                    stdOutSb = stdOutSb.append(string);
                  //out.write(buf, 0, len);
                }
             //System.out.println(stdOutSb.toString());
             is.close();

             InputStream es = proc.getErrorStream();
             logger.error("ERROR:");
             //while(is.read())
                byte[] esbuf = new byte[4096];
                int eslen;
                while ((eslen = es.read(esbuf)) > 0) {
                    string = new String(esbuf);
                    logger.error("ERRORBUF: "+string);
                    //System.out.println(string);
                  //out.write(buf, 0, len);
                }
             is.close();


            proc.waitFor();
            int exitVal = proc.exitValue();
            //System.out.println("Video-Encode exitValue: " + exitVal);
// copy to
//            Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1"

        } catch (Exception e)
      {
        e.printStackTrace();
      }


    }

}
