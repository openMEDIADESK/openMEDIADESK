package com.stumpner.mediadesk.usermanagement.acl;

import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.SuSIDEBaseController;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

import javax.servlet.http.HttpServletRequest;

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
 * Stellt eine einheitliche Funktion zur Verfügung
 * um in MVC-Controllern einen AclContext zu erstellen.
 *
 * Ist auch dafür zuständig, welche Security-Group ein Besucher / User bekommt
 */
public class AclContextFactory {

    private static String REQUEST_ATTRIBUTE_NAME_ACLCTX = "aclContext";

    /**
     * Erstellt einen Acl-Context für den eingeloggten user bzw. gast/visitor
     * @param request
     * @param baseController
     * @deprecated {@link com.stumpner.mediadesk.usermanagement.acl.AclContextFactory#createAclContext(javax.servlet.http.HttpServletRequest)} verwenden.
     */
    static public void createAclContext(HttpServletRequest request, SuSIDEBaseController baseController) {

        //ACL-Context erstellen für das jeweilige Principal
        UserService userService = new UserService();
        User user = baseController.getUser(request);
        SecurityGroup securityGroup = null;

        if (user.getRole()==User.ROLE_UNDEFINED) {
            //Benutzer ist Gast/Besucher
            securityGroup = userService.getSecurityGroupById(0);
        } else {
            securityGroup = userService.getSecurityGroupById(
                    user.getSecurityGroup()
            );
        }

        //AclControllerContext aclContext = new AclControllerContext(securityGroup);
        AclControllerContext aclContext = new AclUserContext(user,securityGroup);
        request.setAttribute(REQUEST_ATTRIBUTE_NAME_ACLCTX,aclContext);

    }

    /**
     * Erstellt einen Acl-Context für den eingeloggten user bzw. gast/visitor
     * @param request
     */
    static public void createAclContext(HttpServletRequest request) {

        //ACL-Context erstellen für das jeweilige Principal
        UserService userService = new UserService();
        User user = WebHelper.getUser(request);
        SecurityGroup securityGroup = null;

        /*
        if (user.getRole()==User.ROLE_UNDEFINED) {
            //Benutzer ist Gast/Besucher
            securityGroup = userService.getSecurityGroupById(1);
        } else {
            securityGroup = userService.getSecurityGroupById(
                    user.getSecurityGroup()
            );
        }
        */

        securityGroup = userService.getSecurityGroupById(
                user.getSecurityGroup()
        );

        //AclControllerContext aclContext = new AclControllerContext(securityGroup);
        AclControllerContext aclContext = new AclUserContext(user,securityGroup);
        request.setAttribute(REQUEST_ATTRIBUTE_NAME_ACLCTX,aclContext);

    }

    static public AclControllerContext createAclContext(String username) throws ObjectNotFoundException, IOServiceException {

        UserService userService = new UserService();
        User user = (User)userService.getByName(username);
        SecurityGroup securityGroup = userService.getSecurityGroupById(
                user.getSecurityGroup()
        );

        return new AclUserContext(user, securityGroup);

    }

    /**
     * gibt den AclContext zurück und erstellt bei bedarf einen neuen
     * @param request
     * @return
     */
    static public AclControllerContext getAclContext(HttpServletRequest request) {

        if (request.getAttribute(REQUEST_ATTRIBUTE_NAME_ACLCTX)==null) {
            createAclContext(request);
        }
        return (AclControllerContext)request.getAttribute(REQUEST_ATTRIBUTE_NAME_ACLCTX);
    }

    /**
     * Gibt die Berechtigung für Zeigen und Lesen(Download) zurück
     * @return
     */
    public static AclPermission[] getViewAndReadPermission() {

        AclPermission[] permissions = new AclPermission[] { new AclPermission("read"), new AclPermission("view") };
        return permissions;
    }
}
