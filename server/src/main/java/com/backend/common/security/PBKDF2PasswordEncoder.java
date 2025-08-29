package com.backend.common.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.common.PasswordUtils;
import org.springframework.stereotype.Component;

/**
 * Not in use
 */
@Component
public class PBKDF2PasswordEncoder implements PasswordEncoder {

  private final PasswordUtils passwordUtils;

  public PBKDF2PasswordEncoder(PasswordUtils passwordUtils) {
    this.passwordUtils = passwordUtils;
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return passwordUtils.hashPassword(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return passwordUtils.verifyPassword(rawPassword.toString(), encodedPassword);
  }
}
