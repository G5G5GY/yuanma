package com.yuanma.conf;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.yuanma.conf.properties.GlobalConfigSwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig extends Swagger2Config{

    @Autowired
    private GlobalConfigSwaggerProperties globalConfigSwaggerProperties;

    @Bean
    public Docket createRestApi() {

        List<Parameter> pars = new ArrayList<Parameter>();
        //pars.add(  new ParameterBuilder().name("appId").description("应用ID").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
        //pars.add(  new ParameterBuilder().name("appkey").description("应用秘钥").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
        //pars.add(  new ParameterBuilder().name("timestamp").description("时间截").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
        //pars.add(  new ParameterBuilder().name("sign").description("签名").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
        pars.add(  new ParameterBuilder().name("Authorization").description("应用ID").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
        return new Docket(DocumentationType.SWAGGER_2).enable(true)
                .apiInfo(apiInfo()).select()
                // 为当前包路径 //RequestHandlerSelectors.basePackage(包名)
                .apis(basePackage(getScanner()))
                .paths(PathSelectors.any()).build()
                	.globalOperationParameters(pars)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title(globalConfigSwaggerProperties.getTitle())
                // 创建人
                // 版本号
                .version(globalConfigSwaggerProperties.getVersion())
                // 描述
                .description(globalConfigSwaggerProperties.getDescription()).build();
    }
}
