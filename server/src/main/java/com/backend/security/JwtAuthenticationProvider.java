package com.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.backend.common.JwtData;
import com.backend.common.JwtUtils;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final String jwtSecret;

    public JwtAuthenticationProvider(JwtUtils jwtUtils,
            CustomUserDetailsService customUserDetailsService,
            @Value("${jwt.secret}") String jwtSecret) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JwtAuthenticationToken) authentication).getToken();

        if (jwtUtils.verifyToken(token, jwtSecret) == null) {
            throw new BadCredentialsException("Invalid JWT token");
        }

        JwtData jwtData = jwtUtils.verifyToken(token, jwtSecret);
        UserDetails userDetails = customUserDetailsService.loadUserFromToken(jwtData);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
