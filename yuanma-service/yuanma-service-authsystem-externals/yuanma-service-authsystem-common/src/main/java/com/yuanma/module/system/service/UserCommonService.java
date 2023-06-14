package com.yuanma.module.system.service;

import com.yuanma.module.system.model.dto.UserDto;

import java.util.Date;

public interface UserCommonService {

    UserDto findByName(String userName);

    void unlock(String username);

    void lock(String username);

    Date getLockDateByUserName(String username);

    void validateSign(Long userId);

}
