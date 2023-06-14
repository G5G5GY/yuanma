package com.yuanma.module.system.aspect.strategy;
import com.yuanma.module.system.encrption.IEncrption;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;


public class BASE64Encryption implements IEncrption {
    @Override
    public String encode(String str) {
        if(StringUtils.isEmpty(str))
            return str;
        Base64 base64 = new Base64();
        String base64Sign = base64.encodeToString(str.getBytes());
        return base64Sign;
    }

    @Override
    public String decode(String str) {
        if(StringUtils.isEmpty(str))
            return str;
        return  new String(Base64.decodeBase64(str.getBytes()));
    }

    @Override
    public  String strategy() {
        return "BASE64";
    }
}
