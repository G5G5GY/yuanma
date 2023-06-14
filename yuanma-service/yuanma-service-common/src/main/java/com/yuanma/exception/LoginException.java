package com.yuanma.exception;

import lombok.Data;

@Data
public class LoginException  extends RuntimeException{
    private String message;
    private String code;

    public LoginException(String msg){
        super(msg);
        this.message = msg;
    }

    public LoginException(String code,String msg){
        super(msg);
        this.code = code;
        this.message = msg;
    }

    public LoginException(ExceptionMessage message){
        super(message.getMsg());
        this.code = message.getCode();
        this.message = message.getMsg();
    }

    public LoginException(ExceptionMessage message,Throwable t){
        super(message.getMsg(),t);
        this.code = message.getCode();
        this.message = message.getMsg();
    }

    public LoginException(ExceptionMessage message,String ...args){
        super(message.getMsg(args));
        this.code = message.getCode();
        this.message = message.getMsg(args);
    }
}
