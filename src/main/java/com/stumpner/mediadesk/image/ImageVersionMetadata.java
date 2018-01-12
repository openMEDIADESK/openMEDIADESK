package com.stumpner.mediadesk.image;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.ApplicationSettings;
import com.stumpner.mediadesk.usermanagement.User;

import java.util.*;

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
 * Date: 08.05.2005
 * Time: 17:13:39
 * To change this template use File | Settings | File Templates.
 */
public class ImageVersionMetadata {

    List metadata = new LinkedList();
    ImageVersion imageVersion = new ImageVersion();
    User creator = new User();
    String[] copyfield;
    ApplicationSettings applicationSettings = new ApplicationSettings();

    String content = ""; //Nur bei TextFiles oder Objekte die bearbeitet werden können

    public List getMetadata() {
        return metadata;
    }

    /**
     * Returns a Map, where the key is the metaKey+"_"+lang ,instead of a list as it makes the method
     * ImageVersionMetadata.getMetadata()
     * The map which is returned only contains Keys where a lang-value exists
     * @return a Map, where the key is the metaKey+lang
     */
    public Map getMetadataLangMap() {

        Map metadataMap = new HashMap();
        Iterator metadataIt = metadata.iterator();

        while (metadataIt.hasNext()) {
            Metadata metadata = (Metadata)metadataIt.next();
            if (metadata.getLang().length()>0) {
                metadataMap.put(metadata.getMetaKey()+"_"+metadata.getLang(),metadata);
            }

        }

        //default languages:
        for (int a=0;a<Config.langFieldsImageVersion.length;a++) {
            String field = Config.langFieldsImageVersion[a];
            for (int b=0;b<Config.langCodesAvailable.length;b++) {
                String lang = Config.langCodesAvailable[b];
                if (!metadataMap.containsKey(field+"_"+lang)) {
                    Metadata metadata = new Metadata();
                    metadataMap.put(field+"_"+lang,metadata);
                }
            }
        }

        return metadataMap;
    }

    /**
     *
     * @param metadataLangMap
     * @deprecated wird in der MultiLang Version nicht mehr benötigt
     */
    public void setMetadataLangMap(Map metadataLangMap) {

        Set keySet = metadataLangMap.keySet();
        Iterator keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            //split the key into fieldVAlue and language
            StringTokenizer st = new StringTokenizer(key,"_");
            String field = st.nextToken();
            String lang = st.nextToken();
            String value = ((Metadata)metadataLangMap.get(key)).getMetaValue();
            setMetadataLang(field, value, lang);
        }

    }

    /**
     * Setzt in der Metadata Liste den eintrag mit den angegebenen werten,
     * gibt es diesen eintrag noch nicht (abhängig von Key) wird er erstellt
     * @param fieldName
     * @param value
     * @param lang
     */
    private void setMetadataLang(String fieldName, String value, String lang) {

        Iterator metadataIt = metadata.iterator();
        boolean exist = false;

        while (metadataIt.hasNext()) {
            Metadata metadataItem = (Metadata)metadataIt.next();
            if (metadataItem.getMetaKey().equalsIgnoreCase(fieldName) &&
                    metadataItem.getLang().equalsIgnoreCase(lang)) {
                metadataItem.setMetaValue(value);
                metadataItem.setLang(lang);
                metadata.set(metadata.indexOf(metadataItem),metadataItem);
                exist = true;
            }

        }

        if (exist==false) {
            Metadata metadataItem = new Metadata();
            metadataItem.setIvid(this.getImageVersion().getIvid());
            metadataItem.setMetaKey(fieldName);
            metadataItem.setMetaValue(value);
            metadataItem.setLang(lang);
            metadata.add(metadataItem);
        }

    }

    public void setMetadata(List metadata) {
        this.metadata = metadata;
    }

    public ImageVersion getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(ImageVersion imageVersion) {
        this.imageVersion = imageVersion;
    }

    //some helper functions for the controller
    public void setPhotographDate(Date date) {
        imageVersion.setPhotographDate(date);
    }

    public Date getPhotographDate() {
        return imageVersion.getPhotographDate();
    }

    public String[] getCopyfield() {
        return copyfield;
    }

    public void setCopyfield(String[] copyfield) {
        this.copyfield = copyfield;
    }

    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
