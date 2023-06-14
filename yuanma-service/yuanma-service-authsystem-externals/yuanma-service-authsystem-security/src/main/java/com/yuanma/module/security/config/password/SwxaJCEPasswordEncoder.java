package com.yuanma.module.security.config.password;

import com.yuanma.module.system.encrption.IEncrption;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwxaJCEPasswordEncoder extends CryptographyPasswordEncoder {


    private IEncrption encrption;
    public SwxaJCEPasswordEncoder(IEncrption encrption){
        this.encrption = encrption;
    }

    @Override
    public String encode(CharSequence rawPassword, String salt) {

        String password1 = super.encode(rawPassword,salt);
        //log.info("rawPassword=>{},password=>{}",rawPassword,password1);
        String password = encrption.encode(password1);
        //log.info("encrption password1=>{},password2=>{}",password1,password);
        return password;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String salt, String encodedPassword) {
        //encodedPassword = encrption.decode(encodedPassword);
        //log.info("rawPassword=>{},encodedPassword=>{}",rawPassword,encodedPassword);
        return super.matches(rawPassword,salt,encodedPassword);
    }

}
