package com.yuanma.module.log.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.entity.LogEntity;
import com.yuanma.module.log.model.dto.LogQueryDto;

public interface LogService {

    Long create(LogEntity resources);

    Page<LogEntity> findByCond(Page pageable, LogQueryDto logDTO);

    LogEntity findById(long id);
}