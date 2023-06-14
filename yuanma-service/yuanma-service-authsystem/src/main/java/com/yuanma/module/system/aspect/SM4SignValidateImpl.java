package com.yuanma.module.system.aspect;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SM4SignValidateImpl extends SignValidateImpl{

    @Autowired
    private EncryptionTemplate encryptionTemplate;

    public String sign(String data){
        return encryptionTemplate.encode(data);
    }

    public void validate(String data,String sign){
        String signData = encryptionTemplate.decode(sign);
        if(!StringUtils.equals(data,signData)){
            throw new RuntimeException("签名数据可能被篡改");
        }
    }

}
