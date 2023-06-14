package com.yuanma.module.system.model.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto extends BaseDto{

    private Long id;

    private List<RoleSmallDto> roles;

    private List<PositionDto> jobs;

    private List<PositionDto> positions;

    private DeptDto dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String gender;

    private String email;

    private Boolean enabled;

    private String phone;

    private String avatarName;

    private String avatarPath;

    private String password;

    private String salt;

    //@JsonIgnore
    private Boolean isAdmin = false;

    private Date pwdResetTime;

    // 是否被锁定
    private Boolean unlocked;

    // 锁定时间
    private Date lockDate;

}
