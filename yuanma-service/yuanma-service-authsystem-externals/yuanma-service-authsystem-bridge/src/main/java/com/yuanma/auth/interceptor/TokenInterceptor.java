package com.yuanma.auth.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.yuanma.auth.bean.UserDataScope;
import com.yuanma.auth.exception.TokenAuthException;
import com.yuanma.auth.utils.UserAuthDataScopeUtils;
import com.yuanma.webmvc.util.RedisUtils;
import com.yuanma.webmvc.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class TokenInterceptor implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    private Class clz;
    public TokenInterceptor(){

    }
    public TokenInterceptor(Class clz){
        this.clz = clz;
    }

    //@Value("${login.header:Authorization}")
    private String header = "Authorization";

    //@Value("${login.tokenStartWith:Bearer}")
    private String tokenStartWith = "Bearer";

    //@Value("${login.onlineKey:online-token-}")
    private String onlineKey="online-token-";

    //@Autowired
   //private RedisUtils redisUtils;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //redisUtils.toString();

        log.info("++++++++enter TokenInterceptor  ++++++++=");

        String method = request.getMethod();
        //处理预请求
        if (HttpMethod.OPTIONS.toString().equals(method)) {
            return true;
        }
        String token = resolveToken(request);
        if(null != token){
            UserDataScope userDataScope = null;
            Object dataScope =  SpringContextHolder.getBean(RedisUtils.class).get(onlineKey+token+"_dataScope");
            System.out.println(dataScope.toString());

            log.info("++++++++dataScope++++++++="+dataScope+",token="+onlineKey+token+"_dataScope");
            if(null != dataScope) {
                if (null != clz) {
                    userDataScope = UserAuthDataScopeUtils.convert(dataScope.toString(), clz);
                } else {
                    userDataScope = UserAuthDataScopeUtils.convert(dataScope.toString());
                }
                log.info("++++++++userDataScope++++++++="+ JSONObject.toJSONString(userDataScope));
            }

            request.setAttribute("userDataScope",userDataScope);
        }
        return true;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(header);
        if(null != bearerToken) {
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(getTokenStartWith())) {
                // 去掉令牌前缀
                return bearerToken.replace(getTokenStartWith(), "");
            } else {
                log.warn("非法Token" , new TokenAuthException(bearerToken));
            }
        }
        return null;
    }
    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}