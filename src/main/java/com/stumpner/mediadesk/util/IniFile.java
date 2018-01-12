package com.stumpner.mediadesk.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

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
 * Date: 28.04.2005
 * Time: 18:05:22
 * To change this template use File | Settings | File Templates.
 */
public class IniFile {

    private Properties p = new Properties();

    public void open(String fileName) {

        Logger logger = Logger.getLogger(getClass());

        try{
              p = new Properties();
              p.load(new FileInputStream(fileName));
            
              //p.list(System.out);
          } catch (Exception e) {
              logger.error(e);
          }

    }

    public String getProperty(String keyName) {
        String value = p.getProperty(keyName);
        return value;
    }

    public String getProperty(String keyName, String defaultValue) {
        String value = p.getProperty(keyName,defaultValue);
        //System.out.println("> "+keyName+": "+value);
        return value;
    }

}
