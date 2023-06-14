package com.yuanma.module.system.encrption;

public interface SignValidate {

    String sign(String data);

    void validate(String data,String sign);


}
