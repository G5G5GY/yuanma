package com.yuanma.module.system.aspect.strategy;

import com.yuanma.module.system.encrption.IEncrption;

public class AESEncryption implements IEncrption {

    private String key = "ww.yuanma.com";

    @Override
    public String encode(String content) {
        try {
            return AESUtil.encrypt(content, key);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public String decode(String str) {
        try {
            return AESUtil.decrypt(str, key);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public String strategy() {
        return "AES";
    }

    public static void main(String[] args) {
        System.out.println(new AESEncryption().encode("I3CR8fibgwvYEJDZ9A3zE+jMNX4="));
    }
}
