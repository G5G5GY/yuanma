package com.yuanma;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTest {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void getPass() {



        String password = encryptor.encrypt("Yuanma123!");
        System.out.println("password: " + password);
    }
}
