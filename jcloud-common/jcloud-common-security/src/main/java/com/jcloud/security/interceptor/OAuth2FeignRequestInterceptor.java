package com.jcloud.security.interceptor;

import com.jcloud.common.consts.Const;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

/**
 * feign token 传递，内部通信无需鉴权
 * @author jiaxm
 * @date 2021/4/21
 */
@Component
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2ClientContext oAuth2ClientContext;

    public OAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext) {
        this.oAuth2ClientContext = oAuth2ClientContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        OAuth2AccessToken oAuth2AccessToken = oAuth2ClientContext.getAccessToken();
        if (oAuth2AccessToken != null) {
            template.header(HttpHeaders.AUTHORIZATION, String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, oAuth2AccessToken.getValue()));
        } else {
            template.header(Const.API_INNER_CALL_HEADER, template.feignTarget().name());
        }
    }
}
