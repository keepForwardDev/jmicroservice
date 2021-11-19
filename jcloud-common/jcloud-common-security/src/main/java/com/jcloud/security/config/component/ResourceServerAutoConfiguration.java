package com.jcloud.security.config.component;

import com.jcloud.security.config.component.resourceserver.CustomRemoteTokenServices;
import com.jcloud.security.config.component.resourceserver.ResourceServerSecurityChainRequestMatcher;
import com.jcloud.security.config.component.resourceserver.UserAccessTokenConverter;
import com.jcloud.security.interceptor.OAuth2HttpRequestInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 *
 * @author jiaxm
 * @date 2021/3/25
 */
public class ResourceServerAutoConfiguration {


    @ConfigurationProperties(value = "security.oauth2.ignore")
    @Bean
    public IgnoreUrlProperties ignoreUrlProperties() {
        return new IgnoreUrlProperties();
    }

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate lbRestTemplate()
    {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
        restTemplate.getInterceptors().add(new OAuth2HttpRequestInterceptor(oAuth2ClientContext));
        restTemplate.setErrorHandler(
                new DefaultResponseErrorHandler()
                {
                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException
                    {
                        if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value())
                        {
                            super.handleError(response);
                        }
                    }
                });
        return restTemplate;
    }

    @Primary
    @Bean(name = "customRemoteTokenServices")
    public CustomRemoteTokenServices customRemoteTokenServices(ResourceServerProperties resourceServerProperties) {
        CustomRemoteTokenServices services = new CustomRemoteTokenServices();
        services.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
        services.setClientId(resourceServerProperties.getClientId());
        services.setClientSecret(resourceServerProperties.getClientSecret());
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new UserAccessTokenConverter());
        services.setAccessTokenConverter(accessTokenConverter);
        return services;
    }

    @Bean
    public ResourceServerSecurityChainRequestMatcher resourceServerSecurityChainRequestMatcher() {
        return new ResourceServerSecurityChainRequestMatcher();
    }

}
