package com.yuanma.webmvc.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class WebMvcUtils {

    /**
     * 获取SpringMVC的请求信息
     *
     * @return ServletRequestAttributes
     */
    private static Optional<ServletRequestAttributes> getWebRequest() {
        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()));
    }

    /**
     * 从SpringMVC的获取ServletRequest请求
     *
     * @return HttpServletRequest的Optional对象
     */
    public static Optional<HttpServletRequest> getRequest() {
        return getWebRequest().map(ServletRequestAttributes::getRequest);
    }

    /**
     * 从SpringMVC的获取ServletResponse请求
     *
     * @return HttpServletResponse的Optional对象
     */
    public static Optional<HttpServletResponse> getResponse() {
        return getWebRequest().map(ServletRequestAttributes::getResponse);
    }

    /**
     * 从SpringMVC的获取HttpSession
     *
     * @return HttpSession的Optional对象
     */
    public static Optional<HttpSession> getSession() {
        return getRequest().map(HttpServletRequest::getSession);
    }

    /**
     * 从SpringMVC的获取Servlet上下文
     *
     * @return ServletContext的Optional对象
     */
    public static Optional<ServletContext> getServletContext() {
        return getSession().map(HttpSession::getServletContext);
    }

}
