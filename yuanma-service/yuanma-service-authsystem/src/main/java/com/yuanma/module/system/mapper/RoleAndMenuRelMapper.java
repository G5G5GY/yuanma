package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.RoleAndMenuRelEntity;
import org.apache.ibatis.annotations.Mapper;

@DS("auth")
public interface RoleAndMenuRelMapper extends BaseCrudMapper<RoleAndMenuRelEntity> {
}
