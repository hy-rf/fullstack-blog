package com.backend.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {

  // Generate a JWT token with userId and roleIds
  public String generateToken(
    AccessTokenData jwtUserData,
    String secretKey,
    Integer expirationDays
  ) {
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
    return Jwts.builder()
      .claim("id", jwtUserData.getId())
      .claim("roleNames", jwtUserData.getRoleNames())
      .claim("username", jwtUserData.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(
        new Date(
          System.currentTimeMillis() + expirationDays * 24L * 60L * 60L * 1000L
        )
      )
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public AccessTokenData verifyToken(String token, String secretKey) {
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
    JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    Jws<Claims> jws;
    jws = jwtParser.parseClaimsJws(token);

    Claims claims = jws.getBody();

    Object userIdObj = claims.get("id");
    if (!(userIdObj instanceof Number)) {
      return null;
    }
    Integer userId = ((Number) userIdObj).intValue();

    Object usernameObj = claims.get("username");
    if (!(usernameObj instanceof String)) {
      return null;
    }
    String userName = ((String) usernameObj).toString();

    Object roleNamesObj = claims.get("roleNames");
    if (!(roleNamesObj instanceof List)) {
      return null;
    }
    List<String> roleNames = ((List<?>) roleNamesObj).stream()
      .filter(Objects::nonNull)
      .map(Object::toString)
      .toList();

    return new AccessTokenData(userId, userName, roleNames);
  }

  public String resolveToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String resolveRefreshToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refresh".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    String bearerToken = request.getHeader("X-Refresh-Token");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
