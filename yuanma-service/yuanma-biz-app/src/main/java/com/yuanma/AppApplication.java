package com.yuanma;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {

       Map properties = new HashMap<String,String>();
       properties.put("jasypt.encryptor.password","YuanmaInf123.com");
       properties.put("jasypt.encryptor.algorithm","PBEWithMD5AndDES");
       properties.put("jasypt.encryptor.iv-generator-classname","org.jasypt.iv.NoIvGenerator");

        new SpringApplicationBuilder()
                .properties(properties)
                .sources(AppApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
