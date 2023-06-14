package com.yuanma.module.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.system.Constants;
import com.yuanma.module.system.entity.PositionEntity;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.model.dto.PositionQueryDto;
import com.yuanma.module.system.query.PositionQuery;
import com.yuanma.module.system.service.PositionService;
import com.yuanma.module.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.yuanma.module.utils.SystemApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api(tags = "岗位管理")
@RestController
@AllArgsConstructor
@RequestMapping(Constants.API + "/positions")
public class PositionController {

    @Autowired
    private PositionService jobService;


    //查询岗位
    @ApiOperation("岗位一览")
    @GetMapping
    @YuanmaLog(value = "岗位一览",moudle = "岗位管理")
    public SystemApiResult query(PositionQuery query){
        PositionQueryDto queryDto = new PositionQueryDto();
        BeanUtils.copyProperties(query,queryDto);

        Page page = new Page(query.getPage(),query.getSize());
        PageUtil.addOrder(query.getSort(),"createTime,desc",page,PositionEntity.class);
        return new SystemApiResult(jobService.queryAll(queryDto, page));
    }


    // 新增岗位
    @ApiOperation("新增岗位")
    @PostMapping
    @YuanmaLog(value = "新增岗位",moudle = "岗位管理",type = LogActionType.ADD)
    public SystemApiResult create(@RequestBody PositionEntity resources){
        if (resources.getId() != null) {
            throw new BadRequestException("岗位Id不应该存在");
        }
        PositionEntity position = jobService.create(resources);
        return new SystemApiResult(position.getId());
    }

    //修改岗位
    @ApiOperation("修改岗位")
    @PutMapping
    @YuanmaLog(value = "修改岗位",moudle = "岗位管理",type = LogActionType.UPDATE)
    public SystemApiResult update(@RequestBody PositionEntity resources){
        jobService.update(resources);
        return new SystemApiResult(resources.getId());
    }


    //删除岗位
    @ApiOperation("删除岗位")
    @DeleteMapping
    @YuanmaLog(value = "删除岗位",moudle = "岗位管理",type = LogActionType.DELETE)
    public SystemApiResult delete(@RequestBody Set<Long> ids){
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return new SystemApiResult(ids);
    }

}
