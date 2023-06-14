package com.yuanma.conf;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ParameterConf implements InitializingBean {


    @Value("${token.secret:'abcdefghijklmnopqrstuvwxyz'}")
    private String tokenSecret;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("tokenSecret",tokenSecret);
    }
}
