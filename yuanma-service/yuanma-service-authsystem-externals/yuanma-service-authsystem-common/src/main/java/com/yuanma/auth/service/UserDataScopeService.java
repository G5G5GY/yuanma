package com.yuanma.auth.service;

import com.yuanma.auth.bean.UserDataScope;

/**
 * 获取用户数据权限
 */
public interface UserDataScopeService {

    UserDataScope obtainUserDataScope(Object userId);

}
