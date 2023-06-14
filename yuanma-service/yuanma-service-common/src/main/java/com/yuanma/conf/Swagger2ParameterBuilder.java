package com.yuanma.conf;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.parameter.Examples;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

@Primary
@Component
public class Swagger2ParameterBuilder implements ExpandedParameterBuilderPlugin {

    private final DescriptionResolver descriptions;
    private final EnumTypeDeterminer enumTypeDeterminer;

    @Autowired
    public Swagger2ParameterBuilder(DescriptionResolver descriptions, EnumTypeDeterminer enumTypeDeterminer) {
        this.descriptions = descriptions;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }

    @Override
    public void apply(ParameterExpansionContext context) {
        context.findAnnotation(ApiModelProperty.class).ifPresent(property -> fromApiModelProperty(context, property));
        context.findAnnotation(ApiParam.class).ifPresent(param -> fromApiParam(context, param));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
        String allowableProperty = StrUtil.emptyToNull(apiParam.allowableValues());
        AllowableValues allowable = allowableValues(Optional.ofNullable(allowableProperty), context.getFieldType().getErasedType());

        maybeSetParameterName(context, apiParam.name()).order(SWAGGER_PLUGIN_ORDER)
                                                       .description(descriptions.resolve(apiParam.value()))
                                                       .hidden(apiParam.hidden())
                                                       .allowableValues(allowable)
                                                       .required(apiParam.required())
                                                       .scalarExample(apiParam.example())
                                                       .parameterAccess(apiParam.access())
                                                       .defaultValue(apiParam.defaultValue())
                                                       .allowMultiple(apiParam.allowMultiple())
                                                       .complexExamples(Examples.examples(apiParam.examples()))
                                                       .build();
    }

    private void fromApiModelProperty(ParameterExpansionContext context, ApiModelProperty apiModelProperty) {
        String allowableProperty = StrUtil.emptyToNull(apiModelProperty.allowableValues());
        AllowableValues allowable = allowableValues(Optional.ofNullable(allowableProperty), context.getFieldType().getErasedType());

        maybeSetParameterName(context, apiModelProperty.name()).order(apiModelProperty.position())
                                                               .description(descriptions.resolve(apiModelProperty.value()))
                                                               .allowableValues(allowable)
                                                               .hidden(apiModelProperty.hidden())
                                                               .required(apiModelProperty.required())
                                                               .scalarExample(apiModelProperty.example())
                                                               .parameterAccess(apiModelProperty.access())
                                                               .build();
    }

    private ParameterBuilder maybeSetParameterName(ParameterExpansionContext context, String parameterName) {
        if (Strings.isNotEmpty(parameterName)) {
            context.getParameterBuilder().name(parameterName);
        }
        return context.getParameterBuilder();
    }

    private AllowableValues allowableValues(final Optional<String> optionalAllowable, Class<?> fieldType) {
        if (enumTypeDeterminer.isEnum(fieldType)) {
            return new AllowableListValues(getEnumValues(fieldType), "LIST");
        } else if (optionalAllowable.isPresent()) {
            return ApiModelProperties.allowableValueFromString(optionalAllowable.get());
        }
        return null;
    }

    private List<String> getEnumValues(final Class<?> subject) {
        return Arrays.stream(subject.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
    }
}
