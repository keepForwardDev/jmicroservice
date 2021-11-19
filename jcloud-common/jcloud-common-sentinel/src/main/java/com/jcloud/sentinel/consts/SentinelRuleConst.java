package com.jcloud.sentinel.consts;

/**
 * @author jiaxm
 * @date 2021/4/19
 */
public class SentinelRuleConst {

    /**
     * sentinel nacos group
     */
    public static final String GROUP_ID = "SENTINEL_GROUP";

    /**
     * 限流后缀
     */
    public static final String FLOW_DATA_ID_POSTFIX = "-flow-rules";

    /**
     * 参数限流后缀
     */
    public static final String PARAM_FLOW_DATA_ID_POSTFIX = "-param-rules";

    /**
     * 降级后缀
     */
    public static final String DEGRADE_DATA_ID_POSTFIX = "-degrade-rules";


    /**
     * 系统保护后缀
     */
    public static final String SYSTEM_DATA_ID_POSTFIX = "-system-rules";


    /**
     * 网关限流后缀
     */
    public static final String GATEWAY_DATA_ID_POSTFIX = "-gateway-flow-rules";


    /**
     * 网关api 分组
     */
    public static final String GATEWAY_API_DATA_ID_POSTFIX = "-gateway-api-definition";
}
