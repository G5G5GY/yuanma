package com.yuanma.module.log.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogQueryDto {
    private String title;
    private String businessType;
    private String operName;
    private String status;
    private String startDate;
    private String endDate;
}
