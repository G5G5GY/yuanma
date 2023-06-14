package com.yuanma.module.system.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zsheng
 * @Date 2022/3/22 10:25
 * @Version 1.0
 * @Describe:
 */
@Data
public class WeatherQuery {
    private String region;
    private String provinces;
    private String city;
    private String weather;
    private String lowestTemp;
    private String highestTemp;
    @ApiModelProperty("开始日期")
    private String startTime;
    @ApiModelProperty("结束日期")
    private String endTime;
    @ApiModelProperty("页数")
    private Integer page = 0;
    @ApiModelProperty("每页多少条")
    private Integer size = 10;
}
