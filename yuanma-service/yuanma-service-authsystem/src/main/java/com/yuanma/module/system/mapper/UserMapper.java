package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.UserEntity;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.model.dto.UserQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

@DS("auth")
public interface UserMapper extends BaseCrudMapper<UserEntity> {

   List<UserDto> findByCond(@Param("cond")UserQueryDto query);

   Page<UserDto> queryAll(@Param("cond")UserQueryDto userQueryDto, Page page);

   UserDto findByUsername(@Param("userName")String username);

   UserDto findByEmail(@Param("email")String email);

   UserDto findById(@Param("userId")Long userId);

   Integer countByDepts(@Param("deptIds") Set<Long> deptIds);

   Integer countByJobs(@Param("jobIds") Set<Long> ids);

   Integer countByRoles(@Param("roleIds") Set<Long> ids);

   List<UserDto> findByRoleId(@Param("roleId")Long roleId);

   Integer updateUserName(@Param("userName")String userName, @Param("userId")Long userId);

   Integer updateEmail(@Param("userName")String userName, @Param("email")String email);

   Integer updatePass(@Param("userName")String username, @Param("password")String pass,
                      @Param("salt")String salt,
                      @Param("lastPasswordResetTime")Date lastPasswordResetTime);

   Integer lock(@Param("userName")String username,@Param("lockDate")Date lockDate);

   Integer unlock(@Param("userName")String username);

   Date getLockDateByUserName(@Param("userName")String username);


   Integer updateSignById(@Param("sign")String sign, @Param("userId")Long userId);

}