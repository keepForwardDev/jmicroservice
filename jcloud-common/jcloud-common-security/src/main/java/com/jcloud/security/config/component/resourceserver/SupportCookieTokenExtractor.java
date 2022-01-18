package com.jcloud.security.config.component.resourceserver;

import com.jcloud.security.consts.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jiaxm
 * @date 2021/4/20
 */
public class SupportCookieTokenExtractor extends BearerTokenExtractor {


    @Override
    protected String extractHeaderToken(HttpServletRequest request) {
        String token = super.extractHeaderToken(request);
        if (StringUtils.isBlank(token)) { //判断有无cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (SecurityConstants.TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isBlank(token)) {
            token = null;
        }
        return token;
    }
}
