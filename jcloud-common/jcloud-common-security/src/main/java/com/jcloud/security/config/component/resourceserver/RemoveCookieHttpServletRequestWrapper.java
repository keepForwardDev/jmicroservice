package com.jcloud.security.config.component.resourceserver;

import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedList;
import java.util.List;

/**
 * 包装HttpServletRequest，移除某些特定的cookie
 * @author jiaxm
 * @date 2021/12/30
 */
public class RemoveCookieHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final List<String> cookieNames;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public RemoveCookieHttpServletRequestWrapper(HttpServletRequest request, List<String> cookieNames) {
        super(request);
        Assert.notNull(cookieNames, "name is required");
        this.cookieNames = cookieNames;
    }

    @Override
    public Cookie[] getCookies() {
        List<Cookie> cookieList = new LinkedList<>();
        HttpServletRequest request = (HttpServletRequest) getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (!cookieNames.contains(cookie.getName())) {
                    cookieList.add(cookie);
                }
            }
        }
        return cookieList.toArray(new Cookie[]{});
    }
}
