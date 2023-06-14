package com.yuanma.module.system.service;

import com.yuanma.module.system.model.dto.MenuDto;

import java.util.List;

public interface MenuCommonService {

    List<MenuDto> findByRoleId(Long roleId);

}
