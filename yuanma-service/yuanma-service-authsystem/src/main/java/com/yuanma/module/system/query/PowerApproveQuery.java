package com.yuanma.module.system.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PowerApproveQuery {
    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("流程编号")
    private String flowNo;

    @ApiModelProperty("审核状态")
    private String status;

    @ApiModelProperty("页数")
    private Integer page = 0;

    @ApiModelProperty("每页多少条")
    private Integer size = 1000;

    @ApiModelProperty("排序")
    private String sort;
}
