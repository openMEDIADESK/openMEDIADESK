package com.stumpner.mediadesk.web.mvc.util;

import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.LngResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 04.03.2010
 * Time: 22:17:09
 * To change this template use File | Settings | File Templates.
 */
public class LngLinkHelper {

    public static void writeLngLinksToRequest(HttpServletRequest httpServletRequest) {

        //Sprachsteuerung (Umschalt-Urls zwischen deutsch und englisch)
        String dePage = Config.redirectStartPage + "?lng=de";
        String enPage = Config.redirectStartPage + "?lng=en";
        if (isLngUriPrefix(httpServletRequest.getRequestURI())) {
            dePage = getPage("en","de",httpServletRequest);
            enPage = getPage("de","en",httpServletRequest); //httpServletRequest.getRequestURI().replaceFirst("/de/","/en/");
        }
        httpServletRequest.setAttribute("dePage",dePage);
        httpServletRequest.setAttribute("enPage",enPage);

        //Lng im Request setzen um mit <c:out value="${lng}"/> darauf zugreifen zu können
        LngResolver lngResolver = new LngResolver();
        lngResolver.resolveLng(httpServletRequest);

    }

    private static String getPage(String lngFrom, String lngTo, HttpServletRequest request) {

        StringBuffer Url = new StringBuffer(WebHelper.getServerNameUrlPathWithQueryString(request).replaceFirst("/"+lngFrom+"/","/"+lngTo+"/"));
        /*
        if (request.getQueryString()!=null) {
            Url.append("?");
            Url.append(request.getQueryString());
        } */

        return Url.toString();

    }

    /**
     * Gibt zurück ob der URI mit einer Sprache anfängt (z.b. /de, /en,...
     * @param uri
     * @return
     */
    private static boolean isLngUriPrefix(String uri) {

        if (uri.startsWith("/de")) return true;
        if (uri.startsWith("/en")) return true;

        return false;
    }

}
