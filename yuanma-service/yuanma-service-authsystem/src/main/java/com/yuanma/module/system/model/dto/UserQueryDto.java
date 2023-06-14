package com.yuanma.module.system.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserQueryDto {

    private Long id;

    private List<Long> userIds = new ArrayList<>();

    private Set<Long> deptIds = new HashSet<>();

    private String blurry;

    private Boolean enabled;

    private Long deptId;

    private String startDate;

    private String endDate;

    private Boolean powerEnable;

}
