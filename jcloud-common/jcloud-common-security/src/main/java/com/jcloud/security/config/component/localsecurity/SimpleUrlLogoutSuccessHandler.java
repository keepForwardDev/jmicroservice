package com.jcloud.security.config.component.localsecurity;

import com.jcloud.common.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleUrlLogoutSuccessHandler extends
        AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (WebUtil.isAjaxRequest(request)) {
            request.getRequestDispatcher("/logoutSuccess").forward(request, response);
            return;
        }
        super.handle(request, response, authentication);
    }
}
