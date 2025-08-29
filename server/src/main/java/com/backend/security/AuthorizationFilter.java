package com.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.common.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/// * AuthorizeFilter is a filter that intercepts HTTP requests to check for JWT tokens.
/*
 * It extracts the token from the request, verifies it, and processes the JWT data. This filter is
 * executed once per request.
 */
@Component
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthorizationFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtUtils.resolveToken(request);
        if (token != null) {
            try {
                Authentication authRequest = new JwtAuthenticationToken(token);
                Authentication authResult = authenticationManager.authenticate(authRequest);
                SecurityContextHolder.getContext().setAuthentication(authResult);
            } catch (AuthenticationException ex) {
                SecurityContextHolder.clearContext();
                log.warn("JWT authentication failed: {}", ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
