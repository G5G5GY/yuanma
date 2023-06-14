package com.yuanma.module.security.service;

public interface LoginLogService {

    final  Integer SUCCESS = 1;
    final  Integer FAILURE = 0;

    Integer countStatusEqualOneByUserName(String username);

    Long save(String userName,Integer status);

    Long updateStatusEqualOneByUserName(String userName);

}
