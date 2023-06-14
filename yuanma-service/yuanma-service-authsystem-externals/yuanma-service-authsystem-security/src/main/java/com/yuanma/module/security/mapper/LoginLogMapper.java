package com.yuanma.module.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanma.module.security.entity.LoginLogEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface LoginLogMapper extends BaseMapper<LoginLogEntity> {

    @Select("select count(1) from sys_Login_Log where login_result = 0 and user_name = #{userName}")
    Integer countLoginResultEqualOneByUserName(@Param("userName") String userName);

    @Update("update sys_Login_Log set login_result = 1 where user_name = #{userName} and  login_result = 0")
    Long updateLoginResultByUserIdAndFailure(@Param("userName") String userName);

    @Select("select * from sys_Login_Log where login_result = #{loginResult} ")
    List<LoginLogEntity> findByLoginResult(@Param("loginResult")String loginResult);

}
