package com.yuanma.module.security.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yuanma.module.security.entity.LoginLogEntity;
import com.yuanma.module.security.mapper.LoginLogMapper;
import com.yuanma.module.security.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@DS("auth")
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;


    @Override
    public Integer countStatusEqualOneByUserName(String username) {
        return loginLogMapper.countLoginResultEqualOneByUserName(username);
    }

    public Long save(String userName,Integer status) {
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        loginLogEntity.setUserName(userName);
        loginLogEntity.setLoginTime(new Date());
        loginLogEntity.setLoginResult(status);
        loginLogEntity.setLoginOrginResult(status);
        loginLogMapper.insert(loginLogEntity);
        return loginLogEntity.getID();
    }

    @Override
    public Long updateStatusEqualOneByUserName(String userName) {
        return loginLogMapper.updateLoginResultByUserIdAndFailure(userName);
    }

}
