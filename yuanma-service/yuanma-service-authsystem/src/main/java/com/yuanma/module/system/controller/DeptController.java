package com.yuanma.module.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.system.Constants;
import com.yuanma.module.system.entity.DeptEntity;
import com.yuanma.module.system.model.dto.DeptDto;
import com.yuanma.module.system.model.dto.DeptQueryDto;
import com.yuanma.module.system.query.DeptQuery;
import com.yuanma.module.system.service.DeptService;
import com.yuanma.module.utils.PageUtil;
import com.yuanma.module.utils.SystemApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Api(tags = "部门管理")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(Constants.API + "/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @ApiOperation("一览")
    @GetMapping
    @YuanmaLog(value = "一览",moudle = "部门管理")
    public SystemApiResult query(DeptQuery deptQuery) throws Exception {
        DeptQueryDto dto = new DeptQueryDto();
        BeanUtils.copyProperties(deptQuery,dto);
        deptQuery.setSort("deptSort,asc");
        if(null == deptQuery.getPid()){
            dto.setPidIsNull(true);
        }
        if(deptQuery.getEnabled()){
            dto.setEnabled(1);
        }

        Page page = new Page(deptQuery.getPage(),deptQuery.getSize());
        PageUtil.addOrder(deptQuery.getSort(),"deptSort,asc",page,DeptEntity.class);
        Page<DeptDto> deptDtos = deptService.queryAll(dto, page);
        return new SystemApiResult(PageUtil.toPage(deptDtos));
    }

    //查询部门:根据ID获取同级与上级数据
    @ApiOperation("根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @YuanmaLog(value = "根据ID获取同级与上级数据",moudle = "部门管理",type=LogActionType.UPDATE)
    public SystemApiResult getSuperior(@RequestBody List<Long> ids) {
        Set<DeptDto> deptDtos  = new LinkedHashSet<>();
        for (Long id : ids) {
            DeptDto deptDto = deptService.findById(id);
            List<DeptDto> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        return new SystemApiResult(deptService.buildTree(new ArrayList<>(deptDtos)));
    }

    @ApiOperation("新增部门")
    @PostMapping
    @YuanmaLog(value = "新增部门",moudle = "部门管理",type=LogActionType.ADD)
    public SystemApiResult create(@RequestBody DeptEntity dept){
        Long deptId = deptService.create(dept);
        return new SystemApiResult(deptId);
    }

    @ApiOperation("修改部门")
    @PutMapping
    @YuanmaLog(value = "修改部门",moudle = "部门管理",type=LogActionType.UPDATE)
    public SystemApiResult update(@RequestBody DeptEntity resources){
        deptService.update(resources);
        return new SystemApiResult();
    }

    @ApiOperation("删除部门")
    @DeleteMapping
    @YuanmaLog(value = "删除部门",moudle = "部门管理",type=LogActionType.DELETE)
    public SystemApiResult delete(@RequestBody Set<Long> ids){
        Set<DeptDto> deptDtos = new HashSet<>();
        for (Long id : ids) {
            List<DeptDto> childDepts = deptService.findByPid(id);
            deptDtos.add(deptService.findById(id));
            if(null != childDepts && !childDepts.isEmpty()){
                deptDtos = deptService.getDeleteDepts(childDepts, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos);
        deptService.delete(deptDtos);
        return new SystemApiResult(ids);
    }

}
