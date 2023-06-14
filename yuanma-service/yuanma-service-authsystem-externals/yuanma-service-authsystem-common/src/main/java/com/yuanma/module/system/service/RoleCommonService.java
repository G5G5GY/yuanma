package com.yuanma.module.system.service;

import com.yuanma.module.system.model.dto.RoleSmallDto;

import java.util.List;

public interface RoleCommonService {

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);


}
