package com.yuanma.module.security.config.password;

import com.yuanma.module.security.utils.PBKDF2WithHmacSHA256Utils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description:
 *
 * @author xth
 * @date 2022/5/18
 */
public class PBKDF2WithHmacSHA256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return PBKDF2WithHmacSHA256Utils.encode(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return PBKDF2WithHmacSHA256Utils.checkPassword(charSequence.toString(), s);
    }
}
