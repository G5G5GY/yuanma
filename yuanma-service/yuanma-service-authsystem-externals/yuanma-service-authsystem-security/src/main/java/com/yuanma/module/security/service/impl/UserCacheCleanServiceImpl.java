package com.yuanma.module.security.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yuanma.module.security.service.UserCacheCleanService;
import com.yuanma.webmvc.util.StringUtils;
import org.springframework.stereotype.Service;

@Service
@DS("auth")
public class UserCacheCleanServiceImpl implements UserCacheCleanService {
    /**
     * 清理特定用户缓存信息<br>
     * 用户信息变更时
     *
     * @param userName /
     */
    public void cleanUserCache(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            UserDetailsServiceImpl.userDtoCache.remove(userName);
        }
    }

    /**
     * 清理所有用户的缓存信息<br>
     * ,如发生角色授权信息变化，可以简便的全部失效缓存
     */
    public void cleanAll() {
        UserDetailsServiceImpl.userDtoCache.clear();
    }
}
