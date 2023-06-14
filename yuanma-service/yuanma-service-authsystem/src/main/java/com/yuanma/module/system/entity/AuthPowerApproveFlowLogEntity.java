package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_auth_power_approve_flow_log")
public class AuthPowerApproveFlowLogEntity extends BaseEntity{

    @TableId(type= IdType.AUTO)
    private Long id;
    private Long flowId;
    private String operator = "安全保密管理员";
    private String approveStatus;
    private String remarker;

}
