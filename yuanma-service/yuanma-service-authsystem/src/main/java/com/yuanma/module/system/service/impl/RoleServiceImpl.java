package com.yuanma.module.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.aspect.AuthPowerActionType;
import com.yuanma.module.system.aspect.AuthSystemProperties;
import com.yuanma.module.system.aspect.YuanmaAuthPower;
import com.yuanma.module.system.entity.*;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.mapper.*;
import com.yuanma.module.system.model.dto.*;
import com.yuanma.module.system.service.RoleCommonService;
import com.yuanma.module.system.service.RoleService;
import com.yuanma.webmvc.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("authRoleService")
@DS("auth")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleAndDeptRelMapper roleAndDeptRelMapper;
    @Autowired
    private RoleAndMenuRelMapper roleAndMenuRelMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private AuthSystemProperties properties ;

    @Override
    public List<RoleDto> queryAll() {
        return roleMapper.findAll(properties.isPowerEnable());
    }

    @Override
    public Page<RoleDto> queryAll(RoleQueryDto queryDto, Page page) {
        queryDto.setPowerEnable(properties.isPowerEnable());
        Page<RoleDto> pager = roleMapper.findByCond(page, queryDto);
        for(RoleDto roleDto:pager.getRecords()){
            roleDto.setMenus(menuMapper.findByRoleId(roleDto.getId()));
        }
        return pager;
    }

    @Override
    public List<RoleDto> queryAll(RoleQueryDto queryDto) {
        return roleMapper.findByCond(queryDto);
    }

    public RoleDto findById(Long id) {
        RoleDto roleDto = roleMapper.findById(id);
        roleDto.setDepts(deptMapper.findByRoleId(roleDto.getId()));
        roleDto.setMenus(menuMapper.findByRoleId(roleDto.getId()));
        return roleDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @YuanmaAuthPower(title = "新增角色信息",module = "角色管理",type = AuthPowerActionType.ADD)
    public Long create(RoleDto roleDto) {
        if (roleMapper.findByName(roleDto.getName()) != null) {
            throw new BadRequestException("角色已经存在！");
        }
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(roleDto,roleEntity);
        roleMapper.insert(roleEntity);
        updateRoleRel( roleDto, roleEntity.getId());
        return roleEntity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @YuanmaAuthPower(title = "更新角色信息",module = "角色管理",type = AuthPowerActionType.UPDATE)
    public void update(RoleDto roleDto) {
        RoleEntity role = roleMapper.selectById(roleDto.getId());
        RoleDto role1 = roleMapper.findByName(roleDto.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new BadRequestException("角色已经存在！");
        }
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        role.setDataScope(roleDto.getDataScope());
        role.setLevel(roleDto.getLevel());
        roleMapper.updateById(role);
        updateRoleRel( roleDto, role.getId());
    }

    private void updateRoleRel(RoleDto roleDto,Long roleId){
        roleAndDeptRelMapper.delete(new LambdaQueryWrapper<RoleAndDeptRelEntity>().eq(RoleAndDeptRelEntity::getRoleId, roleId));
        List<DeptDto> depts = roleDto.getDepts();
        List<RoleAndDeptRelEntity> roleAndDeptRelEntities = depts.stream().map(c -> new RoleAndDeptRelEntity(roleId, c.getId())).collect(Collectors.toList());
        for(RoleAndDeptRelEntity roleAndDeptRelEntity:roleAndDeptRelEntities)  {
            roleAndDeptRelMapper.insert(roleAndDeptRelEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @YuanmaAuthPower(title = "删除角色信息",module = "角色管理",type = AuthPowerActionType.DELETE)
    public void delete(Set<Long> ids) {
        roleMapper.deleteBatchIds(ids);
        roleAndDeptRelMapper.delete(new LambdaQueryWrapper<RoleAndDeptRelEntity>().in(RoleAndDeptRelEntity::getRoleId, ids));
        roleAndMenuRelMapper.delete(new LambdaQueryWrapper<RoleAndMenuRelEntity>().in(RoleAndMenuRelEntity::getRoleId,ids));

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(RoleDto roleDto) {
        Long roleId = roleDto.getId();
        roleAndMenuRelMapper.delete(new LambdaQueryWrapper<RoleAndMenuRelEntity>().eq(RoleAndMenuRelEntity::getRoleId,roleId));
        List<MenuDto> menus = roleDto.getMenus();
        List<RoleAndMenuRelEntity> roleAndMenuRelEntities = menus.stream().map(c -> new RoleAndMenuRelEntity(roleId, c.getId())).collect(Collectors.toList());
        for(RoleAndMenuRelEntity roleAndMenuRelEntity:roleAndMenuRelEntities)  {
            roleAndMenuRelMapper.insert(roleAndMenuRelEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long menuId) {
        roleMapper.untiedMenu(menuId);
    }



    @Override
    public List<RoleSmallDto> findByUsersId(Long id) {
        return roleMapper.findByUserId(id);
    }

    public Integer findByRoles(List<RoleSmallDto> roles) {
        Set<RoleDto> roleDtos = new HashSet<>();
        for (RoleSmallDto role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDto::getLevel).collect(Collectors.toList()));
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userMapper.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<RoleDto> findInMenuId(List<Long> menuIds) {
        return roleMapper.findInMenuId(menuIds);
    }

    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        List<RoleSmallDto> roles = roleMapper.findByUserId(user.getId());
        Set<RoleDto> roleDtos = roles.stream().map(c -> {
            RoleDto dto = new RoleDto();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setDataScope(c.getDataScope());
            dto.setMenus(menuMapper.findByRoleId(c.getId()));
            return dto;
        }).collect(Collectors.toSet());

        permissions = roleDtos.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                .map(MenuDto::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
