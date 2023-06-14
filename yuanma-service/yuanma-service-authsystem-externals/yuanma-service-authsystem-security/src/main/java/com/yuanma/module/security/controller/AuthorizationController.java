package com.yuanma.module.security.controller;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.yuanma.exception.BadRequestException;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.security.annotation.AnonymousDeleteMapping;
import com.yuanma.module.security.annotation.AnonymousGetMapping;
import com.yuanma.module.security.annotation.AnonymousPostMapping;
import com.yuanma.module.security.config.bean.LoginProperties;
import com.yuanma.module.security.config.bean.SecurityProperties;
import com.yuanma.module.security.handler.TokenProvider;
import com.yuanma.module.security.mode.dto.AuthUserDto;
import com.yuanma.module.security.mode.dto.JwtUserDto;
import com.yuanma.module.security.service.AuthorizationService;
import com.yuanma.module.security.service.OnlineUserService;
import com.yuanma.module.security.service.impl.TokenAuthorizationProvider;
import com.yuanma.module.security.token.UsernameNoPasswordAuthenticationToken;
import com.yuanma.module.security.utils.SecurityUtils;
import com.yuanma.webmvc.util.RedisUtils;
import com.yuanma.webmvc.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "系统授权接口")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {



    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Resource
    private LoginProperties loginProperties;



    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    @YuanmaLog(value="登录",moudle = "用户登录" ,type=LogActionType.ADD)
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        if(loginProperties.isLoginCodeEnable()) {
            // 查询验证码
            String code = (String) redisUtils.get(authUser.getUuid());
            // 清除验证码
            redisUtils.del(authUser.getUuid());
            if (StringUtils.isBlank(code)) {
                throw new BadRequestException("验证码不存在或已过期");
            }
            if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
                throw new BadRequestException("验证码错误");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);

        jwtUserDto.getUser().setPassword(null);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return ResponseEntity.ok(authInfo);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    @YuanmaLog(value="获取用户信息",moudle = "用户登录" )
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
    }

    @ApiOperation("获取验证码")
    @AnonymousGetMapping(value = "/code")
    @YuanmaLog(value="获取验证码",moudle = "用户登录" )
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        // 保存
        redisUtils.set(uuid, captcha.text(), loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        log.info("获取验证码成功");
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    @YuanmaLog(value="退出登录",moudle = "用户登录" ,type=LogActionType.ADD)
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
