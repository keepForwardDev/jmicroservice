package com.jcloud.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 操作cookie
 */
public class CookieUtil {
    /**
     * 功能描述:
     * 〈得到Cookie的值, 不编码〉
     *
     * @params : [request, cookieName]
     * @return : java.lang.String

     * @date : 2019/12/31 11:59
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 功能描述:
     * 〈得到Cookie的值〉
     *
     * @params : [request, cookieName, isDecoder]
     * @return : java.lang.String

     * @date : 2019/12/31 11:59
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null){
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 功能描述:
     * 〈得到Cookie的值〉
     *
     * @params : [request, cookieName, encodeString]
     * @return : java.lang.String

     * @date : 2019/12/31 11:59
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null){
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码〉
     *
     * @params : [request, response, cookieName, cookieValue]
     * @return : void

     * @date : 2019/12/31 12:00
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * 根据serverName设置cookie
     * @param serverName
     * @param response
     * @param cookieName
     * @param cookieValue
     */
    public static void setCookie(String serverName, HttpServletResponse response, String cookieName, String cookieValue) {
        doSetCookie(serverName, response, cookieName, cookieValue, -1, "UTF-8");
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值 在指定时间内生效,但不编码〉
     *
     * @params : [request, response, cookieName, cookieValue, cookieMaxage]
     * @return : void

     * @date : 2019/12/31 12:00
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值 不设置生效时间,但编码〉
     *
     * @params : [request, response, cookieName, cookieValue, isEncode]
     * @return : void

     * @date : 2019/12/31 13:48
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值 在指定时间内生效, 编码参数〉
     *
     * @params : [request, response, cookieName, cookieValue, cookieMaxage, isEncode]
     * @return : void

     * @date : 2019/12/31 13:48
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值 在指定时间内生效, 编码参数(指定编码)〉
     *
     * @params : [request, response, cookieName, cookieValue, cookieMaxage, encodeString]
     * @return : void

     * @date : 2019/12/31 13:48
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(getDomainName(request), response, cookieName, cookieValue, cookieMaxage, encodeString);
    }

    /**
     * 功能描述:
     * 〈删除Cookie带cookie域名〉
     *
     * @params : [request, response, cookieName]
     * @return : void

     * @date : 2019/12/31 13:49
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值，并使其在指定时间内生效〉
     *  cookieMaxage : cookie生效的最大秒数
     *
     * @params : [request, response, cookieName, cookieValue, cookieMaxage, isEncode]
     * @return : void

     * @date : 2019/12/31 13:49
     */
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (null != request)// 设置域名的cookie
                cookie.setDomain(getDomainName(request));
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述:
     * 〈设置Cookie的值，并使其在指定时间内生效〉
     *  cookieMaxage : cookie生效的最大秒数
     * @params : [request, response, cookieName, cookieValue, cookieMaxage, encodeString]
     * @return : void

     * @date : 2019/12/31 13:49
     */
    private static final void doSetCookie(String serverName, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (StringUtils.isNotBlank(serverName))// 设置域名的cookie
                cookie.setDomain(serverName);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述:
     * 〈得到cookie的域名〉
     *
     * @params : [request]
     * @return : java.lang.String

     * @date : 2019/12/31 13:50
     */
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }
}
