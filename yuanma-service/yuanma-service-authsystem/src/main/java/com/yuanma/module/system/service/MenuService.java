package com.yuanma.module.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.entity.MenuEntity;
import com.yuanma.module.system.model.dto.MenuDto;
import com.yuanma.module.system.model.dto.MenuQueryDto;
import com.yuanma.module.system.model.vo.MenuVo;

import java.util.List;
import java.util.Set;

public interface MenuService extends MenuCommonService{

    List<MenuDto> findByUser(Long userId);

    List<MenuDto> buildTree(List<MenuDto> menuDtoList);

    List<MenuVo> buildMenus(List<MenuDto> menuDtos) ;

    List<MenuDto> getMenus(Long pid);

    Page<MenuDto> queryAll(MenuQueryDto menuQuery,Page page);

    MenuDto findOne(Long id);

    MenuDto findById(long id);

    List<MenuDto> findByIds(Set<Long> ids);

    List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menus);

    Long create(MenuDto menu);

    void update(MenuDto menu);

    Set<MenuDto> getDeleteMenus(List<MenuDto> menuList, Set<MenuDto> menuSet);

    void delete(Set<MenuDto> menuDtos);
}
