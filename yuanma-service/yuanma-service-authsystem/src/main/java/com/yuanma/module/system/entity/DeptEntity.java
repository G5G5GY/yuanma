package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("sys_dept")
public class DeptEntity  extends BaseEntity{

    @TableId(type = IdType.AUTO,value = "dept_id")
    private Long id;

    // 排序
    private Integer deptSort;

    // 部门名称
    private String name;

    //是否启用
    private Boolean enabled;

    //上级部门
    private Long pid;

    //子节点数目
    private Integer subCount = 0;


}
