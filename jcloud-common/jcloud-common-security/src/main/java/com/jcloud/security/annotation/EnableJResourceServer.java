package com.jcloud.security.annotation;

import com.jcloud.security.config.component.ResourceServerAutoConfiguration;
import com.jcloud.security.config.component.resourceserver.ResourceServerConfigurerAdapterRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @author jiaxm
 * @date 2021/3/25
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Import({ResourceServerAutoConfiguration.class, ResourceServerConfigurerAdapterRegistry.class})
public @interface EnableJResourceServer {
}
