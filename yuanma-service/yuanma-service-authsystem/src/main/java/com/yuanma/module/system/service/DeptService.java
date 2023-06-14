package com.yuanma.module.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.entity.DeptEntity;
import com.yuanma.module.system.model.dto.DeptDto;
import com.yuanma.module.system.model.dto.DeptQueryDto;

import java.util.List;
import java.util.Set;

public interface DeptService {



    Page<DeptDto> queryAll(DeptQueryDto criteria, Page page) throws Exception;
    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    DeptDto findById(Long id);

    List<DeptDto> findByIds(Set<Long> ids);

    /**
     * 创建
     * @param dept /
     */
    Long create(DeptEntity dept);

    /**
     * 编辑
     * @param dept /
     */
    void update(DeptEntity dept);

    /**
     * 删除
     * @param deptDtos /
     *
     */
    void delete(Set<DeptDto> deptDtos);

    /**
     * 根据PID查询
     * @param pid /
     * @return /
     */
    List<DeptDto> findByPid(long pid);

    /**
     * 根据角色ID查询
     * @param id /
     * @return /
     */
    List<DeptDto> findByRoleId(Long id);


    /**
     * 获取待删除的部门
     * @param deptList /
     * @param deptDtos /
     * @return /
     */
    Set<DeptDto> getDeleteDepts(List<DeptDto> deptList, Set<DeptDto> deptDtos);

    /**
     * 根据ID获取同级与上级数据
     * @param deptDto /
     * @param depts /
     * @return /
     */
    List<DeptDto> getSuperior(DeptDto deptDto, List<DeptDto> depts);

    /**
     * 构建树形数据
     * @param deptDtos /
     * @return /
     */
    Object buildTree(List<DeptDto> deptDtos);

    /**
     * 获取
     * @param deptId
     * @param deptList
     * @return
     */
    List<Long> getDeptChildren(Long deptId, List<DeptDto> deptList);

    /**
     * 验证是否被角色或用户关联
     * @param deptDtos /
     */
    void verification(Set<DeptDto> deptDtos);

}
