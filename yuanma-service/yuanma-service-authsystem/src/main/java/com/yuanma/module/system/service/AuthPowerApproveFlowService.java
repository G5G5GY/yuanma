package com.yuanma.module.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.entity.AuthPowerApproveFlowEntity;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowDto;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowQueryDto;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowUpdateDto;

public interface AuthPowerApproveFlowService {

    void finish(AuthPowerApproveFlowUpdateDto dto);

    AuthPowerApproveFlowDto detail(Long id);

    void next(AuthPowerApproveFlowUpdateDto dto);

    Page<AuthPowerApproveFlowDto> queryAll(AuthPowerApproveFlowQueryDto dto, Page page);

    String nextId();

    int save(AuthPowerApproveFlowEntity entity);

}
