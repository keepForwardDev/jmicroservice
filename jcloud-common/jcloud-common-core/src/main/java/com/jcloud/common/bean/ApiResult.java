package com.jcloud.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiaxm
 * @date 2021/1/5
 */
public class ApiResult {

    /**
     * 查询成功
     */
    public static final Integer API_RESULT_SUCCESS = 200;

    /**
     * 无数据
     */
    public static final Integer API_NO_RESULT = 201;

    /**
     * 未开通权限
     */
    public static final Integer API_NO_PERMISSION = 202;

    /**
     * 参数错误
     */
    public static final Integer API_PARAMS_NOT_CORRECT = 204;

    /**
     * api调用超限
     */
    public static final Integer API_OUT_OF_BOUNDS = 203;

    /**
     * api内部错误
     */
    public static final Integer API_ERROR = 199;

    /**
     * api 调用限流
     */
    public static final Integer API_BLOCK = 198;


    public static Map<Integer, String> codeMap = new HashMap<Integer, String>() {
        {
            put(200, "查询成功");
            put(201, "查询无结果");
            put(202, "未开通接口权限");
            put(203, "接口调用次数超出限制");
            put(204, "接口参数传递错误");
            put(104, "接口正在维护中");
            put(105, "接口已下线停用");
            put(199, "接口调用错误");
            put(198, "调用过于频繁，请稍后再试");
        }
    };

    public ApiResult() {
        this.code = API_NO_PERMISSION;
        this.msg = codeMap.get(API_NO_PERMISSION);
    }

    public ApiResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    /**
     * 提示
     */
    private String msg;

    /**
     * 数据
     */
    private Object data;

    /**
     * 额外的数据
     */
    private Object reserveData;

    /**
     * 订单号
     */
    private String orderNumber;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Object getReserveData() {
        return reserveData;
    }

    public void setReserveData(Object reserveData) {
        this.reserveData = reserveData;
    }
}
