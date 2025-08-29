package com.backend.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    // Generate a JWT token with userId and roleIds
    public String generateToken(Long userId, List<String> roleNames, String secretKey,
            long expirationDays, String name) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder().claim("userId", userId).claim("roleNames", roleNames)
                .claim("username", name).setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + expirationDays * 24L * 60L * 60L * 1000L))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public JwtData verifyToken(String token, String secretKey) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            Claims claims = jws.getBody();
            Long userId = null;
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Number) {
                userId = ((Number) userIdObj).longValue();
            }

            // be permissive about username type to avoid RequiredTypeException
            Object usernameObj = claims.get("username");
            String userName = usernameObj instanceof String ? (String) usernameObj : "";

            Object roleIdsObj = claims.get("roleIds");
            List<Long> roleIds = Collections.emptyList();
            if (roleIdsObj instanceof List) {
                roleIds = ((List<?>) roleIdsObj).stream().filter(Objects::nonNull).map(val -> {
                    if (val instanceof Number) {
                        return ((Number) val).longValue();
                    } else {
                        try {
                            return Long.parseLong(val.toString());
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
            }

            Object roleNamesObj = claims.get("roleNames");
            List<String> roleNames = Collections.emptyList();
            if (roleNamesObj instanceof List) {
                roleNames = ((List<?>) roleNamesObj).stream().filter(Objects::nonNull)
                        .map(Object::toString).collect(Collectors.toList());
            }

            return new JwtData(userId, userName, roleIds, roleNames);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }

    }

    public String resolveToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
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
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
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
