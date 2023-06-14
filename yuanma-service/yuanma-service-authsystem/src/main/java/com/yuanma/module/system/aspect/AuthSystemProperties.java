package com.yuanma.module.system.aspect;

import lombok.Data;

@Data
public class AuthSystemProperties {

    // 启动三权功能
    private boolean powerEnable = false;

    // 开启密码功能
    private boolean encryptionEnable = false;

    // 开启签名功能
    private boolean signEnable = false;

    // 相关加密算法
    private String strategy = "AES";

}
