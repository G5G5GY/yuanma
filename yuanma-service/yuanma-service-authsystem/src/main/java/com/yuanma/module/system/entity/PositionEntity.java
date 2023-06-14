package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("sys_job")
public class PositionEntity extends BaseEntity{

    @TableId(type = IdType.AUTO,value = "job_id")
    private Long id;
    //岗位名称
    private String name;
    //岗位排序
    private Long jobSort;
    //是否启用
    private Boolean enabled;

}
