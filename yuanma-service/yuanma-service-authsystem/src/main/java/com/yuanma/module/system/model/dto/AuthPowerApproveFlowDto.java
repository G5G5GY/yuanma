package com.yuanma.module.system.model.dto;

import com.yuanma.module.system.entity.AuthPowerApproveFlowLogEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AuthPowerApproveFlowDto {
    private Long id;
    private String flowNo;
    private String title;
    private String createBy;
    private Date createTime;

    @ApiModelProperty("类型 0:待审批 1:审批通过 2:已驳回 3:已撤回")
    private String status;
    private String module;
    @ApiModelProperty("类型 1:新增 2:更新 3:删除")
    private String type;

    private String reqParams;

    private List<AuthPowerApproveFlowLogEntity> logs;

}
