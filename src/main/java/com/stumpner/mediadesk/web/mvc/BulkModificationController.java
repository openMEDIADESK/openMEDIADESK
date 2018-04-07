package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.MediaSearchService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.BulkModification;
import com.stumpner.mediadesk.search.*;
import com.stumpner.mediadesk.image.util.BulkModificationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * User: franz.stumpner
 * Date: 21.02.2007
 * Time: 21:09:58
 * To change this template use File | Settings | File Templates.
 */
public class BulkModificationController extends SimpleFormControllerMd {

    public BulkModificationController() {

        this.setCommandClass(BulkModification.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        if (!BulkModificationService.inProgress()) {

            BulkModification object = new BulkModification();
            List imageList = this.getImageList(httpServletRequest);
            object.setImageCount(imageList.size());
            super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
            return object;

        } else {

            BulkModification object = new BulkModification();
            object.setImageCount(BulkModificationService.getImageVersionList().size());
            object.setImageProcessed(BulkModificationService.getProcessed());
            object.setRedrawWatermark(BulkModificationService.isProcessWatermark());
            object.setReimportMetadata(BulkModificationService.isProcessMetadata());
            object.setInProgress(true);
            return object;

        }
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException bindException) throws Exception {

        this.setContentTemplateFile("admin_bulkmodification.jsp",httpServletRequest);

        if (!BulkModificationService.inProgress()) {
            return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.
        } else {
            return super.showForm(httpServletRequest, httpServletResponse, bindException);    //To change body of overridden methods use File | Settings | File Templates.    
        }
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

        BulkModification bulk = (BulkModification)object;
        List imageVersionList = this.getImageList(httpServletRequest);

        if (httpServletRequest.getParameter("docancel")==null) {

            //Konfiguration an das Bulk-Modification-Service übergeben
            if (bulk.isReimportMetadata()) {
                logger.debug("BulkModificationController: reimport Metadata...");
                BulkModificationService.setProcessMetadata(true);
            }
            if (bulk.isRedrawWatermark()) {
                logger.debug("BulkModificationController: redraw Watermark...");
                BulkModificationService.setProcessWatermark(true);
            }

            if (bulk.isReimportMetadata() || bulk.isRedrawWatermark()) {
                BulkModificationService.startModification(imageVersionList);
            }

            bulk.setInProgress(true);

        } else {

            BulkModificationService.cancel();
            bulk.setHalted(true);
            bulk.setInProgress(false);

        }

        bulk.setImageProcessed(BulkModificationService.getProcessed());
        this.setContentTemplateFile("admin_bulkmodification.jsp",httpServletRequest);
        return super.onSubmit(httpServletRequest, httpServletResponse, object, bindException);    //To change body of overridden methods use File | Settings | File Templates.
    }


    private List getImageList(HttpServletRequest httpServletRequest) {

        MediaSearchService imageSearch = new MediaSearchService();
        int viewPage = 1;
        //todo: alle gefundenen bilder (nicht nur erste!)
        SearchResult searchResult = new SearchResult();
        String searchString = "";
            //suche aus session
            searchResult = (SearchResult) httpServletRequest.getSession().getAttribute("search");
            ISearchProperty sp = searchResult.getSearchProperty();
            if (sp instanceof MediaSearchProperty) {
                MediaSearchProperty ivsp = (MediaSearchProperty)sp;
                searchString = ivsp.getKeywords() + " " +ivsp.getPeople()+ " " +ivsp.getSite()+ " ";
                if (ivsp.getDateFrom()!=null) {
                    searchString = searchString + " > " +
                            SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).format(ivsp.getDateFrom());
                }
                if (ivsp.getDateTo()!=null) {
                    searchString = searchString + " - " +
                            SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).format(ivsp.getDateTo());
                }
            } else {
                searchString = sp.getKeywords();
            }

            if (searchResult.getSearchProperty() instanceof KeywordSearchProperty) {
                //keyword property
                searchResult = imageSearch.getImageQuery(
                        (KeywordSearchProperty)searchResult.getSearchProperty(),0,10000,this.getUser(httpServletRequest));

            } else if (searchResult.getSearchProperty() instanceof SimpleSearchProperty) {
                //simple search property (orphaned)
                searchResult = imageSearch.getOrphanedQuery(
                        (SimpleSearchProperty)searchResult.getSearchProperty(),0,10000);
            } else {
                searchResult = imageSearch.getAdvancedImageQuery(
                        (MediaSearchProperty)searchResult.getSearchProperty(),0,10000,this.getUser(httpServletRequest));
            }


        return searchResult;

    }



}
