package com.stumpner.mediadesk.usermanagement;

import com.stumpner.mediadesk.core.Config;

import java.security.Principal;
import java.math.BigDecimal;

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
 * User: franzstumpner
 * Date: 24.02.2005
 * Time: 20:32:51
 * To change this template use File | Settings | File Templates.
 */
public class User extends SimpleUser implements Principal, AclPrincipal {

    String email = "";
    String phone = "";
    String cellPhone = "";
    String fax = "";

    String company = "";
    String companyType = "";
    String street = "";
    String city = "";
    String zipCode = "";
    String country = "";

    BigDecimal credits = BigDecimal.valueOf(Config.defaultCredits);
    int vatPercent = Config.vatPercent;

    int role = 0;
    private int securityGroup = 1; // Standardmaessig Security Group A setzen
    private int homeFolderId = -1; //Home-Dir/ Home-Kategorie des Users

    private int mandant = -1; //ID des zugeh�rigen Mandanten (Mandant = ID des Mandanten-Benutzers)
    private String activateCode = ""; //Code/Hash zum aktivieren des Benutzers via Email-Link

    private String autologinKey = ""; //Cookie Value f�r Autologin

    public static int ROLE_UNDEFINED = -1;
    public static int ROLE_USER = 0;
    public static int ROLE_IMPORTER = 4; //new in 2.7
    public static int ROLE_PINMAKLER = 5; //new in 3.3;
    public static int ROLE_PINEDITOR = 6; //new in 2.7
    public static int ROLE_EDITOR = 8; //new in 2.7 (former 10!)
    public static int ROLE_HOME_EDITOR = 9; //new in 3.2 (kann in der homekategorie unterkategorien erstellen)
    public static int ROLE_MASTEREDITOR = 10; //new in 2.7 (former editor)
    public static int ROLE_ADMIN = 99;

    private boolean uploadNotification = true;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public String getName() {
        return getUserName();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getSecurityGroup() {
        return securityGroup;
    }

    public void setSecurityGroup(int securityGroup) {
        this.securityGroup = securityGroup;
    }

    public int getHomeFolderId() {
        return homeFolderId;
    }

    public void setHomeFolderId(int homeFolderId) {
        this.homeFolderId = homeFolderId;
    }

    public int getAclObjectSerialId() {
        return getUserId();
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.activateCode = "";
    }

    public int getAclObjectTypeId() {
        //Users sind immer vom Typ 2
        return 2;
    }

    public int getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(int vatPercent) {
        this.vatPercent = vatPercent;
    }

    public boolean isUploadNotification() {
        return uploadNotification;
    }

    public void setUploadNotification(boolean uploadNotification) {
        this.uploadNotification = uploadNotification;
    }

    public String getAutologinKey() {
        return autologinKey;
    }

    public void setAutologinKey(String autologinKey) {
        this.autologinKey = autologinKey;
    }
}
