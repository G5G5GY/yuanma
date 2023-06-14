package com.yuanma.exception;

import java.text.MessageFormat;

public enum ExceptionMessage {

    ERR_E90000("E90000","应用ID密码不正确"),
    ERR_E90001("E90001","用户密码不正确,输错{0}次后，帐号将被锁定"),
    ERR_E90002("E90002","TOKEN不合理或已经过期"),
    ERR_E90003("E90003","TOKEN已经过期"),
    ERR_E90004("E90004","无效的TOKEN"),
    ERR_E90005("E90005","登录密码连续{0}次输入错误，账户被锁定,{1}分钟之后将会自动解锁"),//，之后将会自动解锁
    ERR_E90006("E90006","再输入错误{0}次后，帐号将被锁定"),
    ERR_E90007("E90007","请求超时，请重新请求"),
    ERR_E90008("E90008","签名错误"),
    ERR_E90009("E90009","{0}不能为空,请在header头传输"),;

    private String msg;
    private String code;
    ExceptionMessage(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public String getMsg(Object... args){
        return MessageFormat.format(msg,args);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
