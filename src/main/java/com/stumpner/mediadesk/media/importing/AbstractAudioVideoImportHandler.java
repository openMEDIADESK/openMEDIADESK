package com.stumpner.mediadesk.media.importing;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.TimeZone;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

import com.stumpner.mediadesk.image.Metadata;

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
 * Basisklasse f�r Audio und Video Imports.
 *
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 28.12.2011
 * Time: 07:19:35
 * To change this template use File | Settings | File Templates.
 */
public class AbstractAudioVideoImportHandler extends BinaryFileImportHandler {

    Logger logger = Logger.getLogger(AbstractAudioVideoImportHandler.class);

    public class FileInfo {
        public String commandOutput = "";
    }

    String executeCommand = "";
    String trick = "";

    public AbstractAudioVideoImportHandler() {

        if (System.getenv("OS")!=null) {
            if (System.getenv("OS").indexOf("Windows")!=-1) {
                this.executeCommand = "cmd /c";
                this.trick = " 2>&1";
            }
        }

    }

    /**
     * Liest die DateiInfos aus
     * @param srcFile
     */
    protected FileInfo readFileInfo(File srcFile) {

        //http://www.longtailvideo.com/support/forums/jw-player/setup-issues-and-embedding/9448/how-to-get-video-duration-with-ffmpeg-and-php
        ////und mit ffmpeg -i <mp3file> -f ffmetadata metadata.txt

        FileInfo fileInfo = new FileInfo();
        long duration = -1;
        Logger logger = Logger.getLogger(AbstractAudioVideoImportHandler.class);
        File metadataFile = new File(srcFile.toString()+".metadata");
        try
        {
            Runtime rt = Runtime.getRuntime();
            String command = "";//executeCommand+" convert -quiet "+srcFile+"["+frame+"] -resize "+ Config.imagesizeThumbnail+" "+fileVideoThumbnail_1+" 2>magicklog";
            if (true) {
                //mit ffmpeg: http://flowplayer.org/tutorials/generating-thumbs.html
                command = executeCommand+" ffmpeg -i "+srcFile+" -f ffmetadata "+metadataFile+" 2>&1";
            }
            //System.out.println(command);
            Process proc = rt.exec(command);
            //System.out.println("getDuration");
            InputStream is = proc.getInputStream();
            //while(is.read())
            String string = new String();
            StringBuffer stdOutSb = new StringBuffer("");
               byte[] buf = new byte[256];
               int len;
               while ((len = is.read(buf)) > 0) {
                   string = new String(buf);
                   //System.out.print(".");
                   //System.out.println(string);
                   stdOutSb = stdOutSb.append(string);
                 //out.write(buf, 0, len);
               }
            System.out.println(stdOutSb.toString());
            is.close();

            InputStream es = proc.getErrorStream();
            StringBuffer errorSb = new StringBuffer();
            //while(is.read())
               byte[] esbuf = new byte[4096];
               int eslen;
               while ((eslen = es.read(esbuf)) > 0) {
                   string = new String(esbuf);
                   errorSb.append(string);
                   //logger.error("ERRORBUF: "+string);
                   //System.out.println(string);
                 //out.write(buf, 0, len);
               }
            is.close();

            proc.waitFor();
            int exitVal = proc.exitValue();
            //System.out.println("GetVideoDuration exitValue: " + exitVal);


            String commandOutput = stdOutSb.toString();
            if (commandOutput.trim().length()==0) {
                //commandOutput leer den Error Output nehmen. Unter Linux wird n�mlich nichts auf Std out geschrieben (bug von ffmpeg?)
                logger.error("ErrorStream wird als CommandOutput verwendet!");
                commandOutput = errorSb.toString();
            }
            fileInfo.commandOutput = commandOutput;

            if (metadataFile.exists()) { metadataFile.delete(); }
// copy to
//            Config.imageStorePath+File.separator+imageVersion.getIvid()+"_1"

        } catch (Exception e)
      {
        e.printStackTrace();
      }

        return fileInfo;

    }

    protected List<Metadata> getMetadataList(FileInfo srcFile) {

        List<Metadata> metadataList = new LinkedList();
        int metadataStart = srcFile.commandOutput.indexOf("Metadata:");
        int metadataEnd = srcFile.commandOutput.indexOf("Duration:");
        try {
            if (metadataStart>=0 && metadataEnd>=0) {
                //TODO: In der ffmpeg version 2.8.11 kommen die Metadaten anders daher
                String metadataBulkString = srcFile.commandOutput.substring(metadataStart+"Metadata:".length(),metadataEnd);
                String[] metadataLines = metadataBulkString.split("\\n");
                for (String metadataLine : metadataLines) {
                    String[] metadataArray = metadataLine.split(":");
                    Metadata metadata = new Metadata();
                    if (metadataArray.length>=2) {
                        metadata.setMetaKey(metadataArray[0].trim());
                        metadata.setMetaValue(metadataArray[1].trim());
                        metadata.setExifTag(false);
                        metadataList.add(metadata);
                    }
                }
            } else {
                System.err.println("Parse-Fehler bei getMetadataList:");
                System.out.println("AbstractAudioVideoImportHandler.getMetadataList() commandOutput="+srcFile.commandOutput);
                System.out.println("metadataStart="+metadataStart);
                System.out.println("metadataEnd="+metadataEnd);
            }
        } catch (StringIndexOutOfBoundsException e) {
                System.err.println("Parse-Fehler bei getMetadataList:");
                System.out.println("AbstractAudioVideoImportHandler.getMetadataList() commandOutput="+srcFile.commandOutput);
                System.out.println("metadataStart="+metadataStart);
                System.out.println("metadataEnd="+metadataEnd);
            e.printStackTrace();
        }

        return metadataList;
    }

    /**
     * gibt die Bitrate in kb/s zur�ck
     * @param srcFile
     * @return
     */
    protected int getBitrate(FileInfo srcFile) {

        //StreamLine = Duration: 00:04:02.20, start: 0.000000, bitrate: 454 kb/s
        //                                                     <bitrate>
        //                                  0, 1             , 2
        String streamLine = getStringBetween(", bitrate:","kb/s",srcFile.commandOutput);
        String bitrateString = streamLine.trim();

        int bitrate = -1;
        bitrate = Integer.parseInt(bitrateString);

        //System.out.println("getBitrate-String: "+streamLine);

        return bitrate;

    }

    protected int getChannels(FileInfo srcFile) {

        //StreamLine = Stream #0.0: Audio: mp3, 44100 Hz, 2 channels, s16, 256 kb/s
        //                                      <samplerate>,<channels>,<?>,<bitrate>
        //oder
        //StreamLine = Stream #0.1(eng): Video: h264, yuv420p, 352x270 [PAR 1:1 DAR 176:135], 359 kb/s, 29.97 fps, 29.97 tbr, 90k tbn, 59.94 tbc
        //oder unter linux
        //StreamLine = Stream #0.0(eng): Audio: mp4a / 0x6134706D, 44100 Hz, stereo
        //                                  0 , 1       , 2         , 3  , 4
        int channels = 0;

        String streamLine = getStreamLine(srcFile.commandOutput,"Audio");

        if (streamLine!=null) {
            //System.out.println("StreamLine getChannels: "+streamLine);
            String[] streamLineArray = streamLine.split(",");


            if (streamLineArray.length>=3) {
                String channelsString = streamLineArray[2].trim();
                if (channelsString.equalsIgnoreCase("mono")) { channels = 1; }
                if (channelsString.equalsIgnoreCase("stereo")) { channels = 2; }
                if (channelsString.equalsIgnoreCase("1 channel")) { channels = 2; }
                if (channelsString.equalsIgnoreCase("2 channels")) { channels = 2; }
            }
        } else {
            //System.out.println("StreamLine getChannels is null");
        }

        return channels;

    }

    //todo:
    protected String getVideoCodec(FileInfo fileInfo) {

        //StreamLine = Stream #0.0: Audio: mp3, 44100 Hz, 2 channels, s16, 256 kb/s
        //                                      <samplerate>,<channels>,<?>,<bitrate>
        //oder
        //StreamLine = Stream #0.1(eng): Video: h264, yuv420p, 352x270 [PAR 1:1 DAR 176:135], 359 kb/s, 29.97 fps, 29.97 tbr, 90k tbn, 59.94 tbc
        //oder unter linux
        //StreamLine = Stream #0.0(eng): Audio: mp4a / 0x6134706D, 44100 Hz, stereo
        //                                  0 , 1       , 2         , 3  , 4
        //Stream #0.1(eng): Video: h264, yuv420p, 800x480, 22.67 fps(r)
        int channels = 0;

        String streamLine = getStreamLine(fileInfo.commandOutput,"Video");

        //System.out.println("StreamLine: getVideocodec: "+streamLine);

        String[] streamLineSplit = streamLine.split(":");
        if (streamLineSplit.length>=3) {
            String[] videoCodecSplit = streamLineSplit[2].split(",");
            return videoCodecSplit[0].trim();
        }

        return null;

    }

    private String getStreamLine(String commandOutput, String streamTitle) {

        String streamLineEntry = getStringBetween("Stream #",streamTitle,commandOutput);

        System.out.println("StreamLineEntry: "+streamLineEntry);

        int startStreamLine = commandOutput.indexOf("Stream #"+streamLineEntry+streamTitle);
        int endStreamLine = commandOutput.indexOf("\n",startStreamLine);

        System.out.println("startStreamLine: "+startStreamLine);
        System.out.println("endStreamLine: "+endStreamLine);

        if (endStreamLine>startStreamLine && startStreamLine>=0) {

            String streamLine = commandOutput.substring(startStreamLine, endStreamLine);
            return streamLine;

        } else {
            return null;
        }

    }

    protected int getSampleRate(FileInfo srcFile) {

        //StreamLine = Stream #0.0: Audio: mp3, 44100 Hz, 2 channels, s16, 256 kb/s
        //                                      <samplerate>,<channels>,<?>,<bitrate>
        //                                  0 , 1       , 2         , 3  , 4
        int sampleRate = 0;
        String streamLine = getStringBetween("Stream #","Hz",srcFile.commandOutput);
        //System.out.println("StreamLine getSampleRate: "+streamLine);
        if (streamLine!=null) {
            String[] streamLineArray = streamLine.split(",");


            if (streamLineArray.length>=2) {
                String sampleString = streamLineArray[1].trim();
                if (sampleString.endsWith(" Hz")) {
                    sampleString = sampleString.substring(0,sampleString.length()-" Hz".length());
                }
                sampleRate = Integer.parseInt(sampleString);
            }
        }

        return sampleRate;

    }


    protected long getDuration(FileInfo srcFile) {

        try {
            return getDurationFromCommandOutput(srcFile.commandOutput);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return -1;
        }

    }

    private long getDurationFromCommandOutput(String commandOutput) throws ParseException {

        long duration = 0;
        String fieldNameStartsWith = "Duration: ";
        String fieldNameEndsWith = ",";

        String durationString = getStringBetween(fieldNameStartsWith, fieldNameEndsWith, commandOutput);

        //System.out.println("COMMAND OUTPUT: "+commandOutput);
        logger.debug("getDurationFromCommandOutput: durationString = "+durationString);
        //System.out.println("getDurationFromCommandOutput: durationString = "+durationString);
        SimpleDateFormat ddf = new SimpleDateFormat("HH:mm:ss.S Z");
        //ddf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            /*
            Date durationDate = ddf.parse(durationString);
            duration = durationDate.getTime();
            System.out.println("getDurationFromCommandOutput: local time durationString parsed = "+durationDate);
            System.out.println("getDurationFromCommandOutput: local time durationString parsed long = "+duration);
            */
            ddf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date durationDate = ddf.parse(durationString+" +0000");
            duration = durationDate.getTime();
            //System.out.println("getDurationFromCommandOutput: utc durationString parsed = "+durationDate);
            //System.out.println("getDurationFromCommandOutput: utc durationString parsed long = "+duration);

            return duration;

        } catch (ParseException e) {
            logger.error("Duration From Command Output unparseable: "+e.getMessage());
            e.printStackTrace();
        }

        return duration;

    }

    private String getStringBetween(String start, String end, String stgToAnalyze) {

        String foundString = null;
        String fieldNameStartsWith = start;
        String fieldNameEndsWith = end;
        Pattern pattern = Pattern.compile(fieldNameStartsWith+"(.*?)"+fieldNameEndsWith);
        //System.out.println("getDurationFromCommandOutput");

        Matcher matcher = pattern.matcher(stgToAnalyze);
        matcher.useAnchoringBounds(false);
        //Check all occurance
        while (matcher.find()) {
            //System.out.println("Start index: "+matcher.start());
            //System.out.println("End index: "+matcher.end());
            //System.out.println("Group: "+matcher.group());

            int startCut = fieldNameStartsWith.length();
            int endCut = matcher.group().length()-fieldNameEndsWith.length();
            foundString = matcher.group().substring(startCut,endCut);


        }
        return foundString;
    }
}

