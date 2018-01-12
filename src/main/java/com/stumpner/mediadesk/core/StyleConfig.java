package com.stumpner.mediadesk.core;

import com.stumpner.mediadesk.util.IniFile;

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
 * Date: 29.06.2005
 * Time: 22:21:44
 * To change this template use File | Settings | File Templates.
 */
public class StyleConfig {

    public static String sepHorizSmall = "";
    public static String sepHorizLarge = "";
    public static String sepVertical = "";
    public static String boxStyle = "";
    public static String boxStyleColor = "";
    public static String boxTextColor = "";
    public static String boxTextColorHover = "";
    public static String boxTextDecoHover = "";
    public static String backgroundColor = "";
    public static String footerFont = "";
    public static String boxHeadlineFont = "";
    public static String boxHeadlineColor = "";
    public static String linkStyleFont = "";
    public static String linkStyleColor = "";
    public static String linkStyleColorHover = "";
    public static String linkStyleDecoHover = "";
    public static String cartLinkFont = "";
    public static String cartLinkColor = "";
    public static String cartLinkColorHover = "";
    public static String cartLinkDecoHover = "";
    public static String pathLinkFont = "";
    public static String pathLinkColor = "";
    public static String pathLinkColorHover = "";
    public static String pathLinkDecoHover="";
    public static String menuLinkFont = "";
    public static String menuLinkColor = "";
    public static String menuLinkColorHover = "";
    public static String menuLinkDecoHover = "";
    public static String headlineFont = "";
    public static String headlineColor = "";
    public static String headlineSize = "";
    public static String subheadlineFont = "";
    public static String subheadlineColor = "";
    public static String subheadlineSize = "";
    public static String errorFont = "";
    public static String errorColor = "";

    public static void initConfiguration(IniFile iniFile) {

        sepHorizSmall = iniFile.getProperty("sepHorizSmall","");
        sepHorizLarge = iniFile.getProperty("sepHorizLarge","");
        sepVertical = iniFile.getProperty("sepVertical","");
        boxStyle = iniFile.getProperty("boxStyle","");
        boxStyleColor = iniFile.getProperty("boxStyleColor","");
        boxTextColor = iniFile.getProperty("boxTextColor","");
        boxTextColorHover = iniFile.getProperty("boxTextColorHover","");
        boxTextDecoHover = iniFile.getProperty("boxTextDecoHover","");
        backgroundColor = iniFile.getProperty("backgroundColor","");
        footerFont = iniFile.getProperty("footerFont","");
        boxHeadlineFont = iniFile.getProperty("boxHeadlineFont","");
        boxHeadlineColor = iniFile.getProperty("boxHeadlineColor","");
        linkStyleFont = iniFile.getProperty("linkStyleFont","");
        linkStyleColor = iniFile.getProperty("linkStyleColor","");
        linkStyleColorHover = iniFile.getProperty("linkStyleColorHover","");
        linkStyleDecoHover = iniFile.getProperty("linkStyleDecoHover","");
        cartLinkFont = iniFile.getProperty("cartLinkFont","");
        cartLinkColor = iniFile.getProperty("cartLinkColor","");
        cartLinkColorHover = iniFile.getProperty("cartLinkColorHover","");
        cartLinkDecoHover = iniFile.getProperty("cartLinkDecoHover","");
        pathLinkFont = iniFile.getProperty("pathLinkFont","");
        pathLinkColor = iniFile.getProperty("pathLinkColor","");
        pathLinkColorHover = iniFile.getProperty("pathLinkColorHover","");
        pathLinkDecoHover= iniFile.getProperty("pathLinkDecoHover","");
        menuLinkFont = iniFile.getProperty("menuLinkFont","");
        menuLinkColor = iniFile.getProperty("menuLinkColor","");
        menuLinkColorHover = iniFile.getProperty("menuLinkColorHover","");
        menuLinkDecoHover = iniFile.getProperty("menuLinkDecoHover","");
        headlineFont = iniFile.getProperty("headlineFont","");
        headlineColor = iniFile.getProperty("headlineColor","");
        headlineSize = iniFile.getProperty("headlineSize","");
        subheadlineFont = iniFile.getProperty("subheadlineFont","");
        subheadlineColor = iniFile.getProperty("subheadlineColor","");
        subheadlineSize = iniFile.getProperty("subheadlineSize","");
        errorFont = iniFile.getProperty("errorFont","Tahoma, Verdana, Arial, Helvetica, sans-serif");
        errorColor = iniFile.getProperty("errorColor","#990000");

    }

}
