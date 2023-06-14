package com.yuanma.module.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AuthPowerApproveFlowUpdateDto {
    private Long id;
    @JsonIgnore
    private String status;
    private String remarker;
    private String userId;
}
