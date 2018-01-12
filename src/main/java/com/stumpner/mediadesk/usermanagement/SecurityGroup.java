package com.stumpner.mediadesk.usermanagement;

import net.stumpner.security.acl.AclPrincipal;

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
 * Date: 20.11.2007
 * Time: 18:07:24
 * To change this template use File | Settings | File Templates.
 */
public class SecurityGroup implements AclPrincipal {
    
    private int id = 0;
    private String name = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAclObjectSerialId() {
        return id;
    }

    public int getAclObjectTypeId() {
        //Security Groups sind immer vom Typ 1
        return 1;
    }
}
