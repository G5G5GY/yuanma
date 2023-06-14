package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_auth_power_approve_flow")
public class AuthPowerApproveFlowEntity extends BaseEntity{

    @TableId(type= IdType.AUTO)
    private Long id;
    private String flowNo;
    private String title;
    private Integer type = 1;
    private String module;
    private String status;
    private String reqInstanceClass;
    private String reqMethod;
    private String reqParamsType;
    private String reqParams;
    private String remarker;
    private String delFlag = "0";

}
