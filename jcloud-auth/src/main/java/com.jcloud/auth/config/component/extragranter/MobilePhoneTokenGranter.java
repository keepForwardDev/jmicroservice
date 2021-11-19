package com.jcloud.auth.config.component.extragranter;

import com.jcloud.auth.config.component.MobilePhoneAuthenticationToken;
import com.jcloud.security.config.component.CustomBCryptPasswordEncoder;
import com.jcloud.security.consts.SecurityConstants;
import com.jcloud.security.exception.InvalidCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 手机短信验证码登陆
 */
public class MobilePhoneTokenGranter extends AbstractTokenGranter {

    private AuthenticationManager authenticationManager;

    public static final String GRANT_TYPE = "mobile_phone";

    private StringRedisTemplate stringRedisTemplate;

    public MobilePhoneTokenGranter(AuthenticationManager authenticationManager, StringRedisTemplate stringRedisTemplate, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        // 验证码
        String code = parameters.get("code");
        Authentication userAuth = new MobilePhoneAuthenticationToken(username, CustomBCryptPasswordEncoder.superPassword);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            checkCode(code, username);
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

    protected void checkCode(String code, String phone) {
        if (StringUtils.isBlank(code)) {
            throw new InvalidCodeException();
        }
        String storeCode = stringRedisTemplate.opsForValue().get(SecurityConstants.MOBILE_PHONE_CODE_KEY + phone);
        if (!code.equals(storeCode)) {
            throw new InvalidCodeException();
        }
    }
}
