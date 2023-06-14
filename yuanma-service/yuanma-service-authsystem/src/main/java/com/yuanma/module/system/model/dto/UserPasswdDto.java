package com.yuanma.module.system.model.dto;

import lombok.Data;

@Data
public class UserPasswdDto {
    private String oldPass;

    private String newPass;
}
