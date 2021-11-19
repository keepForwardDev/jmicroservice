package com.jcloud.security.interceptor;

import com.jcloud.common.consts.Const;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.IOException;

/**
 * restTemplate 支持token传递，内部调用时，无需登录鉴权
 * @author jiaxm
 * @date 2021/11/2
 */
public class OAuth2HttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2ClientContext oAuth2ClientContext;

    public OAuth2HttpRequestInterceptor(OAuth2ClientContext oAuth2ClientContext) {
        this.oAuth2ClientContext = oAuth2ClientContext;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        OAuth2AccessToken oAuth2AccessToken = oAuth2ClientContext.getAccessToken();
        if (oAuth2AccessToken != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, oAuth2AccessToken.getValue()));
        } else {
            String serviceName = request.getURI().getHost();
            request.getHeaders().add(Const.API_INNER_CALL_HEADER, serviceName);
        }
        return execution.execute(request, body);
    }
}
