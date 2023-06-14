package com.yuanma.module.log.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.entity.LogEntity;
import com.yuanma.module.log.mapper.LogMapper;
import com.yuanma.module.log.model.dto.LogQueryDto;
import com.yuanma.module.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@DS("auth")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public Long create(LogEntity resources) {
        logMapper.insert(resources);
        return new Long(resources.getId());
    }

    @Override
    public Page<LogEntity> findByCond(Page pageable, LogQueryDto logDTO) {
        return logMapper.findByCond(pageable,logDTO);
    }

    @Override
    public LogEntity findById(long id) {
        return logMapper.selectById(id);
    }
}