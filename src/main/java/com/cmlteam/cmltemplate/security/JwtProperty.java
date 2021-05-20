package com.cmlteam.cmltemplate.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Data
@ConditionalOnProperty("jwt.enabled")
class JwtProperty {
  private final String jwtSecret;
  private final Long jwtExpirationInMs;

  public JwtProperty(
      @Value("${jwt.secret}") String jwtSecret,
      @Value("${jwt.expirationInMs}") Long jwtExpirationInMs) {
    this.jwtSecret = jwtSecret;
    this.jwtExpirationInMs = jwtExpirationInMs;
  }
}
