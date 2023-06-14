package com.yuanma;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

public class PasswordTest {

    @Test
    public void test(){
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        stringEncryptor.setPassword("YuanmaInf123.com");
        stringEncryptor.setIvGenerator(new org.jasypt.iv.NoIvGenerator());
        System.out.println(stringEncryptor.encrypt("Yuanma123!"));

    }
}
