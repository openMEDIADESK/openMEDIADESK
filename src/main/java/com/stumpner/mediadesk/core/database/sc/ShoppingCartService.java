package com.stumpner.mediadesk.core.database.sc;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.common.util.PaginatedList;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.*;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.image.MediaObject;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.web.mvc.util.WebHelper;
import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.http.HttpServletRequest;

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
 * Date: 17.05.2005
 * Time: 21:46:40
 * To change this template use File | Settings | File Templates.
 */
public class ShoppingCartService extends MultiLanguageService {

    public class Checkout {
        public BigDecimal total = BigDecimal.valueOf(0);
        public BigDecimal subtotalbeforedeposit = BigDecimal.valueOf(0);
        public BigDecimal deposit = BigDecimal.valueOf(0); //Guthaben aus gutschein
        public BigDecimal subtotal = BigDecimal.valueOf(0);
        public BigDecimal vatPercent = BigDecimal.valueOf(Config.vatPercent); //20% Mwst
        public BigDecimal vat = BigDecimal.valueOf(0);
        public int totalInCents = 0;
        public List mediaObjectList = null;
        public User user = null;
        public String transactionDescription = "";
    }

    public Checkout createCheckoutInfo(HttpServletRequest request) {

        Checkout checkout = new Checkout();
        LngResolver lngResolver = new LngResolver();
        ShoppingCartService shoppingCartService = new ShoppingCartService();
        checkout.user = WebHelper.getUser(request);
        shoppingCartService.setUsedLanguage(lngResolver.resolveLng(request));
        checkout.mediaObjectList = shoppingCartService.getShoppingCartImageList(checkout.user.getUserId());
        Iterator mediaObjects = checkout.mediaObjectList.iterator();
        while (mediaObjects.hasNext()) {
            MediaObjectMultiLang mo = (MediaObjectMultiLang)mediaObjects.next();
            if (!Config.currency.isEmpty()) {
                checkout.total = checkout.total.add(mo.getPrice());
            } else {
                checkout.total = checkout.total.add(BigDecimal.valueOf(1));
            }
            if (checkout.transactionDescription.length()==0) {
                checkout.transactionDescription = mo.getVersionName();
            }
        }
        if (checkout.mediaObjectList.size()>1) { checkout.transactionDescription=checkout.transactionDescription+",..."; }
        checkout.subtotalbeforedeposit = checkout.total;
        //Guthaben abziehen
        if (checkout.user.getCredits().compareTo(BigDecimal.valueOf(0))>0) {
            if (checkout.total.compareTo(checkout.user.getCredits())<0) {
                //total ist kleiner als das guthaben (reicht locker aus)
                checkout.deposit = checkout.total;
            } else {
                //total ist größer als das guthaben (reicht nicht aus)
                checkout.deposit = checkout.user.getCredits();
            }
            checkout.total = checkout.total.subtract(checkout.deposit);
        }
        checkout.subtotal = checkout.total;
        if (checkout.user.getVatPercent()!=-1) {
            checkout.vatPercent = BigDecimal.valueOf(checkout.user.getVatPercent());
        }
        checkout.vat = checkout.subtotal.divide(BigDecimal.valueOf(100), 99, RoundingMode.HALF_UP).multiply(checkout.vatPercent).setScale(2, RoundingMode.HALF_UP);
        checkout.total = checkout.subtotal.add(checkout.vat).setScale(2, RoundingMode.HALF_UP);
        checkout.totalInCents = checkout.total.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();

        return checkout;
    }

    public List getShoppingCartImageList(int userId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        List imageList = new LinkedList();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(userId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForList("getShoppingCartImageList",loaderClass);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;
    }

    public PaginatedList getShoppingCartImagesPaginatedList(int userId, int imagesPerPage) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        PaginatedList imageList = null;
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(userId);
        loaderClass.setUsedLanguage(getUsedLanguage());

        try {
            imageList = smc.queryForPaginatedList("getShoppingCartImageList",loaderClass,imagesPerPage);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return imageList;

    }

    public void addImageToShoppingCart(int ivid, int userId) {

        FavoriteMediaDescriptor lid = new FavoriteMediaDescriptor();
        //zuerst löschen damit die bilder nicht doppelt drin sind (kommt nur zur wirkung wenn es noch nicht drin is)
        this.removeImageToShoppingCart(ivid,userId);
        lid.setIvid(ivid);
        lid.setUserId(userId);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.insert("addImageToShoppingCart",lid);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void removeImageToShoppingCart(int ivid, int userId) {

        FavoriteMediaDescriptor lid = new FavoriteMediaDescriptor();
        lid.setIvid(ivid);
        lid.setUserId(userId);
        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("removeImageFromShoppingCart",lid);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteImageFromAllShoppingCart(int ivid) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        try {
            smc.delete("removeImageFromAllShoppingCart",new Integer(ivid));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setPayTransactionId(int userId, String payTransactionId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();

        List mediaList = this.getShoppingCartImageList(userId);
        Iterator mo = mediaList.iterator();
        while (mo.hasNext()) {
            CartObject iv = (CartObject)mo.next();
            iv.setPayTransactionId(payTransactionId);

            try {
                smc.update("setPayTransactionId",iv);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void removeImagesToShoppingCart(List imageList, int userId) {

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject imageVersion = (MediaObject)images.next();
            this.removeImageToShoppingCart(imageVersion.getIvid(),userId);
        }
    }

    public void addImagesToShoppingCart(List imageList, int userId) {

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject imageVersion = (MediaObject)images.next();
            this.addImageToShoppingCart(imageVersion.getIvid(),userId);
        }
    }

    public int getShoppingCartUserCount(int userId) {

        SqlMapClient smc = AppSqlMap.getSqlMapInstance();
        Integer count = new Integer(-1);

        try {
            count = (Integer)smc.queryForObject("getShoppingCartUserCount",new Integer(userId));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return count.intValue();
    }

    /**
     * Gibt das BasicMediaObject-Object zurück wenn sich diese ivid im ShoppinCart des Benutzers befindet
     * @param userid
     * @param ivid
     * @return
     */
    public CartObject getCartObject(int userid, int ivid) {

        List imageList = getShoppingCartImageList(userid);
        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            CartObject i = (CartObject)images.next();
            if (i.getIvid()==ivid) {
                return i;
            }
        }

        return null;
    }

    public boolean isInCart(int userid, int ivid) {

        List<CartObject> imageList = getShoppingCartImageList(userid);

        for (CartObject i : imageList) {
            if (i.getIvid()==ivid) return true;
        }

        return false;
    }




}
