package com.yuanma.module.log.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("检索模块")
public class LogQuery {
    @ApiModelProperty(value = "系统模块",position = 1)
    private String title;
    @ApiModelProperty(value = "操作类型",position = 2)
    private Integer businessType;
    @ApiModelProperty(value = "操作人",position = 3)
    private String operName;
    @ApiModelProperty(value = "状态 0:正常 1:异常",position = 4)
    private String status;
    @ApiModelProperty(value = "操作时间",position = 5)
    private String startDate;
    @ApiModelProperty(value = "操作时间",position = 5)
    private String endDate;
    @ApiModelProperty(value = "分页数",position = 6)
    private Integer page = 0;
    @ApiModelProperty(value = "每页多少条",position = 7 )
    private Integer size = 10;
    @ApiModelProperty(value = "排序 sort=createTime,desc",position = 8 )
    private String sort;
}

