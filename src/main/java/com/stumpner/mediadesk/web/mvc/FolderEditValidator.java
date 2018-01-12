package com.stumpner.mediadesk.web.mvc;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import com.stumpner.mediadesk.image.folder.Folder;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;

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
 * Date: 25.04.2005
 * Time: 21:52:26
 * To change this template use File | Settings | File Templates.
 */
public class FolderEditValidator implements Validator {

    public boolean supports(Class aClass) {
        if (aClass.equals(FolderMultiLang.class)) return true;
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validate(Object o, Errors errors) {
        //To change body of implemented methods use File | Settings | File Templates.
        Folder folder = (Folder)o;
        /*
        Kenen Fehler, weil kein folderName eingegeben werden muss
        //todo: eventuell prüfen ob Titel eingegeben wurde
        if (folder.getFolderName().length()==0) {
            errors.rejectValue("folderName","folderName","Folder must have a name");
        }
        */
    }
}
