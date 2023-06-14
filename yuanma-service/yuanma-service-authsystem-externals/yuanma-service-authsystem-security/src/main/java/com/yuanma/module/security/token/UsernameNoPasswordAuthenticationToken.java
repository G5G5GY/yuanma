package com.yuanma.module.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UsernameNoPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public UsernameNoPasswordAuthenticationToken(Object principal) {
        super(principal, null);
    }


}
