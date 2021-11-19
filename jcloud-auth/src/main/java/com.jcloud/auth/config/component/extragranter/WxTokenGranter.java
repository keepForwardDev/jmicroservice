package com.jcloud.auth.config.component.extragranter;

import cn.hutool.core.bean.BeanUtil;
import com.jcloud.auth.config.component.UnionIdAuthenticationToken;
import com.jcloud.security.config.component.CustomBCryptPasswordEncoder;
import com.jcloud.security.exception.InvalidCodeException;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提供微信登陆方式
 * 首先生成二维码，生成二维码后，设置微信回调接口，在回调接口上，获取用户信息注册，生成token保存，
 * 然后重定向到传递回来的 fromUrl，设置session 保存token?
 *
 */
public class WxTokenGranter extends AbstractTokenGranter {

    private AuthenticationManager authenticationManager;

    public static final String GRANT_TYPE = "wechat";


    private WxMpService wxMpService;


    public WxTokenGranter(AuthenticationManager authenticationManager, WxMpService wxMpService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.wxMpService =wxMpService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        OAuth2AccessToken oAuth2AccessToken = super.getAccessToken(client, tokenRequest);
        return oAuth2AccessToken;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        // 微信返回的授权码
        String code = parameters.get("code");
        WxOAuth2UserInfo wxUser = getWxUserInfo(code);
        Authentication userAuth = new UnionIdAuthenticationToken(BeanUtil.beanToMap(wxUser), CustomBCryptPasswordEncoder.superPassword);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        }
        catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        }
        catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invalid grant
            throw new InvalidGrantException(e.getMessage());
        } catch (InvalidCodeException e) {
            throw e;
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

    protected WxOAuth2UserInfo getWxUserInfo(String code) {
        try {
            // 获取access_token
            WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, null);
            return wxMpUser;
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

}
