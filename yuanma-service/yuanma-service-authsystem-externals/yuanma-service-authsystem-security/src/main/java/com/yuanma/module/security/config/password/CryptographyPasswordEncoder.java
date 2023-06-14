package com.yuanma.module.security.config.password;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.SecureRandom;

@Slf4j
public class CryptographyPasswordEncoder implements SaltPasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public String encode(CharSequence rawPassword, String salt) {
        try {
            byte[] bt1 = DatatypeConverter.parseBase64Binary(salt.toString());
            byte[] bt2 = rawPassword.toString().getBytes("UTF-16LE");
            byte[] bt3 = new byte[bt1.length + bt2.length];
            System.arraycopy(bt1, 0, bt3, 0, bt1.length);
            System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
            MessageDigest md = MessageDigest.getInstance("sha1");
            byte[] digest = md.digest(bt3);
            return Base64.encodeBase64String(digest);
        } catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String salt, String encodedPassword) {
        return matches(encode(rawPassword,salt),encodedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
       // log.info("CryptographyPasswordEncoder");
        //log.info("CryptographyPasswordEncoder->rawPassword=>{},encodedPassword=>{}",rawPassword,encodedPassword);
        return StringUtils.equals(rawPassword.toString(), encodedPassword);
    }

    public String getRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return new String(Base64.encodeBase64(salt));
    }

    public static void main(String[] args) {
        CryptographyPasswordEncoder encoder = new CryptographyPasswordEncoder();
        String pwd = encoder.encode("Yuanma123!","TAF1xAn748BXal9vAF9wosMg5ygb79cEhgfO4/Er6E8=");
        System.out.println(pwd);
    }
}
