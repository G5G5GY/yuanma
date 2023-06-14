package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_roles_menus")
public class RoleAndMenuRelEntity {
    private Long roleId;
    private Long menuId;
}
