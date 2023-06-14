package com.yuanma.module.system.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeptQuery {

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("状态")
    private Boolean enabled = false;

    @ApiModelProperty("父ID")
    private Long pid;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("页数")
    private Integer page = 0;

    @ApiModelProperty("每页多少条")
    private Integer size = 1000;

    @ApiModelProperty("排序")
    private String sort;

}
