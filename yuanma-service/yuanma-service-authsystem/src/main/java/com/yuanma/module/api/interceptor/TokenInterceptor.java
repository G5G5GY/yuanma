package com.yuanma.module.api.interceptor;

import com.yuanma.exception.ExceptionMessage;
import com.yuanma.exception.LoginException;
import com.yuanma.webmvc.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    //private UserService userService;
    private Long expireTime;



    /**
     *
     * @param request
     * @param response
     * @param handler 访问的目标方法
     * @return
     * @throws Exception
     */
    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String method = request.getMethod();
        //处理预请求
        if (HttpMethod.OPTIONS.toString().equals(method)) {
            return true;
        }
        // 应用ID
        String appId = request.getHeader("appId");
        // 应用秘钥
        String appkey = request.getHeader("appkey");
        // 时间截
        String timestamp = request.getHeader("timestamp");
        // 随机字符串
        //String nonce = request.getHeader("nonce");
        // 签名
        String sign = request.getHeader("sign");
        if(StringUtils.isEmpty(appId)){
            throw new LoginException(ExceptionMessage.ERR_E90009,"应用ID");
        }
        if(StringUtils.isEmpty(appkey)){
            throw new LoginException(ExceptionMessage.ERR_E90009,"应用秘钥");
        }
        if(StringUtils.isEmpty(timestamp)){
            throw new LoginException(ExceptionMessage.ERR_E90009,"时间截");
        }
        if(StringUtils.isEmpty(sign)){
            throw new LoginException(ExceptionMessage.ERR_E90009,"签名");
        }
        // 查询相应的用户Id
       // String userId = userService.findUser(appId, appkey);
        if(StringUtils.isEmpty(sign)){
            throw new LoginException(ExceptionMessage.ERR_E90000);
        }
        // 2. 请求时间间隔
        long reqeustInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
        if(reqeustInterval > expireTime){
            throw new LoginException(ExceptionMessage.ERR_E90007);
        }

       // HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);

        // 4. 校验签名(将所有的参数加进来，防止别人篡改参数) 所有参数看参数名升续排序拼接成url
        // 请求参数 + token + timestamp + nonce
        /*ApiUtil.concatSignString(request) + */
        //SortedMap<String, String> bodyParams = HttpUtil.getBodyParams(request);
        //String params = ApiUtil.concatSignString(bodyParams);
        //System.out.println(params);

        String signString =  appId + timestamp + appkey;
        String signature = MD5Util.encode(signString).toUpperCase();
        boolean flag = signature.equals(sign);
        if(!flag){
            throw new LoginException(ExceptionMessage.ERR_E90008);
        }
        return true;
    }
}
