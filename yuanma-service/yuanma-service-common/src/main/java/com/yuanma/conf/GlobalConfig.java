package com.yuanma.conf;

import com.yuanma.conf.properties.GlobalConfigSwaggerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class GlobalConfig {

	@Bean
	@ConfigurationProperties(prefix = "com.winner.global.swagger", ignoreUnknownFields = true)
	public static GlobalConfigSwaggerProperties swagger() {
		return new GlobalConfigSwaggerProperties();
	}

	
}
