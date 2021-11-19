package com.jcloud.common.consts;


/**
 * 常量文件
 */
public class Const {

    public final static int CODE_NO_LOGIN = 0;
    public final static String CODE_NO_LOGIN_STR = "您未登录,请先登录！";
    public final static int CODE_SUCCESS = 1;

    public final static String CODE_SUCCESS_STR = "操作成功！";
    public final static int CODE_ERROR = -1;
    public final static String CODE_ERROR_STR = "操作失败！";

    public final static int CODE_ACCESS_RESTRICTED = -2;
    public final static String CODE_ACCESS_RESTRICTED_STR = "访问受限！";

    public final static String LOGOUT_SUCCESS_STR = "退出成功!";

    public final static String INVALID_TOKEN_MSG = "token非法";

    public final static String INTERNAL_ERROR = "服务器异常，请稍后在试";

    public final static String PARAMS_ERROR = "请传入参数！";

    /**
     * 验证码失败
     */
    public final static int CODE_ERROR_CODE = 4;

    public final static String CODE_ERROR_CODE_STR = "验证码错误，请重新输入！";

    public final static String LOGIN_SUCCESS_RETURN__URL = "fromUrl"; //登陆成功后跳转的url

    public final static int LOGIN_ACCOUNT_LOCKED_HOURS = 24;//账号登陆锁定时长,单位:小时
    public final static int LOGIN_MAX_NUM = 5;// 账号登陆连续输入密码最大次数
    public final static String ENCODING = "UTF-8";
    //cookie加密密钥
    public final static String SECRET_KEY = "j1QasAUE*W87%$#!xCZANPOAFhRfo2#209%&12*2WUO3aNBQ12p3olJD2@9zmj%&*(0923Tm07Xdw3joIKDFWA$@F";

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "descending";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "ascending";

    /**
     * 登录失败跳转地址
     */
    public final static String LOGIN_FAILURE_URL = "/loginFailure";

    /**
     * 开发环境
     */
    public final static String PROFILE_DEV = "dev";

    /**
     * 测试环境
     */
    public final static String PROFILE_TEST = "test";

    /**
     * 正式环境
     */
    public final static String PROFILE_PROD = "prod";

    /**
     * 导出文件地址
     */
    public final static String EXPORT_PATH = "/export";

    /**
     * 文件访问路径
     */
    public final static String FILE_VISIT_PREFIX = "/ext";

    /**
     * 文件上传通用文件夹
     */
    public final static String FILE_UPLOAD_PATH = "common";

    /**
     * open api header，传入这个代表使用open api 拦截
     */
    public final static String OPEN_API_HEADER = "openApi";


    /**
     * open api 执行完成
     * 放入request 中
     */
    public final static String OPEN_API_RESULT = "openApiResult";

    /**
     * 系统内部调用，加入该请求头 value为serviceId 将不会受到权限拦截器的拦截
     */
    public final static String API_INNER_CALL_HEADER = "apiInnerCall";
}
