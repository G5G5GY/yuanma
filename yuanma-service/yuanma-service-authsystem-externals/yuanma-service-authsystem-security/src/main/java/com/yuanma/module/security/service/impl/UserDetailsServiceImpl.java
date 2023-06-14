package com.yuanma.module.security.service.impl;

import com.yuanma.auth.bean.UserDataScope;
import com.yuanma.auth.utils.UserDataScopeUtils;
import com.yuanma.exception.BadRequestException;
import com.yuanma.exception.EntityNotFoundException;
import com.yuanma.module.security.config.bean.LoginProperties;
import com.yuanma.module.security.mode.dto.JwtUserDto;
import com.yuanma.module.security.service.LoginLogService;
import com.yuanma.module.system.model.dto.MenuDto;
import com.yuanma.module.system.model.dto.RoleDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.service.*;
import com.yuanma.webmvc.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired(required = false)
    private  UserCommonService userService;
    @Autowired(required = false)
    private  RoleCommonService roleService;
    @Autowired(required = false)
    private  DataCommonService dataService;
    @Autowired(required = false)
    private  LoginProperties loginProperties;
    @Autowired(required = false)
    private  MenuCommonService menuService;
    @Autowired(required = false)
    private LoginLogService loginLogService;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * 用户信息缓存
     */
    static Map<String, JwtUserDto> userDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        log.info("--------------enter loadUserByUsername method-----------------");
        User.UserBuilder userBuilder;
        boolean searchDb = true;
        JwtUserDto jwtUserDto = null;
        if (loginProperties.isCacheEnable() && userDtoCache.containsKey(username)) {
            jwtUserDto = userDtoCache.get(username);
            searchDb = false;
        }
        if (searchDb) {
            UserDto user;
            try {
                user = userService.findByName(username);
                //    user.setPassword(new BCryptPasswordEncoder().encode());
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("user not find!");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活");
                }
                UserDataScope userDataScope = UserDataScopeUtils.acquireUserDataScope(user.getId());
                if (null != userDataScope) {
                    userDataScope.setUserId(String.valueOf(user.getId()));
                    userDataScope.setUserName(user.getUsername());
                    userDataScope.setUserNickName(user.getNickName());
                    userDataScope.setUserFlag(user.getIsAdmin() ? "1" : "0");
                    if(null != userDataScope.getData()){
                        userDataScope.setDataFlag(true);
                    }
                }
                if(loginProperties.isLockEnable()) {
                    // 判断用户是不是已经锁定
                    if (null != user.getUnlocked() && !user.getUnlocked() && null != user.getLockDate()) {
                        Date clearLockDate = DateUtils.addMinutes(user.getLockDate(), loginProperties.getAutoUnLockMinute());
                        if (new Date().after(clearLockDate)) {
                            loginLogService.updateStatusEqualOneByUserName(user.getUsername());
                            userService.unlock(user.getUsername());
                            user.setUnlocked(true);
                            user.setLockDate(null);
                        }
                    }
                } else {
                    user.setUnlocked(true);
                }
                // 验证签名
                userService.validateSign(user.getId());
                jwtUserDto = new JwtUserDto(
                        user,
                        userDataScope,
                        dataService.getDeptIds(user),
                        mapToGrantedAuthorities(user)
                );
                userDtoCache.put(username, jwtUserDto);
            }
        }
        log.info("--------------exit loadUserByUsername method-----------------");
        return jwtUserDto;
    }

    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        List<RoleSmallDto> roles = roleService.findByUsersId(user.getId());
        Set<RoleDto> roleDtos = roles.stream().map(c -> {
            RoleDto dto = new RoleDto();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setDataScope(c.getDataScope());
            dto.setMenus(menuService.findByRoleId(c.getId()));
            return dto;
        }).collect(Collectors.toSet());

        permissions = roleDtos.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                .map(MenuDto::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
