package com.stumpner.mediadesk.search;

import com.stumpner.mediadesk.core.Config;
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
 * User: franz.stumpner
 * Date: 27.03.2007
 * Time: 19:07:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class SearchProperty implements ISearchProperty {

    String keywords = "";
    int usedLanguage = 0;
    boolean aclEnabled = false;
    int userPrincipal = -1;
    int groupPrincipal = -1;

    public String getKeywords() {
        return getSearchTypeString(keywords);
    }

    /**
     * Gibt je nach typ der Suche (UND/ODER) den richtigen boolschen Operator-String zurück
     * z.b.
     * AND-SUCHE:
     * eishockey wels
     * wird zu: +eishockey +wels
     * @param searchString
     * @return veränderter boolscher Operator String
     */
    protected String getSearchTypeString(String searchString) {

        Logger logger = Logger.getLogger(SearchProperty.class);

        //je nach abhängigkeit von Search-Einstellungen (And=true: UND-Verknüpfung)

        if (Config.searchAnd) {
            //UND-Verknüpfung
            logger.debug("SUCH-TYP: UND-Verknüpfung");
            logger.debug("Original Keyword: "+searchString);
            if (searchString.contains("+") || searchString.contains("-")) {
                //wenn eine boolische suche verwendet wird, dann keine UND-Verknüpfung!!
                logger.debug("SUCHE enthält boolsche Operatoren, UND-Verknüpfung abgebrochen");
                return searchString;
            } else {
                //Und-Verknüpfung aus Keywords machen:
                String kw[] = searchString.split(" ");
                String andKeyword = "";

                if (kw.length>1) {

                    for (int p=0;p<kw.length;p++) {
                        if (p>0)
                            andKeyword = andKeyword+" ";

                        andKeyword = andKeyword+"+"+kw[p];
                    }
                } else {
                    //ein einziges Keyword ohne "+" verwenden, da es sonst probleme
                    //bei bildnummernsuche gibt
                    andKeyword = kw[0];
                }
                //sicherheitsabfrage, damit keyword nicht nur + ist
                if (andKeyword.equalsIgnoreCase("+")) andKeyword = "";
                logger.debug("Search-Keyword: "+andKeyword);
                return andKeyword;
            }
        } else {
            //ODER-Verknüpfung
            logger.debug("SUCH-TYP: ODER-Verknüpfung");
            return searchString;
        }

    }

    public int getUsedLanguage() {
        return usedLanguage;
    }

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    /**
     * Setzt das Zugriffsrecht/Security-Group mit dem gesucht wird...
     * @param
     * @deprecated use setUserPrincipal() or setGroupPrincipal()
     */
    //public void setPrincipalId(int principalId) {
    //    this.principalId = principalId;
    //}

    //public int getPrincipalId() {
    //    return principalId;
    //}

    public int getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(int userPrincipal) {
        this.userPrincipal = userPrincipal;
        this.aclEnabled = true;
    }

    public int getGroupPrincipal() {
        return groupPrincipal;
    }

    public void setGroupPrincipal(int groupPrincipal) {
        this.groupPrincipal = groupPrincipal;
        this.aclEnabled = true;
    }

    public boolean isAclEnabled() {
        return this.aclEnabled;
    }
}
