package com.yuanma.webmvc.annotations;

import com.yuanma.webmvc.vo.ApiResult;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseResult {

    Class<? extends ApiResult> value() default ApiResult.class;


}

