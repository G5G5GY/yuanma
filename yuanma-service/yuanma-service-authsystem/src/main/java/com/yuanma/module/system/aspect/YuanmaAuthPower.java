package com.yuanma.module.system.aspect;

import com.yuanma.module.log.annotation.type.LogActionType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YuanmaAuthPower {

    String value() default "";//标题

    @AliasFor("value")
    String title() default ""; //标题

    String module() default "";

    AuthPowerActionType type() default AuthPowerActionType.ADD;

    Class<?>[] paramGenerics () default {};

}
