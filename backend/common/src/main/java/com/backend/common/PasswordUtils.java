package com.backend.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

  private final BCryptPasswordEncoder bCryptPasswordEncoder =
    new BCryptPasswordEncoder(5);

  public String hashPassword(String password) {
    return bCryptPasswordEncoder.encode(password);
  }

  public boolean verifyPassword(String password, String stored) {
    return bCryptPasswordEncoder.matches(password, stored);
  }
}
