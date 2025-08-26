package com.backend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String token;

    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
        setAuthenticated(false); // Not yet authenticated
    }

    public String getToken() {
        return token;
    }
}
