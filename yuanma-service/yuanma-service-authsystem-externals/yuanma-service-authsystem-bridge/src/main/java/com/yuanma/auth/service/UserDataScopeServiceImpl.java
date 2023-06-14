package com.yuanma.auth.service;

import com.yuanma.auth.bean.UserDataScope;
import com.yuanma.auth.interceptor.TokenInterceptor;
import com.yuanma.webmvc.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取用户数据权限
 */
public class UserDataScopeServiceImpl implements UserDataScopeService{

    private Logger log = LoggerFactory.getLogger(UserDataScopeServiceImpl.class);

    @Override
    public UserDataScope obtainUserDataScope(Object userId) {
        UserDataScope userDataScope = new UserDataScope();
        userDataScope.setUserId(userId.toString());
        if(SpringContextHolder.isExist(UserAuthorityService.class)) {
            UserAuthorityService userAuthorityService = SpringContextHolder.getBean(UserAuthorityService.class);
            userDataScope.setData(userAuthorityService.load(userId));
        } else {
            log.warn("如果要扩展用户权限信息，请继承com.yuanma.auth.service.UserAuthorityService的类实现load方法，\n" +
                    "配置如下信息，并在controller中通过@RequestAttribute(\"userDataScope\") UserDataScope userDataScope获取信息\n"+
                    "@Configuration\n" +
                    "public class WebConfig implements WebMvcConfigurer {\n" +
                    "\n" +
                    "    public void addInterceptors(InterceptorRegistry registry) {\n" +
                    "        registry.addInterceptor(new TokenInterceptor()).addPathPatterns(\"/api/**\");\n" +
                    "    }\n" +
                    "\n" +
                    "}");
        }
        return userDataScope;
    }
}
