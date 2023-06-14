package com.yuanma.module.log.annotation;

import com.yuanma.module.log.annotation.type.LogActionType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YuanmaLog {

    String value() default "";//标题
    @AliasFor("value")
    String title() default ""; //标题
    String moudle() default "";//模块

    LogActionType type() default LogActionType.SELECT;
}
