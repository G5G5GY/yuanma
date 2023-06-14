package com.yuanma.module.security.handler;

import com.alibaba.fastjson.JSON;
import com.yuanma.webmvc.vo.ApiResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403 Forbidden响应
      //response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
      returnFailure(response);
   }

   public void returnFailure(HttpServletResponse response) throws IOException{
      response.setCharacterEncoding("utf-8");
      response.setContentType("application/json;charset=utf-8");
      PrintWriter writer = response.getWriter();
      ApiResult commonResult = new ApiResult().addError("没有权限");
      commonResult.setCode("-1");
      writer.write(JSON.toJSONString(commonResult));
      writer.flush();
   }
}
