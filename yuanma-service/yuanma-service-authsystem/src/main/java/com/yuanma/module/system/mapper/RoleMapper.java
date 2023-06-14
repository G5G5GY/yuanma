package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.RoleEntity;
import com.yuanma.module.system.model.dto.RoleDto;
import com.yuanma.module.system.model.dto.RoleQueryDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@DS("auth")
public interface RoleMapper extends BaseCrudMapper<RoleEntity> {

    List<RoleSmallDto> findByUserIds(@Param("userIds") List<Long> userIds);

    List<RoleSmallDto> findByUserId(@Param("userId")Long id);

    Integer countByDepts(@Param("deptIds") Set<Long> deptIds);

    List<RoleDto> findInMenuId(@Param("menuIds") List<Long> menuIds);

    Integer untiedMenu(@Param("menuId")Long id);

    RoleDto findByName(@Param("name")String name);

    RoleDto findById(@Param("roleId")Long roleId);

    Page<RoleDto> findByCond(Page page,@Param("cond")RoleQueryDto queryDto);

    List<RoleDto> findByCond(@Param("cond")RoleQueryDto queryDto);

    List<RoleDto> findAll(@Param("powerEnable")Boolean isPowerEnable);
}