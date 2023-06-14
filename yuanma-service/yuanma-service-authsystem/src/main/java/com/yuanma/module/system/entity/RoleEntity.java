package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("sys_role")
public class RoleEntity extends BaseEntity{

    @TableId(type = IdType.AUTO,value = "role_id")
    private Long id;
    //名称
    private String name;

    //数据权限，全部 、 本级 、 自定义
    private String dataScope ;

    //级别，数值越小，级别越大
    private Integer level = 3;

    //描述
    private String description;

}
