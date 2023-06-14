package com.yuanma.module.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.system.Constants;
import com.yuanma.module.system.aspect.AuthSystemProperties;
import com.yuanma.module.system.entity.MenuEntity;
import com.yuanma.module.system.model.dto.MenuDto;
import com.yuanma.module.system.model.dto.MenuQueryDto;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.query.MenuQuery;
import com.yuanma.module.system.service.MenuService;
import com.yuanma.module.system.service.UserService;
import com.yuanma.module.utils.EmptyUtils;
import com.yuanma.module.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.yuanma.module.utils.SystemApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = "菜单管理")
@RestController
@AllArgsConstructor
@RequestMapping(Constants.API + "/menus")
public class MenuController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private AuthSystemProperties properties ;

    // 获取前端所需菜单
    @ApiOperation("获取用户的菜单")
    @GetMapping(value = "/build")
    @YuanmaLog(value = "获取用户的菜单",moudle = "菜单管理")
    public SystemApiResult buildMenus(HttpServletRequest req){
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        UserDto user = userService.findById(userId);
        if(null == user){
            throw new SecurityException("user not found");
        }
        List<MenuDto> menuDtoList = menuService.findByUser(user.getId());
        List<MenuDto> menuDtos = menuService.buildTree(menuDtoList);
        return new SystemApiResult(menuService.buildMenus(menuDtos));
    }

    //返回全部的菜单
    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @YuanmaLog(value = "返回全部的菜单",moudle = "菜单管理")
    public SystemApiResult query(@RequestParam(value = "pid",required = false) Long pid){
        List<MenuDto> menuDtos = menuService.getMenus(pid);
        if(properties.isPowerEnable()) {
            List<MenuDto> removes = new ArrayList<>();
            for (MenuDto menuDto : menuDtos) {
                if ("1".equals(menuDto.getPowerFlag())) {
                    removes.add(menuDto);
                }
            }
            menuDtos.removeAll(removes);
        }
        return new SystemApiResult(menuDtos);

    }

    @ApiOperation("菜单一览")
    @GetMapping
    @YuanmaLog(value = "菜单一览",moudle = "菜单管理")
    public SystemApiResult query(MenuQuery query) throws Exception {
        MenuQueryDto queryDto = new MenuQueryDto();
        BeanUtils.copyProperties(query,queryDto);

        if(null == query.getPid()){
            queryDto.setPidIsNull(true);
        }
        Page page = new Page(query.getPage(),query.getSize());
        PageUtil.addOrder("menuSort,asc","menuSort,asc",page, MenuEntity.class);

        Page<MenuDto> pager = menuService.queryAll(queryDto, page);
        return new SystemApiResult(PageUtil.toPage(pager));
    }

    @ApiOperation("根据ID获取同级与上级数据")
    //查询菜单:根据ID获取同级与上级数据
    @PostMapping("/superior")
    @YuanmaLog(value = "根据ID获取同级与上级数据",moudle = "菜单管理")
    public SystemApiResult getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if(EmptyUtils.isNotEmpty(ids)){
            for (Long id : ids) {
                MenuDto menuDto = menuService.findById(id);
                menuDtos.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return new SystemApiResult(menuService.buildTree(new ArrayList<>(menuDtos)));
        }
        return new SystemApiResult(menuService.getMenus(null));
    }

    @ApiOperation("新增菜单")
    @PostMapping
    @YuanmaLog(value = "新增菜单",moudle = "菜单管理",type=LogActionType.ADD)
    public SystemApiResult create(@RequestBody MenuDto resources){
        if (resources.getId() != null) {
          //  throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        Long menuId =  menuService.create(resources);
        return new SystemApiResult(menuId);
    }

    //修改菜单
    @ApiOperation("修改菜单")
    @PutMapping
    @YuanmaLog(value = "修改菜单",moudle = "菜单管理",type=LogActionType.UPDATE)
    public SystemApiResult update(@RequestBody MenuDto resources){
        menuService.update(resources);
        return new SystemApiResult(resources.getId());
    }

    //删除菜单
    @ApiOperation("删除菜单")
    @DeleteMapping
    @YuanmaLog(value = "删除菜单",moudle = "菜单管理",type=LogActionType.DELETE)
    public SystemApiResult delete(@RequestBody Set<Long> ids){
        Set<MenuDto> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDto> menuList = menuService.getMenus(id);
            menuSet.add(menuService.findById(id));
            if(null != menuList && !menuList.isEmpty()) {
                menuSet = menuService.getDeleteMenus(menuList, menuSet);
            }
        }
        menuService.delete(menuSet);
        return new SystemApiResult(ids);
    }

}
