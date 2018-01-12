package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.image.util.SizeExceedException;

import java.io.File;

import com.stumpner.mediadesk.upload.FileRejectException;

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
 * Importiert ein Medienobjekt in die Datenbank. Die Implementierungen dieses Interface können
 * die verschiedenen Medientypen importieren.
 * User: franz.stumpner
 * Date: 12.10.2007
 * Time: 17:47:52
 */
public interface MediaImportHandler {

    /**
     * Importiert die angegebene Mediendatei in die Mediendatenbank
     * @return ID (ivid) des neuen Medienobjekts in der Datenbank
     * @throws SizeExceedException Wenn die Dateigröße überschritten wurde
     * @param file Datei die importiert werden soll
     * @param userId userId des Benutzers, der die Datei importiert
     */
    public int processImport(File file, int userId) throws SizeExceedException, FileRejectException;

}
