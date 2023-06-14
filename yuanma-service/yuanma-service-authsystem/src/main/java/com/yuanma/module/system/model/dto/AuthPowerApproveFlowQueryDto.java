package com.yuanma.module.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthPowerApproveFlowQueryDto {

    private String startDate;

    private String endDate;
    @ApiModelProperty("类型 0:待处理  1:已处理 ")
    // 待处理：处理待审批的记录 已处理：驳回、审批通过、撤回
    private List<String> sstatus = new ArrayList<>();

    private String title;

    private String flowNo;

    private String userId;

}
