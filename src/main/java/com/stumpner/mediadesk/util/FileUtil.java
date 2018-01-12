package com.stumpner.mediadesk.util;

import com.stumpner.mediadesk.core.Config;

import java.io.*;
import java.nio.channels.FileChannel;

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
 * Date: 17.04.2009
 * Time: 14:13:32
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {

    private static final int DEFAULT_BUFFER_SIZE = 256 * 1024; // ..bytes = 256KB.

        public static void copyFile(File in, File out) throws IOException {

            //System.out.println("start copyFile + "+new Date());
            //classicCopyFile(in,out);
            nioHighPerformanceCopyFile(in,out);
            //System.out.println("end copyFile "+new Date());
    }

    private static void nioHighPerformanceCopyFile(File sourceFile, File destFile) throws IOException {

         if(!destFile.exists()) {
          destFile.createNewFile();
         }

         FileChannel source = null;
         FileChannel destination = null;
         try {
          source = new FileInputStream(sourceFile).getChannel();
          destination = new FileOutputStream(destFile).getChannel();
          //destination.transferFrom(source, 0, source.size());
            long remaining = sourceFile.length();
            long position = 0;
            while (remaining > 0) {
                long transferred = source.transferTo(position, remaining, destination);
                remaining -= transferred;
                position += transferred;
            }
         }
         catch (IOException e) {
             System.out.println("["+ Config.instanceName+"] Fehler bei nioHighPerformanceCopyFile: "+e.getMessage());
    }
         finally {
          if(source != null) {
           source.close();
          }
          if(destination != null) {
           destination.close();
          }
         }

    }

    private static void classicCopyFile(File in, File out) throws IOException {

        BufferedInputStream fis  = new BufferedInputStream(new FileInputStream(in));
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(out));
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        int i = 0;
        while((i=fis.read(buf))!=-1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();

    }

}
