package com.jcloud.security.config.component.resourceserver;

import com.jcloud.security.config.component.IgnoreUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestTemplate;

/**
 * 搭建资源服务器，
 * 资源服务器配置后会拦截token，然后去授权服务器获取用户信息
 * 详细流程在 OAuth2AuthenticationProcessingFilter  OAuth2AuthenticationManager
 *
 *
 * @author jiaxm
 * @date 2021/3/25
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private IgnoreUrlProperties ignoreUrlProperties;

    @Autowired
    private CustomRemoteTokenServices customRemoteTokenServices;

    @Autowired
    private RestTemplate lbRestTemplate;

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();

    @Autowired
    private AccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private ResourceServerSecurityChainRequestMatcher resourceServerSecurityChainRequestMatcher;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        SupportCookieTokenExtractor tokenExtractor = new SupportCookieTokenExtractor();
        // 防止集群
        customRemoteTokenServices.setRestTemplate(lbRestTemplate);
        resources.tokenExtractor(tokenExtractor);
        resources.tokenServices(customRemoteTokenServices);
        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(resourceServerSecurityChainRequestMatcher);
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint);
        http.authorizeRequests().antMatchers(ignoreUrlProperties.getIgnoreUrls()).permitAll().
                anyRequest().authenticated();
    }



}
