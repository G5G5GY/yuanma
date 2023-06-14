package com.yuanma.module.system.aspect;

import com.yuanma.module.system.aspect.strategy.ByteUtils;
import com.yuanma.module.system.encrption.SignValidate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;


@Slf4j
public class SignValidateImpl implements SignValidate {

    public String sign(String data){
        try {
            KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
            Signature signature = Signature.getInstance("SM3WithSM2", "SwxaJCE");
            signature.initSign(keyPair.getPrivate());
            signature.update(data.getBytes());
            byte[] out = signature.sign();
            return ByteUtils.byte2hex(out);
        } catch(Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException("生成签名失败");
        }
    }

    public void validate(String data,String sign){
        if(StringUtils.isEmpty(sign)){
            throw new RuntimeException("签名不能为空");
        }
        try {
            KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
            Signature signatureVerify = Signature.getInstance("SM3WithSM2","SwxaJCE");
            signatureVerify.initVerify(keyPair.getPublic());
            signatureVerify.update(data.getBytes());
            boolean flag = signatureVerify.verify(ByteUtils.hex2byte(sign));
            if(!flag){
                throw new RuntimeException("签名数据可能被篡改");
            }
        } catch(Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException("签名验证失败");
        }
    }

    public static void main(String[] args) {
        System.out.println(ByteUtils.hex2byte("3045022100AC62B1172ACC8FDB62835118F8D6DA92BFD0E2EDCC5B7779E3F356EEFCB0A3920220451084800857C9E51649A5E33A9172F3E076D374EDDF3B4D424A107C425E8356"));
    }

}
