package com.yuanma.exception;

public class BusinessException extends RuntimeException{

    private String message;
    private String code;

    public BusinessException(String msg){
        super(msg);
        this.message = msg;
    }

    public BusinessException(String code,String msg){
        super(msg);
        this.code = code;
        this.message = msg;
    }

    public BusinessException(ExceptionMessage message){
        super(message.getMsg());
        this.code = message.getCode();
        this.message = message.getMsg();
    }

    public BusinessException(ExceptionMessage message,Throwable t){
        super(message.getMsg(),t);
        this.code = message.getCode();
        this.message = message.getMsg();
    }

    public BusinessException(ExceptionMessage message,String ...args){
        super(message.getMsg(args));
        this.code = message.getCode();
        this.message = message.getMsg(args);
    }

}
