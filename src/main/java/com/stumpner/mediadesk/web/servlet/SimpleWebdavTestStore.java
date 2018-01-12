package com.stumpner.mediadesk.web.servlet;

import net.sf.webdav.LocalFileSystemStore;
import net.sf.webdav.ITransaction;
import net.sf.webdav.StoredObject;
import net.sf.webdav.exceptions.WebdavException;

import java.io.File;
import java.io.InputStream;
import java.security.Principal;

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
 * Date: 25.01.2009
 * Time: 13:35:59
 * To change this template use File | Settings | File Templates.
 */
public class SimpleWebdavTestStore extends LocalFileSystemStore {

    public SimpleWebdavTestStore(File file) {
        super(file);
    }

    public ITransaction begin(Principal principal) throws WebdavException {
        return super.begin(principal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void checkAuthentication(ITransaction iTransaction) throws SecurityException {
        super.checkAuthentication(iTransaction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void commit(ITransaction iTransaction) throws WebdavException {
        super.commit(iTransaction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void rollback(ITransaction iTransaction) throws WebdavException {
        super.rollback(iTransaction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void createFolder(ITransaction iTransaction, String s) throws WebdavException {
        System.out.println("CreateFolder s="+s);
        super.createFolder(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void createResource(ITransaction iTransaction, String s) throws WebdavException {
        super.createResource(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long setResourceContent(ITransaction iTransaction, String s, InputStream inputStream, String s1, String s2) throws WebdavException {
        System.out.println("setResourceContent s="+s+", s1="+s1+", s2="+s2);
        return super.setResourceContent(iTransaction, s, inputStream, s1, s2);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String[] getChildrenNames(ITransaction iTransaction, String s) throws WebdavException {
        return super.getChildrenNames(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void removeObject(ITransaction iTransaction, String s) throws WebdavException {
        super.removeObject(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public InputStream getResourceContent(ITransaction iTransaction, String s) throws WebdavException {
        return super.getResourceContent(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public long getResourceLength(ITransaction iTransaction, String s) throws WebdavException {
        return super.getResourceLength(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public StoredObject getStoredObject(ITransaction iTransaction, String s) {

        StoredObject storedObject =  super.getStoredObject(iTransaction, s);    //To change body of overridden methods use File | Settings | File Templates.
        System.out.println("getStoredObject s="+s+" [storedObject]="+storedObject);
        if (storedObject!=null) {
            System.out.println("  + storedObject.isNullResource="+storedObject.isNullResource());
        }
        return storedObject;
    }
}
