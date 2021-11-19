package com.jcloud.auth.config;

import com.jcloud.auth.config.component.MultiTypeAuthenticationProvider;
import com.jcloud.common.consts.Const;
import com.jcloud.security.config.component.CustomBCryptPasswordEncoder;
import com.jcloud.security.config.component.IgnoreUrlProperties;
import com.jcloud.security.config.component.localsecurity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

/**
 * 本地security chain
 * 使用的時候直接加入spring 管理
 * @author jiaxm
 * @date 2021/3/25
 */
@EnableWebSecurity
@Import({RemoteUserDetailsService.class})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RemoteUserDetailsService userDetailsService;

    @Autowired
    private IgnoreUrlProperties ignoreUrlProperties;

    @Value("${security.oauth2.loginUrl}")
    private String loginUrl;

    @Autowired
    private CustomBCryptPasswordEncoder customBCryptPasswordEncoder;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 配置资源拦截访问
     * @param http
     * @throws Exception
     */
    protected void configure(HttpSecurity http) throws Exception {
//        http.
//                formLogin().
//                successHandler(new CustomSavedRequestAwareAuthenticationSuccessHandler()).and().
//                authorizeRequests().anyRequest().authenticated().and();

        http.csrf().disable().httpBasic().disable().formLogin().disable();
        http.logout().logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler());
        if (ignoreUrlProperties.getUrls().size() > 0) {
            http.authorizeRequests().antMatchers(ignoreUrlProperties.getUrls().toArray(new String[]{})).permitAll();
        }
        http.authorizeRequests().anyRequest().authenticated();
        UserNamePasswordFilterConfigurer userNamePasswordConfigurer = new UserNamePasswordFilterConfigurer();
        userNamePasswordConfigurer.failureHandler(new ForwardAuthenticationFailureHandler(Const.LOGIN_FAILURE_URL));
        userNamePasswordConfigurer.successHandler(new CustomSavedRequestAwareAuthenticationSuccessHandler());
        http.apply(userNamePasswordConfigurer);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(customLoginUrlAuthenticationEntryPoint());
        // 添加自定义登录验证
        MultiTypeAuthenticationProvider multiTypeAuthenticationProvider = new MultiTypeAuthenticationProvider(userDetailsService, customBCryptPasswordEncoder);
        http.authenticationProvider(multiTypeAuthenticationProvider);
    }


    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/wxAuthorizationUrl", "/wxLogin", loginUrl, "/error");
        if (ignoreUrlProperties.getPublicUrls().size() > 0) {
            web.ignoring().mvcMatchers(ignoreUrlProperties.getPublicUrls().toArray(new String[] {}));
        }
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(customBCryptPasswordEncoder);
    }

    @Bean
    public CustomLoginUrlAuthenticationEntryPoint customLoginUrlAuthenticationEntryPoint() {
        return new CustomLoginUrlAuthenticationEntryPoint(loginUrl);
    }

    @ConfigurationProperties(value = "security.oauth2.ignore")
    @Bean
    public IgnoreUrlProperties ignoreUrlProperties() {
        return new IgnoreUrlProperties();
    }
}
