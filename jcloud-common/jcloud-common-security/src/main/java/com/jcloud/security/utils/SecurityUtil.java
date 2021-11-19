package com.jcloud.security.utils;

import com.jcloud.security.bean.ShiroUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiaxm
 * @date 2021/3/23
 */
public class SecurityUtil {



    public static ShiroUser getCurrentUser() {
        ShiroUser user = null;
        if (getAuthentication() != null) {
            if (getAuthentication().getPrincipal() instanceof ShiroUser) {
                user = (ShiroUser) getAuthentication().getPrincipal();
            }
        }
        return user;
    }

    public static Authentication getAuthentication() {
        Authentication authentication = getSecurityContext().getAuthentication();
        if (authentication == null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (context != null) {
                authentication = context.getAuthentication();
            }
        }
        return authentication;
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
}
