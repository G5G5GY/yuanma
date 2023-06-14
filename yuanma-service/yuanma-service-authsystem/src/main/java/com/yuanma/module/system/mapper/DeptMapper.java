package com.yuanma.module.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.mapper.BaseCrudMapper;
import com.yuanma.module.system.entity.DeptEntity;
import com.yuanma.module.system.model.dto.DeptDto;
import com.yuanma.module.system.model.dto.DeptQueryDto;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.model.dto.UserQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


@DS("auth")
public interface DeptMapper extends BaseCrudMapper<DeptEntity> {

    Page<DeptDto> findByCond(@Param("cond") DeptQueryDto query, Page page);

    List<DeptDto> findByPid(@Param("pid") Long pid);

    DeptDto findById(@Param("id")Long id);

    List<DeptDto> findByIds(@Param("deptIds")Set<Long> ids);

    Integer countByPid(@Param("pid") Long pid);

    List<DeptDto> findByPidIsNull();

    Integer updateSubCntById(@Param("count") Integer count,@Param("deptId")  Long id);

    List<DeptDto> findByRoleId(@Param("roleId") Long roleId);

}
