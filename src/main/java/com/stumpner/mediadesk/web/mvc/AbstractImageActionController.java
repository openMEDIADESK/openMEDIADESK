package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.image.ImageVersion;
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.image.folder.Folder;
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
 * Abstrakter Controller der manipulationen (löschen, einfügen, kopieren, ...) einer Datei/Bildliste ermöglicht
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 05.01.2007
 * Time: 11:10:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageActionController extends AbstractImageSelectController {


    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        //aktionen ausführen:
        ImageVersionService imageService = new ImageVersionService();
        model.put("showMessage",new Integer(0));
        List actionAttributeList = new LinkedList();

        if (httpServletRequest.getParameter("insert")!=null) {
            actionAttributeList.add("insert");
            if (httpServletRequest.getParameter("insert").equalsIgnoreCase("selectedMedia")) {
                //markierte Bilder einfügen:
                try {

                    if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                        List imageList = (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                        Iterator images = imageList.iterator();
                        while (images.hasNext()) {
                            ImageVersion image = (ImageVersion)images.next();
                            //2 Möglichkeiten: insert + move
                            // - INSERT: Einfügen (kopieren)
                            // - MOVE: Verschieben (aus der Originalkategorie entfernen)
                            boolean doCopy = false;
                            if (httpServletRequest.getParameter("func")!=null) {
                                if (httpServletRequest.getParameter("func").equalsIgnoreCase("copy")) {
                                    doCopy = true;
                                }
                            } else {
                                doCopy = Config.copyImages;
                            }

                            if (doCopy) {
                                //Kopieren: Hier wird dann Insert aus der Subklasse aufgerufen
                                this.insert(image,httpServletRequest);
                                model.put("message","message.copy");
                                model.put("showMessage",new Integer(1));
                            } else {
                                //Verschieben: Hier wird dann Move aus der Subklasse aufgerufen
                                Map fromMap = new HashMap();
                                Object containerObject = new Object();
                                if (httpServletRequest.getSession()
                                        .getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM)!=null) {
                                    fromMap = (Map)httpServletRequest.getSession().
                                            getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM);
                                    containerObject = fromMap.get(image);

                                }
                                this.move(image,containerObject,httpServletRequest);
                                model.put("message","message.move");
                                model.put("showMessage",new Integer(1));
                            }
                        }
                        httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                    }

                } catch (DublicateEntry e) {
                    //Insert schlug fehl, weil es dieses Bild/Datei in dieser kategorie/folder bereits gibt
                    model.put("message","message.duplicateentry");
                    model.put("showMessage",new Integer(1));

                    httpServletRequest.getSession().setAttribute("message","message.duplicateentry");
                }
            }
            if (httpServletRequest.getParameter("insert").equalsIgnoreCase("cat")) {
                //Aus einer Kategorie importieren:
                String redirectTo = httpServletRequest.getParameter("redirectTo");
                String type = httpServletRequest.getParameter("type");
                String id = httpServletRequest.getParameter("id");
                String encodedRedirectUrl = httpServletResponse.encodeRedirectURL("categoryselector?type="+type+"&id="+id+"&redirectTo="+redirectTo);
                httpServletResponse.sendRedirect(encodedRedirectUrl);
                return null;
            }
            clearPopupCache(httpServletRequest);
        }
        if (httpServletRequest.getParameter("remove")!=null) {
            actionAttributeList.add("remove");
            if (httpServletRequest.getParameter("remove").equalsIgnoreCase("selectedMedia")) {
                //markierte Bilder aus der kategorie entfernen:
                if (httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
                    List imageList = (List)httpServletRequest.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                    Iterator images = imageList.iterator();
                    while (images.hasNext()) {
                        ImageVersion image = (ImageVersion)images.next();
                        //Hier wird dann Remove aus der Subklasse aufgerufen
                        this.remove(image,httpServletRequest);
                    }
                    httpServletRequest.getSession().removeAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
                }
                clearPopupCache(httpServletRequest);
            } else {
                //remove Parameter mit einem anderen Wert
                Integer ivid = Integer.parseInt(httpServletRequest.getParameter("remove"));
                ImageVersion image = imageService.getImageVersionById(ivid);
                this.remove(image, httpServletRequest);
            }
        }
        if (httpServletRequest.getParameter("delete")!=null) {
            actionAttributeList.add("delete");
            if (httpServletRequest.getParameter("delete").equalsIgnoreCase("selectedMedia")) {
                //markierte Bilder aus der kategorie entfernen:
                // NEU: mit rückfrage!!!
                String redirectTo = "";
                if (httpServletRequest.getParameter("redirectTo")!=null) {
                    redirectTo = httpServletRequest.getParameter("redirectTo");
                }
                String encodedRedirectUrl = httpServletResponse.encodeRedirectURL("deletemedia;jsessionid="+httpServletRequest.getSession().getId()+"?redirectTo="+redirectTo);
                httpServletResponse.sendRedirect(encodedRedirectUrl);
                return null;
            }
            clearPopupCache(httpServletRequest);
        }

        if (httpServletRequest.getParameter("lightbox")!=null) {
            actionAttributeList.add("lightbox");

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user")!=null) {
                //user eingeloggt
                User user = (User)session.getAttribute("user");
                LightboxService lightboxService = new LightboxService();
                if (httpServletRequest.getParameter("lightbox").equals("add")) {
                    //hinzufügen
                    lightboxService.addImageToLightbox(
                            Integer.parseInt((String)httpServletRequest.getParameter("ivid")), user.getUserId()
                    );
                } else {
                    //löschen
                    lightboxService.removeImageToLightbox(
                            Integer.parseInt((String)httpServletRequest.getParameter("ivid")), user.getUserId()
                    );
                }
            }
        }

        if (httpServletRequest.getParameter("shoppingCart")!=null) {
            actionAttributeList.add("shoppingCart");

            HttpSession session = httpServletRequest.getSession();
            if (session.getAttribute("user")!=null) {
                //user eingeloggt
                User user = (User)session.getAttribute("user");
                ShoppingCartService shoppingCartService = new ShoppingCartService();
                if (httpServletRequest.getParameter("shoppingCart").equals("add")) {
                    //hinzufügen
                    shoppingCartService.addImageToShoppingCart(
                            Integer.parseInt((String)httpServletRequest.getParameter("ivid")), user.getUserId()
                    );
                }
            }
        }

        if (actionAttributeList.size()>0) {
            //NEW: Eine Action wurde durchgeführt:
            //es wird die aufgerufene Seite nochmal - aber ohne Action aufgerufen (ohne Action)
            //System.out.println(httpServletRequest.getQueryString());
            String qs = httpServletRequest.getQueryString();
            Iterator actions = actionAttributeList.iterator();
            while (actions.hasNext()) {
                String action = (String)actions.next();
                //System.out.println("actions="+action);
                qs = qs.replaceAll(action+"="+httpServletRequest.getParameter(action),"REPAR");
            }
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(httpServletRequest.getRequestURI())+"?"+qs);
            return null;
        } else {

            if (httpServletRequest.getSession().getAttribute("message")!=null) {
                httpServletRequest.setAttribute("showMessage",1);
                httpServletRequest.setAttribute("message",httpServletRequest.getSession().getAttribute("message"));
                httpServletRequest.getSession().removeAttribute("message");
            }
            return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    protected abstract void insert(ImageVersion image, HttpServletRequest request) throws DublicateEntry;

    protected abstract void remove(ImageVersion image, HttpServletRequest request);

    protected void move(ImageVersion image, Object fromContainerObject, HttpServletRequest request) {

        Logger logger = Logger.getLogger(AbstractImageActionController.class);
        try {
            this.insert(image,request);
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //Original Entfernen
        logger.debug("Original entfernen");
        if (fromContainerObject==null) {
            System.out.println("Original-Objekt existiert nicht");
            logger.info("Original-Objekt existiert nicht");
        } else {
            if (fromContainerObject instanceof Folder) {
                Folder folder = (Folder)fromContainerObject;
                logger.debug("Original war ein Folder: "+folder.getFolderId()+" "+folder.getFolderName());
                FolderService folderService = new FolderService();
                folderService.deleteImageFromFolder(folder,image);
            }
            if (fromContainerObject instanceof Category) {
                Category category = (Category)fromContainerObject;
                //if (category.getCategoryId()!=-1) {
                System.out.println("Original war eine Cat: "+category.getCategoryId()+" "+category.getCatName());
                    logger.debug("Original war eine Cat: "+category.getCategoryId()+" "+category.getCatName());
                    CategoryService categoryService = new CategoryService();
                    categoryService.deleteImageFromCategory(category,image);
                //} else {
                //    logger.debug("Original war eine 00: Aktuellste Bilder - entfernen nicht moeglich");
                //}
            }
        }

    }

    private void clearPopupCache(HttpServletRequest request) {

            request.getSession().removeAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST);
            request.getSession().removeAttribute(Resources.SESSIONVAR_POPUP_ALLIMAGELIST_CACHER);

    }

}
