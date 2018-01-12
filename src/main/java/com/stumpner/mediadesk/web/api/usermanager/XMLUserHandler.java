package com.stumpner.mediadesk.web.api.usermanager;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

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
 * User: stumpner
 * Date: 28.09.2011
 * Time: 11:28:17
 */
public class XMLUserHandler implements ContentHandler {

    private List<SyncedUser> userList = null;
    private SyncedUser user = null;
    private String currentValue = "";
    private String filterGroupname = "";

    public XMLUserHandler(List userList, String filterGroupname) {
        this.userList = userList;
        this.filterGroupname = filterGroupname;
    }

    public List<SyncedUser> getUserList() {
        return userList;
    }

    public void setFilterGroupname(String filterGroupname) {
        this.filterGroupname = filterGroupname;
    }

    /**
     * Wenn vom selben Benutzernamen mehrere Eintr�ge exisitieren, wird immer nur der letzte/aktuellste verwendet
     * @return
     */
    public List<SyncedUser> getUserListDistinct() {

        HashMap<String,SyncedUser> userHashMap = new HashMap<String,SyncedUser>();

        for (SyncedUser user : userList) {
            userHashMap.put(user.getUserName(),user);
        }

        List<SyncedUser> distinctUserList = new ArrayList();
        distinctUserList.addAll(userHashMap.values());
        return distinctUserList;

    }

    public void setDocumentLocator(Locator locator) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startDocument() throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void endDocument() throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("user")) {
            user = new SyncedUser();
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("user")) {
            if (!filterGroupname.equalsIgnoreCase("")) {
                //Filter auf Gruppenname
                if (user.getGroupname().equalsIgnoreCase(filterGroupname)) {
                    //standardeinstellung verwenden
                } else {
                    //Wenn der Gruppenname nicht �bereinstimmt den User sperren/disabled
                    user.setEnabled(false);
                }
            } else {
                //Wenn kein Filter angegeben, standardeinstellung verwenden.
            }
            userList.add(user);
        }

        if (localName.equalsIgnoreCase("username")) { user.setUserName(currentValue); }
        if (localName.equalsIgnoreCase("password")) { user.setEncryptedPassword(currentValue); }
        if (localName.equalsIgnoreCase("first_name")) { user.setFirstName(currentValue); }
        if (localName.equalsIgnoreCase("last_name")) { user.setLastName(currentValue); }
        if (localName.equalsIgnoreCase("address")) { user.setStreet(currentValue); }
        if (localName.equalsIgnoreCase("telephone")) { user.setPhone(currentValue); }
        if (localName.equalsIgnoreCase("telephone")) { user.setPhone(currentValue); }
        if (localName.equalsIgnoreCase("email")) { user.setEmail(currentValue); }
        if (localName.equalsIgnoreCase("city")) { user.setCity(currentValue); }
        if (localName.equalsIgnoreCase("zip")) { user.setZipCode(currentValue); }
        if (localName.equalsIgnoreCase("fax")) { user.setFax(currentValue); }
        if (localName.equalsIgnoreCase("company")) { user.setCompany(currentValue); }

        if (localName.equalsIgnoreCase("company_type")) { user.setCompanyType(currentValue); }
        if (localName.equalsIgnoreCase("country")) { user.setCountry(currentValue); }

        if (localName.equalsIgnoreCase("disabled")) { user.setEnabled(currentValue.equalsIgnoreCase("0")); }
        if (localName.equalsIgnoreCase("enabled")) { user.setEnabled(currentValue.equalsIgnoreCase("1")); }
        if (localName.equalsIgnoreCase("deleted")) { user.setDeleted(currentValue.equalsIgnoreCase("1")); }

        if (localName.equalsIgnoreCase("groupname")) { user.setGroupname(currentValue); }

    }

    public void characters(char ch[], int start, int length) throws SAXException {
        currentValue = new String(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void processingInstruction(String target, String data) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void skippedEntity(String name) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
