package com.yuanma.module.security.service;

import com.yuanma.module.security.mode.dto.AuthUserDto;

public interface AuthorizationService {

    String tryAccquireToken(AuthUserDto dto);

}
