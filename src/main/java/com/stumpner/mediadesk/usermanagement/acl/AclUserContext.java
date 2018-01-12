package com.stumpner.mediadesk.usermanagement.acl;

import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPrincipal;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AccessObject;

import java.security.acl.AclNotFoundException;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;

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
 * Ein Acl-Context der User und Gruppenberechtigungen überprüft.
 * Beim erstellen muss daher ein User und ein Gruppen-Objekt übergeben werden.
 * User: stumpner
 * Date: 25.06.2009
 * Time: 13:58:12
 */
public class AclUserContext extends AclControllerContext {

    //als UserContext wird die Klasse/Objekt selbst verwendet
    AclControllerContext groupContext = null;
    User user = null;

    public AclUserContext(User user, SecurityGroup group) {

        super(user);
        this.user = user;
        this.setDebug(false);
        groupContext = new AclControllerContext(group);
    }

    public User getUser() {
        return user;
    }


    public AclUserContext(AclPrincipal aclPrincipal) {
        super(aclPrincipal);
    }

    /**
     * Überprüft ob der Benutzer oder die Gruppe des Benutzers zugriff auf das Objekt hat
     * @param aclPermission
     * @param accessObject
     * @return
     * @throws AclNotFoundException
     */
    public boolean checkPermission(AclPermission aclPermission, AccessObject accessObject) throws AclNotFoundException {

        if (super.checkPermission(aclPermission,accessObject)) {
            //Zugriff durch User-Berechtigung gegeben
            return true;
        }

        if (groupContext.checkPermission(aclPermission, accessObject)) {
            return true;
        }

        return false;
    }

    /*
    public List getPermittedList(AclPermission aclPermission, List list) {

        List userList = super.getPermittedList(aclPermission,list);
        userList.addAll(groupContext.getPermittedList(aclPermission,list));
        return userList;
    }
    */
}

