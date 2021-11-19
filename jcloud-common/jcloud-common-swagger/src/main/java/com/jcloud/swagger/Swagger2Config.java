package com.jcloud.swagger;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author jiaxm
 * @date 2021/4/6
 */
@Configuration
public class Swagger2Config {

    @EnableSwagger2
    @EnableKnife4j
    @Configuration
    protected class Config {
        @Autowired
        private SwaggerConfigProperty property;

        @Autowired
        private Environment environment;

        @Bean
        public Docket createRestApi() {
            //schema
            List<GrantType> grantTypes=new ArrayList<>();
            //密码模式
            ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant=new ResourceOwnerPasswordCredentialsGrant(property.getTokenUrl());
            grantTypes.add(resourceOwnerPasswordCredentialsGrant);

            OAuth oAuth=new OAuthBuilder().name("oauth2")
                    .grantTypes(grantTypes).build();
            //context
            //scope方位
            List<AuthorizationScope> scopes=new ArrayList<>();
            scopes.add(new AuthorizationScope("server","read all resources"));
            SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
            SecurityContext securityContext= new SecurityContextBuilder().securityReferences(CollectionUtil.toList(securityReference)).build();
            //schemas
            List<SecurityScheme> securitySchemes= CollectionUtil.newArrayList(oAuth);
            //securyContext
            List<SecurityContext> securityContexts=CollectionUtil.newArrayList(securityContext);
            ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .groupName(property.getGroupName())
                    .host(property.getHost())
                    .securityContexts(securityContexts).securitySchemes(securitySchemes)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(StringUtils.defaultString(property.getBasePackage(), "com.jcloud"))
                            //只扫描有api注解的类
                            .and(RequestHandlerSelectors.withClassAnnotation(Api.class)));
            if (property.getExcludePath().isEmpty()) {
                apiSelectorBuilder.paths(PathSelectors.any());
            } else {
                apiSelectorBuilder.paths(excludePredicate());
            }
            return apiSelectorBuilder.build();
        }


        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title(StringUtils.defaultString(property.getTitle(), environment.getProperty("spring.application.name")))
                    .contact(new Contact(property.getContactName(), property.getContactUrl(), property.getContactEmail()))
                    .description(property.getDescription())
                    .termsOfServiceUrl(property.getTermsOfServiceUrl())
                    .build();
        }

        private Predicate<String> excludePredicate() {
            return (t) -> {
                boolean match = true;
                for (String s : property.getExcludePath()) {
                    if (s.matches(t)) {
                        match = false;
                        break;
                    }
                }
                return match;
            };
        }
    }
}
