package com.jcloud.auth.controller;

import com.jcloud.auth.config.component.CustomRedisTokenStore;
import com.jcloud.auth.service.LoginService;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.security.consts.SecurityConstants;
import com.jcloud.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录，post 提交 username，password，到/login,oauth2 登录 /oauth/token
 *
 * 微信
 *
 */
@Api(tags = "用户登录")
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CustomRedisTokenStore customRedisTokenStore;


    /**
     * 登录失败处理，内部调用
     * @param request
     * @return
     */
    @RequestMapping("loginFailure")
    @ResponseBody
    @ApiIgnore
    public ResponseData loginFailure(HttpServletRequest request) {
        ResponseData respon = new ResponseData();
        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception instanceof BadCredentialsException) {
            respon.setMsg("账号或者密码错误！");
        } else if (exception instanceof DisabledException) {
            respon.setMsg("该账号已被禁用！");
        } else if (exception instanceof InternalAuthenticationServiceException) {
            respon.setMsg(exception.getMessage());
        } else {
            respon.setMsg(exception.getMessage());
        }
        return respon;
    }

    @RequestMapping("loginSuccess")
    @ResponseBody
    @ApiIgnore
    public ResponseData loginSuccess() {
        ResponseData respon = ResponseData.getSuccessInstance();
        respon.setData(SecurityUtil.getCurrentUser());
        respon.setMsg("登录成功！");
        return respon;
    }

    @RequestMapping("noLogin")
    @ResponseBody
    @ApiIgnore
    public ResponseData noLogin(HttpServletRequest request) {
        ResponseData respon = new ResponseData();
        respon.setMsg(Const.CODE_NO_LOGIN_STR);
        respon.setCode(Const.CODE_NO_LOGIN);
        Object fromUrl = request.getAttribute("fromUrl");
        if (fromUrl != null) {
            respon.setReserveData(fromUrl);
        }
        return respon;
    }

    @RequestMapping("badClient")
    @ResponseBody
    @ApiIgnore
    public ResponseData badClient() {
        ResponseData respon = new ResponseData();
        respon.setMsg(SecurityConstants.BAD_CLIENT_CREDENTIALS);
        respon.setCode(Const.CODE_ERROR);
        return respon;
    }

    @RequestMapping("logoutSuccess")
    @ResponseBody
    @ApiIgnore
    public ResponseData logoutSuccess() {
        return new ResponseData(Const.CODE_SUCCESS, Const.LOGOUT_SUCCESS_STR);
    }


    /**
     * 微信oauth2 授权地址
     * @param fromUrl 必须传递登录成功后的跳转地址，只需要传递 uri + params 即可，domain 在这里自动加上
     * @return
     */
    @ApiOperation(value = "微信二维码扫码地址")
    @RequestMapping(value = "wxAuthorizationUrl", method = RequestMethod.GET)
    public String wxAuthorizationUrl(@ApiParam(value = "最终微信登录成功后跳转的系统地址", required = true) @RequestParam(required = true, value = "fromUrl") String fromUrl, @ApiIgnore HttpServletRequest request) {
        return loginService.wxAuthorizationUrl(fromUrl, request);
    }

    /**
     * 微信回调登录
     * @param params
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "微信oauth2登录成功后的回调地址")
    @ApiResponse(code = 301, message = "跳转到fromUrl")
    @RequestMapping(value = "wxLogin", method = RequestMethod.GET)
    public String wxLogin(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        return loginService.wxLogin(params, request, response);
    }

    /**
     * 發送短信验证码
     * @param phone
     * @return
     */
    @ApiOperation(value = "发送短信验证码")
    @RequestMapping(value = "/sendSmsCode/{phone}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData sendSmsCode(@PathVariable String phone) {
        return loginService.sendSmsCode(phone);
    }


    @ApiOperation(value = "生成登录验证码")
    @RequestMapping(value = "/loginCode", method = RequestMethod.GET)
    public void loginCode(String requestSn, HttpServletRequest request, HttpServletResponse response) throws Exception {
        loginService.loginCode(requestSn, request, response);
    }

    @ApiOperation(value = "推出登录")
    @RequestMapping(value = "/logoutSystem", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData logout(String token) {
        if (StringUtils.isNotBlank(token)) {
            OAuth2AccessToken auth2AccessToken = customRedisTokenStore.readAccessToken(token);
            if (auth2AccessToken != null) {
                customRedisTokenStore.removeAccessToken(token);
                customRedisTokenStore.removeRefreshToken(auth2AccessToken.getRefreshToken());
            }
        }
        return ResponseData.getSuccessInstance();
    }
}
