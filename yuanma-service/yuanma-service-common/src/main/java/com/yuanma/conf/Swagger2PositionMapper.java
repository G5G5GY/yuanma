package com.yuanma.conf;

import io.swagger.models.parameters.Parameter;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Swagger2PositionMapper extends ServiceModelToSwagger2MapperImpl {

    @Override
    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {
        return super.parameterListToParameterList(list.stream().sorted(Comparator.comparingInt(springfox.documentation.service.Parameter::getOrder)).collect(Collectors.toList()));
    }
}
