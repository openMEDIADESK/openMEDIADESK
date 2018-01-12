package com.stumpner.mediadesk.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.text.SimpleDateFormat;

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
 * Date: 02.05.2005
 * Time: 20:54:36
 * To change this template use File | Settings | File Templates.
 */
public class FolderIndexViewController extends AbstractPageController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        /** Deprecated: seit 3.3 gibt es keine Folder mehr:
         *
         */

        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("cat"));
        return null;
        /*

        FolderService folderService = new FolderService();
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(httpServletRequest));
        List folderListLines = new LinkedList();
        List folderListLine = new LinkedList();
        LinkedList folderListPage = new LinkedList();
        AclControllerContext aclCtx = AclContextFactory.getAclContext(httpServletRequest);

        PaginatedList folderList = null;
        List folderCountList = null;
        int folderCount = 0;
        if (this.getUser(httpServletRequest).getRole()>User.ROLE_EDITOR) {
            //anzahl der Folder berechnen:
            folderCountList = folderService.getFolderList(0);
            folderCount = folderCountList.size();
            //Folderinhalt laden:
            folderList = folderService.getFolderListPages(Config.itemCountPerPage);

        } else {
            //anzahl der Folder berechnen:
            folderCountList = folderService.getVisibleFolderList();
            folderCount = folderCountList.size();
            //Folderinhalt laden:
            folderList = folderService.getVisibleFolderListPages(Config.itemCountPerPage);
        }



        int numberOfPages = folderCount/Config.itemCountPerPage;
        if (folderCount%Config.itemCountPerPage!=0) numberOfPages++;
        //while (folderList.nextPage()) {
        //    folderList.gotoPage(getNumberOfPages);
        //    getNumberOfPages++;
        //}
        //getNumberOfPages=21;

        int viewPage = 0;
        if (httpServletRequest.getParameter("page")!=null) {
            if (httpServletRequest.getParameter("page").length()>0) {
                try {
                    viewPage = Integer.parseInt(
                            httpServletRequest.getParameter("page"))-1;
                } catch (NumberFormatException e) {
                    //keine gültige nummer: nichts tun...
                }
            }
        }
        folderList.gotoPage(viewPage);

        List permittedFolderList = null;
        if (Config.aclOnlyShowPermittetObjects && this.getUser(httpServletRequest).getRole()!=User.ROLE_ADMIN) {
            permittedFolderList = aclCtx.getPermittedList(new AclPermission("read"),folderList);
            //todo: anzeige berichtigen: derzeit werden zwar die berechtigten Folder angezeigt, aber die seiten-anzeige wird trotzdem direkt aus der datenbank geladen (ohne summenprüfung) 

        } else {
            permittedFolderList = folderList;
        }

        //Da es mit der direkten übergabe der Paginated List probleme gab (letzte wurde immer doppelt angezeigt)
        //wurde es so gelöst:
        List viewFolderList = new ArrayList();
        Iterator fs = permittedFolderList.iterator();
        while (fs.hasNext()) {
            Folder folder = (Folder)fs.next();
            viewFolderList.add(folder);
        }

        httpServletRequest.setAttribute("folderList",viewFolderList);
        httpServletRequest.setAttribute("pageSize",Integer.toString(folderList.size()));
        httpServletRequest.setAttribute("pageIndex",Integer.toString(folderList.getPageIndex()+1));

        httpServletRequest.setAttribute("numberOfPages",Integer.toString(numberOfPages));
        String nextPage = (folderList.nextPage()) ? Integer.toString(viewPage+1+1) :"0";
        String prevPage = (folderList.previousPage()) ? Integer.toString(viewPage-1+1) :"0";
        httpServletRequest.setAttribute("nextPage",nextPage);
        httpServletRequest.setAttribute("prevPage",prevPage);

        this.setContentTemplateFile("folderindexview.jsp",httpServletRequest);
        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
        */
    }

    protected void initBinder(HttpServletRequest httpServletRequest, ServletRequestDataBinder servletRequestDataBinder) throws Exception {

        servletRequestDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"),true));
    }

}
