package com.stumpner.mediadesk.util;

import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URL;
import java.net.MalformedURLException;

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
 * Date: 26.04.2005
 * Time: 23:34:44
 * To change this template use File | Settings | File Templates.
 *
 * This Wrapper adds the getLength method to retrieve the Filesize
 */
public class FileWrapper {


    private File file = null;

    public FileWrapper(File file) {
        this.file = file;
    }

    public int hashCode() {
        return file.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long lastModified() {
        return file.lastModified();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long length() {
        return file.length();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long getLength() {
        return (file.length()/1000);
    }

    public void deleteOnExit() {
        file.deleteOnExit();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean canRead() {
        return file.canRead();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean canWrite() {
        return file.canWrite();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean createNewFile() throws IOException {
        return file.createNewFile();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean delete() {
        return file.delete();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean exists() {
        return file.exists();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isAbsolute() {
        return file.isAbsolute();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isDirectory() {
        return file.isDirectory();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isFile() {
        return file.isFile();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isHidden() {
        return file.isHidden();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean mkdir() {
        return file.mkdir();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean mkdirs() {
        return file.mkdirs();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean setReadOnly() {
        return file.setReadOnly();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean setLastModified(long time) {
        return file.setLastModified(time);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public File getAbsoluteFile() {
        return file.getAbsoluteFile();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public File getCanonicalFile() throws IOException {
        return file.getCanonicalFile();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public File getParentFile() {
        return file.getParentFile();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public File[] listFiles() {
        return file.listFiles();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public int compareTo(File pathname) {
        return file.compareTo(pathname);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean renameTo(File dest) {
        return file.renameTo(dest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public int compareTo(Object o) {
        return file.compareTo((File)o);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean equals(Object obj) {
        return file.equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getCanonicalPath() throws IOException {
        return file.getCanonicalPath();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getName() {
        return file.getName();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getParent() {
        return file.getParent();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getPath() {
        return file.getPath();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String toString() {
        return file.toString();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String[] list() {
        return file.list();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public URI toURI() {
        return file.toURI();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public URL toURL() throws MalformedURLException {
        return file.toURL();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public File[] listFiles(FileFilter filter) {
        return file.listFiles(filter);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public File[] listFiles(FilenameFilter filter) {
        return file.listFiles(filter);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String[] list(FilenameFilter filter) {
        return file.list(filter);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
