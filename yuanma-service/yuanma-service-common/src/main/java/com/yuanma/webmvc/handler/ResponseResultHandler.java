package com.yuanma.webmvc.handler;

import com.alibaba.fastjson.JSON;
import com.yuanma.webmvc.annotations.ResponseResult;
import com.yuanma.webmvc.vo.ApiResult;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Set;


@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object>{

    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseResult.class) || returnType.hasMethodAnnotation(ResponseResult.class);

    }

    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        ResponseResult responseResult = returnType.getMethodAnnotation(ResponseResult.class);

        if(null == responseResult)	{
            responseResult = returnType.getContainingClass().getAnnotation(ResponseResult.class);
        }

        // 取得responseResult
        if(null == responseResult)	{
            Set<ResponseResult> sets = AnnotatedElementUtils.getAllMergedAnnotations(returnType.getContainingClass(), ResponseResult.class);
            if(null != sets && !sets.isEmpty()) {
                responseResult = sets.iterator().next();
            }
        }


        Class<? extends ApiResult> result = responseResult.value();

        if(result.isAssignableFrom(ApiResult.class)) {

            if(body  instanceof ApiResult) {
                return body;
            } else {
                if(body instanceof String) {
                    return JSON.toJSONString(new ApiResult(body));
                } else {
                    return new ApiResult(body);
                }
            }

        }

        return body;
    }

}

