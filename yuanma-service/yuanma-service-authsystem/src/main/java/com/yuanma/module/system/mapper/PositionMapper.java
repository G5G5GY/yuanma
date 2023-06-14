package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.PositionEntity;
import com.yuanma.module.system.model.dto.PositionDto;
import com.yuanma.module.system.model.dto.PositionQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS("auth")
public interface PositionMapper extends BaseCrudMapper<PositionEntity> {

    PositionDto findById(@Param("id")Long jobId);

    PositionDto findByName(@Param("name")String name);

    Page<PositionDto> findByCond(Page page, @Param("cond")PositionQueryDto dto);

    List<PositionDto> findByCond(@Param("cond")PositionQueryDto dto);

    List<PositionDto> findByUserId(@Param("userId")Long userId);

}
