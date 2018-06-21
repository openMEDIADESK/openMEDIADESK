package com.stumpner.mediadesk.web.mvc.common;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.FavoriteService;
import com.stumpner.mediadesk.core.database.sc.ShoppingCartService;
import com.stumpner.mediadesk.core.database.sc.MenuService;
import com.stumpner.mediadesk.web.mvc.util.LngLinkHelper;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.web.mvc.SuSIDEBaseController;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.menu.MenuType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

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
 * Diese Klasse schreibt Globale Daten (Config,...) in den Request für
 * SimpleFormControllerMd und/oder AbstractPageController.
 * User: franz.stumpner
 * Date: 09.05.2010
 * Time: 20:31:43
 * To change this template use File | Settings | File Templates.
 */
public class GlobalRequestDataProvider {

    private static String cacheFix = "";

    public static void writeToRequest(HttpServletRequest request, SuSIDEBaseController baseController) {

        request.setAttribute("requestedUri",request.getRequestURI());
        String fqurl = WebHelper.getServerNameUrlPathWithQueryString(request);
        request.setAttribute("fqurl", fqurl);
        try {
            request.setAttribute("fqurlEncoded", URLEncoder.encode(fqurl,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        request.setAttribute("uri",WebHelper.getServerNameUrlPath(request));
        request.setAttribute("url",fqurl);
        request.setAttribute("site",WebHelper.getServerNameUrl(request));

        request.setAttribute("useShoppingCart",new Boolean(Config.useShoppingCart));
        request.setAttribute("useLightbox",new Boolean(Config.useLightbox));
        request.setAttribute("useQuickDownload",new Boolean(Config.quickDownload));

        request.setAttribute("cacheFix", cacheFix);

        if (Config.webTitle.equalsIgnoreCase("mediaDESK (R)")) {
            //Settingsnotify: Wenn noch keine Einstellungen getätigt wurden dann ein notify icon anzeigen!
            request.setAttribute("notifySettings", true);
        }

        if (Config.showDownloadToVisitors && Config.quickDownload) {
            request.setAttribute("showDownload", new Boolean(true));
            request.setAttribute("showImageActionMenu", new Boolean(true));
        } else {
            if (baseController.isLoggedIn(request)) {
                request.setAttribute("showImageActionMenu", new Boolean(true));
                if (Config.quickDownload) {
                    request.setAttribute("showDownload", new Boolean(true));
                } else {
                    request.setAttribute("showDownload", new Boolean(false));
                }
            } else {
                request.setAttribute("showImageActionMenu", new Boolean(false));
                request.setAttribute("showDownload", new Boolean(false));
            }
        }

        //Sprachverlinkung de/en in das Request-Objekt schreiben
        LngLinkHelper.writeLngLinksToRequest(request);

        //ACL-Context erstellen für das jeweilige Principal
        AclContextFactory.createAclContext(request,baseController);

        int lightboxCount = 0;
        int shoppingCartCount = 0;

        if (request.getSession().getAttribute("user")!=null) {
            request.setAttribute("loggedIn",true);
            FavoriteService favoriteService = new FavoriteService();
            ShoppingCartService shoppingCartService = new ShoppingCartService();
            User user = (User) request.getSession().getAttribute("user");

            lightboxCount = favoriteService.getFavUserCount(user.getUserId());
            shoppingCartCount = shoppingCartService.getShoppingCartUserCount(user.getUserId());

            request.setAttribute("showLightbox", lightboxCount>0 || Config.useLightbox ? true : false);

            if (user.getRole()>=User.ROLE_MASTEREDITOR) {
                request.setAttribute("createRootCat",true);
            }
        } else {
            request.setAttribute("loggedIn",false);
        }

        request.setAttribute("lightboxCount",lightboxCount);
        request.setAttribute("shoppingCartCount",shoppingCartCount);

        LngResolver lngResolver = new LngResolver();
        MenuService menuService = new MenuService();
        menuService.setUsedLanguage(lngResolver.resolveLng(request));
        request.setAttribute("sideMenu",menuService.getMenu(MenuType.SIDE));
        request.setAttribute("topMenu",menuService.getMenu(MenuType.TOP));
        //New Bootstrap Template
        request.setAttribute("navLinks",menuService.getMenu(MenuType.NAV));
        request.setAttribute("footer2Links",menuService.getMenu(MenuType.FOOTER2));
        request.setAttribute("footer3Links",menuService.getMenu(MenuType.FOOTER3));
        request.setAttribute("footer4Links",menuService.getMenu(MenuType.FOOTER4));
        request.setAttribute("footer5Links",menuService.getMenu(MenuType.FOOTER5));
        //TOP-Menu Basis Menüpunkt
        request.setAttribute("showMenuUpload", getShowMenuUpload(request, baseController));
        request.setAttribute("showMenuPinmanager", getShowMenuPinmanager(request));
        request.setAttribute("showMenuUsermanager", getShowMenuUsermanager(request));
        request.setAttribute("showMenuSettings", getShowMenuSettings(request));

        Config.putDmsConfigToRequest(request);

        //Benutzerobjekt des eingeloggten Benutzers in den Request schreiben
        request.setAttribute("loggedInUser", WebHelper.getUser(request));
        //Todo Config in Request Objekt schreiben
        HashMap configMap = new HashMap();
        configMap.put("multiLang",new Boolean(Config.multiLang));
        configMap.put("advancedSearchEnabled", new Boolean(Config.advancedSearchEnabled));
        configMap.put("webTitle",Config.webTitle);
        configMap.put("creditSystemEnabled", new Boolean(Config.creditSystemEnabled));
        configMap.put("param", Config.configParam);
        configMap.put("allowRegister", Config.allowRegister);
        configMap.put("currency", Config.currency);

        //CustomFields
        configMap.put("customStr1", Config.customStr1);
        configMap.put("customStr2", Config.customStr2);
        configMap.put("customStr3", Config.customStr3);
        configMap.put("customStr4", Config.customStr4);
        configMap.put("customStr5", Config.customStr5);
        configMap.put("customStr6", Config.customStr6);
        configMap.put("customStr7", Config.customStr7);
        configMap.put("customStr8", Config.customStr8);
        configMap.put("customStr9", Config.customStr9);
        configMap.put("customStr10", Config.customStr10);

        //Footer
        configMap.put("footerCorpLink", Config.footerCorpLink);
        configMap.put("footerCopyright", Config.footerCopyright);
        configMap.put("footerCorpSite", Config.footerCorpSite);

        request.setAttribute("config",configMap);

        //Lic
        request.setAttribute("licFunc",Config.licFunc);
        request.setAttribute("licId",Config.licId);

    }

    private static boolean getShowMenuSettings(HttpServletRequest request) {
        User user = getUser(request);
        if (user!=null) {
            if (user.getRole()==User.ROLE_ADMIN)
            { return true; } else { return false; }
        } else {
            return false;
        }
    }

    private static User getUser(HttpServletRequest request) {
        if (request.getSession().getAttribute("user")!=null) {
            return (User)request.getSession().getAttribute("user");
        } else {
            return null;
        }
    }

    private static boolean getShowMenuUsermanager(HttpServletRequest request) {
        User user = getUser(request);
        if (user!=null) {
            if (user.getRole()==User.ROLE_HOME_EDITOR || user.getRole()==User.ROLE_ADMIN)
            { return true; } else { return false; }
        } else {
            return false;
        }
    }

    private static boolean getShowMenuPinmanager(HttpServletRequest request) {
        User user = getUser(request);
        if (user!=null) {
            if (user.getRole()>=User.ROLE_PINMAKLER)
            { return true; } else { return false; }
        } else {
            return false;
        }
    }

    private static boolean getShowMenuUpload(HttpServletRequest request, SuSIDEBaseController baseController) {
        User user = getUser(request);
        if (user!=null) {
            if (user.getRole()>=User.ROLE_PINEDITOR || user.getRole()==User.ROLE_IMPORTER) {
                /*   //todo: code wenn das upload-menü nicht angezeigt werden soll
                if (user.getRole()==User.ROLE_IMPORTER || user.getRole()==User.ROLE_EDITOR) {
                    //Abhängig von der ACL
                    if (baseController instanceof FolderIndexController) {
                        //Wenn es in einer Kategorie ist
                        FolderIndexController c = ((FolderIndexController)baseController);
                        Folder folder = c.getFolder(request);
                        AclControllerContext aclCtx = AclContextFactory.getAclContext(request);
                        try {
                            return aclCtx.checkPermission(new AclPermission("write"), folder);
                        } catch (AclNotFoundException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else if (user.getRole()==User.ROLE_PINEDITOR) {

                    if (baseController instanceof PinPicViewController) {
                        PinPicViewController p = (PinPicViewController)baseController;
                        Pin pin = p.getPin(request);
                        if (pin.getCreatorUserId()==user.getUserId()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }

                } else {
                    //Alle anderen können immer  */
                    return true;
                //}
            } else { return false; }
        } else {
            return false;
        }
    }

    public static void doCacheFix() {

        cacheFix = "?v="+System.currentTimeMillis();
    }

}
