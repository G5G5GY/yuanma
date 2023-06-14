package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.AuthPowerApproveFlowEntity;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowDto;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowQueryDto;
import org.apache.ibatis.annotations.Param;
@DS("auth")
public interface AuthPowerApproveFlowMapper extends BaseCrudMapper<AuthPowerApproveFlowEntity> {

    Page<AuthPowerApproveFlowDto> findByCond(@Param("cond") AuthPowerApproveFlowQueryDto query, Page page);

    Long nextIdByNow();

    AuthPowerApproveFlowDto findById(@Param("id")Long id);
}
