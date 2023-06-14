package com.yuanma.conf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {



    @Value("${token.request.expire:10000}")
    private  Long expireTime = 1000L;

    @Autowired
    private Environment env;


    public void addInterceptors(InterceptorRegistry registry) {

        /*registry.addInterceptor(new LoginInterceptor())//添加拦截器
                .excludePathPatterns("/doc.html")
                .addPathPatterns("/api/**"); //拦截所有请求
       */

    }

    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);

    }*/








}
