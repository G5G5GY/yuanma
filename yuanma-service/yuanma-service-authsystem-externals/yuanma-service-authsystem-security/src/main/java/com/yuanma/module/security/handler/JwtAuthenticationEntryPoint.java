package com.yuanma.module.security.handler;

import com.alibaba.fastjson.JSON;
import com.yuanma.webmvc.vo.ApiResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
       // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException==null?"Unauthorized":authException.getMessage());
        returnFailure(response);
    }

    public void returnFailure(HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        ApiResult commonResult = new ApiResult().addError("没有认证");
        commonResult.setCode("-1");
        writer.write(JSON.toJSONString(commonResult));
        writer.flush();
    }
}
