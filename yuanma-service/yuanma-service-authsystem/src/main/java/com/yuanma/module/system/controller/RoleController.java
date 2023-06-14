package com.yuanma.module.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.system.Constants;
import com.yuanma.module.system.entity.RoleEntity;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.model.dto.RoleDto;
import com.yuanma.module.system.model.dto.RoleQueryDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.query.RoleQuery;
import com.yuanma.module.system.service.RoleService;
import com.yuanma.module.system.service.UserService;
import com.yuanma.module.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuanma.module.utils.SystemApiResult;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "角色管理")
@RestController
@AllArgsConstructor
@RequestMapping(Constants.API + "/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @ApiOperation("角色详细")
    @GetMapping(value = "/{id}")
    @YuanmaLog(value = "角色详细",moudle = "角色管理")
    public SystemApiResult query(@PathVariable("id") Long id){
        return new SystemApiResult(roleService.findById(id));
    }

    @ApiOperation("全部角色")
    @GetMapping(value = "/all")
    @YuanmaLog(value = "全部角色",moudle = "角色管理")
    public SystemApiResult query(){
        return new SystemApiResult(roleService.queryAll());
    }

    @ApiOperation("角色一览")
    @GetMapping
    @YuanmaLog(value = "角色一览",moudle = "角色管理")
    public SystemApiResult query(RoleQuery query){
        RoleQueryDto queryDto = new RoleQueryDto();
        BeanUtils.copyProperties(query,queryDto);
        Page page = new Page(query.getPage(),query.getSize());
        PageUtil.addOrder(query.getSort(),"level,asc",page, RoleEntity.class);
        Page<RoleDto> roleDtoPage = roleService.queryAll(queryDto, page);
        return new SystemApiResult(PageUtil.toPage(page));
    }


    // 新增角色
    @ApiOperation("新增角色")
    @PostMapping
    @YuanmaLog(value = "新增角色",moudle = "角色管理",type = LogActionType.ADD)
    public SystemApiResult create(HttpServletRequest req,@RequestBody RoleDto resources){
        if (resources.getId() != null) {
            throw new BadRequestException("qeqc");
        }
        getLevels(req,resources.getLevel());
        Long roleId = roleService.create(resources);
        return new SystemApiResult(roleId);
    }


    // 修改角色
    @ApiOperation("修改角色")
    @PutMapping
    @YuanmaLog(value = "修改角色",moudle = "角色管理",type = LogActionType.UPDATE)
    public SystemApiResult update(HttpServletRequest req,@RequestBody RoleDto resources){
        getLevels(req,resources.getLevel());
        roleService.update(resources);
        return new SystemApiResult(resources.getId());
    }

    // 删除角色
    @ApiOperation("删除角色")
    @DeleteMapping
    @YuanmaLog(value = "删除角色",moudle = "角色管理",type = LogActionType.DELETE)
    public SystemApiResult delete( HttpServletRequest req,@RequestBody  Set<Long> ids){
        for (Long id : ids) {
            RoleDto role = roleService.findById(id);
            getLevels(req,role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.delete(ids);
        return new SystemApiResult(ids);
    }

    // 修改角色菜单
    @ApiOperation("修改角色菜单")
    @PutMapping("/menu")
    @YuanmaLog(value = "修改角色菜单",moudle = "角色管理",type = LogActionType.UPDATE)
    public SystemApiResult updateMenu(HttpServletRequest req,@RequestBody RoleDto resources){
        RoleDto role = roleService.findById(resources.getId());
        getLevels(req,role.getLevel());
        roleService.updateMenu(resources);
        return new SystemApiResult(resources.getId());
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private int getLevels(HttpServletRequest req, Integer level){
        String uid = getUid(req);
        if(null != uid) {
            UserDto user = userService.findById(Long.valueOf(uid));
            List<Integer> levels = roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
            int min = Collections.min(levels);
            if (level != null) {
                if (level < min) {
                    throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
                }
            }
            return min;
        }
        return 0;
    }

    private String getUid(HttpServletRequest req){
        Object uid = req.getAttribute("uid");
        if(null != uid){
            return uid.toString();
        }
        return null;
    }

}
