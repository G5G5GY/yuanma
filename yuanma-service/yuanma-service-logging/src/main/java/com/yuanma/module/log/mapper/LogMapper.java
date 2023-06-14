package com.yuanma.module.log.mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.log.entity.LogEntity;

import com.yuanma.module.log.model.dto.LogQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper extends BaseCrudMapper<LogEntity> {

    Page<LogEntity> findByCond(Page pageable, @Param("cond") LogQueryDto logDTO);

}
