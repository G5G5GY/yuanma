package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("sys_user")
public class UserEntity extends BaseEntity{

    @TableId(type = IdType.AUTO,value = "user_id")
    private Long id;

    private Long deptId;

    private String username;
    // 用户昵称
    private String nickName;
    // 邮箱
    private String email;
    // 电话号码
    private String phone;

    //用户性别
    private String gender;

    // 头像真实名称
    private String avatarName;

    // 头像存储的路径
    private String avatarPath;

    // 密码
    private String password;

    private String salt;

    // 是否启用
    private Boolean enabled;

    // 是否为admin账号
    private Boolean isAdmin = false;

    // 最后修改密码的时间
    private Date pwdResetTime;

    private String relUserId;

    // 是否被锁定
    private Boolean unlocked = true;

    // 锁定时间LOCKDATE
    private Date lockDate;

    // 数据签名
    private String sign;


}
