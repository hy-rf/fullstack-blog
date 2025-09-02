package com.backend.helper;

import jakarta.servlet.http.Cookie;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CookieHelper {

  private final Environment environment;

  public CookieHelper(Environment environment) {
    this.environment = environment;
  }

  public Cookie createCookie(String name, String value, int maxAgeInDays) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException(
        "Cookie name must not be null or empty."
      );
    }
    if (maxAgeInDays < 0) {
      throw new IllegalArgumentException("Cookie maxAge must not be negative.");
    }
    Cookie cookie = new Cookie(name, value);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(maxAgeInDays * 24 * 60 * 60);
    if (isProduction()) {
      cookie.setSecure(true);
      cookie.setAttribute("SameSite", "None");
    } else {
      cookie.setSecure(false);
      cookie.setAttribute("SameSite", "Lax");
    }
    return cookie;
  }

  private boolean isProduction() {
    for (String profile : environment.getActiveProfiles()) {
      if (profile.equalsIgnoreCase("prod")) {
        return true;
      }
    }
    return false;
  }
}
