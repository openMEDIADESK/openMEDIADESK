package com.stumpner.mediadesk.web.webdav;

import com.bradmcevoy.http.ResourceFactory;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Auth;
import com.bradmcevoy.common.Path;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.folder.FolderMultiLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.security.acl.AclNotFoundException;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.web.webdav.auth.DigestAuthUtils;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AclControllerContext;

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
 * Date: 16.03.2011
 * Time: 18:50:04
 * To change this template use File | Settings | File Templates.
 */
public class WebdavResourceFactory implements ResourceFactory {

	private Logger log = LoggerFactory.getLogger(WebdavResourceFactory.class);

	public static final String REALM = UserService.REALM;

	@Override
	public Resource getResource(String host, String p) {
        if (Config.webdavEnabled) {
            return getResourceInternal(host, p);
        } else {
            return null;
        }
    }

	public Resource getResourceInternal(String host, String p) {

        //return new AllDepartmentsResource(this, null);

        Path path = Path.path(p).getStripFirst();
		log.debug("getResource: " + path + " pathname: "+path.getName());
        //System.out.println("path= "+path.toString()+" pathname: "+path.getName());
        if( path.isRoot() ) {
            //Root
            System.out.println("Webdav Resource Request: ["+path+"] = ROOT-CATEGORY");
            return new FolderResource(this,"");
        } else {
            // Nicht Root
            //System.out.println("path not root");
            FolderService folderService = new FolderService();
            try {
                FolderMultiLang category = (FolderMultiLang) folderService.getFolderByPath(path.toString());
                //Unterkategorie
                System.out.println("Webdav Resource Request: ["+path+"] = CATEGORY,id="+category.getFolderId()+",name="+category.getName());
                return new FolderResource(this,category);
            } catch (ObjectNotFoundException e) {
                //Medienobjekt in einer Kategorie (Keine Kategorie: - pr�fung auf Medienobjekt in der Kategorie)
                //System.out.println("   Gesucht wird wom�glich ein Medienobjekt... "+ path.getParent().toString());
                //finden des medienobjects
                try {

                    Folder folder = null;

                    if (!path.getParent().toString().equalsIgnoreCase("")) {
                        folder = folderService.getFolderByPath(path.getParent().toString());
                    } else {
                        folder = new Folder();
                        folder.setFolderId(0);
                    }

                    MediaService imageService = new MediaService();
                    SimpleLoaderClass loader = new SimpleLoaderClass();
                    loader.setId(folder.getFolderId());
                    List categoryMediaList = imageService.getFolderMediaObjects(loader);

                    for (Object aCategoryMedia : categoryMediaList) {
                        MediaObjectMultiLang media = (MediaObjectMultiLang)aCategoryMedia;
                        //System.out.println("    suche gerade in: "+media.getVersionName()+" -> "+path.getName());
                        if (media.getVersionName().equalsIgnoreCase(path.getName())) {
                            System.out.println("Webdav Resource Request: ["+path+"] ist MediaObjekt: "+media.getVersionName());
                            return new MediaObjectResource(folder,media);
                        }
                    }

                    //BasicMediaObject gibt es nicht (neu/anlegen)?
                    System.out.println("Webdav Resource Request: ["+path+"] NICHT GEFUNDEN, null");
                    return null;// new NotExistingMediaObjectResource(folder,path);

                } catch (ObjectNotFoundException e1) {
                    //System.err.println("Parent-kategorie des medienobjekts nicht gefunden");
                }
                //todo:
            }

        }

        /*
        if (path.getName().equalsIgnoreCase("testfile2.txt")) {
            log.debug("TestFileResource");
            return new MediaObjectResource();
        } */
        /*
        if( path.isRoot() ) {
			return new AllDepartmentsResource(this, session);
		} else if( path.getLength() == 1 ) {
			return findDepartment(path.getName(), session);
		} else if( path.getLength() == 2) {
			// TODO
			return null;
		} else {
			return null;
		}*/
        return null;
    }
    /*
	public List<Resource> findAllDepartments(Session session) {
		Criteria crit = session.createCriteria(Department.class);
		List list = crit.list();
		if( list == null || list.size() == 0) {
			return Collections.EMPTY_LIST;
		} else {
			List<Resource> departments = new ArrayList<Resource>();
			for( Object o : list ) {
				departments.add( new DepartmentResource(this, (Department)o) );
			}
			return departments;
		}

	}

	public Resource findDepartment(String name, Session session) {
		log.debug("findDepartment: " + name);
		Criteria crit = session.createCriteria(Department.class);
		crit.add(Expression.eq("name", name));
		List list = crit.list();
		if( list == null || list.size() == 0 ) {
			log.debug("not found");
			return null;
		} else {
			Department d = (Department) list.get(0);
			log.debug("found: " + d.getName());
			return new DepartmentResource(this, d);
		}
	}  */

    public static User getUsernameFromAuthHeader(Request request, Request.Method method, Auth auth) {

        User user = null;
        Map headers = request.getHeaders();
        /*
        Iterator keyset = headers.keySet().iterator();
        while (keyset.hasNext()) {
            Object set = keyset.next();
            System.out.println("AUTHORIZE: "+set+" - "+headers.get(set));
        } */

        String authHeader = (String)headers.get("authorization");
        Map headerMap = DigestAuthUtils.parseDigestRequest(authHeader);

        //todo: pr�fen auf zugriffsrechte
        String username = (String)headerMap.get("username");

        UserService userService = new UserService();
        try {
            user = (User)userService.getByName(username);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return user;
    }

    public static AclControllerContext createAclContext(User user) {

        //ACL Laden
        try {
            return AclContextFactory.createAclContext(user.getUserName());
        } catch (ObjectNotFoundException e) {
            return null;
        } catch (IOServiceException e) {
            return null;
        }
    }

    public static boolean authroise(Request request, Folder folder, User user, AclControllerContext aclCtx) {

        //muss nach dem ACL Context initialisieren erfolgen, da der ACL Context noch ben�tigt wird
        if (folder.getFolderId()==0) { return true; } //Root-Kategorie immer berechtigen

        //---------------------
        if (aclCtx==null) { return false; }

         if (user.getRole() == User.ROLE_ADMIN) { return true; } // Admin darf �berall hin

        //request.getHeader("Authorization")

        Map attributes = request.getAttributes();
        Iterator aKeyset = attributes.keySet().iterator();
        while (aKeyset.hasNext()) {
            Object set = aKeyset.next();
            //System.out.println("Atributes: "+set+" - "+attributes.get(set).getClass().getName());
        }

        try {

            //todo:MKCOL -> Kategorie erstellen
            //todo:MOVE --> NEU/Verschieben
            if (request.getMethod() == Request.Method.MOVE ||
                request.getMethod() == Request.Method.MKCOL ||
                request.getMethod() == Request.Method.PUT ||
                request.getMethod() == Request.Method.DELETE ||
                request.getMethod() == Request.Method.LOCK) {
                //Schreiben/Upload/Kategorie erstellen

                if (user.getRole() == User.ROLE_MASTEREDITOR) {
                    //Administratoren und Chef-Redakteure haben immer schreibzugriff
                    return true;
                } else {

                    if (user.getRole() == User.ROLE_IMPORTER || user.getRole() == User.ROLE_EDITOR) {
                        if (aclCtx.checkPermission(new AclPermission("write"), folder)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        //Wenn keine Lieferant oder Redakteur dann keine schreib-berechtigung
                        return false;
                    }

                }

            } else if (request.getMethod() == Request.Method.GET) {

                if (aclCtx.checkPermission(new AclPermission("read"), folder)) {
                    return true;
                } else {
                    return false;
                }
                
            } else {
                if (aclCtx.checkPermission(new AclPermission("view"), folder)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (AclNotFoundException e) {
            //Bei Fehlern nicht berechtigen!
            return false;
        }

    }
}
