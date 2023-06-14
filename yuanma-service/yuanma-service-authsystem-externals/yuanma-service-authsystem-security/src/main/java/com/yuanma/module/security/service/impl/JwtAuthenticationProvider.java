package com.yuanma.module.security.service.impl;

import com.yuanma.exception.ExceptionMessage;
import com.yuanma.exception.LoginException;
import com.yuanma.module.security.config.bean.LoginProperties;
import com.yuanma.module.security.config.password.SaltPasswordEncoder;
import com.yuanma.module.security.mode.dto.JwtUserDto;
import com.yuanma.module.security.service.LoginLogService;
import com.yuanma.module.security.token.UsernameNoPasswordAuthenticationToken;
import com.yuanma.module.system.service.UserCommonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

@Component
public class JwtAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LoginLogService loginLogService;
    private final LoginProperties loginProperties;

    @Autowired(required = false)
    private UserCommonService userService;

    public JwtAuthenticationProvider(UserDetailsService userDetailsService
            , PasswordEncoder passwordEncoder
            ,LoginLogService loginLogService
            ,LoginProperties loginProperties ) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.loginLogService = loginLogService;
        this.loginProperties = loginProperties;
        setUserDetailsService(this.userDetailsService);
        setPasswordEncoder(this.passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        try{
            Authentication auth = super.authenticate(authentication);
            if(loginProperties.isLockEnable()) {
                loginLogService.save(authentication.getName(), LoginLogService.SUCCESS);
                loginLogService.updateStatusEqualOneByUserName(authentication.getName());
            }
            // 登录成功日志
            return auth;
        } catch (BadCredentialsException e) {
            // 密码错误
            if(loginProperties.isLockEnable()) {
                Integer count = loginLogService.countStatusEqualOneByUserName(authentication.getName());
                String leftNum = String.valueOf(loginProperties.getLockLoginFailureNum() - count - 1);
                loginLogService.save(authentication.getName(),LoginLogService.FAILURE);
                if (StringUtils.equals(leftNum, "0")) {
                    userService.lock(authentication.getName());
                    throw new LoginException(ExceptionMessage.ERR_E90005,
                            new String[]{
                                    loginProperties.getLockLoginFailureNum().toString()
                                    , loginProperties.getAutoUnLockMinute().toString()});
                } else {
                    throw new LoginException(ExceptionMessage.ERR_E90001, new String[]{leftNum});
                }
            }
            throw  e;
        } catch (LockedException e) {
            // 锁定时判断，还要多少时间解锁
            if(loginProperties.isLockEnable()) {
                long diff = new Date().getTime() - userService.getLockDateByUserName(authentication.getName()).getTime();
                String min = String.valueOf(loginProperties.getAutoUnLockMinute() - diff % (1000 * 24 * 60 * 60) % (1000 * 60 * 60) / (1000 * 60));
                throw new LoginException(ExceptionMessage.ERR_E90005
                        , new String[]{loginProperties.getLockLoginFailureNum().toString(), min});

            }
            throw  e;
        } catch (Exception e) {
            if(loginProperties.isLockEnable()) {
                loginLogService.save(authentication.getName(), LoginLogService.FAILURE);
            }
            throw  e;
        }
    }


    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
        ReloadableResourceBundleMessageSource localMessageSource = new ReloadableResourceBundleMessageSource();
        localMessageSource.setBasenames("classpath:org/springframework/security/messages");
        messages = new MessageSourceAccessor(localMessageSource);
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if(authentication instanceof UsernameNoPasswordAuthenticationToken){
            return;
        }
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();
        boolean result = true;
        if(passwordEncoder instanceof SaltPasswordEncoder){
            JwtUserDto jwtUserDto = (JwtUserDto) userDetails;
            String salt = jwtUserDto.getUser().getSalt();
            result = ((SaltPasswordEncoder)passwordEncoder).matches(presentedPassword,salt, userDetails.getPassword());
        } else {
            result = passwordEncoder.matches(presentedPassword, userDetails.getPassword());
        }
        if (!result) {
            logger.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

}
