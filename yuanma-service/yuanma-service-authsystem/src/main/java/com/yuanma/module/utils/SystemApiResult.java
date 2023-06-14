package com.yuanma.module.utils;

import java.io.Serializable;
import java.util.HashMap;


public class SystemApiResult implements Serializable {

    private static final long serialVersionUID = -8158734906933781946L;

    private String errcode = "0";

    private String errmsg;

    private Object data = "";

    public SystemApiResult() {}

    public SystemApiResult(String errorCode, String errorMsg) {
        this.errcode = errorCode;
        this.errmsg = errorMsg;
    }
    public SystemApiResult(String errorCode) {
        this.errcode = errorCode;
    }
    public SystemApiResult(Object data) {
        setData(data);
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 校验错误
    public boolean hasError() {
        return null != this.errcode && !this.errcode.equals("0");
    }

    // 添加错误，用于alertError
    public SystemApiResult addError(String message) {
        this.errmsg = message;
        this.errcode = "500";
        return this;
    }

    @Override
    public String toString() {
        return "ApiResult [errcode=" + errcode + ", errmsg=" + errmsg + ", data=" + data + "]";
    }

}
