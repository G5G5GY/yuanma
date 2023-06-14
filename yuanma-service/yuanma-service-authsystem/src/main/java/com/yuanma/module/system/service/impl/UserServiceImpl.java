package com.yuanma.module.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.security.config.password.SaltPasswordEncoder;
import com.yuanma.module.system.aspect.AuthPowerActionType;
import com.yuanma.module.system.aspect.AuthSystemProperties;
import com.yuanma.module.system.aspect.EncryptionTemplate;
import com.yuanma.module.system.aspect.YuanmaAuthPower;
import com.yuanma.module.system.encrption.SignValidate;
import com.yuanma.module.system.entity.*;
import com.yuanma.module.system.exception.BadRequestException;
import com.yuanma.module.system.mapper.*;
import com.yuanma.module.system.model.dto.*;

import com.yuanma.module.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("authUserService")
//@Transactional
@Slf4j
@DS("auth")
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthSystemProperties properties ;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAndRoleRelMapper userAndRoleRelMapper;

    @Autowired
    private UserAndJobRelMapper userAndJobRelMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private EncryptionTemplate encryptionTemplate ;

    @Autowired
    private SignValidate signValidate;

    @Override
    public List<UserDto> queryAll(UserQueryDto userQueryDto) {
        userQueryDto.setPowerEnable(properties.isPowerEnable());
        List<UserDto> userDtos = userMapper.findByCond(userQueryDto);

        for(UserDto userDto:userDtos){
            userDto.setPhone(encryptionTemplate.decode(userDto.getPhone()));
            userDto.setEmail(encryptionTemplate.decode(userDto.getEmail()));
        }

        return userDtos;
    }

    @Override
    public Page<UserDto> queryAll(UserQueryDto userQueryDto, Page page) {
        userQueryDto.setPowerEnable(properties.isPowerEnable());
        Page<UserDto> userDtoPage = userMapper.queryAll(userQueryDto, page);
        for(UserDto userDto:userDtoPage.getRecords()){

            userDto.setPhone(encryptionTemplate.decode(userDto.getPhone()));
            userDto.setEmail(encryptionTemplate.decode(userDto.getEmail()));

            userDto.setRoles(roleMapper.findByUserId(userDto.getId()));
            userDto.setJobs(positionMapper.findByUserId(userDto.getId()));
            userDto.setDept(deptMapper.findById(userDto.getDeptId()));
        }
        return userDtoPage;
    }

    @Override
    public List<UserSmallDto> queryAllByUserId(UserQueryDto userQueryDto) {
        userQueryDto.setPowerEnable(properties.isPowerEnable());
        List<UserDto> byCond = userMapper.findByCond(userQueryDto);
        return  byCond.stream().map(c -> new UserSmallDto(c.getId(),c.getUsername())).collect(Collectors.toList());
    }


    @Override
    public UserDto findById(Long userId) {
        log.info("根据用户id={}查询用户信息",userId);
        UserDto userDto = userMapper.findById(userId);
        log.info("根据用户id={}查询到{}的用户信息",userDto.getUsername());
        userDto.setPhone(encryptionTemplate.decode(userDto.getPhone()));
        userDto.setEmail(encryptionTemplate.decode(userDto.getEmail()));

        userDto.setRoles(roleMapper.findByUserId(userId));
        return userDto;
    }


    public void checkCreateUser(UserDto userDto){
        if (userMapper.findByUsername(userDto.getUsername()) != null) {
            throw new BadRequestException("所选用户名已经存在");
        }
        if (userMapper.findByEmail(userDto.getEmail()) != null) {
            throw new BadRequestException("所选邮箱已经存在");
        }
    }

    @Override
    @YuanmaAuthPower(title = "新增用户信息",module = "用户管理",type = AuthPowerActionType.ADD)
    @Transactional(rollbackFor = Exception.class)
    public Long create(UserDto userDto) {
        /*if (userMapper.findByUsername(userDto.getUsername()) != null) {
            throw new BadRequestException("所选用户名已经存在");
        }
        if (userMapper.findByEmail(userDto.getEmail()) != null) {
            throw new BadRequestException("所选邮箱已经存在");
        }*/
        UserEntity userEntity = new UserEntity();

        BeanUtils.copyProperties(userDto,userEntity);
        if(null == userEntity.getDeptId() && null != userDto.getDept()){
            userEntity.setDeptId(userDto.getDept().getId());
        }

        userEntity.setPhone(encryptionTemplate.encode(userEntity.getPhone()));
        userEntity.setEmail(encryptionTemplate.encode(userEntity.getEmail()));
        userEntity.setUnlocked(true);
        userMapper.insert(userEntity);
        updateUserRel(userDto,userEntity.getId());
        // 执行签名
        performSign(userEntity.getId());

        return userEntity.getId();
    }

    private void performSign(Long userId){
        // 开启签名功能
        if(properties.isSignEnable()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
            UserEntity user =  userMapper.selectById(userId);
            String signData = acquireSignData(user);
            String sign = signValidate.sign(signData);
            userMapper.updateSignById(sign,userId);
        }
    }

    // 获取签名数据
    private String acquireSignData(UserEntity user ){
        StringBuffer signDataBuffer = new StringBuffer();
        // 用户名
        signDataBuffer.append(user.getUsername());
        signDataBuffer.append("|");
        // 手机
        signDataBuffer.append(user.getPhone());
        signDataBuffer.append("|");
        // 邮箱
        signDataBuffer.append(user.getEmail());
        signDataBuffer.append("|");
        // 密码
        signDataBuffer.append(user.getPassword());
        // 角色信息
        List<UserAndRoleRelEntity> userAndRoleRelEntities = userAndRoleRelMapper
                .selectList(new LambdaQueryWrapper<UserAndRoleRelEntity>()
                        .eq(UserAndRoleRelEntity::getUserId, user.getId())
                        .orderByDesc(UserAndRoleRelEntity::getRoleId));
        for(UserAndRoleRelEntity userAndRoleRelEntitie:userAndRoleRelEntities){
            signDataBuffer.append("|");
            signDataBuffer.append(userAndRoleRelEntitie.getRoleId());
        }
        return signDataBuffer.toString();
    }

    public void checkUpdateUser(UserDto resources){
        UserEntity user = userMapper.selectById(resources.getId());
        UserDto user1 = userMapper.findByUsername(resources.getUsername());
        UserDto user2 = userMapper.findByEmail(resources.getEmail());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("所选用户名已经存在");
        }

        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new BadRequestException("所选邮箱已经存在");
        }
    }

    @Override
    @YuanmaAuthPower(title = "更新用户信息",module = "用户管理",type = AuthPowerActionType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDto resources) {

        UserEntity user = userMapper.selectById(resources.getId());
        /*UserDto user1 = userMapper.findByUsername(resources.getUsername());
        UserDto user2 = userMapper.findByEmail(resources.getEmail());

        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("所选用户名已经存在");
        }

        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new BadRequestException("所选邮箱已经存在");
        }*/

        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setDeptId(resources.getDeptId());

        if(null == user.getDeptId() && null != resources.getDept()){
            user.setDeptId(resources.getDept().getId());
        }
        //user.setRoles(resources.getRoles());
        //user.setDept(resources.getDept());
        //user.setPositions(resources.getPositions());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());

        user.setPhone(encryptionTemplate.encode(user.getPhone()));
        user.setEmail(encryptionTemplate.encode(user.getEmail()));


        userMapper.updateById(user);
        updateUserRel(resources,user.getId());

        // 执行签名
        performSign(user.getId());
    }

    public void lock(String username) {
        userMapper.lock(username,new Date());
    }

    public void unlock(String username) {
        userMapper.unlock(username);
    }

    public Date getLockDateByUserName(String username){
        return userMapper.getLockDateByUserName(username);
    }

    private void updateUserRel(UserDto userDto,Long userId){
        userAndRoleRelMapper.delete(new LambdaQueryWrapper<UserAndRoleRelEntity>().eq(UserAndRoleRelEntity::getUserId, userId));
        List<RoleSmallDto> roles = userDto.getRoles();
        List<UserAndRoleRelEntity> userAndRoleRelEntities = roles.stream().map(c -> new UserAndRoleRelEntity(userId, c.getId())).collect(Collectors.toList());
        for(UserAndRoleRelEntity UserAndRoleRelEntity:userAndRoleRelEntities)  {
            userAndRoleRelMapper.insert(UserAndRoleRelEntity);
        }
        userAndJobRelMapper.delete(new LambdaQueryWrapper<UserAndJobRelEntity>().eq(UserAndJobRelEntity::getUserId,userId));
        List<PositionDto> jobs = userDto.getJobs();
        List<UserAndJobRelEntity> userAndJobRelEntities = null;
        if(null != jobs){
            userAndJobRelEntities = jobs.stream().map(c -> new UserAndJobRelEntity(userId, c.getId())).collect(Collectors.toList());
        } else {
            userAndJobRelEntities = userDto.getPositions().stream().map(c -> new UserAndJobRelEntity(userId, c.getId())).collect(Collectors.toList());
        }

        for(UserAndJobRelEntity userAndRoleRelEntity:userAndJobRelEntities)  {
            userAndJobRelMapper.insert(userAndRoleRelEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(UserDto resources) {
        UserEntity user = userMapper.selectById(resources.getId());
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        userMapper.updateById(user);

        // 执行签名
        performSign(user.getId());

    }

    @Override
    @YuanmaAuthPower(title = "删除用户信息",module = "用户管理",type = AuthPowerActionType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        userAndJobRelMapper.delete(new LambdaQueryWrapper<UserAndJobRelEntity>().in(UserAndJobRelEntity::getUserId,ids));
        userAndRoleRelMapper.delete(new LambdaQueryWrapper<UserAndRoleRelEntity>().in(UserAndRoleRelEntity::getUserId, ids));
        userMapper.deleteBatchIds(ids);
    }

    @Override
    public UserDto findByName(String userName) {
        //log.info("-------------start----UserServiceImpl ->userMapper.findByUsername-----------------");
        UserDto userDto = userMapper.findByUsername(userName);

        //log.info("------------end-----UserServiceImpl ->userMapper.findByUsername-----------------");
        if(null != userDto) {
            userDto.setPhone(encryptionTemplate.decode(userDto.getPhone()));
            userDto.setEmail(encryptionTemplate.decode(userDto.getEmail()));
            //log.info("-------------start----UserServiceImpl ->roleMapper.findByUserId-----------------");
            userDto.setRoles(roleMapper.findByUserId(userDto.getId()));
            //log.info("-------------end----UserServiceImpl ->roleMapper.findByUserId-----------------");
            //log.info("-------------start----UserServiceImpl ->deptMapper.findById-----------------");
            userDto.setDept(deptMapper.findById(userDto.getDeptId()));
            //log.info("-------------end----UserServiceImpl ->deptMapper.findById-----------------");
        }
        return userDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePass(String userName, String passwd,String salt) {
        Integer rownum =  userMapper.updatePass(userName, passwd,salt, new Date());
        // 执行签名
        UserDto byUsername = userMapper.findByUsername(userName);
        performSign(byUsername.getId());
        return rownum;
    }

    public int updatePass(String userName, String passwd) {
        Integer rownum =  updatePass(userName, passwd,null);
        return rownum;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateEmail(String userName, String email) {
        Integer rownum =  userMapper.updateEmail(userName, email);
        // 执行签名
        UserDto byUsername = userMapper.findByUsername(userName);
        performSign(byUsername.getId());
        return rownum;
    }

    public void validateSign(Long userId){
        if(properties.isSignEnable()){
            UserEntity userEntity = userMapper.selectById(userId);
            String signData = acquireSignData(userEntity);
            signValidate.validate(signData,userEntity.getSign());
        }
    }





}
