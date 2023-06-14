package com.yuanma.module.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yuanma.module.system.model.dto.DeptDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.service.DataService;
import com.yuanma.module.system.service.DeptService;
import com.yuanma.module.system.service.RoleCommonService;
import com.yuanma.module.system.service.RoleService;
import com.yuanma.module.utils.DataScopeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("authDataService")
@RequiredArgsConstructor
@DS("auth")
public class DataServiceImpl implements DataService {

    private final RoleService roleService;
    private final DeptService deptService;

    /**
     * 用户角色改变时需清理缓存
     * @param user /
     * @return /
     */
    public List<Long> getDeptIds(UserDto user) {
        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();
        // 查询用户角色
        List<RoleSmallDto> roleSet = roleService.findByUsersId(user.getId());
        // 获取对应的部门ID
        for (RoleSmallDto role : roleSet) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.find(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case THIS_LEVEL:
                    deptIds.add(user.getDept().getId());
                    break;
                case CUSTOMIZE:
                    deptIds.addAll(getCustomize(deptIds, role));
                    break;
                default:
                    break;
            }
        }
        return new ArrayList<>(deptIds);
    }

    /**
     * 获取自定义的数据权限
     * @param deptIds 部门ID
     * @param role 角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, RoleSmallDto role){
        List<DeptDto> depts = deptService.findByRoleId(role.getId());
        for (DeptDto dept : depts) {
            deptIds.add(dept.getId());
            List<DeptDto> deptChildren = deptService.findByPid(dept.getId());
            if (deptChildren != null && deptChildren.size() != 0) {
                deptIds.addAll(deptService.getDeptChildren(dept.getId(), deptChildren));
            }
        }
        return deptIds;
    }
}
