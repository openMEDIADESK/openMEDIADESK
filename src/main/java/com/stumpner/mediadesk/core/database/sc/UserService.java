package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.folder.FolderMultiLang;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;

import java.sql.SQLException;
import java.util.*;

import com.ibatis.sqlmap.client.SqlMapClient;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AccessObject;
import net.stumpner.security.acl.exceptions.PermissionAlreadyExistException;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
 * Time: 20:25:09
 * To change this template use File | Settings | File Templates.
 */
public class UserService implements IServiceClass {

    public static String REALM = "mediaDESK";

    /**
     * This Method loads an User identified with its id value
     * @param id
     * @return
     * @throws ObjectNotFoundException
     * @throws IOServiceException
     */
    public Object getById(int id) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        User user = new User();

        try {
            user = (User)smc.queryForObject("getUserById",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return user;
    }

    public List getUserAtomList() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List userList = new ArrayList();

        try {
            userList = smc.queryForList("getUserAtomList",new Integer(10));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return userList;

    }
    
    public List getUserList() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List userList = new ArrayList();

        try {
            userList = smc.queryForList("getUserList",null);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return userList;

    }

    /**
     * Gibt alle Benutzer dieses Mandanten(-Benutzers) zurück
     * @param user
     * @return
     */
    public List getUserList(User user) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        List userList = new ArrayList();

        try {
            userList = smc.queryForList("getUserListMandant",new Integer(user.getUserId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;

    }

    public Object getByName(String name) throws ObjectNotFoundException, IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        User user = new User();

        try {
            user = (User)smc.queryForObject("getUserByName",name);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (user==null) {
            //user does not exist:
            throw new ObjectNotFoundException();
        }

        return user;

    }

    public void save(Object object) throws IOServiceException {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        User user = (User)object;
        try {
            smc.update("saveUser",user);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        /*
        StringBuffer sqlbuffer = new StringBuffer();

        sqlbuffer.append("UPDATE appuser SET ");
        // leaving username - username cannot be changed
        sqlbuffer.append("firstname = '"); sqlbuffer.append(user.getFirstName()); sqlbuffer.append("', ");
        sqlbuffer.append("lastname = '"); sqlbuffer.append(user.getLastName()); sqlbuffer.append("', ");
        sqlbuffer.append("email = '"); sqlbuffer.append(user.getEmail()); sqlbuffer.append("', ");
        sqlbuffer.append("phone = '"); sqlbuffer.append(user.getPhone()); sqlbuffer.append("', ");
        sqlbuffer.append("cellphone = '"); sqlbuffer.append(user.getCellPhone()); sqlbuffer.append("', ");
        sqlbuffer.append("fax = '"); sqlbuffer.append(user.getFax()); sqlbuffer.append("', ");
        sqlbuffer.append("company = '"); sqlbuffer.append(user.getCompany()); sqlbuffer.append("', ");
        sqlbuffer.append("companytype = '"); sqlbuffer.append(user.getCompanyType()); sqlbuffer.append("', ");
        sqlbuffer.append("street = '"); sqlbuffer.append(user.getStreet()); sqlbuffer.append("', ");
        sqlbuffer.append("city = '"); sqlbuffer.append(user.getCity()); sqlbuffer.append("', ");
        sqlbuffer.append("zipcode = '"); sqlbuffer.append(user.getZipCode()); sqlbuffer.append("', ");
        sqlbuffer.append("country = '"); sqlbuffer.append(user.getCountry()); sqlbuffer.append("' ");
        sqlbuffer.append(" WHERE userid = "+user.getUserId());

        Database.updateQuery(sqlbuffer.toString());
        */

    }

    /**
     * Legt einen Benutzer an,
     * @param object
     * @throws IOServiceException
     */
    public synchronized void add(Object object) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        User user = (User)object;
        try {
            this.getByName(user.getUserName());
            //sorry user exists, throw DublicateEntry Exception
            throw new DublicateEntry("UserService.add(): DublicateEntry");
        } catch (ObjectNotFoundException e) {
            //okay - user does not exist, go on...
        }

        User tmpUser = new User();

        try {
            smc.insert("addUser",user);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        /*
        String sql = "INSERT INTO appuser (username) VALUES ('"+user.getUserName()+"')";
        Database.updateQuery(sql);
        */

        try {
            tmpUser = (User)this.getByName(user.getUserName());
        } catch (ObjectNotFoundException e) {
            throw new IOServiceException("DB ERROR: Can not add Object [Not Created]");
        }

        user.setUserId(tmpUser.getUserId());
        this.save(user);
    }

    public void createHomeCategory(User user) throws IOServiceException {

        if (user.getHomeCategoryId()==-1) {

            String homeCategoryName = user.getName()+user.getUserId();

            FolderService folderService = new FolderService();
            FolderMultiLang category = new FolderMultiLang();
            category.setParent(Config.homeCategoryId);
            category.setCatName(homeCategoryName);
            category.setCatTitle(user.getName());
            category.setCatTitleLng1(user.getName());
            category.setCatTitleLng2(user.getName());
            folderService.addFolder(category);

            try {
                category = (FolderMultiLang) folderService.getFolderByName(homeCategoryName);
                user.setHomeCategoryId(category.getCategoryId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            //User-Berechtigung für die Kategorie setzen
            // standardmässig werden beim kategorie anlegen schon die Berechtigungen vom übergordneten übernommen:
            Acl acl = AclController.getAcl(category);
            try {
                acl.addPermission(user,new AclPermission("read"));
                AclController.setAcl(category,acl);
            } catch (PermissionAlreadyExistException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            this.save(user);
        }

    }

    public void deleteById(int id) throws IOServiceException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("deleteUser",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gibt eine Liste der SecurityGroups zurück, inklusive der "virtuellen" Visitors Gruppe
     * @return
     */
    public List getSecurityGroupList() {

        List securityList = new LinkedList();
        securityList.add(getSecurityGroupVisitors());
        securityList.addAll(getRealSecurityGroupList());

        return securityList;
    }

    /**
     * Gibt eine Liste von SecurityGroups (Berechtigungsgruppen) zurück, ausgenommen der nicht realen
     * Gruppe Visitors (= virtuelle Gruppe, stellvertretend für Benutzer die nicht angemeldet sind)
     * @return
     */
    public List getRealSecurityGroupList() {

        List securityList = new LinkedList();
        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            List loadList = smc.queryForList("getSecurityGroupList",new Integer(0));
            securityList.addAll(loadList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return securityList;

    }

    public static SecurityGroup getSecurityGroupVisitors() {

        SecurityGroup securityGroup1 = new SecurityGroup();
        securityGroup1.setId(0);
        securityGroup1.setName(Character.toString((char)214)+"ffentlich");
        return securityGroup1;
    }

    public void addSecurityGroup(SecurityGroup securityGroup) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            smc.insert("addSecurityGroup",securityGroup);

            //securitygroup laden und für alle auflösungen berechtigen
            List securityGroupList = getSecurityGroupList();
            Iterator securityGroups = securityGroupList.iterator();
            while (securityGroups.hasNext()) {
                SecurityGroup sgr = (SecurityGroup)securityGroups.next();
                if (sgr.getName().equalsIgnoreCase(securityGroup.getName())) {
                    //Für alle Auflösungen berechtigen:
                    List formatList = Config.downloadRes;
                    Iterator formats = formatList.iterator();
                    while (formats.hasNext()) {
                        AccessObject accessObject = (AccessObject)formats.next();
                        Acl acl = AclController.getAcl(accessObject);
                        try {
                            acl.addPermission(sgr,new AclPermission(AclPermission.READ));
                            AclController.setAcl(accessObject,acl);
                        } catch (PermissionAlreadyExistException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSecurityGroup(int id) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("deleteSecurityGroup",new Integer(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SecurityGroup getSecurityGroupById(int id) {

        Iterator securities = getSecurityGroupList().iterator();
        while (securities.hasNext()) {

            SecurityGroup sg = (SecurityGroup)securities.next();
            if (sg.getId()==id) return sg;

        }

        SecurityGroup securityGroup1 = new SecurityGroup();
        securityGroup1.setId(0);
        securityGroup1.setName("No Security Group -1");
        return securityGroup1;
    }

    /*

    private void readFromResultSet(ResultSet rs, User user) throws SQLException {

        user.setUserId(rs.getInt("userid"));
        user.setUserName(rs.getString("username"));
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setCellPhone(rs.getString("cellphone"));
        user.setFax(rs.getString("fax"));
        user.setCompany(rs.getString("company"));
        user.setCompanyType(rs.getString("companytype"));
        user.setStreet(rs.getString("street"));
        user.setCity(rs.getString("city"));
        user.setZipCode(rs.getString("zipCode"));
        user.setCountry(rs.getString("country"));

    }
    */

    public User getByEmail(String email) throws ObjectNotFoundException {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();

        User user = new User();

        try {
            user = (User)smc.queryForObject("getUserByEmail",email);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (user==null) {
            //user does not exist:
            throw new ObjectNotFoundException();
        }

        return user;

    }

    public void setEmailAsUsername() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            smc.update("setEmailAsUsername",null);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void setUsernameAsUsername() {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        try {
            smc.update("setUsernameAsUsername",null);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String activateAutologin(User user, HttpServletResponse response) {

        //cookie = base64(userid:autologinkey)
        try {
            User u = (User)this.getById(user.getUserId());
            String userid = Integer.toString(user.getUserId());
            String autologinkey = getRandomString(10);
            if (!u.getAutologinKey().isEmpty()) {
                autologinkey = u.getAutologinKey(); //Bestehenden Loginkey verwenden
            }
            String unencodedCookie = userid+":"+autologinkey;
            byte[] cookieEncoded = Base64.encodeBase64(unencodedCookie.getBytes());
            String cookieEncodedString = new String(cookieEncoded);
            user.setAutologinKey(autologinkey);
            u.setAutologinKey(autologinkey);
            save(u);
            Cookie cookie = new Cookie(AUTOLOGIN_COOKIENAME, cookieEncodedString);
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            return new String(cookieEncoded);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void deactivateAutologin(User user, HttpServletResponse response) {

        if (user.getRole()!=User.ROLE_UNDEFINED) {
            Cookie cookie = new Cookie(AUTOLOGIN_COOKIENAME, "");
            cookie.setMaxAge(-1);
            cookie.setComment("Expiring Cookie");
            response.addCookie(cookie);

            try {
                User u = (User)this.getById(user.getUserId());
                u.setAutologinKey("");
                this.save(u);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    public boolean processAutologin(HttpServletRequest request) {

        if (WebHelper.getUser(request).getUserId()==-1) { //Wenn der User nicht angemeldet ist das autologin prüfen
            String encoded = getAutologinCookieValue(request);
            if (encoded!=null) {
                byte[] decoded = Base64.decodeBase64(encoded.getBytes());
                String decodedString = new String(decoded);
                String[] token = decodedString.split(":");
                int userId = Integer.parseInt(token[0]);
                String autologinkey = token[1];
                try {
                    User user = (User)this.getById(userId);
                    if (user.getAutologinKey().equalsIgnoreCase(autologinkey)) {
                        //Einloggen
                        HttpSession session = request.getSession();
                        System.out.println("Userlogin autologin: user="+user.getUserName()+" from key="+autologinkey);
                        Config.lastLogin = new Date();
                        session.setAttribute("user",user);
                        return true;
                    } else {
                        return false;
                    }
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static String AUTOLOGIN_COOKIENAME = "autologin";

    public String getAutologinCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase(AUTOLOGIN_COOKIENAME)) {
                    if (!c.getValue().isEmpty()) {
                        return c.getValue();
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    private String getRandomString(int size) {

        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        //System.out.println(output);
        return output;
    }
}
