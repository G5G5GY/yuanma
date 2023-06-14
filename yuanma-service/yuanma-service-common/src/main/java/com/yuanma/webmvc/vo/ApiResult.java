package com.yuanma.webmvc.vo;


import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -8158734906933781946L;

    private String code = "200";

    private T msg;

    public ApiResult() {}

    public ApiResult(String errorCode) {
        this.code = StringUtils.isEmpty(errorCode)?"500":errorCode;
    }

    public ApiResult(T data) {
        setMsg(data);
    }

    public static <T> ApiResult createData(T data){
        return new ApiResult(data);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String errcode) {
        this.code = errcode;
    }



    public T getMsg() {
        return msg;
    }

    public void setMsg(T data) {
        this.msg = data;
    }



    // 校验错误
    public boolean hasError() {
        return null != this.code && !this.code.equals("1");
    }

    // 添加错误，用于alertError
    public ApiResult addError(T msg) {
        setMsg(msg);
        this.code = "500";
        return this;
    }

    // 添加错误，用于alertError
    public ApiResult addError(String code,T msg) {
        setMsg(msg);
        this.code = null == code?"500":code;
        return this;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code='" + code + '\'' +
                ", msg=" + msg +
                '}';
    }
}
