package com.yuanma.module.system.model.dto;

import lombok.Data;

@Data
public class DeptQueryDto {

    private String name;

    private Integer enabled ;

    private Long pid;

    private Boolean pidIsNull = false;

    private String startDate;

    private String endDate;
}
