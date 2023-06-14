package com.yuanma.module.security.config.password;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface SaltPasswordEncoder extends PasswordEncoder {

    String getRandomSalt();

    String encode(CharSequence rawPassword,String salt);

    boolean matches(CharSequence rawPassword,String salt, String encodedPassword);

}
