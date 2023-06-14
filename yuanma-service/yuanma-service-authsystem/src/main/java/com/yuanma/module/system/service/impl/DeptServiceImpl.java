package com.yuanma.module.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.aspect.AuthPowerActionType;
import com.yuanma.module.system.aspect.YuanmaAuthPower;
import com.yuanma.module.system.entity.DeptEntity;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.mapper.DeptMapper;
import com.yuanma.module.system.mapper.RoleMapper;
import com.yuanma.module.system.mapper.UserMapper;
import com.yuanma.module.system.model.dto.DeptDto;
import com.yuanma.module.system.model.dto.DeptQueryDto;
import com.yuanma.module.system.service.DeptService;
import com.yuanma.module.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("auth")
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Page<DeptDto> queryAll(DeptQueryDto query, Page page) throws Exception {
        Page<DeptDto> byCond = deptMapper.findByCond(query, page);
        return byCond;
    }

    @Override
    public DeptDto findById(Long id) {
        return deptMapper.findById(id);
    }

    @Override
    public List<DeptDto> findByIds(Set<Long> ids) {
        return deptMapper.findByIds(ids);
    }

    @Override
    public List<DeptDto> findByPid(long pid) {
        return deptMapper.findByPid(pid);
    }

    @Override
    public List<DeptDto> findByRoleId(Long id) {
        return deptMapper.findByRoleId(id);
    }

    @YuanmaAuthPower(title = "新增部门信息",module = "部门管理",type = AuthPowerActionType.ADD)
    @Transactional(rollbackFor = Exception.class)
    public Long create(DeptEntity resources) {
        deptMapper.insert(resources);
        resources.setSubCount(0);
        updateSubCnt(resources.getPid());
        return resources.getId();
    }

    @Override
    @YuanmaAuthPower(title = "更新部门",module = "部门管理",type = AuthPowerActionType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeptEntity resources) {

        DeptDto deptDto = findById(resources.getId());
        // 旧的部门
        Long oldPid = deptDto.getPid();
        Long newPid = resources.getPid();
        if(resources.getPid() != null && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        resources.setId(deptDto.getId());
        deptMapper.updateById(resources);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);

    }

    @Override
    @YuanmaAuthPower(title = "删除部门",module = "部门管理",type = AuthPowerActionType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<DeptDto> deptDtos) {
        for (DeptDto deptDto : deptDtos) {
            deptMapper.deleteById(deptDto.getId());
            updateSubCnt(deptDto.getPid());
        }
    }

    @Override
    public Set<DeptDto> getDeleteDepts(List<DeptDto> childDeptList, Set<DeptDto> deptDtos) {

        for (DeptDto dept : childDeptList) {
            deptDtos.add(dept);
            List<DeptDto> depts = deptMapper.findByPid(dept.getId());
            if(depts!=null && depts.size()!=0){
                getDeleteDepts(depts, deptDtos);
            }
        }
        return deptDtos;
    }

    @Override
    public List<Long> getDeptChildren(Long deptId, List<DeptDto> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept!=null && dept.getEnabled()){
                        List<DeptDto> depts = deptMapper.findByPid(dept.getId());
                        if(!depts.isEmpty()){
                            list.addAll(getDeptChildren(dept.getId(), depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }

    public List<DeptDto> getSuperior(DeptDto deptDto, List<DeptDto> depts) {
        if(deptDto.getPid() == null){
            depts.addAll(deptMapper.findByPidIsNull());
            return depts;
        }
        depts.addAll(deptMapper.findByPid(deptDto.getPid()));
        return getSuperior(findById(deptDto.getPid()), depts);
    }

    @Override
    public Object buildTree(List<DeptDto> deptDtos) {
        Set<DeptDto> trees = new LinkedHashSet<>();
        Set<DeptDto> depts= new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDto::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDto deptDTO : deptDtos) {
            isChild = false;
            if (deptDTO.getPid() == null) {
                trees.add(deptDTO);
            }
            for (DeptDto it : deptDtos) {
                if (it.getPid() != null && deptDTO.getId().equals(it.getPid())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if(isChild) {
                depts.add(deptDTO);
            } else if(deptDTO.getPid() != null &&  !deptNames.contains(findById(deptDTO.getPid()).getName())) {
                depts.add(deptDTO);
            }
        }

        if (EmptyUtils.isEmpty(trees)) {
            trees = depts;
        }
        Map<String,Object> map = new HashMap<>(2);
        map.put("totalElements",deptDtos.size());
        map.put("content",EmptyUtils.isEmpty(trees)? deptDtos :trees);
        return map;
    }

    @Override
    public void verification(Set<DeptDto> deptDtos) {
        Set<Long> deptIds = deptDtos.stream().map(DeptDto::getId).collect(Collectors.toSet());
        if(userMapper.countByDepts(deptIds) > 0){
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if(roleMapper.countByDepts(deptIds) > 0){
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }


    private void updateSubCnt(Long deptId){
        if(deptId != null){
            int count = deptMapper.countByPid(deptId);
            deptMapper.updateSubCntById(count, deptId);
        }
    }









}
