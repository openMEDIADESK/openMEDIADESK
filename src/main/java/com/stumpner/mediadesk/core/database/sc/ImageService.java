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
 * Date: 29.03.2005
 * Time: 19:07:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageService extends MultiLanguageService implements IServiceClass {

    /**
     * Gives Back the image identified by its id
     * @param id
     * @return
     * @throws ObjectNotFoundException
     * @throws IOServiceException
     */
    /*
    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {

        return this.getImageById(id);
    } */

    /**
     * Gives Back the image identified by its id
     * @param id
     * @return
     */
    /*
    public Image getImageById(int id) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        Image image = new Image();

        try {
            image = (Image)smc.queryForObject("getImageById",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return image;
    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getImageByNumber(String number) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        Image image = new Image();

        try {
            image = (Image)smc.queryForObject("getImageByNumber",number);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (image==null) {
            //image not found
            throw new ObjectNotFoundException();
        }

        return image;
    }

    public void save(Object object) throws IOServiceException {
        this.saveImage((Image)object);
    }

    public void saveImage(Image image) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        try {
            smc.update("saveImage",image);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (image instanceof ImageVersionMultiLang) {
            //Das Speichern der Sprachfelder darf nicht von saveImage abhängen, falls dort eine Truncation-Exception
            //passiert, werden die Sprachfelder nicht gespeichert
            try {
                smc.update("saveImageMultiLang",image);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }  */

    /**
     * Adds an new Image without a Version!
     * Can not be called from outside, because an Image must always have an
     * ImageVersion and therefore addImage is only called by the ImageVersionService of addImage
     * @param image
     * @throws IOServiceException
     */
    /*
    protected Image addImage(Image image) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        image.setCreateDate(new Date());
        try {

            //If Image has no number, take systemtime as number:
            if (image.getImageNumber().length()==0) {
                String numberString = Long.toString(System.currentTimeMillis());
                if (numberString.length()>100) {
                    numberString = numberString.substring(1,99);
                }
                image.setImageNumber(numberString);
            }
            this.getImageByNumber(image.getImageNumber());
            //sorry imagenumber exists, throw DublicateEntry Exception
            throw new DublicateEntry("ImageService.addImage(): DublicateEntry");

        } catch (ObjectNotFoundException e) {
            //okay - user does not exist, go on...
        }

        Image tmpImage = new Image();

        try {
            smc.insert("addImage",image);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            tmpImage = (Image)this.getImageByNumber(image.getImageNumber());
        } catch (ObjectNotFoundException e) {
            throw new IOServiceException("DB ERROR: Can not add Object [Not Created]");
        }

        image.setImageId(tmpImage.getImageId());
        this.save(image);

        return image;
    }

    public void add(Object object) throws IOServiceException {

        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteById(int id) throws IOServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteImage(int imageId) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("deleteAllImageMetadataFromImageId", new Integer(imageId));
            smc.delete("deleteImageVersionsByImageId", new Integer(imageId));
            smc.delete("deleteImage",new Integer(imageId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public int getImageCount() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(0);
        try {
            count = (Integer)smc.queryForObject("getImageCount",null);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return count.intValue();
    }     */

    /**
     * Gibt den benötigten Speicherplatz in Mb zurück
     * @return
     */
    /*
    public int getImageMb() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(0);
        try {
            count = (Integer)smc.queryForObject("getImageKb",null);
            if (count==null) {
                //Wenn noch keine Bilder in der Datenbank sind wird null
                //zurückgegeben, deshalb das null abfangen und 0 speichern
                count = new Integer(0);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return count.intValue()/1000;
    }
    */
}
