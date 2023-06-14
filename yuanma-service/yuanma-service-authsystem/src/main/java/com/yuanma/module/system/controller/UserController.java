package com.yuanma.module.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yuanma.module.security.config.password.PBKDF2WithHmacSHA256PasswordEncoder;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.security.annotation.AnonymousGetMapping;
import com.yuanma.module.security.config.password.SaltPasswordEncoder;
import com.yuanma.module.system.Constants;
import com.yuanma.module.system.entity.UserEntity;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.model.dto.DeptDto;
import com.yuanma.module.system.model.dto.MenuDto;
import com.yuanma.module.system.model.dto.RoleDto;
import com.yuanma.module.system.model.dto.RoleSmallDto;
import com.yuanma.module.system.model.dto.UserDto;
import com.yuanma.module.system.model.dto.UserQueryDto;
import com.yuanma.module.system.model.dto.UserSmallDto;
import com.yuanma.module.system.model.vo.UserDataScopeVO;
import com.yuanma.module.system.query.UserPassQuery;
import com.yuanma.module.system.query.UserQuery;
import com.yuanma.module.system.service.*;
import com.yuanma.module.utils.DataScopeType;
import com.yuanma.module.utils.PageUtil;
import com.yuanma.module.utils.SystemApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping(Constants.API + "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @ApiOperation("用户列表")
    @GetMapping
    @YuanmaLog(value = "用户列表",moudle = "用户管理")
    public SystemApiResult query(UserQuery userQuery) {
        UserQueryDto userDto = new UserQueryDto();
        BeanUtils.copyProperties(userQuery, userDto);
        if (!ObjectUtils.isEmpty(userDto.getDeptId())) {
            userDto.getDeptIds().add(userDto.getDeptId());
            userDto.getDeptIds().addAll(deptService.getDeptChildren(userDto.getDeptId(),
                    deptService.findByPid(userDto.getDeptId())));
            userDto.setDeptId(null);
        }
        Page page = new Page(userQuery.getPage(), userQuery.getSize());
        PageUtil.addOrder(userQuery.getSort(), "createTime,desc", page, UserEntity.class);
        return new SystemApiResult(PageUtil.toPage(userService.queryAll(userDto, page)));
    }


    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    @ApiOperation("用户详细信息")
    @GetMapping("/{userId}")
    @YuanmaLog(value = "用户详细信息",moudle = "用户管理")
    public SystemApiResult query(@PathVariable("userId") Long userId) {
        UserDto userDto = userService.findById(userId);
        return new SystemApiResult(userDto);
    }

    @GetMapping("/basic")
    @YuanmaLog(value = "用户基础信息",moudle = "用户管理")
    public SystemApiResult queryAll(@RequestParam("ids") List<Long> userIds) {
        if (null == userIds || userIds.isEmpty()) {
            return new SystemApiResult(new ArrayList<>());
        }
        UserQueryDto criteria = new UserQueryDto();
        criteria.setUserIds(userIds);
        List<UserSmallDto> userSmallDtos = userService.queryAllByUserId(criteria);
        return new SystemApiResult(userSmallDtos);
    }


    @ApiOperation("按用户名取用户信息")
    @GetMapping("/sname/")
    @YuanmaLog(value = "按用户名取用户信息",moudle = "用户管理")
    public SystemApiResult query(@RequestParam(value = "username") String username) {
        UserDto userDto = userService.findByName(username);
        return new SystemApiResult(userDto);
    }

    // 新增用户
    @ApiOperation("新增用户")
    @PostMapping
    @YuanmaLog(value = "新增用户",moudle = "用户管理")
    public SystemApiResult create(HttpServletRequest req, @RequestBody UserDto user) {
        checkLevel(req, user);
        userService.checkCreateUser(user);
        //   String passwd = SecurityUtils.encryptToHashString(com.yuanma.module.system.Constants.DEFAULT_PASSWORD, user.getUsername());
        String passwd = acquirePassword(user,com.yuanma.module.system.Constants.DEFAULT_PASSWORD);
        // 设定默认权限
        user.setPassword(passwd);
        Long userId = userService.create(user);
        return new SystemApiResult(userId);
    }

    //修改用户
    @ApiOperation("修改用户")
    @PutMapping
    @YuanmaLog(value = "修改用户",moudle = "用户管理",type = LogActionType.UPDATE)
    public SystemApiResult update(HttpServletRequest req, @RequestBody UserDto resources) {
        checkLevel(req, resources);
        userService.checkUpdateUser(resources);
        userService.update(resources);
        return new SystemApiResult(resources.getId());
    }


    // 删除用户
    @ApiOperation("删除用户")
    @DeleteMapping
    @YuanmaLog(value = "修改用户",moudle = "用户管理",type = LogActionType.DELETE)
    public SystemApiResult delete(HttpServletRequest req, @RequestBody Set<Long> ids) {
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        UserDto user = userService.findById(userId);
        for (Long id : ids) {
            List<RoleSmallDto> roleSmallDtos = roleService.findByUsersId(id);
            if (null == roleSmallDtos || roleSmallDtos.isEmpty()) {
                throw new BadRequestException("当前用户不存在无法删除");
            }
            Integer currentLevel = Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleSmallDtos.stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.findById(id).getUsername());
            }
        }
        userService.delete(ids);
        return new SystemApiResult(ids);
    }

    @ApiOperation("更新密码")
    @PostMapping(value = "/updatePass")
    @YuanmaLog(value = "更新密码",moudle = "用户管理",type = LogActionType.UPDATE)
    public SystemApiResult updatePass(HttpServletRequest req, UserPassQuery passVo) throws Exception {
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        UserDto user = userService.findById(userId);
        //PBKDF2WithHmacSHA256PasswordEncoder encode = new PBKDF2WithHmacSHA256PasswordEncoder();
        //String oldPass =  SecurityUtils.encryptToHashString(passVo.getOldPass(),user.getUsername());
        //String oldPass = passwordEncoder.encode(passVo.getOldPass());
        String oldPass = acquirePassword(user,passVo.getOldPass());
        //   String newPass = SecurityUtils.encryptToHashString(passVo.getNewPass(),user.getUsername());
        //String newPass = passwordEncoder.encode(passVo.getNewPass());
        if (!_passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        user.setSalt(null);
        String newPass = acquirePassword(user,passVo.getNewPass());
        if (_passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(user.getUsername(), newPass,user.getSalt());
        return new SystemApiResult(user.getId());
    }

    @ApiOperation("重置密码")
    @PostMapping(value = "/resetPass")
    @YuanmaLog(value = "重置密码",moudle = "用户管理",type = LogActionType.UPDATE)
    public SystemApiResult resetPass(HttpServletRequest req, UserPassQuery passVo) throws Exception {
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        UserDto user = userService.findById(userId);
        user.setSalt(null);
        String passwd = acquirePassword(user,com.yuanma.module.system.Constants.DEFAULT_PASSWORD);
        //String passwd = passwordEncoder.encode(com.yuanma.module.system.Constants.DEFAULT_PASSWORD);
        userService.updatePass(user.getUsername(), passwd,user.getSalt());
        return new SystemApiResult(user.getId());
    }

    /*@ApiOperation("更新邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public SystemApiResult updateEmail(HttpServletRequest req, @PathVariable("code") String code, @RequestBody UserDto user) throws Exception {
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        //PBKDF2WithHmacSHA256PasswordEncoder encode = new PBKDF2WithHmacSHA256PasswordEncoder();
        UserDto userDto = userService.findById(userId);
        //   String password = SecurityUtils.encryptToHashString(user.getPassword(),userDto.getUsername());
        String password = passwordEncoder.encode(user.getPassword());
        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        userService.updateEmail(userDto.getUsername(), user.getEmail());
        return new SystemApiResult();
    }*/


    private void checkLevel(HttpServletRequest req, UserDto resources) {
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        UserDto user = userService.findById(userId);
        Integer currentLevel = Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    /**
     * 取得当前用户的菜单信息
     */
    @ApiOperation("取得当前用户的菜单信息")
    @GetMapping("/menus")
    @YuanmaLog(value = "取得当前用户的菜单信息",moudle = "用户管理")
    public SystemApiResult findMenusByUser(HttpServletRequest req) {
        UserDataScopeVO userDataScop = new UserDataScopeVO();
        Long userId = Long.valueOf(req.getAttribute("uid").toString());
        Map<Long, MenuDto> menuDtoMap = new HashMap<>();
        List<RoleSmallDto> roleSmallDtos = userService.findById(userId).getRoles();
        for (RoleSmallDto role : roleSmallDtos) {
            List<MenuDto> menus = roleService.findById(role.getId()).getMenus();
            if (null != menus && !menus.isEmpty()) {
                Set<Long> menuIds = menus.stream().map(c -> c.getId()).collect(Collectors.toSet());
                List<MenuDto> menuDtos = menuService.findByIds(menuIds);
                for (MenuDto menu : menuDtos) {
                    menuDtoMap.put(menu.getId(), menu);
                }
            }
        }
        return new SystemApiResult(menuDtoMap.values());
    }

    /**
     * 根据用户查询数据权限信息
     *
     * @param req
     */
    @ApiOperation("取得当前用户的权限信息")
    @GetMapping("/findDataScopeByUser")
    @YuanmaLog(value = "取得当前用户的权限信息",moudle = "用户管理")
    public UserDataScopeVO findDataScopeByUser(HttpServletRequest req, @RequestParam("username") String username) {
        UserDataScopeVO userDataScope = new UserDataScopeVO();
        UserDto userDto = userService.findByName(username);
        userDataScope.setUserId(userDto.getId());
        userDataScope.setUserName(userDto.getUsername());
        userDataScope.setNickName(userDto.getNickName());
        List<Long> deptIds = new ArrayList<>();
        List<RoleSmallDto> roleSmallDtos = userDto.getRoles();
        if (!roleSmallDtos.isEmpty()) {
            long count = roleSmallDtos.stream().filter(c -> DataScopeType.getLevelByName(c.getDataScope()) == 1).count();
            if (count > 0) {
                userDataScope.setDataScope(1);
                return userDataScope;
            }
            // 取得用户角色信息
            for (RoleSmallDto dto : userDto.getRoles()) {
                Integer level = DataScopeType.getLevelByName(dto.getDataScope());
                userDataScope.setDataScope(level);
                if (level > 2) {
                    RoleDto roleDto = roleService.findById(dto.getId());
                    if (!roleDto.getDepts().isEmpty()) {
                        Set<Long> roleDeptIds = roleDto.getDepts().stream().map(c -> c.getId()).collect(Collectors.toSet());
                        List<DeptDto> deptDtos = deptService.findByIds(roleDeptIds);
                        for (DeptDto deptDto : deptDtos) {
                            obtainDeptIds(deptDto, deptIds);
                        }
                    }
                }
            }
        }
        userDataScope.setDepts(deptIds);
        return userDataScope;
    }

    public void obtainDeptIds(DeptDto deptDtos, List<Long> deptIds) {
        if (deptIds.contains(deptDtos.getId())) {
            deptIds.add(deptDtos.getId());
        }
        if (null != deptDtos.getChildren()) {
            for (DeptDto deptDto : deptDtos.getChildren()) {
                obtainDeptIds(deptDto, deptIds);
            }
        }
    }

    private String acquirePassword(UserDto user,String password){
        String passwd = null;
        if(_passwordEncoder instanceof SaltPasswordEncoder){
            SaltPasswordEncoder saltPasswordEncoder = (SaltPasswordEncoder) _passwordEncoder;
            if(StringUtils.isEmpty(user.getSalt())) {
                user.setSalt(saltPasswordEncoder.getRandomSalt());
            }
            passwd = saltPasswordEncoder.encode(password,user.getSalt());
        } else {
            passwd = _passwordEncoder.encode(password);
        }
        return passwd;
    }
    
}
