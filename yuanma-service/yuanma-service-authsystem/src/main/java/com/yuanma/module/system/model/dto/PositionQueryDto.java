package com.yuanma.module.system.model.dto;

import lombok.Data;

@Data
public class PositionQueryDto {

    private String name;

    private Boolean enabled;

    private String startDate;

    private String endDate;

}
