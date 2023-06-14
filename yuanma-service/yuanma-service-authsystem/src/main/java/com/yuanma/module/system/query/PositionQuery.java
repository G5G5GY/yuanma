package com.yuanma.module.system.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PositionQuery {

    private String name;

    private Boolean enabled;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("页数")
    private Integer page = 0;

    @ApiModelProperty("每页多少条")
    private Integer size = 10;

    @ApiModelProperty("排序")
    private String sort;


}
