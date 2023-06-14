package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@TableName("sys_menu")
public class MenuEntity extends BaseEntity{

    @TableId(type = IdType.AUTO,value = "menu_id")
    private Long id;

    private String title;

    @TableField("component")
    private String componentName;

    // 排序
    private Integer menuSort = 999;

    // 组件路径
    private String component;

    // 路由地址
    private String path;

    // 菜单类型，目录、菜单、按钮
    private Integer type;

    // 权限标识
    private String permission;

    // 菜单图标
    private String icon;

    // 缓存()
    @TableField(exist=false)
    private Boolean cache;

    // 是否隐藏
    private Boolean hidden;

    // 上级菜单
    private Long pid;

    // 子节点数目
    private Integer subCount = 0;

    //外链菜单
    private Boolean iFrame;
}
