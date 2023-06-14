package com.yuanma.module.system.service;

import com.yuanma.module.system.model.dto.UserDto;

import java.util.List;

public interface DataCommonService {

    List<Long> getDeptIds(UserDto user);

}
