package com.yuanma.module.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.aspect.AuthPowerActionType;
import com.yuanma.module.system.aspect.YuanmaAuthPower;
import com.yuanma.module.system.entity.PositionEntity;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.mapper.PositionMapper;
import com.yuanma.module.system.mapper.UserMapper;
import com.yuanma.module.system.model.dto.PositionDto;
import com.yuanma.module.system.model.dto.PositionQueryDto;
import com.yuanma.module.system.service.PositionService;
import com.yuanma.module.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@DS("auth")
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> queryAll(PositionQueryDto queryDto, Page page) {
        Page<PositionDto> byCond = positionMapper.findByCond(page, queryDto);
        return PageUtil.toPage(byCond);
    }

    @Override
    public List<PositionDto> queryAll(PositionQueryDto queryDto) {

        return positionMapper.findByCond(queryDto);
    }




    @Override
    public PositionDto findById(Long id) {
        return positionMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @YuanmaAuthPower(title = "新增岗位信息",module = "岗位管理",type = AuthPowerActionType.ADD)
    public PositionEntity create(PositionEntity resources) {

        PositionDto job = positionMapper.findByName(resources.getName());
        if(job != null){
            throw new BadRequestException("岗位已经存在！");
        }
        positionMapper.insert(resources);
        return resources;
    }

    @Override
    @YuanmaAuthPower(title = "更新岗位信息",module = "岗位管理",type = AuthPowerActionType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public void update(PositionEntity resources) {
        PositionDto job = positionMapper.findById(resources.getId());
        PositionDto old = positionMapper.findByName(resources.getName());
        if(old != null && !old.getId().equals(resources.getId())){
            throw new BadRequestException("岗位已经存在！");
        }
        resources.setId(job.getId());
        positionMapper.updateById(resources);
    }

    @Override
    @YuanmaAuthPower(title = "删除岗位信息",module = "岗位管理",type = AuthPowerActionType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        positionMapper.deleteBatchIds(ids);
    }


    @Override
    public void verification(Set<Long> ids) {
        if(userMapper.countByJobs(ids) > 0){
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }
}
