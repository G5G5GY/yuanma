package com.yuanma.module.system.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuQueryDto {

    private String blurry;

    private Boolean pidIsNull;

    private Long pid;

    private String startDate;

    private String endDate;

    private Boolean powerEnable;


}
