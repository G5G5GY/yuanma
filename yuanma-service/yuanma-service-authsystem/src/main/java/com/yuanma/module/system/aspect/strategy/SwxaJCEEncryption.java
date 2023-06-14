package com.yuanma.module.system.aspect.strategy;

import com.yuanma.module.system.encrption.IEncrption;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

@Slf4j
public class SwxaJCEEncryption  implements IEncrption {

    private final String alg = "SM4";

    @Override
    public String encode(String str) {
        String transformation = getTransform("SM4", "CBC", "PKCS5PADDING");
        int keynum = 1;
        try {
            KeyGenerator kg = KeyGenerator.getInstance(alg, "SwxaJCE");
            kg.init(keynum << 16);
            Key key1 = kg.generateKey();
            byte[] et1 =  encrypt(key1, transformation, str.getBytes());
            return ByteUtils.byte2hex(et1);
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public String decode(String str) {
        String transformation = getTransform("SM4", "CBC", "PKCS5PADDING");
        int keynum = 1;
        try {
            KeyGenerator kg = KeyGenerator.getInstance(alg, "SwxaJCE");
            kg.init(keynum << 16);
            Key key1 = kg.generateKey();
            return decrypt(key1, transformation, ByteUtils.hex2byte(str));
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;

    }

    @Override
    public String strategy() {
        return "SwxaJCE";
    }

    private  String getTransform(String alg, String mode, String padding) {
        return String.valueOf(alg) + "/" + mode + "/" + padding;
    }

    public  byte[] encrypt(Key key, String transformation, byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance(transformation, "SwxaJCE");
            cipher.init(1, key);
            return cipher.doFinal(plain);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String decrypt(Key key, String transformation, byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(transformation, "SwxaJCE");
            cipher.init(2, key);
            byte[] origin = cipher.doFinal(encrypted);
            return new String(origin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
