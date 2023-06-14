package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.MenuEntity;
import com.yuanma.module.system.model.dto.MenuDto;
import com.yuanma.module.system.model.dto.MenuQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@DS("auth")
public interface MenuMapper extends BaseCrudMapper<MenuEntity> {

    Integer countByPid(Long id);

    void updateSubCntById(int count, Long menuId);

    List<MenuDto> findByPidIsNull();

    List<MenuDto> findByPid(@Param("pid")Long pid);

    Page<MenuDto> findByCond(Page page, @Param("cond") MenuQueryDto dto);

    MenuDto findById(@Param("id")Long id);

    List<MenuDto> findByIds(@Param("menuIds")Set<Long> ids);

    MenuDto findByComponentName(@Param("componentName")String componentName);

    List<MenuDto> findByRoleIdsAndTypeNot(@Param("roleIds")Set<Long> roleIds, @Param("type")int type);

    List<MenuDto> findByRoleId(@Param("roleId")Long roleId);

}
