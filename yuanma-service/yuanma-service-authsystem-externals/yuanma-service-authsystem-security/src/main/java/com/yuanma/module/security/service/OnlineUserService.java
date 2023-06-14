package com.yuanma.module.security.service;

import com.yuanma.module.security.mode.dto.JwtUserDto;
import com.yuanma.module.security.mode.dto.OnlineUserDto;

import javax.servlet.http.HttpServletRequest;

public interface OnlineUserService {

    OnlineUserDto getOne(String key);

    void logout(String token);

    void checkLoginOnUser(String userName, String igoreToken);

    void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request);

}