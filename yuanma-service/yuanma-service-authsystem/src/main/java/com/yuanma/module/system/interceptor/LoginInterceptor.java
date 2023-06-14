package com.yuanma.module.system.interceptor;

import com.yuanma.exception.ExceptionMessage;
import com.yuanma.exception.LoginException;
import com.yuanma.webmvc.util.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);


    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o) throws Exception {
        String token = httpServletRequest.getHeader("Authorization");
        log.info("---------------开始进入地址拦截器-------------------"+token);
        try {
            String method = httpServletRequest.getMethod();
            //处理预请求
            if (HttpMethod.OPTIONS.toString().equals(method)) {
                return true;
            } else {
                if (StringUtils.isNotBlank(token)) {
                    try {
                        if (TokenUtils.isTokenExpired(token)) {
                            throw new LoginException(ExceptionMessage.ERR_E90003);
                        }
                        String uid = TokenUtils.getUID(token);
                        if (StringUtils.isEmpty(uid)) {
                            throw new LoginException(ExceptionMessage.ERR_E90002);
                        }
                        httpServletRequest.setAttribute("uid", uid);
                    } catch (LoginException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new LoginException(ExceptionMessage.ERR_E90004);
                    }
                    return true;
                }
                httpServletRequest.setAttribute("uid", "1");
                //throw new LoginException(ExceptionMessage.ERR_E90003);
            }
        }finally {
            log.info("---------------完成地址拦截器处理-------------------"+token);
        }
        return true;
    }


}
