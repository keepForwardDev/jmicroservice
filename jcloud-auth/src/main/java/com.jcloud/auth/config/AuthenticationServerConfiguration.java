package com.jcloud.auth.config;

import cn.hutool.extra.spring.SpringUtil;
import com.jcloud.auth.config.component.*;
import com.jcloud.auth.config.component.extragranter.MobilePhoneTokenGranter;
import com.jcloud.auth.config.component.extragranter.WxTokenGranter;
import com.jcloud.common.config.SystemProperty;
import com.jcloud.security.config.component.resourceserver.UserAccessTokenConverter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ???????????????
 * @author jiaxm
 * @date 2021/3/25
 */
@Configuration
@EnableAuthorizationServer
@EnableWebSecurity
public class AuthenticationServerConfiguration extends AuthorizationServerConfigurerAdapter implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    @Autowired
    private CustomTokenService customTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${security.oauth2.confirmAccessPage:/oauth/confirm_access}")
    private String confirmAccessPage;

    @Value("${security.oauth2.errorPage:/oauth/error}")
    private String errorPage;

    @Autowired
    private RedisAuthorizationCodeServices redisAuthorizationCodeServices;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private SystemProperty systemProperty;

    /**
     * ????????????????????????????????????
     */
    private AuthenticationServerEntryPoint authenticationServerEntryPoint = new AuthenticationServerEntryPoint();

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // ?????????????????????????????????
        security.authenticationEntryPoint(authenticationServerEntryPoint);
        //security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new UserAccessTokenConverter());
        FrameworkEndpointHandlerMapping frameworkEndpointHandlerMapping = endpoints.getFrameworkEndpointHandlerMapping();
        // ????????????????????????
        Map<String, String> forwardMap = new HashMap<>();
        // ????????????????????????????????????
        forwardMap.put("/oauth/confirm_access", confirmAccessPage);
        forwardMap.put("/oauth/error", errorPage);
        frameworkEndpointHandlerMapping.setMappings(forwardMap);
        endpoints
                // ????????????
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                // ????????????????????????
                .userDetailsService(userDetailsService)
                // ?????????????????????
                .authenticationManager(authenticationManager)
                // ?????????????????? refresh_token
                .reuseRefreshTokens(false)
                // ??????????????????????????????
                .accessTokenConverter(accessTokenConverter)
                // ?????????????????????
                .exceptionTranslator(new CustomWebResponseExceptionTranslator())
                .tokenServices(customTokenService)
                .redirectResolver(new CustomRedirectResolver())
                .authorizationCodeServices(redisAuthorizationCodeServices);
        // ????????????????????????
        endpoints.tokenGranter(getTokenGranters(endpoints));
    }

    /**
     * ???????????????????????????????????????????????????OAuth2Authentication ??????access_token???key????????????redis???
     * ??????????????????uuid clientId scope MD5 ????????????
     * @return
     */
    @Bean
    public AuthenticationKeyGenerator authenticationKeyGenerator() {
        return new DefaultAuthenticationKeyGenerator();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        FilterChainProxy filterChainProxy = SpringUtil.getBean(BeanIds.SPRING_SECURITY_FILTER_CHAIN);
        filterChainProxy.getFilterChains().forEach(chain -> {
            chain.getFilters().stream().filter(r -> r instanceof BasicAuthenticationFilter).findFirst()
                    .ifPresent(r -> {
                        BasicAuthenticationFilter basicAuthenticationFilter = (BasicAuthenticationFilter) r;
                        Field field = ReflectionUtils.findField(BasicAuthenticationFilter.class, "authenticationEntryPoint");
                        try {
                            field.setAccessible(true);
                            field.set(basicAuthenticationFilter, authenticationServerEntryPoint);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
        });
    }


    private TokenGranter getTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        ClientDetailsService clientDetails = redisClientDetailsService;
        AuthorizationServerTokenServices tokenServices = customTokenService;
        AuthorizationCodeServices authorizationCodeServices = redisAuthorizationCodeServices;
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,
                requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(new SupportValidCodeAuthenticationManager(authenticationManager, stringRedisTemplate, systemProperty), tokenServices,
                    clientDetails, requestFactory));
        }
        // ?????????????????????
        MobilePhoneTokenGranter mobilePhoneTokenGranter = new MobilePhoneTokenGranter(authenticationManager, stringRedisTemplate, customTokenService, redisClientDetailsService, requestFactory);
        tokenGranters.add(mobilePhoneTokenGranter);
        // ??????????????????
        WxTokenGranter wxTokenGranter = new WxTokenGranter(authenticationManager, wxMpService, customTokenService, redisClientDetailsService, requestFactory);
        tokenGranters.add(wxTokenGranter);
        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(tokenGranters);
        return compositeTokenGranter;
    }
}
