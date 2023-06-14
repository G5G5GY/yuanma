
package com.yuanma.module.security.config.bean;


import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.yuanma.exception.BadConfigurationException;
import lombok.Data;

import java.util.Objects;

/**
 * 配置文件读取
 */
@Data
public class LoginProperties {

    private boolean singleLogin = false;

    private LoginCode loginCode;

    private boolean loginCodeEnable = false;
    /**
     * 用户登录信息缓存
     */
    private boolean cacheEnable=false;

    /**锁定标志**/
    private boolean lockEnable = false;

    /**密码输入错误3次锁定**/
    private Integer lockLoginFailureNum = 3;

    /**自动解锁时间，默认为分钟**/
    private Integer autoUnLockMinute = 30;

    /*public boolean isSingleLogin() {
        return singleLogin;
    }

    public void setSingleLogin(boolean singleLogin) {
        this.singleLogin = singleLogin;
    }

    public LoginCode getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(LoginCode loginCode) {
        this.loginCode = loginCode;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }*/



    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(loginCode)) {
            loginCode = new LoginCode();
            if (Objects.isNull(loginCode.getCodeType())) {
                loginCode.setCodeType(LoginCodeEnum.arithmetic);
            }
        }
        return switchCaptcha(loginCode);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCode 验证码配置信息
     * @return /
     */
    private Captcha switchCaptcha(LoginCode loginCode) {
        Captcha captcha;
        synchronized (this) {
            switch (loginCode.getCodeType()) {
                case arithmetic:
                    // 算术类型
                    captcha = new ArithmeticCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    // 几位数运算，默认是两位
                    captcha.setLen(loginCode.getLength());
                    break;
                case chinese:
                    captcha = new ChineseCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case chinese_gif:
                    captcha = new ChineseGifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case gif:
                    captcha = new GifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case spec:
                    captcha = new SpecCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                default:
                    throw new BadConfigurationException("验证码配置信息错误！！！正确配置查看 me.zhengjie.modules.security.config.bean.LoginCodeEnum ");
            }
        }
        return captcha;
    }
}
