package com.stumpner.mediadesk.core.database.sc;

import com.ibatis.sqlmap.client.SqlMapClient;

import java.util.*;
import java.sql.SQLException;

import com.stumpner.mediadesk.core.database.AppSqlMap;
import com.stumpner.mediadesk.search.KeywordSearchProperty;
import com.stumpner.mediadesk.search.MediaSearchProperty;
import com.stumpner.mediadesk.search.SearchResult;
import com.stumpner.mediadesk.search.ISearchProperty;
import com.stumpner.mediadesk.usermanagement.User;
import org.apache.log4j.Logger;

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
 * Date: 20.05.2005
 * Time: 15:34:54
 * To change this template use File | Settings | File Templates.
 */
public class MediaSearchService extends MediaService {

    static Logger logger = Logger.getLogger(MediaSearchService.class);

    private int lastUniqueId = 0;

    public int getImageQueryCount(ISearchProperty keywordSearchProperty) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        Integer resultCount = new Integer(0);
        keywordSearchProperty.setUsedLanguage(getUsedLanguage());

        try {
            resultCount = (Integer)smc.queryForObject("getImageQueryCount",keywordSearchProperty);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return resultCount.intValue();
    }

    /**
     *
     * @param keywordSearchProperty
     * @param page is starting at 0 for the first page!
     * @param itemsPerPage
     * @return
     */
    public SearchResult getImageQuery(KeywordSearchProperty keywordSearchProperty, int page, int itemsPerPage, User user) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        keywordSearchProperty.setUsedLanguage(getUsedLanguage());
        SearchResult searchResult = new SearchResult();

        if (keywordSearchProperty.getSuid()==0) {
            //prepare statement
            keywordSearchProperty.setSuid(
                this.getUniqueId()
            );
            //alt: if (Config.useAcl & Config.aclOnlyShowPermittetObjects) {
            if (true) {
                keywordSearchProperty.setGroupPrincipal(user.getSecurityGroup());
                keywordSearchProperty.setUserPrincipal(user.getUserId());
            }
            //prüfen auf imagenumber (wenn imagenumber vorkommt, diese(s) bild(er) zurückgeben
            boolean imageNumberFound = false;
            try {
                long start = System.currentTimeMillis();
                logger.debug("{QUERY-TIME SearchService->getImageNumberQuery} start "+start);
                List imageList = smc.queryForList("getImageNumberQuery",keywordSearchProperty);
                logger.debug("{QUERY-TIME SearchService->getImageNumberQuery} finish ["+(System.currentTimeMillis()-start)+" ms]");
                if (imageList.size()>0) {
                    logger.debug("MediaSearchService.getImageQuery: found ImageNumber matching {"+keywordSearchProperty.getKeywords()+"}, count: "+imageList.size());
                    logger.debug("{QUERY-TIME SearchService->prepareImageNumberQuery} start "+start);
                    smc.update("prepareImageNumberQuery",keywordSearchProperty);
                    logger.debug("{QUERY-TIME SearchService->prepareImageNumberQuery} finish ["+(System.currentTimeMillis()-start)+" ms]");
                    imageNumberFound = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!imageNumberFound) {
                //abfrage ausführen
                try {
                    long start = System.currentTimeMillis();
                    logger.debug("{QUERY-TIME SearchService->prepareImageQuery} start "+start);
                    smc.update("prepareImageQuery",keywordSearchProperty);
                    logger.debug("{QUERY-TIME SearchService->prepareImageQuery} finish ["+(System.currentTimeMillis()-start)+" ms]");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Check ob das Ergebnis nur eine bestimmte seite oder alle bilder ausgeben soll
        if (page!=-1) {
            searchResult.setThisPage(page);
            searchResult.setSearchProperty(keywordSearchProperty);
            searchResult.setItemsPerPage(itemsPerPage);
            searchResult.setResultCount(getImageQueryCount(keywordSearchProperty));
            searchResult.setPageCount(
                    //anzahl der Seiten ausrechnen
                    (searchResult.getResultCount()%itemsPerPage > 0) ? (searchResult.getResultCount()/itemsPerPage) + 1 : searchResult.getResultCount()/itemsPerPage
            );
            keywordSearchProperty.setStartItem((page)*itemsPerPage);
            keywordSearchProperty.setItemCount(itemsPerPage);
        } else {
            keywordSearchProperty.setItemCount(-1);
        }


        List imageList = new ArrayList();

        try {
            long start = System.currentTimeMillis();
            logger.debug("{QUERY-TIME SearchService->getImageQuery} start "+start);
            imageList = smc.queryForList("getImageQuery",keywordSearchProperty);
            logger.debug("{QUERY-TIME SearchService->getImageQuery} finish ["+(System.currentTimeMillis()-start)+" ms]");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        searchResult.addAll(imageList);

        //----





        //----

        return searchResult;
    }

    public SearchResult getOrphanedQuery(ISearchProperty searchProperty, int page, int itemsPerPage) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        searchProperty.setUsedLanguage(getUsedLanguage());
        SearchResult searchResult = new SearchResult();

        if (searchProperty.getSuid()==0) {
            //prepare statement
            searchProperty.setSuid(
                this.getUniqueId()
            );
            //abfrage ausführen
            try {
                smc.update("prepareOrphanedQuery",searchProperty);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Check ob das Ergebnis nur eine bestimmte seite oder alle bilder ausgeben soll
        if (page!=-1) {
            searchResult.setThisPage(page);
            searchResult.setSearchProperty(searchProperty);
            searchResult.setItemsPerPage(itemsPerPage);
            searchResult.setResultCount(getImageQueryCount(searchProperty));
            searchResult.setPageCount(
                    //anzahl der Seiten ausrechnen
                    (searchResult.getResultCount()%itemsPerPage > 0) ? (searchResult.getResultCount()/itemsPerPage) + 1 : searchResult.getResultCount()/itemsPerPage
            );
            searchProperty.setStartItem((page)*itemsPerPage);
            searchProperty.setItemCount(itemsPerPage);
        } else {
            searchProperty.setItemCount(-1);
        }

        List imageList = null;

        //wenn etwas nicht ausgefüllt wurde, mit joker versehen (=alle finden)
        //if (image.getPeople().length()==0 && image.getSite().length()>0) { image.setPeople("*"); }
        //if (image.getSite().length()==0 && image.getPeople().length()>0) { image.setSite("*"); };

        try {
            //if (imageVersionSearchProperty.getPeriod()==MediaSearchProperty.PERIOD_NONE) {

                imageList = smc.queryForList("getImageQuery",searchProperty);
            //} else {
            //    imageList = smc.queryForList("getAdvancedImagePeriodQuery",imageVersionSearchProperty);
            //}
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        searchResult.addAll(imageList);

        return searchResult;
    }

    public SearchResult getAdvancedImageQuery(MediaSearchProperty mediaSearchProperty, int page, int itemsPerPage, User user) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        SearchResult searchResult = new SearchResult();
        mediaSearchProperty.setUsedLanguage(getUsedLanguage());

        if (mediaSearchProperty.getSuid()==0) {
            //prepare statement
            mediaSearchProperty.setSuid(
                this.getUniqueId()
            );
            //if (Config.useAcl & Config.aclOnlyShowPermittetObjects) {
            if (true) {
                mediaSearchProperty.setGroupPrincipal(user.getSecurityGroup());
                mediaSearchProperty.setUserPrincipal(user.getUserId());
            }
            //prüfen auf imagenumber (wenn imagenumber vorkommt, diese(s) bild(er) zurückgeben
            try {
                long start = System.currentTimeMillis();
                logger.debug("{QUERY-TIME SearchService->getImageNumberQuery} start "+start);
                List imageList = smc.queryForList("getImageNumberQuery", mediaSearchProperty);
                logger.debug("{QUERY-TIME SearchService->getImageNumberQuery} finish ["+(System.currentTimeMillis()-start)+" ms]");
                if (imageList.size()>0) {
                    logger.debug("MediaSearchService.getImageQuery: found ImageNumber matching {"+ mediaSearchProperty.getKeywords()+"}, count: "+imageList.size());
                    logger.debug("{QUERY-TIME SearchService->prepareImageNumberQuery} start "+start);
                    smc.update("prepareImageNumberQuery", mediaSearchProperty);
                    logger.debug("{QUERY-TIME SearchService->prepareImageNumberQuery} finish ["+(System.currentTimeMillis()-start)+" ms]");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //abfrage ausführen
            try {
                smc.update("prepareAdvancedImageQuery", mediaSearchProperty);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Check ob das Ergebnis nur eine bestimmte seite oder alle bilder ausgeben soll
        if (page!=-1) {
            searchResult.setThisPage(page);
            searchResult.setSearchProperty(mediaSearchProperty);
            searchResult.setItemsPerPage(itemsPerPage);
            searchResult.setResultCount(getImageQueryCount(mediaSearchProperty));
            searchResult.setPageCount(
                    //anzahl der Seiten ausrechnen
                    (searchResult.getResultCount()%itemsPerPage > 0) ? (searchResult.getResultCount()/itemsPerPage) + 1 : searchResult.getResultCount()/itemsPerPage
            );

            mediaSearchProperty.setStartItem((page)*itemsPerPage);
            mediaSearchProperty.setItemCount(itemsPerPage);
        } else {
            mediaSearchProperty.setItemCount(-1);
        }

        List imageList = null;

        //wenn etwas nicht ausgefüllt wurde, mit joker versehen (=alle finden)
        //if (image.getPeople().length()==0 && image.getSite().length()>0) { image.setPeople("*"); }
        //if (image.getSite().length()==0 && image.getPeople().length()>0) { image.setSite("*"); };

        try {
            //if (mediaSearchProperty.getPeriod()==MediaSearchProperty.PERIOD_NONE) {
            
                imageList = smc.queryForList("getImageQuery", mediaSearchProperty);
            //} else {
            //    imageList = smc.queryForList("getAdvancedImagePeriodQuery",mediaSearchProperty);
            //}
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        searchResult.addAll(imageList);

        return searchResult;

    }

    public SearchResult getReQuery(ISearchProperty keywordSearchProperty, int page, int itemsPerPage) {

        SqlMapClient smc =AppSqlMap.getSqlMapInstance();
        keywordSearchProperty.setUsedLanguage(getUsedLanguage());
        SearchResult searchResult = new SearchResult();

        //if (keywordSearchProperty.getSuid()==0) {
            //prepare statement
            keywordSearchProperty.setRequery(
                    this.getUniqueId()
            );
            //abfrage ausführen
            try {
                smc.update("prepareReQuery",keywordSearchProperty);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            keywordSearchProperty.setSuid(keywordSearchProperty.getRequery());
        //}

        // Check ob das Ergebnis nur eine bestimmte seite oder alle bilder ausgeben soll
        if (page!=-1) {
            searchResult.setThisPage(page);
            searchResult.setSearchProperty(keywordSearchProperty);
            searchResult.setItemsPerPage(itemsPerPage);
            searchResult.setResultCount(getImageQueryCount(keywordSearchProperty));
            searchResult.setPageCount(
                    //anzahl der Seiten ausrechnen
                    (searchResult.getResultCount()%itemsPerPage > 0) ? (searchResult.getResultCount()/itemsPerPage) + 1 : searchResult.getResultCount()/itemsPerPage
            );
            keywordSearchProperty.setStartItem((page)*itemsPerPage);
            keywordSearchProperty.setItemCount(itemsPerPage);
        } else {
            keywordSearchProperty.setItemCount(-1);
        }

        List imageList = new ArrayList();

        try {
            imageList = smc.queryForList("getImageQuery",keywordSearchProperty);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        searchResult.addAll(imageList);

        return searchResult;
    }

    private int getUniqueId() {

        int uniqueId = 0;

        while (uniqueId==lastUniqueId) {
          uniqueId = GregorianCalendar.getInstance().get(GregorianCalendar.MILLISECOND)+
                     GregorianCalendar.getInstance().get(GregorianCalendar.SECOND)*100 +
                     GregorianCalendar.getInstance().get(GregorianCalendar.MINUTE)*10000;
        }

        lastUniqueId = uniqueId;
        return uniqueId;


    }

}
