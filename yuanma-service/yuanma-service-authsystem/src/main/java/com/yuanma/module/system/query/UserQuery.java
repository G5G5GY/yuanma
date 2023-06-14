package com.yuanma.module.system.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserQuery {

    private Long id;

    //private List<Long> userIds = new ArrayList<>();

    //private Set<Long> deptIds = new HashSet<>();

    private String blurry;

    private Boolean enabled;

    private Long deptId;

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
