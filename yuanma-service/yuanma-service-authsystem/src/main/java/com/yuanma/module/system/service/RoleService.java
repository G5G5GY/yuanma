package com.yuanma.module.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.entity.RoleEntity;
import com.yuanma.module.system.model.dto.RoleDto;
import com.yuanma.module.system.model.dto.RoleQueryDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import com.yuanma.module.system.model.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface RoleService extends RoleCommonService{

    /**
     * 查询全部数据
     * @return /
     */
    List<RoleDto> queryAll();

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    RoleDto findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    Long create(RoleDto resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(RoleDto resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
   // List<RoleSmallDto> findByUsersId(Long id);



    /**
     * 修改绑定的菜单
     * @param roleDTO /
     */
    void updateMenu(RoleDto roleDTO);

    /**
     * 解绑菜单
     * @param id /
     */
    void untiedMenu(Long id);

    /**
     * 待条件分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Page<RoleDto> queryAll(RoleQueryDto criteria, Page pageable);

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    List<RoleDto> queryAll(RoleQueryDto criteria);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(List<RoleSmallDto> roles);



    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verification(Set<Long> ids);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    List<RoleDto> findInMenuId(List<Long> menuIds);

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDto user);
}
