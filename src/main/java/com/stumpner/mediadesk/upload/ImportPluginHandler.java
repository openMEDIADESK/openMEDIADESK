package com.stumpner.mediadesk.upload;

import java.io.File;

import com.stumpner.mediadesk.core.IMediaObject;

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
 * Interface f�r ein Import Plugin. Eine Implementierung dieses Interface kann die Datei beim Importvorgang bearbeiten, scannen, ....
 * Die Implementierung muss dann der ImportPluginHandlerChain bekannt gemacht werden:
 * ImportPluginHandlerChain.add(..)
 * User: stumpner
 * Date: 13.11.2015
 * Time: 09:14:47
 */
public interface ImportPluginHandler {

    /**
     * In dieser Implementierung kann die zu importierende Datei gepr�ft werden und mit einer FileRejectException abgelehnt werden
     * @param file
     * @param ctx
     * @return
     * @throws FileRejectException
     */
    public PluginResult validateFile(File file, PluginContext ctx) throws FileRejectException;

    /**
     * In dieser Implementierung ist die Datei bereits importiert im Parameter IMediaObject ist das bereits erstellte Datenbankobjekt mit der vergebenen ivid
     * @param IMediaObject
     * @param ctx
     * @return
     */
    public PluginResult process(IMediaObject IMediaObject, PluginContext ctx);

    /**
     * Mit der Implementierung dieser Methode kann das System die Funktionsf�higkeit des Plugins �berpr�fen, z.b. ob das Service installiert bzw. erreichbar ist
     * @param ctx
     * @return
     * @throws Exception
     */
    public PluginResult check(PluginContext ctx) throws Exception;

    public void setParameter(String parameter);

}
