package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @TableField( fill = FieldFill.INSERT)
    private String createBy;
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    @TableField( fill = FieldFill.INSERT)
    private Date createTime;
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
