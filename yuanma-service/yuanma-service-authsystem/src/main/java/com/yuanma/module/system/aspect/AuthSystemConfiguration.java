package com.yuanma.module.system.aspect;

import com.yuanma.module.security.config.bean.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthSystemConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "auth.system", ignoreUnknownFields = true)
    public AuthSystemProperties authSystemProperties(){
        return new AuthSystemProperties();
    }
}
