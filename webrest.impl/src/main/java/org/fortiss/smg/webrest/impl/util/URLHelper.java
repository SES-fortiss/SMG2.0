package org.fortiss.smg.webrest.impl.util;

import javax.servlet.http.HttpServletRequest;

public class URLHelper {

    /**
     * gets the whole url with http and query
     * 
     * @param request
     * @return
     */
    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
