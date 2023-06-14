package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_users_roles")
public class UserAndRoleRelEntity {
    private Long userId;
    private Long roleId;
}
