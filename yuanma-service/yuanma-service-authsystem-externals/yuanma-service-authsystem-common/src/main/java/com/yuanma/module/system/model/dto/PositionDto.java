package com.yuanma.module.system.model.dto;

import lombok.Data;

@Data
public class PositionDto extends BaseDto{

    private Long id;

    private Integer jobSort;

    private String name;

    private Boolean enabled;

}
