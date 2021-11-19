package com.jcloud.auth.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.jcloud.auth.config.component.extragranter.WxTokenGranter;
import com.jcloud.common.config.SystemProperty;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.*;
import com.jcloud.security.consts.SecurityConstants;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jiaxm
 * @date 2021/4/1
 */
@Service
public class LoginService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SystemProperty systemProperty;


    /**
     * 获取微信授权地址
     * @param fromUrl 微信授权成功后需要跳转的地址
     * @param request
     * @return
     */
    public String wxAuthorizationUrl(String fromUrl, HttpServletRequest request) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme(request.getScheme())
                .host(systemProperty.getDomain()).path(SecurityConstants.WX_CALLBACK_URL).queryParam(Const.LOGIN_SUCCESS_RETURN__URL, StringUtils.defaultString(fromUrl));
        String wxAuthorizationUrl = wxMpService.buildQrConnectUrl(uriComponentsBuilder.toUriString(), WxConsts.QrConnectScope.SNSAPI_LOGIN, UUIDUtils.genUUid());
        return "redirect:" + wxAuthorizationUrl;
    }


    /**
     * 微信授权服务器的回调，登录
     * @param params
     * @param request
     * @param response
     * @return 返回跳转地址
     */
    public String wxLogin(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(oAuth2ClientProperties.getClientId(), oAuth2ClientProperties.getClientSecret(), Collections.emptySet());
        // 使用微信授权方式
        params.put("grant_type", WxTokenGranter.GRANT_TYPE);
        try {
            OAuth2AccessToken token = tokenEndpoint.getAccessToken(usernamePasswordAuthenticationToken, params).getBody();
            CookieUtil.setCookie(systemProperty.getDomain(), response, SecurityConstants.TOKEN_COOKIE_NAME, token.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fromUrl = ObjectUtil.toStr(params.get(Const.LOGIN_SUCCESS_RETURN__URL));
        return StringUtils.isBlank(fromUrl) ? null : "redirect:" + fromUrl;
    }

    /**
     * 发送短信验证码
     * TODO 需要驗證用戶惡意攻擊
     * @param mobile
     * @return
     */
    public ResponseData sendSmsCode(String mobile) {
        ResponseData commonRespon = new ResponseData();
        if (StringUtils.isBlank(mobile)) {
            commonRespon.setMsg("未填写手机号");
            return commonRespon;
        }
        // 校验手机号
        if (!ValidatorUtil.isMobilePhone(mobile)) {
            commonRespon.setMsg("请填写正确的手机号");
            return commonRespon;
        }
        String redisKey = SecurityConstants.MOBILE_PHONE_CODE_KEY + mobile;
        // 获取redis中的code
        String redisCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(redisCode)) {
            redisCode = RandomUtil.randomNumbers(6);
        }
        boolean send= SendMsgUtil.sendMobileCode(mobile,new String[]{redisCode,String.valueOf(SecurityConstants.CODE_TIME)});
        if (send) {
            stringRedisTemplate.opsForValue().set(redisKey, redisCode, SecurityConstants.CODE_TIME, TimeUnit.MINUTES);
        }
        commonRespon.setCode(Const.CODE_SUCCESS);
        commonRespon.setMsg("发送成功");
        return commonRespon;
    }

    public void loginCode(String requestSn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(requestSn)) {
            return;
        }
        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100,4,150);
        stringRedisTemplate.opsForValue().set(requestSn, lineCaptcha.getCode(), SecurityConstants.CODE_TIME, TimeUnit.MINUTES);
        OutputStream out= response.getOutputStream();
        lineCaptcha.write(out);
        out.flush();
    }
}
