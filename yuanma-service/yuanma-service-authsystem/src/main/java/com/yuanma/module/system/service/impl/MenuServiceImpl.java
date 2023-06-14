package com.yuanma.module.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.aspect.AuthPowerActionType;
import com.yuanma.module.system.aspect.AuthSystemProperties;
import com.yuanma.module.system.aspect.YuanmaAuthPower;
import com.yuanma.module.system.entity.MenuEntity;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.mapper.MenuMapper;
import com.yuanma.module.system.mapper.RoleMapper;
import com.yuanma.module.system.model.dto.MenuDto;
import com.yuanma.module.system.model.dto.MenuQueryDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import com.yuanma.module.system.model.vo.MenuMetaVo;
import com.yuanma.module.system.model.vo.MenuVo;
import com.yuanma.module.system.service.MenuCommonService;
import com.yuanma.module.system.service.MenuService;
import com.yuanma.module.utils.EmptyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("authMenuService")
@DS("auth")
public class MenuServiceImpl implements MenuService  {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AuthSystemProperties properties ;

    @Override
    public Page<MenuDto> queryAll(MenuQueryDto menuQuery, Page page) {
        menuQuery.setPowerEnable(properties.isPowerEnable());
        return menuMapper.findByCond(page,menuQuery);
    }

    @Override
    @YuanmaAuthPower(title = "新增菜单信息",module = "菜单管理",type = AuthPowerActionType.ADD)
    public Long create(MenuDto menu) {
        MenuEntity resources = new MenuEntity();
        BeanUtil.copyProperties(menu,resources);
        if(StringUtils.isNotBlank(menu.getComponentName())){
            if(menuMapper.findByComponentName(menu.getComponentName()) != null){
                throw new BadRequestException("菜单名称已经存在");
            }
        }
        if(null == menu.getPid() || menu.getPid().equals(0L)){
            resources.setPid(null);
        }
        if(resources.getIFrame()){
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http)||resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        menuMapper.insert(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(resources.getPid());
        return resources.getId();
    }


    @Override
    public List<MenuDto> findByUser(Long userId) {
        List<RoleSmallDto> roles = roleMapper.findByUserId(userId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
        return menuMapper.findByRoleIdsAndTypeNot(roleIds, 2);
    }

    @Override
    @YuanmaAuthPower(title = "更新菜单信息",module = "菜单管理",type = AuthPowerActionType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDto resources) {
        if(resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        MenuEntity menu = menuMapper.selectById(resources.getId());

        if(resources.getIFrame()){
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http)||resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }

        if(resources.getPid().equals(0L)){
            resources.setPid(null);
        }

        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resources.getPid();

        if(StringUtils.isNotBlank(resources.getComponentName())){
            MenuDto menu1 = menuMapper.findByComponentName(resources.getComponentName());
            if(menu1 != null && !menu1.getId().equals(menu.getId())){
                throw new BadRequestException("名字不能相同");
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setComponentName(resources.getComponentName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        menuMapper.updateById(menu);
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }

    @Override
    public Set<MenuDto> getDeleteMenus(List<MenuDto> menuList, Set<MenuDto> menuSet) {
        // 递归找出待删除的菜单
        for (MenuDto menu1 : menuList) {
            menuSet.add(menu1);
            List<MenuDto> menus = menuMapper.findByPid(menu1.getId());
            if(menus!=null && menus.size()!=0){
                getDeleteMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    @YuanmaAuthPower(title = "删除菜单信息",module = "菜单管理",type = AuthPowerActionType.DELETE,paramGenerics = MenuDto.class)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<MenuDto> menuDtos) {
        for (MenuDto menu : menuDtos) {
            roleMapper.untiedMenu(menu.getId());
            menuMapper.deleteById(menu.getId());
            updateSubCnt(menu.getPid());
        }
    }

    @Override
    public List<MenuDto> getMenus(Long pid) {
        if(pid != null && !pid.equals(0L)){
            return menuMapper.findByPid(pid);
        } else {
            return menuMapper.findByPidIsNull();
        }
    }

    @Override
    public MenuDto findById(long id) {
        return menuMapper.findById(id);
    }

    public List<MenuDto> findByIds(Set<Long> ids){
        return menuMapper.findByIds(ids);
    }

    public List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menus) {
        if(menuDto.getPid() == null){
            return menuMapper.findByPidIsNull();
        }
        menus.addAll(menuMapper.findByPid(menuDto.getPid()));
        return getSuperior(findById(menuDto.getPid()), menus);
    }

    public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDto menuDTO : menuDtos) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (MenuDto it : menuDtos) {
                if (null != it.getPid() && null != menuDTO.getId() && menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if(trees.size() == 0){
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(EmptyUtils.isNotEmpty(menuDTO.getComponentName())  ? menuDTO.getComponentName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() :menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if(!menuDTO.getIFrame()){
                            if(menuDTO.getPid() == null){
                                menuVo.setComponent(EmptyUtils.isEmptyWithTryCatch(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                            }else if(!EmptyUtils.isEmptyWithTryCatch(menuDTO.getComponent())){
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(),menuDTO.getIcon(),!menuDTO.getCache()));
                        if(menuDtoList !=null && menuDtoList.size()!=0){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if(menuDTO.getPid() == null){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if(!menuDTO.getIFrame()){
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }


    public MenuDto findOne(Long id) {
        return findById(id);
    }


    private void updateSubCnt(Long menuId){
        if(menuId != null){
            int count = menuMapper.countByPid(menuId);
            menuMapper.updateSubCntById(count, menuId);
        }
    }

    @Override
    public List<MenuDto> findByRoleId(Long roleId) {
        return menuMapper.findByRoleId(roleId);
    }
}
