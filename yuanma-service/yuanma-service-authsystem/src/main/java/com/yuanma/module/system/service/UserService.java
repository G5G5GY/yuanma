package com.yuanma.module.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.entity.UserEntity;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.model.dto.UserQueryDto;
import com.yuanma.module.system.model.dto.UserSmallDto;

import java.util.List;
import java.util.Set;

public interface UserService extends UserCommonService{

    List<UserDto> queryAll(UserQueryDto userQueryDto);

    Page<UserDto> queryAll(UserQueryDto userQueryDto, Page page);

    UserDto findById(Long userId);

    List<UserSmallDto> queryAllByUserId(UserQueryDto userQueryDto);



    Long create(UserDto user);

    void update(UserDto userDto);

    void delete(Set<Long> ids);

    int updatePass(String userName,String passwd,String salt);

    int updatePass(String userName,String passwd);

    int updateEmail(String userName,String email);

    void checkCreateUser(UserDto userDto);

    void checkUpdateUser(UserDto resources);



}
