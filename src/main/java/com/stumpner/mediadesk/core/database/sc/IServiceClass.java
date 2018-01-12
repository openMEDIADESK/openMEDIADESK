package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

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
 * Date: 24.02.2005
 * Time: 20:25:27
 * To change this template use File | Settings | File Templates.
 *
 * IServiceClass-Interface for Service classes who stores and loads objects from and to the database
 *
 */
public interface IServiceClass {

    /**
     * Loads an Object from the Database, given by the id
     * @param id
     * @return
     */
    public Object getById(int id) throws ObjectNotFoundException, IOServiceException;

    /**
     * Loads an Object from the Database, given by its name
     * @param name
     * @return
     */
    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException;

    /**
     * Saves an existing Objects to the database
     * @param object
     */
    public void save(Object object) throws IOServiceException;

    /**
     * Creates/Add an new Object to the database
     * @param object
     */
    public void add(Object object) throws IOServiceException;

    /**
     * Delete an Object by giving its id
     * @param id
     */
    public void deleteById(int id) throws IOServiceException;

}
