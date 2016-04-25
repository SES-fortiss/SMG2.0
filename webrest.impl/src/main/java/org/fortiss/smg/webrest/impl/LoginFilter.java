package org.fortiss.smg.webrest.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fortiss.smg.webrest.impl.util.URLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFilter implements Filter {

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> nonceMap;

    private FilterConfig filterConfig;

    private static final Logger logger = LoggerFactory
            .getLogger(LoginFilter.class);

    /**
     * Checks whether a nonce has been used before or not
     * nonce = number used once
     * 
     * @param public_key
     * @param nonce
     * @param current_timestamp
     * @return
     */
    public static boolean checkNonce(String public_key, String nonce,
            long current_timestamp) {

        LoginFilter.cleanupNonce(current_timestamp);

        // first entry: public key
        // second entry: nonce + timestamp
        if (LoginFilter.nonceMap == null) {
        	//TODO: Was ist mit die Zahlen da 200, 0.9f, 1
            LoginFilter.nonceMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>(
                    200, 0.9f, 1);
        }

        // check whether entry exists
        ConcurrentHashMap<String, Long> concurrentHashMap = LoginFilter.nonceMap
                .get(public_key);
        if (concurrentHashMap == null) {
            ConcurrentHashMap<String, Long> newEntryMap = new ConcurrentHashMap<String, Long>(
                    20, 0.9f, 1);
            LoginFilter.nonceMap.put(public_key, newEntryMap);
        }
        concurrentHashMap = LoginFilter.nonceMap.get(public_key);

        // check whether the is there
        if (concurrentHashMap.containsKey(nonce)) {
            return false;
        } else {
            concurrentHashMap.put(nonce, current_timestamp);
            return true;
        }
    }

    /**
     * Deletes all entries that are older than the maximum timestamp difference
     * 
     * @param current_timestamp
     */
    private static void cleanupNonce(long current_timestamp) {
        if (LoginFilter.nonceMap != null) {
            Set<Entry<String, ConcurrentHashMap<String, Long>>> entrySet = LoginFilter.nonceMap
                    .entrySet();
            if (entrySet != null) {
                for (Entry<String, ConcurrentHashMap<String, Long>> entry : entrySet) {
                    Set<Entry<String, Long>> entrySetInner = entry.getValue()
                            .entrySet();
                    if (entrySetInner != null) {
                        for (Entry<String, Long> item : entrySetInner) {
                            if (current_timestamp - item.getValue() < Constants.MAX_TIMESTAMP_DIFFERENCE) {
                                // entry is ok
                            } else {
                                LoginFilter.nonceMap.get(entry.getKey())
                                        .remove(item.getKey());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
        LoginFilter.nonceMap = null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // This is very important and has to be for the first call of any
        // parameter
        request.setCharacterEncoding("UTF-8");

        // check on valid input
        Map<String, String[]> parameters = request.getParameterMap();
        if (!parameters.containsKey("signature")
                || !parameters.containsKey("accesskey")
                || !parameters.containsKey("timestamp")
                || !parameters.containsKey("nonce")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        String signature = "", accesskey = "", nonce = "";
        long userTimestamp = 0;

        String signature_temp = request.getParameter("signature");
        if (signature_temp.length() > 4) {
            signature = signature_temp;
            //dirty
            signature = signature.replace(" ", "+");
            logger.debug("Received signature: " + signature);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        String accesskey_tmp = request.getParameter("accesskey");
        if (accesskey_tmp.length() > 4) {
        	accesskey = accesskey_tmp;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        // we expect a unix timestamp
        String user_timestamp_temp = request.getParameter("timestamp");
        if (user_timestamp_temp.length() > 4) {
            userTimestamp = Long.parseLong(user_timestamp_temp)* 1000;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        String nonce_temp = request.getParameter("nonce");
        if (nonce_temp.length() > 4) {
            nonce = nonce_temp;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        // whole url with http and query
        String request_url = URLHelper.getFullURL(request);

        // switch off the signature
        int lastIndexOf = request_url.lastIndexOf("&signature=");

        if (lastIndexOf > 0) {
            request_url = request_url.substring(0, lastIndexOf);
        }

        long current_timestamp = new Date().getTime();

        boolean success = false;
		try {
			success = BundleFactory.getIKeyManager().checkSignature(
					accesskey, request_url, signature);
		} catch (TimeoutException e) {
			logger .info("No connection?", e.fillInStackTrace());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

        // check validity
        if (success) {
            // check whether this signature is recent
            if (Math.abs(current_timestamp - userTimestamp) < Constants.MAX_TIMESTAMP_DIFFERENCE) {

                // check nonce
                if (LoginFilter.checkNonce(accesskey, nonce, current_timestamp)) {

                    // Don't know what this is good for, probably not bad
                   chain.doFilter(request, response);

                } else {
                    // used before
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    LoginFilter.logger.debug(request.getRequestURI()
                            + " was accessed. User tried to resend a request");
                }
            } else {
                // sorry to old
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                LoginFilter.logger.debug(request.getRequestURI()
                        + " was accessed. Wrong timestamp");
            }

        } else {
            // sorry not valid
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            LoginFilter.logger.debug(request.getRequestURI()
                    + " was accessed. Unauthorized - Login abortes");
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

    }

}
