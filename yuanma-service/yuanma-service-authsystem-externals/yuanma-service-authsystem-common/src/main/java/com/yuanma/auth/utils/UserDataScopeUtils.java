package com.yuanma.auth.utils;

import com.yuanma.auth.bean.UserDataScope;
import com.yuanma.auth.service.UserDataScopeService;

import java.util.ServiceLoader;


public class UserDataScopeUtils {

    private static ServiceLoader<UserDataScopeService> services = ServiceLoader.load(UserDataScopeService.class);
    public static UserDataScope acquireUserDataScope(Object userId){
        for(UserDataScopeService service:services){
            return service.obtainUserDataScope(userId);
        }
        return null;
    }
}
