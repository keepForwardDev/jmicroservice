package com.jcloud.security.config.component.resourceserver;

import cn.hutool.core.bean.BeanUtil;
import com.jcloud.security.bean.ShiroUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Map;

/**
 * /oauth/check_token 在授权服务器，将用户信息传递过来，传过来在资源服务器 构建OAuth2Authentication
 * 因此授权服务器，跟资源服务器要用同一个转换器
 * @author jiaxm
 * @date 2021/3/30
 */
public class UserAccessTokenConverter implements UserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        ShiroUser shiroUser = (ShiroUser) userAuthentication.getPrincipal();
        Map<String, Object> toMap = BeanUtil.beanToMap(shiroUser);
        toMap.remove("password");
        return toMap;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        ShiroUser user = BeanUtil.mapToBean(map, ShiroUser.class, false);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        return usernamePasswordAuthenticationToken;
    }
}
