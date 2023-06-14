package com.yuanma.module.system.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserDataScopeVO {

    // 用户ID
    private Long userId;
    // 用户名
    private String userName;

    private String nickName;
    // 数据权限
    private Integer dataScope;
    // 部门
    private List<Long> depts;

}
