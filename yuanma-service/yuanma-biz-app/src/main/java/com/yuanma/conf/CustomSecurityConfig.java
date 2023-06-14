package com.yuanma.conf;

import com.yuanma.module.security.config.password.CryptographyPasswordEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**/@Configuration
public class CustomSecurityConfig {

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        //return new BCryptPasswordEncoder();
        return new CryptographyPasswordEncoder();
    }/**/
}
