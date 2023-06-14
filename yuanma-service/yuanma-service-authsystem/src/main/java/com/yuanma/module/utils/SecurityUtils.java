package com.yuanma.module.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    public static boolean matches(String password1,String password2){
        return StringUtils.equals(password1,password2);
    }
    /**
     * 生成密钥
     * @param password
     * @param passwordSalt
     * @param passwordSalt
     * @return
     */
    public static String encryptToHashString(String password, String passwordSalt){
        try {
            byte[] bt1 = DatatypeConverter.parseBase64Binary(passwordSalt);
            byte[] bt2 = password.getBytes("UTF-16LE");
            byte[] bt3 = new byte[bt1.length + bt2.length];
            System.arraycopy(bt1, 0, bt3, 0, bt1.length);
            System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
            MessageDigest md = MessageDigest.getInstance("sha1");
            byte[] digest = md.digest(bt3);
            return Base64.encodeBase64String(digest);
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public enum HashName{
        MD5("0","MD5"),
        SHA1("1","SHA1"),
        SHA256("2","SHA256"),
        SHA384("3","SHA384"),
        SHA512("4","SHA512");
        private String code;
        private String name;
        HashName(String code,String name){
            this.code = code;
            this.name = name;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public static String getNameByCode(String code){
            for(HashName hashName:HashName.values()){
                if(hashName.getCode().equals(code)){
                    return hashName.getName();
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(HashName.getNameByCode("1"));
    }





}
