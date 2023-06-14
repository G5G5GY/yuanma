package com.yuanma.conf;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.yuanma.conf.properties.GlobalConfigSwaggerProperties;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
public class Swagger2Config {

	@Autowired
	private GlobalConfigSwaggerProperties globalConfigSwaggerProperties;

	@Bean
	@ConditionalOnMissingBean
	public Docket createRestApi() {

		List<Parameter> pars = new ArrayList<Parameter>();
		//pars.add(  new ParameterBuilder().name("appId").description("应用ID").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		//pars.add(  new ParameterBuilder().name("appkey").description("应用秘钥").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		//pars.add(  new ParameterBuilder().name("timestamp").description("时间截").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		//pars.add(  new ParameterBuilder().name("sign").description("签名").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		pars.add(  new ParameterBuilder().name("Authorization").description("应用ID").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		return new Docket(DocumentationType.SWAGGER_2).enable(globalConfigSwaggerProperties.isEnable())
				.apiInfo(apiInfo()).select()
				// 为当前包路径 //RequestHandlerSelectors.basePackage(包名)
				.apis(basePackage(getScanner()))
				.paths(PathSelectors.any()).build()
			//	.globalOperationParameters(pars)
		;
	}

	// 访问地址http://127.0.0.1:9879/doc.html
	// 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
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

	public static Predicate<RequestHandler> basePackage(final String basePackage) {
		return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
	}

	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
		return input -> {
			// 循环判断匹配
			for (String strPackage : basePackage.split(splitor)) {
				boolean isMatch = input.getPackage().getName().startsWith(strPackage);
				if (isMatch) {
					return true;
				}
			}
			return false;
		};
	}

	@SuppressWarnings("deprecation")
	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
		return Optional.ofNullable(input.declaringClass());
	}

	// 定义分隔符
	private static final String splitor = ";";



	public  String getScanner() {
		return StringUtil.join(";", globalConfigSwaggerProperties.getScanner());
	}

}
