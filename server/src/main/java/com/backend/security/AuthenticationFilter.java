package com.backend.security;

import com.backend.common.JwtData;
import com.backend.common.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/// * AuthorizeFilter is a filter that intercepts HTTP requests to check for JWT tokens.
/*
 * It extracts the token from the request, verifies it, and processes the JWT data. This filter is
 * executed once per request.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private final JwtUtils jwtUtils;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String token = jwtUtils.resolveToken(request);
    if (token != null) {
      try {
        JwtData jwtData = jwtUtils.verifyToken(token, jwtSecret);
        CustomUserDetails userDetails = new CustomUserDetails(jwtData);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authRequest);
      } catch (AuthenticationException ex) {
        SecurityContextHolder.clearContext();
        log.warn("JWT authentication failed: {}", ex.getMessage());
      }
    } else {
      log.info("No token provided");
    }
    filterChain.doFilter(request, response);
  }
}
