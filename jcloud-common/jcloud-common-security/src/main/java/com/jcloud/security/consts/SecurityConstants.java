package com.jcloud.security.consts;

/**
 * @author jiaxm
 * @date 2021/3/25
 */
public class SecurityConstants {

    /**
     * 所有角色的前缀需要加上这个
     */
    private static final String ROLE_PREFIX = "ROLE_";

    /***
     * 资源服务器默认bean名称
     */
    public static final String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";


    public static final String BAD_CREDENTIALS = "账号或密码错误！";

    public static final String INVALID_REFRESH_TOKEN = "刷新令牌错误！";

    public static final String INVALID_CODE_MSG = "授权码错误!";

    /**
     * 登出在redis中的key + token
     */
    public final static String KICKED_OUT_REDIS_KEY = "kicked_out_";

    /**
     * oauth2授权码模式 code key
     */
    public final static String OAUTH_CODE_KEY = "oauth_code_";

    /**
     * 手机号验证码存储的key
     */
    public final static String MOBILE_PHONE_CODE_KEY = "mobile_phone_code_";

    public final static String KICKED_OUT_MSG = "账号在其他地方登录，当前登录已失效，请重新登录！";

    public final static String INVALID_TOKEN_MSG = "授权失败，请重新登录授权！";

    /**
     * 登录成功但是没有授权资源
     */
    public final static String ACCESS_DENY_MSG = "无权访问该资源！";

    public final static String NOT_SUPPORTED_GRANT_TYPE = "不支持的授权类型！";

    public final static String INVALID_MOBILE_CODE_MSG = "验证码错误！";

    public static final String BAD_CLIENT_CREDENTIALS = "客户端client凭证错误！";

    /**
     * 微信回调地址
     */
    public final static String WX_CALLBACK_URL = "/auth/wxLogin";

    /**
     * 设置浏览器缓存token cookie 的名称
     */
    public final static String TOKEN_COOKIE_NAME = "jcloud_access_token";

    /**
     * 验证码过期时间,单位分钟
     */
    public final static Integer CODE_TIME = 5;


}
