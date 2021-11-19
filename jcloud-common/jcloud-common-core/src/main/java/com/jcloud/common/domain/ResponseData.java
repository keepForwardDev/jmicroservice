package com.jcloud.common.domain;


import com.jcloud.common.consts.Const;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "公共响应", description = "博士云所有响应数据结构，code 为 1 是 响应成功 0 是未登录 -1 是失败")
public class ResponseData<T> implements Serializable {

    @ApiModelProperty(value = "响应状态码 1 是成功 其余都是未成功")
    private int code; //返回标识

    @ApiModelProperty(value = "状态码的描述")
    private String msg = ""; //返回提示信息

    @ApiModelProperty(value = "异常信息")
    private String exception = ""; //异常信息

    @ApiModelProperty(value = "返回的数据")
    private T data; // 返回的数据

    @ApiModelProperty(value = "返回的其他信息")
    private Object reserveData;

    public ResponseData() {
        this.code = Const.CODE_ERROR;
        this.msg = Const.CODE_ERROR_STR;
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(int code, String msg, T data, Object reserveData) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.reserveData = reserveData;
    }


    public ResponseData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getReserveData() {
        return reserveData;
    }

    public void setReserveData(Object reserveData) {
        this.reserveData = reserveData;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public static ResponseData getSuccessInstance() {
        return new ResponseData(Const.CODE_SUCCESS, Const.CODE_SUCCESS_STR);
    }

    public static ResponseData getSuccessInstance(Object object) {
        return new ResponseData(Const.CODE_SUCCESS, Const.CODE_SUCCESS_STR, object);
    }
}
