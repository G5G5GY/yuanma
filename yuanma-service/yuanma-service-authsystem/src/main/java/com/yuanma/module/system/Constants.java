package com.yuanma.module.system;

public class Constants {

    public final static String DEFAULT_PASSWORD = "A123.com";

    /**
     - rest_j表示接口符合Jersey规范
     - rest_s表示接口符合springMVC Rest规范
     - v1为服务的版本号，**版本号会随着服务的版本进行升级**
     - {applicationName}为微服务名
     */
    public static final String API = "/api/rest_s/v1/system/";
}
