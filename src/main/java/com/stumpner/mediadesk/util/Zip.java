package com.stumpner.mediadesk.util;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.*;

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
 * Date: 17.05.2005
 * Time: 23:22:59
 * To change this template use File | Settings | File Templates.
 */
public class Zip {

  public void main(String filename[], String zipentryname[], OutputStream out) throws IOException {
    byte b[] = new byte[512];
    ZipOutputStream zout = new ZipOutputStream(out);
    for(int i = 0; i < filename.length; i ++) {
      InputStream in = new FileInputStream(filename[i]);
      ZipEntry e = new ZipEntry(zipentryname[i]);

        //todo: teilweise error bei gleichen filenamen

      zout.putNextEntry(e);
      int len=0;
      while((len=in.read(b)) != -1) {
        zout.write(b,0,len);
        }
      zout.closeEntry();
      print(e);
      }
    zout.close();
      //System.out.println("All Bytes transferred, clean up...");
    }

  public void print(ZipEntry e){
    PrintStream err = System.err;
    err.print("added " + e.getName());
    if (e.getMethod() == ZipEntry.DEFLATED) {
      long size = e.getSize();
      if (size > 0) {
        long csize = e.getCompressedSize();
        long ratio = ((size-csize)*100) / size;
        err.println(" (deflated " + ratio + "%)");
        }
      else {
        err.println(" (deflated 0%)");
        }
      }
    else {
      err.println(" (stored 0%)");
      }
    }

}
